package com.zt.rainbowweather.api;

import com.zt.rainbowweather.entity.Icons;
import com.zt.rainbowweather.entity.background.BackdropTheme;
import com.zt.rainbowweather.entity.background.SkinTheme;
import com.zt.rainbowweather.entity.city.HotCity;

import com.zt.rainbowweather.entity.weather.AirThDay;
import com.zt.rainbowweather.entity.weather.ConventionWeather;


import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface AppService {

//    /**
//     * 空气质量数据集合
//     * @param location 需要查询的城市或地区
//     * @param key 用户认证key
//     * */
//    @GET("s6/air")
//    Observable<AirQuality> AirQualityRxJava(@Query("location") String location, @Query("key") String key);//@Query参数定义
//
    /**
     * 空气质量7天预报
     * @param location 需要查询的城市或地区
     * @param key 用户认证key
     * */
    @GET("s6/air/forecast")
    Observable<AirThDay> AirThDayRxJava(@Query("location") String location, @Query("key") String key);//@Query参数定义
//
//    /**
//     * 空气质量逐小时预报
//     * @param location 需要查询的城市或地区
//     * @param key 用户认证key
//     * */
//    @GET("s6/air/hourly")
//    Observable<AirHour> AirHourRxJava(@Query("location") String location, @Query("key") String key);//@Query参数定义
//
//    /**
//     * 空气质量实况
//     * @param location 需要查询的城市或地区
//     * @param key 用户认证key
//     * */
//    @GET("s6/air/now")
//    Observable<AirPES> AirPESRxJava(@Query("location") String location, @Query("key") String key);//@Query参数定义
//
//    /**
//     * 生活指数
//     * @param location 需要查询的城市或地区
//     * @param key 用户认证key
//     * */
//    @GET("s6/weather/lifestyle")
//    Observable<IndexOfLiving> IndexOfLivingRxJava(@Query("location") String location, @Query("key") String key);//@Query参数定义
//
//    /**
//     * 太阳高度
//     * @param lat 所查询地区的纬度
//     * @param lon 所查询地区的经度
//     * @param date 查询日期，格式为yyyyMMdd
//     * @param time 查询时间，格式为HHmm，24 时制
//     * @param alt 海拔高度，单位为米
//     * @param tz 查询地区所在时区
//     * @param key 用户认证key
//     * */
//    @GET("s6/solar/solar-elevation-angle")
//    Observable<String> SunHeightRxJava(@Query("lat") String lat,@Query("lon") String lon,@Query("date") String date,@Query("time") String time,@Query("alt") String alt,@Query("tz") String tz,@Query("key") String key);//@Query参数定义

    /**
     * 常规天气数据集合
     * @param location 需要查询的城市或地区
     * @param key 用户认证key
     * */
    @GET("s6/weather")
    Observable<ConventionWeather> ConventionWeatherRxJava(@Query("location") String location, @Query("key") String key);//@Query参数定义
//
//    /**
//     * 3-10天天气预报
//     * @param location 需要查询的城市或地区
//     * @param key 用户认证key
//     * */
//    @GET("s6/weather/forecast")
//    Observable<WeatherReport> WeatherReportRxJava(@Query("location") String location, @Query("key") String key);//@Query参数定义
//
//    /**
//     * 格点7天预报
//     * @param location 需要查询的城市或地区
//     * @param key 用户认证key
//     * */
//    @GET("s6/weather/grid-forecast")
//    Observable<Forecast> ForecastRxJava(@Query("location") String location, @Query("key") String key);//@Query参数定义
//
//    /**
//     * 格点逐小时预报
//     * @param location 需要查询的城市或地区
//     * @param key 用户认证key
//     * */
//    @GET("s6/weather/grid-hourly")
//    Observable<GridHourly> GridHourlyRxJava(@Query("location") String location, @Query("key") String key);//@Query参数定义
//
//    /**
//     * 分钟级降雨（邻近预报）
//     * @param location 需要查询的城市或地区
//     * @param key 用户认证key
//     * */
//    @GET("s6/weather/grid-minute")
//    Observable<GridMinute> GridMinuteRxJava(@Query("location") String location, @Query("key") String key);//@Query参数定义
//
//    /**
//     * 格点实况天气
//     * @param location 需要查询的城市或地区
//     * @param key 用户认证key
//     * */
//    @GET("s6/weather/grid-now")
//    Observable<Now> GridNowRxJava(@Query("location") String location, @Query("key") String key);//@Query参数定义
//
//    /**
//     * 逐小时天气预报
//     * @param location 需要查询的城市或地区
//     * @param key 用户认证key
//     * */
//    @GET("s6/weather/hourly")
//    Observable<Hourly> HourlyRxJava(@Query("location") String location, @Query("key") String key);//@Query参数定义
//
//    /**
//     * 实况天气
//     * @param location 需要查询的城市或地区
//     * @param key 用户认证key
//     * */
//    @GET("s6/weather/now")
//    Observable<NowWeather> NowRxJava(@Query("location") String location, @Query("key") String key);//@Query参数定义

    /**
     * 城市搜索
     * @param location 需要查询的城市或地区
     * @param key 用户认证key
     * @param group  group=world
     * */
    @GET("find")
    Observable<HotCity> CitySearchRxJava(@Query("location") String location, @Query("key") String key, @Query("group") String group);//@Query参数定义

    /**
     * 热门城市列表
     * @param group  group=world
     * @param key 用户认证key
     * @param number 返回热门城市的数量
     * */
    @GET("top")
    Observable<HotCity> HotCityRxJava(@Query("group") String group, @Query("key") String key, @Query("number") String number);//@Query参数定义

    /**
     * 看一看打点
     * */
    @POST("app/put-log")
    Observable<String> LookAtRxJava(@Body RequestBody requestBody);


}
