package com.ntucap.itcm.classes;

import android.content.ContentValues;

import com.ntucap.itcm.utils.DataUtil;

/**
 * Created by ProgrammerYuan on 28/05/17.
 */

public class ITCMReward extends ITCMObject {

    private int mType;
    private int mQty, mValue;
    private String mDate;
    private String mRemark;

    public ITCMReward() {
        this(1);
    }

    public ITCMReward(int type) {
        this(type, 0, 0, "", "");
    }

    public ITCMReward(int type, int sectionIndex, int itemIndex) {
        mType = type;
        mQty = 1;
        mValue = 10;
        mDate = (DataUtil.getMonthDaysNum(2016, sectionIndex % 12) - itemIndex) + " " +
                        DataUtil.getMonthAcronym(11 - sectionIndex % 12);
        mRemark = "Whatever Rewards~";
    }

    public ITCMReward(int type, int qty, int value, String date, String remark) {
        mType = type;
        mQty = qty;
        mValue = value;
        mDate = date;
        mRemark = remark;
    }

    public void setType(int type) {
        mType = type;
    }

    public void setQty(int qty) {
        mQty = qty;
    }

    public void setValue(int value) {
        mValue = value;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public void setRemark(String remark) {
       mRemark = remark;
    }

    public int getType() {
        return mType;
    }

    public int getQty() {
        return mQty;
    }

    public int getValue() {
        return mValue;
    }

    public String getValueString() {
        return String.format("S$%d", getValue());
    }

    public String getDate() {
        return mDate;
    }

    public String getRemark() {
        return mRemark;
    }

    @Override
    public String getCreateTableSQL() {
        return null;
    }

    @Override
    public ContentValues getUpdateContentValue() {
        return null;
    }

    @Override
    public String getDeleteTableSQL() {
        return null;
    }
}
