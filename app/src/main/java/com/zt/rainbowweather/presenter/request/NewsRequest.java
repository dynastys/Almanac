package com.zt.rainbowweather.presenter.request;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.xy.xylibrary.utils.RomUtils;
import com.xy.xylibrary.utils.SaveShare;
import com.zt.rainbowweather.api.AppService;
import com.zt.rainbowweather.api.NewsService;
import com.zt.rainbowweather.api.RequestSyntony;
import com.zt.rainbowweather.entity.ServiceList;
import com.zt.rainbowweather.entity.news.AppSwitch;
import com.zt.rainbowweather.entity.news.Article;
import com.zt.rainbowweather.entity.news.LatestVersion;
import com.zt.rainbowweather.entity.news.NewColumn;
import com.zt.rainbowweather.entity.news.Nnotice;
import com.zt.rainbowweather.entity.news.Switch;
import com.zt.rainbowweather.entity.weather.UserData;
import com.zt.rainbowweather.ui.activity.StartActivity;
import com.zt.rainbowweather.utils.ConstUtils;
import com.zt.rainbowweather.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class NewsRequest {

    private CompositeSubscription mSubscriptions = new CompositeSubscription();
    private static NewsRequest newsRequest;
    private String url = "http://api.xytq.qukanzixun.com/";
    public static NewsRequest getNewsRequest() {
        if(newsRequest == null){
            synchronized (NewsRequest.class){
                if(newsRequest == null){
                    newsRequest = new NewsRequest();
                }
            }
        }
        return newsRequest;
    }
    /**
     * 新闻栏目
     * */
    public void getNewsColumnData(Context context,final RequestSyntony<NewColumn> requestSyntony){
        mSubscriptions.add(NewsConnextor.getConnextor(context).getAppService(NewsService.class,url).NewColumnRxJava(ConstUtils.app_market_code)
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<NewColumn>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(NewColumn newColumn) {
                        requestSyntony.onNext(newColumn);
                    }
                })
        );
    }

    /**
     * app模块开关
     * */
    public void getAppSwitchData(Context context,final RequestSyntony<AppSwitch> requestSyntony){
        mSubscriptions.add(NewsConnextor.getConnextor(context).getAppService(NewsService.class,url).AppSwitchRxJava(ConstUtils.app_market_code, Util.getVersion(context))
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<AppSwitch>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(AppSwitch appSwitch) {
                        requestSyntony.onNext(appSwitch);
                    }
                })
        );
    }

    /**
     * 同步用户当前地域信息
     * */
    public void getAppRegionData(Context context, UserData userData){
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("user_id", userData.user_id);
            requestData.put("device_token", userData.device_token);
            requestData.put("imei", userData.imei);
            requestData.put("province", userData.province);
            requestData.put("city", userData.city);
            requestData.put("county", userData.county);
            requestData.put("longitude", userData.longitude);
            requestData.put("latitude", userData.latitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),requestData.toString());
        Log.e("requestData", "getAppRegionData: "+requestData.toString() );
        mSubscriptions.add(NewsConnextor.getConnextor(context).getAppService(NewsService.class,url).AppRegionRxJava(requestBody)
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<AppSwitch>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(AppSwitch appSwitch) {
                        Log.e("requestData", "getAppRegionData: "+appSwitch.toString() );
                    }
                })
        );
    }

    /**
     * 新闻列表
     * */
    public void getNewsListData(Context context, String channelid,int pageindex,int pagesize,final RequestSyntony<Article> requestSyntony){
        mSubscriptions.add(NewsConnextor.getConnextor(context).getAppService(NewsService.class,url).ArticleRxJava(channelid, Util.getIMEI(context),Util.getMACAddress(context),Util.getAndroidId(context),pageindex,pagesize)
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<Article>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(Article article) {
                        requestSyntony.onNext(article);
                    }
                })
        );
    }

    /**
     * 服务数据
     * */
    public void getServiceListData(Context context,final RequestSyntony<ServiceList> requestSyntony){
        mSubscriptions.add(NewsConnextor.getConnextor(context).getAppService(NewsService.class,"http://api.xy1732.cn/api/").NewColumnRxJava()
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<ServiceList>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(ServiceList serviceList) {
                        requestSyntony.onNext(serviceList);
                    }
                })
        );
    }

    /**
     * 更新接口
     * */
    public void getUpdateData(Context context,final RequestSyntony<LatestVersion> requestSyntony){
        mSubscriptions.add(NewsConnextor.getConnextor(context).getAppService(NewsService.class,url).Latest(ConstUtils.app_market_code)
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<LatestVersion>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(LatestVersion latestVersion) {
                        requestSyntony.onNext(latestVersion);
                    }
                })
        );
    }

    /**
     * Ad频控
     * */
    public void getSwitchData(Context context,final RequestSyntony<Switch> requestSyntony){
        mSubscriptions.add(NewsConnextor.getConnextor(context).getAppService(NewsService.class,"http://api.xy1732.cn/api/").Switch(ConstUtils.app_market_code)
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<Switch>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(Switch sw) {
                        requestSyntony.onNext(sw);
                    }
                })
        );
    }


    /**
     * 首页公告-跑马灯
     * */
    public void getNnoticeData(Context context,final RequestSyntony<Nnotice> requestSyntony){
        mSubscriptions.add(NewsConnextor.getConnextor(context).getAppService(NewsService.class,url).NnoticeRxJava()
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<Nnotice>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(Nnotice nnotice) {
                        requestSyntony.onNext(nnotice);
                    }
                })
        );
    }

    /**
     * 新闻栏目缓存
     * */
    public void NewsData(Context context){
        try {
             LitePal.deleteAll(NewColumn.DataBean.class);
            NewsRequest.getNewsRequest().getAppSwitchData(context, new RequestSyntony<AppSwitch>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(AppSwitch appSwitch) {
                    try {
                        if(appSwitch.getData() != null && appSwitch.getData().size() > 0){
                            ConstUtils.home_news = appSwitch.getData().get(0).isOn_off(); //首页底部资讯
                            ConstUtils.almanac_icon = appSwitch.getData().get(1).isOn_off(); //黄历icon
                            ConstUtils.almanac_news = appSwitch.getData().get(2).isOn_off(); //黄历资讯
                            ConstUtils.take_a_look = appSwitch.getData().get(3).isOn_off(); //看一看
                            RomUtils.take_a_look = appSwitch.getData().get(3).isOn_off(); //看一看
                            ConstUtils.personal_center_icon1 = appSwitch.getData().get(4).isOn_off(); //个人中心icon1
                            ConstUtils.personal_center_icon2 = appSwitch.getData().get(5).isOn_off(); //个人中心icon2
                            if (appSwitch.getData().get(6).isOn_off()) {
                                SaveShare.saveValue(context, "ISAD", "1");
                            } else {
                                SaveShare.saveValue(context, "ISAD", "0");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            NewsRequest.getNewsRequest().getNewsColumnData(context, new RequestSyntony<NewColumn>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(NewColumn newColumn) {
                    LitePal.saveAll(newColumn.getData());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
