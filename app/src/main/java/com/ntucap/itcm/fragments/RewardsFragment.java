package com.ntucap.itcm.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ntucap.itcm.R;
import com.ntucap.itcm.utils.adapters.RewardHistoryAdapter;
import com.ntucap.itcm.views.IrisSwitchButton;

import org.zakariya.stickyheaders.StickyHeaderLayoutManager;

/**
 * Created by ProgrammerYuan on 28/05/17.
 */

public class RewardsFragment extends ITCMFragment implements IrisSwitchButton.OnIrisSwitchListener {

    private RecyclerView mRvRewards;
    private IrisSwitchButton mPanelSwitch;
    private LinearLayout mLLRewardsSummary;
    private RewardHistoryAdapter mRewardsAdapter;

    private static final int REWARDS_SUMMARY_INDEX = 0;
    private static final int REWARDS_HISTORY_INDEX = 1;

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
            mPanelSwitch.setItemsWithArray(new String[]{"NEW", "HISTORY"});
            mRvRewards = (RecyclerView) mInflatedView.findViewById(R.id.rv_rewards_frag_rewards);
            mLLRewardsSummary = (LinearLayout) mInflatedView.findViewById(R.id.ll_new_rewards_frag_rewards);
            mRewardsAdapter = new RewardHistoryAdapter(getActivity(), 3);
            mRvRewards.setLayoutManager(new StickyHeaderLayoutManager());
            mRvRewards.setAdapter(mRewardsAdapter);
            bindListeners();
        }
        return mInflatedView;
    }

    private void bindListeners() {
        mPanelSwitch.setOnSwitchListener(this);
    }

    @Override
    public void onSwitch(int switchIndex) {
        switch(switchIndex) {
            case REWARDS_SUMMARY_INDEX:
                mLLRewardsSummary.setVisibility(View.VISIBLE);
                mRvRewards.setVisibility(View.INVISIBLE);
                break;
            case REWARDS_HISTORY_INDEX:
                mLLRewardsSummary.setVisibility(View.INVISIBLE);
                mRvRewards.setVisibility(View.VISIBLE);
                break;
        }
    }
}
