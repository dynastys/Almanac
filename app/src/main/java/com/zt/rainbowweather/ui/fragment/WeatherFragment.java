package com.zt.rainbowweather.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.common.eventbus.Subscribe;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;
import com.xy.xylibrary.Interface.RlSimpleTarget;
import com.xy.xylibrary.base.BaseFragment;
import com.xy.xylibrary.ui.fragment.task.TaskType;
import com.xy.xylibrary.utils.GlideUtil;
import com.xy.xylibrary.utils.SaveShare;
import com.xy.xylibrary.view.ColumnHorizontalScrollView;
import com.xy.xylibrary.view.CustomScrollViewPager;
import com.zt.rainbowweather.BasicApplication;
import com.zt.rainbowweather.api.RequestSyntony;
import com.zt.rainbowweather.entity.AddressBean;
import com.zt.rainbowweather.entity.City;
import com.zt.rainbowweather.entity.HeaderBean;
import com.zt.rainbowweather.entity.WeatherUtilBean;
import com.zt.rainbowweather.entity.city.Refresh;
import com.zt.rainbowweather.entity.city.Share;
import com.zt.rainbowweather.entity.news.MessageEvent;
import com.zt.rainbowweather.entity.news.ViewPageEvent;
import com.zt.rainbowweather.entity.weather.AirThDay;
import com.zt.rainbowweather.entity.weather.ConventionWeather;
import com.zt.rainbowweather.entity.weather.ViewPageScrollTo;
import com.zt.rainbowweather.presenter.WeatherLogic;
import com.zt.rainbowweather.presenter.home.WeatherPageData;
import com.zt.rainbowweather.presenter.map.MapLocation;
import com.zt.rainbowweather.presenter.request.WeatherRequest;
import com.zt.rainbowweather.ui.activity.AdviseMoreDetailActivity;
import com.zt.rainbowweather.ui.activity.AtlasActivity;
import com.zt.rainbowweather.ui.activity.DXiangLiActivity;
import com.zt.rainbowweather.ui.activity.IndexDetailsActivity;
import com.zt.rainbowweather.ui.activity.MainActivity;
import com.zt.rainbowweather.ui.activity.WeatherDetailsActivity;
import com.zt.rainbowweather.utils.AdDialog;
import com.zt.rainbowweather.utils.ConstUtils;
import com.zt.rainbowweather.utils.Lunar;
import com.zt.rainbowweather.utils.SizeUtils;
import com.zt.rainbowweather.utils.Util;
import com.zt.rainbowweather.utils.WeatherUtils;
import com.zt.rainbowweather.view.AutoVerticalScrollTextView;
import com.zt.rainbowweather.view.DrawableCenterTextView;
import com.zt.rainbowweather.view.MiuiWeatherView;
import com.zt.rainbowweather.view.MyVideoView;
import com.zt.rainbowweather.view.SunView;
import com.zt.rainbowweather.view.TranslucentActionBar;
import com.zt.rainbowweather.view.TranslucentScrollView;
import com.zt.rainbowweather.view.tab.SegmentTabLayout;
import com.zt.weather.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;
import org.salient.artplayer.MediaPlayerManager;
import org.salient.artplayer.VideoView;
import org.salient.artplayer.ui.ControlPanel;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author zw
 * @time 2019-3-8
 * 天气页面
 */
public class WeatherFragment extends BaseFragment implements TranslucentScrollView.TranslucentChangedListener, RequestSyntony<ConventionWeather>, TranslucentScrollView.OnHoldTabScrollViewScrollChanged,OnRefreshListener, OnMultiPurposeListener, RlSimpleTarget, WeatherPageData.AirQualityListener {

