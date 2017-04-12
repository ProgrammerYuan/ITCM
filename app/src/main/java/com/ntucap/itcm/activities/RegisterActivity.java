package com.ntucap.itcm.activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dpizarro.uipicker.library.picker.PickerUI;
import com.dpizarro.uipicker.library.picker.PickerUISettings;
import com.ntucap.itcm.R;
import com.ntucap.itcm.utils.ValidationUtility;

import java.util.ArrayList;
import java.util.Arrays;

public class RegisterActivity extends AppCompatActivity
        implements View.OnClickListener, PickerUI.PickerUIItemClickListener
                    ,View.OnTouchListener{

    private static final String LOG_TAG = "RegisterActivity!";

    private TextView tv_signup_btn, tv_age_input, tv_gender_input;
    private EditText et_email_input, et_password_input, et_password_comfirm_input,
            et_firstname_input, et_lastname_input;
    private ImageView iv_back_btn,iv_mask;
    private PickerUI mPicker;
    private ArrayList<String> ages, genders;
    private int pickerSelectedID;
    private int inputCount = 0;
    private String genderFormat, ageFormat;

    private static final int MAX_INPUT_COUNT = 7;
    private static final int ALPHA_ANIM_DURATION = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        tv_signup_btn = (TextView) findViewById(R.id.tv_signup_confirm_act_register);
        et_email_input = (EditText) findViewById(R.id.et_email_input_act_register);
        et_password_input = (EditText) findViewById(R.id.et_pw_input_act_register);
        et_password_comfirm_input = (EditText) findViewById(R.id.et_pw_confirm_input_act_register);
        et_firstname_input = (EditText) findViewById(R.id.et_firstname_input_act_register);
        et_lastname_input = (EditText) findViewById(R.id.et_lastname_input_act_register);
        tv_age_input = (TextView) findViewById(R.id.et_age_input_act_register);
        tv_gender_input = (TextView) findViewById(R.id.et_gender_input_act_register);
        iv_back_btn = (ImageView) findViewById(R.id.iv_back_arrow_act_register);
        iv_mask = (ImageView) findViewById(R.id.iv_mask_act_register);
        mPicker = (PickerUI) findViewById(R.id.picker_ui_act_register);
        ages = new ArrayList<>();
        for(int i = 0;i < 67; i ++)
            ages.add(String.valueOf(i));
        genders = new ArrayList<>(Arrays.asList(new String[]{"Male", "Female"}));
        PickerUISettings pickerUISettings = new PickerUISettings.Builder()
                .withAutoDismiss(false)
                .withItemsClickables(true)
                .withUseBlur(false)
                .build();
        mPicker.setSettings(pickerUISettings);
        bindListeners();
        genderFormat = getString(R.string.str_gender_format);
        ageFormat = getString(R.string.str_age_format);
    }

    private void bindListeners() {
        tv_signup_btn.setOnClickListener(this);
        iv_back_btn.setOnClickListener(this);
        tv_gender_input.setOnClickListener(this);
        tv_age_input.setOnClickListener(this);
        new CountTextWatcher(et_email_input);
        new CountTextWatcher(et_password_input);
        new CountTextWatcher(et_password_comfirm_input);
        new CountTextWatcher(et_firstname_input);
        new CountTextWatcher(et_lastname_input);
        iv_mask.setOnTouchListener(this);
        mPicker.setOnClickItemPickerUIListener(this);
    }

    private boolean signUp() {
        String email = et_email_input.getText().toString();
        if(!ValidationUtility.validate(email)) {

        }
        return true;
    }

    private void controlMask(boolean show, int duration) {
        ObjectAnimator animator;
        if(show) {
            animator = ObjectAnimator.ofFloat(iv_mask, "alpha", 0.0f, 1.0f);
        } else {
            animator = ObjectAnimator.ofFloat(iv_mask, "alpha", 1.0f, 0.0f);
        }
        animator.setDuration(duration);
        animator.start();
    }

    private void controlPicker(boolean show) {
        controlMask(show, ALPHA_ANIM_DURATION);
        mPicker.slide();
    }

    private void setSignupBtnState() {
        if(inputCount == 0) {
            tv_signup_btn.setEnabled(false);
        } else {
            tv_signup_btn.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent;
        switch (id) {
            case R.id.tv_signup_confirm_act_register:
                break;
            case R.id.iv_back_arrow_act_register:
                intent = new Intent(this, EntranceActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.et_gender_input_act_register:
                pickerSelectedID = -1;
                mPicker.setItems(this, genders);
                controlPicker(true);
                break;
            case R.id.et_age_input_act_register:
                pickerSelectedID = -1;
                mPicker.setItems(this,ages);
                controlPicker(true);
            default:
                break;
        }
    }

    @Override
    public void onItemClickPickerUI(int which, int position, String valueResult) {
        Log.e(LOG_TAG, which + " / " + position + " / " + valueResult);
        Log.e(LOG_TAG, "R.id.picker:" + R.id.picker_ui_act_register);
        int age = -1;
        try {
            age = Integer.parseInt(valueResult);
        } catch (NumberFormatException e) {
            Log.e(LOG_TAG, valueResult);
        }
        if(pickerSelectedID == position) {
            if(age == -1) {
                if(tv_gender_input.getText().length() == 0) inputCount ++;
                tv_gender_input.setText(String.format(genderFormat, valueResult));
            } else {
                if(tv_age_input.getText().length() == 0) inputCount ++;
                tv_age_input.setText(String.format(ageFormat, age));
            }
            controlPicker(false);
        } else {
            pickerSelectedID = position;
        }
    }

    @Override
    public void onBackPressed() {
        if(mPicker.isPanelShown()) {
            controlPicker(false);
        } else {
            Intent intent = new Intent(this, EntranceActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_mask_act_register:
                return mPicker.isPanelShown();
            default:
                break;
        }
        return false;
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
            setSignupBtnState();
        }
    }
}
