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
import com.ntucap.itcm.ITCMApplication;
import com.ntucap.itcm.R;
import com.ntucap.itcm.classes.ITCMUser;
import com.ntucap.itcm.db.ITCMDB;
import com.ntucap.itcm.utils.NetUtil;

public class ChangePasswordActivity extends ITCMActivity implements View.OnClickListener{

    private static final String LOG_TAG = "CHANGE_PW_ACTIVITY";

    private int inputCount = 0;
    private ImageView mBackBtn;
    private TextView mTvSubmit;
    private EditText mOldPasswordInput, mNewPasswordInput, mConfirmPasswordInput;

    private static final int INPUT_COUNT_MAX = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        mTvSubmit = (TextView) findViewById(R.id.tv_submit_act_change_pw);
        mBackBtn = (ImageView) findViewById(R.id.iv_nav_act_change_pw);
        mOldPasswordInput = (EditText) findViewById(R.id.et_old_pw_input_act_change_pw);
        mNewPasswordInput = (EditText) findViewById(R.id.et_new_pw_input_act_change_pw);
        mConfirmPasswordInput = (EditText) findViewById(R.id.et_new_pw_confirm_input_act_change_pw);
        bindListeners();
    }

    private void bindListeners() {
        mTvSubmit.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);
        new CountTextWatcher(mOldPasswordInput);
        new CountTextWatcher(mNewPasswordInput);
        new CountTextWatcher(mConfirmPasswordInput);
    }

    private void setSubmitBtnState() {
        mTvSubmit.setEnabled(inputCount >= INPUT_COUNT_MAX);
    }

    private void changePassword() {
        final String oldPassword, newPassword, confirmPassword;

        oldPassword = mOldPasswordInput.getText().toString();
        newPassword = mNewPasswordInput.getText().toString();
        confirmPassword = mConfirmPasswordInput.getText().toString();

        if(!newPassword.equals(confirmPassword)) {
            toast("Please Input Consistent New Password");
            return;
        }

        NetUtil.changePassword(oldPassword, newPassword, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String message = response.getString("body");
                ITCMUser user = ITCMApplication.getCurrentUser();
                user.setPassword(newPassword);
                ITCMDB.saveUser(user);
                user = null;
                toast(message);
                finish();
            }
        }, new DefaultErrorListener());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_submit_act_change_pw:
                changePassword();
                break;
            case R.id.iv_nav_act_change_pw:
                finish();
                break;
        }
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
            setSubmitBtnState();
        }

    }
}
