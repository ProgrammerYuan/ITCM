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

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dpizarro.uipicker.library.picker.PickerUI;
import com.ntucap.itcm.R;
import com.ntucap.itcm.activities.ITCMActivity;
import com.ntucap.itcm.classes.events.PickerHideEvent;
import com.ntucap.itcm.classes.events.PickerShowEvent;
import com.ntucap.itcm.classes.events.UploadPreferenceEvent;
import com.ntucap.itcm.utils.EventUtil;
import com.ntucap.itcm.utils.NetUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by ProgrammerYuan on 02/05/17.
 */

public class PreferenceFragment extends ITCMFragment
        implements View.OnClickListener{

    private int mAirTemp , mHumidity, mComfortLevelFrom, mComfortLevelTo;

    private TextView mAirTempText, mHumidityText, mRangeFromText, mRangeToText;
    private LinearLayout mAirTempBtn, mHumidityBtn,mRangeFromBtn, mRangeToBtn;
    private ArrayList<String> mAirTempArray, mHumidityArray, mRangeFromArray, mRangeToArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                mAirTemp = event.getResponseValue();
                break;
            case EventUtil.EVENT_ID_HUMID_FRAG_PREF:
                mHumidity = event.getResponseValue();
                mHumidityText.setText(event.getResponse());
                break;
            case EventUtil.EVENT_ID_RANGE_FROM_FRAG_PREF:
                mComfortLevelFrom = event.getResponseValue();
                mRangeFromText.setText(event.getResponse());
                break;
            case EventUtil.EVENT_ID_RANGE_TO_FRAG_PREF:
                mComfortLevelTo = event.getResponseValue();
                mRangeToText.setText(event.getResponse());
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceived(UploadPreferenceEvent event) {
        HashMap<String, String> params = new HashMap<>();
        params.put("preferredInfoorAirTemp", String.valueOf(mAirTemp));
        params.put("preferredInfoorAirHumidity", String.valueOf(mHumidity));
        params.put("acceptableComfortLevelFrom", String.valueOf(mComfortLevelFrom));
        params.put("acceptableComfortLevelTo", String.valueOf(mComfortLevelTo));
        NetUtil.uploadUserPreference(params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                toast("User preference successfully uploaded");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                toast("Something wrong with your network");
            }
        });
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
}
