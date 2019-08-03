package com.zt.rainbowweather.presenter;

import android.app.Activity;
import android.support.annotation.MainThread;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.zt.xuanyin.controller.NativeAd;

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

    /*穿山甲*/
    public void PangolinAd(Activity baseContext, RelativeLayout mSplashContainer,TTSplashAd.AdInteractionListener adInteractionListener,NativeAd nativelogic,PangolinListener pangolinListener){
        try {
            //step2:创建TTAdNative对象
            TTAdNative mTTAdNative = TTAdSdk.getAdManager().createAdNative(baseContext);//baseContext建议为activity
            //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
            AdSlot adSlot = new AdSlot.Builder()
                    .setCodeId("823044247")//"823044265"
                    .setSupportDeepLink(true)
                    .setImageAcceptedSize(1080, 1920)
                    .build();
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
}
