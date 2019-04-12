package com.zt.rainbowweather.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chenguang.weather.R;
import com.umeng.message.PushAgent;
import com.xy.xylibrary.base.BaseActivity;
import com.xy.xylibrary.utils.Utils;
import com.zt.rainbowweather.BasicApplication;
import com.zt.rainbowweather.api.RequestSyntony;
import com.zt.rainbowweather.entity.AddressBean;
import com.zt.rainbowweather.entity.City;
import com.zt.rainbowweather.entity.CityEvent;
import com.zt.rainbowweather.entity.city.HotCity;
import com.zt.rainbowweather.entity.weather.ConventionWeather;
import com.zt.rainbowweather.presenter.request.WeatherRequest;
import com.zt.rainbowweather.ui.adapter.HotCityAdapter;
import com.zt.rainbowweather.ui.adapter.SearchCityAdapter;
import com.zt.rainbowweather.utils.ActivityUtils;
import com.zt.rainbowweather.utils.SizeUtils;
import com.zt.rainbowweather.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchCityActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.rv_hot)
    RecyclerView rvHot;
    @BindView(R.id.ll_hot)
    LinearLayout llHot;
    @BindView(R.id.rv_search)
    RecyclerView rvSearch;
    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;
    @BindView(R.id.tv_text)
    TextView tvText;
    @BindView(R.id.list_bar)
    TextView listBar;
    private HotCityAdapter mHotCityAdapter;
    private SearchCityAdapter mSearchCityAdapter;
    private City locatedCity;
    private List<AddressBean> mAddresses;

    public static void startActivity(Context context, List<AddressBean> addressBeans) {
        Intent intent = new Intent(context, SearchCityActivity.class);
        intent.putExtra("address", (Serializable) addressBeans);
        context.startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        dismissLoadingDialog();
    }

    @SuppressLint("CheckResult")
    private void searchKey(String key) {
        WeatherRequest.getWeatherRequest().getCitySearchData(SearchCityActivity.this,key,new RequestSyntony<HotCity>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HotCity hotCity) {
                List<City> cities = new ArrayList<>();
                for (int i = 0; i < hotCity.getHeWeather6().get(0).getBasic().size(); i++) {
                    City city = new City();
                    city.name = hotCity.getHeWeather6().get(0).getBasic().get(i).getLocation();
                    city.affiliation = hotCity.getHeWeather6().get(0).getBasic().get(i).getParent_city();
                    city.latitude = Double.parseDouble(hotCity.getHeWeather6().get(0).getBasic().get(i).getLat());
                    city.locationKey = hotCity.getHeWeather6().get(0).getBasic().get(i).getCid();
                    city.longitude = Double.parseDouble(hotCity.getHeWeather6().get(0).getBasic().get(i).getLon());
                    city.status = 0;
                    city.isLocate = "0";
                    city.isChecked = "0";
                    cities.add(city);
                }
                showSearchData(cities);
            }
        });
    }

    private void showSearchData(List<City> list) {
        if (list == null) {//加载数据失败
            if (mSearchCityAdapter.getData().size() < 1) {//数据加载失败
                mSearchCityAdapter.setEmptyView(getTextView("网络错误."));
                mSearchCityAdapter.notifyDataSetChanged();
            } else {//加载更多失败
                mSearchCityAdapter.loadMoreFail();
            }
            return;
        }
        //加载成功，但数据为空
        if (list.size() == 0 && mSearchCityAdapter.getData().size() == 0) {//数据真的为空
            mSearchCityAdapter.setEmptyView(getTextView("无匹配城市"));
            mSearchCityAdapter.notifyDataSetChanged();
            return;
        }
        //加载数据成功
        if (mSearchCityAdapter.getData().size() == 0) {//第一次加载数据
            mSearchCityAdapter.setNewData(list);
            mSearchCityAdapter.disableLoadMoreIfNotFullPage();
//            Log.e("onError", "onError: ");
        } else {
            if (list.size() == 0) {//加载更多成功但数据为空
                mSearchCityAdapter.loadMoreEnd();
            } else {//加载更多成功
                mSearchCityAdapter.addData(list);
                mSearchCityAdapter.loadMoreComplete();
            }
        }
    }

    public TextView getTextView(String text) {
        TextView tv = new TextView(SearchCityActivity.this);
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.topMargin = SizeUtils.dp2px(40);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setLayoutParams(lp);
        tv.setText(text);
        return tv;
    }


    private void addCityWeather(City city) {
        showLoadingDialog("");
        Log.e("TAG", "addCityWeather: "+LitePal.findAll(City.class).size());
        if(LitePal.findAll(City.class).size() <= 9){
            List<City> cities = LitePal.where("name=?", city.name).find(City.class);
            ContentValues values=new ContentValues();
            values.put("isChecked","0");
            LitePal.updateAll(City.class, values, "isChecked = ?","1");
            if(cities == null || cities.size() == 0){
                city.isChecked = "1";
                city.save();
            }
            EventBus.getDefault().post(new CityEvent(city));
            ActivityUtils.finishToActivity(MainActivity.class, false);
        }else{
            ToastUtils.showLong("只能添加9个城市哦！");
        }
    }

    @SuppressLint("CheckResult")
    private void getHotCityData() {
        WeatherRequest.getWeatherRequest().getHotCityData(SearchCityActivity.this,new RequestSyntony<HotCity>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HotCity hotCity) {
                List<City> cities = new ArrayList<>();
                for (int i = 0; i < hotCity.getHeWeather6().get(0).getBasic().size(); i++) {
                    City city = new City();
                    city.name = hotCity.getHeWeather6().get(0).getBasic().get(i).getLocation();
                    city.affiliation = hotCity.getHeWeather6().get(0).getBasic().get(i).getParent_city();
                    city.latitude = Double.parseDouble(hotCity.getHeWeather6().get(0).getBasic().get(i).getLat());
                    city.locationKey = hotCity.getHeWeather6().get(0).getBasic().get(i).getCid();
                    city.longitude = Double.parseDouble(hotCity.getHeWeather6().get(0).getBasic().get(i).getLon());
                    city.status = 0;
                    city.isLocate = "0";
                    city.isChecked = "0";
                    cities.add(city);
                }
                showHotCityData(cities);
            }
        });
    }

    private void showHotCityData(List<City> cities) {
        if (cities != null && cities.size() > 0) {
            rvHot.setLayoutManager(new StaggeredGridLayoutManager(cities.size() / 5 + 1,
                    StaggeredGridLayoutManager.HORIZONTAL) {
                @Override
                public boolean canScrollHorizontally() {
                    return false;
                }
            });
        } else {
            cities = new ArrayList<>();
        }
        if (locatedCity != null) {
            cities.add(0, locatedCity);
            locatedCity.isChecked = "0";
        }
        for (City city : cities) {
            for (AddressBean mAddress : mAddresses) {
                if (city.affiliation.equals(mAddress.city.affiliation)) {
                    city.isChecked = "1";
                }
            }
        }

        mHotCityAdapter.setNewData(cities);
    }

    @OnClick({R.id.iv_back, R.id.iv_clear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_clear:
                etSearch.setText("");
                break;
        }
    }

    @Override
    protected Activity getContext() {
        return SearchCityActivity.this;
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_search_city;
    }

    @Override
    protected void loadViewLayout() {
        ViewGroup.LayoutParams layoutParams = listBar.getLayoutParams();
        layoutParams.height = Utils.getStatusBarHeight(SearchCityActivity.this);
        listBar.setLayoutParams(layoutParams);
        listBar.setBackgroundColor(getResources().getColor(R.color.blue_light));

    }

    @Override
    protected void bindViews() {
        rvHot.setLayoutManager(new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager
                .HORIZONTAL) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        });
        rvHot.setAdapter(mHotCityAdapter = new HotCityAdapter(null));
        rvSearch.setLayoutManager(new LinearLayoutManager(SearchCityActivity.this));
        rvSearch.addItemDecoration(new DividerItemDecoration(SearchCityActivity.this, DividerItemDecoration
                .VERTICAL));
        mSearchCityAdapter = new SearchCityAdapter(null);
        mSearchCityAdapter.bindToRecyclerView(rvSearch);
