package com.zt.rainbowweather.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.umeng.analytics.MobclickAgent;
import com.zt.rainbowweather.view.X5WebView;
import com.zt.weather.R;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebViewClient;
import com.umeng.message.PushAgent;
import com.xy.xylibrary.base.BaseActivity;
import com.zt.rainbowweather.utils.DeeplinkUtils;
import java.util.List;
import butterknife.BindView;
import static com.zt.xuanyin.view.AdLinkActivity.deviceCanHandleIntent;
import static com.zt.xuanyin.view.AdLinkActivity.isDeepLink;

/**
 * @author zw
 * @time 2019-3-8
 * 新闻资讯周公解梦落地页详情
 * */
public class AdviseMoreDetailActivity extends BaseActivity {

    @BindView(R.id.detail_toolbar)
    Toolbar detail_toolbar;
    @BindView(R.id.detail_progress)
    ProgressBar detail_progress;
    @BindView(R.id.webView_url)
    FrameLayout mViewParent;

    private static X5WebView mWebView;
    private String url;
    private String title;
    public static final int MSG_OPEN_TEST_URL = 0;
    public static final int MSG_INIT_UI = 1;
    private final int mUrlStartNum = 0;
    private int mCurrentUrl = mUrlStartNum;
    private boolean mNeedTestPage = false;

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("AdviseMoreDetailActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this); //统计时长
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("AdviseMoreDetailActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)，必须保证 onPageEnd 在 onPause 之前调用，因为SDK会在 onPause 中保存onPageEnd统计到的页面数据。
        MobclickAgent.onPause(this);
    }

    @SuppressLint("HandlerLeak")
    private Handler mTestHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case MSG_OPEN_TEST_URL:
                        if (!mNeedTestPage) {
                            return;
                        }
                        String testUrl = "file:///sdcard/outputHtml/html/"
                                + Integer.toString(mCurrentUrl) + ".html";
                        if (mWebView != null) {
                            mWebView.loadUrl(testUrl);
                        }
                        mCurrentUrl++;
                        break;
                    case MSG_INIT_UI:
                        loadViewLayout(1);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.handleMessage(msg);
        }
    };

        @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView != null && mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                finish();
                return false;
            }
        }
        return true;
    }

    public static void startActivity(Activity context, String title, String url) {
        Intent intent = new Intent(context, AdviseMoreDetailActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        context.overridePendingTransition(com.constellation.xylibrary.R.anim.in_from_right, com.constellation.xylibrary.R.anim.out_to_left);
        context.startActivity(intent);
    }
    @Override
    public void onDestroy() {
        if (mTestHandler != null)
            mTestHandler.removeCallbacksAndMessages(null);
        if (mWebView != null)
            mWebView.destroy();
        super.onDestroy();
    }

    @Override
    protected void loadViewLayout() {
        mTestHandler.sendEmptyMessageDelayed(MSG_INIT_UI, 10);
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


    protected void loadViewLayout(int v) {
         detail_toolbar.setTitle(title);
        setSupportActionBar(detail_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mWebView = new X5WebView(this, null);
        mViewParent.addView(mWebView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.FILL_PARENT,
                FrameLayout.LayoutParams.FILL_PARENT));
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView view, String url) {
                Log.e("url111", "shouldOverrideUrlLoading: "+url );
                if(!TextUtils.isEmpty(url)) {
                    if(DeeplinkUtils.getDeeplinkUtils().CanOpenDeeplink(AdviseMoreDetailActivity.this, url)){
                        if(url.startsWith("http://") || url.startsWith("https://")){
                            view.loadUrl(url);
                        }else{
                            DeeplinkUtils.getDeeplinkUtils().OpenDeeplink(AdviseMoreDetailActivity.this, url);
                        }
                    }else{
                        //隐士意图
                        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri
                                .parse(url));
                        if (isDeepLink(url) && deviceCanHandleIntent(getApplicationContext(), intent)) {
                            startActivity(intent);
                            finish();
                        }
                    }
                }
                return false;
            }

            @Override
            public void onReceivedSslError(com.tencent.smtt.sdk.WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                sslErrorHandler.proceed();//接受信任所有网站的证书
            }

            @Override
            public void onPageFinished(com.tencent.smtt.sdk.WebView view, String url) {
                 super.onPageFinished(view, url);
                mTestHandler.sendEmptyMessageDelayed(MSG_OPEN_TEST_URL, 5000);// 5s?
                if (Integer.parseInt(Build.VERSION.SDK) >= 16)
                    if (detail_toolbar != null && !TextUtils.isEmpty(view.getTitle())) {
                        detail_toolbar.setTitle(view.getTitle());
                    }
            }
        });

        mWebView.setWebChromeClient(new com.tencent.smtt.sdk.WebChromeClient() {
            @Override
            public void onProgressChanged(com.tencent.smtt.sdk.WebView webView, int i) {
                super.onProgressChanged(webView, i);
                 if (detail_progress != null){
                    detail_progress.setProgress(i);
                    if(i == 100){
                        detail_progress.setVisibility(View.GONE);
                    }else{
                        detail_progress.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public boolean onJsConfirm(com.tencent.smtt.sdk.WebView arg0, String arg1, String arg2,
                                       JsResult arg3) {
                return super.onJsConfirm(arg0, arg1, arg2, arg3);
            }
            View myVideoView;
            View myNormalView;
            IX5WebChromeClient.CustomViewCallback callback;
            /**
             * 全屏播放配置
             */
            @Override
            public void onShowCustomView(View view,
                                         IX5WebChromeClient.CustomViewCallback customViewCallback) {
            }

            @Override
            public void onHideCustomView() {
                try {
                    if (callback != null) {
                        callback.onCustomViewHidden();
                        callback = null;
                    }
                    if (myVideoView != null) {
                        ViewGroup viewGroup = (ViewGroup) myVideoView.getParent();
                        viewGroup.removeView(myVideoView);
                        viewGroup.addView(myNormalView);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mWebView.setDownloadListener((arg0, arg1, arg2, arg3, arg4) -> new AlertDialog.Builder(AdviseMoreDetailActivity.this)
                .setTitle("是否进行下载？")
                .setPositiveButton("是",
                        (dialog, which) -> {
                            // TODO: 2017-5-6 处理下载事件
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse(arg0));
                            startActivity(intent);
                        })
                .setNegativeButton("否",
                        (dialog, which) -> {
                        })
                .setOnCancelListener(
                        dialog -> {
                        }).show());
        WebSettings webSetting = mWebView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(false);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        Log.e("url111", "shouldOverrideUrlLoading:111 "+ url);
        mWebView.loadUrl(url);
        CookieSyncManager.createInstance(AdviseMoreDetailActivity.this);
        CookieSyncManager.getInstance().sync();

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
