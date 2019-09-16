package com.zt.rainbowweather.presenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zt.weather.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xy.xylibrary.utils.Utils;
import com.zt.rainbowweather.ui.fragment.HomeFragment;
import com.zt.rainbowweather.utils.Util;
import com.zt.rainbowweather.view.MiuiWeatherView;
import com.zt.rainbowweather.view.TranslucentActionBar;

/**
 * 天气页面逻辑
 * */
public class WeatherLogic {

    private static WeatherLogic weatherLogic;
    private Activity activity;
    private Drawable[] drawable = {null};
    private boolean ISBG = true;

    public static WeatherLogic getWeatherLogic() {
        if (weatherLogic == null) {
            synchronized (WeatherLogic.class){
                if (weatherLogic == null) {
                    weatherLogic = new WeatherLogic();
                }
            }
        }
        return weatherLogic;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initData(Activity activity, RelativeLayout rlHeader, RelativeLayout relWetherBg, TextView shopListBar, TextView listBar, MiuiWeatherView weather, HomeFragment homeFragments, SmartRefreshLayout refreshLayout, OnRefreshListener onRefreshListener, OnMultiPurposeListener onMultiPurposeListener) {
        this.activity = activity;
        try {
            ViewGroup.LayoutParams para1;
            para1 = rlHeader.getLayoutParams();
            para1.height = Util.ScreenHeight(activity) * 4 / 5;
            rlHeader.setLayoutParams(para1);
//        relWetherBg.setBackgroundResource(R.mipmap.day);
            ViewGroup.LayoutParams layoutParamsBar = shopListBar.getLayoutParams();
            layoutParamsBar.height = Utils.getStatusBarHeight(activity) + Utils.dip2px(activity, 50);
            shopListBar.setLayoutParams(layoutParamsBar);
            ViewGroup.LayoutParams layoutParams = listBar.getLayoutParams();
            layoutParams.height = Utils.getStatusBarHeight(activity);
            listBar.setLayoutParams(layoutParams);
            listBar.setVisibility(View.GONE);
            //解决view左右滑动有view_pager左右滑动的冲突
            weather.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        homeFragments.setCanSlipping(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        homeFragments.setCanSlipping(false);
                        break;
                }
                return false;
            });
            //刷新
//        refreshLayout.setOnRefreshListener(onRefreshListener);
            refreshLayout.setOnMultiPurposeListener(onMultiPurposeListener);
            refreshLayout.setPrimaryColorsId(R.color.blue_light2);
            refreshLayout.setEnableOverScrollDrag(false);
            refreshLayout.setEnableOverScrollBounce(false);
//            refreshLayout.autoRefresh();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*动态标题栏*/
    @SuppressLint("ResourceAsColor")
    public void onTranslucentChanged(int transAlpha, TextView wetherBg,RelativeLayout relWetherBg,TranslucentActionBar DetailsActionbar,HomeFragment homeFragments) {

    }
}
