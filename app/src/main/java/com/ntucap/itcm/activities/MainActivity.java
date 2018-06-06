package com.ntucap.itcm.activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dpizarro.uipicker.library.picker.PickerUI;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.ntucap.itcm.R;
import com.ntucap.itcm.classes.events.BandConnectEvent;
import com.ntucap.itcm.classes.events.ChooseImageEvent;
import com.ntucap.itcm.classes.events.PickerHideEvent;
import com.ntucap.itcm.classes.events.PickerShowEvent;
import com.ntucap.itcm.classes.events.RefreshEvent;
import com.ntucap.itcm.classes.events.ShowToastEvent;
import com.ntucap.itcm.classes.events.UploadPreferenceEvent;
import com.ntucap.itcm.fragments.EnvironmentalFragment;
import com.ntucap.itcm.fragments.ITCMFragment;
import com.ntucap.itcm.fragments.MeFragment;
import com.ntucap.itcm.fragments.PreferenceFragment;
import com.ntucap.itcm.fragments.RewardsFragment;
import com.ntucap.itcm.utils.DataUtil;
import com.ntucap.itcm.utils.EventUtil;
import com.ntucap.itcm.utils.ITCMImageLoader;
import com.ntucap.itcm.utils.adapters.PagerFragmentAdapter;
import com.ntucap.itcm.utils.dialogs.ITCMDialogFragment;
import com.ntucap.itcm.utils.dialogs.ITCMLoadingDialog;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainActivity extends ITCMActivity
        implements ViewPager.OnPageChangeListener, OnTabSelectListener,
        View.OnTouchListener, PickerUI.PickerUIItemClickListener, View.OnClickListener{

    private static final String LOG_TAG = "MAINACTIVITY";

    private int mPickerEventId;
    private int mCurrentPickerIndex = 0;

    private TextView mTvTitle;
    private ImageView mMask, mIvNavIcon;
    private PickerUI mPicker;
    private ViewPager mViewpager;
    private BottomBar mBottomBar;
    private PagerFragmentAdapter mAdapter;
    private ArrayList<ITCMFragment> mFragments;

    private static final int ALPHA_ANIM_DURATION = 400;
    private static final int IMAGE_PICKER = 1711;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvTitle = (TextView) findViewById(R.id.tv_title_act_main);
        mViewpager = (ViewPager) findViewById(R.id.viewPager);
        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);
        mIvNavIcon = (ImageView) findViewById(R.id.iv_nav_act_main);
        mMask = (ImageView) findViewById(R.id.iv_mask_act_main);
        mPicker = (PickerUI) findViewById(R.id.picker_ui_act_main);
        for(int i = 0; i < 3; i ++)
            Log.d(LOG_TAG, DataUtil.getYMDTFromDate(new Date())[i]);
        init();
        bindListeners();
    }

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

    private void init() {
        mFragments = new ArrayList<>();
        mFragments.add(new EnvironmentalFragment());
        mFragments.add(new PreferenceFragment());
        mFragments.add(new RewardsFragment());
        mFragments.add(new MeFragment());
        mAdapter = new PagerFragmentAdapter(getSupportFragmentManager(), mFragments);
        mViewpager.setOffscreenPageLimit(3);
        mViewpager.setAdapter(mAdapter);

        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new ITCMImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(1);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.CIRCLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }

    private void bindListeners() {
        mViewpager.addOnPageChangeListener(this);
        mPicker.setOnClickItemPickerUIListener(this);
        mTvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ITCMLoadingDialog dialog = ITCMLoadingDialog.newInstance(null);
                dialog.show(getSupportFragmentManager(),"test");
            }
        });
        mBottomBar.setOnTabSelectListener(this);
        mMask.setOnTouchListener(this);
        mIvNavIcon.setOnClickListener(this);
    }

    private void controlMask(boolean show, int duration) {
        ObjectAnimator animator;
        if(show) {
            animator = ObjectAnimator.ofFloat(mMask, "alpha", 0.0f, 1.0f);
        } else {
            animator = ObjectAnimator.ofFloat(mMask, "alpha", 1.0f, 0.0f);
        }
        animator.setDuration(duration);
        animator.start();
    }

    private void controlPicker(boolean show, int slideNumber) {
        controlMask(show, ALPHA_ANIM_DURATION);
        if(show) mPicker.slide(slideNumber);
        else mPicker.slide();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceived(PickerShowEvent event) {
        if(mPickerEventId != event.getEventId()) {
            mCurrentPickerIndex = event.getPickerSlideNumber();
            mPickerEventId = event.getEventId();
        }
        mPicker.setItems(this, Arrays.asList(getResources().getStringArray(event.getArrayResId())));
        controlPicker(true, mCurrentPickerIndex);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceived(ShowToastEvent event) {
        int senderId = event.getSenderId();
        switch (senderId) {
            case EventUtil.SENDER_ID_ENVIRONMENTAL:
            case EventUtil.SENDER_ID_PREFERENCE:
            case EventUtil.SENDER_ID_REWARD:
            case EventUtil.SENDER_ID_ME:
                if(mViewpager.getCurrentItem() == senderId)
                    toastWithDuration(event.getMessage(), event.getDuration());
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceived(ChooseImageEvent event) {
        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, IMAGE_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                images.size();
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_mask_act_main:
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if (mPicker.isPanelShown()) controlPicker(false, 0);
                }
                return mPicker.isPanelShown();
        }
        return false;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mBottomBar.selectTabAtPosition(position, true);
        String title = "";
        int imageResId = 0;
        switch (position) {
            case 0:
                title = "Dashboard";
                imageResId = R.drawable.ic_pair_band;
                break;
            case 1:
                title = "Preference";
                imageResId = R.drawable.ic_upload;
                break;
            case 2:
                title = "Reward";
                imageResId = R.drawable.ic_refresh;
                break;
            case 3:
                title = "Me";
                imageResId = R.drawable.ic_settings;
                break;
        }
        mTvTitle.setText(title);
        mIvNavIcon.setImageResource(imageResId);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabSelected(@IdRes int tabId) {
        int tab_id = -1;
        switch (tabId) {
            case R.id.tab_dashboard:
                tab_id = 0;
                break;
            case R.id.tab_preference:
                tab_id = 1;
                break;
            case R.id.tab_reward:
                tab_id = 2;
                break;
            case R.id.tab_me:
                tab_id = 3;
                break;
        }
        mViewpager.setCurrentItem(tab_id);
    }

    @Override
    public void onItemClickPickerUI(int which, int position, String valueResult) {
        if (mCurrentPickerIndex == position) {
            controlPicker(false, mCurrentPickerIndex);
            PickerHideEvent event = new PickerHideEvent(mPickerEventId, valueResult, position);
            switch (mPickerEventId) {
                case EventUtil.EVENT_ID_RANGE_FROM_FRAG_PREF:
                    event.setResponseValue(3 - position);
                    break;
                case EventUtil.EVENT_ID_RANGE_TO_FRAG_PREF:
                    event.setResponseValue(-position);
                    break;
                case EventUtil.EVENT_ID_AIRTEMP_FRAG_PREF:
                case EventUtil.EVENT_ID_HUMID_FRAG_PREF:
                    event.setResponseValue(DataUtil.extractOneNumberFromString(valueResult));
                    break;
                case EventUtil.EVENT_ID_SUBMITTING_FRAG_ME:
                    event.setResponseValue(3 - position);
                    break;
                case EventUtil.EVENT_ID_CLOTHING_FRAG_ME:
                default:
                    break;
            }
            EventBus.getDefault().post(event);
        } else {
            mCurrentPickerIndex = position;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = null;
        switch (id) {
            case R.id.iv_nav_act_main:
                switch (mViewpager.getCurrentItem()) {
                    case 0:
                        EventBus.getDefault().post(new BandConnectEvent());
                        break;
                    case 1:
                        EventBus.getDefault().post(new UploadPreferenceEvent());
                        break;
                    case 2:
                        EventBus.getDefault().post(new RefreshEvent());
                        break;
                    case 3:
                        intent = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        break;
                }
        }
    }
}
