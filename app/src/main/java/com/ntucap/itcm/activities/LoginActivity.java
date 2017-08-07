package com.ntucap.itcm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ntucap.itcm.ITCMApplication;
import com.ntucap.itcm.R;
import com.ntucap.itcm.classes.ITCMUser;
import com.ntucap.itcm.db.ITCMDB;
import com.ntucap.itcm.utils.NetUtil;
import com.ntucap.itcm.utils.ValidationUtil;

public class LoginActivity extends ITCMActivity implements View.OnClickListener{

    private static final String LOG_TAG = "LoginActivity!";

    private TextView tv_login_btn, tv_signup_btn;
    private EditText et_email_input, et_password_input;
    private ImageView iv_back;
    private int inputCount = 0;

    private static final int INPUT_COUNT_MAX = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tv_login_btn = (TextView) findViewById(R.id.tv_login_act_login);
        tv_signup_btn = (TextView) findViewById(R.id.tv_signup_act_login);
        et_email_input = (EditText) findViewById(R.id.et_email_input_act_login);
        et_password_input = (EditText) findViewById(R.id.et_pw_input_act_login);
        iv_back = (ImageView) findViewById(R.id.iv_back_arrow_act_login);
        bindListeners();
    }

    private void bindListeners() {
        tv_login_btn.setOnClickListener(this);
        tv_signup_btn.setOnClickListener(this);
        new CountTextWatcher(et_email_input);
        new CountTextWatcher(et_password_input);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent;
        switch (id) {
            case R.id.tv_login_act_login:
                login();
                break;
            case R.id.tv_signup_act_login:
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.iv_back_arrow_act_login:
                intent = new Intent(this, EntranceActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;

        }
    }

    private void login() {
        final String username = et_email_input.getText().toString(),
                password = et_password_input.getText().toString();
        if (!ValidationUtil.validateEmail(username)) {
            toast("Please Input Valid Email Address");
            return;
        }

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
                        user.setIsCurrentUser(true);
                        ITCMDB.saveUser(user);
                        ITCMApplication.setCurrentUser(user);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, new DefaultErrorListener());
            }
        }, new DefaultErrorListener());
    }

    private void setLoginBtnState() {
        if(inputCount >= INPUT_COUNT_MAX) tv_login_btn.setEnabled(true);
        else tv_login_btn.setEnabled(false);
    }

    private class CountTextWatcher implements TextWatcher {

        EditText mEditTextListened;
        int oldLength;
        public CountTextWatcher(EditText editText) {
            this.mEditTextListened = editText;
            mEditTextListened.addTextChangedListener(this);
            oldLength = mEditTextListened.getText().length();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            oldLength = mEditTextListened.getText().length();
            Log.d(LOG_TAG,"oldLength:" + oldLength);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String str = s.toString();
            if(str.length() == 0 && oldLength != 0) inputCount--;
            else if(str.length() != 0 && oldLength == 0) inputCount++;
            setLoginBtnState();
        }

    }
}
