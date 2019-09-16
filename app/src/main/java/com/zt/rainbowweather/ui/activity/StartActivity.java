package com.zt.rainbowweather.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.xy.xylibrary.base.BaseActivity;
import com.xy.xylibrary.utils.DisplayUtil;
import com.xy.xylibrary.utils.SaveShare;
import com.xy.xylibrary.utils.Utils;
import com.zt.rainbowweather.BasicApplication;
import com.zt.rainbowweather.api.RequestSyntony;
import com.zt.rainbowweather.entity.background.AppSpread;
import com.zt.rainbowweather.entity.news.Switch;
import com.zt.rainbowweather.presenter.StartAd;
import com.zt.rainbowweather.presenter.almanac.AlmanacLogic;
import com.zt.rainbowweather.presenter.map.MapLocation;
import com.zt.rainbowweather.presenter.request.BackgroundRequest;
import com.zt.rainbowweather.presenter.request.NewsRequest;
import com.zt.rainbowweather.presenter.request.WeatherRequest;
import com.zt.rainbowweather.utils.ConstUtils;
import com.zt.rainbowweather.utils.RxCountDown;
import com.zt.rainbowweather.utils.Util;
import com.zt.rainbowweather.view.ShapeTextView;
import com.zt.weather.R;
import com.zt.xuanyin.Interface.AdProtogenesisListener;
import com.zt.xuanyin.Interface.Const;
import com.zt.xuanyin.controller.Ad;
import com.zt.xuanyin.controller.NativeAd;
import com.zt.xuanyin.entity.model.Native;
import com.zt.xuanyin.view.AdLinkActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.BuildConfig;
import rx.Subscriber;
import rx.Subscription;

