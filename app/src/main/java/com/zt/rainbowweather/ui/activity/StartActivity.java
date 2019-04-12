package com.zt.rainbowweather.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.chenguang.weather.R;

import com.umeng.message.PushAgent;
import com.xy.xylibrary.base.BaseActivity;
import com.xy.xylibrary.utils.DisplayUtil;
import com.xy.xylibrary.utils.SaveShare;
import com.zt.rainbowweather.BasicApplication;
import com.zt.rainbowweather.entity.City;
import com.zt.rainbowweather.utils.ConstUtils;
import com.zt.rainbowweather.utils.PermissionConstants;
import com.zt.rainbowweather.utils.PermissionUtils;
import com.zt.rainbowweather.utils.RxCountDown;
import com.zt.rainbowweather.view.ShapeTextView;
import com.zt.xuanyin.Interface.AdProtogenesisListener;
import com.zt.xuanyin.controller.Ad;
import com.zt.xuanyin.controller.NativeAd;
import com.zt.xuanyin.entity.model.Native;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BuildConfig;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;


public class StartActivity extends BaseActivity implements AMapLocationListener {

    private ImageView ivImage;
    private TextView tvVersionName;
    private ShapeTextView tvSkip;
    private RelativeLayout container;
     //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private Subscription subscription;
    private NativeAd nativelogic;
    private float ClickX;
    private float ClickY;
    private boolean ISINTENT = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        initView();
        tvVersionName.setText(BuildConfig.VERSION_NAME);
        startCountDown();
        tvSkip.setOnClickListener(v -> skip());
    }

    @Override
    protected Activity getContext() {
        return StartActivity.this;
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_start;
    }

    private void loadAd() {//
        try {
            // 获得开屏对象
            nativelogic = Ad.getAd().NativeAD(this, "e81ab28d-3a51-4f52-9fc3-7225dc8231ad", "9432a40d-9fa7-4d1a-b760-58a32edc9465","879A2AFF20E64EC8AEB3973B77985004", new AdProtogenesisListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public void onADReady(final Native url) {
                    findViewById(R.id.tv_ad_flag).setVisibility(View.VISIBLE);
                    Glide.with(StartActivity.this).load(url.src).into(ivImage);
                    nativelogic.AdShow(container);
                    ivImage.setOnTouchListener(new View.OnTouchListener() {

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:

                                    ClickX = event.getRawX();
                                    ClickY = event.getRawY();
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    break;
                                case MotionEvent.ACTION_UP:
                                    if(Math.abs(event.getRawX() - ClickX) < 20 && Math.abs(event.getRawY() - ClickY) < 20){

                                        skip();
                                        if (subscription != null) {
                                            subscription.unsubscribe();
                                        }
                                        // 点击开屏广告处理
                                        nativelogic.OnClick(container);

                                    }
                                    break;
                            }
                            return true;
                        }
                    });
                }

                @Override
                public void onAdFailedToLoad(String error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("CheckResult")
    private void startCountDown() {
        try {
            subscription = RxCountDown.countdown(5)
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {

                        }
                    })
                    .subscribe(new Subscriber<Integer>() {
                        @Override
                        public void onCompleted() {
                            skip();
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(Integer integer) {
                            tvSkip.setText(getString(R.string.format_skip, integer));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void skip() {
     setData();
     intentActivity(MainActivity.class);
     finish();
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, 0);
    }

    @Override
    protected void loadViewLayout() {
        loadAd();
    }

    @Override
    protected void bindViews() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
            locate();
            requestRequestPermission();
    }

    @Override
    protected void setListener() {

    }

    private void initView() {
        try {
            ivImage = findViewById(R.id.iv_image);
            tvVersionName = findViewById(R.id.tv_version_name);
            tvSkip = findViewById(R.id.tv_skip);
            container = findViewById(R.id.container);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DisplayUtil.hasVirtualNavigationBar(this)) {
                findViewById(R.id.content_view).setPadding(0, 0, 0, DisplayUtil.getNavigationBarHeight(this));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void locate() {
        try {
            mlocationClient = new AMapLocationClient(StartActivity.this);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置返回地址信息，默认为true
            mLocationOption.setNeedAddress(true);
            //设置定位监听
            mlocationClient.setLocationListener(StartActivity.this);
            //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        //设置定位间隔,单位毫秒,默认为2000ms
//        mLocationOption.setInterval(2000);
            //获取一次定位结果：
            //该方法默认为false。
            mLocationOption.setOnceLocation(true);
            //获取最近3s内精度最高的一次定位结果：
//       设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
//        mLocationOption.setOnceLocationLatest(true);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            //启动定位
            mlocationClient.startLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    private void requestRequestPermission() {

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        try {
            if(!TextUtils.isEmpty(aMapLocation.getCity())){
                City locatedCity = new City();
                if (aMapLocation != null && aMapLocation.getLatitude() != 0 && aMapLocation
                        .getLongitude() != 0) {
                    locatedCity.longitude = aMapLocation.getLongitude();
                    locatedCity.latitude = aMapLocation.getLatitude();
                    locatedCity.name = aMapLocation.getCity();
                    locatedCity.affiliation = aMapLocation.getDistrict() + "," + aMapLocation.getStreet();
                } else {
                    locatedCity.affiliation = locatedCity.name = ConstUtils.LOCATE_FAILED;
                }
                locatedCity.isLocate = "1";
                locatedCity.isChecked = "1";
                assert aMapLocation != null;

                List<City> cities = LitePal.where("name=?", aMapLocation.getCity()).find(City.class);
                if(cities == null || cities.size() == 0){
                    locatedCity.save();
                }
                List<City> nameCity = LitePal.where("id=?", "0").find(City.class);
                ContentValues values=new ContentValues();
                values.put("isChecked","0");
                LitePal.updateAll(City.class, values, "isChecked = ?","1");
                if(cities == null || cities.size() == 0){
                    ContentValues values2 = new ContentValues();
                    values2.put("longitude", aMapLocation.getLongitude());
                    values2.put("latitude", aMapLocation.getLatitude());
                    values2.put("affiliation", aMapLocation.getDistrict() + "," + aMapLocation.getStreet());
                    nameCity.get(0).isChecked = "1";
                    nameCity.get(0).longitude = aMapLocation.getLongitude();
                    nameCity.get(0).latitude = aMapLocation.getLatitude();
                    nameCity.get(0).name = aMapLocation.getCity();
                    nameCity.get(0).affiliation = aMapLocation.getDistrict() + "," + aMapLocation.getStreet();
                    nameCity.get(0).isLocate = "1";
                    nameCity.get(0).save();
                }
                BasicApplication.setLocatedCity(locatedCity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        mlocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mlocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }

    @Override
    public void granted() {
        Log.e("TAG", "granted:启动定位 " );
            //启动定位
            mlocationClient.startLocation();
    }

    @Override
    public void denied(List<String> deniedList) {
        setData();
    }

    private void setData(){
        if(LitePal.findAll(City.class) == null || LitePal.findAll(City.class).size() == 0){
            try {
                City locatedCity = new City();
                locatedCity.name = "上海市";
                locatedCity.isLocate = "1";
                locatedCity.isChecked = "1";
                List<City> cities = LitePal.where("name=?", "上海市").find(City.class);
                if(cities == null || cities.size() == 0){
                    locatedCity.save();
                }
                List<City> nameCity = LitePal.where("name=?", locatedCity.name).find(City.class);
                ContentValues values=new ContentValues();
                values.put("isChecked","0");
                LitePal.updateAll(City.class, values, "isChecked = ?","1");
                if(cities == null || cities.size() == 0){
                    nameCity.get(0).isChecked = "1";
                    nameCity.get(0).save();
                }
                BasicApplication.setLocatedCity(locatedCity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
