package com.ntucap.itcm.classes.events;

/**
 * Created by ProgrammerYuan on 08/06/17.
 */

public class PickerShowEvent {

    private int mEventId;
    private int mArrayResId;

    public PickerShowEvent(int eventId, int arrayResId) {
        this.mEventId = eventId;
        this.mArrayResId = arrayResId;
    }

    public int getEventId() {
        return mEventId;
    }

    public int getArrayResId() {
        return mArrayResId;
    }
}
