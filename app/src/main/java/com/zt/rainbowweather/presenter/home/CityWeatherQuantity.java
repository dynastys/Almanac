package com.zt.rainbowweather.presenter.home;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.chenguang.weather.R;
import com.google.gson.Gson;
import com.xy.xylibrary.base.BaseFragment;
import com.zt.rainbowweather.BasicApplication;
import com.zt.rainbowweather.entity.AddressBean;
import com.zt.rainbowweather.entity.City;
import com.zt.rainbowweather.entity.CityEvent;
import com.zt.rainbowweather.entity.MoveCityEvent;
import com.zt.rainbowweather.entity.city.Event;
import com.zt.rainbowweather.ui.adapter.BaseFragmentPagerAdapter;
import com.zt.rainbowweather.ui.fragment.HomeFragment;
import com.zt.rainbowweather.ui.fragment.WeatherFragment;
import com.zt.rainbowweather.utils.ConstUtils;
import com.zt.rainbowweather.utils.SPUtils;
import com.zt.rainbowweather.utils.SizeUtils;
import com.zt.rainbowweather.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CityWeatherQuantity {

    private Activity activity;
    private List<BaseFragment> fragments = new ArrayList<>();
    private BaseFragmentPagerAdapter mAdapter;
    private HomeFragment homeFragment;
    private ViewPager viewPager;
    private RadioGroup rbMain;
    private List<City> mCities;
    private City locatedCity;
    private RadioButton rb;

    public CityWeatherQuantity(Activity activity, HomeFragment homeFragment, ViewPager viewPager, RadioGroup rbMain) {
        this.activity = activity;
        this.homeFragment = homeFragment;
        this.viewPager = viewPager;
        this.rbMain = rbMain;
    }

    public List<AddressBean> getAllAddresses() {
        List<AddressBean> addressBeans = new ArrayList<>();
        for (BaseFragment fragment : mAdapter.getFragments()) {
            addressBeans.add(((WeatherFragment) fragment).getAddressBean());
        }
        return addressBeans;
    }
    int from,to;
    public void setMoveFragmentPosition(MoveCityEvent event){
        new Thread(() -> {
             from = isAlreadyExists(event.sourceCity);
             to = isAlreadyExists(event.desCity);
        }).start();

        mAdapter.moveFragmentPosition(from, to);
    }


    public void PageSize(OnPageChangeListener onPageChangeListener) {
        mCities = LitePal.findAll(City.class);
        rbMain.removeAllViews();
        viewPager.removeAllViews();
        for (int i = 0; i < mCities.size(); i++) {
            locatedCity = mCities.get(i);
            rb = new RadioButton(activity);
            rb.setButtonDrawable(null);
            if (i == 0) {//首个
                rb.setCompoundDrawablesWithIntrinsicBounds(activity.getResources().getDrawable(R.drawable
                        .selector_main_first), null, null, null);
            } else {
                rb.setCompoundDrawablesWithIntrinsicBounds(activity.getResources().getDrawable(R.drawable
                        .selector_main_dot), null, null, null);
            }

            RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(ViewGroup
                    .LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(SizeUtils.dp2px(8), 0, 0, 0);
            rbMain.addView(rb, lp);
            if (locatedCity != null) {
                fragments.add(WeatherFragment.newInstance(locatedCity,i+"", homeFragment));
            }
        }

        mAdapter = new BaseFragmentPagerAdapter(homeFragment.getChildFragmentManager(), fragments);
        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {
                ((RadioButton) rbMain.getChildAt(position)).setChecked(true);
                onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                onPageChangeListener.onPageSelected(position);
//                ((WeatherFragment) (mAdapter.getFragments().get(position))).setStatusBarDynamicly();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                onPageChangeListener.onPageScrollStateChanged(state);
            }
        });

        viewPager.setOffscreenPageLimit(mCities.size());
        for (int i = 0; i < mCities.size(); i++) {
            if (mCities.get(i).isChecked.equals("1")) {
                viewPager.setCurrentItem(i);
            }
        }
    }
    public int isAlreadyExists(City city) {
        try {
            List<AddressBean> allAddresses = getAllAddresses();
            if (allAddresses.size() == 0) {
                return -1;
            }
            for (int i = 0; i < allAddresses.size(); i++) {
                Log.e("isAlreadyExists", "isAlreadyExists: "+allAddresses.get(i).city.affiliation +"aaa"+ city.affiliation);
                if (!TextUtils.isEmpty(allAddresses.get(i).city.affiliation) && allAddresses.get(i).city.affiliation.equals(city.affiliation)) {
                    return i;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    public List<City> getAllCities() {
        List<City> cities = new ArrayList<>();
        for (BaseFragment fragment : mAdapter.getFragments()) {
            cities.add(((WeatherFragment) fragment).getAddressBean().city);
        }
        return cities;
    }
    public void saveCityToSp() {
         LitePal.saveAll(getAllCities());
        SPUtils.getInstance(ConstUtils.SP_FILE_NAME).put("addresses", new Gson().toJson
                (getAllCities()));

    }
    public List<City> setFragments(){
         SPUtils.getInstance(ConstUtils.SP_FILE_NAME).put("addresses", new Gson().toJson
                (getAllCities()));
        viewPager.setCurrentItem(0);
        return getAllCities();
    }
     public void updateWeatherFragment(Event event) {
        if (event.isDelete){//删除一个城市
            delete(event.city);
            saveCityToSp();
        } else {
            update(event.city);
        }
    }

    private void delete(City city) {
        int index = isAlreadyExists(city);
        if (index != -1) {
            if (viewPager.getCurrentItem() == index) {
                viewPager.setCurrentItem(0);
            }
            rbMain.removeViewAt(index);
            mAdapter.removeFragment(index);
        }
        saveCityToSp();
    }
    private void update(City city) {
        int index = isAlreadyExists(city);
        //不存在
        if (index == -1) {
            if (mAdapter.getFragments().size() > 9) {
                ToastUtils.showShort("最多添加10个城市");
                viewPager.setCurrentItem(0);
                return;
            }
            rb = new RadioButton(activity);
            rb.setButtonDrawable(null);
            if (mAdapter.getFragments().size() == 0) {//首个
                rb.setCompoundDrawablesWithIntrinsicBounds(activity.getResources().getDrawable(R.drawable
                        .selector_main_first), null, null, null);
            } else {
                rb.setCompoundDrawablesWithIntrinsicBounds(activity.getResources().getDrawable(R.drawable
                        .selector_main_dot), null, null, null);
            }
            RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(ViewGroup
                    .LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(SizeUtils.dp2px(8), 0, 0, 0);
            rbMain.addView(rb, lp);
//            fragments.add(WeatherFragment.newInstance(city));
//            mAdapter.notifyDataSetChanged();
            mAdapter.addFragment(WeatherFragment.newInstance(city,mAdapter.getFragments().size()+"",homeFragment));
            viewPager.setCurrentItem(mAdapter.getFragments().size() - 1, false);
        } else {
            ((WeatherFragment) (mAdapter.getFragments().get(index))).updateLocateWeather(city);
            viewPager.setCurrentItem(index, false);
        }
        saveCityToSp();
    }
}
