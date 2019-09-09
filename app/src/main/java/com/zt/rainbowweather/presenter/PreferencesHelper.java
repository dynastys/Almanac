package com.zt.rainbowweather.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.zt.rainbowweather.BasicApplication;


/**
 * Created by Chu on 2017/8/26.
 */

public class PreferencesHelper {
    private static final String K_FIRST_START_APP = "first_start_app";
    private static final String K_FEEDBACK_ID = "feedback_id";
    private static final String K_SHOW_FEEDBACK_CONTACT_DIALOG = "show_feedback_contact_dialog";
    private static final String K_FEEDBACK_CONTACT = "feedback_contact";
    private static final String K_LAST_LOGIN_NAME = "last_login_name";


    private static PreferencesHelper sPreferencesHelper;
    private SharedPreferences mPreferences;

    private PreferencesHelper(Context context) {
        if (context != null) {
            mPreferences = context.getSharedPreferences("app-prefs", Context.MODE_PRIVATE);
        }
    }

    public static PreferencesHelper getInstance() {
        if (sPreferencesHelper == null) {
            sPreferencesHelper = new PreferencesHelper(BasicApplication.getContext());
        }
        return sPreferencesHelper;
    }




    public boolean isFirstStartApp() {
        return mPreferences.getBoolean(K_FIRST_START_APP, true);
    }

    public void setFirstStartApp(boolean b) {
        mPreferences
                .edit()
                .putBoolean(K_FIRST_START_APP, b)
                .apply();
    }


    public void setFeedbackId(String id) {
        mPreferences
                .edit()
                .putString(K_FEEDBACK_ID, id)
                .apply();
    }

    public String getFeedbackId() {
        return mPreferences.getString(K_FEEDBACK_ID, null);
    }

    public boolean getCanShowFeedbackContactDialog() {
        return mPreferences.getBoolean(K_SHOW_FEEDBACK_CONTACT_DIALOG, true);
    }

    public void setCanShowFeedbackContactDialog(boolean canShow) {
        mPreferences
                .edit()
                .putBoolean(K_SHOW_FEEDBACK_CONTACT_DIALOG, canShow)
                .apply();
    }

    public void setFeedbackContact(String contact) {
        mPreferences
                .edit()
                .putString(K_FEEDBACK_CONTACT, contact)
                .apply();
    }

    public String getFeedbackContact() {
        return mPreferences.getString(K_FEEDBACK_CONTACT, null);
    }

    public void setLastLoginName(String loginName){
        mPreferences
                .edit()
                .putString(K_LAST_LOGIN_NAME, loginName)
                .apply();
    }
    public String getLastLoginName() {
        return mPreferences.getString(K_LAST_LOGIN_NAME, null);
    }

}
