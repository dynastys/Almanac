package com.zt.rainbowweather;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.check.ox.sdk.LionSDK;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.zt.rainbowweather.entity.City;
import com.zt.rainbowweather.utils.utils;

import org.litepal.LitePal;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class BasicApplication extends Application {
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        utils.init(this);
        // 初始化LitePal数据库
        LitePal.initialize(this);
        // 初始化SDK
        UMConfigure.init(this, "5c7cea573fc195c5820014f5", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, null);
        UMConfigure.init(this, "5c7cea573fc195c5820014f5", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "1ac72d4a95374a17a582111739052c89");
        //获取消息推送代理示例
        PushAgent mPushAgent = PushAgent.getInstance(this);
       //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
                Log.i("TAG","注册成功：deviceToken：-------->  " + deviceToken);
            }
            @Override
            public void onFailure(String s, String s1) {
                Log.e("TAG","注册失败：-------->  " + "s:" + s + ",s1:" + s1);
            }
        });
        // 选用AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
        handleSSLHandshake();
//        OxSDK.init(this);
        LionSDK.init(this);
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
