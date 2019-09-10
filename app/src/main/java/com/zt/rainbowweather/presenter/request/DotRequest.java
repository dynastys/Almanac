package com.zt.rainbowweather.presenter.request;

import android.content.Context;

import com.zt.rainbowweather.api.RequestSyntony;
import com.zt.rainbowweather.entity.news.NewColumn;

import rx.subscriptions.CompositeSubscription;

public class DotRequest {

    private CompositeSubscription mSubscriptions = new CompositeSubscription();
    private static DotRequest dotRequest;
    private String url = "http://121.199.42.243:8004/Help/";
    public static DotRequest getDotRequest(){
        if (dotRequest == null){
            synchronized (DotRequest.class){
                if (dotRequest == null){
                    dotRequest = new DotRequest();
                }
            }
        }
        return dotRequest;
    }

    public void getActivity(Context context, final RequestSyntony<NewColumn> requestSyntony){

    }
}
