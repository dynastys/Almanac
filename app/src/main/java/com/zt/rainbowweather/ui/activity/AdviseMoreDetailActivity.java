package com.zt.rainbowweather.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.chenguang.weather.R;
import com.umeng.message.PushAgent;
import com.xy.xylibrary.base.BaseActivity;

import java.util.List;

import butterknife.BindView;

public class AdviseMoreDetailActivity extends BaseActivity {

    @BindView(R.id.detail_toolbar)
    Toolbar detail_toolbar;
    @BindView(R.id.detail_progress)
    ProgressBar detail_progress;
    @BindView(R.id.detail_web)
    WebView detail_web;
    private String url;
    private String title;

    public static void startActivity(Context context, String title, String url) {
        Intent intent = new Intent(context, AdviseMoreDetailActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
    }

    @Override
    protected Activity getContext() {
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        return AdviseMoreDetailActivity.this;
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_advise_more_detail;
    }

    @Override
    protected void loadViewLayout() {
        //toolbar
        detail_toolbar.setTitle(title);
        setSupportActionBar(detail_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //webView
        WebSettings settings = detail_web.getSettings();
        //是否支持"视口"("ViewPort")
        settings.setUseWideViewPort(true);
        //设置是否在概述模式，即WebView加载页面，缩小内容以适合屏幕宽度
        settings.setLoadWithOverviewMode(true);
        //是否启用JavaScript的执行，默认false
        settings.setJavaScriptEnabled(true);
        //设置是否启用应用程序缓存的应用程序缓存,默认false
        settings.setAppCacheEnabled(true);
        //设置基本布局算法,默认NARROW_COLUMNS
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        //设置是否WebView应该支持缩放使用屏幕上的变焦控制和手势,默认true
        settings.setSupportZoom(true);
//        //开启硬件加速
//        settings.setLa
//        settings.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        //WebView加载给定的url
        detail_web.loadUrl(url);
        //解析，渲染网页
        detail_web.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                detail_web.setWebChromeClient(new MyWebChromeClient());
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
    }

    @Override
    protected void bindViews() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    protected void setListener() {

    }

    @Override
    public void granted() {

    }

    @Override
    public void denied(List<String> deniedList) {

    }

    /**
     * 设置打开网页时的进度条显示
     */
    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //当进度newProgress == 100时,隐藏进度条
            if (detail_progress != null) {
                if (newProgress == 100) {
                    detail_progress.setVisibility(View.GONE);
                } else {
                    //判断进度条的状态
                    if (detail_progress.getVisibility() == View.GONE) {
                        detail_progress.setVisibility(View.VISIBLE);
                    }
                    //设置进度条进度值
                    detail_progress.setProgress(newProgress);
                }
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
