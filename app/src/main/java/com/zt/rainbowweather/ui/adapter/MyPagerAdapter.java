package com.zt.rainbowweather.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.xy.xylibrary.base.BaseFragment;

import java.util.ArrayList;

public class MyPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<BaseFragment> mFragments = new ArrayList<>();
    private String[] mTitles_3 = {"趋势", "列表"};

    public MyPagerAdapter(FragmentManager fm,ArrayList<BaseFragment> mFragments) {
        super(fm);
        this.mFragments = mFragments;

    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles_3[position];
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }
}
