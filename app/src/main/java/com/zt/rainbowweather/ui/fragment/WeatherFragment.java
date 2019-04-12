package com.zt.rainbowweather.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.check.ox.sdk.LionListener;
import com.check.ox.sdk.LionWallView;
import com.chenguang.weather.R;
import com.google.common.eventbus.Subscribe;
import com.umeng.analytics.MobclickAgent;
import com.xy.xylibrary.Interface.SwipeRefreshListener;
import com.xy.xylibrary.base.BaseFragment;
import com.xy.xylibrary.config.ColumnHorizontalPackage;
import com.xy.xylibrary.refresh.SuperEasyRefreshLayout;
import com.xy.xylibrary.utils.Utils;
import com.xy.xylibrary.view.ColumnHorizontalScrollView;
import com.xy.xylibrary.view.CustomScrollViewPager;
import com.zt.rainbowweather.BasicApplication;
import com.zt.rainbowweather.api.RequestSyntony;
import com.zt.rainbowweather.entity.AddressBean;
import com.zt.rainbowweather.entity.City;
import com.zt.rainbowweather.entity.HeaderBean;
import com.zt.rainbowweather.entity.WeatherUtilBean;
import com.zt.rainbowweather.entity.city.Refresh;
import com.zt.rainbowweather.entity.news.NewColumn;
import com.zt.rainbowweather.entity.weather.ConventionWeather;
import com.zt.rainbowweather.presenter.dynamic.DynamicWeatherView;
import com.zt.rainbowweather.presenter.home.WeatherPageData;
import com.zt.rainbowweather.presenter.request.NewsRequest;
import com.zt.rainbowweather.ui.activity.IndexOfLivingActivity;
import com.zt.rainbowweather.utils.Lunar;
import com.zt.rainbowweather.view.DrawableCenterTextView;
import com.zt.rainbowweather.view.MiuiWeatherView;
import com.zt.rainbowweather.view.ScrollFutureDaysWeatherView;
import com.zt.rainbowweather.view.TranslucentActionBar;
import com.zt.rainbowweather.view.TranslucentScrollView;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WeatherFragment extends BaseFragment implements SwipeRefreshListener, TranslucentScrollView.TranslucentChangedListener, RequestSyntony<ConventionWeather>, TranslucentScrollView.OnHoldTabScrollViewScrollChanged,LionListener {

    @BindView(R.id.dynamicWeather)
    DynamicWeatherView dynamicWeather;
    @BindView(R.id.tv_h_curr_temp)
    TextView tvHCurrTemp;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_h_curr_weather)
    TextView tvHCurrWeather;
    @BindView(R.id.tv_h_rain)
    DrawableCenterTextView tvHRain;
    @BindView(R.id.rv_advise_title)
    RecyclerView rvAdviseTitle;
    @BindView(R.id.banner)
    ImageView banner;
    @BindView(R.id.relat_ad)
    RelativeLayout relatAd;
    @BindView(R.id.weather)
    MiuiWeatherView weather;
    @BindView(R.id.home_scrollview)
    TranslucentScrollView scrollView;
    @BindView(R.id.list_swipe_refresh)
    SuperEasyRefreshLayout ListSwipeRefresh;
    Unbinder unbinder;
    @BindView(R.id.details_actionbar)
    TranslucentActionBar DetailsActionbar;
    @BindView(R.id.sfdwv)
    ScrollFutureDaysWeatherView sfdwv;
    @BindView(R.id.mRadioGroup_content)
    LinearLayout mRadioGroupContent;
    @BindView(R.id.column)
    ColumnHorizontalScrollView column;
    @BindView(R.id.viewpager_column)
    CustomScrollViewPager viewpagerColumn;
    @BindView(R.id.forecast_date)
    TextView forecastDate;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.tv_h_aqi)
    DrawableCenterTextView tvHAqi;
    @BindView(R.id.at_present)
    TextView atPresent;
    @BindView(R.id.rl_center)
    RelativeLayout rlCenter;
    @BindView(R.id.rl_top)
    RelativeLayout topRl;
    @BindView(R.id.lin_wether)
    LinearLayout linWether;
    @BindView(R.id.list_bar)
    TextView listBar;
    @BindView(R.id.load_more)
    TextView loadMore;
    @BindView(R.id.shop_list_bar)
    TextView shopListBar;
    @BindView(R.id.TMAw1)
    LionWallView TMAw1;

    private City currCity;
    private HeaderBean mHeaderBean = new HeaderBean();
    private int mHeight = 0;
    private boolean canJump = true;
    private WeatherPageData weatherPageData;
    private AddressBean mAddressBean;
    private static HomeFragment homeFragments;
    private String size = "0";


    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_wether;
    }

    public static WeatherFragment newInstance(City city, String size,HomeFragment homeFragment) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle bundle = new Bundle();
        bundle.putString("size",size);
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
            ViewGroup.LayoutParams layoutParamsBar = shopListBar.getLayoutParams();
            layoutParamsBar.height = Utils.getStatusBarHeight(getActivity())+ Utils.dip2px(getActivity(),50);
            shopListBar.setLayoutParams(layoutParamsBar);
            ViewGroup.LayoutParams layoutParams = listBar.getLayoutParams();
            layoutParams.height = Utils.getStatusBarHeight(getActivity());
