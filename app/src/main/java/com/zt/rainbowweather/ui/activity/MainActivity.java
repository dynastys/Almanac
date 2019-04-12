package com.zt.rainbowweather.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.chenguang.weather.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.timmy.tdialog.TDialog;
import com.timmy.tdialog.base.BindViewHolder;
import com.timmy.tdialog.listener.OnViewClickListener;
import com.umeng.message.PushAgent;
import com.xy.xylibrary.ui.activity.BaseChoiceActivity;
import com.xy.xylibrary.ui.adapter.GradientTabStripAdapter;
import com.xy.xylibrary.view.ViewPagerSlide;
import com.zt.rainbowweather.entity.City;
import com.zt.rainbowweather.entity.CityEvent;
import com.zt.rainbowweather.entity.city.Event;
import com.zt.rainbowweather.presenter.ExtraFunction;
import com.zt.rainbowweather.presenter.UpdatePort;
import com.zt.rainbowweather.ui.adapter.MyGradientTabStripAdapter;
import com.zt.rainbowweather.ui.fragment.NewsFragment;
import com.zt.rainbowweather.ui.service.FloatingService;
import com.zt.rainbowweather.utils.ConstUtils;
import com.zt.rainbowweather.utils.SPUtils;
import com.zt.rainbowweather.utils.utils;
import com.zt.xuanyin.Interface.AdProtogenesisListener;
import com.zt.xuanyin.controller.Ad;
import com.zt.xuanyin.controller.NativeAd;
import com.zt.xuanyin.entity.model.Native;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;
import java.util.List;


public class MainActivity extends BaseChoiceActivity implements OnViewClickListener {

    private int position;
    private MyGradientTabStripAdapter myGradientTabStripAdapter;
    private NativeAd nativelogic;
    private BindViewHolder MyviewHolder;
    private ExtraFunction extraFunction;
    private double hourse;

    @Override
    public GradientTabStripAdapter setGradientTabStripAdapter(FragmentManager fragmentManager, ViewPagerSlide tabVp) {
        myGradientTabStripAdapter = new MyGradientTabStripAdapter(fragmentManager, tabVp);
        return myGradientTabStripAdapter;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void loadViewLayout() {
        DialogShow();
        extraFunction = new ExtraFunction(MainActivity.this);
        extraFunction.AppListData();
        extraFunction.KeepAlive();
        extraFunction.NotificationBar();
        extraFunction.setNoninductive();
        UpdatePort.getUpdatePort().UpdateDialog(MainActivity.this);
        new com.zt.noninductive.Ad().NativeAD(this);
    }

    /*应用外弹窗*/
    private void floatWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                extraFunction.FloatingWindow(MainActivity.this,MainActivity.this);
            } else {

            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        this.position = position;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && position == 2) {
            if (((NewsFragment) myGradientTabStripAdapter.getItem(position)).onKeyDown(keyCode, event)) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setCityEvent(CityEvent cityEvent) {
        EventBus.getDefault().post(new Event(cityEvent.city,cityEvent.isDelete));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        extraFunction.onDestroy();
     }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        PushAgent.getInstance(this).onAppStart();



        long starttime = System.currentTimeMillis();
        SharedPreferences sharedPreferences =getSharedPreferences("TIME",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //读取
        long endtime =sharedPreferences.getLong("time",0);
        //保存
        editor.putLong("time",starttime);
        editor.commit();
        if (endtime != 0){
            long timeDiff = starttime - endtime;
            hourse = timeDiff / (1000 * 60 * 60 * 24);
        }else {
            floatWindow();
        }
        if (hourse>7){
            floatWindow();
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                startService(new Intent(MainActivity.this, FloatingService.class));
            }
        }
    }
    private List<City> getCityFromSP() {
        String json = SPUtils.getInstance(ConstUtils.SP_FILE_NAME).getString("addresses");
        if (!TextUtils.isEmpty(json)) {
            return new Gson().fromJson(json, new TypeToken<List<City>>() {
            }.getType());
        }
        return null;
    }

    @Override
    protected void onStop() {
        super.onStop();
         List<City> cities = getCityFromSP();
         if (cities != null){
             for (int j = 0; j < cities.size(); j++) {
                 cities.get(j).save();
             }
         }
        if(!utils.isForeground(MainActivity.this)){
            startService(new Intent(MainActivity.this, FloatingService.class));
        }
    }

    private void DialogShow() {
        try {
            // 获得开屏广告对象
            nativelogic = new Ad().NativeAD(this, "85890385-161e-4cfc-9d15-a2842ab86194", "7c6680c9-e304-49a6-a692-7efb2dd38224", "FA3967E6BBE243B6A3A6EAD8A42C98A4", new AdProtogenesisListener() {
                @Override
                public void onADReady(final Native url) {
                    if (url.src != null){
                        new TDialog.Builder(getSupportFragmentManager())
                                .setLayoutRes(R.layout.dialog)    //设置弹窗展示的xml布局
                                .setScreenWidthAspect(MainActivity.this, 0.6f)   //设置弹窗宽度(参数aspect为屏幕宽度比例 0 - 1f)
                                .setScreenHeightAspect(MainActivity.this, 0.4f)  //设置弹窗高度(参数aspect为屏幕宽度比例 0 - 1f)
                                .setCancelableOutside(false)
                                .setOnBindViewListener(viewHolder -> {
                                    MyviewHolder = viewHolder;
                                    ImageView imageView = viewHolder.getView(R.id.dialog_image);
                                    Glide.with(MainActivity.this).load(url.src).into(imageView);
                                })
                                .setOnDismissListener(dialog -> {
                                })
                                .addOnClickListener(R.id.dialog_image, R.id.dialog_cancel)
                                .setOnViewClickListener(MainActivity.this)
                                .setDimAmount(0.6f)     //设置弹窗背景透明度(0-1f)
                                .setDialogAnimationRes(R.style.animate_dialog)  //设置弹窗动画
                                .create()   //创建TDialog
                                .show();    //展示
                    }


                }

                @Override
                public void onAdFailedToLoad(String error) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
        switch (view.getId()) {
            case R.id.dialog_image:
                RelativeLayout relativeLayout = MyviewHolder.getView(R.id.dialog_rel);
                // 点击开屏广告处理
                nativelogic.OnClick(relativeLayout);
                tDialog.dismiss();
                break;
            case R.id.dialog_cancel:
                tDialog.dismiss();
                break;
            case R.id.dialog_confirm_tv:
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0);
                tDialog.dismiss();
                break;
            case R.id.dialog_cancel_tv:
                tDialog.dismiss();
                break;
        }
    }

    @Override
    public void granted() {

    }

    @Override
    public void denied(List<String> deniedList) {

    }
}
