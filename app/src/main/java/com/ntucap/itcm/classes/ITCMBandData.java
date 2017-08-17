package com.ntucap.itcm.classes;

import android.content.ContentValues;
import android.util.Log;
import android.util.Range;

import com.microsoft.band.sensors.MotionType;
import com.microsoft.band.sensors.UVIndexLevel;
import com.ntucap.itcm.ITCMApplication;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by ProgrammerYuan on 15/08/17.
 */

public class ITCMBandData extends ITCMObject {

    private int mHeartRate, mFeedback, mConsumedCalories,
                mAmbientLight, mClothingIndex, mGSR;
    private long mCaloryRecordTime, mFlightsAscended, mFlightDescended, mSteppingGain, mSteppingLoss,
                 mStepsAscended, mStepsDescended, mTotalGain, mTotalLoss, mCaloryTodayInit, mCaloryToday, mDistanceToday,
                 mStepsToday;
    private float mRate, mCurrentPace, mCurrentSpeed,
                  mAccelerationX, mAccelerationY, mAccelerationZ,
                  mAngularVelocityX, mAngularVelocityY, mAngularVelocityZ;
    private double mAirPressure, mAirTemp, mSkinTemp, mRRInterval;
    private String[] mDates;
    private MotionType mMotionType;
    private UVIndexLevel mUVIndexLevel;
    private Range<Integer> mUserPreferenceRange;

    private static final float METABOLIC_RATE_COEFF = 0.0167f;
    private static final float[] CLOTHING_LEVEL = {0.53f, 0.57f, 0.60f, 0.62f, 0.65f, 0.68f};
    public ITCMBandData() {
        mCaloryTodayInit = -1;
    }

