package com.zt.rainbowweather.presenter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.MainThread;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.tencent.smtt.sdk.QbSdk;
import com.xy.xylibrary.base.AppContext;
import com.yilan.sdk.common.util.FSLogcat;
import com.yilan.sdk.ui.YLUIInit;
import com.zt.rainbowweather.BasicApplication;
import com.zt.rainbowweather.feedback.CustomUserProvider;
import com.zt.xuanyin.controller.NativeAd;
import org.android.agoo.oppo.OppoRegister;
import org.android.agoo.vivo.VivoRegister;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.LCChatProfileProvider;

public class StartAd {
    private static final String TAG = "SplashActivity";

    private static StartAd startAd;

    public static StartAd getStartAd() {
        if (startAd == null) {
            synchronized (StartAd.class){
                if (startAd == null) {
                    startAd = new StartAd();
                }
            }
        }
        return startAd;
    }

    public interface PangolinListener{
        void onError(int code, String message);
        void onSplashAdLoad();
    }
    public void Application(Context context){
        if(BasicApplication.getBasicApplication() != null){
            AppContext.getUserInfo(context,"","",null);
            //华为
//            HuaWeiRegister.register(BasicApplication.getBasicApplication());
             //vivo 通道
            VivoRegister.register(context);
//            //OPPO通道，参数1为app key，参数2为app secret
            OppoRegister.register(context, "266ee86d3d314ef6bc1fea232346c81f", "9ce20d95d5e142b18896f13344cacd67");
            //穿山甲广告强烈建议在应用对应的Application#onCreate()方法中调用，避免出现content为null的异常
            TTAdSdk.init(BasicApplication.getBasicApplication(),
                    new TTAdConfig.Builder()
                            .appId("5023044")
                            .useTextureView(true) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                            .appName("星云天气")
                            .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                            .allowShowNotify(true) //是否允许sdk展示通知栏提示
                            .allowShowPageWhenScreenLock(true) //是否在锁屏场景支持展示广告落地页
//                            .debug(true) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                            .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI, TTAdConstant.NETWORK_STATE_3G) //允许直接下载的网络状态集合
                            .supportMultiProcess(false) //是否支持多进程，true支持
                            .build());

//            // 初始化SDK
//            UMConfigure.init(context, "5d07585d3fc195c9ba001330", "tg_1", UMConfigure.DEVICE_TYPE_PHONE, null);
//            UMConfigure.init(context, "5d07585d3fc195c9ba001330", "tg_1", UMConfigure.DEVICE_TYPE_PHONE, "e583e679267b3542c272b1b36337687b");
            LCChatKit.getInstance().setProfileProvider((LCChatProfileProvider) CustomUserProvider.getInstance());
            LCChatKit.getInstance().init(BasicApplication.getBasicApplication(), "vnB7DYkxpxsC1Gz6nMpBcdYO-gzGzoHsz", "Nmpeus4pLkxx19cXD0jyyUtq");
//            //获取消息推送代理示例
//            PushAgent mPushAgent = PushAgent.getInstance(context);
//            //注册推送服务，每次调用register方法都会回调该接口
//            mPushAgent.register(new IUmengRegisterCallback() {
//                @Override
//                public void onSuccess(String deviceToken) {
//                    //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
//                    Log.e("TAG", "注册成功：deviceToken：-------->  "+deviceToken );
//                }
//
//                @Override
//                public void onFailure(String s, String s1) {
//                    Log.e("TAG", "注册失败：-------->  " + "s:" + s + ",s1:" + s1);
//                }
//            });
//            // 选用AUTO页面采集模式
//            MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
            try {
                QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

                    @Override
                    public void onViewInitFinished(boolean arg0) {
                        //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                        Log.d("app", " onViewInitFinished is " + arg0);
                    }

                    @Override
                    public void onCoreInitFinished() {
                        // TODO Auto-generated method stub
                    }
                };
                //x5内核初始化接口
                QbSdk.initX5Environment(BasicApplication.getBasicApplication(), cb);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //如果明确某个进程不会使用到广告SDK，可以只针对特定进程初始化广告SDK的content
            //if (PROCESS_NAME_XXXX.equals(processName)) {
            //   TTAdSdk.init(context, config);
            //}
            //一览广告初始化
            YLUIInit.getInstance()
                    .setApplication(BasicApplication.getBasicApplication())
                    .setAccessKey("ylj9beit69ar")
                    .setAccessToken("6x1k831m4yasqb458v5ilocvo1cmd9u8")
                    .build();
            FSLogcat.DEBUG = true;
        }
    }
    /*穿山甲*/
    public void PangolinAd(Activity baseContext, RelativeLayout mSplashContainer,TTSplashAd.AdInteractionListener adInteractionListener,NativeAd nativelogic,PangolinListener pangolinListener){
        try {
            //step2:创建TTAdNative对象
            TTAdNative mTTAdNative = TTAdSdk.getAdManager().createAdNative(baseContext);//baseContext建议为activity
            //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
            AdSlot adSlot = new AdSlot.Builder()
                    .setCodeId("823044533")//"823044265"
                    .setSupportDeepLink(true)
                    .setImageAcceptedSize(1080, 1920)
                    .build();
            Log.e("Application", "initSophix:0000 "+System.currentTimeMillis());

            mTTAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
                @Override
                @MainThread
                public void onError(int code, String message) {
                    Log.d(TAG, message);
                    pangolinListener.onError(code,message);
//                    if (nativelogic != null) {
//                        nativelogic.OnRequest(code+"",message);
//                    }
                }

                @Override
                @MainThread
                public void onTimeout() {

                }

                @Override
                @MainThread
                public void onSplashAdLoad(TTSplashAd ad) {
                    pangolinListener.onSplashAdLoad();
                    //                mHasLoaded = true;
                    //                mHandler.removeCallbacksAndMessages(null);
                    if (ad == null) {
//                        if (nativelogic != null) {
//                            Log.d(TAG, "NO_AD");
//                            nativelogic.OnRequest("4006","NO_AD");
//                        }
                        return;
                    }
//                    if (nativelogic != null) {
//                        Log.d(TAG, "0");
//                        nativelogic.OnRequest("0","msg");
//
//                    }
                    //设置SplashView的交互监听器
                    ad.setSplashInteractionListener(adInteractionListener);
                    //获取SplashView
                    View view = ad.getSplashView();
                    mSplashContainer.removeAllViews();
                    //把SplashView 添加到ViewGroup中
                    mSplashContainer.addView(view);

                }
            }, 4000);
        } catch (Exception e) {
            Log.d(TAG, "onAdTimeOver"+e.getMessage());
            e.printStackTrace();
        }
    }

    public void loadViewLayout() {
        if(BasicApplication.getBasicApplication() != null){
            //穿山甲广告强烈建议在应用对应的Application#onCreate()方法中调用，避免出现content为null的异常
            TTAdSdk.init(BasicApplication.getBasicApplication(),
                    new TTAdConfig.Builder()
                            .appId("5023044")
                            .useTextureView(true) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                            .appName("星云天气")
                            .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                            .allowShowNotify(true) //是否允许sdk展示通知栏提示
                            .allowShowPageWhenScreenLock(true) //是否在锁屏场景支持展示广告落地页
//                            .debug(true) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                            .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI, TTAdConstant.NETWORK_STATE_3G) //允许直接下载的网络状态集合
                            .supportMultiProcess(false) //是否支持多进程，true支持
                            .build());

        }
    }
}
