package com.xy.xylibrary.presenter;

import android.content.Context;

import com.xy.xylibrary.api.DotService;
import com.xy.xylibrary.request.RequestConnextor;


public class DotConnextor extends RequestConnextor<DotService> {
    private static DotConnextor connextor;
    private static Context context;

    public static DotConnextor getConnextor(Context contexts) {
        context = contexts;
        if (connextor == null) {
            synchronized (DotConnextor.class){
                if (connextor == null) {
                    connextor = new DotConnextor();
                    connextor.setContext(context);
                }
            }
        }
        return connextor;
    }
}
