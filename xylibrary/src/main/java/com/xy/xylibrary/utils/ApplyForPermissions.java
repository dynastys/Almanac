package com.xy.xylibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;


import com.xy.xylibrary.Interface.PermissionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限请求
 */

public class ApplyForPermissions {

    private static ApplyForPermissions applyForPermissions = null;
    private static Context context;


    public static ApplyForPermissions getInstence(Context c) {
        context = c;
        if (applyForPermissions == null) {
            synchronized (ApplyForPermissions.class) {
                if (applyForPermissions == null) {
                    applyForPermissions = new ApplyForPermissions();
                }
            }
        }
        return applyForPermissions;
    }

    /**
     * 申请权限
     */
    public static void requestRuntimePermissions(
            String[] permissions, PermissionListener listener) {
        List<String> permissionList = new ArrayList<>();
        // 遍历每一个申请的权限，把没有通过的权限放在集合中
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            } else {
                listener.granted();
            }
        }
        // 申请权限
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) context,
                    permissionList.toArray(new String[permissionList.size()]), 1);
        }
    }

    public static String[] PERMISSIONS_STORAGE = {
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_NETWORK_STATE",
            "android.permission.ACCESS_WIFI_STATE",
            "android.permission.CHANGE_WIFI_STATE",
            "android.permission.INTERNET",
            "android.permission.READ_PHONE_STATE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.ACCESS_LOCATION_EXTRA_COMMANDS",
            "android.permission.BLUETOOTH",
            "android.permission.BLUETOOTH_ADMIN",
            "android.permission.SYSTEM_ALERT_WINDOW",
            "android.permission.GET_TASKS",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.BIND_JOB_SERVICE",
            "android.permission.RECEIVE_BOOT_COMPLETED",
             "android.permission.MANAGE_USERS",
            "android.permission.WAKE_LOCK",
            "android.permission.MOUNT_UNMOUNT_FILESYSTEMS",
            "android.permission.PACKAGE_USAGE_STATS",
            "android.permission.BATTERY_STATS",
            "android.permission.WRITE_SETTINGS"
    };

    public static void ApplyFor(PermissionListener mListener) {
        if (Build.VERSION.SDK_INT >= 23) {//判断当前系统是不是Android6.0
            requestRuntimePermissions(PERMISSIONS_STORAGE, mListener);
        }
    }

}
