package com.zt.rainbowweather.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import com.xy.xylibrary.ui.adapter.GradientTabStripAdapter;
import com.yilan.sdk.ui.littlevideo.LittleVideoFragment;
import com.zt.rainbowweather.ui.fragment.AlmanacFragment;
import com.zt.rainbowweather.ui.fragment.HomeFragment;
import com.zt.rainbowweather.ui.fragment.NewsFragment;
import com.zt.rainbowweather.ui.fragment.ServeFragment;
import com.zt.weather.R;
import com.zt.rainbowweather.utils.ConstUtils;

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
        if(ConstUtils.take_a_look){
            return 4;
        }
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(ConstUtils.take_a_look){
            switch (position) {
                default:
                case 0:
                    return "首页";
                case 1:
                    return "运势";
                case 2:
                    return "看一看";
                case 3:
                    return "服务";
            }
        }else{
            switch (position) {
                default:
                case 0:
                    return "首页";
                case 1:
                    return "运势";
                case 2:
                    return "服务";
            }
        }

    }

    @Override
    public Drawable getNormalDrawable(int position, Context context) {
        try {
            if(ConstUtils.take_a_look){
                switch (position) {
                    default:
                    case 0:
                        return ContextCompat.getDrawable(context, R.mipmap.ic_filter_drama_no);
                    case 1:
                        return ContextCompat.getDrawable(context, R.mipmap.ic_looks_black);
                    case 2:
                        return ContextCompat.getDrawable(context, R.mipmap.ic_fiber_new_no);
                    case 3:
                        return ContextCompat.getDrawable(context, R.mipmap.ic_apps_no);
                }
            }else{
                switch (position) {
                    default:
                    case 0:
                        return ContextCompat.getDrawable(context, R.mipmap.ic_filter_drama_no);
                    case 1:
                        return ContextCompat.getDrawable(context, R.mipmap.ic_looks_black);
                    case 2:
                        return ContextCompat.getDrawable(context, R.mipmap.ic_apps_no);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ContextCompat.getDrawable(context, R.mipmap.ic_apps_no);
        }

    }

    @Override
    public Drawable getSelectedDrawable(int position, Context context) {
        try {
            if(ConstUtils.take_a_look){
                switch (position) {
                    default:
                    case 0:
                        return ContextCompat.getDrawable(context,  R.mipmap.ic_filter_drama);
                    case 1:
                        return ContextCompat.getDrawable(context,  R.mipmap.ic_looks);
                    case 2:
                        return ContextCompat.getDrawable(context,  R.mipmap.ic_fiber_new);
                    case 3:
                        return ContextCompat.getDrawable(context,  R.mipmap.ic_apps);
                }
            }else{
                switch (position) {
                    default:
                    case 0:
                        return ContextCompat.getDrawable(context,  R.mipmap.ic_filter_drama);
                    case 1:
                        return ContextCompat.getDrawable(context,  R.mipmap.ic_looks);
                    case 2:
                        return ContextCompat.getDrawable(context,  R.mipmap.ic_apps);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ContextCompat.getDrawable(context,  R.mipmap.ic_apps);
        }

    }

   public NewsFragment newsFragment;
    @Override
    public Fragment getItem(int position){
        if(ConstUtils.take_a_look){
            switch (position){
                default:
                case 0:
                    return new HomeFragment();
                case 1:
                    return new AlmanacFragment();
                case 2:
                    //        小视频
//                    LittleVideoFragment littleVideoFragment = LittleVideoFragment.newInstance();
                    return  LittleVideoFragment.newInstance();
//                    return new NewsFragment("http://ssp.xuanad.com/page/f1e294cc-8546-438b-b0af-801ca170b2d0");
                case 3:
                    return new ServeFragment();
            }
        }else{
            switch (position){
                default:
                case 0:
                    return new HomeFragment();
                case 1:
                    return new AlmanacFragment();
                case 2:
                    return new ServeFragment();
            }
        }

    }
}
