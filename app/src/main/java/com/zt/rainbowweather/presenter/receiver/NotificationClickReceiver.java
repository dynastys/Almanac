package com.zt.rainbowweather.presenter.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zt.rainbowweather.presenter.notification.NotificationAd;
import com.zt.rainbowweather.ui.activity.MainActivity;

public class NotificationClickReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("onReceive", "onReceive: " );
        if(NotificationAd.nativelogic != null){
            NotificationAd.nativelogic.OnClick(null);
        }
        //todo 跳转之前要处理的逻辑
        Intent newIntent = new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newIntent);
    }
}
