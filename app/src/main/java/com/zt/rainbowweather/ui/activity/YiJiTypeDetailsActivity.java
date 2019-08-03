package com.zt.rainbowweather.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zt.weather.R;
import com.xy.xylibrary.base.BaseActivity;
import com.xy.xylibrary.utils.Utils;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author zw
 * @time 2019-3-8
 * 宜忌详情
 * */
public class YiJiTypeDetailsActivity extends BaseActivity {

    @BindView(R.id.list_bar)
    TextView listBar;
    @BindView(R.id.finish_file_head)
    ImageView finishFileHead;
    @BindView(R.id.file_head_title)
    TextView fileHeadTitle;
    @BindView(R.id.file_co_option)
    TextView fileCoOption;
    @BindView(R.id.type_ad)
    ImageView typeAd;
    @BindView(R.id.type_note)
    TextView typeNote;
    @BindView(R.id.type_note_tv)
    TextView typeNoteTv;
    @BindView(R.id.underlying_word)
    TextView underlyingWord;
    @BindView(R.id.underlying_word_tv)
    TextView underlyingWordTv;
    @BindView(R.id.type_details)
    TextView typeDetails;

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("YiJiTypeDetailsActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this); //统计时长
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("YiJiTypeDetailsActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)，必须保证 onPageEnd 在 onPause 之前调用，因为SDK会在 onPause 中保存onPageEnd统计到的页面数据。
        MobclickAgent.onPause(this);
    }

    @Override
    protected Activity getContext() {
        return YiJiTypeDetailsActivity.this;
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_yi_ji_type_details;
    }

    @Override
    protected void loadViewLayout() {
        ViewGroup.LayoutParams layoutParams = listBar.getLayoutParams();
        layoutParams.height = Utils.getStatusBarHeight(YiJiTypeDetailsActivity.this);
        listBar.setLayoutParams(layoutParams);
        finishFileHead.setVisibility(View.VISIBLE);
        fileHeadTitle.setText("举办婚礼");
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

    @OnClick(R.id.finish_file_head)
    public void onClick() {
        finish();
    }
}
