package com.zt.rainbowweather.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qq.e.ads.banner2.UnifiedBannerADListener;
import com.qq.e.ads.banner2.UnifiedBannerView;
import com.qq.e.comm.util.AdError;
import com.umeng.analytics.MobclickAgent;
import com.xy.xylibrary.Interface.OnInitViewListener;
import com.xy.xylibrary.base.BaseFragment;
import com.xy.xylibrary.base.BasicDialog;
import com.xy.xylibrary.config.DialogConfig;
import com.xy.xylibrary.utils.GlideUtil;
import com.xy.xylibrary.utils.SaveShare;
import com.zt.rainbowweather.api.RequestSyntony;
import com.zt.rainbowweather.entity.OutLookWeather;
import com.zt.rainbowweather.entity.calendar.DanXiangLi;
import com.zt.rainbowweather.entity.calendar.HuangLi;
import com.zt.rainbowweather.presenter.PangolinBannerAd;
import com.zt.rainbowweather.presenter.home.WeatherPageData;
import com.zt.rainbowweather.presenter.request.AlmanacRequest;
import com.zt.rainbowweather.ui.activity.DXiangLiActivity;
import com.zt.rainbowweather.ui.activity.YiJiActivity;
import com.zt.rainbowweather.utils.PopupWindowUtil;
import com.zt.rainbowweather.utils.Util;
import com.zt.rainbowweather.utils.WeatherDetailsDialog;
import com.zt.rainbowweather.utils.WeatherUtils;
import com.zt.rainbowweather.view.ChangeTextViewSpace;
import com.zt.rainbowweather.view.SunriseView;
import com.zt.weather.R;
import com.zt.xuanyin.Interface.AdProtogenesisListener;
import com.zt.xuanyin.controller.Ad;
import com.zt.xuanyin.controller.NativeAd;
import com.zt.xuanyin.entity.model.Native;

