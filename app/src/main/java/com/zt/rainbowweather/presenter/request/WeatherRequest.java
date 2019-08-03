package com.zt.rainbowweather.presenter.request;

import android.content.Context;

import com.zt.rainbowweather.api.AppService;
import com.zt.rainbowweather.api.RequestSyntony;
import com.zt.rainbowweather.entity.Icons;
import com.zt.rainbowweather.entity.city.HotCity;

import com.zt.rainbowweather.entity.weather.AirThDay;
import com.zt.rainbowweather.entity.weather.ConventionWeather;

import com.zt.rainbowweather.utils.ConstUtils;
import com.zt.rainbowweather.utils.NetUtils;
import com.zt.rainbowweather.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.http.Query;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class WeatherRequest {

    private CompositeSubscription mSubscriptions = new CompositeSubscription();
    private static WeatherRequest weatherRequest;

    public static WeatherRequest getWeatherRequest() {
        if(weatherRequest == null){
         synchronized (WeatherRequest.class){
             if(weatherRequest == null){
                 weatherRequest = new WeatherRequest();
             }
         }
        }
        return weatherRequest;
    }

    /**
     * 城市搜索
     * */
    public void getCitySearchData(Context context,String location,final RequestSyntony<HotCity> requestSyntony){
        mSubscriptions.add(WeatherConnextor.getConnextor(context).getAppService(AppService.class,"https://search.heweather.net/").CitySearchRxJava(location, ConstUtils.KEY,"cn")
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<HotCity>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(HotCity hotCity) {
                        requestSyntony.onNext(hotCity);
                    }
                })
        );
    }

    /**
     * 热门城市
     * */
    public void getHotCityData(Context context,final RequestSyntony<HotCity> requestSyntony){
        mSubscriptions.add(WeatherConnextor.getConnextor(context).getAppService(AppService.class,"https://search.heweather.net/").HotCityRxJava("cn", ConstUtils.KEY,"30")
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<HotCity>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(HotCity hotCity) {
                        requestSyntony.onNext(hotCity);
                    }
                })
        );
    }

    /**
     * 看一看打点
     * */
    public void getLookAtData(Context context,final RequestSyntony<String> requestSyntony){
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("app", "星云天气");
            requestData.put("page", "看一看");
            requestData.put("content","进入视频");
            requestData.put("app_ver", Util.getVersion(context));
            requestData.put("imsi", Util.getIMSI(context));
            requestData.put("network", NetUtils.getNetworkState(context));
            requestData.put("os", "1");
            requestData.put("os_ver",Util.getVersionString());
            requestData.put("imei", Util.getIMEI(context));
            requestData.put("brand",Util.getDeviceBrand());
            requestData.put("model",Util.getDeviceModel());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),requestData.toString());
        mSubscriptions.add(WeatherConnextor.getConnextor(context).getAppService(AppService.class,"http://api.xytq.qukanzixun.com/").LookAtRxJava(requestBody)
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(String s) {
                        requestSyntony.onNext(s);
                    }
                })
        );
    }

