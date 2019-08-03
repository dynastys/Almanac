package com.zt.rainbowweather.presenter;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.xy.xylibrary.utils.SaveShare;
import com.zt.rainbowweather.presenter.receiver.NotificationClickReceiver;
import com.zt.rainbowweather.ui.activity.MainActivity;
import com.zt.rainbowweather.ui.activity.StartActivity;
import com.zt.rainbowweather.ui.service.CancelNoticeService;
import com.zt.rainbowweather.utils.ConstUtils;
import com.zt.rainbowweather.utils.WeatherUtils;
import com.zt.weather.R;

public class WeatherWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        ConstUtils.appWidgetIds = appWidgetIds;
        Log.e("RemoteViews", "setAlterData:ssss "+ ConstUtils.appWidgetIds[0] );
        SaveShare.saveValue(context,"appWidgetIds",ConstUtils.appWidgetIds[0]+"");

    }

    public static RemoteViews updateRemoteViews(Context context,com.zt.rainbowweather.entity.weather.Notification notifications) {
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widget_item);
        if (null == notifications) {
            return null;
        } else {
            Log.e("RemoteViews", "setAlterData:aaa " );
            try {
                view.setTextViewText(R.id.city, notifications.city + "刚刚更新");
                view.setTextViewText(R.id.tmp, notifications.tmp+"℃");
                view.setTextViewText(R.id.tmp_max_min, notifications.tmp_max_min);
                view.setTextViewText(R.id.qlty, notifications.qlty);
                view.setTextViewText(R.id.cond_txt, notifications.cond_txt);
                view.setImageViewResource(R.id.icon_weather, WeatherUtils.getWeatherStatus(Integer.parseInt(notifications.cond_code)).iconRes);
                Intent pedometerIntent = new Intent(context, StartActivity.class);
                pedometerIntent.setAction(Intent.ACTION_MAIN);
                pedometerIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                pedometerIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//关键的一步，设置启动模式
//        pedometerIntent.setComponent(new ComponentName(this, StepCounterHomePageActivity.class));
                pedometerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                        pedometerIntent, 0);
                view.setOnClickPendingIntent(R.id.notification_lin1,contentIntent);
            } catch (Exception e) {
                Log.e("RemoteViews", "setAlterData:eee " + e.getMessage());
                e.printStackTrace();
            }
            return view;
        }
    }

    public static int[] getInt(){
        return ConstUtils.appWidgetIds;
    }

}
