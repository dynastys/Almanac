package com.zt.rainbowweather.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.umeng.analytics.MobclickAgent;
import com.xy.xylibrary.base.AppContext;
import com.xy.xylibrary.presenter.DotRequest;
import com.xy.xylibrary.signin.AdTask;
import com.xy.xylibrary.presenter.DotRequest;
import com.xy.xylibrary.ui.fragment.task.TaskLogic;
import com.xy.xylibrary.ui.fragment.task.TaskType;
import com.xy.xylibrary.utils.DeeplinkUtils;
import com.xy.xylibrary.utils.Utils;
import com.zt.rainbowweather.BasicApplication;
import com.zt.rainbowweather.api.RequestSyntony;
import com.zt.rainbowweather.presenter.request.WeatherRequest;
import com.zt.rainbowweather.ui.fragment.HomeFragment;
import com.zt.rainbowweather.ui.fragment.WeatherFragment;
import com.zt.rainbowweather.utils.AdDialog;
import com.zt.rainbowweather.utils.FinishTaskDialog;
import com.zt.rainbowweather.utils.AdDialog;
import com.zt.rainbowweather.utils.ToastUtils;
import com.zt.rainbowweather.utils.utils;
import com.zt.weather.R;
import com.timmy.tdialog.TDialog;
import com.timmy.tdialog.base.BindViewHolder;
import com.timmy.tdialog.listener.OnViewClickListener;
import com.umeng.message.PushAgent;
import com.xy.xylibrary.ui.activity.BaseChoiceActivity;
import com.xy.xylibrary.ui.adapter.GradientTabStripAdapter;
import com.xy.xylibrary.utils.SaveShare;
import com.xy.xylibrary.view.ViewPagerSlide;
import com.zt.rainbowweather.entity.City;
import com.zt.rainbowweather.entity.CityEvent;
import com.zt.rainbowweather.entity.background.IsUserLight;
import com.zt.rainbowweather.entity.city.CityX;
import com.zt.rainbowweather.entity.city.Event;
import com.zt.rainbowweather.presenter.ExtraFunction;
import com.zt.rainbowweather.presenter.UpdatePort;
import com.zt.rainbowweather.ui.adapter.MyGradientTabStripAdapter;
import com.zt.rainbowweather.ui.service.FloatingService;
import com.zt.rainbowweather.utils.ConstUtils;
import com.zt.xuanyin.Interface.AdProtogenesisListener;
import com.zt.xuanyin.controller.Ad;
import com.zt.xuanyin.controller.NativeAd;
import com.zt.xuanyin.entity.model.Native;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zw
 * @time 2019-3-8
 * 主页面
 */
public class MainActivity extends BaseChoiceActivity implements OnViewClickListener {

