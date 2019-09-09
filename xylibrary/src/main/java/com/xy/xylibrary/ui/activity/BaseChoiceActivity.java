package com.xy.xylibrary.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.constellation.xylibrary.R;
import com.xy.xylibrary.base.BaseActivity;
import com.xy.xylibrary.ui.adapter.GradientTabStripAdapter;
import com.xy.xylibrary.ui.fragment.task.TaskType;
import com.xy.xylibrary.utils.RomUtils;
import com.xy.xylibrary.utils.SaveShare;
import com.xy.xylibrary.utils.ToastUtils;
import com.xy.xylibrary.view.ViewPagerSlide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import am.widget.basetabstrip.BaseTabStrip;
import am.widget.gradienttabstrip.GradientTabStrip;

/**
 * zw
 * 主页面
 */
public abstract class BaseChoiceActivity extends BaseActivity implements ViewPager.OnPageChangeListener, BaseTabStrip.OnItemClickListener {


    private ViewPagerSlide tabVp;
    private GradientTabStrip gtsGtsTabs;
    private GradientTabStripAdapter adapter;
    private String Clipboard;
    private TaskType taskType;

    @Override
    protected Activity getContext() {
        return BaseChoiceActivity.this;
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_base_choice;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setCityEvent(TaskType taskType) {
        try {
            this.taskType = taskType;
            if (taskType.tasktype == 5 || taskType.tasktype == 3|| taskType.tasktype == 1) {
                if (tabVp != null) {
                    tabVp.setCurrentItem(0);
                }
            } else if (taskType.tasktype == 4) {
                if (tabVp != null) {
                    tabVp.setCurrentItem(3);
                    SaveShare.saveValue(BaseChoiceActivity.this,"SP","4");
                 }
            }else if (taskType.tasktype == 6) {
                if (tabVp != null) {
                    tabVp.setCurrentItem(1);
                    SaveShare.saveValue(BaseChoiceActivity.this,"HL","5");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        EventBus.getDefault().post(new Event(cityEvent.city, cityEvent.isDelete));
    }

    @SuppressLint("WrongConstant")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setTabStrip(String s) {
        if(adapter != null){
            adapter.isTagEnable(2);
            if(!TextUtils.isEmpty(s) && s.equals("1")){
                adapter.getSelectedDrawable(1,BaseChoiceActivity.this);
                adapter.getPageTitle(1);
//                tabVp.setCurrentItem(0);
            }else{
                adapter.getSelectedDrawable(0,BaseChoiceActivity.this);
                adapter.getPageTitle(0);
//                tabVp.setCurrentItem(0);
            }
//            adapter.notifyDataSetChanged();
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                gtsGtsTabs.setFocusable(0);
//            }

        }
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
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && gtsGtsTabs != null) {
            SaveShare.saveValue(this, "Height", "" + gtsGtsTabs.getHeight());

        }
    }

    public abstract GradientTabStripAdapter setGradientTabStripAdapter(FragmentManager fragmentManager, ViewPagerSlide tabVp);

    /**
     * 处理数据
     */
    @Override
    protected void processLogic(Bundle savedInstanceState) {
        try {
            if (adapter == null) {
                adapter = setGradientTabStripAdapter(getSupportFragmentManager(), tabVp);
                tabVp.setAdapter(adapter);
                tabVp.setSlide(false);
                gtsGtsTabs.setAdapter(adapter);
                tabVp.addOnPageChangeListener(this);
                if (RomUtils.take_a_look) {
                    tabVp.setOffscreenPageLimit(4);
                } else {
                    tabVp.setOffscreenPageLimit(3);
                }
                gtsGtsTabs.bindViewPager(tabVp);
                gtsGtsTabs.setOnItemClickListener(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        UpdateDialog();
    }

    public GradientTabStripAdapter getAdapter() {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
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