//    /**
//     * 实况天气
//     * */
//    public void getNowWeatherData(Context context,String location,final RequestSyntony<NowWeather> requestSyntony){
//        mSubscriptions.add(WeatherConnextor.getConnextor(context).getAppService(AppService.class).NowRxJava(location, ConstUtils.KEY)
//                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
//                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
//                .subscribe(new Observer<NowWeather>() {
//                    @Override
//                    public void onCompleted() {
//                        requestSyntony.onCompleted();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        requestSyntony.onError(e);
//                    }
//
//                    @Override
//                    public void onNext(NowWeather nowWeather) {
//                        requestSyntony.onNext(nowWeather);
//                    }
//                })
//        );
//    }
//
//    /**
//     * 逐小时天气预报
//     * */
//    public void getHourlyData(Context context,String location,final RequestSyntony<Hourly> requestSyntony){
//        mSubscriptions.add(WeatherConnextor.getConnextor(context).getAppService(AppService.class).HourlyRxJava(location, ConstUtils.KEY)
//                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
//                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
//                .subscribe(new Observer<Hourly>() {
//                    @Override
//                    public void onCompleted() {
//                        requestSyntony.onCompleted();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        requestSyntony.onError(e);
//                    }
//
//                    @Override
//                    public void onNext(Hourly hourly) {
//                        requestSyntony.onNext(hourly);
//                    }
//                })
//        );
//    }
//
//    /**
//     * 格点实况天气
//     * (暂无)
//     * */
//    public void getGridNowData(Context context,String location,final RequestSyntony<Now> requestSyntony){
//        mSubscriptions.add(WeatherConnextor.getConnextor(context).getAppService(AppService.class).GridNowRxJava(location, ConstUtils.KEY)
//                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
//                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
//                .subscribe(new Observer<Now>() {
//                    @Override
//                    public void onCompleted() {
//                        requestSyntony.onCompleted();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        requestSyntony.onError(e);
//                    }
//
//                    @Override
//                    public void onNext(Now now) {
//                        requestSyntony.onNext(now);
//                    }
//                })
//        );
//    }
//
//    /**
//     * 分钟级降雨
//     * （暫無）
//     * */
//    public void getGridMinuteData(Context context,String location,final RequestSyntony<GridMinute> requestSyntony){
//        mSubscriptions.add(WeatherConnextor.getConnextor(context).getAppService(AppService.class).GridMinuteRxJava(location, ConstUtils.KEY)
//                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
//                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
//                .subscribe(new Observer<GridMinute>() {
//                    @Override
//                    public void onCompleted() {
//                        requestSyntony.onCompleted();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        requestSyntony.onError(e);
//                    }
//
//                    @Override
//                    public void onNext(GridMinute gridMinute) {
//                        requestSyntony.onNext(gridMinute);
//                    }
//                })
//        );
//    }
//
//    /**
//     * 格点逐小时预报
//     *  * (暂无)
//     * */
//    public void getGridHourlyData(Context context,String location,final RequestSyntony<GridHourly> requestSyntony){
//        mSubscriptions.add(WeatherConnextor.getConnextor(context).getAppService(AppService.class).GridHourlyRxJava(location, ConstUtils.KEY)
//                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
//                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
//                .subscribe(new Observer<GridHourly>() {
//                    @Override
//                    public void onCompleted() {
//                        requestSyntony.onCompleted();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        requestSyntony.onError(e);
//                    }
//
//                    @Override
//                    public void onNext(GridHourly gridHourly) {
//                        requestSyntony.onNext(gridHourly);
//                    }
//                })
//        );
//    }
//
//    /**
//     * 格点7天预报
//     * (暂无)
//     * */
//    public void getForecastData(Context context,String location,final RequestSyntony<Forecast> requestSyntony){
//        mSubscriptions.add(WeatherConnextor.getConnextor(context).getAppService(AppService.class).ForecastRxJava(location, ConstUtils.KEY)
//                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
//                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
//                .subscribe(new Observer<Forecast>() {
//                    @Override
//                    public void onCompleted() {
//                        requestSyntony.onCompleted();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        requestSyntony.onError(e);
//                    }
//
//                    @Override
//                    public void onNext(Forecast forecast) {
//                        requestSyntony.onNext(forecast);
//                    }
//                })
//        );
//    }
//
//    /**
//     * 3-10天天气预报
//     * */
//    public void getWeatherReportData(Context context,String location,final RequestSyntony<WeatherReport> requestSyntony){
//        mSubscriptions.add(WeatherConnextor.getConnextor(context).getAppService(AppService.class).WeatherReportRxJava(location, ConstUtils.KEY)
//                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
//                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
//                .subscribe(new Observer<WeatherReport>() {
//                    @Override
//                    public void onCompleted() {
//                        requestSyntony.onCompleted();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        requestSyntony.onError(e);
//                    }
//
//                    @Override
//                    public void onNext(WeatherReport weatherReport) {
//                        requestSyntony.onNext(weatherReport);
//                    }
//                })
//        );
//    }

    /**
     *常规天气数据集合
     * */
    public void getConventionWeatherData(Context context,String location,final RequestSyntony<ConventionWeather> requestSyntony){
        mSubscriptions.add(WeatherConnextor.getConnextor(context).getAppService(AppService.class).ConventionWeatherRxJava(location, ConstUtils.KEY)
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<ConventionWeather>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(ConventionWeather conventionWeather) {
                        requestSyntony.onNext(conventionWeather);
                    }
                })
        );
    }
