package com.zt.rainbowweather.presenter.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.chenguang.weather.R;
import com.xy.xylibrary.utils.GlideUtil;
import com.zt.rainbowweather.presenter.receiver.NotificationClickReceiver;
import com.zt.rainbowweather.ui.activity.MainActivity;
import com.zt.xuanyin.Interface.AdProtogenesisListener;
import com.zt.xuanyin.controller.Ad;
import com.zt.xuanyin.controller.NativeAd;
import com.zt.xuanyin.entity.model.Native;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * 通知栏
 */
public class NotificationAd implements AdProtogenesisListener {

    private static NotificationAd notificationAd;
    public static NativeAd nativelogic;
    private Context context;
    private Native aNative;

    public static NotificationAd getNotificationAd() {
        if (notificationAd == null) {
            synchronized (NotificationAd.class) {
                if (notificationAd == null) {
                    notificationAd = new NotificationAd();
                }
            }
        }
        return notificationAd;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setAd(Context context) {
        this.context = context;
        // 获得通知栏广告对象
        nativelogic = Ad.getAd().NativeAD(context, "98f8e423-02e0-49f5-989f-af46f5c59203", "28119d54-13f5-47df-855e-c3f92f1495ea", "67C53558D3E3485EA681EA21735A5003", this);

    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public void downloadImage(final Context context, final String url) {
        new getImageCacheAsyncTask(context).execute(url);

    }

    @Override
    public void onADReady(Native aNative) {
        this.aNative = aNative;
        downloadImage(context,aNative.infoIcon.get(0));
    }

    @Override
    public void onAdFailedToLoad(String s) {

    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class getImageCacheAsyncTask extends AsyncTask<String, Void, File> {
        private final Context context;

        public getImageCacheAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected File doInBackground(String... params) {
            String imgUrl =  params[0];
            try {
                return Glide.with(context)
                        .load(imgUrl)
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
            } catch (Exception ex) {
                Log.e("result", "onPostExecute:ex "+ex.toString() );
                return null;
            }
        }

        @Override
        protected void onPostExecute(File result) {
            if (result == null) {
                return;
            }
            try {
                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel mChannel = new NotificationChannel("001", "name", NotificationManager.IMPORTANCE_LOW);
                    mNotificationManager.createNotificationChannel(mChannel);
                }
                Intent clickIntent = new Intent(context, NotificationClickReceiver.class); //点击通知之后要发送的广播
                int id = (int) (System.currentTimeMillis() / 1000);
                PendingIntent contentIntent = PendingIntent.getBroadcast(context, id, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.list_item);
                rv.setTextViewText(R.id.commodity_name, aNative.title);//标题
                rv.setTextViewText(R.id.commodity_details, aNative.desc);//描叙
                rv.setOnClickPendingIntent(R.id.commodity_rel,contentIntent);
                Notification.Builder builder3 = new Notification.Builder(context);
                builder3.setContentIntent(contentIntent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    builder3.setChannelId("001");
                }
                builder3.setSmallIcon(R.mipmap.ic_launcher);
                builder3.setLargeIcon(decodeFile(result.getPath()));
                builder3.setAutoCancel(true);
                builder3.setContentTitle( aNative.title);
                builder3.setContentText(aNative.desc);
                mNotificationManager.notify(1, builder3.build());
                nativelogic.AdShow(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e("result", "onPostExecute: "+result.toString() );
        }
    }

    /**
     * 根据 路径 得到 file 得到 bitmap
     * @param filePath
     * @return
     * @throws IOException
     */
    public Bitmap decodeFile(String filePath) throws IOException {
        Bitmap b = null;
        int IMAGE_MAX_SIZE = 600;

        File f = new File(filePath);
        if (f == null){
            return null;
        }
        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis = new FileInputStream(f);
        BitmapFactory.decodeStream(fis, null, o);
        fis.close();

        int scale = 1;
        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
            scale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        fis = new FileInputStream(f);
        b = BitmapFactory.decodeStream(fis, null, o2);
        fis.close();
        return b;
    }

}
