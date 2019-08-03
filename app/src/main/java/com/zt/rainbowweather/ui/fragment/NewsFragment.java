package com.zt.rainbowweather.ui.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.zt.weather.R;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.umeng.analytics.MobclickAgent;
import com.xy.xylibrary.base.BaseFragment;
import com.xy.xylibrary.utils.Utils;
import com.xy.xylibrary.view.X5WebView;
import com.zt.rainbowweather.entity.background.IsUserLight;
import org.greenrobot.eventbus.EventBus;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author zw
 * @time 2019-3-8
 * 新闻（h5）
 */
public class NewsFragment extends BaseFragment {

    @BindView(R.id.list_bar)
    TextView listBar;
    @BindView(R.id.finish_file_head)
    ImageView finishFileHead;
    @BindView(R.id.file_head_title)
    TextView fileHeadTitle;
    @BindView(R.id.file_co_option)
    TextView fileCoOption;
    @BindView(R.id.progressBar1)
    ProgressBar mPageLoadingProgressBar;
    @BindView(R.id.webView1)
    FrameLayout mViewParent;
    Unbinder unbinder;

    private static X5WebView mWebView;
    private String mHomeUrl = "http://ssp.xuanad.com/page/f1e294cc-8546-438b-b0af-801ca170b2d0";
    public static final int MSG_OPEN_TEST_URL = 0;
    public static final int MSG_INIT_UI = 1;
    private final int mUrlStartNum = 0;
    private int mCurrentUrl = mUrlStartNum;
    private boolean mNeedTestPage = false;
    private boolean ISFirst = true;

    public NewsFragment() {

    }

    @SuppressLint("ValidFragment")
    public NewsFragment(String mHomeUrl) {
        this.mHomeUrl = mHomeUrl;
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
//                        String testUrl = "file:///sdcard/outputHtml/html/"
//                                + Integer.toString(mCurrentUrl) + ".html";
//                        if (mWebView != null) {
//                            mWebView.loadUrl(testUrl);
//                        }
//                        mCurrentUrl++;
                        break;
                    case MSG_INIT_UI:
                        initData();
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_browser;
    }

    private void initProgressBar() {
        mPageLoadingProgressBar.setMax(100);
        mPageLoadingProgressBar.setProgressDrawable(this.getResources()
                .getDrawable(R.drawable.color_progressbar));
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        EventBus.getDefault().post(new IsUserLight(false));
        if(isVisibleToUser){
            MobclickAgent.onEvent(getActivity(),"k_y_k");
        }
        if (isVisibleToUser && mWebView != null && ISFirst) {
            ISFirst = false;
            initData();
        }
    }

    private void initData(){
        try {
            if(mViewParent != null && mWebView != null){
                mWebView = new X5WebView(getActivity());
            }
            mViewParent.addView(mWebView, new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.FILL_PARENT,
                    FrameLayout.LayoutParams.FILL_PARENT));
            initProgressBar();
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return false;
                }

                @Override
                public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {

                    sslErrorHandler.proceed();//接受信任所有网站的证书
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    // mTestHandler.sendEmptyMessage(MSG_OPEN_TEST_URL);
                    mTestHandler.sendEmptyMessageDelayed(MSG_OPEN_TEST_URL, 5000);// 5s?
                    if (Integer.parseInt(Build.VERSION.SDK) >= 16)
                        if (fileHeadTitle != null && !TextUtils.isEmpty(view.getTitle())) {
                            fileHeadTitle.setText(view.getTitle());
                        }
                    /* mWebView.showLog("test Log"); */
                }
            });


            mWebView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView webView, int i) {
                    super.onProgressChanged(webView, i);
                    if (mPageLoadingProgressBar != null) {
                        mPageLoadingProgressBar.setProgress(i);
                        if(i == 100){
                            mPageLoadingProgressBar.setVisibility(View.GONE);
                        }else{
                            mPageLoadingProgressBar.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public boolean onJsConfirm(WebView arg0, String arg1, String arg2,
                                           JsResult arg3) {
                    return super.onJsConfirm(arg0, arg1, arg2, arg3);
                }

                View myVideoView;
                View myNormalView;
                IX5WebChromeClient.CustomViewCallback callback;

                // /////////////////////////////////////////////////////////
                //

                //
                /**
                 * 全屏播放配置
                 */
                @Override
                public void onShowCustomView(View view,
                                             IX5WebChromeClient.CustomViewCallback customViewCallback) {
                    ViewGroup viewGroup = (ViewGroup) mViewParent.getParent();
                    viewGroup.removeView(mViewParent);
                    viewGroup.addView(view);
                    myVideoView = view;
                    myNormalView = mViewParent;
                    callback = customViewCallback;
                }

                @Override
                public void onHideCustomView() {
                    if (callback != null) {
                        callback.onCustomViewHidden();
                        callback = null;
                    }
                    if (myVideoView != null) {
                        ViewGroup viewGroup = (ViewGroup) myVideoView.getParent();
                        viewGroup.removeView(myVideoView);
                        viewGroup.addView(myNormalView);
                    }
                }
                @Override
                public boolean onJsAlert(WebView arg0, String arg1, String arg2,
                                         JsResult arg3) {
                    /**
                     * 这里写入你自定义的window alert
                     */
                    return super.onJsAlert(null, arg1, arg2, arg3);
                }
            });

            mWebView.setDownloadListener((arg0, arg1, arg2, arg3, arg4) -> new AlertDialog.Builder(getActivity())
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
                                // TODO Auto-generated method stub
                                Toast.makeText(
                                        getActivity(),
                                        "fake message: refuse download...",
                                        Toast.LENGTH_SHORT).show();
                            }).show());

            WebSettings webSetting = mWebView.getSettings();
            webSetting.setAllowFileAccess(true);
            webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            webSetting.setSupportZoom(false);
            webSetting.setBuiltInZoomControls(false);
            webSetting.setUseWideViewPort(true);
            webSetting.setSupportMultipleWindows(false);
            webSetting.setAppCacheEnabled(true);
            webSetting.setDomStorageEnabled(true);
            webSetting.setJavaScriptEnabled(true);
            webSetting.setGeolocationEnabled(true);
            webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
            webSetting.setAppCachePath(getActivity().getDir("appcache", 0).getPath());
            webSetting.setDatabasePath(getActivity().getDir("databases", 0).getPath());
            webSetting.setGeolocationDatabasePath(getActivity().getDir("geolocation", 0)
                    .getPath());
            webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
            mWebView.loadUrl(mHomeUrl);
            CookieSyncManager.createInstance(getActivity());
            CookieSyncManager.getInstance().sync();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initData(View view) {
        try {
            ViewGroup.LayoutParams layoutParams = listBar.getLayoutParams();
            layoutParams.height = Utils.getStatusBarHeight(getActivity());
            listBar.setLayoutParams(layoutParams);
            mWebView = new X5WebView(getActivity());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initListener() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        assert rootView != null;
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.finish_file_head)
    public void onViewClicked() {
    }


    public static boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView != null && mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                return false;
            }

        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("NewsFragment"); //统计页面("MainScreen"为页面名称，可自定义)
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("NewsFragment");
    }
}
