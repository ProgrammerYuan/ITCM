package com.ntucap.itcm.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ntucap.itcm.ITCMApplication;
import com.ntucap.itcm.R;
import com.ntucap.itcm.activities.ChangePasswordActivity;
import com.ntucap.itcm.activities.EntranceActivity;
import com.ntucap.itcm.classes.ITCMUser;
import com.ntucap.itcm.classes.events.PickerHideEvent;
import com.ntucap.itcm.classes.events.PickerShowEvent;
import com.ntucap.itcm.db.ITCMDB;
import com.ntucap.itcm.utils.EventUtil;
import com.ntucap.itcm.utils.dialogs.ITCMDialogFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by ProgrammerYuan on 04/06/17.
 */

public class MeFragment extends ITCMFragment implements View.OnClickListener{

    private TextView mTvUserName, mTvUserDetail;
    private LinearLayout mLLChangePassword, mLLClothing, mLLFeedbackSubmit, mLLLogout;

    @Override
    public void onStart() {
        super.onStart();
        applyData();
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
            mTvUserName = (TextView) mInflatedView.findViewById(R.id.tv_username_frag_me);
            mTvUserDetail = (TextView) mInflatedView.findViewById(R.id.tv_user_detail_frag_me);
            mLLChangePassword = (LinearLayout) mInflatedView.findViewById(R.id.ll_change_pw_frag_me);
            mLLClothing = (LinearLayout) mInflatedView.findViewById(R.id.ll_clothing_frag_me);
            mLLFeedbackSubmit = (LinearLayout) mInflatedView.findViewById(R.id.ll_feedback_frag_me);
            mLLLogout = (LinearLayout) mInflatedView.findViewById(R.id.ll_logout_frag_me);
            bindListeners();
        }
        return mInflatedView;
    }

    private void bindListeners() {
        mLLChangePassword.setOnClickListener(this);
        mLLClothing.setOnClickListener(this);
        mLLFeedbackSubmit.setOnClickListener(this);
        mLLLogout.setOnClickListener(this);
    }

    private void applyData() {
        ITCMUser user = ITCMApplication.getCurrentUser();
        mTvUserName.setText(user.getFullName());
        mTvUserDetail.setText(user.getCombinedDetail());
        user = null;
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
            case R.id.ll_change_pw_frag_me:
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                getActivity().startActivity(intent);
                break;
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
            case R.id.ll_logout_frag_me:
                Bundle data = new Bundle();
                data.putString(ITCMDialogFragment.DATA_KEY_TITLE, "Logout");
                data.putString(ITCMDialogFragment.DATA_KEY_HINT, "Do You Want to Logout?");
                data.putString(ITCMDialogFragment.DATA_KEY_CONFIRM, "Confirm");
                data.putString(ITCMDialogFragment.DATA_KEY_CANCEL, "Cancel");
                final ITCMDialogFragment dialog = ITCMDialogFragment.newInstance(data);
                dialog.setConfirmListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("LOGTAGDIALOG___", "Confirm");
                        if (ITCMDB.signout() != -1) {
                            ITCMApplication.setCurrentUser(null);
                            Intent intent = new Intent(getActivity(), EntranceActivity.class);
                            getActivity().startActivity(intent);
                            getActivity().finish();
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show(getActivity().getSupportFragmentManager(), "");
                break;
        }
    }
}
