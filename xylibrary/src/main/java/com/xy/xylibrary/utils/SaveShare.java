package com.xy.xylibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/*
* SharedPreferences工具类
* */
public class SaveShare {
	public static void saveValue(Context context, String key, String value) {
		try {
			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(context);
			Editor editor = sp.edit();
			editor.putString(key, value);
			editor.commit();
		} catch (Exception exp) {

		}
	}

	public static String getValue(Context context, String key) {
		try {
			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(context);
			return sp.getString(key, "");
		} catch (Exception exp) {
			return "";
		}
	}
}
