package com.ntucap.itcm.fragments;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dpizarro.uipicker.library.picker.PickerUI;
import com.ntucap.itcm.R;
import com.ntucap.itcm.classes.events.PickerHideEvent;
import com.ntucap.itcm.classes.events.PickerShowEvent;
import com.ntucap.itcm.utils.EventUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by ProgrammerYuan on 02/05/17.
 */

public class PreferenceFragment extends ITCMFragment
        implements View.OnClickListener, PickerUI.PickerUIItemClickListener{

    private TextView mAirTempText, mHumidityText, mRangeFromText, mRangeToText;
    private LinearLayout mAirTempBtn, mHumidityBtn,mRangeFromBtn, mRangeToBtn;
    private ArrayList<String> mAirTempArray, mHumidityArray, mRangeFromArray, mRangeToArray;

    private int mCurrentPickerIndex = -1;
    private int mAirTempSlideNumber, mHumiditySlideNumber, mRangeFromSlideNumber, mRangeToSlideNumber;

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
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState,
                R.layout.fragment_preference);
        if(!mInitialized) {
            mInitialized = true;
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
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceived(PickerHideEvent event) {
        switch (event.getEventId()) {
            case EventUtil.EVENT_ID_AIRTEMP_FRAG_PREF:
                mAirTempText.setText(event.getResponse());
                break;
            case EventUtil.EVENT_ID_HUMID_FRAG_PREF:
                mHumidityText.setText(event.getResponse());
                break;
            case EventUtil.EVENT_ID_RANGE_FROM_FRAG_PREF:
                mRangeFromText.setText(event.getResponse());
                break;
            case EventUtil.EVENT_ID_RANGE_TO_FRAG_PREF:
                mRangeToText.setText(event.getResponse());
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_air_temp_frag_pref:
                EventBus.getDefault().post(
                        new PickerShowEvent(EventUtil.EVENT_ID_AIRTEMP_FRAG_PREF, R.array.str_array_air_temp)
                );
                break;
            case R.id.ll_rela_humid_frag_pref:
                EventBus.getDefault().post(
                        new PickerShowEvent(EventUtil.EVENT_ID_HUMID_FRAG_PREF, R.array.str_array_humidity)
                );
                break;
            case R.id.ll_range_from_frag_pref:
                EventBus.getDefault().post(
                        new PickerShowEvent(EventUtil.EVENT_ID_RANGE_FROM_FRAG_PREF, R.array.str_array_hotness)
                );
                break;
            case R.id.ll_range_to_frag_pref:
                EventBus.getDefault().post(
                        new PickerShowEvent(EventUtil.EVENT_ID_RANGE_TO_FRAG_PREF, R.array.str_array_coldness)
                );
                break;
            default:
                break;
        }

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
