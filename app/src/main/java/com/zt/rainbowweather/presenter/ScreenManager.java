package com.zt.rainbowweather.presenter;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zt.rainbowweather.ui.activity.MainActivity;
import com.zt.rainbowweather.ui.activity.SinglePixelActivity;

import java.lang.ref.WeakReference;
import java.util.List;

/**1像素管理类
 *
 * Created by jianddongguo on 2017/7/8.
 */
public class ScreenManager {
    private static final String TAG = "ScreenManager";
    private Context mContext;
    private static ScreenManager mSreenManager;
    // 使用弱引用，防止内存泄漏
    private WeakReference<Activity> mActivityRef;

    private ScreenManager(Context mContext){
        this.mContext = mContext;
    }

    // 单例模式
    public static ScreenManager getScreenManagerInstance(Context context){
        if(mSreenManager == null){
            mSreenManager = new ScreenManager(context);
        }
        return mSreenManager;
    }

    // 获得SinglePixelActivity的引用
    public void setSingleActivity(Activity mActivity){
        mActivityRef = new WeakReference<>(mActivity);
    }

    // 启动SinglePixelActivity
    public void startActivity(){
       if(isBackground(mContext)){
           Log.d(TAG,"准备启动SinglePixelActivity...");
           Intent intent = new Intent(mContext,SinglePixelActivity.class);
           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           mContext.startActivity(intent);
       }else{
           Intent intent = new Intent(mContext,MainActivity.class);
           mContext.startActivity(intent);
       }

    }

    // 结束SinglePixelActivity
    public void finishActivity(){

            Log.d(TAG,"准备结束SinglePixelActivity...");
        if(mActivityRef != null){
            Activity mActivity = mActivityRef.get();
            if(mActivity != null){
                mActivity.finish();
            }
        }
    }

    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                /*
                BACKGROUND=400 EMPTY=500 FOREGROUND=100
                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                 */
                Log.i(context.getPackageName(), "此appimportace ="
                        + appProcess.importance
                        + ",context.getClass().getName()="
                        + context.getClass().getName());
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(context.getPackageName(), "处于后台"
                            + appProcess.processName);
                    return true;
                } else {
                    Log.i(context.getPackageName(), "处于前台"
                            + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }
}