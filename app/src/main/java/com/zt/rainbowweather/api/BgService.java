package com.zt.rainbowweather.api;

import com.zt.rainbowweather.entity.background.AppSpread;
import com.zt.rainbowweather.entity.background.BackdropTheme;
import com.zt.rainbowweather.entity.background.Background;
import com.zt.rainbowweather.entity.background.PersonalCenterIcon;
import com.zt.rainbowweather.entity.background.SkinTheme;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface BgService {
    /**
     * 图片集合
     * */
    @GET("data/福利/20/1")
    Observable<Background> BackgroundRxJava();//@Query参数定义

    /**
     * 皮肤主题集合
     * */
    @GET("backdrop-themes")
    Observable<SkinTheme> BackdropThemesRxJava();

    /**
     * 按主题id查询图片（主题id、可选参数当前图片id）
     * */
    @GET("backdrop-themes/{backdrop_theme_id}")
    Observable<BackdropTheme> BackdropThemeIdRxJava(@Path("backdrop_theme_id") int backdrop_theme_id, @Query("location") String location, @Query("parent_city") String parent_city, @Query("admin_area") String admin_area, @Query("current_img_id") int current_img_id);

    /**
     * APP开屏
     * */
    @GET("app-open")
    Observable<AppSpread> AppSpreadRxJava();

    /**
     * 获取个人中心第一板块icon列表
     * */
    @GET("icons/server")
    Observable<PersonalCenterIcon> PersonalCenterIconRxJava(@Query("app_market_code") String app_market_code);

    /**
     * 获取指数icon
     * */
    @GET("icons")
    Observable<PersonalCenterIcon> IndexIconRxJava(@Query("type_id") String type_id,@Query("app_market_code") String app_market_code);
}
