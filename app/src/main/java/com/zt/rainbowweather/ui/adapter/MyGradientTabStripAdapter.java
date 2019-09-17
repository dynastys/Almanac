package com.zt.rainbowweather.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;

import com.xy.xylibrary.ui.adapter.GradientTabStripAdapter;
import com.xy.xylibrary.ui.fragment.task.TaskFragment;
import com.xy.xylibrary.utils.SaveShare;
import com.xy.xylibrary.utils.Utils;
import com.yilan.sdk.ui.littlevideo.LittleVideoFragment;
import com.zt.rainbowweather.ui.fragment.AlmanacFragment;
import com.zt.rainbowweather.ui.fragment.HomeFragment;
import com.zt.rainbowweather.ui.fragment.ServeFragment;
import com.zt.weather.R;
import com.zt.rainbowweather.utils.ConstUtils;

public class MyGradientTabStripAdapter extends GradientTabStripAdapter {

    private Context context;
    public MyGradientTabStripAdapter(FragmentManager fm, ViewPager tabVp) {
        super(fm, tabVp);
    }

    @Override
    public boolean isTabTagEnable(int position) {
       switch (position){
           case 1:
               return false;
           case 2:
               if(context != null && !TextUtils.isEmpty(SaveShare.getValue(context, "userId"))){
                   String JB = SaveShare.getValue(context,"JB");
                   if(!TextUtils.isEmpty(JB) && JB.equals(Utils.getOldDate(0))){
                       return false;
                   }
               }
//               SaveShare.saveValue(context,"JB", Utils.getOldDate(0));
               return true;
           case 3:
               if(context != null){
                   String vidoe = SaveShare.getValue(context,"video");
                   if(!TextUtils.isEmpty(vidoe) && vidoe.equals(Utils.getOldDate(0))){
                       return false;
                   }
               }

               return true;
           case 4:
               return false;
       }
        return false;
    }

    @Override
    public String getTabTag(int position) {
        return null;
    }


    @Override
    public int getCount() {
        if(ConstUtils.take_a_look){
            return 5;
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
                    return "赚金币";
                case 3:
                    return "视频";
                case 4:
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
            this.context = context;
            if(ConstUtils.take_a_look){
                switch (position) {
                    default:
                    case 0:
                        return ContextCompat.getDrawable(context, R.mipmap.ic_filter_drama_no);
                    case 1:
                        return ContextCompat.getDrawable(context, R.mipmap.ic_looks_black);
                    case 2:
                        return ContextCompat.getDrawable(context, R.mipmap.task_no);
                    case 3:
                        return ContextCompat.getDrawable(context, R.mipmap.ic_fiber_new_no);
                    case 4:
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
                        return ContextCompat.getDrawable(context,  R.mipmap.task_se);
                    case 3:
                        return ContextCompat.getDrawable(context,  R.mipmap.ic_fiber_new);
                    case 4:
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
                    return new TaskFragment();
                case 3:
                    //        小视频
                     return  LittleVideoFragment.newInstance();
                 case 4:
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
