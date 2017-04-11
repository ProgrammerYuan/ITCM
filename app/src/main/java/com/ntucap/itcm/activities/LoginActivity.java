package com.ntucap.itcm.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ntucap.itcm.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tv_login_btn, tv_signup_btn;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tv_login_btn = (TextView) findViewById(R.id.tv_login_act_login);
        tv_signup_btn = (TextView) findViewById(R.id.tv_signup_act_login);
        iv_back = (ImageView) findViewById(R.id.iv_back_arrow_act_login);
        bindListeners();
    }

    private void bindListeners() {
        tv_login_btn.setOnClickListener(this);
        tv_signup_btn.setOnClickListener(this);
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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
