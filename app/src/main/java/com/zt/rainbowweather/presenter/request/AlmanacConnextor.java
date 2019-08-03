package com.zt.rainbowweather.presenter.request;


import android.content.Context;

import com.xy.xylibrary.request.RequestConnextor;
import com.zt.rainbowweather.api.CalendarService;

public class AlmanacConnextor extends RequestConnextor<CalendarService> {

    private static AlmanacConnextor almanacConnextor;
    private static Context context;
    public static AlmanacConnextor getAlmanacConnextor(Context contexts) {
        context = contexts;
        if (almanacConnextor == null) {
            synchronized (AlmanacConnextor.class){
                if (almanacConnextor == null) {
                    almanacConnextor = new AlmanacConnextor();
                    almanacConnextor.setContext(context);
                }
            }
        }
        return almanacConnextor;
    }
}
