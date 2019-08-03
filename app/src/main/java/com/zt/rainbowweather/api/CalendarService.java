package com.zt.rainbowweather.api;

import com.zt.rainbowweather.entity.Icons;
import com.zt.rainbowweather.entity.calendar.CrazyDreamRecommend;
import com.zt.rainbowweather.entity.calendar.DanXiangLi;
import com.zt.rainbowweather.entity.calendar.Festival;
import com.zt.rainbowweather.entity.calendar.HuangLi;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface CalendarService {

    /**
     * 查询节日数据
     * */
    @GET("calendar/Festival")
    Observable<Festival> FestivalRxJava(@Query("date") String date);//@Query参数定义

    /**
     * 获取黄历数据 参数：日期(yyyy-MM-dd) 返回：当天数据
     * */
    @GET("calendar/huangli")
    Observable<HuangLi> HuangLiRxJava(@Query("date") String date);//@Query参数定义

    /**
     * 获取单向历数据
     * */
    @GET("calendar/danxiangli")
    Observable<DanXiangLi> DanXiangLiRxJava(@Query("date") String date);//@Query参数定义

    /**
     * 按icon类别id获取所属icon
     * */
    @GET("icons")
    Observable<Icons> GainIconRxJava(@Query("type_id") int type_id, @Query("app_market_code") String app_market_code);

    /**
     * 周公解梦搜索页面
     * */
    @GET("zgjm/search")
    Observable<Icons> CrazyDreamRxJava(@Query("keyword") String keyword);

    /**
     * 周公解梦，推荐接口
     * */
    @GET("zgjm/hot")
    Observable<CrazyDreamRecommend> CrazyDreamRecommendRxJava();
}
