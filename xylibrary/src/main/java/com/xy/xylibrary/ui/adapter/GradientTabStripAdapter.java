package com.xy.xylibrary.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 import android.support.v4.view.ViewPager;
import android.util.Log;

import am.widget.gradienttabstrip.GradientTabStrip;

/**
 * GradientTabStripAdapter
 * Created by Alex on 2016/5/19.
 */
public abstract class GradientTabStripAdapter extends FragmentPagerAdapter implements
        GradientTabStrip.GradientTabAdapter {
    ViewPager tabVp;
    public GradientTabStripAdapter(FragmentManager fm, ViewPager tabVp) {
        super(fm);
        this.tabVp = tabVp;
    }
//
//    @Override
//    public int getCount() {
//        return 4;
//    }

//    public abstract Drawable getTabNormalDrawable(int position, Context context);
//
//    public abstract  Drawable getTabSelectedDrawable(int position, Context context);

    public abstract boolean isTabTagEnable(int position);

    public abstract String getTabTag(int position);

//    @Override
//    public Fragment getItem(int position) {
//        String title = getPageTitle(position).toString();
//        switch (position) {
//            default:
//            case 0:
//                return new ShoppingMallFragment();
//            case 1:
//                return new GenreFragment();
//            case 2:
//                return new CollectFragment();
//            case 3:
//                return new MyFragment();
//        }
//    }
//
//    @Override
//    public CharSequence getPageTitle(int position) {
//        switch (position) {
//            default:
//            case 0:
//                return "首页";
//            case 1:
//                return "分类";
//            case 2:
//                return "收藏";
//            case 3:
//                return "我的";
//        }
//    }

    @Override
    public Drawable getNormalDrawable(int position, Context context) {
       return null;

    }

    @Override
    public Drawable getSelectedDrawable(int position, Context context) {
      return null;
    }

    @Override
    public boolean isTagEnable(int position) {
         return isTabTagEnable(position);
    }

    @Override
    public String getTag(int position) {
       return getTabTag(position);
    }
}
