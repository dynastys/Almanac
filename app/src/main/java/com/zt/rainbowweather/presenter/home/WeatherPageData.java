package com.zt.rainbowweather.presenter.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.qq.e.ads.banner2.UnifiedBannerADListener;
import com.qq.e.ads.banner2.UnifiedBannerView;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.ads.nativ.NativeExpressMediaListener;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.util.AdError;
import com.tencent.smtt.sdk.WebChromeClient;
import com.zt.rainbowweather.BasicApplication;
import com.zt.rainbowweather.entity.city.CityEarlyWarning;
import com.zt.rainbowweather.entity.weather.WeatherVideo;
import com.zt.rainbowweather.presenter.PangolinBannerAd;
import com.zt.rainbowweather.ui.activity.IndexDetailsActivity;
import com.zt.rainbowweather.ui.adapter.MyPagerAdapter;
import com.zt.rainbowweather.ui.fragment.ListFragment;
import com.zt.rainbowweather.ui.fragment.TendencyFragment;
import com.zt.rainbowweather.view.MyVideoView;
import com.zt.rainbowweather.view.X5WebView;
import com.zt.rainbowweather.view.tab.OnTabSelectListener;
import com.zt.rainbowweather.view.tab.SegmentTabLayout;
import com.zt.weather.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xy.xylibrary.Interface.RlSimpleTarget;
import com.xy.xylibrary.base.BaseAdapter;
import com.xy.xylibrary.base.BaseFragment;
import com.xy.xylibrary.config.ColumnHorizontalPackage;
import com.xy.xylibrary.utils.GlideUtil;
import com.xy.xylibrary.utils.SaveShare;
import com.xy.xylibrary.utils.Utils;
import com.xy.xylibrary.view.ColumnHorizontalScrollView;
import com.xy.xylibrary.view.CustomScrollViewPager;
import com.zt.rainbowweather.api.RequestSyntony;
import com.zt.rainbowweather.entity.City;
import com.zt.rainbowweather.entity.OutLookWeather;
import com.zt.rainbowweather.entity.WeatherBean;
import com.zt.rainbowweather.entity.WeatherUtilBean;
import com.zt.rainbowweather.entity.background.BackdropTheme;
import com.zt.rainbowweather.entity.news.NewColumn;
import com.zt.rainbowweather.entity.news.Nnotice;
import com.zt.rainbowweather.entity.weather.AirThDay;
import com.zt.rainbowweather.entity.weather.ConventionWeather;
import com.zt.rainbowweather.entity.weather.Notification;
import com.zt.rainbowweather.presenter.GridItemDecoration;
import com.zt.rainbowweather.presenter.request.BackgroundRequest;
import com.zt.rainbowweather.presenter.request.NewsRequest;
import com.zt.rainbowweather.presenter.request.WeatherRequest;
import com.zt.rainbowweather.ui.activity.AdviseMoreDetailActivity;
import com.zt.rainbowweather.ui.activity.IndexOfLivingActivity;
import com.zt.rainbowweather.ui.fragment.ColumnFragment;
import com.zt.rainbowweather.ui.fragment.WeatherFragment;
import com.zt.rainbowweather.ui.service.CancelNoticeService;
import com.zt.rainbowweather.utils.AutoVerticalScrollTextViewUtil;
import com.zt.rainbowweather.utils.ConstUtils;
import com.zt.rainbowweather.utils.SizeUtils;
import com.zt.rainbowweather.utils.WeatherUtils;
import com.zt.rainbowweather.view.AutoVerticalScrollTextView;
import com.zt.rainbowweather.view.MiuiWeatherView;
import com.zt.rainbowweather.view.TranslucentActionBar;
import com.zt.rainbowweather.view.TranslucentScrollView;
import com.zt.xuanyin.Interface.AdProtogenesisListener;
import com.zt.xuanyin.controller.Ad;
import com.zt.xuanyin.controller.NativeAd;
import com.zt.xuanyin.entity.model.Native;

