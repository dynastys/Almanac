package com.zt.rainbowweather.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.umeng.analytics.MobclickAgent;
import com.zt.rainbowweather.entity.City;
import com.zt.rainbowweather.entity.OutLookWeather;
import com.zt.weather.R;
import com.zt.rainbowweather.ui.activity.WeatherDetailsActivity;
import com.zt.rainbowweather.utils.WeatherUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tb on 2017/5/13.
 * 未来若干天天气滑动控件
 */

public class ScrollFutureDaysWeatherView extends ViewGroup {
    private static final String TAG = "ScrollFutureDaysWeatherView";
    /**
     * 未来若干天天气View的集合（每个item都是一样的）
     */
    private List<View> contents = new ArrayList<>();
    /**
     * 未来若干天天气温度的图表
     */
    private FutureDaysChart sevenDaysChart;
    /**
     * 未来具体的天数（包含昨天一个）
     */
    public static final int days = 7;
    /**
     * 每个item的宽度
     */
    private int futureDayItemWidth;
    /**
     * 温度图表的高度
     */
    private int futureDayChartHeight;
    /**
     * 未来若干天天气控件总宽度（viewgroup的宽度）
     */
    private int futureDayTotalWidth;
    /**
     * 具体的每个item的宽度（单位：dp）
     */
    public static final int ITEM_WIDTH = 80;

    private Context context;


    public ScrollFutureDaysWeatherView(Context context) {
        this(context, null);
        this.context = context;
    }

    public ScrollFutureDaysWeatherView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public ScrollFutureDaysWeatherView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ScrollFutureDaysWeatherView(Context context, AttributeSet attrs, int defStyleAttr, int
            defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init(context);
    }

