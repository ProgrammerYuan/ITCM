package com.ntucap.itcm.fragments;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dpizarro.uipicker.library.picker.PickerUI;
import com.ntucap.itcm.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by ProgrammerYuan on 02/05/17.
 */

public class PreferenceFragment extends ITCMFragment
        implements View.OnClickListener, View.OnTouchListener,PickerUI.PickerUIItemClickListener{

    private PickerUI mPicker;
    private ImageView mMask;
    private TextView mAirTempText, mHumidityText, mRangeFromText, mRangeToText;
    private LinearLayout mAirTempBtn, mHumidityBtn,mRangeFromBtn, mRangeToBtn;
    private ArrayList<String> mAirTempArray, mHumidityArray, mRangeFromArray, mRangeToArray;

    private int mCurrentPickerIndex = -1;
    private int mAirTempSlideNumber, mHumiditySlideNumber, mRangeFromSlideNumber, mRangeToSlideNumber;

    private static final int ALPHA_ANIM_DURATION = 400;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialization of the slide numbers
        mAirTempSlideNumber = mHumiditySlideNumber = 0;
        mRangeFromSlideNumber = mRangeToSlideNumber = 0;

        //Initialization of the arrays
        mAirTempArray = new ArrayList<>();
        mHumidityArray = new ArrayList<>();
        mRangeFromArray = new ArrayList<>();
        mRangeToArray = new ArrayList<>();

        Collections.addAll(mAirTempArray,
                mContext.getResources().getStringArray(R.array.str_array_air_temp));
        Collections.addAll(mHumidityArray,
                mContext.getResources().getStringArray(R.array.str_array_humidity));
        Collections.addAll(mRangeFromArray,
                mContext.getResources().getStringArray(R.array.str_array_hotness));
        Collections.addAll(mRangeToArray,
                mContext.getResources().getStringArray(R.array.str_array_coldness));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState,
                R.layout.fragment_preference);
        if(!mInitialized) {
            mInitialized = true;
            mPicker = (PickerUI) mInflatedView.findViewById(R.id.picker_ui_frag_pref);
            mMask = (ImageView) mInflatedView.findViewById(R.id.iv_mask_frag_pref);
            mAirTempText = (TextView) mInflatedView.findViewById(R.id.tv_air_temp_frag_pref);
            mHumidityText = (TextView) mInflatedView.findViewById(R.id.tv_humidity_frag_pref);
            mRangeFromText = (TextView) mInflatedView.findViewById(R.id.tv_range_from_frag_pref);
            mRangeToText = (TextView) mInflatedView.findViewById(R.id.tv_range_to_frag_pref);
            mAirTempBtn = (LinearLayout) mInflatedView.findViewById(R.id.ll_air_temp_frag_pref);
            mHumidityBtn = (LinearLayout) mInflatedView.findViewById(R.id.ll_rela_humid_frag_pref);
            mRangeFromBtn = (LinearLayout) mInflatedView.findViewById(R.id.ll_range_from_frag_pref);
            mRangeToBtn = (LinearLayout) mInflatedView.findViewById(R.id.ll_range_to_frag_pref);
            bindListeners();
        }
        return mInflatedView;
    }

    private void bindListeners() {
        mAirTempBtn.setOnClickListener(this);
        mHumidityBtn.setOnClickListener(this);
        mRangeFromBtn.setOnClickListener(this);
        mRangeToBtn.setOnClickListener(this);
        mPicker.setOnClickItemPickerUIListener(this);
    }

    private void controlMask(boolean show, int duration) {
        ObjectAnimator animator;
        if(show) {
            animator = ObjectAnimator.ofFloat(mMask, "alpha", 0.0f, 1.0f);
        } else {
            animator = ObjectAnimator.ofFloat(mMask, "alpha", 1.0f, 0.0f);
        }
        animator.setDuration(duration);
        animator.start();
    }

    private void controlPicker(boolean show, int slideNumber) {
        controlMask(show, ALPHA_ANIM_DURATION);
        if(show) mPicker.slide(slideNumber);
        else mPicker.slide();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_air_temp_frag_pref:
                mCurrentPickerIndex = 0;
                mPicker.setItems(mContext, mAirTempArray);
                controlPicker(true, mAirTempSlideNumber);
                break;
            case R.id.ll_rela_humid_frag_pref:
                mCurrentPickerIndex = 1;
                mPicker.setItems(mContext, mHumidityArray);
                controlPicker(true, mHumiditySlideNumber);
                break;
            case R.id.ll_range_from_frag_pref:
                mCurrentPickerIndex = 2;
                mPicker.setItems(mContext, mRangeFromArray);
                controlPicker(true, mRangeFromSlideNumber);
                break;
            case R.id.ll_range_to_frag_pref:
                mCurrentPickerIndex = 3;
                mPicker.setItems(mContext, mRangeToArray);
                controlPicker(true, mRangeToSlideNumber);
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_mask_frag_pref:
                return mPicker.isPanelShown();
        }
        return false;
    }

    @Override
    public void onItemClickPickerUI(int which, int position, String valueResult) {
        switch (mCurrentPickerIndex) {
            case 0:
                if (mAirTempSlideNumber == position) {
                    mAirTempText.setText(valueResult);
                } else {
                    mAirTempSlideNumber = position;
                }
                break;
            case 1:
                if (mHumiditySlideNumber == position) {
                    mHumidityText.setText(valueResult);
                } else {
                    mHumiditySlideNumber = position;
                }
                break;
            case 2:
                if (mRangeFromSlideNumber == position) {
                    mRangeFromText.setText(valueResult);
                } else {
                    mRangeFromSlideNumber = position;
                }
                break;
            case 3:
                if (mRangeToSlideNumber == position) {
                    mRangeToText.setText(valueResult);
                } else {
                    mRangeToSlideNumber = position;
                }
                break;
        }
    }
}