import org.litepal.LitePal;
import org.salient.artplayer.MediaPlayerManager;
import org.salient.artplayer.ui.ControlPanel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class WeatherPageData implements RequestSyntony<BackdropTheme>, RlSimpleTarget, NativeExpressAD.NativeExpressADListener, NativeExpressMediaListener {

    private AppCompatActivity context;
    private RecyclerView recyclerView;
    private MiuiWeatherView weather;
    //    private ScrollFutureDaysWeatherView scrollFutureDaysWeatherView;
    private RelativeLayout relWetherBg;
    private ColumnHorizontalPackage columnHorizontalPackage;
    private ArrayList<ColumnFragment> fragments = new ArrayList<>();
    /*栏目数据*/
    private List<String> userColumnList = new ArrayList<>();
    private String[] ID = new String[]{"26", "28", "29", "30", "31", "32", "27", "33", "34", "35", "36", "37", "38", "40", "39", "32"};
    private NativeAd nativelogic, nativelogic1, nativelogic2;
    public static Notification notification;
    private List<AirThDay.HeWeather6Bean.AirForecastBean> air_forecast = new ArrayList<>();
    private BaseFragment fragment;
    private LinearLayout mRadioGroupContent;
    private ColumnHorizontalScrollView column;
    private CustomScrollViewPager viewpagerColumn;
    private List<OutLookWeather> lookWeathers = new ArrayList<>();
    private List<ConventionWeather.HeWeather6Bean.LifestyleBean> lifestyleBeans = new ArrayList<>();
    private RlSimpleTarget rlSimpleTarget;
    private City city;
    private AutoVerticalScrollTextViewUtil autoVerticalScrollTextViewUtil;
    private ConventionWeather conventionWeatherData;
    private String[] mTitles_3 = {"趋势", "列表"};
    private ArrayList<BaseFragment> mFragments = new ArrayList<>();
    private SegmentTabLayout tabVp7;
    private ViewPager vp7;
    private WeatherFragment weatherFragment;
    private GridItemDecoration divider;
    private MediaController mediaController;

    public WeatherPageData(AppCompatActivity activity) {
        this.context = activity;
    }

    public WeatherPageData(AppCompatActivity activity, WeatherFragment weatherFragment, RecyclerView recyclerView, MiuiWeatherView weather, RelativeLayout relWetherBg, SegmentTabLayout tabVp7, ViewPager vp7) {
        this.context = activity;
        this.recyclerView = recyclerView;
        this.weather = weather;
        this.relWetherBg = relWetherBg;
        this.tabVp7 = tabVp7;
        this.vp7 = vp7;
        this.weatherFragment = weatherFragment;
    }


    /**
     * 使用自定义webview播放视频
     */
    public void startPlay(org.salient.artplayer.VideoView x5VideoWebview, String vedioUrl,String ImagrBg,String source) {
        x5VideoWebview.setUp(vedioUrl);
        final ControlPanel controlPanel = new ControlPanel(context);
        x5VideoWebview.setControlPanel(controlPanel);
        ((ImageView) controlPanel.findViewById(R.id.video_cover)).setImageResource(0);
        TextView tvTitle = controlPanel.findViewById(R.id.tvTitle);
        tvTitle.setText(source);
        MediaPlayerManager.instance().setMute(true);
        ((CheckBox)controlPanel.findViewById(R.id.ivVolume)).setChecked(false);
        GlideUtil.getGlideUtil().setImages(context,ImagrBg,(ImageView) controlPanel.findViewById(R.id.video_cover));
//        x5VideoWebview.startFullscreen(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
//        WeatherRequest.getWeatherRequest().getLookAtData(getActivity(), "天气预报", "播放视频");
//        videoView.setVideoPath(vedioUrl);
//        //创建MediaController对象
//        mediaController = new MediaController(context);
//        //VideoView与MediaController建立关联
//        videoView.setMediaController(mediaController);
//        //让VideoView获取焦点
//        videoView.requestFocus();
    }

    public MediaController getMediaController() {
        if(mediaController != null){
            mediaController.hide();
        }
        return mediaController;
    }

    public Boolean checkIsVisible(View view) {
        // 如果已经加载了，判断广告view是否显示出来，然后曝光
        int screenWidth = Utils.getWindowsWidth(context);
        int screenHeight =  Utils.getScreenHeight(context);
        Rect rect = new Rect(0, 0, screenWidth, screenHeight);
        int[] location = new int[2];
        view.getLocationInWindow(location);
        if (view.getLocalVisibleRect(rect)) {
            return true;
        } else {
            //view已不在屏幕可见区域;
            return false;
        }
    }

    /**
     * 新闻视频和预警
     * */
    public void VideoWarning(TextView videoT, org.salient.artplayer.VideoView x5VideoWebview){
        WeatherRequest.getWeatherRequest().getWeatherVideoData(context, new RequestSyntony<WeatherVideo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(WeatherVideo weatherVideo) {
                if(weatherVideo != null && weatherVideo.getData() != null){
//                    GlideUtil.getGlideUtil().setImages1(context,weatherVideo.getData().getCover(),imageView);
                    videoT.setText(weatherVideo.getData().getType());
                    startPlay(x5VideoWebview,weatherVideo.getData().getVideourl(),weatherVideo.getData().getCover(),weatherVideo.getData().getSource());
                }
            }
        });


    }
    /**
     * 轮播新闻
     * */
    public void NnoticeData(AutoVerticalScrollTextView keyword) {
        NewsRequest.getNewsRequest().getNnoticeData(context, new RequestSyntony<Nnotice>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Nnotice nnotice) {
                // 初始化AutoVerticalScrollTextView控制器
                autoVerticalScrollTextViewUtil = new AutoVerticalScrollTextViewUtil(keyword, nnotice.getData());
                // 设置滚动的时间间隔
                autoVerticalScrollTextViewUtil.setDuration(5000);
                // 开启滚动
                autoVerticalScrollTextViewUtil.start();

                // 点击事件监听
                autoVerticalScrollTextViewUtil.setOnMyClickListener((position, dataBean) -> {
                    if (dataBean != null) {
                        AdviseMoreDetailActivity.startActivity(context, dataBean.getTitle(), dataBean.getHtml_url(),"1");
                    }
                    if (autoVerticalScrollTextViewUtil.getIsRunning())
                        autoVerticalScrollTextViewUtil.stop(); // 停止滚动
                    else
                        autoVerticalScrollTextViewUtil.start(); // 开启滚动
                });

            }
        });
    }


    private void tl_3(SegmentTabLayout mTabLayout_3, ViewPager vp_3, List<OutLookWeather> lookWeathers, City city) {
        try {
            mFragments.clear();
            mFragments.add(new TendencyFragment(lookWeathers, city));
            mFragments.add(new ListFragment(lookWeathers, city));
            vp_3.setAdapter(new MyPagerAdapter(weatherFragment.getChildFragmentManager(), mFragments));
            mTabLayout_3.setTabData(mTitles_3);
            mTabLayout_3.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelect(int position) {
                    vp_3.setCurrentItem(position);
                }

                @Override
                public void onTabReselect(int position) {
                }
            });

            vp_3.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    mTabLayout_3.setCurrentTab(position);
                    SaveShare.saveValue(context, "Vp", position + "");
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            vp_3.setCurrentItem(TextUtils.isEmpty(SaveShare.getValue(context, "Vp")) ? 0 : Integer.parseInt(SaveShare.getValue(context, "Vp")));
        } catch (NumberFormatException e) {
            vp_3.setCurrentItem(0);
            e.printStackTrace();
        }
    }


    public void onResume() {
        // 开启滚动
        if (autoVerticalScrollTextViewUtil != null) {
            autoVerticalScrollTextViewUtil.start();
        }
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(BackdropTheme backdropTheme) {
        SaveShare.saveValue(context, "INUSE", backdropTheme.getData().getImg_src());
        GlideUtil.getGlideUtil().getDrawableImages(context, backdropTheme.getData().getImg_src(), WeatherPageData.this);

    }

    @Override
    public void onResourceReady(Drawable resource) {
        rlSimpleTarget.onResourceReady(resource);

    }

    @Override
    public void onADLoaded(List<NativeExpressADView> adList) {
        // 释放前一个 NativeExpressADView 的资源
        relatAd.setVisibility(View.VISIBLE);
        if (nativeExpressADView != null) {
            nativeExpressADView.destroy();
        }
        // 3.返回数据后，SDK 会返回可以用于展示 NativeExpressADView 列表
        nativeExpressADView = adList.get(0);
        if (nativeExpressADView.getBoundData().getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
            nativeExpressADView.setMediaListener(WeatherPageData.this);
        }
        nativeExpressADView.render();
        if (relatAd.getChildCount() > 0) {
            relatAd.removeAllViews();
        }
        // 需要保证 View 被绘制的时候是可见的，否则将无法产生曝光和收益。
        relatAd.addView(nativeExpressADView);
    }


    @Override
    public void onRenderFail(NativeExpressADView nativeExpressADView) {
        relatAd.setVisibility(View.GONE);
    }

    @Override
    public void onRenderSuccess(NativeExpressADView nativeExpressADView) {
        if (nativelogic != null) {
            nativelogic.OnRequest("0", "msg");
        }
    }

    @Override
    public void onADExposure(NativeExpressADView nativeExpressADView) {
        Log.i("TAG", "onADExposure");
        try {
            if (nativelogic != null) {
                nativelogic.AdShow(null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onADClicked(NativeExpressADView nativeExpressADView) {
        Log.i("TAG", "onADClicked");
        try {
            if (nativelogic != null) {
                nativelogic.OnClick(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onADClosed(NativeExpressADView nativeExpressADView) {
        Log.i("TAG", "onADClosed");

    }

    @Override
    public void onADLeftApplication(NativeExpressADView nativeExpressADView) {
        Log.i("TAG", "onADLeftApplication");

    }

    @Override
    public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {
        Log.i("TAG", "onADOpenOverlay");

    }

    @Override
    public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {
        Log.i("TAG", "onADCloseOverlay");

    }

    @Override
    public void onNoAD(AdError adError) {
        Log.i("TAG", "onNoAD");
        try {
            if (nativelogic != null) {
                nativelogic.OnRequest(adError.getErrorCode() + "", adError.getErrorMsg());
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onVideoInit(NativeExpressADView nativeExpressADView) {
        Log.i("TAG", "onVideoInit");

    }

    @Override
    public void onVideoLoading(NativeExpressADView nativeExpressADView) {
        Log.i("TAG", "onVideoLoading");

    }

    @Override
    public void onVideoReady(NativeExpressADView nativeExpressADView, long l) {
        Log.i("TAG", "onVideoReady");

    }

    @Override
    public void onVideoStart(NativeExpressADView nativeExpressADView) {
        Log.i("TAG", "onVideoStart");

    }

    @Override
    public void onVideoPause(NativeExpressADView nativeExpressADView) {
        Log.i("TAG", "onVideoPause");

    }

    @Override
    public void onVideoComplete(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onVideoError(NativeExpressADView nativeExpressADView, AdError adError) {

    }

    @Override
    public void onVideoPageOpen(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onVideoPageClose(NativeExpressADView nativeExpressADView) {

    }

    public interface AirQualityListener {
        void AirQuality(List<AirThDay.HeWeather6Bean.AirForecastBean> air_forecast);
    }

    public void Icons(ImageView wetherBgImage, int backdrop_theme_id, int current_img_id, ConventionWeather.HeWeather6Bean.BasicBean basicBean, RlSimpleTarget rlSimpleTarget) {
        this.rlSimpleTarget = rlSimpleTarget;
        BackgroundRequest.getBackgroundRequest().getBackdropThemeIdData(context, backdrop_theme_id, 0, basicBean.getLocation(), basicBean.getParent_city(), basicBean.getAdmin_area(), WeatherPageData.this);
    }

    /**
     * 获取天气缓存
     * */
    public boolean WeatherDataCacheGain(City city,RequestSyntony<ConventionWeather> requestSyntony){
        try {
            ConventionWeather.HeWeather6Bean.BasicBean basicBean =  LitePal.findFirst(ConventionWeather.HeWeather6Bean.BasicBean.class);
//                    LitePal.where("location = ?",city.name).findFirst(ConventionWeather.HeWeather6Bean.BasicBean.class);
            if(basicBean != null){
                ConventionWeather conventionWeather = new ConventionWeather();
                List<ConventionWeather.HeWeather6Bean> heWeather6Beans = new ArrayList<>();
                ConventionWeather.HeWeather6Bean heWeather6Bean = new ConventionWeather.HeWeather6Bean();
                heWeather6Bean.setBasic(LitePal.findFirst(ConventionWeather.HeWeather6Bean.BasicBean.class));
                heWeather6Bean.setUpdate(LitePal.findFirst(ConventionWeather.HeWeather6Bean.UpdateBean.class));
                heWeather6Bean.setNow(LitePal.findFirst(ConventionWeather.HeWeather6Bean.NowBean.class));
                heWeather6Bean.setDaily_forecast(LitePal.findAll(ConventionWeather.HeWeather6Bean.DailyForecastBean.class));
                heWeather6Bean.setHourly(LitePal.findAll(ConventionWeather.HeWeather6Bean.HourlyBean.class));
                heWeather6Bean.setLifestyle(LitePal.findAll(ConventionWeather.HeWeather6Bean.LifestyleBean.class));
                heWeather6Beans.add(heWeather6Bean);
                conventionWeather.setHeWeather6(heWeather6Beans);
                requestSyntony.onNext(conventionWeather);
                SaveShare.saveValue(context, "sunrise", conventionWeather.getHeWeather6().get(0).getDaily_forecast().get(0).getSr());
                SaveShare.saveValue(context, "sunset", conventionWeather.getHeWeather6().get(0).getDaily_forecast().get(0).getSs());
                setExponent(conventionWeather.getHeWeather6().get(0).getLifestyle());
                HourWeather(conventionWeather.getHeWeather6().get(0).getHourly());
                DayTendency(conventionWeather.getHeWeather6().get(0).getDaily_forecast());
//                addWeatherData(conventionWeather,rainAlarmPro,rainAlarmProLin,airQualityListener,requestSyntony);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    /**
     * 天气数据缓存
     * */
    private void WeatherDataCache(ConventionWeather conventionWeather){
        try {
            LitePal.deleteAll(ConventionWeather.HeWeather6Bean.BasicBean.class);
            LitePal.deleteAll(ConventionWeather.HeWeather6Bean.UpdateBean.class);
            LitePal.deleteAll(ConventionWeather.HeWeather6Bean.NowBean.class);
            LitePal.deleteAll(ConventionWeather.HeWeather6Bean.DailyForecastBean.class);
            LitePal.deleteAll(ConventionWeather.HeWeather6Bean.HourlyBean.class);
            LitePal.deleteAll(ConventionWeather.HeWeather6Bean.LifestyleBean.class);
            ConventionWeather.HeWeather6Bean.BasicBean basic = conventionWeather.getHeWeather6().get(0).getBasic();
            ConventionWeather.HeWeather6Bean.UpdateBean update = conventionWeather.getHeWeather6().get(0).getUpdate();
            String status = conventionWeather.getHeWeather6().get(0).getStatus();
            ConventionWeather.HeWeather6Bean.NowBean now = conventionWeather.getHeWeather6().get(0).getNow();
            List<ConventionWeather.HeWeather6Bean.DailyForecastBean> daily_forecast = conventionWeather.getHeWeather6().get(0).getDaily_forecast();
            List<ConventionWeather.HeWeather6Bean.HourlyBean> hourly = conventionWeather.getHeWeather6().get(0).getHourly();
            List<ConventionWeather.HeWeather6Bean.LifestyleBean> lifestyle = conventionWeather.getHeWeather6().get(0).getLifestyle();
            basic.save();
            update.save();
            now.save();
            LitePal.saveAll(daily_forecast);
            LitePal.saveAll(hourly);
            LitePal.saveAll(lifestyle);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void addWeatherData(ConventionWeather conventionWeather,TextView rainAlarmPro, LinearLayout rainAlarmProLin,AirQualityListener airQualityListener,RequestSyntony<ConventionWeather> requestSyntony){
        try {
            conventionWeatherData = conventionWeather;
            requestSyntony.onNext(conventionWeather);
            SaveShare.saveValue(context, "sunrise", conventionWeather.getHeWeather6().get(0).getDaily_forecast().get(0).getSr());
            SaveShare.saveValue(context, "sunset", conventionWeather.getHeWeather6().get(0).getDaily_forecast().get(0).getSs());
            setExponent(conventionWeather.getHeWeather6().get(0).getLifestyle());
            HourWeather(conventionWeather.getHeWeather6().get(0).getHourly());
//                            SwitchDynamicWeather(conventionWeather.getHeWeather6().get(0).getNow().getCond_txt(), relWetherBg);
            WeatherRequest.getWeatherRequest().getAirThDayData(context, city.name, new RequestSyntony<AirThDay>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onNext(AirThDay airThDay) {
                    try {
                        if (airThDay != null && airThDay.getHeWeather6() != null) {
                            air_forecast = airThDay.getHeWeather6().get(0).getAir_forecast();
                            airQualityListener.AirQuality(air_forecast);
                            DayTendency(conventionWeather.getHeWeather6().get(0).getDaily_forecast());
                            AlterNotification();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            WeatherRequest.getWeatherRequest().getCityEarlyWarningData(context, conventionWeather.getHeWeather6().get(0).getBasic().getAdmin_area(),  conventionWeather.getHeWeather6().get(0).getBasic().getParent_city(),  conventionWeather.getHeWeather6().get(0).getBasic().getLocation(), new RequestSyntony<CityEarlyWarning>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onNext(CityEarlyWarning cityEarlyWarning) {
                    if(cityEarlyWarning != null && cityEarlyWarning.getData() != null && rainAlarmPro != null){
                        rainAlarmPro.setVisibility(View.VISIBLE);
                        rainAlarmProLin.setVisibility(View.VISIBLE);
                        rainAlarmPro.setText(cityEarlyWarning.getData().getWarning_type());
//                        GlideUtil.getGlideUtil().setImages(context, cityEarlyWarning.getData().getIcon(), rainAlarmPro,1);
                        rainAlarmPro.setOnClickListener(v -> {
                            AdviseMoreDetailActivity.startActivity(context, cityEarlyWarning.getData().getWarning_type(), cityEarlyWarning.getData().getWarning_url(),"0");
                        });
                    }else{
                        rainAlarmProLin.setVisibility(View.GONE);
                    }
                }
            });
            WeatherDataCache(conventionWeather);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 天气数据请求
     */
    public void RequestWeatherData(City city,TextView rainAlarmPro,LinearLayout rainAlarmProLin, RequestSyntony<ConventionWeather> requestSyntony, AirQualityListener airQualityListener,int type) {
        this.city = city;
        if(type == 0){
            if(WeatherDataCacheGain(city,requestSyntony)){
                ConventionWeatherData(city,rainAlarmPro,rainAlarmProLin,requestSyntony,airQualityListener);
            }
        }else{
            ConventionWeatherData(city,rainAlarmPro,rainAlarmProLin,requestSyntony,airQualityListener);
        }

    }

    private void ConventionWeatherData(City city,TextView rainAlarmPro,LinearLayout rainAlarmProLin,RequestSyntony<ConventionWeather> requestSyntony, AirQualityListener airQualityListener){
        WeatherRequest.getWeatherRequest().getConventionWeatherData(context, city.name, new RequestSyntony<ConventionWeather>() {

            @Override
            public void onCompleted() {
                requestSyntony.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                requestSyntony.onError(e);
            }

            @Override
            public void onNext(ConventionWeather conventionWeather) {
                if (conventionWeather != null && conventionWeather.getHeWeather6() != null && conventionWeather.getHeWeather6().size() > 0) {
                    addWeatherData(conventionWeather,rainAlarmPro,rainAlarmProLin,airQualityListener,requestSyntony);

                }
            }
        });
    }
    public void AlterNotification() {
        try {
            if (city != null) {
                notification = new Notification();
                notification.city = city.name;
                ColumnFragment.cityName = city.name;
                 if(conventionWeatherData.getHeWeather6() != null && conventionWeatherData.getHeWeather6().size() > 0){
                    notification.cond_txt = conventionWeatherData.getHeWeather6().get(0).getNow().getCond_txt() == null ? "" : conventionWeatherData.getHeWeather6().get(0).getNow().getCond_txt();
                    if(air_forecast.size() > 0){
                        notification.qlty = air_forecast.get(0).getQlty() + " " + air_forecast.get(0).getAqi();
                    }
                    notification.time = System.currentTimeMillis();
                    notification.tmp = conventionWeatherData.getHeWeather6().get(0).getNow().getTmp();
                    notification.tmp_max_min = conventionWeatherData.getHeWeather6().get(0).getDaily_forecast().get(0).getTmp_max() + "°/" + conventionWeatherData.getHeWeather6().get(0).getDaily_forecast().get(0).getTmp_min() + "°";
                    notification.cond_code = conventionWeatherData.getHeWeather6().get(0).getNow().getCond_code() == null ? "100" : conventionWeatherData.getHeWeather6().get(0).getNow().getCond_code();
                }
            }
            String notifications = SaveShare.getValue(context, "Notifications");
            if (TextUtils.isEmpty(notifications)) {
                SaveShare.saveValue(context, "ISNotification", "1");
                SaveShare.saveValue(context, "Notifications", "1");
            }
            String notification2 = SaveShare.getValue(context, "ISNotification");
            if (notification != null && !TextUtils.isEmpty(notification2)) {
                Intent intent = new Intent(context, CancelNoticeService.class);
                intent.putExtra("notification", notification);
                context.startService(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 新闻栏目
     */
    public void RequestNewsData(BaseFragment fragment, LinearLayout mRadioGroupContent, ColumnHorizontalScrollView column, CustomScrollViewPager viewpagerColumn) {
        try {
            this.fragment = fragment;
            this.mRadioGroupContent = mRadioGroupContent;
            this.column = column;
            this.viewpagerColumn = viewpagerColumn;
            if (ConstUtils.home_news) {
                List<NewColumn.DataBean> dateBeanList = LitePal.findAll(NewColumn.DataBean.class);
                if (dateBeanList != null && dateBeanList.size() > 0) {
                    Column(dateBeanList);
                } else {
                    NewsRequest.getNewsRequest().getNewsColumnData(context, new RequestSyntony<NewColumn>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(NewColumn newColumn) {
                            Column(newColumn.getData());
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Column(List<NewColumn.DataBean> dateBeanList) {
        try {
//            fragments.clear();
//            userColumnList.clear();
//            columnHorizontalPackage = null;
            for (int i = 0; i < dateBeanList.size(); i++) {
                //传递数据到fragment
                Bundle data = new Bundle();
                if (i == 0) {
                    SaveShare.saveValue(context, "Channelid", dateBeanList.get(i).getChannelid() + "");
                    SaveShare.saveValue(context, "ColumnName", dateBeanList.get(i).getChannel_name() + "");
                }
                data.putString("text", "" + dateBeanList.get(i).getChannelid());
                data.putString("ColumnName", "" + dateBeanList.get(i).getChannel_name());
                if(notification != null){
                    data.putString("city", notification.city);
                }
                ColumnFragment newfragment = new ColumnFragment();
                newfragment.setviewPager(viewpagerColumn);
                newfragment.setArguments(data);
                //加载fragment
                fragments.add(newfragment);
                userColumnList.add(dateBeanList.get(i).getChannel_name());
            }

            columnHorizontalPackage = new ColumnHorizontalPackage<String>(context, column, viewpagerColumn, true);
            columnHorizontalPackage.initData(fragment, mRadioGroupContent, fragments, userColumnList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 刷新
     */
    public void setSwipeRefreshOnRefresh(SmartRefreshLayout shopListSwipeRefresh) {
//        //刷新
//        SwipeRefreshOnRefresh swipeRefreshOnRefresh = new SwipeRefreshOnRefresh();
//        swipeRefreshOnRefresh.SwipeRefresh(context, shopListSwipeRefresh, swipeRefreshListener);
    }

    /**
     * 渐变状态栏
     */
    public void GradientStatusBar(TranslucentScrollView DetailsScrollview, TranslucentActionBar DetailsActionbar, TranslucentScrollView.TranslucentChangedListener translucentChangedListener) {

        try {
            //初始actionBar
            DetailsActionbar.setData(null, 0, null, 0, null, null);
            //开启渐变
            DetailsActionbar.setNeedTranslucent();
            //设置状态栏高度
            DetailsActionbar.setStatusBarHeight((Utils.getStatusBarHeight() + SizeUtils.dp2px(5)));
            //设置透明度变化监听
            DetailsScrollview.setTranslucentChangedListener(translucentChangedListener);
            //关联需要渐变的视图
            DetailsScrollview.setTransView(DetailsActionbar);
            //设置ActionBar键渐变颜色
            DetailsScrollview.setTransColor(context.getResources().getColor(R.color.tab_color));
            //关联伸缩的视图
//    shopDetailsScrollview.setPullZoomView(shopDetailsBanner);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private List<String> listSize = new ArrayList<>();
    /**
     * 指数
     */
    public void setExponent(List<ConventionWeather.HeWeather6Bean.LifestyleBean> lifestyleBeans) {
        try {
            this.lifestyleBeans = lifestyleBeans;
//        ScrollLinearLayoutManager linearLayoutManager = new ScrollLinearLayoutManager(context);
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
            if (divider == null) {
                divider = new GridItemDecoration.Builder(context)
                        .setHorizontalSpan(R.dimen.dp_1)
                        .setVerticalSpan(R.dimen.dp_1)
                        .setColorResource(R.color.halving_line)
                        .setShowLastLine(true)
                        .build();
                recyclerView.addItemDecoration(divider);
            }

            List<ConventionWeather.HeWeather6Bean.LifestyleBean> list = new ArrayList<>();
            List<Integer> images = new ArrayList<>();
            for (int i = 0; i < lifestyleBeans.size(); i++) {
                switch (lifestyleBeans.get(i).getType()) {
                    case "air":
                        lifestyleBeans.get(i).setType("空气");
//                        list.add(lifestyleBeans.get(i));
                        images.add(R.mipmap.air_index);
                        break;
                    case "comf":
                        lifestyleBeans.get(i).setType("舒适度");
                        list.add(lifestyleBeans.get(i));
                        images.add(R.mipmap.comfort_level);
                        listSize.add(i+"");
                        break;
                    case "drsg":
                        lifestyleBeans.get(i).setType("穿衣");
                        list.add(lifestyleBeans.get(i));
                        images.add(R.mipmap.dress);
                        listSize.add(i+"");
                        break;
                    case "uv":
                        lifestyleBeans.get(i).setType("紫外线");
                        list.add(lifestyleBeans.get(i));
                        images.add(R.mipmap.ultraviolet_ray);
                        listSize.add(i+"");
                        break;
                    case "sport":
                        lifestyleBeans.get(i).setType("运动");
                        list.add(lifestyleBeans.get(i));
                        images.add(R.mipmap.exercise);
                        listSize.add(i+"");
                        break;
                    case "flu":
                        lifestyleBeans.get(i).setType("感冒");
                        list.add(lifestyleBeans.get(i));
                        images.add(R.mipmap.influenza);
                        listSize.add(i+"");
                        break;
                    case "cw":
                        lifestyleBeans.get(i).setType("洗车");
//                        list.add(lifestyleBeans.get(i));
                        images.add(R.mipmap.car_wash);
                        break;
                    case "ac":
                        lifestyleBeans.get(i).setType("空调");
//                        list.add(lifestyleBeans.get(i));
                        images.add(R.mipmap.ac_icon);
                        break;
                    case "ag":
                        lifestyleBeans.get(i).setType("过敏");
//                        list.add(lifestyleBeans.get(i));
                        images.add(R.mipmap.irritability_icon);
                        break;
                    case "fsh":
                        lifestyleBeans.get(i).setType("钓鱼");
//                        list.add(lifestyleBeans.get(i));
                        images.add(R.mipmap.fsh_icon);
                        break;
                    case "ptfc":
                        lifestyleBeans.get(i).setType("交通");
                        list.add(lifestyleBeans.get(i));
                        images.add(R.mipmap.ptfc_icon);
                        listSize.add(i+"");
                        break;
                    case "airc":
                        lifestyleBeans.get(i).setType("晾晒");
                        list.add(lifestyleBeans.get(i));
                        images.add(R.mipmap.airc_icon);
                        listSize.add(i+"");
                        break;
                    case "mu":
                        lifestyleBeans.get(i).setType("化妆");
//                        list.add(lifestyleBeans.get(i));
                        images.add(R.mipmap.mu_icon);
                        break;
                    case "gl":
                        lifestyleBeans.get(i).setType("太阳镜");
//                        list.add(lifestyleBeans.get(i));
                        images.add(R.mipmap.gl_icon);
                        break;
                    case "trav":
                        lifestyleBeans.get(i).setType("旅游");
                        list.add(lifestyleBeans.get(i));
                        images.add(R.mipmap.trav_icon);
                        listSize.add(i+"");
                        break;
                    case "spi":
                        lifestyleBeans.get(i).setType("防晒");
//                        list.add(lifestyleBeans.get(i));
                        images.add(R.mipmap.spi_icon);
                        break;
                }

            }
//            ConventionWeather.HeWeather6Bean.LifestyleBean lifestyleBean = new ConventionWeather.HeWeather6Bean.LifestyleBean();
//            lifestyleBean.setType("更多>>");
//            list.add(lifestyleBean);
            BaseAdapter baseAdapter = new BaseAdapter<>(R.layout.recycler_item, list, (viewHolder, item) -> {
                try {
                    if (TextUtils.isEmpty(item.getTxt())) {
                        viewHolder.getView(R.id.recycler_image).setVisibility(View.GONE);
                        viewHolder.setText(R.id.popup_recycler_tv, item.getType());
                        viewHolder.getView(R.id.popup_recycler_tv).setVisibility(View.VISIBLE);
                        ((TextView) viewHolder.getView(R.id.popup_recycler_tv)).setTextColor(Color.WHITE);
                    } else {
                        viewHolder.setText(R.id.brf, item.getBrf());
                        viewHolder.setText(R.id.popup_recycler_tv, item.getType() + "指数");
                        //                    viewHolder.setImageResource(R.id.popup_recycler_image, imgs[viewHolder.getAdapterPosition()]);
                        GlideUtil.getGlideUtil().setImages(context, images.get(viewHolder.getAdapterPosition()), (ImageView) viewHolder.getView(R.id.recycler_image));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            baseAdapter.setOnItemClickListener((adapter, view, position) -> {
                try {
                    if (TextUtils.isEmpty(list.get(position).getTxt())) {
                        IndexOfLivingActivity.startActivity(context, lifestyleBeans);
                    } else {
//                        AdviseTitleBean adviseTitleBean = new AdviseTitleBean();
//                        adviseTitleBean.contentUrl = "https://sec-cdn.static.xiaomi.net/secStatic/imgs/b769c44e846b6f0fed3c9780d0bf431aae425f02.png";
//                        adviseTitleBean.headerImgUrl = "https://sec-cdn.static.xiaomi.net/secStatic/imgs/b769c44e846b6f0fed3c9780d0bf431aae425f02.png";
//                        adviseTitleBean.imgUrl = "http://f4.market.xiaomi.com/download/MiSafe/001a2c4210f6944e83427fd96c3216666b4de8646/a.webp";
//                        adviseTitleBean.title = list.get(position).getBrf();
//                        adviseTitleBean.headerSummary = list.get(position).getTxt();
//                        adviseTitleBean.channelId = list.get(position).getType();
//                        adviseTitleBean.indexId = ID[position];
//                        AdviseDetailActivity.startActivity(context, adviseTitleBean);
                        Intent IndexIntent = new Intent(context, IndexDetailsActivity.class);
                        IndexIntent.putExtra("datas", (Serializable) getLifestyleBean());
                        IndexIntent.putExtra("Size", listSize.get((position > listSize.size()? 0 : position))+"");
                        IndexIntent.putExtra("City", city.name);
                        context.startActivity(IndexIntent);
                    }
                } catch (Exception e) {
                    Log.e("context", "setExponent: " + e.getMessage());
                    e.printStackTrace();
                }
            });
            recyclerView.setAdapter(baseAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 趋势预报
     */
    public void DayTendency(List<ConventionWeather.HeWeather6Bean.DailyForecastBean> DailyForecast) {
        try {
            Random ra1 = new Random();
            lookWeathers.clear();
            for (int i = 0; i < DailyForecast.size(); i++) {
                OutLookWeather outLookWeather = new OutLookWeather();
                if (i == 0) {
                    outLookWeather.week = "今天";
                } else if (i == 1) {
                    outLookWeather.week = "明天";
                } else {
                    outLookWeather.week = WeatherUtils.getWeek(DailyForecast.get(i).getDate());
                }
                if (!TextUtils.isEmpty(DailyForecast.get(i).getDate()) && DailyForecast.get(i).getDate().length() >= 4) {
                    outLookWeather.date = DailyForecast.get(i).getDate().substring(DailyForecast.get(i).getDate().length() - 5, DailyForecast.get(i).getDate().length());
                }
                if(air_forecast != null &&  air_forecast.size() > 0){
                    outLookWeather.airQuality = air_forecast.get(i).getQlty();
                }
                WeatherUtilBean weatherDayD = new WeatherUtilBean();
                weatherDayD.iconRes = Integer.parseInt(DailyForecast.get(i).getCond_code_d());
                weatherDayD.weather = DailyForecast.get(i).getCond_txt_d();
                weatherDayD.weatherId = Integer.parseInt(DailyForecast.get(i).getCond_code_d());
                outLookWeather.weatherDay = weatherDayD;
                WeatherUtilBean weatherDayN = new WeatherUtilBean();
                weatherDayN.iconRes = Integer.parseInt(DailyForecast.get(i).getCond_code_n());
                weatherDayN.weather = DailyForecast.get(i).getCond_txt_n();
                weatherDayN.weatherId = Integer.parseInt(DailyForecast.get(i).getCond_code_n());
                outLookWeather.weatherNight = weatherDayN;
                outLookWeather.wind = DailyForecast.get(i).getWind_dir();
                outLookWeather.windLevel = DailyForecast.get(i).getWind_sc() + "级";
                outLookWeather.highTemperature = Integer.parseInt(DailyForecast.get(i).getTmp_max());
                outLookWeather.lowTemperature = Integer.parseInt(DailyForecast.get(i).getTmp_min());
                outLookWeather.uv_index = DailyForecast.get(i).getUv_index();
                outLookWeather.sr = DailyForecast.get(i).getSr();
                outLookWeather.ss = DailyForecast.get(i).getSs();
                outLookWeather.fl = "";
                lookWeathers.add(outLookWeather);
            }
            tl_3(tabVp7, vp7, lookWeathers, city);
//            scrollFutureDaysWeatherView.setData(lookWeathers, city);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public List<OutLookWeather> getOutLookWeathers() {
        if (lookWeathers.size() > 0) {
            return lookWeathers;
        }
        return lookWeathers;
    }

    public List<ConventionWeather.HeWeather6Bean.LifestyleBean> getLifestyleBean() {
        if (lifestyleBeans.size() > 0) {
            return lifestyleBeans;
        }
        return lifestyleBeans;
    }

    /**
     * 小时预报
     */
    public void HourWeather(List<ConventionWeather.HeWeather6Bean.HourlyBean> Hourlys) {
        try {
            List<WeatherBean> list = new ArrayList<>();
            Random ra1 = new Random();
            for (int i = 0; i < Hourlys.size(); i++) {
                WeatherBean weatherBean = new WeatherBean();
                weatherBean.temperature = Integer.parseInt(Hourlys.get(i).getTmp());
                weatherBean.temperatureStr = Hourlys.get(i).getTmp() + "°";
                if (!TextUtils.isEmpty(Hourlys.get(i).getTime()) && Hourlys.get(i).getTime().length() >= 4) {
                    weatherBean.time = Hourlys.get(i).getTime().substring(Hourlys.get(i).getTime().length() - 5, Hourlys.get(i).getTime().length());
                }
                WeatherUtilBean weatherDay = new WeatherUtilBean();
                weatherDay.iconRes = Integer.parseInt(Hourlys.get(i).getCond_code());
                weatherDay.weather = Hourlys.get(i).getCond_txt() == null ? "" : Hourlys.get(i).getCond_txt();
                weatherDay.weatherId = Integer.parseInt(Hourlys.get(i).getCond_code());
                weatherBean.weather = weatherDay;
                weatherBean.weatherType = 3;
                list.add(weatherBean);
            }
            weather.setData(list);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private int Picture = R.mipmap.weather_bg;

    public int getPicture() {
        return Picture;
    }

    /**
     * 动态图
     */
    public void SwitchDynamicWeather(String which, RelativeLayout relWetherBg) {
//        final String[] items = new String[]{"晴", "晴（夜晚）", "多云", "阴", "雨", "雨夹雪",
//                "雪", "冰雹", "雾", "雾霾", "沙尘暴"};
//        String whichs = "晴";
//        for (int i = 0; i < items.length; i++) {
//            if (which.contains(items[i])) {
//                whichs = items[i];
//            }
//        }
//        ShortWeatherInfo info = new ShortWeatherInfo();
//        info.setCode("100");
//        info.setWindSpeed("11");
//        BaseWeatherType type;
//
//        switch (whichs) {
//            case "晴":
//                Picture = R.mipmap.day;
//                relWetherBg.setBackgroundResource(R.mipmap.day);
//                break;
//            case "晴（夜晚）":
//                Picture = R.mipmap.day_night;
//                relWetherBg.setBackgroundResource(R.mipmap.day_night);
//                break;
//            case "多云":
//                Picture = R.mipmap.night_cloud;
//                relWetherBg.setBackgroundResource(R.mipmap.night_cloud);
//                break;
//            case "阴":
//                Picture = R.mipmap.cloudy;
//                relWetherBg.setBackgroundResource(R.mipmap.cloudy);
//                break;
//            case "雨":
//                Picture = R.mipmap.rain;
//                relWetherBg.setBackgroundResource(R.mipmap.rain);
//                break;
//            case "雨夹雪":
//                Picture = R.mipmap.rain;
//                relWetherBg.setBackgroundResource(R.mipmap.rain);
//                break;
//            case "雪":
//                Picture = R.mipmap.snow;
//                relWetherBg.setBackgroundResource(R.mipmap.snow);
//                break;
//            case "冰雹":
//                Picture = R.mipmap.snow;
//                relWetherBg.setBackgroundResource(R.mipmap.snow);
//                break;
//            case "雾":
//                Picture = R.mipmap.fog;
//                relWetherBg.setBackgroundResource(R.mipmap.fog);
//                break;
//            case "雾霾":
//                Picture = R.mipmap.haze;
//                relWetherBg.setBackgroundResource(R.mipmap.haze);
//                break;
//            case "沙尘暴":
//                Picture = R.mipmap.haze;
//                relWetherBg.setBackgroundResource(R.mipmap.haze);
//                break;
//            case "雷":
//                Picture = R.mipmap.hunder;
//                relWetherBg.setBackgroundResource(R.mipmap.hunder);
//                break;
//            default:
//                Picture = R.mipmap.day;
//                relWetherBg.setBackgroundResource(R.mipmap.day);
//        }
    }

    public void AirForecast(String air_forecast, int i, TextView tvAirQuality) {
        switch (air_forecast) {
            case "优":
                tvAirQuality.setText("优质");
                tvAirQuality.setBackground(context.getResources().getDrawable(R.drawable.search30_1));
                break;
            case "良":
                tvAirQuality.setText("良好");
                tvAirQuality.setBackground(context.getResources().getDrawable(R.drawable.search30_2));
                break;
            case "轻度污染":
                tvAirQuality.setText("轻度");
                tvAirQuality.setBackground(context.getResources().getDrawable(R.drawable.search30_3));
                break;
            case "中度污染":
                tvAirQuality.setText("中度");
                tvAirQuality.setBackground(context.getResources().getDrawable(R.drawable.search30_4));
                break;
            case "重度污染":
                tvAirQuality.setText("重度");
                tvAirQuality.setBackground(context.getResources().getDrawable(R.drawable.search30_5));
                break;
            case "严重污染":
                tvAirQuality.setText("严重");
                tvAirQuality.setBackground(context.getResources().getDrawable(R.drawable.search30_6));
                break;
            default:
                tvAirQuality.setText("良好");
                tvAirQuality.setBackground(context.getResources().getDrawable(R.drawable.search30));
        }

    }

    private boolean banner1 = true;
    private boolean banner2 = true;

    public void AdShow(boolean b1, boolean b2) {
        if (nativelogic != null && banner1 && b1) {
            banner1 = false;
            nativelogic.AdShow(null);
        }
        if (nativelogic1 != null && banner2 && b2) {
            banner2 = false;
            nativelogic1.AdShow(null);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void BannerAd(Activity context, ImageView banner, RelativeLayout relatAd, LinearLayout adLin, FrameLayout bannerContainer, RelativeLayout GDTAd, RelativeLayout GDTAd1, ImageView adImageBanner, RelativeLayout adIcon, ImageView adIconImage) {//
        try {
            String ISAD = SaveShare.getValue(context, "ISAD");
            if (!TextUtils.isEmpty(ISAD) && ISAD.equals("1")) {
                // 获得原生banner广告对象
                nativelogic = Ad.getAd().NativeAD(context, "98f8e423-02e0-49f5-989f-af46f5c59203", "e480a102-5dc7-4984-85b0-3c25cd0d9b29", "67C53558D3E3485EA681EA21735A5003", new AdProtogenesisListener() {
                    @Override
                    public void onADReady(Native url, NativeAd nativelogic) {
                        if (TextUtils.isEmpty(nativelogic.nativeObject.sdk_code)) {
                            Glide.with(context).load(url.src).into(banner);
                            relatAd.setVisibility(View.VISIBLE);
                        } else {
                            refreshAd(GDTAd1);
                        }
                    }

                    @Override
                    public void onAdFailedToLoad(String error) {
                        relatAd.setVisibility(View.GONE);
                    }
                });
                banner.setOnClickListener(view -> {
                    //点击上报
                    nativelogic.OnClick(relatAd);
                });

                // 获得原生banner广告对象
                nativelogic1 = Ad.getAd().NativeAD(context, "98f8e423-02e0-49f5-989f-af46f5c59203", "e33fc0af-e47c-4970-b600-259b326cb63f", "67C53558D3E3485EA681EA21735A5003", new AdProtogenesisListener() {
                    @Override
                    public void onADReady(Native url, NativeAd nativelogic) {
                        try {
                            if (TextUtils.isEmpty(nativelogic.nativeObject.sdk_code)) {
                                Glide.with(context).load(url.src).into(adImageBanner);
                                adLin.setVisibility(View.VISIBLE);
                                GDTAd.setVisibility(View.GONE);
                            } else if (nativelogic.nativeObject.sdk_code.equals("GDT_SDK")) {
                                GDTBanner(GDTAd);
                                adLin.setVisibility(View.GONE);
                                bannerContainer.setVisibility(View.GONE);
                            } else if (nativelogic.nativeObject.sdk_code.equals("TOUTIAO_SDK")) {
                                GDTAd.setVisibility(View.GONE);
                                adLin.setVisibility(View.GONE);
                                PangolinBannerAd.getPangolinBannerAd().BannerAD(context, bannerContainer, nativelogic);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onAdFailedToLoad(String error) {
                        adLin.setVisibility(View.GONE);
                    }
                });
                adImageBanner.setOnClickListener(view -> {
                    //点击上报
                    nativelogic1.OnClick(null);
                });
                // 获得原生banner广告对象
                nativelogic2 = Ad.getAd().NativeAD(context, "98f8e423-02e0-49f5-989f-af46f5c59203", "a5be5758-8738-4936-9eb3-de3f9a6853ba", "67C53558D3E3485EA681EA21735A5003", new AdProtogenesisListener() {
                    @Override
                    public void onADReady(Native url, NativeAd nativelogic) {
                        if (TextUtils.isEmpty(url.src)) {
                            Glide.with(context).load(url.infoIcon.get(0)).into(adIconImage);
                        } else {
                            Glide.with(context).load(url.src).into(adIconImage);
                        }
                        adIcon.setVisibility(View.VISIBLE);
                        nativelogic.AdShow(null);
                    }

                    @Override
                    public void onAdFailedToLoad(String error) {
                        adIcon.setVisibility(View.GONE);
                    }
                });
                adIcon.setOnClickListener(v -> {
                    //点击上报
                    nativelogic2.OnClick(null);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private UnifiedBannerView GDTBanner(RelativeLayout relatAd) {
        // 创建 Banner 广告 AdView 对象
        // appId : 在 http://e.qq.com/dev/ 能看到的 app 唯一字符串"1109529834", "9080865985085582",
        // posId : 在 http://e.qq.com/dev/ 生成的数字串，并非 appid 或者 appkey
//        Log.i("AD_DEMO", "GDTBanner: "+nativelogic1.nativeObject.appid + nativelogic1.nativeObject.posid);


        UnifiedBannerView banner = new UnifiedBannerView(context, nativelogic1.nativeObject.appid, nativelogic1.nativeObject.posid, new UnifiedBannerADListener() {
            @Override
            public void onNoAD(AdError adError) {
                Log.i("AD_DEMO", "BannerNoAD，eCode=" + adError.getErrorCode() + adError.getErrorMsg());
                try {
                    relatAd.setVisibility(View.GONE);
                    if (nativelogic1 != null) {
                        nativelogic1.OnRequest(adError.getErrorCode() + "", adError.getErrorMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onADReceive() {
                try {
                    relatAd.setVisibility(View.VISIBLE);
                    if (nativelogic1 != null) {
                        nativelogic1.OnRequest("0", "msg");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("AD_DEMO", "onADReceive");
            }

            @Override
            public void onADExposure() {
                Log.i("AD_DEMO", "onADExposure");
                try {
                    if (nativelogic1 != null) {
                        nativelogic1.AdShow(relatAd);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onADClosed() {
                Log.i("AD_DEMO", "onADClosed");
            }

            @Override
            public void onADClicked() {
                Log.i("AD_DEMO", "onADClicked");
                try {
                    if (nativelogic1 != null) {
                        nativelogic1.OnClick(relatAd);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onADLeftApplication() {
                Log.i("AD_DEMO", "onADLeftApplication");
            }

            @Override
            public void onADOpenOverlay() {

            }

            @Override
            public void onADCloseOverlay() {

            }
        });
        //设置广告轮播时间，为0或30~120之间的数字，单位为s,0标识不自动轮播
        banner.setRefresh(30);

        relatAd.addView(banner);
        /* 发起广告请求，收到广告数据后会展示数据     */
        banner.loadAD();
        return banner;
    }

    private NativeExpressADView nativeExpressADView;
    private RelativeLayout relatAd;

    // 仅展示部分代码，完整代码请参考 GDTUnionDemo 工程
    // 1.加载广告，先设置加载上下文环境和条件
    private void refreshAd(RelativeLayout relatAd) {
        this.relatAd = relatAd;
        try {//"1109529834", "8060772054096821"
            NativeExpressAD nativeExpressAD = new NativeExpressAD(context, new ADSize(ADSize.FULL_WIDTH, ADSize.AUTO_HEIGHT), nativelogic.nativeObject.appid, nativelogic.nativeObject.posid, WeatherPageData.this); // 传入Activity
            // 注意：如果您在联盟平台上新建原生模板广告位时，选择了支持视频，那么可以进行个性化设置（可选）
//        nativeExpressAD.setVideoOption(new VideoOption.Builder()
//                .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.WIFI) // WIFI 环境下可以自动播放视频
//                .setAutoPlayMuted(true) // 自动播放时为静音
//                .build()); //
            nativeExpressAD.loadAD(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
