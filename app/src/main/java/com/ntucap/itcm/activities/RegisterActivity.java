package com.ntucap.itcm.activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Response;
import com.dpizarro.uipicker.library.picker.PickerUI;
import com.dpizarro.uipicker.library.picker.PickerUISettings;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperToast;
import com.ntucap.itcm.ITCMApplication;
import com.ntucap.itcm.R;
import com.ntucap.itcm.classes.ITCMUser;
import com.ntucap.itcm.db.ITCMDB;
import com.ntucap.itcm.utils.NetUtil;
import com.ntucap.itcm.utils.ValidationUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class RegisterActivity extends ITCMActivity
        implements View.OnClickListener, PickerUI.PickerUIItemClickListener
                    ,View.OnTouchListener{

    private static final String LOG_TAG = "RegisterActivity!";

    private TextView tv_signup_btn, tv_age_input, tv_gender_input,  tv_weight_input, tv_height_input;
    private EditText et_email_input, et_password_input, et_password_confirm_input,
            et_firstname_input, et_lastname_input;
    private ImageView iv_back_btn,iv_mask;
    private PickerUI mPicker;
    private ArrayList<String> ages, genders, weights, heights;
    private int inputCount = 0;
    private int age = -1,height, weight;
    private String gender, genderFormat, ageFormat, weightFormat, heightFormat;

    private int mCurrentPickerIndex;
    private int[] mSlideNumbers = new int[4];

    private static final int MAX_INPUT_COUNT = 9;
    private static final int ALPHA_ANIM_DURATION = 400;

    private static final int AGE_PICKER_INDEX = 0;
    private static final int GENDER_PICKER_INDEX = 1;
    private static final int WEIGHT_PICKER_INDEX = 2;
    private static final int HEIGHT_PICKER_INDEX = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        tv_signup_btn = (TextView) findViewById(R.id.tv_signup_confirm_act_register);
        et_email_input = (EditText) findViewById(R.id.et_email_input_act_register);
        et_password_input = (EditText) findViewById(R.id.et_pw_input_act_register);
        et_password_confirm_input = (EditText) findViewById(R.id.et_pw_confirm_input_act_register);
        et_firstname_input = (EditText) findViewById(R.id.et_firstname_input_act_register);
        et_lastname_input = (EditText) findViewById(R.id.et_lastname_input_act_register);
        tv_age_input = (TextView) findViewById(R.id.et_age_input_act_register);
        tv_gender_input = (TextView) findViewById(R.id.et_gender_input_act_register);
        tv_weight_input = (TextView) findViewById(R.id.et_weight_input_act_register);
        tv_height_input = (TextView) findViewById(R.id.et_height_input_act_register);
        iv_back_btn = (ImageView) findViewById(R.id.iv_back_arrow_act_register);
        iv_mask = (ImageView) findViewById(R.id.iv_mask_act_register);
        mPicker = (PickerUI) findViewById(R.id.picker_ui_act_register);

        //Picker Data Initialization
        ages = new ArrayList<>();
        genders = new ArrayList<>();
        weights = new ArrayList<>();
        heights = new ArrayList<>();

        initPickerDataSets();

        PickerUISettings pickerUISettings = new PickerUISettings.Builder()
                .withAutoDismiss(false)
                .withItemsClickables(true)
                .withUseBlur(false)
                .build();
        mPicker.setSettings(pickerUISettings);
        bindListeners();
        genderFormat = getString(R.string.str_gender_format);
        ageFormat = getString(R.string.str_age_format);
        weightFormat = getString(R.string.str_weight_format);
        heightFormat = getString(R.string.str_height_format);
    }

    private void initPickerDataSets() {

        for(int i = 0; i < 4;i ++)
            mSlideNumbers[i] = 0;

        for(int i = 0;i < 67; i ++)
            ages.add(String.valueOf(i));

        genders.add("Male");
        genders.add("Female");

        for(int i = 30; i <= 120; i ++)
            weights.add(String.valueOf(i));

        for(int i = 120; i <= 230; i ++)
            heights.add(String.valueOf(i));
    }

    private void bindListeners() {
        tv_signup_btn.setOnClickListener(this);
        iv_back_btn.setOnClickListener(this);
        tv_gender_input.setOnClickListener(this);
        tv_age_input.setOnClickListener(this);
        tv_weight_input.setOnClickListener(this);
        tv_height_input.setOnClickListener(this);
        new CountTextWatcher(et_email_input);
        new CountTextWatcher(et_password_input);
        new CountTextWatcher(et_password_confirm_input);
        new CountTextWatcher(et_firstname_input);
        new CountTextWatcher(et_lastname_input);
        iv_mask.setOnTouchListener(this);
        mPicker.setOnClickItemPickerUIListener(this);
    }

    private HashMap<String, String> checkSignUpInput() {
        HashMap<String, String> ret = new HashMap<>();
        String email = et_email_input.getText().toString();
        if(!ValidationUtil.validateEmail(email)) {
            SuperToast.create(this, "Not a valid Email Address", Style.DURATION_MEDIUM).show();
            return null;
        }

        String password = et_password_input.getText().toString(),
                passwordConfirm = et_password_confirm_input.getText().toString();
        if(password.length() == 0) {
            SuperToast.create(this, "Please input password", Style.DURATION_MEDIUM).show();
            return null;
        }

        if(passwordConfirm.length() == 0) {
            SuperToast.create(this, "Please confirm password", Style.DURATION_MEDIUM).show();
            return null;
        }

        if(!password.equals(passwordConfirm)) {
            SuperToast.create(this, "Inconsistent Password", Style.DURATION_MEDIUM).show();
            return null;
        }

        String firstName = et_firstname_input.getText().toString(),
                lastName = et_lastname_input.getText().toString();

        if(firstName.length() == 0) {
            SuperToast.create(this, "Please input firstname", Style.DURATION_MEDIUM).show();
            return null;
        }

        if(lastName.length() == 0) {
            SuperToast.create(this, "Please input lastname", Style.DURATION_MEDIUM).show();
            return null;
        }

        ret.put("email", email);
        ret.put("password", password);
        ret.put("firstName", firstName);
        ret.put("lastName", lastName);
        ret.put("age", String.valueOf(age));
        ret.put("gender", gender);
        ret.put("weight", String.valueOf(weight));
        ret.put("height", String.valueOf(height));
        return ret;
    }

    private void signUp(HashMap<String, String> parameters) {
        if(parameters == null) return;
        final ITCMUser user = new ITCMUser(parameters);
        user.setIsCurrentUser(true);
        NetUtil.register(user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                toast("Registration succeed, check your email inbox please");
                ITCMDB.saveUser(user);
                Intent intent = new Intent(RegisterActivity.this, EntranceActivity.class);
                startActivity(intent);
                RegisterActivity.this.finish();
            }
        }, new DefaultErrorListener());


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
        } else if (inputCount == MAX_INPUT_COUNT){
            tv_signup_btn.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent;
        switch (id) {
            case R.id.tv_signup_confirm_act_register:
                signUp(checkSignUpInput());
                break;
            case R.id.iv_back_arrow_act_register:
                intent = new Intent(this, EntranceActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.et_gender_input_act_register:
                mCurrentPickerIndex = GENDER_PICKER_INDEX;
                mPicker.setItems(this, genders);
                controlPicker(true);
                break;
            case R.id.et_age_input_act_register:
                mCurrentPickerIndex = AGE_PICKER_INDEX;
                mPicker.setItems(this, ages);
                controlPicker(true);
                break;
            case R.id.et_weight_input_act_register:
                mCurrentPickerIndex = WEIGHT_PICKER_INDEX;
                mPicker.setItems(this, weights);
                controlPicker(true);
                break;
            case R.id.et_height_input_act_register:
                mCurrentPickerIndex = HEIGHT_PICKER_INDEX;
                mPicker.setItems(this, heights);
                controlPicker(true);
            default:
                break;
        }
    }

    @Override
    public void onItemClickPickerUI(int which, int position, String valueResult) {
        int originTextLength = -1;
        if(mSlideNumbers[mCurrentPickerIndex] == position) {
            switch (mCurrentPickerIndex) {
                case AGE_PICKER_INDEX:
                    originTextLength = tv_age_input.getText().length();
                    age = Integer.valueOf(valueResult);
                    tv_age_input.setText(String.format(ageFormat, age));
                    break;
                case GENDER_PICKER_INDEX:
                    originTextLength = tv_gender_input.getText().length();
                    gender = valueResult;
                    tv_gender_input.setText(String.format(genderFormat, valueResult));
                    break;
                case WEIGHT_PICKER_INDEX:
                    originTextLength = tv_weight_input.getText().length();
                    weight = Integer.valueOf(valueResult);
                    tv_weight_input.setText(String.format(weightFormat, weight));
                    break;
                case HEIGHT_PICKER_INDEX:
                    originTextLength = tv_height_input.getText().length();
                    height = Integer.valueOf(valueResult);
                    tv_height_input.setText(String.format(heightFormat, height));
                    break;
            }
            if(originTextLength == 0) inputCount ++;
            controlPicker(false);
            setSignupBtnState();
        } else {
            mSlideNumbers[mCurrentPickerIndex] = position;
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
