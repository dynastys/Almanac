package com.zt.rainbowweather.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class SystemUtils {
    private static String PACKAGE_NAME = "com.chenguang.weather";
    public static boolean isAppAlive(Context context){
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        boolean isAppRunning = false;
         //100表示取的最大的任务数，info.topActivity表示当前正在运行的Activity，info.baseActivity表系统后台有此进程在运行
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(PACKAGE_NAME) || info.baseActivity.getPackageName().equals(PACKAGE_NAME)) {
                isAppRunning = true;
                Log.i("ActivityService isRun()",info.topActivity.getPackageName() + " info.baseActivity.getPackageName()="+info.baseActivity.getPackageName());
                break;
            }
        }
        Log.i("ActivityService isRun()", "com.ad 程序  ...isAppRunning......"+isAppRunning);
        return isAppRunning;
    }
}
