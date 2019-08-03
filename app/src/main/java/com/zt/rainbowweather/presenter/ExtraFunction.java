package com.zt.rainbowweather.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import com.zt.weather.R;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.timmy.tdialog.TDialog;
import com.timmy.tdialog.listener.OnViewClickListener;
import com.xy.xylibrary.utils.SaveShare;
import com.zt.rainbowweather.entity.City;
import com.zt.rainbowweather.presenter.notification.NotificationAd;
import com.zt.rainbowweather.ui.activity.MainActivity;
import com.zt.rainbowweather.ui.service.DaemonService;
import com.zt.rainbowweather.ui.service.FloatingService;
import com.zt.rainbowweather.utils.ConstUtils;
import com.zt.rainbowweather.utils.SPUtils;
import com.zt.rainbowweather.utils.utils;
import org.litepal.LitePal;
import java.util.List;

/**
 * 额外功能集合
 */
public class ExtraFunction {

    private static ExtraFunction extraFunction;
    private static Activity mcontext;
    private ScreenListener mScreenListener;
    private ScreenManager mScreenManager;

    public ExtraFunction(Activity context) {
        mcontext = context;
    }

    public static ExtraFunction getExtraFunction(Activity context) {
        mcontext = context;
        if (extraFunction == null) {
            synchronized (ExtraFunction.class) {
                if (extraFunction == null) {
                    extraFunction = new ExtraFunction(context);
                }
            }
        }
        return extraFunction;
    }

    /**
     * 无感sdk
     */
    public void setNoninductive() {

    }

    /**
     * 获取应用列表
     */
    public void AppListData() {
//         utils.getAppList(mcontext);
    }

    /**
     * 通知栏
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void NotificationBar() {
        try {
            /*通知栏*/
            NotificationAd.getNotificationAd().setAd(mcontext);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*应用外弹窗*/
    private void floatWindow(MainActivity mainActivity, String windows, OnViewClickListener onViewClickListener) {
        try {
            if (!TextUtils.isEmpty(windows) && (System.currentTimeMillis() - Long.parseLong(windows)) > 24 * 60 * 60 * 1000) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(mcontext)) {
                        extraFunction.FloatingWindow(mainActivity, onViewClickListener);
                    }
                }
            }
            SaveShare.saveValue(mcontext, "windows", System.currentTimeMillis() + "");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * 悬浮窗
     */
    public void FloatingWindow(MainActivity mainActivity, OnViewClickListener onViewClickListener) {

        try {
            /*悬浮窗*/
            new TDialog.Builder(mainActivity.getSupportFragmentManager())
                    .setLayoutRes(R.layout.notification_dialog)    //设置弹窗展示的xml布局
                    .setScreenWidthAspect(mcontext, 0.8f)   //设置弹窗宽度(参数aspect为屏幕宽度比例 0 - 1f)
                    .setScreenHeightAspect(mcontext, 0.3f)  //设置弹窗高度(参数aspect为屏幕宽度比例 0 - 1f)
                    .setCancelableOutside(false)
                    .setOnBindViewListener(viewHolder -> {
                    })
                    .setOnDismissListener(dialog -> {
                    })
                    .addOnClickListener(R.id.dialog_confirm_tv, R.id.dialog_cancel_tv)
                    .setOnViewClickListener(onViewClickListener)
                    .setDimAmount(0.6f)     //设置弹窗背景透明度(0-1f)
                    .setDialogAnimationRes(R.style.animate_dialog)  //设置弹窗动画
                    .create()   //创建TDialog
                    .show();    //展示
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保活测试
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void KeepAlive() {
        try {
            /**
             * 保活测试
             * */
            Intent intent = new Intent(mcontext, DaemonService.class);
            mcontext.startService(intent);
            JobSchedulerManager.getJobSchedulerInstance(mcontext).startJobScheduler();
            // 1. 注册锁屏广播监听器
            mScreenListener = new ScreenListener(mcontext);
            mScreenManager = ScreenManager.getScreenManagerInstance(mcontext);
            mScreenListener.setScreenReceiverListener(new ScreenListener.ScreenStateListener() {

                @Override
                public void onScreenOn() {
                    // 移除"1像素"
                    mScreenManager.finishActivity();
                    Log.e("DaemonService", "onScreenOn: ");
                }

                @Override
                public void onScreenOff() {

                    // 接到锁屏广播，将SportsActivity切换到可见模式
                    // 如果你觉得，直接跳出SportActivity很不爽
                    // 那么，我们就制造个"1像素"惨案
                    mScreenManager.startActivity();
                    Log.e("DaemonService", "onScreenOff:");
                }

                @Override
                public void onUserPresent() {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<City> getCityFromSP() {
        try {
            String json = SPUtils.getInstance(ConstUtils.SP_FILE_NAME).getString("addresses");
            if (!TextUtils.isEmpty(json)) {
                return new Gson().fromJson(json, new TypeToken<List<City>>() {
                }.getType());
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void onStop(Activity activity) {

        try {
            List<City> cities = getCityFromSP();
            if (cities != null) {
                for (int j = 0; j < cities.size(); j++) {
                    List<City> citie = LitePal.where("name=?", cities.get(j).name).find(City.class);
                    if (citie == null || citie.size() == 0) {
                        cities.get(j).save();
                    }
                }
            }
            if (!utils.isForeground(activity)) {
                activity.startService(new Intent(activity, FloatingService.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        mScreenListener.unregisterListener();
    }
}
