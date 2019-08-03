package com.zt.rainbowweather.ui.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.xy.xylibrary.utils.SaveShare;
import com.zt.rainbowweather.presenter.WeatherWidget;
import com.zt.rainbowweather.utils.ConstUtils;
import com.zt.weather.R;
import com.zt.rainbowweather.ui.activity.MainActivity;

/** 移除前台Service通知栏标志，这个Service选择性使用
 *
 * Created by jianddongguo on 2017/7/7.
 */
public class CancelNoticeService extends Service {

    private NotificationManager mNM;
    private Notification notification;


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
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
           com.zt.rainbowweather.entity.weather.Notification notification = intent.getParcelableExtra("notification");
            setAlterData(notification);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Show a Notification while this service is running.
     * 在service运行时，显示通知信息
     */
    public void showNotification(com.zt.rainbowweather.entity.weather.Notification notifications) {
        try {
            mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "1");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel("1", "1", NotificationManager.IMPORTANCE_MIN);
                mChannel.enableLights(true);
                 mChannel.setShowBadge(true);
                mNM.createNotificationChannel(mChannel);
            }
            notification = mBuilder
                    // Title for API <16 (4.0 and below) devices.
                    .setContentTitle("标题")
                    // Content for API <24 (7.0 and below) devices.
                    .setContentText("内容")
                    .setSmallIcon(R.mipmap.icon)
                    .setLargeIcon(BitmapFactory.decodeResource(
                            getResources(),
                            R.mipmap.icon))
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                    .setCategory(Notification.CATEGORY_REMINDER)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build();
            // 标志位的设置：应设置为可以自动取消，这样用户就可以取消他，如果设置为Intent.FLAG_ACTIVITY_CLEAR_TOP | Notification.FLAG_ONGOING_EVENT;则会一直显示通知<br>//        Notification.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP | Notification.FLAG_ONGOING_EVENT;
            notification.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP | Notification.FLAG_ONGOING_EVENT;
            Intent pedometerIntent = new Intent(this, MainActivity.class);
            pedometerIntent.setAction(Intent.ACTION_MAIN);
            pedometerIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            pedometerIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//关键的一步，设置启动模式
//        pedometerIntent.setComponent(new ComponentName(this, StepCounterHomePageActivity.class));
            pedometerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                    pedometerIntent, 0);
            //        Notification.setLatestEventInfo(this, text,
//                getText(R.string.notification_subtitle)+":  "+SensorData.stepNum+" 步", contentIntent);
            // 其中R.layout.notification是一个布局文件<br>
            notification.contentView = new RemoteViews(getPackageName(),R.layout.notification);

            notification.contentView.setOnClickPendingIntent(R.id.notification_lin,contentIntent);
            //前台运行
//            startForeground(1, notification);
            mNM.notify(1, notification);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(notification != null){
            notification.clone();
            mNM.cancelAll();
        }
    }

    public void setAlterData(com.zt.rainbowweather.entity.weather.Notification notifications){
        try {
            showNotification(notifications);
            notification.contentView.setTextViewText(R.id.city, notifications.city + "刚刚更新");
            notification.contentView.setTextViewText(R.id.tmp, notifications.tmp+"℃");
            notification.contentView.setTextViewText(R.id.tmp_max_min, notifications.tmp_max_min);
            notification.contentView.setTextViewText(R.id.qlty, notifications.qlty);
            notification.contentView.setTextViewText(R.id.cond_txt, notifications.cond_txt);
            mNM.notify(1, notification);
            AppWidgetManager manager = AppWidgetManager.getInstance(this);
            RemoteViews view = WeatherWidget.updateRemoteViews(this,notifications);
            if (null != view) {
               String appWidgetIds = SaveShare.getValue(this,"appWidgetIds");
               if(TextUtils.isEmpty(appWidgetIds)){
                   appWidgetIds = "7";
               }
                manager.updateAppWidget(Integer.parseInt(appWidgetIds), view);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }
}