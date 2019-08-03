package com.zt.rainbowweather.presenter.request;

import android.content.Context;

import com.xy.xylibrary.request.RequestConnextor;
import com.zt.rainbowweather.api.BgService;

public class BackgroundConnextor extends RequestConnextor<BgService> {

    private static BackgroundConnextor connextor;
    private static Context context;

    public static BackgroundConnextor getConnextor(Context contexts) {
        context = contexts;
        if (connextor == null) {
            synchronized (BackgroundConnextor.class){
                if (connextor == null) {
                    connextor = new BackgroundConnextor();
                    connextor.setContext(context);
                }
            }
        }
        return connextor;
    }
}