//        dynamicWeather.setZOrderOnTop(true);
            dynamicWeather.getHolder().setFormat(PixelFormat.TRANSPARENT);
            listBar.setLayoutParams(layoutParams);
            listBar.setVisibility(View.GONE);
            currCity = (City) getArguments().getSerializable("city");
            size = getArguments().getString("size");
            mHeaderBean.address = currCity.name;
            topRl.setVisibility(View.GONE);
            scrollView.setOnObservableScrollViewScrollChanged(this);
            weatherPageData = new WeatherPageData(getActivity(), rvAdviseTitle, weather, sfdwv, dynamicWeather);
            weatherPageData.RequestWeatherData(currCity, WeatherFragment.this);
            weatherPageData.setSwipeRefreshOnRefresh(ListSwipeRefresh, WeatherFragment.this);
            weatherPageData.GradientStatusBar(scrollView, DetailsActionbar, WeatherFragment.this);
            weatherPageData.BannerAd(getActivity(),banner,relatAd);
                 new Thread(() -> {
                    weatherPageData.RequestNewsData(WeatherFragment.this,mRadioGroupContent,column,viewpagerColumn);
                }).start();


            //解决view左右滑动有view_pager左右滑动的冲突
            weather.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        homeFragments.setCanSlipping(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        homeFragments.setCanSlipping(false);
                        break;
                }
                return false;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        TMAw1.setAdListener(WeatherFragment.this);
        TMAw1.loadAd(273281);
    }

    @Override
    protected void initListener() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    /**
     * 刷新回调
     */
    @Override
    public void onRefresh() {
        weatherPageData.RequestWeatherData(currCity, WeatherFragment.this);
        weatherPageData.BannerAd(getActivity(),banner,relatAd);
//        weatherPageData.RequestNewsData(WeatherFragment.this,mRadioGroupContent,column,viewpagerColumn);
    }

    @Override
    public void onDropHeight(float overscrollTop) {
        if (canJump) {
            EventBus.getDefault().post(new Refresh(overscrollTop));
        }
    }

    @Override
    public void onTranslucentChanged(int transAlpha) {
        if (transAlpha > 180) {
            DetailsActionbar.setNeedTranslucent(true, true);
        } else {
            DetailsActionbar.setNeedTranslucent(true, false);
        }
        homeFragments.setCanSlipping(false);
    }

    @Subscribe
    public void updateLocateWeather(City city) {
        currCity = city;
        if (city.name.equals("定位失败")) {//如果是定位的城市
            tvAddress.setText("请开启定位权限或网络连接");
            if (ListSwipeRefresh != null) {
                ListSwipeRefresh.setRefreshing(false);
            }
        } else {
            tvAddress.setText(city.affiliation);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
//           if(currCity != null && !LitePal.findAll(City.class).get(Integer.parseInt(size)).name.equals(currCity.name) && dynamicWeather != null){
//               weatherPageData.RequestWeatherData(currCity, WeatherFragment.this);
//               weatherPageData.BannerAd(getActivity(),banner,relatAd);
//           }
            if(dynamicWeather != null){
                dynamicWeather.onResume();
            }
        }else{
            if (dynamicWeather != null) {
                dynamicWeather.onPause();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(dynamicWeather != null){
            dynamicWeather.onResume();
        }
        MobclickAgent.onPageStart("WeatherFragment"); //统计页面("MainScreen"为页面名称，可自定义)
    }

    @Override
    public void onPause() {
        super.onPause();
        if (dynamicWeather != null) {
            dynamicWeather.onPause();
        }
        MobclickAgent.onPageEnd("WeatherFragment");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dynamicWeather != null) {
            dynamicWeather.onDestroy();
        }
        if (TMAw1 != null) {
            TMAw1.destroy();
        }
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(ConventionWeather conventionWeather) {
        if (ListSwipeRefresh != null) {
            ListSwipeRefresh.setRefreshing(false);
        }
        atPresent.setText("当前：" + conventionWeather.getHeWeather6().get(0).getNow().getTmp() + "℃");
        WeatherUtilBean weatherDayN = new WeatherUtilBean();
        weatherDayN.iconRes = Integer.parseInt(conventionWeather.getHeWeather6().get(0).getNow().getCond_code());
        weatherDayN.weather = conventionWeather.getHeWeather6().get(0).getNow().getCond_txt();
        weatherDayN.weatherId = Integer.parseInt(conventionWeather.getHeWeather6().get(0).getNow().getCond_code());
        mHeaderBean.weather = weatherDayN;
        mHeaderBean.currTemp = Integer.parseInt(conventionWeather.getHeWeather6().get(0).getNow().getTmp());
        Calendar calendar = Calendar.getInstance();
        Lunar lunar = new Lunar(calendar);
        //农历的月日
        forecastDate.setText("农历 " + lunar.toString());
        tvHCurrTemp.setText(conventionWeather.getHeWeather6().get(0).getNow().getTmp() + "℃");
        if (BasicApplication.getLocatedCity() != null && !TextUtils.isEmpty(BasicApplication.getLocatedCity().name) && BasicApplication.getLocatedCity().name.contains(conventionWeather.getHeWeather6().get(0).getBasic().getLocation())) {
            tvAddress.setText(BasicApplication.getLocatedCity().affiliation);
        } else {
            tvAddress.setText(conventionWeather.getHeWeather6().get(0).getBasic().getLocation());
        }
        tvHCurrWeather.setText(conventionWeather.getHeWeather6().get(0).getNow().getCond_txt());
        tvHAqi.setText("湿度 " + conventionWeather.getHeWeather6().get(0).getNow().getHum());
        tvHRain.setText(conventionWeather.getHeWeather6().get(0).getNow().getWind_dir() + " " + conventionWeather.getHeWeather6().get(0).getNow().getWind_sc());
        loadMore.setOnClickListener(view -> IndexOfLivingActivity.startActivity(getActivity(), conventionWeather.getHeWeather6().get(0).getLifestyle()));
    }


    @Override
    public void onObservableScrollViewScrollChanged(int l, int t, int oldl, int oldt) {
        if (linWether != null) {
            mHeight = linWether.getHeight();
        }
        if (t >= mHeight) {
            if (column.getParent() != topRl) {
                rlCenter.removeView(column);
                topRl.addView(column);
                Log.e("TAG", "onDropHeight: ");
                listBar.setVisibility(View.VISIBLE);
                topRl.setVisibility(View.VISIBLE);
                DetailsActionbar.setVisibility(View.GONE);
                EventBus.getDefault().post(new Refresh(100));
                canJump = false;
            }
        } else {
            if (column.getParent() != rlCenter) {
                topRl.removeView(column);
                rlCenter.addView(column);
                Log.e("TAG", "onDropHeight: ");
                EventBus.getDefault().post(new Refresh(0));
                listBar.setVisibility(View.GONE);
                topRl.setVisibility(View.GONE);
                DetailsActionbar.setVisibility(View.VISIBLE);
                canJump = true;
            }
        }

    }

    @Override
    public void onReceiveAd() {

    }

    @Override
    public void onFailedToReceiveAd() {

    }

    @Override
    public void onLoadFailed() {

    }

    @Override
    public void onCloseClick() {

    }

    @Override
    public void onAdClick() {

    }

    @Override
    public void onAdExposure() {

    }
}
