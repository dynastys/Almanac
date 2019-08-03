package com.zt.rainbowweather.ui.activity;

import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.zt.rainbowweather.entity.calendar.HuangLi;
import com.zt.rainbowweather.ui.fragment.YiJiFragment;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author zw
 * @time 2019-3-8
 * 宜忌页面
 * */
public class YiJiActivity extends BaseActivity {

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
    private HuangLi huangLis;
    private List<BaseFragment> mFragment;
    private String[] title = new String[]{"宜", "忌"};
    private String YI_JI;


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("YiJiActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this); //统计时长
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("YiJiActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)，必须保证 onPageEnd 在 onPause 之前调用，因为SDK会在 onPause 中保存onPageEnd统计到的页面数据。
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Activity getContext() {
        return YiJiActivity.this;
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_atlas;
    }

    @Override
    protected void loadViewLayout() {
        try {
            huangLis = (HuangLi) getIntent().getSerializableExtra("huangLis");
            YI_JI = getIntent().getStringExtra("yi_ji");
            ViewGroup.LayoutParams layoutParams = listBar.getLayoutParams();
            layoutParams.height = Utils.getStatusBarHeight(YiJiActivity.this);
            listBar.setLayoutParams(layoutParams);
            finishFileHead.setVisibility(View.VISIBLE);
            fileHeadTitle.setText("黄历");
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
            tablayoutVp.addTab(tablayoutVp.newTab().setText("宜"));
            tablayoutVp.addTab(tablayoutVp.newTab().setText("忌"));
            mFragment = new ArrayList<>();

            YiJiFragment yiJiFragment1 = new YiJiFragment(huangLis.getData().getYi());
            mFragment.add(yiJiFragment1);
            YiJiFragment yiJiFragment2 = new YiJiFragment(huangLis.getData().getJi());
            mFragment.add(yiJiFragment2);

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
            if(TextUtils.isEmpty(YI_JI)){
                viewpagerAtlas.setCurrentItem(0);
            }else{
                switch (YI_JI){
                    case "宜":
                        viewpagerAtlas.setCurrentItem(0);
                        break;
                    case "忌":
                        viewpagerAtlas.setCurrentItem(1);
                        break;
                }
            }
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