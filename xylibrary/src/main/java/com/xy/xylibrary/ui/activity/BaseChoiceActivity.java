package com.xy.xylibrary.ui.activity;

import android.app.Activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.constellation.xylibrary.R;
import com.xy.xylibrary.base.BaseActivity;
import com.xy.xylibrary.ui.adapter.GradientTabStripAdapter;
import com.xy.xylibrary.utils.RomUtils;
import com.xy.xylibrary.utils.SaveShare;
import com.xy.xylibrary.view.ViewPagerSlide;
import am.widget.basetabstrip.BaseTabStrip;
import am.widget.gradienttabstrip.GradientTabStrip;

/**
 * zw
 * 主页面
 */
public abstract class BaseChoiceActivity extends BaseActivity implements ViewPager.OnPageChangeListener, BaseTabStrip.OnItemClickListener{


    private ViewPagerSlide tabVp;
    private GradientTabStrip gtsGtsTabs;
    private GradientTabStripAdapter adapter;
    private String Clipboard;


    @Override
    protected Activity getContext() {
        return BaseChoiceActivity.this;
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_base_choice;
    }


    /**
     * find控件
     */
    @Override
    protected void bindViews() {
        tabVp = findViewById(R.id.tab_vp);
        gtsGtsTabs = findViewById(R.id.gts_gts_tabs);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && gtsGtsTabs != null)
        {
            SaveShare.saveValue(this,"Height",""+ gtsGtsTabs.getHeight());

        }
    }
    public abstract GradientTabStripAdapter setGradientTabStripAdapter(FragmentManager fragmentManager,ViewPagerSlide tabVp);

    /**
     * 处理数据
     */
    @Override
    protected void processLogic(Bundle savedInstanceState) {
        try {
            if(adapter == null){
                adapter = setGradientTabStripAdapter(getSupportFragmentManager(),tabVp);
                tabVp.setAdapter(adapter);
                tabVp.setSlide(false);
                gtsGtsTabs.setAdapter(adapter);
                tabVp.addOnPageChangeListener(this);
                if(RomUtils.take_a_look){
                    tabVp.setOffscreenPageLimit(4);
                }else{
                    tabVp.setOffscreenPageLimit(3);
                }
                gtsGtsTabs.bindViewPager(tabVp);
                gtsGtsTabs.setOnItemClickListener(this);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
//        UpdateDialog();
    }

    public GradientTabStripAdapter getAdapter(){
       return adapter;
    }

    /**
     * 设置监听
     */
    @Override
    protected void setListener() {
    }

    @Override
    public void onItemClick(int position) {
     }

    @Override
    public void onSelectedClick(int position) {
     }

    @Override
    public void onDoubleClick(int position) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
     }

    @Override
    public void onPageSelected(int position) {
     }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            showToast("再按一次退出");
            exitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }


 }