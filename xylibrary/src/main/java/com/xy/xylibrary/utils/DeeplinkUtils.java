package com.xy.xylibrary.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.List;


public class DeeplinkUtils {
    private static DeeplinkUtils deeplink = new DeeplinkUtils();

    private DeeplinkUtils() {

    }

    public static DeeplinkUtils getDeeplinkUtils() {


        return deeplink;
    }

    // 判断是否支持deeplink
    public boolean CanOpenDeeplink(Context context, String deeplink) {
        if (deeplink != null) {
            try {
                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(deeplink));
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                final PackageManager packageManager = context.getPackageManager();
                final List<ResolveInfo> infos = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY | PackageManager.GET_RESOLVED_FILTER);
                Log.e("TAG", (infos.size() > 0) + "");
                return (infos.size() > 0);
            } catch (Exception e) {
                Log.e("TAG", e.getMessage());
                e.printStackTrace();
            }
        }
        return false;
    }

    // 调起deeplink
    public void OpenDeeplink2(final Context context, String dplink) {
        final String userAgent = Utils.getDefaultUserAgentString(context);
        try {
            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(dplink));
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                 for (String url: deeplink) {
//                    LogUtils.sendReportHttpRequest(context,url, userAgent);
//                }
//            }
//        }).start();

    }

    // 调起deeplink
    public void OpenDeeplink(final Context context, String deeplink) {
        if (!TextUtils.isEmpty(deeplink)) {
             try {
                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(deeplink));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }


    }

    // 判断是否安装该应用
    public boolean isAppInstall(Context context, String packageName) {
        try {
            if ((context.getPackageManager().getPackageInfo(packageName, 0)) != null) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    // 根据安装包安装应用
    public void Install(Context context, File file) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            context.startActivity(intent);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //获取包名
    public String apkPackage(Context context, String file) {
        ApplicationInfo appInfo;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(file, PackageManager.GET_ACTIVITIES);
            appInfo = info.applicationInfo;
            return appInfo.packageName;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    //根据包名吊起APP
    public void doStartApplicationWithPackageName(Context context, String packagename) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等  
        PackageInfo packageinfo = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent  
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历  
        List<ResolveInfo> resolveinfoList = context.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname  
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]  
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent  
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            // 设置ComponentName参数1:packagename参数2:MainActivity路径  
            ComponentName cn = new ComponentName(packageName, className);

            intent.setComponent(cn);
            context.startActivity(intent);
        }
    }
}
