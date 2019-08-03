package com.zt.rainbowweather.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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
 * 图集分类（暂时弃用）
 * */
public class ClassifyActivity extends BaseActivity {

    @BindView(R.id.list_bar)
    TextView listBar;
    @BindView(R.id.finish_file_head)
    ImageView finishFileHead;
    @BindView(R.id.file_head_title)
    TextView fileHeadTitle;
    @BindView(R.id.file_co_option)
    TextView fileCoOption;
    @BindView(R.id.list_recycler)
    RecyclerView listRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ClassifyActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this); //统计时长
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ClassifyActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)，必须保证 onPageEnd 在 onPause 之前调用，因为SDK会在 onPause 中保存onPageEnd统计到的页面数据。
        MobclickAgent.onPause(this);
    }

    @Override
    protected Activity getContext() {
        return ClassifyActivity.this;
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_classify;
    }

    @Override
    protected void loadViewLayout() {
        ViewGroup.LayoutParams layoutParams = listBar.getLayoutParams();
        layoutParams.height = Utils.getStatusBarHeight(ClassifyActivity.this);
        listBar.setLayoutParams(layoutParams);
        finishFileHead.setVisibility(View.VISIBLE);
        fileHeadTitle.setText("天气背景");
    }

    @Override
    protected void bindViews() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
//      RecommendLogic.getRecommendLogic().InitView(ClassifyActivity.this,listRecycler,atlasListSwipeRefresh,true);
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
