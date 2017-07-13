package com.ntucap.itcm.classes.events;

/**
 * Created by ProgrammerYuan on 08/06/17.
 */

public class PickerHideEvent {

    private int mEventId;
    private String mResponse;

    public PickerHideEvent(int eventId, String response) {
        setEventId(eventId);
        setResponse(response);
    }

    public void setEventId(int eventId) {
        this.mEventId = eventId;
    }

    public void setResponse(String response) {
        this.mResponse = response == null ? "" : mResponse;
    }

    public int getEventId() {
        return mEventId;
    }

    public String getResponse() {
        return mResponse;
    }

}