/**
 * @author zw
 * @time 2019-3-8
 * 开屏
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class StartActivity extends BaseActivity implements RequestSyntony<Switch>, MapLocation.Dismiss, SplashADListener, TTSplashAd.AdInteractionListener {

    private ImageView ivImage;
    private TextView tvVersionName;
    private TextView splash_skip_tv;
    private ShapeTextView tvSkip;
    private RelativeLayout container, ad_relative;
    private Subscription subscription;
    private float ClickX;
    private float ClickY;
    private MapLocation mapLocation;
    private String src = "";
    private int size = 1;
    private SplashAD splashAD;
    private boolean ISGDTSKIP = true;
    private NativeAd nativelogicDd;

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("StartActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)，必须保证 onPageEnd 在 onPause 之前调用，因为SDK会在 onPause 中保存onPageEnd统计到的页面数据。
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setTheme(R.style.AppTheme);//恢复原有的样式
            // 隐藏pad底部虚拟键
            Window _window = getWindow();
            WindowManager.LayoutParams params = _window.getAttributes();
            params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
            _window.setAttributes(params);
            PushAgent.getInstance(this).onAppStart();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected Activity getContext() {
        return StartActivity.this;
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_start;
    }

    private void loadAd() {
        try {
            // 获得开屏对象
            Ad.setKeyword(ConstUtils.app_market_code);
            Ad.getAd().NativeAD(this, "98f8e423-02e0-49f5-989f-af46f5c59203", "9432a40d-9fa7-4d1a-b760-58a32edc9465", "67C53558D3E3485EA681EA21735A5003", new AdProtogenesisListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public void onADReady(final Native url, NativeAd nativelogic) {
                    src = url.src;
                    nativelogicDd = nativelogic;
                    if (!TextUtils.isEmpty(nativelogic.nativeObject.sdk_code)) {
                        if (nativelogic.nativeObject.sdk_code.equals("GDT_SDK")) {
                            fetchSplashAD(StartActivity.this, container, tvSkip, nativelogic.nativeObject.appid, nativelogic.nativeObject.posid, StartActivity.this, 0);
                        } else if (nativelogic.nativeObject.sdk_code.equals("TOUTIAO_SDK")) {
                            //在合适的时机申请权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题
                            TTAdSdk.getAdManager().requestPermissionIfNecessary(StartActivity.this);
                            WeatherRequest.getWeatherRequest().getLookAtData(StartActivity.this, "广告", "请求");
                            StartAd.getStartAd().PangolinAd(StartActivity.this, container, StartActivity.this, null, new StartAd.PangolinListener() {
                                @Override
                                public void onError(int code, String message) {
                                    ImageBg();
                                }

                                @SuppressLint("ResourceAsColor")
                                @Override
                                public void onSplashAdLoad() {
                                    ad_relative.setVisibility(View.GONE);
//                                    tvSkip.setTextColor(R.color.white);
//                                    splash_skip_tv.setVisibility(View.VISIBLE);
                                    Log.e("Application", "initSophix:222 " + System.currentTimeMillis());
                                }
                            });
                        }
                    } else {
                        tvSkip.setOnClickListener(v -> skip());
                        findViewById(R.id.tv_ad_flag).setVisibility(View.VISIBLE);
                        Glide.with(StartActivity.this).load(url.src).into(ivImage);
                        nativelogic.AdShow(container);
                        ivImage.setOnTouchListener((v, event) -> {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    ClickX = event.getRawX();
                                    ClickY = event.getRawY();
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    break;
                                case MotionEvent.ACTION_UP:
                                    if (Math.abs(event.getRawX() - ClickX) < 20 && Math.abs(event.getRawY() - ClickY) < 20) {
                                        skip();
                                        if (subscription != null) {
                                            subscription.unsubscribe();
                                        }
                                        // 点击开屏广告处理
                                        nativelogic.OnClick(container);
                                    }
                                    break;
                            }
                            return true;
                        });
                    }

                }

                @Override
                public void onAdFailedToLoad(String error) {
                    tvSkip.setOnClickListener(v -> skip());
                    ImageBg();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("CheckResult")
    private void startCountDown() {
        try {
            subscription = RxCountDown.countdown(5)
                    .doOnSubscribe(() -> {

                    })
                    .subscribe(new Subscriber<Integer>() {
                        @Override
                        public void onCompleted() {
//                            if(!TextUtils.isEmpty(BasicApplication.url)){
//                                AdviseMoreDetailActivity.startActivity(StartActivity.this, "资讯", BasicApplication.url,"0");
//                                Log.e("getNotification", "getNotification: ");
//                                BasicApplication.url = "";
//                                SaveShare.saveValue(StartActivity.this, "skip", "skip");
//                                SaveShare.saveValue(StartActivity.this, "finish", "finish");
//                                SaveShare.saveValue(StartActivity.this, "location", "location");
//                                ISGDTSKIP = false;
//                            }else{
                                if(BasicApplication.getLocatedCity() != null && !TextUtils.isEmpty(BasicApplication.getLocatedCity().name)){
                                    intentActivity(MainActivity.class);
                                    finish();
                                }
                                if (ISGDTSKIP) {
                                    skip();
                                }
//                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(Integer integer) {
                            size = integer;
                            tvSkip.setText(getString(R.string.format_skip, integer));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void skip() {
        try {
            if (!TextUtils.isEmpty(SaveShare.getValue(StartActivity.this, "location")) && TextUtils.isEmpty(SaveShare.getValue(StartActivity.this, "skip"))) {
                if (mapLocation != null)
                    //启动定位
                    mapLocation.startLocation();
                if (TextUtils.isEmpty(SaveShare.getValue(StartActivity.this, "Guide"))) {
                    startActivity(new Intent(StartActivity.this, GuideActivity.class));
                } else {
                    intentActivity(MainActivity.class);
                }
 //                mapLocation.setData();
                this.finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchSplashAD(Activity activity, ViewGroup adContainer, View skipContainer,
                               String appId, String posId, SplashADListener adListener, int fetchDelay) {
        splashAD = new SplashAD(activity, skipContainer, appId, posId, adListener, fetchDelay);
        splashAD.fetchAndShowIn(adContainer);
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void loadViewLayout() {
        StartAd.getStartAd().loadViewLayout();
    }

    @Override
    protected void bindViews() {
        try {
            ivImage = findViewById(R.id.iv_image);
            initView();
//            String AppOpenBg = SaveShare.getValue(StartActivity.this, "AppOpenBg");
//            String Click_url = SaveShare.getValue(StartActivity.this, "Click_url");
//            if(!TextUtils.isEmpty(AppOpenBg) && !TextUtils.isEmpty(Click_url)){
//                AppOpenBg();
//            }
            AlmanacLogic.getAlmanacLogic().getAlmanacData(StartActivity.this, Utils.getDayOfWeekByDate());
            NewsRequest.getNewsRequest().NewsData(mContext);
            String ISAD = SaveShare.getValue(mContext, "ISAD");
            if (!TextUtils.isEmpty(ISAD) && ISAD.equals("1")) {
                loadAd();
            }
            mapLocation = MapLocation.getMapLocation();
            mapLocation.locate(StartActivity.this);
            startCountDown();
            StartAd.getStartAd().Application(StartActivity.this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ImageBg() {
        BackgroundRequest.getBackgroundRequest().getAppSpreadData(StartActivity.this, new RequestSyntony<AppSpread>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(AppSpread appSpread) {
                try {
                    if (appSpread.getData() != null) {
                        if (TextUtils.isEmpty(src)) {
                            Glide.with(StartActivity.this).load(appSpread.getData().getImg_src()).into(ivImage);
                            ivImage.setOnClickListener(v -> {
                                SaveShare.saveValue(StartActivity.this, "skip", "skip");
                                SaveShare.saveValue(StartActivity.this, "finish", "finish");
                                final Intent intent = new Intent(StartActivity.this, AdLinkActivity.class);
                                intent.putExtra(Const.REDIRECT_URI, appSpread.getData().getClick_url());
                                startActivityForResult(intent, 1);
                            });
                        }
                        SaveShare.saveValue(StartActivity.this, "AppOpenBg", appSpread.getData().getImg_src());
                        SaveShare.saveValue(StartActivity.this, "Click_url", appSpread.getData().getClick_url());
                    } else {
                        AppOpenBg();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void AppOpenBg() {
        try {
            Glide.with(StartActivity.this).load(SaveShare.getValue(StartActivity.this, "AppOpenBg")).into(ivImage);
            ivImage.setOnClickListener(v -> {
                final Intent intent = new Intent(StartActivity.this, AdLinkActivity.class);
                intent.putExtra(Const.REDIRECT_URI, SaveShare.getValue(StartActivity.this, "Click_url"));
                startActivityForResult(intent, 1);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        requestRequestPermission();
    }

    @Override
    protected void setListener() {
    }

    private void initView() {
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Util.isRoot()) {
                //做一些处理
                if (TextUtils.isEmpty(SaveShare.getValue(StartActivity.this, "location"))) {
                    ISGDTSKIP = false;
                }
            } else {
                SaveShare.saveValue(StartActivity.this, "location", "location");
                SaveShare.saveValue(StartActivity.this, "skip", "");
                SaveShare.saveValue(StartActivity.this, "finish", "finish");
            }

            tvVersionName = findViewById(R.id.tv_version_name);
            tvSkip = findViewById(R.id.tv_skip);
            container = findViewById(R.id.container);
            splash_skip_tv = findViewById(R.id.splash_skip_tv);
            ad_relative = findViewById(R.id.ad_relative);
            if (tvVersionName != null) {
                tvVersionName.setText(BuildConfig.VERSION_NAME);
                tvVersionName.setText(getResources().getString(R.string.about_tv));
            }
            tvSkip.setOnClickListener(v -> skip());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DisplayUtil.hasVirtualNavigationBar(this)) {
                findViewById(R.id.content_view).setPadding(0, 0, 0, DisplayUtil.getNavigationBarHeight(this));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    private void requestRequestPermission() {

    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            if (mapLocation != null)
                mapLocation.onStop();//停止定位后，本地定位服务并不会被销毁
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (mapLocation != null)
                mapLocation.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void granted() {
        try {
            if (mapLocation != null)
                //启动定位
                mapLocation.startLocation();
            ISGDTSKIP = true;
            SaveShare.saveValue(StartActivity.this, "location", "location");
            if (size == 0) {
                skip();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void denied(List<String> deniedList) {
        try {
            ISGDTSKIP = false;
            for (int i = 0; i < deniedList.size(); i++) {
                if ("android.permission.ACCESS_FINE_LOCATION".equals(deniedList.get(i)) && TextUtils.isEmpty(SaveShare.getValue(StartActivity.this, "location"))) {
                    mapLocation.checkLocationPermission(StartActivity.this, StartActivity.this);
                }
            }
            SaveShare.saveValue(StartActivity.this, "location", "location");
            if (mapLocation != null)
                //启动定位
                mapLocation.startLocation();

            if (size == 0) {
                skip();
            } else {
                ISGDTSKIP = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        ImageBg();
    }

    @Override
    public void onNext(Switch aSwitch) {
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            MobclickAgent.onPageStart("StartActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)
            MobclickAgent.onResume(this); //统计时长
            if (!TextUtils.isEmpty(SaveShare.getValue(StartActivity.this, "skip")) && !TextUtils.isEmpty(SaveShare.getValue(StartActivity.this, "finish"))) {
                SaveShare.saveValue(StartActivity.this, "skip", "");
                skip();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void dismiss() {
        SaveShare.saveValue(StartActivity.this, "skip", "");
        if (size == 0) {
            skip();
        }
    }

    @Override
    public void onADDismissed() {
        skip();
    }

    @Override
    public void onNoAD(AdError adError) {
        if (nativelogicDd != null) {
            nativelogicDd.OnRequest(adError.getErrorCode() + "", adError.getErrorMsg());
        }
        ImageBg();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onADPresent() {
        if (nativelogicDd != null) {
            nativelogicDd.OnRequest("0", "msg");
        }
//        tvSkip.setTextColor(R.color.white);
//        splash_skip_tv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onADClicked() {
        ISGDTSKIP = false;
        skip();
        if (nativelogicDd != null) {
            nativelogicDd.OnClick(container);
        }
    }

    @Override
    public void onADTick(long l) {
    }

    @Override
    public void onADExposure() {
        if (nativelogicDd != null) {
            nativelogicDd.AdShow(container);
        }
    }

    @Override
    public void onAdClicked(View view, int type) {
        if (nativelogicDd != null) {
            nativelogicDd.OnClick(container);
        }
        if (type == 3) {
            SaveShare.saveValue(StartActivity.this, "skip", "skip");
            SaveShare.saveValue(StartActivity.this, "finish", "finish");
        }
    }

    @Override
    public void onAdShow(View view, int type) {
        try {
            WeatherRequest.getWeatherRequest().getLookAtData(StartActivity.this, "广告", "展示");
            if (nativelogicDd != null) {
                nativelogicDd.AdShow(container);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAdSkip() {
        skip();
        if (nativelogicDd != null) {
            nativelogicDd.OnRequest("开屏广告跳过", "开屏广告跳过");
        }
    }

    @Override
    public void onAdTimeOver() {
        skip();
    }
}

