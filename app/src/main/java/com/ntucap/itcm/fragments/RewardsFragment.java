package com.ntucap.itcm.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ntucap.itcm.R;
import com.ntucap.itcm.activities.ITCMActivity;
import com.ntucap.itcm.classes.ITCMReward;
import com.ntucap.itcm.classes.events.PickerHideEvent;
import com.ntucap.itcm.classes.events.RefreshEvent;
import com.ntucap.itcm.classes.events.RefreshRewardsEvent;
import com.ntucap.itcm.utils.EventUtil;
import com.ntucap.itcm.utils.NetUtil;
import com.ntucap.itcm.utils.adapters.RewardHistoryAdapter;
import com.ntucap.itcm.utils.dialogs.ITCMDialogFragment;
import com.ntucap.itcm.views.IrisSwitchButton;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.zakariya.stickyheaders.StickyHeaderLayoutManager;

import java.util.ArrayList;

/**
 * Created by ProgrammerYuan on 28/05/17.
 */

public class RewardsFragment extends ITCMFragment implements IrisSwitchButton.OnIrisSwitchListener, View.OnClickListener{

    private int mNewRewardsCount = 0;

    private RecyclerView mRvRewards;
    private TextView mTvNewRewardsHint, mTvNewRewardsCount;
    private IrisSwitchButton mPanelSwitch;
    private LinearLayout mLLRewardsSummary, mLLNewRewardsHint;
    private RewardHistoryAdapter mRewardsAdapter;

    private static final int REWARDS_SUMMARY_INDEX = 0;
    private static final int REWARDS_HISTORY_INDEX = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentId = EventUtil.SENDER_ID_REWARD;
        mNeedRegisterEventBust = true;
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
            mTvNewRewardsHint = (TextView) mInflatedView.findViewById(R.id.tv_hint_frag_rewards);
            mTvNewRewardsCount = (TextView) mInflatedView.findViewById(R.id.tv_msg_count_frag_rewards);
            mLLRewardsSummary = (LinearLayout) mInflatedView.findViewById(R.id.ll_new_rewards_frag_rewards);
            mLLNewRewardsHint = (LinearLayout) mInflatedView.findViewById(R.id.ll_rewards_hint);
            mRewardsAdapter = new RewardHistoryAdapter(getActivity(), 3);
            mRvRewards.setLayoutManager(new StickyHeaderLayoutManager());
            mRvRewards.setAdapter(mRewardsAdapter);
            bindListeners();
            setNewRewardsCount(1);
            getReward();
        }
        return mInflatedView;
    }

    private void bindListeners() {
        mRewardsAdapter.setRewardItemClickListener(new RewardHistoryAdapter.OnRewardItemClickListener() {
            @Override
            public void onRewardItemClick(ITCMReward reward) {
                Bundle data = new Bundle();
                data.putString(ITCMDialogFragment.DATA_KEY_TITLE, "Reward Detail");
                data.putString(ITCMDialogFragment.DATA_KEY_HINT, reward.getPopUpMessage());
                data.putInt(ITCMDialogFragment.DATA_KEY_HINT_GRAVITY, Gravity.LEFT);
                data.putInt(ITCMDialogFragment.DATA_KEY_CANCEL_VISIBILITY, View.GONE);
                ITCMDialogFragment.newInstance(data).show(getActivity().getSupportFragmentManager(), "");

            }
        });
        mLLNewRewardsHint.setOnClickListener(this);
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

    private ArrayList<ITCMReward> parseRewardsJson(JSONArray rewardsJsonArray) {
        ArrayList<ITCMReward> rewards = new ArrayList<>();
        int len = rewardsJsonArray.size();
        for(int i = 0; i < len; i ++)
            rewards.add(new ITCMReward(rewardsJsonArray.getJSONObject(i)));
        return rewards;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceived(RefreshEvent event) {
        getReward();
    }

    private void getReward() {
        NetUtil.getRewardHistory(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray sectionArray = response.getJSONArray("sectionArray");
                JSONObject object;
                if(sectionArray != null) {
                    int len = sectionArray.size();
                    for (int i = 0; i < len; i ++) {
                        object = sectionArray.getJSONObject(i);
                        mRewardsAdapter.addSection(object.getString("month") + " " + object.getString("year"),
                                parseRewardsJson(object.getJSONArray("dataArray"))
                        );
                    }
                }
                toast("Rewards refreshed succeed");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
    }

    private void setNewRewardsCount(int rewardsCount) {
        mNewRewardsCount = rewardsCount;
        if(mNewRewardsCount != 0) {
            mTvNewRewardsHint.setText("New gift available!");
            mTvNewRewardsCount.setVisibility(View.VISIBLE);
            mTvNewRewardsCount.setText(String.valueOf(rewardsCount));
        } else {
            mTvNewRewardsHint.setText("No gift available.");
            mTvNewRewardsCount.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_rewards_hint:
                if(mNewRewardsCount > 0) {
                    ITCMReward reward = new ITCMReward("", 2, "S$39", "11 Oct", "Two GV Movie Tickets!!");
                    Bundle data = new Bundle();
                    data.putString(ITCMDialogFragment.DATA_KEY_TITLE, "Reward Detail");
                    data.putString(ITCMDialogFragment.DATA_KEY_HINT, reward.getPopUpMessage());
                    data.putInt(ITCMDialogFragment.DATA_KEY_HINT_GRAVITY, Gravity.START);
                    ITCMDialogFragment.newInstance(data).show(getActivity().getSupportFragmentManager(), "");
                    setNewRewardsCount(0);
                } else {
                    Bundle data = new Bundle();
                    data.putString(ITCMDialogFragment.DATA_KEY_TITLE, "Reward Detail");
                    data.putString(ITCMDialogFragment.DATA_KEY_HINT, "There is no gift available.");
                    data.putInt(ITCMDialogFragment.DATA_KEY_HINT_GRAVITY, Gravity.START);
                    data.putInt(ITCMDialogFragment.DATA_KEY_CANCEL_VISIBILITY, View.GONE);
                    ITCMDialogFragment.newInstance(data).show(getActivity().getSupportFragmentManager(), "");
                }
                break;
        }
    }
}
