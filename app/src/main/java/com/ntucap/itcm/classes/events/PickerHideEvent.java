package com.ntucap.itcm.classes.events;

/**
 * Created by ProgrammerYuan on 08/06/17.
 */

public class PickerHideEvent {

    private int mEventId;
    private String mResponse;
    private int mResponseValue;

    public final static int DEFAULT_RESPONSE_VALUE = -1;

    public PickerHideEvent(int eventId, String response) {
        this(eventId, response, DEFAULT_RESPONSE_VALUE);
    }

    public PickerHideEvent(int eventId, String response, int responseValue) {
        setEventId(eventId);
        setResponse(response);
        setResponseValue(responseValue);
    }

    public void setEventId(int eventId) {
        this.mEventId = eventId;
    }

    public void setResponse(String response) {
        this.mResponse = (response == null) ? "" : response;
    }

    public void setResponseValue(int responseValue) {
        mResponseValue = responseValue;
    }

    public int getEventId() {
        return mEventId;
    }

    public String getResponse() {
        return mResponse;
    }

    public int getResponseValue() {
        return mResponseValue;
    }

}
