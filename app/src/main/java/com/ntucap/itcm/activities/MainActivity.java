package com.ntucap.itcm.activities;

import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ntucap.itcm.R;
import com.ntucap.itcm.fragments.EnvironmentalFragment;
import com.ntucap.itcm.fragments.ITCMFragment;
import com.ntucap.itcm.fragments.PreferenceFragment;
import com.ntucap.itcm.fragments.RewardsFragment;
import com.ntucap.itcm.utils.adapters.PagerFragmentAdapter;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements ViewPager.OnPageChangeListener, OnTabSelectListener{

    private ViewPager mViewpager;
    private PagerFragmentAdapter mAdapter;
    private ArrayList<ITCMFragment> mFragments;
    private BottomBar mBottomBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewpager = (ViewPager) findViewById(R.id.viewPager);
        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);
        init();
        bindListeners();
    }

    private void init() {
        mFragments = new ArrayList<>();
        mFragments.add(new EnvironmentalFragment());
        mFragments.add(new PreferenceFragment());
        mFragments.add(new RewardsFragment());
        mFragments.add(new EnvironmentalFragment());
        mAdapter = new PagerFragmentAdapter(getSupportFragmentManager(), mFragments);
        mViewpager.setAdapter(mAdapter);
    }

    private void bindListeners() {
        mViewpager.addOnPageChangeListener(this);
        mBottomBar.setOnTabSelectListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mBottomBar.selectTabAtPosition(position, true);
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
}