import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class WeatherDetailsFragment extends BaseFragment implements RequestSyntony<DanXiangLi>, NestedScrollView.OnScrollChangeListener, PopupWindowUtil.PopupWindowPort {

    @BindView(R.id.weather_details_icon)
    ImageView weatherDetailsIcon;
    @BindView(R.id.weather_details_state)
    TextView weatherDetailsState;
    @BindView(R.id.temperature)
    TextView temperature;
    @BindView(R.id.wind_power)
    TextView windPower;
    @BindView(R.id.weather_details_today_excellent_bg)
    TextView weatherDetailsTodayExcellentBg;
    @BindView(R.id.air_quality)
    TextView airQuality;
    @BindView(R.id.weather_details_date)
    TextView weatherDetailsDate;
    @BindView(R.id.weather_details_week)
    TextView weatherDetailsWeek;
    @BindView(R.id.weather_details_lunar_calendar)
    TextView weatherDetailsLunarCalendar;
    @BindView(R.id.weather_details_suitable)
    TextView weatherDetailsSuitable;
    @BindView(R.id.weather_details_avoid)
    TextView weatherDetailsAvoid;
    @BindView(R.id.chanxiangli_tv)
    ChangeTextViewSpace chanxiangliTv;
    @BindView(R.id.dynamic_state)
    ImageView dynamicState;
    Unbinder unbinder;
    @BindView(R.id.weather_details_icon_lin)
    LinearLayout weatherDetailsIconLin;
    @BindView(R.id.sun)
    SunriseView sun;
    @BindView(R.id.humidity_name)
    TextView humidityName;
    @BindView(R.id.humidity)
    TextView humidity;
    @BindView(R.id.wind_power_name)
    TextView windPowerName;
    @BindView(R.id.weather_details_today_excellent_bg_name)
    TextView weatherDetailsTodayExcellentBgName;
    @BindView(R.id.sendible_temperature_name)
    TextView sendibleTemperatureName;
    @BindView(R.id.sendible_temperature)
    TextView sendibleTemperature;
    @BindView(R.id.sunrise)
    TextView sunrise;
    @BindView(R.id.sunset)
    TextView sunset;
    @BindView(R.id.cultivate_calendar)
    TextView cultivateCalendar;
    @BindView(R.id.ji_yi_lin)
    LinearLayout jiYiLin;
    @BindView(R.id.ad_image_banner)
    ImageView adImageBanner;
    @BindView(R.id.ad_image_banner_clear)
    ImageView adImageBannerClear;
    @BindView(R.id.tv_ad_flag)
    TextView tvAdFlag;
    @BindView(R.id.ad_lin)
    LinearLayout adLin;
    @BindView(R.id.weather_details_scroll)
    NestedScrollView weatherDetailsScroll;
    @BindView(R.id.dynamic_state_lin)
    LinearLayout dynamicStateLin;
    @BindView(R.id.WD_GDT_ad)
    RelativeLayout WDGDTAd;
    @BindView(R.id.banner_container)
    FrameLayout bannerContainer;
    @BindView(R.id.danxiangli_tv_details)
    TextView danxiangliTvDetails;
    @BindView(R.id.humidity_popup)
    RelativeLayout humidityPopup;
    @BindView(R.id.wind_power_popup)
    RelativeLayout windPowerPopup;

    private OutLookWeather outLookWeather;
    private DanXiangLi danXiangLi;
    private DanXiangLi.DataBean bean;
    private HuangLi huangLis;
    public static boolean ISAD = true;
    private PopupWindow menuWindow;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_weather_details;
    }

    private void DialogShow(String title, int notes,int image,int type){
        WeatherDetailsDialog weatherDetailsDialog = new WeatherDetailsDialog(getActivity(),title, notes,image,type);
        weatherDetailsDialog.setCanceledOnTouchOutside(false);
        weatherDetailsDialog.show();
        weatherDetailsDialog.setClicklistener(new WeatherDetailsDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {

            }

            @Override
            public void doCancel() {
                weatherDetailsDialog.dismiss();
            }
        });
    }
    @Override
    protected void initData(View view) {
        try {
            Bundle args = getArguments();
//          获取栏目号（有时候获取不到，需要第二次获取）
            assert args != null;
            outLookWeather = (OutLookWeather) args.getSerializable("DailyForecastBean");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (outLookWeather != null) {
            ViewData();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            String ISAD = SaveShare.getValue(getActivity(), "ISAD");
            if (!TextUtils.isEmpty(ISAD) && ISAD.equals("1")) {
                BannerAd();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void ViewData() {
        danXiangLi = new DanXiangLi();
        bean = new DanXiangLi.DataBean();
        GlideUtil.getGlideUtil().setGifImages(getActivity(), R.mipmap.dynamic, dynamicState);
        weatherDetailsState.setText(outLookWeather.weatherDay.weather);
        temperature.setText(outLookWeather.highTemperature + "/" + outLookWeather.lowTemperature + "°");
        windPower.setText(outLookWeather.wind + " " + outLookWeather.windLevel);
        airQuality.setText(outLookWeather.airQuality);
        weatherDetailsWeek.setText(outLookWeather.week);
        sendibleTemperature.setText(outLookWeather.uv_index + "级");
        sunrise.setText(outLookWeather.sr);
        sunset.setText(outLookWeather.ss);
        String[] strings = outLookWeather.date.split("-");
        weatherDetailsDate.setText(strings[1]);
        bean.month = strings[0] + "月";
        bean.monthY = Util.MonthEnglish(Util.TurnDigital(strings[0]));
        bean.NongLiDay = strings[1] + "";
        WeatherPageData weatherPageData = new WeatherPageData((AppCompatActivity) getActivity());
        weatherPageData.AirForecast(outLookWeather.airQuality, 0, weatherDetailsTodayExcellentBg);
        String[] sunrise = outLookWeather.sr.split(":");
        String[] sunset = outLookWeather.ss.split(":");
        weatherDetailsIcon.setImageResource(WeatherUtils.getWeatherStatus(outLookWeather.weatherDay.iconRes).iconRes);
        weatherDetailsScroll.setOnScrollChangeListener(WeatherDetailsFragment.this);
        Calendar calendarNow = Calendar.getInstance();
        if (outLookWeather.week.equals("今天")) {
            int hour = calendarNow.get(Calendar.HOUR_OF_DAY);
            if (hour < Util.TurnDigital(sunrise[0])) {
                sun.sunAnim(0);
            } else if (hour > Util.TurnDigital(sunset[0])) {
                sun.sunAnim(0.99f);
            } else {
                sun.sunAnim(((float) hour - (float) Util.TurnDigital(sunrise[0])) / ((float) Util.TurnDigital(sunset[0]) - (float) Util.TurnDigital(sunrise[0])));
            }
        } else {
            sun.sunAnim(0);
        }
        AlmanacRequest.getAlmanacRequest().getDanXiangLiData(getActivity(), outLookWeather.date, WeatherDetailsFragment.this);
        AlmanacRequest.getAlmanacRequest().getHuangLiData(getActivity(), outLookWeather.date, new RequestSyntony<HuangLi>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HuangLi huangLi) {
                if (huangLi.getData() != null) {
                    huangLis = huangLi;
                    bean.sui_ci_shengxiao = huangLi.getData().getSui_ci().get(1) + huangLi.getData().getSui_ci().get(2) + huangLi.getData().getSui_ci().get(0);
                    bean.week = "周" + huangLi.getData().getWeek();
                    weatherDetailsLunarCalendar.setText(bean.sui_ci_shengxiao);
                    if (huangLi.getData().getYi() != null && huangLi.getData().getYi().size() > 0) {
                        if(huangLi.getData().getYi().get(0).getValues() != null && huangLi.getData().getYi().get(0).getValues().size() > 0){
                            if(huangLi.getData().getYi().get(0).getValues().size() > 1){
                                weatherDetailsSuitable.setText(huangLi.getData().getYi().get(0).getValues().get(0) + " " + huangLi.getData().getYi().get(0).getValues().get(1));
                            }else{
                                weatherDetailsSuitable.setText(huangLi.getData().getYi().get(0).getValues().get(0));
                            }
                        }
                    }
                    if (huangLi.getData().getJi() != null && huangLi.getData().getJi().size() > 0) {
                        if(huangLi.getData().getJi().get(0).getValues() != null && huangLi.getData().getJi().get(0).getValues().size() > 0){
                            if(huangLi.getData().getJi().get(0).getValues().size() > 1){
                                weatherDetailsAvoid.setText(huangLi.getData().getJi().get(0).getValues().get(0) + " " + huangLi.getData().getJi().get(0).getValues().get(1));
                            }else{
                                weatherDetailsAvoid.setText(huangLi.getData().getJi().get(0).getValues().get(0));
                            }
                        }
                     }

                }
            }
        });
//        windPowerPopup.setOnClickListener(v -> menuWindow = PopupWindowUtil.getPopupWindowUtil().setPopupWindowUtil(getActivity(), R.layout.pop_menu,true, humidityPopup, WeatherDetailsFragment.this));
        if (ISAD) {
            ISAD = false;
            String ISAD = SaveShare.getValue(getActivity(), "ISAD");
            if (!TextUtils.isEmpty(ISAD) && ISAD.equals("1")) {
                BannerAd();
            }
        }
    }

    @Override
    protected void initListener() {

    }

    private NativeAd nativelogic;
    private boolean aBoolean = true;

    public void BannerShow(boolean b) {
        if (nativelogic != null && b && aBoolean) {
            aBoolean = false;
            nativelogic.AdShow(null);
        }
    }

    public void BannerAd() {//
        // 获得原生banner广告对象
        nativelogic = Ad.getAd().NativeAD(getActivity(), "98f8e423-02e0-49f5-989f-af46f5c59203", "3ab86803-f1bf-4f18-86d8-1ca3f7d7962e", "67C53558D3E3485EA681EA21735A5003", new AdProtogenesisListener() {
            @Override
            public void onADReady(Native url, NativeAd nativelogic) {
                try {
                    if (TextUtils.isEmpty(nativelogic.nativeObject.sdk_code)) {
                        Glide.with(Objects.requireNonNull(getActivity())).load(url.src).into(adImageBanner);
                        adLin.setVisibility(View.VISIBLE);
                        bannerContainer.setVisibility(View.GONE);
                        WDGDTAd.setVisibility(View.GONE);
                    } else if (nativelogic.nativeObject.sdk_code.equals("GDT_SDK")) {
                        GDTBanner(WDGDTAd);
                        bannerContainer.setVisibility(View.GONE);
                        adLin.setVisibility(View.GONE);
                    } else if (nativelogic.nativeObject.sdk_code.equals("TOUTIAO_SDK")) {
                        adLin.setVisibility(View.GONE);
                        WDGDTAd.setVisibility(View.GONE);
                        PangolinBannerAd.getPangolinBannerAd().BannerAD(getActivity(), bannerContainer, nativelogic);
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
            nativelogic.OnClick(null);
        });
    }

    private UnifiedBannerView GDTBanner(RelativeLayout relatAd) {
        // 创建 Banner 广告 AdView 对象
        // appId : 在 http://e.qq.com/dev/ 能看到的 app 唯一字符串
        // posId : 在 http://e.qq.com/dev/ 生成的数字串，并非 appid 或者 appkey
        UnifiedBannerView banner = new UnifiedBannerView(getActivity(), nativelogic.nativeObject.appid, nativelogic.nativeObject.posid, new UnifiedBannerADListener() {
            @Override
            public void onNoAD(AdError adError) {
                Log.i("AD_DEMO", "BannerNoAD，eCode=" + adError.getErrorCode());
                try {
                    relatAd.setVisibility(View.GONE);
                    if (nativelogic != null) {
                        nativelogic.OnRequest(adError.getErrorCode() + "", adError.getErrorMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onADReceive() {
                try {
                    relatAd.setVisibility(View.VISIBLE);
                    if (nativelogic != null) {
                        nativelogic.OnRequest("0", "msg");
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
                    if (nativelogic != null) {
                        nativelogic.AdShow(relatAd);
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
                    if (nativelogic != null) {
                        nativelogic.OnClick(relatAd);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        assert rootView != null;
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(DanXiangLi danXiangLi) {
        if (danXiangLi.getData() != null) {
//            this.danXiangLi = danXiangLi;
            bean.setAuthor_name(danXiangLi.getData().getAuthor_name());
            bean.setAuthor_type(danXiangLi.getData().getAuthor_type());
            bean.setContent(danXiangLi.getData().getContent());
            bean.setDate(danXiangLi.getData().getDate());
            bean.setExtract(danXiangLi.getData().getExtract());
            bean.setProduction(danXiangLi.getData().getProduction());
            bean.setType(danXiangLi.getData().getType());
            cultivateCalendar.setText(danXiangLi.getData().getContent());
            danxiangliTvDetails.setText(danXiangLi.getData().getExtract());
        }

    }

    @OnClick({R.id.ji_yi_lin, R.id.dynamic_state, R.id.ad_image_banner_clear, R.id.humidity_popup,R.id.wind_power_popup,R.id.weather_details_today_excellent_bg_rel,R.id.sendible_temperature_rel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ji_yi_lin:
                if (huangLis != null) {
                    Intent intent = new Intent(getActivity(), YiJiActivity.class);
                    intent.putExtra("huangLis", huangLis);
                    Objects.requireNonNull(getActivity()).overridePendingTransition(com.constellation.xylibrary.R.anim.in_from_right, com.constellation.xylibrary.R.anim.out_to_left);
                    getActivity().startActivity(intent);
                }
                break;
            case R.id.dynamic_state:
                break;
            case R.id.ad_image_banner_clear:
                adLin.setVisibility(View.GONE);
                break;
            case R.id.humidity_popup:
                DialogShow("室内空气质量标准：",R.string.popup_tv,R.mipmap.humidity,0);
//                menuWindow = PopupWindowUtil.getPopupWindowUtil().setPopupWindowUtil(getActivity(), R.layout.pop_menu,false, humidityPopup, WeatherDetailsFragment.this);
                break;
            case R.id.wind_power_popup:
                DialogShow("",R.string.wind_power_popup_tv,R.mipmap.wind_power,2);
                 break;
            case R.id.weather_details_today_excellent_bg_rel:
                DialogShow("",R.string.air_quality_popup_tv,R.mipmap.air_quality,2);
                 break;
            case R.id.sendible_temperature_rel:
                DialogShow("",R.string.ultraviolet_ray_popup_tv,R.mipmap.ultraviolet_ray_head,1);
                break;
        }
    }

    @Override
    public void onScrollChange(NestedScrollView nestedScrollView, int i, int i1, int i2, int i3) {
        BannerShow(adLin.getLocalVisibleRect(new Rect()));
    }

    @OnClick(R.id.dynamic_state_lin)
    public void onClick() {
        danXiangLi.setData(bean);
        MobclickAgent.onEvent(getActivity(), "Weather_Details_danxiangli", "Weather_Details_danxiangli");
        DXiangLiActivity.startActivity(getActivity(), danXiangLi);
    }

    @Override
    public void setData(View view) {
         TextView textView = view.findViewById(R.id.pop_tv);
         textView.setText(getString(R.string.wind_power_popup_tv));
    }
}
