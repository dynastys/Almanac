package com.zt.rainbowweather.presenter.request;

import android.content.Context;

import com.xy.xylibrary.request.RequestConnextor;
import com.zt.rainbowweather.api.AppService;
import com.zt.rainbowweather.api.NewsService;

public class NewsConnextor extends RequestConnextor<NewsService> {

    private static NewsConnextor connextor;
    private static Context context;

    public static NewsConnextor getConnextor(Context contexts) {
        context = contexts;
        if (connextor == null) {
            synchronized (NewsConnextor.class){
                if (connextor == null) {
                    connextor = new NewsConnextor();
                    connextor.setContext(context);
                }
            }
        }
        return connextor;
    }
}
