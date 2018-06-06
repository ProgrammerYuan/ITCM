package com.ntucap.itcm.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.ntucap.itcm.ITCMApplication;
import com.ntucap.itcm.R;
import com.ntucap.itcm.activities.ChangePasswordActivity;
import com.ntucap.itcm.activities.EntranceActivity;
import com.ntucap.itcm.activities.UserProfileActivity;
import com.ntucap.itcm.classes.ITCMUser;
import com.ntucap.itcm.classes.ITCMUserPreference;
import com.ntucap.itcm.classes.events.ChooseImageEvent;
import com.ntucap.itcm.classes.events.PickerHideEvent;
import com.ntucap.itcm.classes.events.PickerShowEvent;
import com.ntucap.itcm.db.ITCMDB;
import com.ntucap.itcm.utils.BitmapCache;
import com.ntucap.itcm.utils.DataUtil;
import com.ntucap.itcm.utils.EventUtil;
import com.ntucap.itcm.utils.ITCMErrorListener;
import com.ntucap.itcm.utils.NetUtil;
import com.ntucap.itcm.utils.dialogs.ITCMDialogFragment;
import com.ntucap.itcm.views.CircularNetworkImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;

/**
 * Created by ProgrammerYuan on 04/06/17.
 */

public class MeFragment extends ITCMFragment implements View.OnClickListener{

    private int mClothingSN, mFeedbackSN;

    private CircularNetworkImageView mIvAvatar;
    private TextView mTvUserName, mTvUserDetail;
    private LinearLayout mLLChangePassword, mLLClothing, mLLFeedbackSubmit, mLLLogout;
    private RelativeLayout mRLProfile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentId = EventUtil.SENDER_ID_ME;
        mNeedRegisterEventBust = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState,
                R.layout.fragment_me);
        if(!mInitialized) {
            mInitialized = true;
            mIvAvatar = (CircularNetworkImageView) mInflatedView.findViewById(R.id.iv_avatar_frag_me);
            mTvUserName = (TextView) mInflatedView.findViewById(R.id.tv_username_frag_me);
            mTvUserDetail = (TextView) mInflatedView.findViewById(R.id.tv_user_detail_frag_me);
            mLLChangePassword = (LinearLayout) mInflatedView.findViewById(R.id.ll_change_pw_frag_me);
            mLLClothing = (LinearLayout) mInflatedView.findViewById(R.id.ll_clothing_frag_me);
            mLLFeedbackSubmit = (LinearLayout) mInflatedView.findViewById(R.id.ll_feedback_frag_me);
            mLLLogout = (LinearLayout) mInflatedView.findViewById(R.id.ll_logout_frag_me);
            mRLProfile = (RelativeLayout) mInflatedView.findViewById(R.id.rl_profile_frag_me);
            bindListeners();
            applyData();
        }
        return mInflatedView;
    }

    private void bindListeners() {
        mLLChangePassword.setOnClickListener(this);
        mLLClothing.setOnClickListener(this);
        mLLFeedbackSubmit.setOnClickListener(this);
        mLLLogout.setOnClickListener(this);
        mRLProfile.setOnClickListener(this);

    }

    private void applyData() {
        ITCMUser user = ITCMApplication.getCurrentUser();
        mTvUserName.setText(user.getFullName());
        mTvUserDetail.setText(user.getCombinedDetail());
        user = null;
        NetUtil.getUserAvatar(new Response.Listener<byte[]>() {
            @Override
            public void onResponse(byte[] response) {
                mIvAvatar.setImageBitmap(BitmapFactory.decodeByteArray(response, 0, response.length));
            }
        }, ITCMErrorListener.getInstance());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceived(PickerHideEvent event) {
        switch (event.getEventId()) {
            case EventUtil.EVENT_ID_CLOTHING_FRAG_ME:
                getBandData().setClothingIndex(event.getResponseValue());
                break;
            case EventUtil.EVENT_ID_SUBMITTING_FRAG_ME:
                getBandData().setDates(DataUtil.getYMDTFromDate(new Date()));
                getBandData().setFeedBack(event.getResponseValue());
                ITCMUserPreference userPreference = ITCMDB.getCurrentUserPreference();
                if(userPreference != null) {
                    getBandData().setUserPreferenceRange(
                            userPreference.getComfortLevelTo(),
                            userPreference.getComfortLevelFrom()
                    );
                }
                NetUtil.uploadUserBandData(getBandData().getParams(), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        toast("Band Data Upload Succeed");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        toast("Something wrong with your network");
                    }
                });
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = null;
        switch (id) {
            case R.id.ll_change_pw_frag_me:
                intent = new Intent(getActivity(), ChangePasswordActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.ll_clothing_frag_me:
                EventBus.getDefault().post(
                        new PickerShowEvent(
                                EventUtil.EVENT_ID_CLOTHING_FRAG_ME,
                                R.array.str_array_clothing,
                                mClothingSN)
                );
                break;
            case R.id.ll_feedback_frag_me:
                EventBus.getDefault().post(
                        new PickerShowEvent(
                                EventUtil.EVENT_ID_SUBMITTING_FRAG_ME,
                                R.array.str_array_feedback,
                                mFeedbackSN)
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
                        Log.d("LOGTAGDIALOG", "Confirm");
                        if (ITCMDB.signout() > 0) {
                            ITCMApplication.setCurrentUser(null);
                            Intent intent = new Intent(getActivity(), EntranceActivity.class);
                            Bundle data = new Bundle();
                            data.putBoolean("autologin", false);
                            intent.putExtras(data);
                            getActivity().startActivity(intent);
                            getActivity().finish();
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show(getActivity().getSupportFragmentManager(), "");
                break;
            case R.id.rl_profile_frag_me:
                EventBus.getDefault().post(new ChooseImageEvent());
//                intent = new Intent(getActivity(), UserProfileActivity.class);
//                getActivity().startActivity(intent);
                break;
        }
    }
}
