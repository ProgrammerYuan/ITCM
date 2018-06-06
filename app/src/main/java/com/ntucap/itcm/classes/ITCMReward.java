package com.ntucap.itcm.classes;

import android.content.ContentValues;

import com.alibaba.fastjson.JSONObject;
import com.ntucap.itcm.utils.DataUtil;

/**
 * Created by ProgrammerYuan on 28/05/17.
 */

public class ITCMReward extends ITCMObject {

    private String mType;
    private int mQty;
    private String mValue;
    private String mDate;
    private String mRemark;

    private static final String JSON_KEY_TYPE = "rewardType";
    private static final String JSON_KEY_QTY = "rewardQuantity";
    private static final String JSON_KEY_VALUE = "rewardValue";
    private static final String JSON_KEY_DATE = "rewardDate";
    private static final String JSON_KEY_REMARK = "rewardRemark";


    public ITCMReward() {
        super();
    }

    public ITCMReward(JSONObject json) {
        this();
        mType = json.getString(JSON_KEY_TYPE);
        mQty = json.getIntValue(JSON_KEY_QTY);
        mValue = json.getString(JSON_KEY_VALUE);
        mDate = json.getString(JSON_KEY_DATE);
        mRemark = json.getString(JSON_KEY_REMARK);
    }

    public ITCMReward(String type) {
        this(type, 0, "", "", "");
    }

    public ITCMReward(String type, int sectionIndex, int itemIndex) {
        mType = type;
        mQty = 1;
        mValue = "S$" + 10;
        mDate = (DataUtil.getMonthDaysNum(2016, sectionIndex % 12) - itemIndex) + " " +
                        DataUtil.getMonthAcronym(11 - sectionIndex % 12);
        mRemark = "Whatever Rewards~";
    }

    public ITCMReward(String type, int qty, String value, String date, String remark) {
        mType = type;
        mQty = qty;
        mValue = value;
        mDate = date;
        mRemark = remark;
    }

    public void setType(String type) {
        mType = type;
    }

    public void setQty(int qty) {
        mQty = qty;
    }

    public void setValue(String value) {
        mValue = value;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public void setRemark(String remark) {
       mRemark = remark;
    }

    public String getType() {
        return mType;
    }

    public int getQty() {
        return mQty;
    }

    public String getValue() {
        return mValue;
    }

    public String getValueString() {
        return mValue;
    }

    public String getDate() {
        return mDate;
    }

    public String getRemark() {
        return mRemark;
    }

    public String getPopUpMessage() {
        return  "Date: " + getDate() + "\n" +
                "Quantity: " + String.valueOf(getQty()) + "\n" +
                "Value: " + getValueString() + "\n" +
                "Remarks: " + "\n" +
                mRemark;

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
