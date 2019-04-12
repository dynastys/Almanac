package com.zt.rainbowweather.presenter.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chenguang.weather.R;
import com.xy.xylibrary.Interface.BaseAdapterListener;
import com.xy.xylibrary.Interface.SwipeRefreshListener;
import com.xy.xylibrary.base.BaseAdapter;
import com.xy.xylibrary.base.BaseFragment;
import com.xy.xylibrary.config.ColumnHorizontalPackage;
import com.xy.xylibrary.config.SwipeRefreshOnRefresh;
import com.xy.xylibrary.refresh.SuperEasyRefreshLayout;
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
import com.zt.rainbowweather.entity.advise.AdviseTitleBean;
import com.zt.rainbowweather.entity.news.NewColumn;
import com.zt.rainbowweather.entity.weather.ConventionWeather;
import com.zt.rainbowweather.entity.weather.Now;
import com.zt.rainbowweather.presenter.ScrollGridLayoutManager;
import com.zt.rainbowweather.presenter.ScrollLinearLayoutManager;
import com.zt.rainbowweather.presenter.dynamic.BaseWeatherType;
import com.zt.rainbowweather.presenter.dynamic.DynamicWeatherView;
import com.zt.rainbowweather.presenter.dynamic.FogType;
import com.zt.rainbowweather.presenter.dynamic.HailType;
import com.zt.rainbowweather.presenter.dynamic.HazeType;
import com.zt.rainbowweather.presenter.dynamic.OvercastType;
import com.zt.rainbowweather.presenter.dynamic.RainType;
import com.zt.rainbowweather.presenter.dynamic.SandstormType;
import com.zt.rainbowweather.presenter.dynamic.ShortWeatherInfo;
import com.zt.rainbowweather.presenter.dynamic.SnowType;
import com.zt.rainbowweather.presenter.dynamic.SunnyType;
import com.zt.rainbowweather.presenter.request.NewsRequest;
import com.zt.rainbowweather.presenter.request.WeatherRequest;
import com.zt.rainbowweather.ui.activity.AdviseDetailActivity;
import com.zt.rainbowweather.ui.fragment.ColumnFragment;
import com.zt.rainbowweather.ui.fragment.WeatherFragment;
import com.zt.rainbowweather.utils.ToastUtils;
import com.zt.rainbowweather.utils.WeatherUtils;
import com.zt.rainbowweather.view.MiuiWeatherView;
import com.zt.rainbowweather.view.ScrollFutureDaysWeatherView;
import com.zt.rainbowweather.view.TranslucentActionBar;
import com.zt.rainbowweather.view.TranslucentScrollView;
import com.zt.xuanyin.Interface.AdProtogenesisListener;
import com.zt.xuanyin.controller.Ad;
import com.zt.xuanyin.controller.NativeAd;
import com.zt.xuanyin.entity.model.Native;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WeatherPageData {

    private Activity context;
    private RecyclerView recyclerView;
    private MiuiWeatherView weather;
    private ScrollFutureDaysWeatherView scrollFutureDaysWeatherView;
    private DynamicWeatherView dynamicWeather;
    private ColumnHorizontalPackage columnHorizontalPackage;
    private ArrayList<ColumnFragment> fragments = new ArrayList<>();
    /*栏目数据*/
    private List<String> userColumnList = new ArrayList<>();
    private String[] ID = new String[]{"26","28","29","30","32"};
    private NativeAd nativelogic;
    private float ClickX;
    private float ClickY;

    public WeatherPageData(Activity activity,RecyclerView recyclerView,MiuiWeatherView weather,ScrollFutureDaysWeatherView scrollFutureDaysWeatherView,DynamicWeatherView dynamicWeather){
        this.context = activity;
        this.recyclerView = recyclerView;
        this.weather = weather;
        this.dynamicWeather = dynamicWeather;
        this.scrollFutureDaysWeatherView = scrollFutureDaysWeatherView;
    }

    /**
     * 天气数据请求
     * */
    public void RequestWeatherData(City city,RequestSyntony<ConventionWeather> requestSyntony){
          WeatherRequest.getWeatherRequest().getConventionWeatherData(context, city.name,new RequestSyntony<ConventionWeather>() {

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
                requestSyntony.onNext(conventionWeather);
                setExponent(conventionWeather.getHeWeather6().get(0).getLifestyle());
                HourWeather(conventionWeather.getHeWeather6().get(0).getHourly());
                DayTendency(conventionWeather.getHeWeather6().get(0).getDaily_forecast());
                SwitchDynamicWeather(conventionWeather.getHeWeather6().get(0).getNow().getCond_txt(), dynamicWeather);
            }
        });
    }


    /**
     * 新闻栏目
     * */
    public void RequestNewsData(BaseFragment fragment,LinearLayout mRadioGroupContent,ColumnHorizontalScrollView column,CustomScrollViewPager viewpagerColumn){
        NewsRequest.getNewsRequest().getNewsColumnData(context, new RequestSyntony<NewColumn>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(NewColumn newColumn) {

                    for (int i = 0; i < newColumn.getDate().size(); i++) {
                        //传递数据到fragment
                        Bundle data = new Bundle();
                        if(i == 0){
                            SaveShare.saveValue(context,"Channelid",newColumn.getDate().get(i).getChannelid()+"");
                        }
                        data.putString("text", "" + newColumn.getDate().get(i).getChannelid());
                        ColumnFragment newfragment = new ColumnFragment();
                        newfragment.setviewPager(viewpagerColumn);
                        newfragment.setArguments(data);
                        //加载fragment
                        fragments.add(newfragment);
                        userColumnList.add(newColumn.getDate().get(i).getChannel_name());
                    }

                columnHorizontalPackage = new ColumnHorizontalPackage<String>(context, column, viewpagerColumn);
                columnHorizontalPackage.initData(fragment, mRadioGroupContent, fragments, userColumnList);
            }
        });

    }

    /**
     * 刷新
     * */
    public void setSwipeRefreshOnRefresh(SuperEasyRefreshLayout shopListSwipeRefresh, final SwipeRefreshListener swipeRefreshListener){
        //刷新
        SwipeRefreshOnRefresh swipeRefreshOnRefresh = new SwipeRefreshOnRefresh();
        swipeRefreshOnRefresh.SwipeRefresh(context, shopListSwipeRefresh, swipeRefreshListener);
    }

    /**
     * 渐变状态栏
     * */
    public void GradientStatusBar(TranslucentScrollView DetailsScrollview, TranslucentActionBar DetailsActionbar, TranslucentScrollView.TranslucentChangedListener translucentChangedListener){
        //初始actionBar
        DetailsActionbar.setData(null, 0, null, 0, null, null);
        //开启渐变
        DetailsActionbar.setNeedTranslucent();
        //设置状态栏高度
        DetailsActionbar.setStatusBarHeight(Utils.getStatusBarHeight());
        //设置透明度变化监听
        DetailsScrollview.setTranslucentChangedListener(translucentChangedListener);
        //关联需要渐变的视图
        DetailsScrollview.setTransView(DetailsActionbar);
        //设置ActionBar键渐变颜色
        DetailsScrollview.setTransColor(context.getResources().getColor(R.color.main_bg4));
        //关联伸缩的视图
//    shopDetailsScrollview.setPullZoomView(shopDetailsBanner);
    }

    /**
     * 指数
     * */
    public void setExponent(List<ConventionWeather.HeWeather6Bean.LifestyleBean> lifestyleBeans){
        ScrollLinearLayoutManager linearLayoutManager = new ScrollLinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        List<ConventionWeather.HeWeather6Bean.LifestyleBean> list = new ArrayList<>();
        List<String> images = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            switch (lifestyleBeans.get(i).getType()){
                case "air":
                    lifestyleBeans.get(i).setType("空气");
                    list.add(lifestyleBeans.get(i));
                    images.add("http://bos.tq.ifjing.com/rbpiczy/weather/pm/v1/icon/t_pm_status_bkg_1.png");
                    break;
                case "comf":
                    lifestyleBeans.get(i).setType("舒适度");
                    list.add(lifestyleBeans.get(i));
                    images.add("http://bos.tq.ifjing.com/weather/guide/icon/v1/guide-temperature-20171205.png");
                    break;
                case "drsg":
                    lifestyleBeans.get(i).setType("穿衣");
                    list.add(lifestyleBeans.get(i));
                    images.add("http://bos.tq.ifjing.com/weather/v1/guide/chuanyi/fy.png");
                    break;
                case "uv":
                    lifestyleBeans.get(i).setType("紫外线");
                    list.add(lifestyleBeans.get(i));
                    images.add("http://bos.tq.ifjing.com/weather/guide/icon/v1/uv.png");
                    break;
                case "sport":
                    lifestyleBeans.get(i).setType("运动");
                    list.add(lifestyleBeans.get(i));
                    images.add("http://bos.tq.ifjing.com/weather/guide/icon/v1/yd.png");
                    break;
                case "mu":
                    lifestyleBeans.get(i).setType("化妆");
                    list.add(lifestyleBeans.get(i));
                    images.add("http://bos.tq.ifjing.com/weather/guide/icon/v1/hz.png");
                    break;
                case "flu":
                    lifestyleBeans.get(i).setType("感冒");
                    list.add(lifestyleBeans.get(i));
                    images.add("http://bos.tq.ifjing.com/weather/guide/icon/v1/gm.png");
                    break;
                case "cw":
                    lifestyleBeans.get(i).setType("洗车");
                    list.add(lifestyleBeans.get(i));
                    images.add("http://bos.tq.ifjing.com/weather/guide/icon/v1/xc.png");
                    break;
            }
//            list.add(lifestyleBeans.get(i));
        }
        BaseAdapter baseAdapter = new BaseAdapter<>(R.layout.recycler_item, list, (viewHolder, item) -> {
            try {
                viewHolder.setText(R.id.brf, item.getBrf());
                viewHolder.setText(R.id.popup_recycler_tv, item.getType());
//                    viewHolder.setImageResource(R.id.popup_recycler_image, imgs[viewHolder.getAdapterPosition()]);
                GlideUtil.getGlideUtil().setImages(context, images.get(viewHolder.getAdapterPosition()), (ImageView) viewHolder.getView(R.id.popup_recycler_image));

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        baseAdapter.setOnItemClickListener((adapter, view, position) -> {
            AdviseTitleBean adviseTitleBean = new AdviseTitleBean();
            adviseTitleBean.contentUrl = "https://sec-cdn.static.xiaomi.net/secStatic/imgs/b769c44e846b6f0fed3c9780d0bf431aae425f02.png";
            adviseTitleBean.headerImgUrl = "https://sec-cdn.static.xiaomi.net/secStatic/imgs/b769c44e846b6f0fed3c9780d0bf431aae425f02.png";
            adviseTitleBean.imgUrl = "http://f4.market.xiaomi.com/download/MiSafe/001a2c4210f6944e83427fd96c3216666b4de8646/a.webp";
            adviseTitleBean.title = lifestyleBeans.get(position).getBrf();
            adviseTitleBean.headerSummary = lifestyleBeans.get(position).getTxt();
            adviseTitleBean.channelId = lifestyleBeans.get(position).getType();
            adviseTitleBean.indexId = ID[position];
            AdviseDetailActivity.startActivity(context, adviseTitleBean);
        });
        recyclerView.setAdapter(baseAdapter);
    }

    /**
     * 趋势预报
     * */
    public void DayTendency(List<ConventionWeather.HeWeather6Bean.DailyForecastBean> DailyForecast){
        List<OutLookWeather> lookWeathers = new ArrayList<>();
        Random ra1 =new Random();
        for (int i = 0; i < DailyForecast.size(); i++) {
            OutLookWeather outLookWeather = new OutLookWeather();
            if(i == 0){
                outLookWeather.week = "今天";
            }else if(i == 1){
                outLookWeather.week = "明天";
            } else {
                outLookWeather.week = WeatherUtils.getWeek(DailyForecast.get(i).getDate());
            }
            if (!TextUtils.isEmpty(DailyForecast.get(i).getDate()) && DailyForecast.get(i).getDate().length()>=4){
                outLookWeather.date = DailyForecast.get(i).getDate().substring(DailyForecast.get(i).getDate().length()-5,DailyForecast.get(i).getDate().length());
            }
            outLookWeather.airQuality = WeatherUtils.getAqi(Integer.parseInt(DailyForecast.get(i).getVis()));
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

            lookWeathers.add(outLookWeather);
        }
        scrollFutureDaysWeatherView.setData(lookWeathers);
    }

    /**
     * 小时预报
     * */
    public void HourWeather(List<ConventionWeather.HeWeather6Bean.HourlyBean> Hourlys){
        List<WeatherBean> list = new ArrayList<>();
        Random ra1 =new Random();

        for (int i = 0; i < Hourlys.size(); i++) {
            WeatherBean weatherBean = new WeatherBean();
            weatherBean.temperature = Integer.parseInt(Hourlys.get(i).getTmp());
            weatherBean.temperatureStr = Hourlys.get(i).getTmp()+"°";
            if (!TextUtils.isEmpty(Hourlys.get(i).getTime()) && Hourlys.get(i).getTime().length()>=4){
                weatherBean.time = Hourlys.get(i).getTime().substring(Hourlys.get(i).getTime().length()-5,Hourlys.get(i).getTime().length());
            }
            WeatherUtilBean weatherDay = new WeatherUtilBean();
            weatherDay.iconRes = Integer.parseInt(Hourlys.get(i).getCond_code());
            weatherDay.weather = Hourlys.get(i).getCond_txt();
            weatherDay.weatherId = Integer.parseInt(Hourlys.get(i).getCond_code());
            weatherBean.weather = weatherDay;
            weatherBean.weatherType = 3;
            list.add(weatherBean);
        }
        weather.setData(list);
    }


    /**
     * 动态图
     * */
    public void SwitchDynamicWeather(String which,DynamicWeatherView dynamicWeatherView) {
        final String[] items = new String[]{"晴", "晴（夜晚）", "多云", "阴", "雨", "雨夹雪",
                "雪", "冰雹", "雾", "雾霾", "沙尘暴"};
        String whichs = "晴";
        for (int i = 0; i < items.length; i++) {
            if (which.contains(items[i])) {
                whichs = items[i];
            }
        }
        ShortWeatherInfo info = new ShortWeatherInfo();
        info.setCode("100");
        info.setWindSpeed("11");
        BaseWeatherType type;
        switch (whichs) {
            case "晴":
                info.setSunrise("00:01");
                info.setSunset("23:59");
                info.setMoonrise("00:00");
                info.setMoonset("00:01");
                type = new SunnyType(context, info);
                break;
            case "晴（夜晚）":
                info.setSunrise("00:00");
                info.setSunset("00:01");
                info.setMoonrise("00:01");
                info.setMoonset("23:59");
                type = new SunnyType(context, info);
                break;
            case "多云":
                info.setSunrise("00:01");
                info.setSunset("23:59");
                info.setMoonrise("00:00");
                info.setMoonset("00:01");
                SunnyType sunnyType = new SunnyType(context, info);
                sunnyType.setCloud(true);
                type = sunnyType;
                break;
            case "阴":
                type = new OvercastType(context, info);
                break;
            case "雨":
                RainType rainType = new RainType(context, RainType.RAIN_LEVEL_2, RainType.WIND_LEVEL_2);
                rainType.setFlashing(true);
                type = rainType;
                break;
            case "雨夹雪":
                RainType rainSnowType = new RainType(context, RainType.RAIN_LEVEL_1, RainType.WIND_LEVEL_1);
                rainSnowType.setSnowing(true);
                type = rainSnowType;
                break;
            case "雪":
                type = new SnowType(context, SnowType.SNOW_LEVEL_2);
                break;
            case "冰雹":
                type = new HailType(context);
                break;
            case "雾":
                type = new FogType(context);
                break;
            case "雾霾":
                type = new HazeType(context);
                break;
            case "沙尘暴":
                type = new SandstormType(context);
                break;
            default:
                type = new SunnyType(context, info);
        }
        dynamicWeatherView.setType(type);

    }

    @SuppressLint("ClickableViewAccessibility")
    public void BannerAd(Context context, ImageView banner, RelativeLayout relatAd) {//
        // 获得原生banner广告对象
        nativelogic = Ad.getAd().NativeAD(context, "250af218-2f24-4a3b-bcfe-6e268269ff5c", "e33fc0af-e47c-4970-b600-259b326cb63f", "D6BA664F3B4D46AC92C98D710CDB0084", new AdProtogenesisListener() {
            @Override
            public void onADReady(Native url) {
                Glide.with(context).load(url.src).into(banner);
                nativelogic.AdShow(relatAd);
                relatAd.setVisibility(View.VISIBLE);
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

    }

}