    Unbinder unbinder;
    @BindView(R.id.wether_bg)
    TextView wetherBg;
    @BindView(R.id.shop_list_bar)
    TextView shopListBar;
    @BindView(R.id.iv_search_flag)
    ImageView ivSearchFlag;
    @BindView(R.id.keyword)
    AutoVerticalScrollTextView keyword;
    @BindView(R.id.view_search)
    RelativeLayout viewSearch;
    @BindView(R.id.tv_lin)
    LinearLayout tvLin;
    @BindView(R.id.wallpaper_cut)
    RelativeLayout wallpaperCut;
    @BindView(R.id.tv_h_curr_temp)
    TextView tvHCurrTemp;
    @BindView(R.id.tv_h_curr_weather)
    TextView tvHCurrWeather;
    @BindView(R.id.forecast_date)
    TextView forecastDate;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_h_aqi)
    DrawableCenterTextView tvHAqi;
    @BindView(R.id.tv_h_rain)
    DrawableCenterTextView tvHRain;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.today)
    TextView today;
    @BindView(R.id.today_excellent_bg)
    TextView todayExcellentBg;
    @BindView(R.id.today_temperature)
    TextView todayTemperature;
    @BindView(R.id.today_excellent)
    TextView todayExcellent;
    @BindView(R.id.today_image)
    ImageView todayImage;
    @BindView(R.id.today_weather)
    TextView todayWeather;
    @BindView(R.id.tomorrow)
    TextView tomorrow;
    @BindView(R.id.tomorrow_excellent_bg)
    TextView tomorrowExcellentBg;
    @BindView(R.id.tomorrow_temperature)
    TextView tomorrowTemperature;
    @BindView(R.id.tomorrow_excellent)
    TextView tomorrowExcellent;
    @BindView(R.id.tomorrow_image)
    ImageView tomorrowImage;
    @BindView(R.id.tomorrow_weather)
    TextView tomorrowWeather;
    @BindView(R.id.at_present)
    TextView atPresent;
    @BindView(R.id.weather)
    MiuiWeatherView weather;
    @BindView(R.id.sv)
    SunView sv;
    //    @BindView(R.id.sfdwv)
//    ScrollFutureDaysWeatherView sfdwv;
    @BindView(R.id.rv_advise_title)
    RecyclerView rvAdviseTitle;
    @BindView(R.id.lin_wether)
    LinearLayout linWether;
    @BindView(R.id.mRadioGroup_content)
    LinearLayout mRadioGroupContent;
    @BindView(R.id.column)
    ColumnHorizontalScrollView column;
    @BindView(R.id.rl_center)
    RelativeLayout rlCenter;
    @BindView(R.id.viewpager_column)
    CustomScrollViewPager viewpagerColumn;
    @BindView(R.id.home_scrollview)
    TranslucentScrollView scrollView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.details_actionbar)
    TranslucentActionBar DetailsActionbar;
    @BindView(R.id.list_bar)
    TextView listBar;
    @BindView(R.id.rl_top)
    RelativeLayout topRl;
    @BindView(R.id.rel_wether_bg)
    RelativeLayout relWetherBg;
    @BindView(R.id.wind)
    TextView wind;
    @BindView(R.id.wind_rank)
    TextView windRank;
    @BindView(R.id.humidity)
    TextView humidity;
    @BindView(R.id.somatosensory)
    TextView somatosensory;
    @BindView(R.id.seeing)
    TextView seeing;
    @BindView(R.id.maximum_temperature)
    TextView maximumTemperature;
    @BindView(R.id.minimum_temperature)
    TextView minimumTemperature;
    @BindView(R.id.wether_bg2)
    TextView wetherBg2;
    @BindView(R.id.wether_bg_image)
    ImageView wetherBgImage;
    @BindView(R.id.today_rel)
    RelativeLayout todayRel;
    @BindView(R.id.tomorrow_rel)
    RelativeLayout tomorrowRel;
    @BindView(R.id.lin_wether_pm)
    LinearLayout linWetherPm;
    @BindView(R.id.ad_image_banner)
    ImageView adImageBanner;
    @BindView(R.id.ad_image_banner_clear)
    ImageView adImageBannerClear;
    @BindView(R.id.banner)
    ImageView banner;
    @BindView(R.id.tv_ad_flag)
    TextView tvAdFlag;
    @BindView(R.id.relat_ad)
    RelativeLayout relatAd;
    @BindView(R.id.ad_lin)
    LinearLayout adLin;
    @BindView(R.id.ad_icon_image)
    ImageView adIconImage;
    @BindView(R.id.ad_icon)
    RelativeLayout adIcon;
    @BindView(R.id.sunrise_and_sunset_lin)
    LinearLayout sunriseAndSunsetLin;
    @BindView(R.id.GDT_ad)
    RelativeLayout GDTAd;
    @BindView(R.id.GDT_ad1)
    RelativeLayout GDTAd1;
    @BindView(R.id.banner_container)
    FrameLayout bannerContainer;
    @BindView(R.id.tab_vp_7)
    SegmentTabLayout tabVp7;
    @BindView(R.id.vp_7)
    ViewPager vp7;
    @BindView(R.id.wether_even_more)
    TextView wetherEvenMore;
    @BindView(R.id.x5_video_webview)
    VideoView x5VideoWebview;
    //    @BindView(R.id.iv_first_frame)
