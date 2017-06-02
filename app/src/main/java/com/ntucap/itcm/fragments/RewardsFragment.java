package com.ntucap.itcm.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dpizarro.uipicker.library.picker.PickerUI;
import com.ntucap.itcm.R;
import com.ntucap.itcm.utils.adapters.RewardHistoryAdapter;
import com.ntucap.itcm.views.IrisSwitchButton;

import org.zakariya.stickyheaders.StickyHeaderLayoutManager;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ProgrammerYuan on 28/05/17.
 */

public class RewardsFragment extends ITCMFragment {

    private RecyclerView mRvRewards;
    private RewardHistoryAdapter mRewardsAdapter;
    private IrisSwitchButton mPanelSwitch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState,
                R.layout.fragment_rewards);
        if(!mInitialized) {
            mInitialized = true;
            mPanelSwitch = (IrisSwitchButton) mInflatedView.findViewById(R.id.sw_panel_frag_rewards);
            mRvRewards = (RecyclerView) mInflatedView.findViewById(R.id.rv_rewards_frag_rewards);
            mRewardsAdapter = new RewardHistoryAdapter(getActivity(), 3);
            mRvRewards.setLayoutManager(new StickyHeaderLayoutManager());
            mRvRewards.setAdapter(mRewardsAdapter);
        }
        return mInflatedView;
    }
}
