package com.zt.rainbowweather.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import com.xy.xylibrary.ui.adapter.GradientTabStripAdapter;
import com.zt.rainbowweather.ui.fragment.HomeFragment;
import com.zt.rainbowweather.ui.fragment.NewsFragment;
import com.zt.rainbowweather.ui.fragment.ServeFragment;
import com.chenguang.weather.R;

public class MyGradientTabStripAdapter extends GradientTabStripAdapter {

    public MyGradientTabStripAdapter(FragmentManager fm, ViewPager tabVp) {
        super(fm, tabVp);
    }


    @Override
    public boolean isTabTagEnable(int position) {
        return false;
    }

    @Override
    public String getTabTag(int position) {
        return null;
    }


    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            default:
            case 0:
                return "首页";
            case 1:
                return "运势";
            case 2:
                return "资讯";
            case 3:
                return "服务";
        }
    }

    @Override
    public Drawable getNormalDrawable(int position, Context context) {
        switch (position) {
            default:
            case 0:
                return ContextCompat.getDrawable(context, R.drawable.ic_filter_drama_no);
            case 1:
                return ContextCompat.getDrawable(context, R.drawable.ic_looks_black);
            case 2:
                return ContextCompat.getDrawable(context, R.drawable.ic_fiber_new_no);
            case 3:
                return ContextCompat.getDrawable(context, R.drawable.ic_apps_no);
        }
    }

    @Override
    public Drawable getSelectedDrawable(int position, Context context) {
        switch (position) {
            default:
            case 0:
                return ContextCompat.getDrawable(context,  R.drawable.ic_filter_drama);
            case 1:
                return ContextCompat.getDrawable(context,  R.drawable.ic_looks);
            case 2:
                return ContextCompat.getDrawable(context,  R.drawable.ic_fiber_new);
            case 3:
                return ContextCompat.getDrawable(context,  R.drawable.ic_apps);
        }
    }

   public NewsFragment newsFragment;
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            default:
            case 0:
                return new HomeFragment();
            case 1:
                return new NewsFragment("http://ssp.xuanad.com/page/ec7f6602-4807-4642-b68d-cb7fdf1231f4");
            case 2:
//                if(newsFragment == null){
//                    newsFragment = new NewsFragment();
//                }
                return new NewsFragment();
            case 3:
                return new ServeFragment();
        }
    }
}