//        rvSearch.setAdapter(mSearchCityAdapter);
        mHotCityAdapter.setOnItemClickListener((adapter, view, position) -> {
            City city = (City) adapter.getData().get(position);
            addCityWeather(city);
        });
        mSearchCityAdapter.setOnItemClickListener((adapter, view, position) -> {
            City city = (City) adapter.getData().get(position);
            addCityWeather(city);
        });
        getHotCityData();


    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
//        BarUtils.setStatusBarColor(SearchCityActivity.this, getResources().getColor(R.color.bar_color_1));
//        BarUtils.addMarginTopEqualStatusBarHeight(rlRoot);
        locatedCity = BasicApplication.getLocatedCity();
        mAddresses = (List<AddressBean>) getIntent().getSerializableExtra
                ("address");
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String key = s.toString();
                if (!TextUtils.isEmpty(key)) {
                    ivClear.setVisibility(View.VISIBLE);
                    tvText.setVisibility(View.INVISIBLE);
                    rvHot.setVisibility(View.INVISIBLE);
                    rvSearch.setVisibility(View.VISIBLE);
                    searchKey(key);
                } else {
                    ivClear.setVisibility(View.GONE);
                    mSearchCityAdapter.getData().clear();
                    mSearchCityAdapter.notifyDataSetChanged();
                    tvText.setVisibility(View.VISIBLE);
                    ivClear.setVisibility(View.GONE);
                    rvHot.setVisibility(View.VISIBLE);
                    rvSearch.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        PushAgent.getInstance(this).onAppStart();
    }

    @Override
    public void granted() {

    }

    @Override
    public void denied(List<String> deniedList) {

    }
}
