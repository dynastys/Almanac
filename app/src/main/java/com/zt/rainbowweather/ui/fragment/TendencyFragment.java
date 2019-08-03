package com.zt.rainbowweather.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xy.xylibrary.base.BaseFragment;
import com.zt.rainbowweather.entity.City;
import com.zt.rainbowweather.entity.OutLookWeather;
import com.zt.rainbowweather.view.ScrollFutureDaysWeatherView;
import com.zt.weather.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TendencyFragment extends BaseFragment {

    @BindView(R.id.sfdwv)
    ScrollFutureDaysWeatherView sfdwv;
    Unbinder unbinder;

    private List<OutLookWeather> lookWeathers;
    private City city;

    public TendencyFragment(){

    }
    @SuppressLint("ValidFragment")
    public TendencyFragment(List<OutLookWeather> lookWeathers,City city){
        this.lookWeathers = lookWeathers;
        this.city = city;

    }
    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_tendency;
    }

    @Override
    protected void initData(View view) {
        try {
            if(lookWeathers != null){
                sfdwv.setData(lookWeathers, city);
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
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        Log.e("city", "initData: " );
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