//    ImageView ivFirstFrame;
    @BindView(R.id.rain_alarm_pro)
    TextView rainAlarmPro;
    @BindView(R.id.weather_forecast)
    TextView weatherForecast;
    @BindView(R.id.rain_alarm_pro_lin)
    LinearLayout rainAlarmProLin;
    @BindView(R.id.next_two_hours)
    TextView nextTwoHours;
    @BindView(R.id.next_two_hours_lin)
    LinearLayout nextTwoHoursLin;
    @BindView(R.id.tv_wether_bg)
    ImageView tvWetherBg;

    private TaskType taskType, taskType4;
    private int transAlphaY;
    private City currCity;
    private HeaderBean mHeaderBean = new HeaderBean();
    private int mHeight = 0;
    private WeatherPageData weatherPageData;
    private AddressBean mAddressBean;
    @SuppressLint("StaticFieldLeak")
    private static HomeFragment homeFragments;
    private WeatherLogic weatherLogic;
    private boolean LookVideo = false;
    private int hour;
    private int minute;
    private String INUSE = "";
    private String size;
    private String imageUrl;
    private Drawable resource;
    public static boolean ISNEWS = true;
    public static boolean ISNEWSONR = true;
    private boolean isVisibleToUser = false;

    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void setCityEvent(TaskType taskType) {
        try {
            if (taskType.tasktype == 3) {
                this.taskType = taskType;
                if (null != scrollView) {
                    scrollView.smoothScrollTo(0, 0);
                }
            } else if (taskType.tasktype == 5) {
                if (null != scrollView) {
                    scrollView.smoothScrollTo(0, rlHeader.getHeight());
                    Intent intent1 = new Intent(getActivity(), WeatherDetailsActivity.class);
                    intent1.putExtra("datas", (Serializable) weatherPageData.getOutLookWeathers());
                    intent1.putExtra("Size", "0");
                    intent1.putExtra("City", currCity.name);
                    startActivity(intent1);
                    TaskType taskType3 = LitePal.where("tasktype = ?", "5").findFirst(TaskType.class);
                    SaveShare.saveValue(getActivity(), "JB", "");
                    taskType3.ISStartTask = true;
                    taskType3.save();
                    EventBus.getDefault().post("");
                    //                if (taskType != null && !taskType.ISStartTask) {
                    //                    TaskLogic.getTaskLogic().FinishTask(getActivity(), "", taskType.taskId, taskType.IsDouble);
                    //                    taskType = null;
                    //                }
                    MobclickAgent.onEvent(getActivity(), "home_Weather_Details", "home_Weather_Details");
                }
            } else if (taskType.tasktype == 1) {
                if (linWether != null) {
                    taskType4 = taskType;
                    scrollView.smoothScrollTo(0, linWether.getHeight() - column.getHeight() - SizeUtils.dp2px(getActivity(), 10));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_wether;
    }

    public static WeatherFragment newInstance(City city, String size, HomeFragment homeFragment) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle bundle = new Bundle();
        bundle.putString("size", size);
        bundle.putSerializable("city", city);
        fragment.setArguments(bundle);
        homeFragments = homeFragment;
        return fragment;
    }

    public AddressBean getAddressBean() {
        if (mAddressBean == null)
            mAddressBean = new AddressBean();
        mAddressBean.city = currCity;
        mAddressBean.header = mHeaderBean;
        return mAddressBean;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initData(View view) {
        try {
            x5VideoWebview.setFocusable(false);
//            x5VideoWebview.setUp("http://vfx.mtime.cn/Video/2018/06/27/mp4/180627094726195356.mp4");
//            final ControlPanel controlPanel = new ControlPanel(getActivity());
//            x5VideoWebview.setControlPanel(controlPanel);
//            TextView tvTitle = controlPanel.findViewById(R.id.tvTitle);
//            tvTitle.setText("天气预报");
//            GlideUtil.getGlideUtil().setImages(getActivity(),"http://img5.mtime.cn/mg/2018/07/06/093947.51483272.jpg",(ImageView) controlPanel.findViewById(R.id.video_cover));

            if (null != scrollView) {
                scrollView.smoothScrollTo(0, 0);
            }
            rvAdviseTitle.setHasFixedSize(true);
            rvAdviseTitle.setNestedScrollingEnabled(false);
            INUSE = SaveShare.getValue(getActivity(), "INUSE");
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    todayRel.setBackgroundResource(R.drawable.ripple_bg);
                    tomorrowRel.setBackgroundResource(R.drawable.ripple_bg2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            assert getArguments() != null;
            currCity = (City) getArguments().getSerializable("city");
            size = getArguments().getString("size");
            mHeaderBean.address = currCity.name;
            topRl.setVisibility(View.GONE);
            scrollView.setOnObservableScrollViewScrollChanged(this);

            weatherPageData = new WeatherPageData((MainActivity) getActivity(), WeatherFragment.this, rvAdviseTitle, weather, relWetherBg, tabVp7, vp7);
            weatherPageData.GradientStatusBar(scrollView, DetailsActionbar, WeatherFragment.this);
            weatherLogic = WeatherLogic.getWeatherLogic();
//            weatherPageData.VideoWarning(ivFirstFrame, weatherForecast, x5VideoWebview);
            if (ISNEWS) {
                ISNEWS = false;
                if (SaveShare.getDrawable(Objects.requireNonNull(getActivity()), currCity.name) != null) {
                    tvWetherBg.setImageDrawable(SaveShare.getDrawable(Objects.requireNonNull(getActivity()), currCity.name));
                } else {
                    GlideUtil.getGlideUtil().getDrawableImages(getActivity(), INUSE, WeatherFragment.this);
                }
                if (weatherPageData.WeatherDataCacheGain(currCity, WeatherFragment.this)) {
                    weatherPageData.RequestWeatherData(currCity, rainAlarmPro, rainAlarmProLin, WeatherFragment.this, WeatherFragment.this, 0);
                }
                weatherPageData.BannerAd(getActivity(), banner, relatAd, adLin, bannerContainer, GDTAd, GDTAd1, adImageBanner, adIcon, adIconImage);
                weatherPageData.VideoWarning(weatherForecast, x5VideoWebview);
                weatherPageData.RequestNewsData(WeatherFragment.this, mRadioGroupContent, column, viewpagerColumn);
                weatherPageData.NnoticeData(keyword);
            } else {
                this.isVisibleToUser = true;
                weatherPageData.WeatherDataCacheGain(currCity, WeatherFragment.this);
            }

            if (size.equals("0")) {
                ISNews = false;
            }
            if (!TextUtils.isEmpty(BasicApplication.url)) {
                AdviseMoreDetailActivity.startActivity(getActivity(), "资讯", BasicApplication.url, "1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        assert rootView != null;
        unbinder = ButterKnife.bind(this, rootView);
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    private boolean ISBG = true;
    private boolean ISNews = true;

    @SuppressLint("ResourceAsColor")
    @Override
    public void onTranslucentChanged(int transAlpha) {
        try {
            transAlphaY = transAlpha;
            wetherBg.setVisibility(View.VISIBLE);
            wetherBg.getBackground().setAlpha(transAlpha);
            wetherBg2.setVisibility(View.VISIBLE);
            wetherBg2.getBackground().setAlpha(transAlpha);
            if (transAlpha > 180) {
                try {
                    if (ISBG) {
                        ISBG = false;
                        if (resource != null) {
                            tvWetherBg.setImageDrawable(new BitmapDrawable(Util.rsBlur(getActivity(), Util.drawable2Bitmap(resource), 25)));
                        } else {
                            resource = SaveShare.getDrawable(Objects.requireNonNull(getActivity()), currCity.name);
                            tvWetherBg.setImageDrawable(new BitmapDrawable(Util.rsBlur(getActivity(), Util.drawable2Bitmap(resource), 25)));
                        }
                        DetailsActionbar.setNeedTranslucent(true, true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                EventBus.getDefault().post(new Refresh(0));
                if (!ISBG) {
                    if (resource != null) {
                        tvWetherBg.setImageDrawable(resource);
                    } else {
                        resource = SaveShare.getDrawable(Objects.requireNonNull(getActivity()), currCity.name);
                        tvWetherBg.setImageDrawable(resource);
                    }
                    ISBG = true;
                    DetailsActionbar.setNeedTranslucent(true, false);
//                    }
                }
            }
            homeFragments.setCanSlipping(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void MessageShare(Share share) {
        if (scrollView != null) {
            scrollView.fling(0);
            scrollView.smoothScrollTo(0, 0);
        }
    }

    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void MessageView(ViewPageEvent viewPageEvent) {
        if (viewpagerColumn != null) {
            viewpagerColumn.resetHeight(viewPageEvent.getMessage());
        }
    }

    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void ViewPageScrollTo(ViewPageScrollTo viewPageScrollTo) {
        if (scrollView != null) {
            scrollView.scrollTo(0, mHeight);
        }
    }

    @Subscribe
    public void updateLocateWeather(City city) {
        try {
            currCity = city;
            if (city.name.equals("定位失败")) {//如果是定位的城市
                tvAddress.setText("请开启定位权限或网络连接");
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh(100);
                }
            } else {
                if (tvAddress != null) {
                    tvAddress.setText(city.affiliation);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && weatherPageData != null && rlHeader != null) {

            try {
                wetherBg.setVisibility(View.VISIBLE);
                wetherBg.getBackground().setAlpha(transAlphaY);
                wetherBg2.setVisibility(View.VISIBLE);
                wetherBg2.getBackground().setAlpha(transAlphaY);
                if (ISNews) {
                    if (SaveShare.getDrawable(Objects.requireNonNull(getActivity()), currCity.name) != null) {
                        tvWetherBg.setImageDrawable(SaveShare.getDrawable(Objects.requireNonNull(getActivity()), currCity.name));
                    }
                    this.isVisibleToUser = false;
                    ISNews = false;
                    weatherPageData.RequestWeatherData(currCity, rainAlarmPro, rainAlarmProLin, WeatherFragment.this, WeatherFragment.this, 1);
                    weatherPageData.RequestNewsData(WeatherFragment.this, mRadioGroupContent, column, viewpagerColumn);
                    weatherPageData.VideoWarning(weatherForecast, x5VideoWebview);
                     weatherPageData.NnoticeData(keyword);
                    weatherLogic.initData(getActivity(), rlHeader, relWetherBg, shopListBar, listBar, weather, homeFragments, refreshLayout, WeatherFragment.this, WeatherFragment.this);
                    weatherPageData.BannerAd(getActivity(), banner, relatAd, adLin, bannerContainer, GDTAd, GDTAd1, adImageBanner, adIcon, adIconImage);
                }
                weatherPageData.AlterNotification();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (x5VideoWebview != null) {
            x5VideoWebview.setFocusable(false);
        }
        try {
            if (weatherPageData != null && weatherPageData.checkIsVisible(linWether) && ISNEWSONR) {
                Log.e("timeaaaaa", "onNext: " + System.currentTimeMillis());
                ISNEWSONR = false;
                weatherPageData.RequestWeatherData(currCity, rainAlarmPro, rainAlarmProLin, WeatherFragment.this, WeatherFragment.this, 1);
            }
            MobclickAgent.onPageStart("WeatherFragment"); //统计页面("MainScreen"为页面名称，可自定义)
            INUSE = SaveShare.getValue(getActivity(), "INUSE");
            if (!TextUtils.isEmpty(INUSE) && ConstUtils.ISBG) {
                ConstUtils.ISBG = false;
                GlideUtil.getGlideUtil().getDrawableImages(getActivity(), INUSE, WeatherFragment.this);
            }
            if (weatherPageData != null) {
                weatherPageData.onResume();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (x5VideoWebview != null) {
            x5VideoWebview.pause();
            MediaPlayerManager.instance().pause();
        }
        resource = null;
        MobclickAgent.onPageEnd("WeatherFragment");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            EventBus.getDefault().unregister(this);
            MediaPlayerManager.instance().releasePlayerAndView(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onNext(ConventionWeather conventionWeather) {
        try {
            if (conventionWeather.getHeWeather6().get(0) == null) {
                return;
            }
            if (refreshLayout != null) {
                refreshLayout.finishRefresh(100);
            }
            wind.setText(conventionWeather.getHeWeather6().get(0).getNow().getWind_dir());
            windRank.setText(conventionWeather.getHeWeather6().get(0).getNow().getWind_spd() + "级");
            humidity.setText(conventionWeather.getHeWeather6().get(0).getNow().getHum() + "%");
            somatosensory.setText(conventionWeather.getHeWeather6().get(0).getNow().getFl() + "℃");
            seeing.setText(conventionWeather.getHeWeather6().get(0).getNow().getVis() + "Km");
            todayTemperature.setText(conventionWeather.getHeWeather6().get(0).getDaily_forecast().get(0).getTmp_max() + "/" + conventionWeather.getHeWeather6().get(0).getDaily_forecast().get(0).getTmp_min() + "℃");
//            todayExcellent.setText("优");
            todayImage.setImageResource(WeatherUtils.getWeatherStatus(Integer.parseInt(conventionWeather.getHeWeather6().get(0).getDaily_forecast().get(0).getCond_code_d())).iconRes);
            todayWeather.setText(conventionWeather.getHeWeather6().get(0).getDaily_forecast().get(0).getCond_txt_d());
            tomorrow.setText(WeatherUtils.getWeek(conventionWeather.getHeWeather6().get(0).getDaily_forecast().get(1).getDate()));
            tomorrowTemperature.setText(conventionWeather.getHeWeather6().get(0).getDaily_forecast().get(1).getTmp_max() + "/" + conventionWeather.getHeWeather6().get(0).getDaily_forecast().get(1).getTmp_min() + "℃");
//            tomorrowExcellent.setText("优");
            tomorrowImage.setImageResource(WeatherUtils.getWeatherStatus(Integer.parseInt(conventionWeather.getHeWeather6().get(0).getDaily_forecast().get(1).getCond_code_d())).iconRes);
            tomorrowWeather.setText(conventionWeather.getHeWeather6().get(0).getDaily_forecast().get(1).getCond_txt_d());
            atPresent.setText("当前：" + conventionWeather.getHeWeather6().get(0).getNow().getTmp() + "°");
            WeatherUtilBean weatherDayN = new WeatherUtilBean();
            weatherDayN.iconRes = Integer.parseInt(conventionWeather.getHeWeather6().get(0).getNow().getCond_code());
            weatherDayN.weather = conventionWeather.getHeWeather6().get(0).getNow().getCond_txt() == null ? "" : conventionWeather.getHeWeather6().get(0).getNow().getCond_txt();
            weatherDayN.weatherId = Integer.parseInt(conventionWeather.getHeWeather6().get(0).getNow().getCond_code());
            mHeaderBean.weather = weatherDayN;
            mHeaderBean.currTemp = Integer.parseInt(conventionWeather.getHeWeather6().get(0).getNow().getTmp());
            if (mHeaderBean.currTemp != 0) {
                SaveShare.saveValue(getActivity(), currCity.name, mHeaderBean.currTemp + "," + mHeaderBean.weather.iconRes);
            }
            Calendar calendar = Calendar.getInstance();
            Lunar lunar = new Lunar(calendar);
            //农历的月日
            forecastDate.setText("农历 " + lunar.toString());
            tvHCurrTemp.setText(conventionWeather.getHeWeather6().get(0).getNow().getTmp());
            if (BasicApplication.getLocatedCity() != null && !TextUtils.isEmpty(BasicApplication.getLocatedCity().name) && BasicApplication.getLocatedCity().name.contains(conventionWeather.getHeWeather6().get(0).getBasic().getLocation())) {
                tvAddress.setText(BasicApplication.getLocatedCity().affiliation);
            } else {
                tvAddress.setText(conventionWeather.getHeWeather6().get(0).getBasic().getLocation());
            }
            tvHCurrWeather.setText(conventionWeather.getHeWeather6().get(0).getNow().getCond_txt() == null ? "" : conventionWeather.getHeWeather6().get(0).getNow().getCond_txt());
            tvHAqi.setText("湿度 " + conventionWeather.getHeWeather6().get(0).getNow().getHum());
            tvHRain.setText(conventionWeather.getHeWeather6().get(0).getNow().getWind_dir() + " " + conventionWeather.getHeWeather6().get(0).getNow().getWind_sc());
            maximumTemperature.setText(conventionWeather.getHeWeather6().get(0).getDaily_forecast().get(0).getTmp_max() + "℃");
            minimumTemperature.setText(conventionWeather.getHeWeather6().get(0).getDaily_forecast().get(0).getTmp_min() + "℃");
            if (TextUtils.isEmpty(INUSE)) {
//                drawable[0] = getResources().getDrawable(weatherPageData.getPicture());
            }
            String[] sunrise = conventionWeather.getHeWeather6().get(0).getDaily_forecast().get(0).getSr().split(":");
            String[] sunset = conventionWeather.getHeWeather6().get(0).getDaily_forecast().get(0).getSs().split(":");
            // 设置日出时间
            sv.setSunrise(Util.TurnDigital(sunrise[0]), Util.TurnDigital(sunrise[1]));
            // 设置日落时间
            sv.setSunset(Util.TurnDigital(sunset[0]), Util.TurnDigital(sunset[1]));
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
//            weatherLogic.setAd(TMAw1, WeatherFragment.this);
            if (!isVisibleToUser) {
                isVisibleToUser = true;
                weatherPageData.Icons(wetherBgImage, SaveShare.getValue(getActivity(), "backdrop_theme_id").equals("") ? 1 : Integer.parseInt(SaveShare.getValue(getActivity(), "backdrop_theme_id")), 0, conventionWeather.getHeWeather6().get(0).getBasic(), WeatherFragment.this);
            }

            weatherPageData.AlterNotification();
            weatherLogic.initData(getActivity(), rlHeader, relWetherBg, shopListBar, listBar, weather, homeFragments, refreshLayout, WeatherFragment.this, WeatherFragment.this);
            for (int i = 0; i < 2; i++) {
                if (conventionWeather.getHeWeather6().get(0).getHourly().get(i).getCond_txt().contains("雨")) {
                    nextTwoHoursLin.setVisibility(View.GONE);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onObservableScrollViewScrollChanged(int l, int t, int oldl, int oldt) {
        try {
            if (linWether != null) {
                mHeight = linWether.getHeight() - column.getHeight() - SizeUtils.dp2px(getActivity(), 10);
            }
            if (t >= mHeight) {
                if (column.getParent() != topRl) {
                    rlCenter.removeView(column);
                    topRl.addView(column);
//                    listBar.setVisibility(View.VISIBLE);
                    topRl.setVisibility(View.VISIBLE);
//                    DetailsActionbar.setVisibility(View.GONE);.
//                    EventBus.getDefault().post(new Refresh(200));
                }
            } else {
                if (column.getParent() != rlCenter) {
                    topRl.removeView(column);
                    rlCenter.addView(column);
//                    EventBus.getDefault().post(new Refresh(0));
                    listBar.setVisibility(View.GONE);
                    topRl.setVisibility(View.GONE);
//                    DetailsActionbar.setVisibility(View.VISIBLE);
                }
            }
            if (isVisible(null) && b) {
                b = false;
                // 设置当前时间
                sv.setCurrentTime(hour, minute);
            }
            weatherPageData.AdShow(relatAd.getLocalVisibleRect(new Rect()), adLin.getLocalVisibleRect(new Rect()));
            int scrollY = scrollView.getScrollY();
            View onlyChild = scrollView.getChildAt(0);
            if (onlyChild.getHeight() <= scrollY + scrollView.getHeight() && weatherPageData.checkIsVisible(viewpagerColumn)) {   // 如果满足就是到底部了
                EventBus.getDefault().post(new MessageEvent(currCity.name, currCity.name));
            }

            if (null != x5VideoWebview) {
                weatherPageData.getMediaController();
                if (weatherPageData.checkIsVisible(x5VideoWebview)) {
                    if (taskType != null && !taskType.ISStartTask) {
                        TaskType taskType3 = LitePal.where("tasktype = ?", "3").findFirst(TaskType.class);
                        SaveShare.saveValue(getActivity(), "JB", "");
                        taskType3.ISStartTask = true;
                        taskType3.save();
                        EventBus.getDefault().post("");
//                    TaskLogic.getTaskLogic().FinishTask(getActivity(),"",taskType.taskId,taskType.IsDouble);
                        taskType = null;
                    }
                    x5VideoWebview.start();
                    if (!LookVideo) {
                        LookVideo = true;
//                        ((ImageView) x5VideoWebview.getControlPanel().findViewById(R.id.video_cover)).setImageResource(0);
//                        x5VideoWebview.startFullscreen(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                        MediaPlayerManager.instance().setMute(true);
                        WeatherRequest.getWeatherRequest().getLookAtData(getActivity(), "天气预报", "播放视频");
                    }
                } else {
                    x5VideoWebview.pause();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean b = true;

    private boolean isVisible(View v) {
        return sv.getLocalVisibleRect(new Rect());
    }

    @Override
    public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
        if (isDragging || offset > 0) {
            EventBus.getDefault().post(new Refresh(200));
        } else {
            EventBus.getDefault().post(new Refresh(0));
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        try {
            MobclickAgent.onEvent(getActivity(), "home_refresh");
            //请求数据
            if (currCity != null) {
                if (currCity.affiliation.equals(ConstUtils.LOCATE_FAILED)) {//定位失败
                    MapLocation.getMapLocation().startLocation();
                } else {
                    isVisibleToUser = false;
                    weatherPageData.RequestWeatherData(currCity, rainAlarmPro, rainAlarmProLin, WeatherFragment.this, WeatherFragment.this, 1);
//                    if (ISNEWS) {
//                        ISNEWS = false;
//                        weatherPageData.RequestNewsData(WeatherFragment.this, mRadioGroupContent, column, viewpagerColumn);
//                    }
                }
            } else {
                refreshLayout.finishRefresh(100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void AirQuality(List<AirThDay.HeWeather6Bean.AirForecastBean> air_forecast) {
        try {
            todayExcellent.setText(air_forecast.get(0).getQlty());
            weatherPageData.AirForecast(air_forecast.get(0).getQlty(), 0, todayExcellentBg);
            tomorrowExcellent.setText(air_forecast.get(1).getQlty());
            weatherPageData.AirForecast(air_forecast.get(1).getQlty(), 1, tomorrowExcellentBg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Refresh() {
        ISNews = true;
    }

    @OnClick(R.id.wallpaper_cut)
    public void onClick() {
        Intent intent = new Intent(getActivity(), AtlasActivity.class);
        Objects.requireNonNull(getActivity()).overridePendingTransition(com.constellation.xylibrary.R.anim.in_from_right, com.constellation.xylibrary.R.anim.out_to_left);
        startActivity(intent);
    }

    @Override
    public void onResourceReady(@NonNull Drawable resource) {
        try {
            SaveShare.putDrawable(Objects.requireNonNull(getActivity()), currCity.name, resource);
            Drawable drawable = SaveShare.getDrawable(Objects.requireNonNull(getActivity()), currCity.name);
            if (drawable != null) {
                tvWetherBg.setImageDrawable(drawable);
            }
            this.resource = null;
////            imageUrl = SaveShare.getValue(getActivity(), "INUSE");
//            tvWetherBg.setImageDrawable(resource);
//            SaveShare.putDrawable(Objects.requireNonNull(getActivity()), "icon",resource);
//            if(imageUrl != null){
//                GlideUtil.getGlideUtil().setImages(getActivity(),imageUrl,tvWetherBg);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
    }

    @Override
    public void onHeaderReleased(RefreshHeader header, int headerHeight, int maxDragHeight) {
    }

    @Override
    public void onHeaderStartAnimator(RefreshHeader header, int headerHeight, int maxDragHeight) {
    }

    @Override
    public void onHeaderFinish(RefreshHeader header, boolean success) {
    }

    @Override
    public void onFooterMoving(RefreshFooter footer, boolean isDragging, float percent, int offset, int footerHeight, int maxDragHeight) {
    }

    @Override
    public void onFooterReleased(RefreshFooter footer, int footerHeight, int maxDragHeight) {
    }

    @Override
    public void onFooterStartAnimator(RefreshFooter footer, int footerHeight, int maxDragHeight) {
    }

    @Override
    public void onFooterFinish(RefreshFooter footer, boolean success) {
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
    }


    @OnClick({R.id.today_rel, R.id.tomorrow_rel, R.id.ad_image_banner_clear, R.id.tv_h_curr_temp, R.id.forecast_date, R.id.sunrise_and_sunset_lin, R.id.wether_even_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sunrise_and_sunset_lin:
            case R.id.today_rel:
                Intent intent1 = new Intent(getActivity(), WeatherDetailsActivity.class);
                intent1.putExtra("datas", (Serializable) weatherPageData.getOutLookWeathers());
                intent1.putExtra("Size", "0");
                intent1.putExtra("City", currCity.name);
                startActivity(intent1);
                MobclickAgent.onEvent(getActivity(), "home_Weather_Details", "home_Weather_Details");
                break;
            case R.id.tomorrow_rel:
                Intent intent = new Intent(getActivity(), WeatherDetailsActivity.class);
                intent.putExtra("datas", (Serializable) weatherPageData.getOutLookWeathers());
                intent.putExtra("Size", "1");
                intent.putExtra("City", currCity.name);
                startActivity(intent);
                MobclickAgent.onEvent(getActivity(), "home_Weather_Details", "home_Weather_Details");
                break;
            case R.id.ad_image_banner_clear:
                adLin.setVisibility(View.GONE);
                break;
            case R.id.forecast_date:
            case R.id.tv_h_curr_temp:
//                AdDialog adDialog = new AdDialog(getActivity(),"", 1);
//                adDialog.setCanceledOnTouchOutside(false);
//                adDialog.show();
//                adDialog.setClicklistener(new AdDialog.ClickListenerInterface() {
//                    @Override
//                    public void doConfirm() {
//
//                    }
//
//                    @Override
//                    public void doCancel() {
//                        adDialog.dismiss();
//                    }
//                });
                DXiangLiActivity.startActivity(getActivity(), null);
                break;
            case R.id.wether_even_more:
                Intent IndexIntent = new Intent(getActivity(), IndexDetailsActivity.class);
                IndexIntent.putExtra("datas", (Serializable) weatherPageData.getLifestyleBean());
                IndexIntent.putExtra("Size", "0");
                IndexIntent.putExtra("City", currCity.name);
                getActivity().startActivity(IndexIntent);
                break;
//            case R.id.iv_first_frame:
//                try {
//                    ivFirstFrame.setVisibility(View.GONE);
//                    x5VideoWebview.setVisibility(View.VISIBLE);
//                    x5VideoWebview.start();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
        }
    }

}
