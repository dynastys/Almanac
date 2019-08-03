package com.zt.rainbowweather.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zt.weather.R;
import com.xy.xylibrary.base.BaseActivity;
import com.xy.xylibrary.utils.Shares;
import com.xy.xylibrary.utils.Utils;
import com.zt.rainbowweather.presenter.ToolbarHelper;
import java.util.List;
import butterknife.BindView;
import butterknife.BuildConfig;
import butterknife.ButterKnife;

/**
 * @author zw
 * @time 2019-3-8
 * 关于
 */
public class AboutActivity extends BaseActivity {

    @BindView(R.id.status_bar_about)
    TextView statusBarAbout;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_about, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                Shares.share(this, R.string.shape);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected Activity getContext() {
        return AboutActivity.this;
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_about;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void loadViewLayout() {
        ViewGroup.LayoutParams layoutParams = statusBarAbout.getLayoutParams();
        layoutParams.height = Utils.getStatusBarHeight(AboutActivity.this);
        statusBarAbout.setLayoutParams(layoutParams);
        ToolbarHelper.initToolbar(this, R.id.toolbar, true, "关于星云");
        TextView textView = findViewById(R.id.tv_version_name);
        textView.setText(getString(R.string.app_name) + " " + BuildConfig.VERSION_NAME);
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
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("AboutActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this); //统计时长
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("AboutActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)，必须保证 onPageEnd 在 onPause 之前调用，因为SDK会在 onPause 中保存onPageEnd统计到的页面数据。
        MobclickAgent.onPause(this);
    }
}
