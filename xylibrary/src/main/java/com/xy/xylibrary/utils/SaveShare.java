package com.xy.xylibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/*
* SharedPreferences工具类
* */
public class SaveShare {

	private static final String MY_PREFERENCE = "set";

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

	public static boolean putDrawable(Context context, String key, Drawable d) {
		Editor e = null;
		try {
			SharedPreferences sp = context.getSharedPreferences(MY_PREFERENCE,
                    Context.MODE_PRIVATE);
			paraCheck(sp, key);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			((BitmapDrawable) d).getBitmap()
                    .compress(Bitmap.CompressFormat.JPEG, 50, baos);
			String imageBase64 = new String(Base64.encode(baos.toByteArray(),
                    Base64.DEFAULT));
			e = sp.edit();
			e.putString(key, imageBase64);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return e.commit();
	}

	public static Drawable getDrawable(Context context, String key
									   ) {
		try {
			SharedPreferences sp = context.getSharedPreferences(MY_PREFERENCE,
                    Context.MODE_PRIVATE);
			paraCheck(sp, key);
			String imageBase64 = sp.getString(key, "");
			if (TextUtils.isEmpty(imageBase64)) {
                return null;
            }

			byte[] base64Bytes = Base64.decode(imageBase64.getBytes(),
                    Base64.DEFAULT);
			ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
			Drawable ret = Drawable.createFromStream(bais, "");
			if (ret != null) {
                return ret;
            } else {
                return null;
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void paraCheck(SharedPreferences sp, String key) {
		if (sp == null) {
			throw new IllegalArgumentException();
		}
		if (TextUtils.isEmpty(key)) {
			throw new IllegalArgumentException();
		}
	}
}
