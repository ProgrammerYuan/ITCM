package com.ntucap.itcm.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ntucap.itcm.R;
import com.ntucap.itcm.classes.events.PickerHideEvent;
import com.ntucap.itcm.classes.events.PickerShowEvent;
import com.ntucap.itcm.utils.EventUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by ProgrammerYuan on 04/06/17.
 */

public class MeFragment extends ITCMFragment implements View.OnClickListener{

    private LinearLayout mLLClothing;
    private LinearLayout mLLFeedbackSubmit;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState,
                R.layout.fragment_me);
        if(!mInitialized) {
            mInitialized = true;
            mLLClothing = (LinearLayout) mInflatedView.findViewById(R.id.ll_clothing_frag_me);
            mLLFeedbackSubmit = (LinearLayout) mInflatedView.findViewById(R.id.ll_feedback_frag_me);
        }
        return mInflatedView;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceived(PickerHideEvent event) {
        switch (event.getEventId()) {
            case EventUtil.EVENT_ID_CLOTHING_FRAG_ME:

                break;
            case EventUtil.EVENT_ID_SUBMITTING_FRAG_ME:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_clothing_frag_me:
                EventBus.getDefault().post(
                        new PickerShowEvent(EventUtil.EVENT_ID_CLOTHING_FRAG_ME, 0)
                );
                break;
            case R.id.ll_feedback_frag_me:
                EventBus.getDefault().post(
                        new PickerShowEvent(EventUtil.EVENT_ID_SUBMITTING_FRAG_ME, 0)
                );
                break;
        }
    }
}
