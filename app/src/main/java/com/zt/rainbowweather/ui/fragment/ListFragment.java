package com.zt.rainbowweather.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.xy.xylibrary.base.BaseAdapter;
import com.xy.xylibrary.base.BaseFragment;
import com.xy.xylibrary.utils.GlideUtil;
import com.zt.rainbowweather.entity.City;
import com.zt.rainbowweather.entity.OutLookWeather;
import com.zt.rainbowweather.presenter.ScrollLinearLayoutManager;
import com.zt.rainbowweather.ui.activity.AdviseMoreDetailActivity;
import com.zt.rainbowweather.ui.activity.WeatherDetailsActivity;
import com.zt.rainbowweather.utils.WeatherUtils;
import com.zt.weather.R;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 列表模式
 * */
public class ListFragment extends BaseFragment {

    @BindView(R.id.list_recycler_weather)
    RecyclerView listRecycler;
    Unbinder unbinder;
    private List<OutLookWeather> lookWeathers;
    private City city;
    private BaseAdapter baseAdapter;

    public ListFragment() {

    }

    @SuppressLint("ValidFragment")
    public ListFragment(List<OutLookWeather> lookWeathers, City city) {
        this.lookWeathers = lookWeathers;
        this.city = city;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_list;
    }

    @Override
    protected void initData(View view) {
        ScrollLinearLayoutManager setScrollEnable = new ScrollLinearLayoutManager(getActivity());
        setScrollEnable.setScrollEnable(false);
        listRecycler.setLayoutManager(setScrollEnable);
        baseAdapter = new BaseAdapter<>(R.layout.list_item_future_days_weather, lookWeathers, (viewHolder, item) -> {
            viewHolder.setText(R.id.list_tv_week,item.getWeek())
                    .setText(R.id.list_tv_weather_day,item.weatherDay.weather.equals(item.weatherNight.weather)?item.weatherNight.weather : item.weatherDay.weather+"转"+item.weatherNight.weather)
                    .setText(R.id.list_tv_wind,item.highTemperature+"～"+item.lowTemperature+"°");
            TextView tvAirQuality = viewHolder.getView(R.id.list_tv_air_quality);

            tvAirQuality.setText(item.airQuality);
            if (item.getAirQuality().equals("优")) {
                tvAirQuality.setBackground(getResources().getDrawable(R.drawable.search30_1));
            } else if (item.getAirQuality().equals("良")) {
                tvAirQuality.setBackground(getResources().getDrawable(R.drawable.search30_2));
            } else if (item.getAirQuality().equals("轻度污染")) {
                tvAirQuality.setBackground(getResources().getDrawable(R.drawable.search30_3));
            } else if (item.getAirQuality().equals("中度污染")) {
                tvAirQuality.setBackground(getResources().getDrawable(R.drawable.search30_4));
            } else if (item.getAirQuality().equals("重度污染")) {
                tvAirQuality.setBackground(getResources().getDrawable(R.drawable.search30_5));
            } else if (item.getAirQuality().equals("严重污染")) {
                tvAirQuality.setBackground(getResources().getDrawable(R.drawable.search30_6));
            } else {
                tvAirQuality.setBackground(getResources().getDrawable(R.drawable.search30));
            }


            ImageView imageView = viewHolder.getView(R.id.list_iv_weather_img_day);
            //加载图片
            imageView.setImageResource(WeatherUtils.getWeatherStatus(item.weatherDay.iconRes).iconRes);
        });
        baseAdapter.setOnItemClickListener((adapter, view1, position) -> {
            Intent intent = new Intent(getActivity(), WeatherDetailsActivity.class);
            intent.putExtra("datas", (Serializable) lookWeathers);
            intent.putExtra("Size",position+"");
            intent.putExtra("City",city.name);
            getActivity().startActivity(intent);
            MobclickAgent.onEvent(getActivity(), "home_Weather_Details", "home_Weather_Details");
        });


//        listRecycler.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getActivity()), 1));
        listRecycler.setAdapter(baseAdapter);
        listRecycler.setHasFixedSize(true);
        listRecycler.setNestedScrollingEnabled(false);
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
}
