package com.xy.xylibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

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

            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Uri uri;
            if (Build.VERSION.SDK_INT > 23) {
                uri = FileProvider.getUriForFile(contexts, "com.zt.weather.fileprovider", f);
            } else {
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

    public static Uri localshare(Activity context, String pic, View tempView,boolean b) {
        contexts = context;
        try {
            Bitmap bgimg0 = null;
            if(b){
                Bitmap bgimg1;
                if(tempView == null){
                    bgimg1 = screenShot(context);
                }else{
                    bgimg1 = convertViewToBitmap(tempView);
                }
                Bitmap bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.shape_bottom_bg, null);
                bitmap2 = getNewBitmap(bitmap2,bgimg1.getWidth());
                bgimg0 = newBitmap(bgimg1,bitmap2);
            }else{
                Bitmap bitmap1 = convertViewToBitmap(tempView);
//                Bitmap bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.shape_bottom_bg, null);
//                bitmap2 = getNewBitmap(bitmap2,bitmap1.getWidth());
//                bgimg0 = newBitmap(bitmap1,bitmap2);
                //            bgimg0 = drawTextToBitmap(context,bgimg0,"上海","111111111");

            }
            return saveBitmap(bgimg0, pic);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void Share(Context context,Uri uri){
        if (uri != null) {
            Intent share_intent = new Intent();
            share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
            share_intent.setType("image/"); //设置分享内容的类型
            share_intent.putExtra(Intent.EXTRA_STREAM,uri);
            //创建分享的Dialog
            share_intent = Intent.createChooser(share_intent, "分享图片");
            context.startActivity(share_intent);
        } else {
            Toast.makeText(context, "分享失败", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 修改图片高度
     */
    public static Bitmap getNewBitmap(Bitmap bitmap1, int newWidth){
        // 获得图片的宽高.
        int width = bitmap1.getWidth();
        int height = bitmap1.getHeight();
        // 计算缩放比例.
        float scaleWidth = ((float) newWidth) /width;

        // 取得想要缩放的matrix参数.
        android.graphics.Matrix matrix = new android.graphics.Matrix();
        matrix.postScale(scaleWidth, 1);
        // 得到新的图片.
        Bitmap newBitmap = Bitmap.createBitmap(bitmap1, 0, 0, width, height, matrix, true);
        return newBitmap;
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
        String Height = SaveShare.getValue(activity,"Height");
        int height;
        if(!TextUtils.isEmpty(Height)){
            height = (int) getDeviceDisplaySize(activity)[1] - Integer.parseInt(Height);
        }else{
            height = (int) getDeviceDisplaySize(activity)[1];
        }
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
     * 拼接图片
     *
     * @param bit1
     * @param bit2
     * @return 返回拼接后的Bitmap
     */
    private static Bitmap newBitmap(Bitmap bit1,Bitmap bit2){
        int width = bit1.getWidth();
        int height = bit1.getHeight() + bit2.getHeight();
        //创建一个空的Bitmap(内存区域),宽度等于第一张图片的宽度，高度等于两张图片高度总和
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //将bitmap放置到绘制区域,并将要拼接的图片绘制到指定内存区域
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bit1, 0, 0, null);
        canvas.drawBitmap(bit2, 0, bit1.getHeight(), null);
        return bitmap;
    }


    /**
     * Bitmap对象是否为空。
     */
    public static boolean isEmptyBitmap(Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

    static Bitmap bitmap;

    public static Bitmap drawTextToBitmap(Activity mContext, Bitmap bit, String mText, String str3) {
        try {
            Resources resources = mContext.getResources();
            float scale = resources.getDisplayMetrics().density;
            // Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId);
            bitmap = bit;
            Bitmap.Config bitmapConfig = bitmap.getConfig();
            if (bitmapConfig == null) {
                bitmapConfig = Bitmap.Config.ARGB_8888;
            }
            bitmap = bitmap.copy(bitmapConfig, true);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);//消除锯齿
            paint.setDither(true); //获取跟清晰的图像采样
            paint.setFilterBitmap(true);//过滤一些
            paint.setColor(Color.RED);
            paint.setTextSize((int) (2 * scale));
            paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);//阴影制作半径，x偏移量，y偏移量，阴影颜色
            Rect bounds = new Rect();
            paint.getTextBounds(mText, 0, mText.length(), bounds);
            int y = (bitmap.getHeight() + bounds.height()) / 4;
            int x = 0;

            canvas.drawText(mText, x * scale, y * scale, paint);
            canvas.drawText(str3, x * scale, y * scale + 25, paint);
//       canvas.drawText(mText, x * scale, 210, paint);
//       canvas.drawText(str3, x * scale,210 + 25 , paint);
            return bitmap;
        } catch (Exception e) {
            return null;
        }

    }
}