    private int position1;
    private MyGradientTabStripAdapter myGradientTabStripAdapter;
    private NativeAd nativelogic;
    private BindViewHolder MyviewHolder;
    private ExtraFunction extraFunction;
    private List<City> cities = new ArrayList<>();
    private CountDownTimer timer,timer2;
    private int time;


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        switch (position) {
            case 0:
                DotRequest.getDotRequest().getActivity(getContext(), "首页-主页");
                break;
            case 1:
                DotRequest.getDotRequest().getActivity(getContext(), "黄历-主页");
                break;
            case 2:
                DotRequest.getDotRequest().getActivity(getContext(), "任务-主页");
                break;
            case 3:
                DotRequest.getDotRequest().getActivity(getContext(), "看一看-主页");
                break;
            case 4:
                DotRequest.getDotRequest().getActivity(getContext(), "服务-主页");
                break;
        }
        try {
            setIsUserLightMode(false);
            if (position == 3) {
                try {
                    SaveShare.saveValue(MainActivity.this, "video", Utils.getOldDate(0));
                    TaskType taskType3 = LitePal.where("tasktype = ?", "4").findFirst(TaskType.class);
                    if (taskType3 == null || taskType3.taskfinishsize >= taskType3.tasksize) {
                        return;
                    }
                    myGradientTabStripAdapter.isTagEnable(3);
                    String Sp = SaveShare.getValue(MainActivity.this, "SP");
                    if (!TextUtils.isEmpty(Sp) && Sp.equals("4")) {
                        time = (int) taskType3.CompleteMinTime;
                        if (time == 0) {
                            time = 60 * 1000;
                        }
                        if(timer != null){
                            timer.cancel();
                            timer = null;
                        }
                        timer = new CountDownTimer(time, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                Log.e("timer", "onTick: " + millisUntilFinished);
                                taskType3.schedule = (int) millisUntilFinished;
                                taskType3.save();
                            }

                            @Override
                            public void onFinish() {
                                try {
                                    if (position1 != 3) {
                                        if(timer != null){
                                            timer.cancel();
                                        }
                                        return;
                                    }
                                    if (taskType3.tasksize > 1 && taskType3.taskfinishsize < taskType3.tasksize) {
                                        taskType3.taskfinishsize++;
                                        taskType3.schedule = 0;
                                        taskType3.save();
                                        TaskLogic.getTaskLogic().FinishTask(MainActivity.this, "", taskType3.taskId, false);
                                        timer.start();
                                    } else {
                                        SaveShare.saveValue(MainActivity.this, "JB", "");
    //                                    taskType3.ISStartTask = true;
                                        taskType3.schedule = 0;
                                        taskType3.save();
                                        timer = null;
                                        TaskLogic.getTaskLogic().FinishTask(MainActivity.this, "", taskType3.taskId, false);
                                        EventBus.getDefault().post("");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        timer.start();
                    }
                    WeatherRequest.getWeatherRequest().getLookAtData(MainActivity.this, "看一看", "进入视频");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                if(timer != null){
                    timer.cancel();
                }
            }
            if (position == 1) {
                try {
                    SaveShare.saveValue(MainActivity.this, "almanac", Utils.getOldDate(0));
                    myGradientTabStripAdapter.isTagEnable(3);
                    TaskType taskType3 = LitePal.where("tasktype = ?", "6").findFirst(TaskType.class);
                    String Sp = SaveShare.getValue(MainActivity.this, "HL");
                    if (!TextUtils.isEmpty(Sp) && Sp.equals("5")) {
                        if(timer != null){
                            timer.cancel();
                            timer = null;
                        }
                       int times = (int) taskType3.CompleteMinTime;
                        if (times == 0) {
                            times = 5 * 1000;
                        }
                         SaveShare.saveValue(MainActivity.this, "HL", "");
                        timer2 = new CountDownTimer(times, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                            }

                            @Override
                            public void onFinish() {
                                try {
                                    timer2 = null;
                                    if (taskType3 == null) {
                                        ToastUtils.showLong("获取任务失败！");
                                        return;
                                    }
                                    FinishTaskDialog adDialog = new FinishTaskDialog(MainActivity.this, "6", 0);
                                    adDialog.setClicklistener(new FinishTaskDialog.ClickListenerInterface() {
                                        @Override
                                        public void doConfirm(boolean size) {
                                            TaskLogic.getTaskLogic().FinishTask(MainActivity.this, "", taskType3.taskId, size);
                                            adDialog.dismiss();
                                        }

                                        @Override
                                        public void doCancel() {
                                            adDialog.dismiss();
                                        }
                                    });
                                    adDialog.show();
                                    EventBus.getDefault().post("1");
                                    SaveShare.saveValue(MainActivity.this, "JB", "");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        timer2.start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                if (timer2 != null) {
                    timer2.cancel();
                }
            }
            if (position == 2 || position == 0 || position == 4) {
                if (timer != null) {
                    timer.cancel();
                }
            }
            if (position == 2) {
                SaveShare.saveValue(MainActivity.this, "JB", Utils.getOldDate(0));
                myGradientTabStripAdapter.isTagEnable(2);
                setIsUserLightMode(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public GradientTabStripAdapter setGradientTabStripAdapter(FragmentManager fragmentManager, ViewPagerSlide tabVp) {
        myGradientTabStripAdapter = new MyGradientTabStripAdapter(fragmentManager, tabVp);
        return myGradientTabStripAdapter;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void loadViewLayout() {
        try {
            getWindow().setFormat(PixelFormat.TRANSLUCENT);
            String ISAD = SaveShare.getValue(this, "ISAD");
            if (!TextUtils.isEmpty(ISAD) && ISAD.equals("1")) {
                //            DialogShow();
            }
            cities = LitePal.findAll(City.class);
            if (cities == null || cities.size() == 0) {
                SearchCityActivity.startActivity(MainActivity.this, null);
                finish();
            }
            extraFunction = new ExtraFunction(MainActivity.this);
            extraFunction.AppListData();
//        extraFunction.KeepAlive();
            extraFunction.NotificationBar();
            extraFunction.setNoninductive();
            UpdatePort.getUpdatePort().UpdateDialog(MainActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setCity1Event(TaskType taskType) {
        try {
             if (taskType.tasktype == 8) {
                 if(TextUtils.isEmpty(SaveShare.getValue(MainActivity.this, "down"))){
                     ToastUtils.showLong("开始下载，请稍后...");
                     SaveShare.saveValue(MainActivity.this, "down", "" + taskType.tasktype);
                     utils.Download(mContext, taskType.link);
                 }else{
                     ToastUtils.showLong("任务正在进行中");
                 }

            } else if (taskType.tasktype == 9) {
                 TaskType taskType3 = LitePal.where("tasktype = ?", "9").findFirst(TaskType.class);
                if (taskType3 != null) {
                    SaveShare.saveValue(MainActivity.this, "JB", "");
                    TaskLogic.getTaskLogic().FinishTask(MainActivity.this, "", taskType3.taskId, false);
                    AdviseMoreDetailActivity.startActivity(MainActivity.this, "落地页", taskType3.link, "0");
                }
            } else if (taskType.tasktype == 11) {
                 try {
                     AdDialog adDialog = new AdDialog(MainActivity.this, "11", 0);
                     adDialog.setClicklistener(new AdDialog.ClickListenerInterface() {
                         @Override
                         public void doConfirm(boolean b) {
     //                        TaskType taskType3 = LitePal.where("tasktype = ?", "11").findFirst(TaskType.class);
     //                        if (taskType3 != null) {
     //                            TaskLogic.getTaskLogic().FinishTask2(MainActivity.this, "", taskType3.taskId, b);
     //                        }
                             EventBus.getDefault().post(new AdTask());
                             adDialog.dismiss();
                         }

                         @Override
                         public void doCancel() {
                             EventBus.getDefault().post(new AdTask());
                             adDialog.dismiss();
                         }
                     });
                     adDialog.show();
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             } else if (taskType.tasktype == -1) {
                 AdviseMoreDetailActivity.startActivity(MainActivity.this, "福利", "http://link.qukanzixun.com/link/936d38e3-9ae2-4379-af40-26b2f2b14973", "0");
            } else if (taskType.tasktype == 6) {
             } else if (taskType.tasktype == 1) {
             } else if (taskType.tasktype == 3) {
             } else if (taskType.tasktype == 5) {
             } else if (taskType.tasktype == 4) {
             } else if (taskType.tasktype == 7) {
             } else {
                 UpdatePort.getUpdatePort().UpdateDialog(MainActivity.this);
            }
        } catch (Exception e) {
            Log.e("Exception", "setCity1Event: "+e.getMessage().toString());
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setIsUserLight(IsUserLight isUserLight) {
        setIsUserLightMode(isUserLight.getMessage());
    }

    @Override
    public void onPageSelected(int position) {
        this.position1 = position;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            if (ConstUtils.take_a_look) {
                if (keyCode == KeyEvent.KEYCODE_BACK && position1 == 0) {
                    if (((HomeFragment) myGradientTabStripAdapter.getItem(position1)).onKeyDown(keyCode, event)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setCityEvent(CityEvent cityEvent) {
        EventBus.getDefault().post(new Event(cityEvent.city, cityEvent.isDelete));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setCity(City city) {
        EventBus.getDefault().post(new CityX(city));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setTabStrip(String s) {
        if (myGradientTabStripAdapter != null) {
            myGradientTabStripAdapter.isTagEnable(2);
            myGradientTabStripAdapter.isTabTagEnable(2);

             if(!TextUtils.isEmpty(s) && s.equals("1")){
//                 myGradientTabStripAdapter.isTagEnable(1);
//                 myGradientTabStripAdapter.notifyDataSetChanged();
            } else {
//                 myGradientTabStripAdapter.isTagEnable(0);
//                 myGradientTabStripAdapter.notifyDataSetChanged();
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            List<City> citys = LitePal.findAll(City.class);
            for (int i = 0; i < citys.size(); i++) {
                Log.e("citys", "onDestroy: " + citys.get(i).name);
            }
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }
            WeatherFragment.ISNEWSONR = true;
            WeatherFragment.ISNEWS = true;

            extraFunction.onDestroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            EventBus.getDefault().register(this);
            PushAgent.getInstance(this).onAppStart();

//          floatWindow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                startService(new Intent(MainActivity.this, FloatingService.class));
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        extraFunction.onStop(MainActivity.this);
    }

    private void DialogShow() {
        try {
            // 获得开屏广告对象
            nativelogic = new Ad().NativeAD(this, "98f8e423-02e0-49f5-989f-af46f5c59203", "7c6680c9-e304-49a6-a692-7efb2dd38224", "67C53558D3E3485EA681EA21735A5003", new AdProtogenesisListener() {
                @Override
                public void onADReady(final Native url, NativeAd nativelogic) {
                    if (url.src != null) {
                        new TDialog.Builder(getSupportFragmentManager())
                                .setLayoutRes(R.layout.dialog)    //设置弹窗展示的xml布局
                                .setScreenWidthAspect(MainActivity.this, 0.6f)   //设置弹窗宽度(参数aspect为屏幕宽度比例 0 - 1f)
                                .setScreenHeightAspect(MainActivity.this, 0.4f)  //设置弹窗高度(参数aspect为屏幕宽度比例 0 - 1f)
                                .setCancelableOutside(false)
                                .setOnBindViewListener(viewHolder -> {
                                    MyviewHolder = viewHolder;
                                    ImageView imageView = viewHolder.getView(R.id.dialog_image);
                                    Glide.with(MainActivity.this).load(url.src).into(imageView);
                                })
                                .setOnDismissListener(dialog -> {
                                })
                                .addOnClickListener(R.id.dialog_image, R.id.dialog_cancel)
                                .setOnViewClickListener(MainActivity.this)
                                .setDimAmount(0.6f)     //设置弹窗背景透明度(0-1f)
                                .setDialogAnimationRes(R.style.animate_dialog)  //设置弹窗动画
                                .create()   //创建TDialog
                                .show();    //展示
                    }
                }

                @Override
                public void onAdFailedToLoad(String error) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
        switch (view.getId()) {
            case R.id.dialog_image:
                RelativeLayout relativeLayout = MyviewHolder.getView(R.id.dialog_rel);
                // 点击开屏广告处理
                nativelogic.OnClick(relativeLayout);
                tDialog.dismiss();
                break;
            case R.id.dialog_cancel:
                tDialog.dismiss();
                break;
            case R.id.dialog_confirm_tv:
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0);
                tDialog.dismiss();
                break;
            case R.id.dialog_cancel_tv:
                tDialog.dismiss();
                break;
        }
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
        if (myGradientTabStripAdapter != null) {
            myGradientTabStripAdapter.isTagEnable(2);
        }
        if(timer != null){
            timer.cancel();
            timer.start();
        }
        if (timer2 != null) {
            timer2.start();
        }
        hideInput();
        Log.e("Phone", "onNext: " + BasicApplication.appCount);
        if (!TextUtils.isEmpty(BasicApplication.url) && BasicApplication.appCount <= 1) {
            AdviseMoreDetailActivity.startActivity(MainActivity.this, "资讯", BasicApplication.url, "0");
//            BasicApplication.url = "";
        }
        MobclickAgent.onPageStart("MainActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this); //统计时长
    }

    @Override
    public void onPause() {
        super.onPause();
        if(timer != null){
            timer.cancel();
        }
        if (timer2 != null) {
            timer2.cancel();
        }
        MobclickAgent.onPageEnd("MainActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)，必须保证 onPageEnd 在 onPause 之前调用，因为SDK会在 onPause 中保存onPageEnd统计到的页面数据。
        MobclickAgent.onPause(this);
    }

}
