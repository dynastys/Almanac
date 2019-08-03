package com.zt.rainbowweather.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zt.weather.R;
import com.xy.xylibrary.base.BaseActivity;
import com.xy.xylibrary.base.BaseFragment;
import com.xy.xylibrary.ui.adapter.FragmentPagerAdapter;
import com.xy.xylibrary.utils.Utils;
import com.zt.rainbowweather.ui.fragment.RecommendFragment;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author zw
 * @time 2019-3-8
 * 图集页面
 * */
public class AtlasActivity extends BaseActivity {

    @BindView(R.id.finish_file_head)
    ImageView finishFileHead;
    @BindView(R.id.file_head_title)
    TextView fileHeadTitle;
    @BindView(R.id.file_co_option)
    TextView fileCoOption;
    @BindView(R.id.tablayout_vp)
    TabLayout tablayoutVp;
    @BindView(R.id.viewpager_atlas)
    ViewPager viewpagerAtlas;
    @BindView(R.id.list_bar)
    TextView listBar;

    private List<BaseFragment> mFragment;
    private String[] title = new String[]{"推荐"};

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("AtlasActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this); //统计时长
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("AtlasActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)，必须保证 onPageEnd 在 onPause 之前调用，因为SDK会在 onPause 中保存onPageEnd统计到的页面数据。
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Activity getContext() {
        return AtlasActivity.this;
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_atlas;
    }

    @Override
    protected void loadViewLayout() {
        try {
            MobclickAgent.onEvent(AtlasActivity.this,"bg_seting");
            ViewGroup.LayoutParams layoutParams = listBar.getLayoutParams();
            layoutParams.height = Utils.getStatusBarHeight(AtlasActivity.this);
            listBar.setLayoutParams(layoutParams);
            finishFileHead.setVisibility(View.VISIBLE);
            fileHeadTitle.setText("天气背景");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void bindViews() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        try {
            tablayoutVp.addTab(tablayoutVp.newTab().setText("推荐"));
//        tablayoutVp.addTab(tablayoutVp.newTab().setText("分类"));
//            tablayoutVp.addTab(tablayoutVp.newTab().setText("已下载"));
            tablayoutVp.setVisibility(View.GONE);
            mFragment = new ArrayList<>();
            mFragment.add(new RecommendFragment());
//        mFragment.add(new ClassifyFragment());
//            mFragment.add(new HaveDownFragment());
            viewpagerAtlas.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                @Override
                public BaseFragment getItem(int position) {
                    return mFragment.get(position);
                }

                @Override
                public int getCount() {
                    return mFragment.size();
                }
                @Override
                public CharSequence getPageTitle(int position) {
                    return title[position];
                }
            });
            viewpagerAtlas.setOffscreenPageLimit(2);
            tablayoutVp.setupWithViewPager(viewpagerAtlas);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
