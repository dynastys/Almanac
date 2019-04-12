package com.xy.xylibrary.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.xy.xylibrary.utils.DownLoadUtils;
import com.xy.xylibrary.utils.SaveShare;
import com.xy.xylibrary.utils.Utils;


/*下载完成的监控*/
public class DownService extends Service {

    private String userAgent;
    private String file;
    private DownLoadUtils downLoadUtils;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        userAgent = Utils.getDefaultUserAgentString(this);
        file =  intent.getStringExtra("file");
        SaveShare.saveValue(this,"file",file);

        downLoadUtils = new DownLoadUtils(this);
        downLoadUtils.doApk(file);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }




}
