package com.zt.rainbowweather.presenter.request;

import android.content.Context;

import com.zt.rainbowweather.api.AppService;
import com.zt.rainbowweather.api.NewsService;
import com.zt.rainbowweather.api.RequestSyntony;
import com.zt.rainbowweather.entity.ServiceList;
import com.zt.rainbowweather.entity.news.Article;
import com.zt.rainbowweather.entity.news.LatestVersion;
import com.zt.rainbowweather.entity.news.NewColumn;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class NewsRequest {

    private CompositeSubscription mSubscriptions = new CompositeSubscription();
    private static NewsRequest newsRequest;

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
    public void getNewsColumnData(Context context, final RequestSyntony<NewColumn> requestSyntony){
        mSubscriptions.add(NewsConnextor.getConnextor(context).getAppService(NewsService.class,"http://api.xy1732.cn/api/").NewColumnRxJava("chtq")
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
     * 新闻列表
     * */
    public void getNewsListData(Context context, String channelid,int pageindex,int pagesize,final RequestSyntony<Article> requestSyntony){
        mSubscriptions.add(NewsConnextor.getConnextor(context).getAppService(NewsService.class,"http://api.xy1732.cn/api/").ArticleRxJava(channelid,pageindex,pagesize)
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
        mSubscriptions.add(NewsConnextor.getConnextor(context).getAppService(NewsService.class,"http://api.xy1732.cn/api/").Latest()
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
}
