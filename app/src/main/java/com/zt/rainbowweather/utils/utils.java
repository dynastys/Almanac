package com.zt.rainbowweather.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import com.zt.rainbowweather.ui.service.DownLoadService;
import com.zt.xuanyin.controller.DownService;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *     author:
 *                                      ___           ___           ___         ___
 *         _____                       /  /\         /__/\         /__/|       /  /\
 *        /  /::\                     /  /::\        \  \:\       |  |:|      /  /:/
 *       /  /:/\:\    ___     ___    /  /:/\:\        \  \:\      |  |:|     /__/::\
 *      /  /:/~/::\  /__/\   /  /\  /  /:/~/::\   _____\__\:\   __|  |:|     \__\/\:\
 *     /__/:/ /:/\:| \  \:\ /  /:/ /__/:/ /:/\:\ /__/::::::::\ /__/\_|:|____    \  \:\
 *     \  \:\/:/~/:/  \  \:\  /:/  \  \:\/:/__\/ \  \:\~~\~~\/ \  \:\/:::::/     \__\:\
 *      \  \::/ /:/    \  \:\/:/    \  \::/       \  \:\  ~~~   \  \::/~~~~      /  /:/
 *       \  \:\/:/      \  \::/      \  \:\        \  \:\        \  \:\         /__/:/
 *        \  \::/        \__\/        \  \:\        \  \:\        \  \:\        \__\/
 *         \__\/                       \__\/         \__\/         \__\/
 *     blog  : http://blankj.com
 *     time  : 16/12/08
 *     desc  : utils about initialization
 * </pre>
 */
public final class utils {

    @SuppressLint("StaticFieldLeak")
    private static Application sApplication;

    private static final LinkedList<Activity> ACTIVITY_LIST = new LinkedList<>();

    private static Application.ActivityLifecycleCallbacks mCallbacks = new Application
            .ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            setTopActivity(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            setTopActivity(activity);
        }

        @Override
        public void onActivityResumed(Activity activity) {
            setTopActivity(activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            ACTIVITY_LIST.remove(activity);
        }
    };

    private utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Init utils.
     * <p>Init it in the class of Application.</p>
     *
     * @param context context
     */
    public static void init(@NonNull final Context context) {
        init((Application) context.getApplicationContext());
    }

    //当前应用是否处于前台
    public static boolean isForeground(Context context) {
        if (context != null) {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
            String currentPackageName = cn.getPackageName();
            if (!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName())) {
                return true;
            }
            return false;
        }
        return false;
    }
    /**
     * Init utils.
     * <p>Init it in the class of Application.</p>
     *
     * @param app application
     */
    public static void init(@NonNull final Application app) {
        utils.sApplication = app;
        utils.sApplication.registerActivityLifecycleCallbacks(mCallbacks);
    }

    /**
     * Return the context of Application object.
     *
     * @return the context of Application object
     */
    public static Application getApp() {
        if (sApplication != null) return sApplication;
        throw new NullPointerException("u should init first");
    }

    public static Application getContext() {
        if (sApplication != null) return sApplication;
        throw new NullPointerException("u should init first");
    }

    private static void setTopActivity(final Activity activity) {
        if (ACTIVITY_LIST.contains(activity)) {
            if (!ACTIVITY_LIST.getLast().equals(activity)) {
                ACTIVITY_LIST.remove(activity);
                ACTIVITY_LIST.addLast(activity);
            }
        } else {
            ACTIVITY_LIST.addLast(activity);
        }
    }

    static LinkedList<Activity> getActivityList() {
        return ACTIVITY_LIST;
    }

    static Context getTopActivityOrApp() {
        if (isAppForeground()) {
            Activity topActivity = getTopActivity();
            return topActivity == null ? utils.getApp() : topActivity;
        } else {
            return utils.getApp();
        }
    }

    static boolean isAppForeground() {
        ActivityManager am =
                (ActivityManager) utils.getApp().getSystemService(Context.ACTIVITY_SERVICE);
        if (am == null) return false;
        List<ActivityManager.RunningAppProcessInfo> info = am.getRunningAppProcesses();
        if (info == null || info.size() == 0) return false;
        for (ActivityManager.RunningAppProcessInfo aInfo : info) {
            if (aInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return aInfo.processName.equals(utils.getApp().getPackageName());
            }
        }
        return false;
    }

    static Activity getTopActivity() {
        if (!ACTIVITY_LIST.isEmpty()) {
            final Activity topActivity = ACTIVITY_LIST.getLast();
            if (topActivity != null) {
                return topActivity;
            }
        }
        // using reflect to get top activity
        try {
            @SuppressLint("PrivateApi")
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke
                    (null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            Map activities = (Map) activitiesField.get(activityThread);
            if (activities == null) return null;
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    Activity activity = (Activity) activityField.get(activityRecord);
                    utils.setTopActivity(activity);
                    return activity;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void Download(final Context context, final String url){
        try {
            // 创建一个下载存储位置
            String downloadStringName = url.substring(url.lastIndexOf("/") + 1, url.length());
            String downloadPath = ConstUtils.PATH + "/" + downloadStringName+System.currentTimeMillis()+".apk";
             Log.e("file", "DownReceiver: " + downloadPath);
             File file = new File(downloadPath);

            DownloadManager downloadManager = (DownloadManager) (context
                    .getSystemService(Context.DOWNLOAD_SERVICE));
            Uri downloadUri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setDestinationUri(Uri.fromFile(file));
            long id = downloadManager.enqueue(request);
//			//启动下载service
            Intent intentOne = new Intent(context, DownLoadService.class);
            intentOne.putExtra("file",downloadPath);
             context.startService(intentOne);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static String getHostAppId(Context appContext) throws IllegalArgumentException {
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = appContext.getPackageManager().getApplicationInfo(appContext.getPackageName(), PackageManager.GET_META_DATA);
            if(applicationInfo == null){
                throw new IllegalArgumentException(" get application info = null, has no meta data! ");
            }
            return applicationInfo.metaData.getString("XUANYIN_FileProvider");
        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalArgumentException(" get application info error! ", e);
        }
    }
    public static void doApk(Context context,String path) {
        try {
             Intent intent = new Intent(Intent.ACTION_VIEW);
            File file = (new File(path));
            //版本在7.0以上是不能直接通过uri访问的
            if (Build.VERSION.SDK_INT > 23) {
                // 由于没有在Activity环境下启动Activity,设置下面的标签
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                Uri apkUri = FileProvider.getUriForFile(context,   "com.zt.weather.fileprovider", file);
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            } else {
                // 跳转到系统安装页面
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setDataAndType(Uri.fromFile(file),
                        "application/vnd.android.package-archive");
                // 会返回结果,回调方法onActivityResul
            }
            context.startActivity(intent);

        } catch (Exception e) {
            System.out.println("安装错误------>" + e.getMessage());
        }

    }
}

