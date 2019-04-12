package com.zt.rainbowweather.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
 import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.xy.xylibrary.Interface.BaseAdapterListener;
import com.xy.xylibrary.Interface.SwipeRefreshListener;
import com.xy.xylibrary.base.BaseAdapter;
import com.xy.xylibrary.config.SwipeRefreshOnRefresh;
import com.xy.xylibrary.refresh.SuperEasyRefreshLayout;
import com.xy.xylibrary.utils.GlideUtil;
import com.xy.xylibrary.utils.Utils;
import com.zt.rainbowweather.entity.OutLookWeather;
import com.zt.rainbowweather.entity.WeatherBean;
import com.zt.rainbowweather.entity.WeatherUtilBean;
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
import com.zt.rainbowweather.ui.fragment.HomeFragment;
import com.zt.rainbowweather.view.MiuiWeatherView;
import com.zt.rainbowweather.view.ScrollFutureDaysWeatherView;
import com.zt.rainbowweather.view.TranslucentActionBar;
import com.zt.rainbowweather.view.TranslucentScrollView;
import com.chenguang.weather.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HomeLogic {

   private Activity context;

   public HomeLogic(Activity context){
          this.context = context;
   }

}