    private void init(Context context) {
        futureDayItemWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                ITEM_WIDTH, context.getResources().getDisplayMetrics());
        futureDayChartHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                FutureDaysChart.CHART_HEIGHT, context.getResources().getDisplayMetrics());
        futureDayTotalWidth = futureDayItemWidth * days;
        for (int i = 0; i < days; i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_future_days_weather,
                    null, false);
            view.findViewById(R.id.view).getLayoutParams().height = futureDayChartHeight;
            contents.add(view);
            addView(view, new LayoutParams(futureDayItemWidth, LayoutParams.WRAP_CONTENT));

        }
        sevenDaysChart = new FutureDaysChart(context);
        sevenDaysChart.setCubic(true);
        addView(sevenDaysChart, new LayoutParams(futureDayTotalWidth, LayoutParams.WRAP_CONTENT));
    }

    public List<View> getAllViews() {
        return contents;
    }

    public FutureDaysChart getSevenDaysChart() {
        return sevenDaysChart;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int totalHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() != View.GONE) {
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
                totalHeight += childView.getMeasuredHeight();
            }
        }
        //为ViewGroup设置宽高
        setMeasuredDimension(futureDayTotalWidth, totalHeight);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        int left = 0;
        for (int j = 0; j < getChildCount() - 1; j++) {
            View child = getChildAt(j);
            if (child.getVisibility() != View.GONE) {
                child.layout(left, 0, left + child.getMeasuredWidth(), child.getMeasuredHeight());
                left += futureDayItemWidth;
            }
        }
        View emptyView = contents.get(0).findViewById(R.id.view);
        int top = emptyView.getTop();
        View last = getChildAt(getChildCount() - 1);
        last.layout(0, top, getMeasuredWidth(), top + futureDayChartHeight);
    }

    private FutureDaysChart futureDaysChart;

    @SuppressLint("ResourceAsColor")
    public void setData(List<OutLookWeather> datas, City city) {
        try {

            futureDaysChart = this.getSevenDaysChart();
            if(futureDaysChart != null && datas != null){
                futureDaysChart.setDatas(datas);
            }
            List<View> viewList = this.getAllViews();
            for (int i = 0; i < viewList.size(); i++) {
                View view = viewList.get(i);
                TextView tvWeek = view.findViewById(R.id.tv_week);
                TextView tvDate = view.findViewById(R.id.tv_date);
                TextView tvWeatherDay = view.findViewById(R.id.tv_weather_day);
                ImageView ivWeatherDayImg = view.findViewById(R.id.iv_weather_img_day);
                TextView tvWeatherNight = view.findViewById(R.id.tv_weather_night);
                ImageView ivWeatherNightImg = view.findViewById(R.id.iv_weather_img_night);
                TextView tvWind = view.findViewById(R.id.tv_wind);
                TextView tvWindLevel = view.findViewById(R.id.tv_wind_level);
                TextView tvAirQuality = view.findViewById(R.id.tv_air_quality);
                try {
                    tvWeek.setText(datas.get(i).getWeek());
                    tvDate.setText(datas.get(i).getDate());
                    tvWeatherDay.setText(datas.get(i).weatherDay.weather);
                    tvWeatherNight.setText(datas.get(i).weatherNight.weather);
                    tvWind.setText(datas.get(i).getWind());
                    tvWindLevel.setText(datas.get(i).getWindLevel());
                    tvAirQuality.setText(datas.get(i).getAirQuality());

                    if (datas.get(i).getAirQuality().equals("优")) {
                        tvAirQuality.setBackground(getResources().getDrawable(R.drawable.search30_1));
                    } else if (datas.get(i).getAirQuality().equals("良")) {
                        tvAirQuality.setBackground(getResources().getDrawable(R.drawable.search30_2));
                    } else if (datas.get(i).getAirQuality().equals("轻度污染")) {
                        tvAirQuality.setBackground(getResources().getDrawable(R.drawable.search30_3));
                    } else if (datas.get(i).getAirQuality().equals("中度污染")) {
                        tvAirQuality.setBackground(getResources().getDrawable(R.drawable.search30_4));
                    } else if (datas.get(i).getAirQuality().equals("重度污染")) {
                        tvAirQuality.setBackground(getResources().getDrawable(R.drawable.search30_5));
                    } else if (datas.get(i).getAirQuality().equals("严重污染")) {
                        tvAirQuality.setBackground(getResources().getDrawable(R.drawable.search30_6));
                    } else {
                        tvAirQuality.setBackground(getResources().getDrawable(R.drawable.search30));
                    }

                    if (i == 0) {
                        tvWeek.setTextColor(0xff469DF9);
                        view.setBackgroundColor(0x22808080);
    //                    tvWeek.setTextColor(Color.GRAY);
                    } else {
                        tvWeek.setTextColor(Color.WHITE);
                    }
                    //加载图片
                    ivWeatherDayImg.setImageResource(WeatherUtils.getWeatherStatus(datas.get(i).weatherDay.iconRes).iconRes);
                    if(datas.get(i).weatherNight.iconRes == 100){
                        datas.get(i).weatherNight.iconRes = 1000;
                    }
                    if(datas.get(i).weatherNight.iconRes == 101){
                        datas.get(i).weatherNight.iconRes = 1001;
                    }
                    if(datas.get(i).weatherNight.iconRes == 102){
                        datas.get(i).weatherNight.iconRes = 1002;
                    }
                    if(datas.get(i).weatherNight.iconRes == 102){
                        datas.get(i).weatherNight.iconRes = 1002;
                    }
                    ivWeatherNightImg.setImageResource(WeatherUtils.getWeatherStatus(datas.get(i).weatherNight.iconRes).iconRes);
                    LinearLayout weather_lin = view.findViewById(R.id.weather_day_lin);
                    int finalI = i;
                    weather_lin.setOnClickListener(v -> {
                        Intent intent = new Intent(context, WeatherDetailsActivity.class);
                        intent.putExtra("datas", (Serializable) datas);
                        intent.putExtra("Size",finalI+"");
                        intent.putExtra("City",city.name);
                        context.startActivity(intent);
                        MobclickAgent.onEvent(context, "home_Weather_Details", "home_Weather_Details");
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
