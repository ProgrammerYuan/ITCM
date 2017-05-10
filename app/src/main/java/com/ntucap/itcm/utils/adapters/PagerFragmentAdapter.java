package com.ntucap.itcm.utils.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ntucap.itcm.fragments.ITCMFragment;

import java.util.ArrayList;

/**
 * Created by ProgrammerYuan on 18/04/17.
 */

public class PagerFragmentAdapter extends FragmentStatePagerAdapter {

    private ArrayList<ITCMFragment> fragments = null;

    public PagerFragmentAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        if(fragments == null) fragments = new ArrayList<>();
    }

    public PagerFragmentAdapter(FragmentManager fragmentManager, ArrayList<ITCMFragment> fragments){
        this(fragmentManager);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        position = Math.max(0,Math.min(position, getCount() - 1));
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