    public HashMap<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("year", mDates[0]);
        params.put("date", mDates[1]);
        params.put("time", mDates[2]);
        params.put("userComfortPreference", getPreferenceRange().contains(mFeedback) ? "1" : "0");
        params.put("userFeedback", String.valueOf(mFeedback));
        params.put("metabolicRate", String.valueOf(getMetabolicRate()));
        params.put("clothingLevel", String.valueOf(CLOTHING_LEVEL[mClothingIndex]));
        params.put("ambientLight", String.valueOf(mAmbientLight));
        params.put("airPressure", String.valueOf(mAirPressure));
        params.put("uvLevelData", String.valueOf(getUVLevelNumber(mUVIndexLevel)));
        params.put("accelerationX", String.valueOf(mAccelerationX));
        params.put("accelerationY", String.valueOf(mAccelerationY));
        params.put("accelerationZ", String.valueOf(mAccelerationZ));
        params.put("flightsAscended", String.valueOf(mFlightsAscended));
        params.put("flightsDescended", String.valueOf(mFlightDescended));
        params.put("rate", String.valueOf(mRate));
        params.put("steppingGain", String.valueOf(mSteppingGain));
        params.put("steppingLoss", String.valueOf(mSteppingLoss));
        params.put("stepsAscended", String.valueOf(mStepsAscended));
        params.put("stepsDescended", String.valueOf(mStepsDescended));
        params.put("totalGain", String.valueOf(mTotalGain));
        params.put("totalLoss", String.valueOf(mTotalLoss));
        params.put("caloryToday", String.valueOf(mCaloryToday));
        params.put("distanceToday", String.valueOf(mDistanceToday));
        params.put("currentPace", String.valueOf(mCurrentPace));
        params.put("currentSpeed", String.valueOf(mCurrentSpeed));
        params.put("currentMotionType", String.valueOf(getMotionTypeNumber(mMotionType)));
        params.put("angularVelocityX", String.valueOf(mAngularVelocityX));
        params.put("angularVelocityY", String.valueOf(mAngularVelocityY));
        params.put("angularVelocityZ", String.valueOf(mAngularVelocityZ));
        params.put("gsr", String.valueOf(mGSR));
        params.put("heartRate", String.valueOf(mHeartRate));
        params.put("stepsToday", String.valueOf(mStepsToday));
        params.put("rrInterval", String.valueOf(mRRInterval));
        params.put("skinTemp", String.valueOf(mSkinTemp));
        return params;
    }

    public void setDates(String[] dates) {
        mDates = dates;
    }

    public void setUserPreferenceRange(int from, int to) {
        setUserPreferenceRange(new Range<Integer>(from, to));
    }

    public void setUserPreferenceRange(Range<Integer> preferenceRange) {
        mUserPreferenceRange = preferenceRange;
    }

    public void setFeedBack(int feedBack) {
        mFeedback = feedBack;
    }

    public void setClothingIndex(int clothingIndex) {
        mClothingIndex = clothingIndex;
    }

    public void setAmbientLight(int ambientLight) {
        mAmbientLight = ambientLight;
    }

    public void setAirPressure(double airPressure) {
        mAirPressure = airPressure;
    }

    public void setUVIndexLevel(UVIndexLevel uvIndexLevel) {
        mUVIndexLevel = uvIndexLevel;
    }

    public void setAccelerationX(float accelerationX) {
        mAccelerationX = accelerationX;
    }

    public void setAccelerationY(float accelerationY) {
        mAccelerationY = accelerationY;
    }

    public void setAccelerationZ(float accelerationZ) {
        mAccelerationZ = accelerationZ;
    }

    public void setFlightsAscended(long flightsAscended) {
        mFlightsAscended = flightsAscended;
    }

    public void setFlightsDesended(long flightsDesended) {
        mFlightDescended = flightsDesended;
    }

    public void setRate(float rate) {
        mRate = rate;
    }

    public void setSteppingGain(long steppingGain) {
        mSteppingGain = steppingGain;
    }

    public void setSteppingLoss(long steppingLoss) {
        mSteppingLoss = steppingLoss;
    }

    public void setStepsAscended(long stepsAscended) {
        mStepsAscended = stepsAscended;
    }

    public void setStepsDescended(long stepsDescended) {
        mStepsDescended = stepsDescended;
    }

    public void setTotalGain(long totalGain) {
        mTotalGain = totalGain;
    }

    public void setTotalLoss(long totalLoss) {
        mTotalLoss = totalLoss;
    }

    public void setCaloryToday(long caloryToday) {
        if(mCaloryTodayInit == -1) {
            mCaloryTodayInit = caloryToday;
            mCaloryRecordTime = new Date().getTime();
        }
        mCaloryToday = caloryToday;
    }

    public void setDistanceToday(long distanceToday) {
        mDistanceToday = distanceToday;
    }

    public void setCurrentPace(float currentPace) {
        mCurrentPace = currentPace;
    }

    public void setCurrentSpeed(float currentSpeed) {
        mCurrentSpeed = currentSpeed;
    }

    public void setMotionType(MotionType motionType) {
        mMotionType = motionType;
    }

    public void setAngularVelocityX(float angularVelocityX) {
        mAngularVelocityX = angularVelocityX;
    }

    public void setAngularVelocityY(float angularVelocityY) {
        mAngularVelocityY = angularVelocityY;
    }

    public void setAngularVelocityZ(float angularVelocityZ) {
        mAngularVelocityZ = angularVelocityZ;
    }

    public void setGSR(int GSR) {
        mGSR = GSR;
    }

    public void setHeartRate(int heartRate) {
        mHeartRate = heartRate;
    }

    public void setStepsToday(long stepsToday) {
        mStepsToday = stepsToday;
    }

    public void setRRInterval(double rrInterval) {
        mRRInterval = rrInterval;
    }

    public void setSkinTemp(double skinTemp) {
        mSkinTemp = skinTemp;
    }

    private int getUVLevelNumber(UVIndexLevel UVLevel){
        int ret;
        switch (UVLevel) {
            case NONE:
                ret = 1;
                break;
            case LOW:
                ret = 2;
                break;
            case MEDIUM:
                ret = 3;
                break;
            case HIGH:
                ret = 4;
                break;
            case VERY_HIGH:
                ret = 5;
                break;
            default:
                ret = 0;
                break;
        }
        return ret;
    }

    private int getMotionTypeNumber(MotionType motionType) {
        int ret;
        switch (motionType) {
            case IDLE:
                ret = 1;
                break;
            case WALKING:
                ret = 2;
                break;
            case JOGGING:
                ret = 3;
                break;
            case RUNNING:
                ret = 4;
                break;
            case UNKNOWN:
            default:
                ret = 0;
                break;
        }
        return ret;
    }

    private float getMetabolicRate() {
        ITCMUser user = ITCMApplication.getCurrentUser();
        long timeInterval = new Date().getTime() - mCaloryRecordTime;
        float ret = 0f;
        try {
            ret = mConsumedCalories * 1f / (timeInterval * user.getWeight() * METABOLIC_RATE_COEFF);
        } catch (ArithmeticException e){ //Divided by zero
            e.printStackTrace();
        }
        return ret;
    }

    private Range<Integer> getPreferenceRange() {
        if(mUserPreferenceRange == null) mUserPreferenceRange = new Range<>(-1, 1);
        return mUserPreferenceRange;
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
