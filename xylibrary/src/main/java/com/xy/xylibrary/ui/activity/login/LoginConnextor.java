package com.xy.xylibrary.ui.activity.login;

import android.content.Context;

import com.xy.xylibrary.request.RequestConnextor;

public class LoginConnextor extends RequestConnextor<LoginApi> {

    private static LoginConnextor connextor;
    private static Context context;

    public static LoginConnextor getConnextor(Context contexts) {
        context = contexts;
        if (connextor == null) {
            synchronized (LoginConnextor.class){
                if (connextor == null) {
                    connextor = new LoginConnextor();
                    connextor.setContext(context);
                }
            }
        }
        return connextor;
    }
}
