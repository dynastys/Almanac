package com.zt.rainbowweather.presenter.request;

import android.content.Context;

import com.xy.xylibrary.request.RequestConnextor;
import com.zt.rainbowweather.api.AppService;

public class WeatherConnextor extends RequestConnextor<AppService> {

    private static WeatherConnextor connextor;
    private static Context context;

    public static WeatherConnextor getConnextor(Context contexts) {
        context = contexts;
        if (connextor == null) {
            synchronized (WeatherConnextor.class){
                if (connextor == null) {
                    connextor = new WeatherConnextor();
                    connextor.setContext(context);
                }
            }
        }
        return connextor;
    }
}
