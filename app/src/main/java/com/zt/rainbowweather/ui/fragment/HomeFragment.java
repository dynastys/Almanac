package com.zt.rainbowweather.ui.fragment;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.check.ox.sdk.LionListener;
import com.check.ox.sdk.LionWallView;
import com.chenguang.weather.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;
import com.xy.xylibrary.base.BaseFragment;
import com.xy.xylibrary.utils.Shares;
import com.xy.xylibrary.utils.Utils;
import com.zt.rainbowweather.entity.AddressBean;
import com.zt.rainbowweather.entity.City;
import com.zt.rainbowweather.entity.MoveCityEvent;
import com.zt.rainbowweather.entity.city.Event;
import com.zt.rainbowweather.entity.city.Refresh;
import com.zt.rainbowweather.presenter.home.CityWeatherQuantity;
import com.zt.rainbowweather.presenter.home.OnPageChangeListener;
import com.zt.rainbowweather.ui.activity.AddressActivity;
import com.zt.rainbowweather.utils.ConstUtils;
import com.zt.rainbowweather.utils.SPUtils;
import com.zt.rainbowweather.view.NoScrollViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author zw
 * @time 2019-3-8
 * 首页
 */
public class HomeFragment extends BaseFragment implements OnPageChangeListener {


    Unbinder unbinder;
    @BindView(R.id.view_pager)
    NoScrollViewPager viewPager;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.rb_main)
    RadioGroup rbMain;
    @BindView(R.id.ll_main_indicator)
    LinearLayout llMainIndicator;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.actionBarSize)
    TextView actionBarSize;
    @BindView(R.id.name_city)
    TextView nameCity;


    private CityWeatherQuantity cityWeatherQuantity;
    private List<City> cities = new ArrayList<>();
    private String share_details;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initData(View view) {

        cities = LitePal.findAll(City.class);
        ViewGroup.LayoutParams layoutParams = actionBarSize.getLayoutParams();
        layoutParams.height = Utils.getStatusBarHeight(getActivity());
        actionBarSize.setLayoutParams(layoutParams);
        cityWeatherQuantity = new CityWeatherQuantity(getActivity(), HomeFragment.this, viewPager, rbMain);
        cityWeatherQuantity.PageSize(HomeFragment.this);

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event event) {
        cities = LitePal.findAll(City.class);
//        cityWeatherQuantity = new CityWeatherQuantity(getActivity(), HomeFragment.this, viewPager, rbMain);
        cityWeatherQuantity.updateWeatherFragment(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Refresh event) {
        if (event.overscrollTop == 0) {
            llBottom.setVisibility(View.VISIBLE);
        } else
            llBottom.setVisibility(View.GONE);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void moveCityPosition(MoveCityEvent event) {
        cityWeatherQuantity.setMoveFragmentPosition(event);
        cities = cityWeatherQuantity.setFragments();



    }

    @Override
    protected void initListener() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private long exitTime = 0;

    @OnClick({R.id.iv_add, R.id.iv_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_add:
                AddressActivity.startActivity(getActivity(), cityWeatherQuantity.getAllAddresses());
                break;
            case R.id.iv_more:
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    Shares.localshare(getActivity(), "aaa", viewPager);
                    exitTime = System.currentTimeMillis();
                }
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        try {
            nameCity.setText(cities.get(position).name);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        share_details
    }

    @Override
    public void onPageSelected(int position) {
        llBottom.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void setCanSlipping(boolean enable) {
        try {
            viewPager.setNoScroll(enable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        cityWeatherQuantity.saveCityToSp();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("HomeFragment"); //统计页面("MainScreen"为页面名称，可自定义)
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("HomeFragment");

    }


}
