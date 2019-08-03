package com.zt.rainbowweather;

import android.content.Context;
import android.support.annotation.Keep;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixApplication;
import com.taobao.sophix.SophixEntry;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;


public class SophixStubApplication extends SophixApplication {
    private final String TAG = "SophixStubApplication";
    // 此处SophixEntry应指定真正的Application，并且保证RealApplicationStub类名不被混淆。
    @Keep
    @SophixEntry(BasicApplication.class)
    static class RealApplicationStub {}
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//         如果需要使用MultiDex，需要在此处调用。
         MultiDex.install(this);
        initSophix();
    }
    private void initSophix() {
        String appVersion = "0.0.0";
        try {
            appVersion = this.getPackageManager()
                    .getPackageInfo(this.getPackageName(), 0)
                    .versionName;
        } catch (Exception e) {
        }
        Log.i(TAG, appVersion);
        try {
            final SophixManager instance = SophixManager.getInstance();
            instance.setContext(this)
                    .setAppVersion(appVersion)
                    .setSecretMetaData(null, null, null)
                    .setEnableDebug(false)
                    .setEnableFullLog()
                    .setPatchLoadStatusStub((mode, code, info, handlePatchVersion) -> {
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            Log.i(TAG, "sophix load patch success!");
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 如果需要在后台重启，建议此处用SharePreference保存状态。
                            Log.i(TAG, "sophix preload patch success. restart app to make effect.");
                        }else{
                            Log.i(TAG, "code"+code);
                        }
                    }).initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            // queryAndLoadNewPatch不可放在attachBaseContext 中，否则无网络权限，建议放在后面任意时刻，如onCreate中
            SophixManager.getInstance().queryAndLoadNewPatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}