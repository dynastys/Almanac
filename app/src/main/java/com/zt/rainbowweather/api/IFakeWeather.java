package com.zt.rainbowweather.api;


import com.zt.rainbowweather.entity.FakeWeather;

import java.util.List;

/**
 * Created by liyu on 2017/9/1.
 */

public interface IFakeWeather {

    FakeWeather.FakeBasic getFakeBasic();
    FakeWeather.FakeNow getFakeNow();
    FakeWeather.FakeAqi getFakeAqi();
    List<FakeWeather.FakeForecastDaily> getFakeForecastDaily();
    List<FakeWeather.FakeForecastHourly> getFakeForecastHourly();
    List<FakeWeather.FakeSuggestion> getFakeSuggestion();

}
