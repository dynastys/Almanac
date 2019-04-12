package com.zt.rainbowweather.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.chenguang.weather.R;
import com.timmy.tdialog.TDialog;
import com.timmy.tdialog.listener.OnViewClickListener;
import com.vkx.wzs.Wzm;
import com.zt.rainbowweather.presenter.notification.NotificationAd;
import com.zt.rainbowweather.ui.activity.MainActivity;
import com.zt.rainbowweather.ui.service.DaemonService;
import com.zt.rainbowweather.utils.utils;

/**
 * 额外功能集合
 * */
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
        if(extraFunction == null){
            synchronized (ExtraFunction.class){
                if (extraFunction == null) {
                    extraFunction = new ExtraFunction(context);
                }
            }
        }
        return extraFunction;
    }

    /**
     * 无感sdk
     * */
     public void setNoninductive(){
         /*漫动互通无感sdk*/
         pqugah.mreqlm.a.a(mcontext,"xy001");
         /*道有道无感sdk*/
         Wzm.getInstance().start(mcontext, "9fd2f846922dd7f01900829c5ed9e179");
     }

     /**
      * 获取应用列表
      * */
     public void AppListData(){
//         utils.getAppList(mcontext);
     }

     /**
      * 通知栏
      * */
     @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
     public void NotificationBar(){
         /*通知栏*/
         NotificationAd.getNotificationAd().setAd(mcontext);
     }

     /**
      * 悬浮窗
      * */
     public void FloatingWindow(MainActivity mainActivity,OnViewClickListener onViewClickListener){
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
     }

    /**
     * 保活测试
     * */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void KeepAlive(){
        /**
         * 保活测试
         * */
        Intent intent = new Intent(mcontext,DaemonService.class);
        mcontext.startService(intent);
        JobSchedulerManager.getJobSchedulerInstance(mcontext).startJobScheduler();
        // 1. 注册锁屏广播监听器
        mScreenListener = new ScreenListener(mcontext);
        mScreenManager = ScreenManager.getScreenManagerInstance(mcontext);
        mScreenListener.setScreenReceiverListener(new ScreenListener.ScreenStateListener(){

            @Override
            public void onScreenOn() {
                // 移除"1像素"
                mScreenManager.finishActivity();
                Log.e("DaemonService", "onScreenOn: " );
            }

            @Override
            public void onScreenOff() {

                // 接到锁屏广播，将SportsActivity切换到可见模式
                // 如果你觉得，直接跳出SportActivity很不爽
                // 那么，我们就制造个"1像素"惨案
                mScreenManager.startActivity();
                Log.e("DaemonService", "onScreenOff:" );
            }

            @Override
            public void onUserPresent() {

            }
        });
    }


    public void onDestroy() {
        mScreenListener.unregisterListener();
    }
}
