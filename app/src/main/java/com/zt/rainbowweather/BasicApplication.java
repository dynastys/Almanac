package com.zt.rainbowweather;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.widget.RemoteViews;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;
import com.xy.xylibrary.base.AppContext;
import com.xy.xylibrary.utils.RomUtils;
import com.xy.xylibrary.utils.SaveShare;
import com.xy.xylibrary.utils.Utils;
import com.zt.rainbowweather.entity.City;
import com.zt.rainbowweather.entity.WeatherUtilBean;
import com.zt.rainbowweather.feedback.CustomUserProvider;
import com.zt.rainbowweather.utils.WeatherUtils;
import com.zt.rainbowweather.utils.utils;
import com.zt.weather.R;

import org.android.agoo.huawei.HuaWeiRegister;
import org.android.agoo.xiaomi.MiPushRegistar;
import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Calendar;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.LCChatProfileProvider;


public class BasicApplication extends LitePalApplication {
    private static Context mContext;
    private static BasicApplication basicApplication;
    // 此 id 与 key 仅供测试使用
    private final String APP_ID = "1qayTChMYPLfRWj0JFAKNHgC-gzGzoHsz";
    private final String APP_KEY = "Xu2qfC934e4T5U4TPP4JMfom";
    public static String url = "";
    public static BasicApplication getBasicApplication() {
        return basicApplication;
    }
    public static int appCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        try {

            basicApplication = this;
            mContext = this;
            utils.init(this);
            Utils.init(this);
            AppContext.registToWX(this);
            // 初始化LitePal数据库
            LitePal.initialize(BasicApplication.this);
//        LitePalApplication.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // 初始化SDK
            UMConfigure.init(this, "5d07585d3fc195c9ba001330", RomUtils.app_youm_code, UMConfigure.DEVICE_TYPE_PHONE, null);
            UMConfigure.init(this, "5d07585d3fc195c9ba001330", RomUtils.app_youm_code, UMConfigure.DEVICE_TYPE_PHONE, "e583e679267b3542c272b1b36337687b");
            //获取消息推送代理示例
            PushAgent mPushAgent = PushAgent.getInstance(this);
            //注册推送服务，每次调用register方法都会回调该接口
            mPushAgent.register(new IUmengRegisterCallback() {
                @Override
                public void onSuccess(String deviceToken) {
                    //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
                    SaveShare.saveValue(BasicApplication.this, "deviceToken", deviceToken);
                    Log.e("TAG", "注册成功：deviceToken：-------->  " + deviceToken);
                }

                @Override
                public void onFailure(String s, String s1) {
                    Log.e("TAG", "注册失败：-------->  " + "s:" + s + ",s1:" + s1);
                }
            });
            UmengMessageHandler messageHandler = new UmengMessageHandler() {

                @Override
                public Notification getNotification(Context context, UMessage msg) {
                    try {
                        switch (msg.builder_id) {
                            case 1:
                                String Time = "";
                                long time = System.currentTimeMillis();
                                final Calendar mCalendar = Calendar.getInstance();
                                mCalendar.setTimeInMillis(time);
                                String hour = String.valueOf(mCalendar.get(Calendar.HOUR));
                                String min = String.valueOf(mCalendar.get(Calendar.MINUTE));
                                int apm = mCalendar.get(Calendar.AM_PM);
                                switch (apm) {
                                    case 0:
                                        Time = "上午 " + hour + ":" + min;
                                        break;
                                    case 1:
                                        Time = "下午 " + hour + ":" + min;
                                        break;
                                }
                                NotificationManager mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(BasicApplication.this, "Channel");
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    NotificationChannel mChannel = new NotificationChannel("Channel", "Channel", NotificationManager.IMPORTANCE_HIGH);
                                   // 该渠道的通知是否使用震动
                                    mChannel.enableVibration(true);
                                    // 设置显示模式
                                    mChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                                    mNM.createNotificationChannel(mChannel);
                                }
                                String[] str = msg.title.split("/-");

                                RemoteViews myNotificationView = new RemoteViews(context.getPackageName(),
                                        R.layout.notification_view);
                                WeatherUtilBean weatherUtilBean;
                                if(str.length > 1){
                                    weatherUtilBean = WeatherUtils.getWeatherStatus(Integer.parseInt(str[1]));
                                    myNotificationView.setImageViewResource(R.id.notification_wether_icon,
                                            weatherUtilBean.iconRes);
                                }else{
                                    weatherUtilBean = new WeatherUtilBean();
                                    weatherUtilBean.iconRes = R.mipmap.icon;
                                }
                                myNotificationView.setTextViewText(R.id.notification_wether_title, str[0]);
                                myNotificationView.setTextViewText(R.id.notification_wether_dec, msg.text);
                                myNotificationView.setTextViewText(R.id.notification_time, Time);
                                myNotificationView.setImageViewResource(R.id.notification_small_icon,
                                        getSmallIconId(context, msg));

                                Notification mNotification =  mBuilder.setContentTitle(str[0])
                                    // Content for API <24 (7.0 and below) devices.
                                    .setContentText(msg.text)
                                    .setSmallIcon(R.mipmap.icon)
                                    .setLargeIcon(BitmapFactory.decodeResource(
                                            getResources(),
                                            weatherUtilBean.iconRes))
                                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                                    .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                                    .setCategory(Notification.CATEGORY_REMINDER)
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setAutoCancel(true)
                                    .build();
                                // 标志位的设置：应设置为可以自动取消，这样用户就可以取消他，如果设置为Intent.FLAG_ACTIVITY_CLEAR_TOP | Notification.FLAG_ONGOING_EVENT;则会一直显示通知<br>//        Notification.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP | Notification.FLAG_ONGOING_EVENT;
//                                mNotification.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP;
                                //由于Android v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
                                mNotification.bigContentView = myNotificationView;
    //                            mNM.notify(0, mNotification);
                                if(msg.extra != null){
                                    url = msg.extra.get("url");
                                }
                                return mNotification;
                            default:
                                if(msg.extra != null){
                                    url = msg.extra.get("url");
                                }
                                return super.getNotification(context, msg);
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    return super.getNotification(context, msg);
                }
            };


            mPushAgent.setMessageHandler(messageHandler);
//            // 选用AUTO页面采集模式
            MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
            handleSSLHandshake();

            //小米推送
            MiPushRegistar.register(this, "2882303761518029190", "5201802946190"); // 小米开放平台申请到的 APPID 和 APPKEY
            //魅族
//            MeizuRegister.register(this,"", "");
            //华为
            HuaWeiRegister.register(this);

            registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

                }

                @Override
                public void onActivityStarted(Activity activity) {
                    appCount++;
                }

                @Override
                public void onActivityResumed(Activity activity) {
                }

                @Override
                public void onActivityPaused(Activity activity) {
                }

                @Override
                public void onActivityStopped(Activity activity) {
                    appCount--;
                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * vector兼容5.0以下系统
     */
    static {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion < 21) {
            //适配android5.0以下
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        }
    }

    public static City getLocatedCity() {
        return locatedCity;
    }

    public static void setLocatedCity(City locatedCity) {
        BasicApplication.locatedCity = locatedCity;
    }

    private static City locatedCity;

    private void initLog() {

    }

    public static Context getContext() {
        return mContext;
    }

    /*忽略https的证书校验*/
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("TLS");
            // trustAllCerts信任所有的证书
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }

}
