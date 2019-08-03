package com.xy.xylibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.constellation.xylibrary.R;
import com.xy.xylibrary.Interface.RlSimpleTarget;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * 图片圆角处理
 * */
public class GlideUtil {

    private static GlideUtil glideUtil;

    public static GlideUtil getGlideUtil() {
        if (glideUtil == null) {
            synchronized (GlideUtil.class){
                if (glideUtil == null) {
                    glideUtil = new GlideUtil();
                }
            }
        }
        return glideUtil;
    }

    public void setImages(Context context, String url, ImageView imageView, int type) {
        try {
            //设置图片圆角角度
            RoundedCorners roundedCorners= new RoundedCorners(10);
            RequestOptions options = new RequestOptions().bitmapTransform(roundedCorners)
    //                .placeholder(R.drawable.image_bg)	//加载成功之前占位图     
                    .error(R.drawable.image_bg)//加载错误之后的错误图 
                    .dontAnimate()
    //                .fitCenter()  //让图片全充，
    //                .override(400,400)//指定图片的尺寸//指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）  .fitCenter() //指定图片的缩放类型为centerCrop（等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）  .centerCrop()               
    //                .circleCrop()//指定图片的缩放类型为centerCrop （圆形）     
    //                .skipMemoryCache(true)//跳过内存缓存         
    //                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存所有版本的图像   
    //                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过磁盘缓存   
    //                .diskCacheStrategy(DiskCacheStrategy.DATA);//只缓存原来分辨率的图片   
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);	//只缓存最终的图片     
            Glide.with(context).load(url) .apply(options).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setImages(Context context, Object url, ImageView imageView){
        RequestOptions options = new RequestOptions()
//                .placeholder(R.drawable.image_bg)	//加载成功之前占位图     
                .error(R.drawable.image_bg)//加载错误之后的错误图 
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);	//只缓存最终的图片     
        Glide.with(context).load(url).transition(
                withCrossFade()).apply(options).into(imageView);
    }

    public void setImages1(Context context, Object url, ImageView imageView){
        RequestOptions options = new RequestOptions()
//                .placeholder(R.drawable.image_bg)	//加载成功之前占位图     
                .error(R.drawable.image_bg)//加载错误之后的错误图 
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);	//只缓存最终的图片     
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    public void setDetailsImages(Context context, Object url, ImageView imageView){
        RequestOptions options = new RequestOptions()
//                .placeholder(R.drawable.image_bg)	//加载成功之前占位图     
                .error(R.drawable.image_bg)//加载错误之后的错误图 
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);	//只缓存最终的图片     
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    /**
     * 加载GRF
     * */
    public void setGifImages(Context context, String url, ImageView imageView){
              Glide.with(context).asGif().load(url).into(imageView);
     }
    /**
     * 加载GRF
     * */
    public void setGifImages(Context context, int url, ImageView imageView){
        Glide.with(context).asGif().load(url).into(imageView);
    }
     /**
      * 返回Bitmap
      * */
     public void getBitmapImages(final Context context, Object url, final RemoteViews rv){
         RequestOptions options = new RequestOptions()
//                 .placeholder(R.drawable.icon)	//加载成功之前占位图     
                 .error(R.drawable.icon)//加载错误之后的错误图 
                 .dontAnimate()
                 .diskCacheStrategy(DiskCacheStrategy.RESOURCE);	//只缓存最终的图片     
//         Glide.with(context).asBitmap().load(url).apply(options).centerCrop()
//                 .override(150, 150)
//                 .into(new SimpleTarget<Bitmap>() {
//                     @Override
//                     public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//
//                     }
//                 });
     }

     /**
      * 加载背景图片
      * */
     public void getDrawableImages(final Context context, Object url, final RlSimpleTarget rlSimpleTarget){
         SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
             @Override
             public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                 try {
                     rlSimpleTarget.onResourceReady(resource);
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
//                 rl.setBackground(resource);
             }
         };
         Glide.with(context).load(url).transition(
                 withCrossFade()).into(simpleTarget);

     }
}
