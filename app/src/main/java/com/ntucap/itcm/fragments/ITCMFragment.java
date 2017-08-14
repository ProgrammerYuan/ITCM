package com.ntucap.itcm.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.github.johnpersano.supertoasts.library.SuperToast;
import com.ntucap.itcm.R;
import com.ntucap.itcm.activities.ITCMActivity;

import static com.github.johnpersano.supertoasts.library.Style.DURATION_MEDIUM;

/**
 * Created by ProgrammerYuan on 18/04/17.
 */

public class ITCMFragment extends Fragment {

    protected View mInflatedView = null;
    protected boolean mInitialized = false;
    protected Context mContext;

    public ITCMFragment() {
        super();
    }

    public ITCMActivity getITCMActivity() {
        ITCMActivity activity = null;
        try {
            activity = (ITCMActivity)mContext;
        }catch (ClassCastException e) {
            e.printStackTrace();
        }
        return activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState, int resourceId) {
        if(mInflatedView == null) {
            mInflatedView = layoutInflater.inflate(resourceId, null);
        }

        ViewParent parentView = mInflatedView.getParent();
        if(parentView instanceof ViewGroup) {
            ((ViewGroup)parentView).removeView(mInflatedView);
        }
        return mInflatedView;
    }

    protected void toast(String message) {
        toastWithDuration(message, DURATION_MEDIUM);
    }

    protected void toastWithDuration(String message, int duration) {
        SuperToast.create(getActivity(), message, duration).show();
    }

}
