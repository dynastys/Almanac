package com.zt.rainbowweather.ui.activity;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zt.weather.R;
import com.xy.xylibrary.base.BaseActivity;
import com.xy.xylibrary.utils.SaveShare;
import com.xy.xylibrary.utils.Utils;
import com.xy.xylibrary.view.ColumnHorizontalScrollView;
import com.zt.rainbowweather.entity.OutLookWeather;
import com.zt.rainbowweather.presenter.news.NativeNewsLogic;
import com.zt.rainbowweather.utils.Util;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zw
 * @time 2019-3-8
 * 天气详情
 * */
public class WeatherDetailsActivity extends BaseActivity {

    @BindView(R.id.wether_bg)
    TextView wetherBg;
    @BindView(R.id.list_bar)
    TextView listBar;
    @BindView(R.id.finish_file_head)
    ImageView finishFileHead;
    @BindView(R.id.file_head_title)
    TextView fileHeadTitle;
    @BindView(R.id.mRadioGroup_content)
    LinearLayout mRadioGroupContent;
    @BindView(R.id.weather_day)
    ColumnHorizontalScrollView weatherDay;
    @BindView(R.id.viewpager_weather_day)
    ViewPager viewpagerWeatherDay;
    @BindView(R.id.weather_details_lin)
    RelativeLayout weatherDetailsLin;

    private List<OutLookWeather> outLookWeathers = new ArrayList<>();
    private String Size;
    private String City = "上海市";

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("WeatherDetailsActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this); //统计时长
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("WeatherDetailsActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)，必须保证 onPageEnd 在 onPause 之前调用，因为SDK会在 onPause 中保存onPageEnd统计到的页面数据。
        MobclickAgent.onPause(this);
    }

    @Override
    protected Activity getContext() {
        return WeatherDetailsActivity.this;
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_weather_details;
    }

    @Override
    protected void loadViewLayout() {
        try {
            MobclickAgent.onEvent(WeatherDetailsActivity.this,"Weather_Details");
            outLookWeathers = (List<OutLookWeather>) getIntent().getSerializableExtra
                    ("datas");
            Size = getIntent().getStringExtra("Size");
            City = getIntent().getStringExtra("City");
            ViewGroup.LayoutParams layoutParams = listBar.getLayoutParams();
            layoutParams.height = Utils.getStatusBarHeight(WeatherDetailsActivity.this);
            listBar.setLayoutParams(layoutParams);
            finishFileHead.setVisibility(View.VISIBLE);
            fileHeadTitle.setText(City);
            Drawable resource = SaveShare.getDrawable(WeatherDetailsActivity.this, "icon");
            if(resource != null){
                weatherDetailsLin.setBackground(new BitmapDrawable(Util.rsBlur(WeatherDetailsActivity.this, Util.drawable2Bitmap(resource), 20)));
            }else{
                weatherDetailsLin.setBackground(new BitmapDrawable(Util.rsBlur(WeatherDetailsActivity.this, Util.drawable2Bitmap(weatherDetailsLin.getBackground()), 20)));
            }
            NativeNewsLogic.getNativeNewsLogic().WeatherDetails(WeatherDetailsActivity.this, outLookWeathers, mRadioGroupContent, weatherDay, viewpagerWeatherDay);
            if (!TextUtils.isEmpty(Size)) {
                viewpagerWeatherDay.setCurrentItem(Integer.parseInt(Size));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void bindViews() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    protected void setListener() {

    }

    @Override
    public void granted() {

    }

    @Override
    public void denied(List<String> deniedList) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         ButterKnife.bind(this);
    }

    @OnClick(R.id.finish_file_head)
    public void onClick() {
        finish();
    }
}
