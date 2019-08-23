package com.zt.rainbowweather.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class X5WebView extends WebView {
	TextView title;
	private OnScrollChangedCallback mOnScrollChangedCallback;

	private WebViewClient client = new WebViewClient() {
		/**
		 * 防止加载网页时调起系统浏览器
		 */
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	};
	public OnScrollChangedCallback getOnScrollChangedCallback() {
		return mOnScrollChangedCallback;
	}

	public void setOnScrollChangedCallback(
			final OnScrollChangedCallback onScrollChangedCallback) {
		mOnScrollChangedCallback = onScrollChangedCallback;
	}

	/**
	 * Impliment in the activity/fragment/view that you want to listen to the webview
	 */
	public interface OnScrollChangedCallback {
		void onScroll(int dx, int dy);
	}
	public int oldtsize = -1;
	private int SlideDistance = -1;
	@Override
	protected void onScrollChanged(final int l, final int t, final int oldl,
								   final int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (mOnScrollChangedCallback != null && t > oldt) {
			if(oldt > oldtsize){
				if(oldtsize == -1 || t - oldtsize > 100){
					SlideDistance = oldtsize;
					oldtsize = oldt;
					mOnScrollChangedCallback.onScroll(l - oldl, oldt);
				}

			}

		}
	}

	@SuppressLint("SetJavaScriptEnabled")
	public X5WebView(Context arg0, AttributeSet arg1) {
		super(arg0, arg1);
		this.setWebViewClient(client);
		// this.setWebChromeClient(chromeClient);
		// WebStorage webStorage = WebStorage.getInstance();
		initWebViewSettings();
		this.getView().setClickable(true);
	}

	private void initWebViewSettings() {
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

		// this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
		// settings 的设计

	}


	public X5WebView(Context arg0) {
		super(arg0);
		setBackgroundColor(85621);
	}


}
