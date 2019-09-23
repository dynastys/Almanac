package com.xy.xylibrary.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.constellation.xylibrary.R;
import com.xy.xylibrary.base.BaseActivity;
import com.xy.xylibrary.ui.adapter.GradientTabStripAdapter;
import com.xy.xylibrary.ui.fragment.task.TaskLogic;
import com.xy.xylibrary.ui.fragment.task.TaskType;
import com.xy.xylibrary.utils.DeeplinkUtils;
import com.xy.xylibrary.utils.RomUtils;
import com.xy.xylibrary.utils.SaveShare;
import com.xy.xylibrary.utils.ToastUtils;
import com.xy.xylibrary.view.GradientTabStrip;
import com.xy.xylibrary.view.ViewPagerSlide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import am.widget.basetabstrip.BaseTabStrip;


/**
 * zw
 * 主页面
 */
public abstract class BaseChoiceActivity extends BaseActivity implements ViewPager.OnPageChangeListener, BaseTabStrip.OnItemClickListener, View.OnClickListener {


    private ViewPagerSlide tabVp;
    private GradientTabStrip gtsGtsTabs;
    private GradientTabStripAdapter adapter;
    private String Clipboard;
    private TaskType taskType;
    //    private ImageView indicate_image;
    private int indicatesize = 0;

    @Override
    public void onClick(View v) {
//        if(TextUtils.isEmpty(SaveShare.getValue(this,"indicate"))){
//            indicatesize++;
//            ToastUtils.showLong(indicatesize+"");
//            indicate_image.setImageDrawable(getResources().getDrawable(R.drawable.my_wallet_bg));
//            if(indicatesize == 3){
//                indicate_image.setVisibility(View.GONE);
//                SaveShare.saveValue(this,"indicate","indicate");
//            }
//        }
    }

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
            if (taskType.tasktype == 5 || taskType.tasktype == 3 || taskType.tasktype == 1) {
                if (tabVp != null) {
                    tabVp.setCurrentItem(0);
                }
            } else if (taskType.tasktype == 4) {
                if (tabVp != null) {
                    tabVp.setCurrentItem(3);
                    SaveShare.saveValue(BaseChoiceActivity.this, "SP", "4");
                }
            } else if (taskType.tasktype == 6) {
                if (tabVp != null) {
                    tabVp.setCurrentItem(1);
                    SaveShare.saveValue(BaseChoiceActivity.this, "HL", "5");
                }
            } else if (taskType.tasktype == 10) {
                if (!TextUtils.isEmpty(taskType.link) && DeeplinkUtils.getDeeplinkUtils().CanOpenDeeplink(BaseChoiceActivity.this, taskType.link)) {
                    TaskType taskType3 = LitePal.where("tasktype = ?", taskType.tasktype + "").findFirst(TaskType.class);
                    SaveShare.saveValue(BaseChoiceActivity.this, "JB", "");
//                    taskType3.ISStartTask = true;
//                    taskType3.save();
                    TaskLogic.getTaskLogic().FinishTask2(BaseChoiceActivity.this,"",taskType3.taskId,false);
                    DeeplinkUtils.getDeeplinkUtils().OpenDeeplink2(BaseChoiceActivity.this, taskType.link);
                } else {
                    ToastUtils.showLong("吊起失败");
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
        if (adapter != null) {
            adapter.isTagEnable(2);
            if (!TextUtils.isEmpty(s) && s.equals("1")) {
                adapter.getSelectedDrawable(1, BaseChoiceActivity.this);
                adapter.getPageTitle(1);
//                tabVp.setCurrentItem(0);
            } else {
                adapter.getSelectedDrawable(0, BaseChoiceActivity.this);
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

//        indicate_image = findViewById(R.id.indicate_image);
//        indicate_image.setOnClickListener(this);
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

//                if(TextUtils.isEmpty(SaveShare.getValue(this,"indicate"))){
//                    indicate_image.setVisibility(View.VISIBLE);
//                    indicate_image.setImageDrawable(getResources().getDrawable(R.drawable.my_wallet_bg));
//                }
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