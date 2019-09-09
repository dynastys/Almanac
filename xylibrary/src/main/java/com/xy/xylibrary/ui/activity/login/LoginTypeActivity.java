package com.xy.xylibrary.ui.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.constellation.xylibrary.R;
import com.umeng.analytics.MobclickAgent;
import com.xy.xylibrary.base.AppContext;
import com.xy.xylibrary.base.BaseActivity;
import com.xy.xylibrary.utils.Utils;
import com.xy.xylibrary.view.MagicButton;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginTypeActivity extends BaseActivity implements View.OnClickListener{


    private TextView statusBarType;
    private MagicButton magicBtn;
    private TextView tvVersionName;
    private LinearLayout wechatLogin;
    private LinearLayout phoneLogin;
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MainActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this); //统计时长
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MainActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)，必须保证 onPageEnd 在 onPause 之前调用，因为SDK会在 onPause 中保存onPageEnd统计到的页面数据。
        MobclickAgent.onPause(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Activity getContext() {
        return LoginTypeActivity.this;
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_login_type;
    }

    @Override
    protected void loadViewLayout() {
        try {
//            setIsUserLightMode(true);
            statusBarType = findViewById(R.id.status_bar_type);
            magicBtn = findViewById(R.id.magic_btn);
//            tvVersionName = findViewById(R.id.tv_version_name);
            wechatLogin = findViewById(R.id.wechat_login);
            phoneLogin = findViewById(R.id.phone_login);
            magicBtn.setOnClickListener(LoginTypeActivity.this);
            wechatLogin.setOnClickListener(LoginTypeActivity.this);
            phoneLogin.setOnClickListener(LoginTypeActivity.this);
            ViewGroup.LayoutParams layoutParams = statusBarType.getLayoutParams();
            layoutParams.height = Utils.getStatusBarHeight(LoginTypeActivity.this);
            statusBarType.setLayoutParams(layoutParams);
            setIsUserLightMode(true);
//            tvVersionName.setText("星云天气 "+Utils.getAppInfo(LoginTypeActivity.this));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.magic_btn) {
            finish();
        } else if (i == R.id.wechat_login) {
            AppContext.ISLOGIN = true;
            AppContext.wxLogin();
            finish();
        } else if (i == R.id.phone_login) {
            Intent intent = new Intent(LoginTypeActivity.this, LoginActivity.class);
            intent.putExtra("type", "手机");
            startActivity(intent);
            finish();
        }
    }
}
