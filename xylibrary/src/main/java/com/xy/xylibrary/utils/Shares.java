package com.xy.xylibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.view.View;

import com.constellation.xylibrary.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import butterknife.BuildConfig;


/**
 * author : zchu
 * date   : 2017/10/24
 * desc   :
 */

public class Shares {
    public static Context contexts;
    public static void share(Context context, int stringRes) {
        contexts = context;
        share(context, context.getString(stringRes));
    }

    public static void share(Context context, String extraText) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.share));
        intent.putExtra(Intent.EXTRA_TEXT, extraText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(
                Intent.createChooser(intent, context.getString(R.string.share)));
    }

    public static void shareImage(Context context, Uri uri, String title) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/jpeg");
        context.startActivity(Intent.createChooser(shareIntent, title));
    }

    // 读取Assets里面的图片转化成Bitmap
    private static Bitmap getImageFromAssetsFile(Context context, String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    // Bitmap以文件File形式保存在本地
    private static Uri saveBitmap(Bitmap bm, String picName) {
        try {
            String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/zqhd/" + picName + ".jpg";
            File f = new File(dir);
            if (!f.exists()) {
                f.getParentFile().mkdirs();
                f.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(f);

            bm.compress(Bitmap.CompressFormat.JPEG, 80, out);
            out.flush();
            out.close();
            Uri uri;
            if (Build.VERSION.SDK_INT > 23) {
                uri = FileProvider.getUriForFile(contexts, "com.chenguang.weather.fileprovider", f);
            }else{
                uri = Uri.fromFile(f);
            }
            return uri;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void localshare(Activity context, String pic,View tempView) {
        contexts = context;
        Bitmap bgimg0 = convertViewToBitmap(tempView);
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
        share_intent.setType("image/"); //设置分享内容的类型
        share_intent.putExtra(Intent.EXTRA_STREAM, saveBitmap(bgimg0, pic));
        //创建分享的Dialog
        share_intent = Intent.createChooser(share_intent, "分享图片");
        context.startActivity(share_intent);
    }
    private static Bitmap convertViewToBitmap(View tempView) {
        /**
         * 创建一个bitmap放于画布之上进行绘制 （简直如有神助）
         */
        Bitmap bitmap = Bitmap.createBitmap(tempView.getWidth(),
                tempView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        tempView.draw(canvas);
        return bitmap;
    }

    /**
     * 获取当前屏幕截图，不包含状态栏（Status Bar）。
     *
     * @param activity activity
     * @return Bitmap
     */
    public static Bitmap screenShot(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int statusBarHeight = getStatusBarHeight(activity);
        int width = (int) getDeviceDisplaySize(activity)[0];
        int height = (int) getDeviceDisplaySize(activity)[1];

        Bitmap ret = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();

        return ret;
    }

    public static float[] getDeviceDisplaySize(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        float[] size = new float[2];
        size[0] = width;
        size[1] = height;

        return size;
    }

    public static int getStatusBarHeight(Context context) {
        int height = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = context.getResources().getDimensionPixelSize(resourceId);
        }

        return height;
    }
    /**
     * Bitmap对象是否为空。
     */
    public static boolean isEmptyBitmap(Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

}
