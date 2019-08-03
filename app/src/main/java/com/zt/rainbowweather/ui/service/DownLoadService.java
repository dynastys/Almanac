package com.zt.rainbowweather.ui.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.zt.rainbowweather.utils.utils;
import com.zt.xuanyin.Interface.DownMessage;
import com.zt.xuanyin.down.DataDownLoadReceiver;
import com.zt.xuanyin.down.DataInstallReceiver;


public class DownLoadService extends Service implements DownMessage {
    private String file;
    private DataDownLoadReceiver myReceiver;
    private DataInstallReceiver installReceiver;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            if(intent != null){
                file =  intent.getStringExtra("file");
            }
            // 一旦完成下载就调用广播执行代码android.intent.action.DOWNLOAD_COMPLETE
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.DOWNLOAD_COMPLETE");
            //注册广播接收器
            myReceiver = new DataDownLoadReceiver();
            //因为这里需要注入Message，所以不能在AndroidManifest文件中静态注册广播接收器
            myReceiver.a(this);
            this.registerReceiver(myReceiver, intentFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void getMessage(int i) {
        new Thread(() -> {
            switch (i) {
                case 0:
                    if(file != null){
                        utils.doApk(DownLoadService.this,file);
                    }
                    break;

            }

        }).start();
    }
}
