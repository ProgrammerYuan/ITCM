package com.ntucap.itcm.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ntucap.itcm.R;

/**
 * Created by ProgrammerYuan on 18/04/17.
 */

public class EnvironmentalFragment extends ITCMFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState,
                R.layout.fragment_environmental);

        return mInflatedView;
    }
}
