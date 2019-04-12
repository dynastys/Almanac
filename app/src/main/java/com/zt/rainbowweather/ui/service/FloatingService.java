package com.zt.rainbowweather.ui.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.chenguang.weather.R;
import com.zt.xuanyin.Interface.AdProtogenesisListener;
import com.zt.xuanyin.controller.Ad;
import com.zt.xuanyin.controller.NativeAd;
import com.zt.xuanyin.entity.model.Native;


public class FloatingService extends Service {

    private WindowManager manager;
    private WindowManager.LayoutParams params;
    private View view;
    private NativeAd nativelogic;
    private float ClickX;
    private float ClickY;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showFloatingView();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private boolean nOpen = true;
    /**
     * 悬浮窗控件可以是任意的View的子类类型
     */
    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showFloatingView() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                nOpen = Settings.canDrawOverlays(getApplicationContext());
            }
            if (nOpen) {
                manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                view = View.inflate(getApplicationContext(), R.layout.dialog, null);
                CardView dialog = view.findViewById(R.id.dialog);
                ImageView interstitial = view.findViewById(R.id.dialog_image);
                RelativeLayout interstitialContainer = view.findViewById(R.id.dialog_rel);
                ImageView cancel = view.findViewById(R.id.dialog_cancel);
                // 获得开屏广告对象
                nativelogic = new Ad().NativeAD(this, "0bdd768a-aad0-4105-ac66-95a27c0516de", "7127e520-1e95-4141-bb5c-ce96d6771186","CD028F25B3274D7F9AEA0BBE4F449F6A", new AdProtogenesisListener() {
                    @Override
                    public void onADReady(Native url) {
                        dialog.setVisibility(View.VISIBLE);
                        Glide.with(getApplicationContext()).load(url.src).into(interstitial);
                        nativelogic.AdShow(interstitialContainer);
                        cancel.setImageDrawable(getDrawable(R.drawable.ic_clear_black));
                    }

                    @Override
                    public void onAdFailedToLoad(String error) {
                        manager.removeView(view);
                    }
                });
                cancel.setOnClickListener(view1 -> manager.removeView(view));
                interstitial.setOnTouchListener((v, event) -> {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            ClickX = event.getRawX();
                            ClickY = event.getRawY();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            break;
                        case MotionEvent.ACTION_UP:
                            if(Math.abs(event.getRawX() - ClickX) < 20 && Math.abs(event.getRawY() - ClickY) < 20){
                                try {
                                    //点击上报
                                    nativelogic.OnClick(interstitialContainer);
                                    manager.removeView(view);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            break;
                    }
                    return true;
                });
                //设置layoutParams
                params = new WindowManager.LayoutParams();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                } else {
                    params.type = WindowManager.LayoutParams.TYPE_PHONE;
                }
                params.format = PixelFormat.RGBA_8888;
                params.width = WindowManager.LayoutParams.WRAP_CONTENT;
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;

                //设置不阻挡其他view的触摸事件
                params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
    //            params.x = 500;
    //            params.y = 300;
    //            params.gravity = Gravity.CENTER_HORIZONTAL;

                //添加view到windowManager
                manager.addView(view, params);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
