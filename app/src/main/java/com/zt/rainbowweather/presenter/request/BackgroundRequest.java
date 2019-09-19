package com.zt.rainbowweather.presenter.request;

import android.content.Context;

import com.zt.rainbowweather.api.BgService;
import com.zt.rainbowweather.api.NewsService;
import com.zt.rainbowweather.api.RequestSyntony;
import com.zt.rainbowweather.entity.background.AppSpread;
import com.zt.rainbowweather.entity.background.BackdropTheme;
import com.zt.rainbowweather.entity.background.Background;
import com.zt.rainbowweather.entity.background.PersonalCenterIcon;
import com.zt.rainbowweather.entity.background.SkinTheme;
import com.zt.rainbowweather.entity.news.NewColumn;
import com.zt.rainbowweather.utils.ConstUtils;

import retrofit2.http.Query;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 背景图片
 * */
public class BackgroundRequest {

    private static BackgroundRequest backgroundRequest;
    private CompositeSubscription mSubscriptions = new CompositeSubscription();
    private String url = "http://api.xytq.qukanzixun.com/";
    public static BackgroundRequest getBackgroundRequest() {
        if (backgroundRequest == null) {
            synchronized (BackgroundRequest.class){
                if (backgroundRequest == null) {
                    backgroundRequest = new BackgroundRequest();
                }
            }
        }
        return backgroundRequest;
    }

    /**
     * 图片集合
     * http://gank.io/api/data/%E7%A6%8F%E5%88%A9/20/1
     * */
    public void getPictureData(Context context, final RequestSyntony<Background> requestSyntony){
        mSubscriptions.add(BackgroundConnextor.getConnextor(context).getAppService(BgService.class,"http://gank.io/api/").BackgroundRxJava()
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<Background>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(Background background) {
                        requestSyntony.onNext(background);
                    }
                })
        );
    }

    /**
     * 皮肤主题集合
     * */
    public void getBackdropThemesData(Context context, final RequestSyntony<SkinTheme> requestSyntony){
        mSubscriptions.add(BackgroundConnextor.getConnextor(context).getAppService(BgService.class,url).BackdropThemesRxJava()
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<SkinTheme>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(SkinTheme skinTheme) {
                        requestSyntony.onNext(skinTheme);
                    }
                })
        );
    }

    /**
     * 按主题id查询图片（主题id、可选参数当前图片id）
     * */
    public void getBackdropThemeIdData(Context context, int backdrop_theme_id,int current_img_id,String location,String parent_city,String admin_area, final RequestSyntony<BackdropTheme> requestSyntony){
        mSubscriptions.add(BackgroundConnextor.getConnextor(context).getAppService(BgService.class,url).BackdropThemeIdRxJava(backdrop_theme_id,location,parent_city,admin_area,current_img_id)
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<BackdropTheme>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(BackdropTheme backdropTheme) {
                        requestSyntony.onNext(backdropTheme);
                    }
                })
        );
    }

    /**
     * APP开屏
     * */
    public void getAppSpreadData(Context context, final RequestSyntony<AppSpread> requestSyntony){
        mSubscriptions.add(BackgroundConnextor.getConnextor(context).getAppService(BgService.class,url).AppSpreadRxJava()
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<AppSpread>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(AppSpread appSpread) {
                        requestSyntony.onNext(appSpread);
                    }
                })
        );
    }


    /**
     * 获取个人中心第一板块icon列表
     * */
    public void getPersonalCenterIconData(Context context,String app_market_code, final RequestSyntony<PersonalCenterIcon> requestSyntony){
        mSubscriptions.add(BackgroundConnextor.getConnextor(context).getAppService(BgService.class,url).PersonalCenterIconRxJava(ConstUtils.app_market_code)
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<PersonalCenterIcon>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(PersonalCenterIcon personalCenterIcon) {
                        requestSyntony.onNext(personalCenterIcon);
                    }
                })
        );
    }

}
