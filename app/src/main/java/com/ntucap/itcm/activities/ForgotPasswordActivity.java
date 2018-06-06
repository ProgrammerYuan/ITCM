package com.ntucap.itcm.activities;

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
import com.ntucap.itcm.R;
import com.ntucap.itcm.utils.NetUtil;
import com.ntucap.itcm.utils.ValidationUtil;

public class ForgotPasswordActivity extends ITCMActivity implements View.OnClickListener {


    private int inputCount = 0;

    TextView mSubmitBtn;
    ImageView mBackBtn;
    EditText mEmailInput;

    private static final int INPUT_COUNT_MAX = 1;
    private static final String LOG_TAG = "Forgot Password Activity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mBackBtn = (ImageView) findViewById(R.id.iv_nav_act_forgot_pw);
        mSubmitBtn = (TextView) findViewById(R.id.tv_submit_act_forgot_pw);
        mEmailInput = (EditText) findViewById(R.id.et_email_input_act_forgot_pw);
        bindListeners();
    }

    public void bindListeners() {
        mSubmitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_submit_act_forgot_pw:
                forgotPassword();
                break;
            case R.id.iv_nav_act_forgot_pw:
                onBackPressed();
                break;
        }
    }

    private void forgotPassword() {
        String email = mEmailInput.getText().toString();
        if(email.length() == 0) {
            toast("Please Input Your Email Address");
        } else if(!ValidationUtil.validateEmail(email)) {
            toast("Please Input Valid Email Address");
        }
        NetUtil.forgotPassword(email, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ForgotPasswordActivity.this.finish();
            }
        }, new DefaultErrorListener());
    }

    private void setLoginBtnState() {
        if(inputCount >= INPUT_COUNT_MAX) mSubmitBtn.setEnabled(true);
        else mSubmitBtn.setEnabled(false);
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
