package com.xy.xylibrary.ui.fragment.task;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.constellation.xylibrary.R;
import com.timmy.tdialog.TDialog;
import com.timmy.tdialog.base.BindViewHolder;
import com.timmy.tdialog.listener.OnViewClickListener;
import com.xy.xylibrary.Interface.ActiveListener;
import com.xy.xylibrary.Interface.SwipeRefreshListener;
import com.xy.xylibrary.base.AppContext;
import com.xy.xylibrary.base.BaseFragment;
import com.xy.xylibrary.config.SwipeRefreshOnRefresh;
import com.xy.xylibrary.indicate.IndicatePage;
import com.xy.xylibrary.refresh.SuperEasyRefreshLayout;
import com.xy.xylibrary.signin.ActiveValue;
import com.xy.xylibrary.signin.ActiveView;
import com.xy.xylibrary.signin.AdTask;
import com.xy.xylibrary.signin.AppSignInList;
import com.xy.xylibrary.signin.CompleteActive;
import com.xy.xylibrary.signin.StepsView;
import com.xy.xylibrary.ui.activity.login.LoginRequest;
import com.xy.xylibrary.ui.activity.login.LoginTypeActivity;
import com.xy.xylibrary.ui.activity.login.RequestSyntony;
import com.xy.xylibrary.ui.activity.login.UserMessage;
import com.xy.xylibrary.ui.activity.task.LookOverDetailActivity;
import com.xy.xylibrary.ui.activity.task.SignInActivity;
import com.xy.xylibrary.utils.SaveShare;
import com.xy.xylibrary.utils.ToastUtils;
import com.xy.xylibrary.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class TaskFragment extends BaseFragment implements SwipeRefreshListener, View.OnClickListener, SignInRort,OnViewClickListener,AppContext.UserGold,ActiveListener {

    private StepsView stepView;
    private Button linxiasamo;
    private TextView signIn15;
    private RecyclerView recyclerLayoutList;
    private SuperEasyRefreshLayout swipeRefresh;
    private ActiveView ActiveValueStepView;
    private TextView listBar, gold, gold_RMB;
    private TextView gold_money, linxiasamo_cover, sign_fate;
    private ImageView promptly_sign_bg;
    private boolean isVisibleToUser = false;
    public static int Multiple = 1;
    private ActiveValue activeValue;
    private TextView active_value;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_task;
    }

    @Override
    protected void initData(View view) {
        try {
            promptly_sign_bg = view.findViewById(R.id.promptly_sign_bg);
            gold_RMB = view.findViewById(R.id.gold_RMB);
            gold = view.findViewById(R.id.gold_money_number);
            sign_fate = view.findViewById(R.id.sign_fate);
            stepView = view.findViewById(R.id.step_view);
            ActiveValueStepView = view.findViewById(R.id.active_value_step_view);
            active_value = view.findViewById(R.id.active_value);
            linxiasamo = view.findViewById(R.id.linxiasamo);
            recyclerLayoutList = view.findViewById(R.id.recycler_layout_list);
            swipeRefresh = view.findViewById(R.id.swipe_refresh);
            listBar = view.findViewById(R.id.list_bar);
            signIn15 = view.findViewById(R.id.sign_in_15);
            gold_money = view.findViewById(R.id.gold_money);
            linxiasamo_cover = view.findViewById(R.id.linxiasamo_cover);
            ViewGroup.LayoutParams layoutParams = listBar.getLayoutParams();
            layoutParams.height = Utils.getStatusBarHeight(getActivity());
            listBar.setLayoutParams(layoutParams);
            linxiasamo.setOnClickListener(TaskFragment.this);
            signIn15.setOnClickListener(TaskFragment.this);
            gold_money.setOnClickListener(TaskFragment.this);
            ActiveValueStepView.setOnClickListener(TaskFragment.this);
            SpannableString spannableString = new SpannableString("连续15天签到领取7500+金币");
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#656565")), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#f48421")), 2, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#656565")), 5, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#f48421")), 9, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#656565")), 14, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            signIn15.setText(spannableString);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshTask(AdTask adTask) {
        if (recyclerLayoutList != null) {
            new CountDownTimer(5000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    AppContext.getUserInfo(getActivity(), "", SaveShare.getValue(getActivity(), "userId"),TaskFragment.this);
                    TaskLogic.getTaskLogic().getAppTaskList(getActivity(), recyclerLayoutList);
                }
            }.start();

         }
    }
    @Override
    protected void initListener() {
        try {
            SwipeRefreshOnRefresh swipeRefreshOnRefresh = new SwipeRefreshOnRefresh();
            swipeRefreshOnRefresh.SwipeRefresh(getActivity(), swipeRefresh, TaskFragment.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && stepView != null) {
            IndicatePage.getIndicatePage().setGoldNewbieGuide(TaskFragment.this,getActivity(), linxiasamo, recyclerLayoutList);
            this.isVisibleToUser = isVisibleToUser;
            InitData();
        }
    }

    private void InitData() {
        try {
            if (stepView != null) {
                //任务列表
                TaskLogic.getTaskLogic().getAppTaskList(getActivity(), recyclerLayoutList);
                //签到
                TaskLogic.getTaskLogic().initData(stepView, this);
                //用户数据
                TaskLogic.getTaskLogic().setUserGold(this);
                //活跃值
                TaskLogic.getTaskLogic().ActiveValueStepViewData(getActivity(),ActiveValueStepView,TaskFragment.this);
                //加载视频广告
                TaskLogic.getTaskLogic().loadVideoAd("923044756", TTAdConstant.VERTICAL);
                if (AppContext.userMessageData != null && gold != null) {
                    gold.setText(AppContext.userMessageData.gold + "");
                    gold_RMB.setText("约" + Utils.doubleToString((double) AppContext.userMessageData.gold / 10000) + "元");
                    active_value.setText(AppContext.userMessageData.active + "");
                }
                long loginTime = Long.parseLong((TextUtils.isEmpty(SaveShare.getValue(getActivity(), "loginTime"))? "0" : SaveShare.getValue(getActivity(), "loginTime")));
                //判断是否登录和是否在三天内未登录再次弹出登录提示框
                if (TextUtils.isEmpty(SaveShare.getValue(getActivity(), "userId")) && System.currentTimeMillis() - loginTime > 24*3*60*1000) {//
                    SaveShare.saveValue(getActivity(), "loginTime",System.currentTimeMillis()+"");
                    TaskLogic.getTaskLogic().LoadDialog((AppCompatActivity) getActivity(),TaskFragment.this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        TaskLogic.getTaskLogic().getAppTaskList(getActivity(), recyclerLayoutList);
        TaskLogic.getTaskLogic().initData(stepView, TaskFragment.this);
        TaskLogic.getTaskLogic().ActiveValueStepViewData(getActivity(),ActiveValueStepView,TaskFragment.this);

    }

    @Override
    public void onDropHeight(float overscrollTop) {
    }

    @Override
    public void onClick(View v) {
        try {
            int i = v.getId();
            if (i == R.id.linxiasamo) {
                if (!TextUtils.isEmpty(SaveShare.getValue(getActivity(), "userId"))) {
                    if (!linxiasamo.getText().equals("已签到")) {
                        linxiasamo_cover.setVisibility(View.VISIBLE);
                        promptly_sign_bg.setBackground(getResources().getDrawable(R.drawable.promptly_sign_bg));
                        linxiasamo.setBackground(getResources().getDrawable(R.drawable.search_5));
                        linxiasamo.setText("已签到");
                         TaskLogic.getTaskLogic().requestSuccessData(stepView,Multiple, new AppContext.UserGold() {
                            @Override
                            public void gold(UserMessage userMessage) {
                                TaskLogic.getTaskLogic().SignInDialog(getActivity(), TaskFragment.this);
                                AppContext.getUserInfo(getActivity(), "", SaveShare.getValue(getActivity(), "userId"),TaskFragment.this);
                            }
                        });
                    }
                } else {
                    Intent intent1 = new Intent(getActivity(), LoginTypeActivity.class);
                    getActivity().startActivity(intent1);
                }
            } else if (i == R.id.sign_in_15) {
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                startActivity(intent);
            } else if (i == R.id.gold_money) {
                if (!TextUtils.isEmpty(SaveShare.getValue(getActivity(), "userId"))) {
                    Intent intent = new Intent(getActivity(), LookOverDetailActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent1 = new Intent(getActivity(), LoginTypeActivity.class);
                    getActivity().startActivity(intent1);
                    ToastUtils.showLong("请先登录哦！");
                }
            }else if(i == R.id.active_value_step_view){
                if(TextUtils.isEmpty(SaveShare.getValue(getActivity(), "userId"))){
                    Intent intent1 = new Intent(getActivity(), LoginTypeActivity.class);
                    getActivity().startActivity(intent1);
                }else{
                    if(activeValue != null){
                         try {
                            int gold = 0;
                            for (int j = 0; j < activeValue.getData().getActiveRewardsVms().size(); j++) {
                                if(activeValue.getData().getUserActive() > activeValue.getData().getActiveRewardsVms().get(j).getActive() && !activeValue.getData().getActiveRewardsVms().get(j).isU_IsComplete()){
                                     gold = gold + activeValue.getData().getActiveRewardsVms().get(j).getGold();
                                     LoginRequest.getWeatherRequest().getCompleteActiveRewardsData(getActivity(),activeValue.getData().getActiveRewardsVms().get(j).getId(), new RequestSyntony<CompleteActive>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onNext(CompleteActive completeActive) {
    //                                    if (activeValue != null && activeValue.getData() != null) {
    //                                        setActiveData(activeValue,activeValue.getData().getActiveRewardsVms(), ActiveValueStepView);
    //                                        activeListener.Active(activeValue);
    //                                    }
                                        }
                                    });

                                }
                            }
                            TaskLogic.getTaskLogic().ActiveDialog((AppCompatActivity) getActivity(),TaskFragment.this,gold);
                            AppContext.getUserInfo(getActivity(), "", SaveShare.getValue(getActivity(), "userId"),TaskFragment.this);
                            TaskLogic.getTaskLogic().ActiveValueStepViewData(getActivity(),ActiveValueStepView,TaskFragment.this);
                        } catch (Exception e) {
                             e.printStackTrace();
                        }
                    }else {
                        ToastUtils.showLong("暂无活跃值领取");
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void SignIn(AppSignInList appSignInList) {
        try {
            if (swipeRefresh != null) {
                swipeRefresh.setRefreshing(false);
            }
            if (appSignInList != null && appSignInList.getData() != null) {
                if (appSignInList.getData().isToDayIsSign()) {
                    linxiasamo_cover.setVisibility(View.VISIBLE);
                    promptly_sign_bg.setBackground(getResources().getDrawable(R.drawable.promptly_sign_bg));
                    linxiasamo.setBackground(getResources().getDrawable(R.drawable.search_5));
                    linxiasamo.setText("已签到");
                } else {
                    linxiasamo_cover.setVisibility(View.GONE);
                    promptly_sign_bg.setBackground(getResources().getDrawable(R.drawable.promptly_sign_bg2));
                    linxiasamo.setBackground(getResources().getDrawable(R.drawable.withdraw_search_5));
                    linxiasamo.setText("立即签到");
                }

//        gold.setText((AppContext.userMessageData.gold+appSignInList.getData().getSignAtureVms().get(0).getGold())+"");
//        gold_RMB.setText("约"+Utils.doubleToString((AppContext.userMessageData.gold+appSignInList.getData().getSignAtureVms().get(0).getGold())/10000)+"元");
            sign_fate.setText(appSignInList.getData().getSignCount() + "天");
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void SignInDefeated() {
        try {
            linxiasamo_cover.setVisibility(View.GONE);
            promptly_sign_bg.setBackground(getResources().getDrawable(R.drawable.promptly_sign_bg2));
            linxiasamo.setBackground(getResources().getDrawable(R.drawable.withdraw_search_5));
            linxiasamo.setText("立即签到");
            sign_fate.setText("0天");
            gold.setText("==");
            active_value.setText("0");
            gold_RMB.setText("约***元");
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (isVisibleToUser && gold != null) {
                AppContext.getUserInfo(getActivity(), "", SaveShare.getValue(getActivity(), "userId"),TaskFragment.this);
                TaskLogic.getTaskLogic().getAppTaskList(getActivity(), recyclerLayoutList);
                TaskLogic.getTaskLogic().initData(stepView, this);
                TaskLogic.getTaskLogic().ActiveValueStepViewData(getActivity(),ActiveValueStepView,TaskFragment.this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
        int i = view.getId();
        if (i == R.id.login_see_btn) {
            if(TextUtils.isEmpty(SaveShare.getValue(getActivity(), "userId"))){
                Intent intent1 = new Intent(getActivity(), LoginTypeActivity.class);
                getActivity().startActivity(intent1);
            }else{
                tDialog.dismiss();
            }
        } else if (i == R.id.login_img) {
            tDialog.dismiss();
        }else if(i == R.id.sign_in_see_btn){
            tDialog.dismiss();
            TaskFragment.Multiple++;
            TaskLogic.getTaskLogic().requestSuccessData(stepView,Multiple, new AppContext.UserGold() {
                @Override
                public void gold(UserMessage userMessage) {
                    AppContext.getUserInfo(getActivity(), "", SaveShare.getValue(getActivity(), "userId"),TaskFragment.this);
                }
            });
            TaskLogic.getTaskLogic(). DoubleGold();

        }else if(i == R.id.sign_img){
            tDialog.dismiss();
        }else if(i == R.id.login_interaction_ad){

            TaskType taskType = new TaskType();
            taskType.tasktype = -1;
            EventBus.getDefault().post(taskType);
         }else if(i == R.id.sign_interaction_ad){
             TaskType taskType = new TaskType();
            taskType.tasktype = -1;
            EventBus.getDefault().post(taskType);

        }
    }

    @Override
    public void gold(UserMessage userMessage) {
        if (userMessage != null) {
            gold.setText(userMessage.gold == 0 ? "==" : userMessage.gold + "");
            gold_RMB.setText("约" + Utils.doubleToString((double) userMessage.gold / 10000) + "元");
            active_value.setText(userMessage.active + "");
        }
    }

    @Override
    public void Active(ActiveValue activeValue) {
        this.activeValue = activeValue;
        if(active_value != null && activeValue != null && activeValue.getData() != null){
            active_value.setText(activeValue.getData().getUserActive() + "");
        }

    }
}
