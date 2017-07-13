package com.ntucap.itcm.utils.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ntucap.itcm.R;
import com.ntucap.itcm.classes.ITCMReward;
import com.ntucap.itcm.utils.DataUtil;

import org.zakariya.stickyheaders.SectioningAdapter;

import java.util.ArrayList;

/**
 * Created by ProgrammerYuan on 28/05/17.
 */

public class RewardHistoryAdapter extends SectioningAdapter {

    private ArrayList<Section> mSections;
    private Context mContext;

    class Section {
        private int type;
        private String title;
        private ArrayList<ITCMReward> rewards;

        public Section(int type, int sectionIndex, String title) {
            this.type = type;
            this.title = DataUtil.getMonth(11 - sectionIndex) + " 2016";
            rewards = new ArrayList<>();
            if(type == 0)
                for(int i = 0; i < 30; i ++)
                    rewards.add(new ITCMReward(i % 2, sectionIndex, i));
        }

        public String getTitle() {
            return title;
        }

        public int getType() {
            return type;
        }

        public ITCMReward getItem(int index) throws IndexOutOfBoundsException{
            if(rewards == null) return null;
            return rewards.get(index);
        }
    }


    private class ITCMHeaderViewHolder extends SectioningAdapter.HeaderViewHolder {
        TextView mTvTitle;

        public ITCMHeaderViewHolder(View headerView) {
            super(headerView);
            mTvTitle = (TextView) headerView.findViewById(R.id.tv_title_item_header);
        }
    }

    private class ITCMItemViewHolder extends SectioningAdapter.ItemViewHolder {
        LinearLayout mContainer;
        TextView mTvDate, mTvQty, mTvValue, mTvRemark;

        public ITCMItemViewHolder(View itemView) {
            super(itemView);
            mContainer = (LinearLayout) itemView.findViewById(R.id.ll_container_item_rewards);
            mTvDate = (TextView) itemView.findViewById(R.id.tv_date_item_rewards);
            mTvQty = (TextView) itemView.findViewById(R.id.tv_qty_item_rewards);
            mTvValue = (TextView) itemView.findViewById(R.id.tv_value_item_rewards);
            mTvRemark = (TextView) itemView.findViewById(R.id.tv_remark_item_rewards);
        }

        public void setBackground(int color){
            mContainer.setBackgroundColor(color);
        }

        public void fillData(ITCMReward reward) {
            setDate(reward.getDate());
            setQty(reward.getQty());
            setValue(reward.getValueString());
            setRemark(reward.getRemark());
        }

        private void setDate(String date) {
            if(mTvDate != null) mTvDate.setText(date);
        }

        private void setQty(int qty) {
            if(mTvQty != null) mTvQty.setText(String.valueOf(qty));
        }

        private void setValue(String valueStr) {
            if(mTvValue != null) mTvValue.setText(String.valueOf(valueStr));
        }

        private void setRemark(String remark) {
            if(mTvRemark != null) mTvRemark.setText(remark);
        }

    }

    public RewardHistoryAdapter(Context context, int numSections) {
        mContext = context;
        mSections = new ArrayList<>();
        for(int i = 0; i < numSections * 2; i ++) {
            mSections.add(new Section(i % 2, i / 2, DataUtil.getMonth(11 - i / 2)));
        }
    }

    @Override
    public int getNumberOfSections() {
        return mSections.size();
    }

    @Override
    public int getNumberOfItemsInSection(int sectionIndex) {
        return mSections.get(sectionIndex).rewards.size();
    }

    @Override
    public boolean doesSectionHaveHeader(int sectionIndex) {
        return true;
    }

    @Override
    public int getSectionHeaderUserType(int sectionIndex) {
        return mSections.get(sectionIndex).getType();
    }

    @Override
    public int getSectionItemUserType(int sectionIndex, int itemIndex) {
        return 0;
    }

    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new ITCMItemViewHolder(inflater.inflate(R.layout.view_common_item,null));

    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(headerType == 0) {
            return new ITCMHeaderViewHolder(inflater.inflate(R.layout.view_first_header, null));
        } else {
            return new ITCMHeaderViewHolder(inflater.inflate(R.layout.view_last_footer, null));
        }

    }

    @Override
    public void onBindItemViewHolder(SectioningAdapter.ItemViewHolder viewHolder, int sectionIndex, int itemIndex, int itemType) {
        try {
            ITCMItemViewHolder itemViewHolder = (ITCMItemViewHolder) viewHolder;
            Section s = mSections.get(sectionIndex);
            ITCMReward reward = s.getItem(itemIndex);
            int color;
            if(reward.getType() == 0) color = mContext.getResources().getColor(R.color.forthTheme);
            else color = mContext.getResources().getColor(R.color.white);
            itemViewHolder.setBackground(color);
            itemViewHolder.fillData(reward);

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onBindHeaderViewHolder(SectioningAdapter.HeaderViewHolder viewHolder, int sectionIndex, int headerType) {
        if(sectionIndex % 2 == 0) {
            ITCMHeaderViewHolder itemHeaderViewHolder = (ITCMHeaderViewHolder) viewHolder;
            itemHeaderViewHolder.mTvTitle.setText(mSections.get(sectionIndex).getTitle());
        }
    }
}
