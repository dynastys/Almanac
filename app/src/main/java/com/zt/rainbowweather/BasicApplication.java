package com.zt.rainbowweather;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.RemoteViews;

import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.check.ox.sdk.LionSDK;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;
import com.xy.xylibrary.base.AppContext;
import com.xy.xylibrary.utils.SaveShare;
import com.xy.xylibrary.utils.Utils;
import com.yilan.sdk.ui.YLUIInit;
import com.zt.rainbowweather.entity.City;
import com.zt.rainbowweather.entity.WeatherUtilBean;
import com.zt.rainbowweather.feedback.CustomUserProvider;
import com.zt.rainbowweather.ui.activity.AdviseMoreDetailActivity;
import com.zt.rainbowweather.utils.WeatherUtils;
import com.zt.rainbowweather.utils.utils;
import com.zt.weather.R;

import org.android.agoo.huawei.HuaWeiRegister;
import org.android.agoo.mezu.MeizuRegister;
import org.android.agoo.oppo.OppoRegister;
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


    @Override
    public void onCreate() {
        super.onCreate();
        basicApplication = this;
        mContext = this;
        utils.init(this);
        Utils.init(this);
        AppContext.registToWX(this);
        // 初始化LitePal数据库
        LitePal.initialize(BasicApplication.this);
//        LitePalApplication.initialize(this);
        try {
            // 初始化SDK
            UMConfigure.init(this, "5d07585d3fc195c9ba001330", "tg_1", UMConfigure.DEVICE_TYPE_PHONE, null);
            UMConfigure.init(this, "5d07585d3fc195c9ba001330", "tg_1", UMConfigure.DEVICE_TYPE_PHONE, "e583e679267b3542c272b1b36337687b");
            //获取消息推送代理示例
            PushAgent mPushAgent = PushAgent.getInstance(this);
            UmengMessageHandler messageHandler = new UmengMessageHandler() {

                @Override
                public Notification getNotification(Context context, UMessage msg) {
                    try {
                        Log.e("getNotification", "getNotification: ");
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
                                WeatherUtilBean weatherUtilBean = WeatherUtils.getWeatherStatus(Integer.parseInt(str[1]));
                                myNotificationView.setImageViewResource(R.id.notification_wether_icon,
                                        weatherUtilBean.iconRes);
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
                                    .build();
                                // 标志位的设置：应设置为可以自动取消，这样用户就可以取消他，如果设置为Intent.FLAG_ACTIVITY_CLEAR_TOP | Notification.FLAG_ONGOING_EVENT;则会一直显示通知<br>//        Notification.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP | Notification.FLAG_ONGOING_EVENT;
                                mNotification.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP;
                                //由于Android v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
                                mNotification.bigContentView = myNotificationView;
    //                            mNM.notify(0, mNotification);
                                try {
                                    if(msg.extra != null){
                                        url = msg.extra.get("url");
                                    }
                                } catch (Exception e) {
                                     e.printStackTrace();
                                }
                                return mNotification;
                            default:
                                try {
                                    if(msg.extra != null){
                                        url = msg.extra.get("url");
                                    }
                                } catch (Exception e) {
                                     e.printStackTrace();
                                }
                                return super.getNotification(context, msg);
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    return super.getNotification(context, msg);
                }
            };

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
            mPushAgent.setMessageHandler(messageHandler);
//            // 选用AUTO页面采集模式
            MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
            handleSSLHandshake();
//        OxSDK.init(this);
//            LionSDK.init(this);
//            try {
//                QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
//
//                    @Override
//                    public void onViewInitFinished(boolean arg0) {
//                        //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
//                        Log.d("app", " onViewInitFinished is " + arg0);
//                    }
//
//                    @Override
//                    public void onCoreInitFinished() {
//                        // TODO Auto-generated method stub
//                    }
//                };
//                //x5内核初始化接口
//                QbSdk.initX5Environment(getApplicationContext(), cb);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            // 关于 CustomUserProvider 可以参看后面的文档
//        // 初始化参数依次为 this, AppId, AppKey
//        AVOSCloud.initialize(this,"vnB7DYkxpxsC1Gz6nMpBcdYO-gzGzoHsz","Nmpeus4pLkxx19cXD0jyyUtq");
//        // 放在 SDK 初始化语句 AVOSCloud.initialize() 后面，只需要调用一次即可
//        AVOSCloud.setDebugLogEnabled(true);
//            LCChatKit.getInstance().setProfileProvider((LCChatProfileProvider) CustomUserProvider.getInstance());
//            LCChatKit.getInstance().init(getApplicationContext(), "vnB7DYkxpxsC1Gz6nMpBcdYO-gzGzoHsz", "Nmpeus4pLkxx19cXD0jyyUtq");
//
//            //穿山甲广告强烈建议在应用对应的Application#onCreate()方法中调用，避免出现content为null的异常
//            TTAdSdk.init(getApplicationContext(),
//                    new TTAdConfig.Builder()
//                            .appId("5023044")
//                            .useTextureView(true) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
//                            .appName("星云天气")
//                            .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
//                            .allowShowNotify(true) //是否允许sdk展示通知栏提示
//                            .allowShowPageWhenScreenLock(true) //是否在锁屏场景支持展示广告落地页
////                            .debug(true) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
//                            .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI, TTAdConstant.NETWORK_STATE_3G) //允许直接下载的网络状态集合
//                            .supportMultiProcess(false) //是否支持多进程，true支持
//                            .build());
//
//            //如果明确某个进程不会使用到广告SDK，可以只针对特定进程初始化广告SDK的content
//            //if (PROCESS_NAME_XXXX.equals(processName)) {
//            //   TTAdSdk.init(context, config);
//            //}
//            //一览广告初始化
//            YLUIInit.getInstance()
//                    .setApplication(this)
//                    .setAccessKey("ylj9beit69ar")
//                    .setAccessToken("6x1k831m4yasqb458v5ilocvo1cmd9u8")
//                    .build();
            //小米推送
            MiPushRegistar.register(this, "2882303761518029190", "5201802946190"); // 小米开放平台申请到的 APPID 和 APPKEY
            //魅族
//            MeizuRegister.register(this,"", "");
            //华为
            HuaWeiRegister.register(this);
            //OPPO通道，参数1为app key，参数2为app secret
            OppoRegister.register(this, "266ee86d3d314ef6bc1fea232346c81f", "9ce20d95d5e142b18896f13344cacd67");
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
