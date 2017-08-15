package com.ntucap.itcm.classes.events;

/**
 * Created by ProgrammerYuan on 08/06/17.
 */

public class PickerShowEvent {

    private int mEventId;
    private int mArrayResId;
    private int mPickerSlideNumber;

    public PickerShowEvent(int eventId, int arrayResId, int pickerSlideNumber) {
        this.mEventId = eventId;
        this.mPickerSlideNumber = pickerSlideNumber;
        this.mArrayResId = arrayResId;
    }

    public int getEventId() {
        return mEventId;
    }

    public int getArrayResId() {
        return mArrayResId;
    }

    public int getPickerSlideNumber() {
        return mPickerSlideNumber;
    }
}
