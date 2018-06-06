package com.ntucap.itcm.classes.events;

import com.github.johnpersano.supertoasts.library.Style;

/**
 * Created by ProgrammerYuan on 16/08/17.
 */

public class ShowToastEvent {

    private int mSenderId;
    private int mDuration;
    private String mMessage;

    private static final String DEFAULT_TOAST_MESSAGE = "";
    private static final int DEFAULT_TOAST_DURATION = Style.DURATION_MEDIUM;

    public ShowToastEvent(int senderId) {
        this(senderId, DEFAULT_TOAST_MESSAGE);
    }

    public ShowToastEvent(int senderId, String message) {
        this(senderId, message, DEFAULT_TOAST_DURATION);
    }
    
    public ShowToastEvent(int senderId, String message, int duration) {
        mSenderId = senderId;
        mMessage = message;
        mDuration = duration;
    }

    public void setSenderId(int senderId) {
        mSenderId = senderId;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public int getSenderId() {
        return mSenderId;
    }

    public String getMessage() {
        return mMessage;
    }

    public int getDuration() {
        return mDuration;
    }
}
