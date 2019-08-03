package com.xy.xylibrary.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.constellation.xylibrary.R;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.utils.TbsLog;

public class X5WebView extends WebView {

	private TextView title;
	private ProgressBar mPageLoadingProgressBar;
	private Context context;
	private TextView textView;
	public static final int MSG_OPEN_TEST_URL = 0;
	public static final int MSG_INIT_UI = 1;
	private final int mUrlStartNum = 0;
	private int mCurrentUrl = mUrlStartNum;
	private boolean mNeedTestPage = false;


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

						loadUrl(testUrl);


						mCurrentUrl++;
						break;
					case MSG_INIT_UI:
						SetWebview();
						break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			super.handleMessage(msg);
		}
	};
	private WebViewClient client = new WebViewClient() {
		/**
		 * 防止加载网页时调起系统浏览器
		 */
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	};

	@SuppressLint("SetJavaScriptEnabled")
	public X5WebView(Context arg0,TextView textView, AttributeSet arg1,ProgressBar mPageLoadingProgressBar) {
		super(arg0, arg1);
		this.setWebViewClient(client);
		this.mPageLoadingProgressBar = mPageLoadingProgressBar;
		this.textView = textView;
		initWebViewSettings();
		this.getView().setClickable(true);
	}

	private void initWebViewSettings() {
		try {
			WebSettings webSetting = this.getSettings();
			webSetting.setJavaScriptEnabled(true);
			webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
			webSetting.setAllowFileAccess(true);
			webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
			webSetting.setSupportZoom(true);
			webSetting.setBuiltInZoomControls(false);
			webSetting.setUseWideViewPort(true);
			webSetting.setSupportMultipleWindows(true);
			// webSetting.setLoadWithOverviewMode(true);
			webSetting.setAppCacheEnabled(true);
			// webSetting.setDatabaseEnabled(true);
			webSetting.setDomStorageEnabled(true);
			webSetting.setGeolocationEnabled(true);
			webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
			// webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
			webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
			// webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
			webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
			SetWebview();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
		// settings 的设计

	}
	private void initProgressBar() {
		// ProgressBar(getApplicationContext(),
		// null,
		// android.R.attr.progressBarStyleHorizontal);
		if(mPageLoadingProgressBar != null){
			mPageLoadingProgressBar.setMax(100);
			mPageLoadingProgressBar.setProgressDrawable(this.getResources()
					.getDrawable(R.drawable.color_progressbar));
		}
	}

	private void SetWebview(){
		try {
			initProgressBar();
			this.setWebViewClient(new WebViewClient() {
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
					if(mTestHandler != null){
						mTestHandler.sendEmptyMessageDelayed(MSG_OPEN_TEST_URL, 5000);// 5s?

					}
					if (Integer.parseInt(Build.VERSION.SDK) >= 16)

						if(textView != null && !TextUtils.isEmpty(view.getTitle())){
							textView.setText(view.getTitle());
						}

					/* mWebView.showLog("test Log"); */
				}
			});

			this.setWebChromeClient(new WebChromeClient() {
				@Override
				public void onProgressChanged(WebView webView, int i) {
					super.onProgressChanged(webView, i);
					if(mPageLoadingProgressBar != null){
						mPageLoadingProgressBar.setProgress(i);
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

				/**
				 * 全屏播放配置
				 */
				@Override
				public void onShowCustomView(View view,
											 IX5WebChromeClient.CustomViewCallback customViewCallback) {
					//				FrameLayout normalView = (FrameLayout) findViewById(R.id.web_filechooser);
					//				ViewGroup viewGroup = (ViewGroup) normalView.getParent();
					//				viewGroup.removeView(normalView);
					//				viewGroup.addView(view);
					//				myVideoView = view;
					//				myNormalView = normalView;
					//				callback = customViewCallback;
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

			this.setDownloadListener(new DownloadListener() {

				@Override
				public void onDownloadStart(final String arg0, String arg1, String arg2,
											String arg3, long arg4) {
					new AlertDialog.Builder(context)
							.setTitle("是否进行下载？")
							.setPositiveButton("是",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog,
															int which) {
											// TODO: 2017-5-6 处理下载事件
											Intent intent = new Intent(Intent.ACTION_VIEW);
											intent.addCategory(Intent.CATEGORY_BROWSABLE);
											intent.setData(Uri.parse(arg0));
											context.startActivity(intent);
										}
									})
							.setNegativeButton("否",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog,
															int which) {

										}
									})
							.setOnCancelListener(
									new DialogInterface.OnCancelListener() {

										@Override
										public void onCancel(DialogInterface dialog) {
											// TODO Auto-generated method stub
											Toast.makeText(
													context,
													"fake message: refuse download...",
													Toast.LENGTH_SHORT).show();
										}
									}).show();
				}
			});
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

	}

	public X5WebView(Context arg0) {
		super(arg0);
//		setBackgroundColor(85621);
	}


}
