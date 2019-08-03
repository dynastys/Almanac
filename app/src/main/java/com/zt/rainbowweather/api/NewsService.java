package com.zt.rainbowweather.api;

import com.zt.rainbowweather.entity.ServiceList;
import com.zt.rainbowweather.entity.news.AppSwitch;
import com.zt.rainbowweather.entity.news.Article;
import com.zt.rainbowweather.entity.news.LatestVersion;
import com.zt.rainbowweather.entity.news.NewColumn;
import com.zt.rainbowweather.entity.news.Nnotice;
import com.zt.rainbowweather.entity.news.Switch;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface NewsService {

    /**
     * 新闻栏目
     * @param code
     * */
    @GET("channl")
    Observable<NewColumn> NewColumnRxJava(@Query("code") String code,@Query("appkey") String appkey);//@Query参数定义

    /**
     * 新闻列表
     * @param channelid 栏目id
     * @param pageindex 分页大小1
     * @param pagesize 分页总数10
     * */
    @GET("articles/{channelid}")
    Observable<Article> ArticleRxJava(@Path("channelid") String channelid,@Query("imei") String imei,@Query("mac") String mac,@Query("androidid") String androidid, @Query("pageindex") int pageindex, @Query("pagesize") int pagesize);//@Query参数定义

    /**
     * 服务数据
     * */
    @GET("thirdparty-links")
    Observable<ServiceList> NewColumnRxJava();

    /**
     * 安卓获取最新版本
     * */
    @GET("android/releases/latest")
    Observable<LatestVersion> Latest(@Query("app_market_code") String app_market_code);

    /**
     * AD频控http://api.xy1732.cn/api/ad_switch?appkey=应用市场
     * */
    @GET("ad_switch")
    Observable<Switch> Switch(@Query("appkey") String appkey);

    /**
     *首页公告-跑马灯
     * */
    @GET("home/notice")
    Observable<Nnotice> NnoticeRxJava();

    /**
     * 获取首页资讯栏目
     * @param app_market_code
     * */
    @GET("channls")
    Observable<NewColumn> NewColumnRxJava(@Query("app_market_code") String app_market_code);//@Query参数定义

    /**
     * App模块开关
     * @param app_market_code
     * @param version_name
     * */
    @GET("app/module-switch")
    Observable<AppSwitch> AppSwitchRxJava(@Query("app_market_code") String app_market_code,@Query("version_name") String version_name);//@Query参数定义

}
