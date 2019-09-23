package com.zt.rainbowweather.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.xy.xylibrary.base.BaseActivity;
import com.xy.xylibrary.utils.SaveShare;
import com.zt.rainbowweather.BasicApplication;
import com.zt.rainbowweather.presenter.home.WeatherPageData;
import com.zt.rainbowweather.view.guidepage.BGABanner;
import com.zt.rainbowweather.view.guidepage.BGALocalImageSize;
import com.zt.weather.R;

import java.util.List;


public class GuideActivity extends BaseActivity {
    private static final String TAG = GuideActivity.class.getSimpleName();
    private BGABanner mBackgroundBanner;
    private BGABanner mForegroundBanner;


    @Override
    protected Activity getContext() {
        return GuideActivity.this;
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void loadViewLayout() {
        initView();
        setListener();
        processLogic();
        if(BasicApplication.getLocatedCity() != null){
            new WeatherPageData(GuideActivity.this).ConventionWeatherData(BasicApplication.getLocatedCity());
        }
    }

    @Override
    protected void bindViews() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    private void initView() {
        mBackgroundBanner = findViewById(R.id.banner_guide_background);
        mForegroundBanner = findViewById(R.id.banner_guide_foreground);
    }

    public void setListener() {
        /**
         * 设置进入按钮和跳过按钮控件资源 id 及其点击事件
         * 如果进入按钮和跳过按钮有一个不存在的话就传 0
         * 在 BGABanner 里已经帮开发者处理了防止重复点击事件
         * 在 BGABanner 里已经帮开发者处理了「跳过按钮」和「进入按钮」的显示与隐藏
         */
        mForegroundBanner.setEnterSkipViewIdAndDelegate(R.id.btn_guide_enter, R.id.tv_guide_skip, new BGABanner.GuideDelegate() {
            @Override
            public void onClickEnterOrSkip() {
                SaveShare.saveValue(GuideActivity.this,"Guide","Guide");
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void processLogic() {
        // Bitmap 的宽高在 maxWidth maxHeight 和 minWidth minHeight 之间
        BGALocalImageSize localImageSize = new BGALocalImageSize(720, 1280, 320, 640);
//        // 设置数据源
//        mBackgroundBanner.setData(localImageSize, ImageView.ScaleType.CENTER_CROP,
//                R.drawable.uoko_guide_background_1,
//                R.drawable.uoko_guide_background_2,
//                R.drawable.uoko_guide_background_3);
        mForegroundBanner.setData(localImageSize, ImageView.ScaleType.CENTER_CROP,
                R.drawable.uoko_guide_background_1,
                R.drawable.uoko_guide_background_3,
                R.drawable.uoko_guide_background_4
                );
    }

    @Override
    public void onResume() {
        super.onResume();
        // 如果开发者的引导页主题是透明的，需要在界面可见时给背景 Banner 设置一个白色背景，避免滑动过程中两个 Banner 都设置透明度后能看到 Launcher
        mBackgroundBanner.setBackgroundResource(android.R.color.white);
    }

    @Override
    public void granted() {

    }

    @Override
    public void denied(List<String> deniedList) {

    }
}