//
//    /**
//     *生活指数
//     * */
//    public void getIndexOfLivingData(Context context,String location,final RequestSyntony<IndexOfLiving> requestSyntony){
//        mSubscriptions.add(WeatherConnextor.getConnextor(context).getAppService(AppService.class).IndexOfLivingRxJava(location, ConstUtils.KEY)
//                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
//                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
//                .subscribe(new Observer<IndexOfLiving>() {
//                    @Override
//                    public void onCompleted() {
//                        requestSyntony.onCompleted();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        requestSyntony.onError(e);
//                    }
//
//                    @Override
//                    public void onNext(IndexOfLiving indexOfLiving) {
//                        requestSyntony.onNext(indexOfLiving);
//                    }
//                })
//        );
//    }
//
//    /**
//     * 空气质量实况
//     * */
//    public void getAirPESData(Context context,String location,final RequestSyntony<AirPES> requestSyntony){
//        mSubscriptions.add(WeatherConnextor.getConnextor(context).getAppService(AppService.class).AirPESRxJava(location, ConstUtils.KEY)
//                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
//                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
//                .subscribe(new Observer<AirPES>() {
//                    @Override
//                    public void onCompleted() {
//                        requestSyntony.onCompleted();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        requestSyntony.onError(e);
//                    }
//
//                    @Override
//                    public void onNext(AirPES airPES) {
//                        requestSyntony.onNext(airPES);
//                    }
//                })
//        );
//    }
//
//    /**
//     * 空气质量逐小时预报
//     * （暂无）
//     * */
//    public void getAirHourData(Context context,String location,final RequestSyntony<AirHour> requestSyntony){
//        mSubscriptions.add(WeatherConnextor.getConnextor(context).getAppService(AppService.class).AirHourRxJava(location, ConstUtils.KEY)
//                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
//                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
//                .subscribe(new Observer<AirHour>() {
//                    @Override
//                    public void onCompleted() {
//                        requestSyntony.onCompleted();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        requestSyntony.onError(e);
//                    }
//
//                    @Override
//                    public void onNext(AirHour airHour) {
//                        requestSyntony.onNext(airHour);
//                    }
//                })
//        );
//    }

    /**
     * 空气质量7天预报
     * */
    public void getAirThDayData(Context context,String location,final RequestSyntony<AirThDay> requestSyntony){
        mSubscriptions.add(WeatherConnextor.getConnextor(context).getAppService(AppService.class).AirThDayRxJava(location, ConstUtils.KEY)
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<AirThDay>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(AirThDay airThDay) {
                        requestSyntony.onNext(airThDay);
                    }
                })
        );
    }

//    /**
//     * 空气质量数据集合
//     * */
//    public void getAirQualityData(Context context,String location,final RequestSyntony<AirQuality> requestSyntony){
//        mSubscriptions.add(WeatherConnextor.getConnextor(context).getAppService(AppService.class).AirQualityRxJava(location, ConstUtils.KEY)
//                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
//                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
//                .subscribe(new Observer<AirQuality>() {
//                    @Override
//                    public void onCompleted() {
//                        requestSyntony.onCompleted();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        requestSyntony.onError(e);
//                    }
//
//                    @Override
//                    public void onNext(AirQuality airQuality) {
//                        requestSyntony.onNext(airQuality);
//                    }
//                })
//        );
//    }



}
