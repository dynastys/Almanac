package com.xy.xylibrary.presenter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTBannerAd;
import com.zt.xuanyin.controller.NativeAd;

public class PangolinBannerAd {

    private static PangolinBannerAd pangolinBannerAd;

    public static PangolinBannerAd getPangolinBannerAd() {
        if (pangolinBannerAd == null) {
            synchronized (PangolinBannerAd.class){
                if (pangolinBannerAd == null) {
                    pangolinBannerAd = new PangolinBannerAd();
                }
            }
        }
        return pangolinBannerAd;
    }

    public void BannerAD(Activity baseContext, final FrameLayout mBannerContainer, final NativeAd nativelogic){
        //step2:创建TTAdNative对象
        TTAdNative mTTAdNative = TTAdSdk.getAdManager().createAdNative(baseContext);//baseContext建议为activity
        //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(nativelogic.nativeObject.posid) //广告位id
                .setSupportDeepLink(true)
                .setImageAcceptedSize(600, 300)
                .build();
        //step5:请求广告，对请求回调的广告作渲染处理
        mTTAdNative.loadBannerAd(adSlot, new TTAdNative.BannerAdListener() {

            @Override
            public void onError(int code, String message) {
                Log.e("TAG", "onError: "+message );
                 mBannerContainer.removeAllViews();
                if (nativelogic != null) {
                    nativelogic.OnRequest(code+"",message);
                }
            }

            @Override
            public void onBannerAdLoad(final TTBannerAd ad) {
                if (ad == null) {
                    return;
                }
                if (nativelogic != null) {
                    nativelogic.OnRequest( "0","msg");
                }
                View bannerView = ad.getBannerView();
                if (bannerView == null) {
                    return;
                }
                mBannerContainer.setVisibility(View.VISIBLE);
                //设置广告互动监听回调
                ad.setBannerInteractionListener(new TTBannerAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int type) {
                        try {
                            if (nativelogic != null) {
                                nativelogic.OnClick(null);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onAdShow(View view, int type) {
                        try {
                            if (nativelogic != null) {
                                nativelogic.AdShow(null);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
                //设置轮播的时间间隔  间隔在30s到120秒之间的值，不设置默认不轮播
                ad.setSlideIntervalTime(30 * 1000);
                mBannerContainer.removeAllViews();
                mBannerContainer.addView(bannerView);

                //（可选）设置下载类广告的下载监听
                bindDownloadListener(ad);
                //在banner中显示网盟提供的dislike icon，有助于广告投放精准度提升
                ad.setShowDislikeIcon(new TTAdDislike.DislikeInteractionCallback() {
                    @Override
                    public void onSelected(int position, String value) {
                        //用户选择不喜欢原因后，移除广告展示
                        mBannerContainer.removeAllViews();
                        mBannerContainer.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancel() {

                    }
                });

            }
        });
    }
    private void bindDownloadListener(TTBannerAd ad) {
        ad.setDownloadListener(new TTAppDownloadListener() {
            @Override
            public void onIdle() {

            }

            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {

            }

            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
             }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
             }

            @Override
            public void onInstalled(String fileName, String appName) {
             }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
             }
        });
    }
}
