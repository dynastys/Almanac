package com.zt.rainbowweather.api;

import com.zt.rainbowweather.entity.ServiceList;
import com.zt.rainbowweather.entity.news.Article;
import com.zt.rainbowweather.entity.news.LatestVersion;
import com.zt.rainbowweather.entity.news.NewColumn;
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
    Observable<NewColumn> NewColumnRxJava(@Query("code") String code);//@Query参数定义

    /**
     * 新闻列表
     * @param channelid 栏目id
     * @param pageindex 分页大小1
     * @param pagesize 分页总数10
     * */
    @GET("article/{channelid}")
    Observable<Article> ArticleRxJava(@Path("channelid") String channelid, @Query("pageindex") int pageindex, @Query("pagesize") int pagesize);//@Query参数定义

    /**
     * 服务数据
     * */
    @GET("thirdparty-links")
    Observable<ServiceList> NewColumnRxJava();

    /**
     * 安卓获取最新版本
     * */
    @GET("android/releases/latest")
    Observable<LatestVersion> Latest();
}
