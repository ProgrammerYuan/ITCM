package com.ntucap.itcm.activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ntucap.itcm.ITCMApplication;
import com.ntucap.itcm.R;
import com.ntucap.itcm.classes.ITCMUser;
import com.ntucap.itcm.classes.ITCMUserPreference;
import com.ntucap.itcm.db.ITCMDB;
import com.ntucap.itcm.utils.NetUtil;
import com.ntucap.itcm.utils.ValidationUtil;

/**
 * Created by ProgrammerYuan on 04/04/17.
 */

public class EntranceActivity extends ITCMActivity implements View.OnClickListener{

    private TextView tv_login_btn, tv_signup_btn;
    private LinearLayout ll_btns;
    private int networkResponseCount = 0;
    private boolean mAutoLogin = true;

    ITCMUser updateUser = null;
    ITCMUserPreference userPreference = null;

    private static final int NETWORK_RESPONSE_COUNT_MAX = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);

        Bundle data = getIntent().getExtras();

        if(data != null)
            mAutoLogin = data.getBoolean("autologin", true);

        tv_login_btn = (TextView) findViewById(R.id.tv_login_act_entrance);
        tv_signup_btn = (TextView) findViewById(R.id.tv_signup_act_entrance);
        ll_btns = (LinearLayout) findViewById(R.id.ll_btns_act_entrance);
        ll_btns.setAlpha(0.0f);
        bindListeners();
        if(mAutoLogin) autologin();
        else showButtons();
    }

    private void bindListeners() {
        tv_login_btn.setOnClickListener(this);
        tv_signup_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent;
        switch (id) {
            case R.id.tv_login_act_entrance:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.tv_signup_act_entrance:
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }

    private void addNetworkResponseCount() {
        networkResponseCount ++;
        if(networkResponseCount == NETWORK_RESPONSE_COUNT_MAX) {
            ITCMDB.saveUser(updateUser);
            ITCMApplication.setCurrentUser(updateUser);
            ITCMDB.saveUserPreference(userPreference);
            Intent intent = new Intent(EntranceActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void showButtons() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(ll_btns, "alpha", 0.0f, 1.0f);
        animator.setDuration(1 * 1000);
        animator.start();
    }

    private void autologin() {
        updateUser = ITCMApplication.getCurrentUser();
        if (updateUser != null) {
            final String username = updateUser.getEmail(), password = updateUser.getPassword();
            NetUtil.login(username, password, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    ITCMApplication.setAccessToken(response.getString("access_token"));
                    ITCMApplication.setTokenExpireDuration(response.getLong("expires_in"));
                    ITCMApplication.setRefreshToken(response.getString("refresh_token"));
                    NetUtil.getUserInfo(new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            response.put("password", password);
                            ITCMUser user = new ITCMUser(response);
                            updateUser.update(user);
                            updateUser.setIsCurrentUser(true);
                            addNetworkResponseCount();
                        }
                    }, new DefaultErrorListener());
                    NetUtil.getUserPreference(new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            userPreference = new ITCMUserPreference(response);
                            addNetworkResponseCount();
                        }
                    }, new DefaultErrorListener());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    showButtons();
                }
            });
        } else {
            showButtons();
        }
    }

}
