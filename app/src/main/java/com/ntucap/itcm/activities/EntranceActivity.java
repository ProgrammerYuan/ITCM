package com.ntucap.itcm.activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ntucap.itcm.R;

/**
 * Created by ProgrammerYuan on 04/04/17.
 */

public class EntranceActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tv_login_btn, tv_signup_btn;
    private LinearLayout ll_btns;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);
        tv_login_btn = (TextView) findViewById(R.id.tv_login_act_entrance);
        tv_signup_btn = (TextView) findViewById(R.id.tv_signup_act_entrance);
        ll_btns = (LinearLayout) findViewById(R.id.ll_btns_act_entrance);
        ll_btns.setAlpha(0.0f);
        bindListeners();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showButtons();
            }
        }, 2000);
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

    private void showButtons() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(ll_btns, "alpha", 0.0f, 1.0f);
        animator.setDuration(1 * 1000);
        animator.start();
    }

}
