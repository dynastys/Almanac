package com.zt.rainbowweather.presenter.request;

import android.content.Context;

import com.zt.rainbowweather.api.AppService;
import com.zt.rainbowweather.api.CalendarService;
import com.zt.rainbowweather.api.RequestSyntony;
import com.zt.rainbowweather.entity.Icons;
import com.zt.rainbowweather.entity.calendar.CrazyDreamRecommend;
import com.zt.rainbowweather.entity.calendar.DanXiangLi;
import com.zt.rainbowweather.entity.calendar.Festival;
import com.zt.rainbowweather.entity.calendar.HuangLi;
import com.zt.rainbowweather.utils.ConstUtils;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class AlmanacRequest {

    private static AlmanacRequest almanacRequest;
    private CompositeSubscription mSubscriptions = new CompositeSubscription();
    private String url = "http://api.xytq.qukanzixun.com/";
    public static AlmanacRequest getAlmanacRequest() {
        if (almanacRequest == null) {
            synchronized (AlmanacRequest.class){
                if (almanacRequest == null) {
                    almanacRequest = new AlmanacRequest();
                }
            }
        }
        return almanacRequest;
    }

    /**
     * 查询节日数据
     * @param date 2019-5-29
     * */
    public void getFestivalData(Context context,String date, final RequestSyntony<Festival> requestSyntony){
        mSubscriptions.add(AlmanacConnextor.getAlmanacConnextor(context).getAppService(CalendarService.class,url).FestivalRxJava(date)
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<Festival>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(Festival festival) {
                        requestSyntony.onNext(festival);
                    }
                })
        );
    }

    /**
     * 获取黄历数据 参数：日期(yyyy-MM-dd) 返回：当天数据
     * @param date 2019-5-29
     * */
    public void getHuangLiData(Context context,String date, final RequestSyntony<HuangLi> requestSyntony){
        mSubscriptions.add(AlmanacConnextor.getAlmanacConnextor(context).getAppService(CalendarService.class,url).HuangLiRxJava(date)
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<HuangLi>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(HuangLi huangLi) {
                        requestSyntony.onNext(huangLi);
                    }
                })
        );
    }

    /**
     * 获取单向历数据
     * @param date 2019-5-29
     * */
    public void getDanXiangLiData(Context context,String date, final RequestSyntony<DanXiangLi> requestSyntony){
        mSubscriptions.add(AlmanacConnextor.getAlmanacConnextor(context).getAppService(CalendarService.class,url).DanXiangLiRxJava(date)
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<DanXiangLi>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(DanXiangLi danXiangLi) {
                        requestSyntony.onNext(danXiangLi);
                    }
                })
        );
    }

    /**
     * icon列表
     * */
    public void getGainIconData(Context context, int type_id,String app_market_code,final RequestSyntony<Icons> requestSyntony){
        mSubscriptions.add(AlmanacConnextor.getAlmanacConnextor(context).getAppService(CalendarService.class,url).GainIconRxJava(type_id, ConstUtils.app_market_code)
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<Icons>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(Icons icons) {
                        requestSyntony.onNext(icons);
                    }
                })
        );
    }


    /**
     * icon列表
     * */
    public void getCrazyDreamRecommendData(Context context,final RequestSyntony<CrazyDreamRecommend> requestSyntony){
        mSubscriptions.add(AlmanacConnextor.getAlmanacConnextor(context).getAppService(CalendarService.class,url).CrazyDreamRecommendRxJava()
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<CrazyDreamRecommend>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(CrazyDreamRecommend crazyDreamRecommend) {
                        requestSyntony.onNext(crazyDreamRecommend);
                    }
                })
        );
    }

}
