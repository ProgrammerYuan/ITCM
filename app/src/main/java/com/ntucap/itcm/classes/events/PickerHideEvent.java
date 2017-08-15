package com.ntucap.itcm.classes.events;

/**
 * Created by ProgrammerYuan on 08/06/17.
 */

public class PickerHideEvent {

    private int mEventId;
    private int mResponseValue;
    private int mPickerSlideNumber;
    private String mResponse;

    public final static int DEFAULT_RESPONSE_VALUE = -1;
    public final static int DEFAULT_SLIDE_NUMBER = 0;
    public PickerHideEvent(int eventId, String response) {
        this(eventId, response, DEFAULT_RESPONSE_VALUE, DEFAULT_SLIDE_NUMBER);
    }

    public PickerHideEvent(int eventId, String response, int slideNumber) {
        this(eventId, response, DEFAULT_RESPONSE_VALUE, slideNumber);
    }

    public PickerHideEvent(int eventId, String response, int responseValue, int slideNumber) {
        setEventId(eventId);
        setResponse(response);
        setResponseValue(responseValue);
        setPickerSlideNumber(slideNumber);
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

    public void setPickerSlideNumber(int pickerSlideNumber) {
        mPickerSlideNumber = pickerSlideNumber;
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

    public int getPickerSlideNumber() {
        return mPickerSlideNumber;
    }

}
