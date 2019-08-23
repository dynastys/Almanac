package com.xy.xylibrary.ui.fragment.task;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.constellation.xylibrary.R;
import com.xy.xylibrary.Interface.SwipeRefreshListener;
import com.xy.xylibrary.base.AppContext;
import com.xy.xylibrary.base.BaseFragment;
import com.xy.xylibrary.config.SwipeRefreshOnRefresh;
import com.xy.xylibrary.refresh.SuperEasyRefreshLayout;
import com.xy.xylibrary.signin.AppSignInList;
import com.xy.xylibrary.signin.StepsView;
import com.xy.xylibrary.ui.activity.login.LoginTypeActivity;
import com.xy.xylibrary.ui.activity.login.UserMessage;
import com.xy.xylibrary.ui.activity.task.LookOverDetailActivity;
import com.xy.xylibrary.ui.activity.task.SignInActivity;
import com.xy.xylibrary.utils.SaveShare;
import com.xy.xylibrary.utils.Utils;
import org.greenrobot.eventbus.EventBus;

public class TaskFragment extends BaseFragment implements SwipeRefreshListener,View.OnClickListener,SignInRort{

    private StepsView stepView;
    private Button linxiasamo;
    private TextView signIn15;
    private RecyclerView recyclerLayoutList;
    private SuperEasyRefreshLayout swipeRefresh;
    private TextView listBar,gold,gold_RMB;
    private TextView gold_money,linxiasamo_cover,sign_fate;
    private UserMessage userMessageData;
    private ImageView promptly_sign_bg;

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
            SpannableString spannableString = new SpannableString("连续15天签到领取7500+金币");
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#656565")),0,2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#f48421")),2,5,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#656565")),5,9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#f48421")),9,14,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#656565")),14,16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            signIn15.setText(spannableString);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void initListener() {
        try {
            SwipeRefreshOnRefresh swipeRefreshOnRefresh = new SwipeRefreshOnRefresh();
            swipeRefreshOnRefresh.SwipeRefresh(getActivity(), swipeRefresh, TaskFragment.this);
//        for (int i = 0; i < 6; i++) {
//            lists.add("任务" + i);
//        }
            TaskLogic.getTaskLogic().getAppTaskList(getActivity(), recyclerLayoutList);
            TaskLogic.getTaskLogic().initData(stepView,this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && stepView != null){
            InitData();
        }
    }

    private void InitData(){
        try {
            if(stepView != null){
    //            AppContext.getUserInfo(getActivity(), "", SaveShare.getValue(getActivity(), "userId"), new AppContext.UserGold() {
    //                @Override
    //                public void gold(UserMessage userMessage) {
    //                    if(userMessage != null ){
    //                        gold.setText(userMessage.gold == 0?"==":userMessage.gold+"");
    //                        gold_RMB.setText("约"+Utils.doubleToString((double) userMessage.gold/10000)+"元");
    //                    }
    //                }
    //            });
                TaskLogic.getTaskLogic().loadVideoAd("923044756", TTAdConstant.VERTICAL);
    //            TaskLogic.getTaskLogic().getAppTaskList(getActivity(), recyclerLayoutList);
    //            TaskLogic.getTaskLogic().initData(stepView,this);
                if(AppContext.userMessageData != null && gold != null){
                    gold.setText(AppContext.userMessageData.gold+"");
                    gold_RMB.setText("约"+Utils.doubleToString((double)AppContext.userMessageData.gold/10000)+"元");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postAtTime(new Runnable() {
            @Override
            public void run() {
                TaskLogic.getTaskLogic().getAppTaskList(getActivity(), recyclerLayoutList);

                if (swipeRefresh != null) {
                    swipeRefresh.setRefreshing(false);
                }
            }
        }, 3000);
    }

    @Override
    public void onDropHeight(float overscrollTop) {
    }

    @Override
    public void onClick(View v) {
        try {
            int i = v.getId();
            if (i == R.id.linxiasamo) {
                if(!TextUtils.isEmpty(SaveShare.getValue(getActivity(), "userId"))){
                    if(!linxiasamo.getText().equals("已签到")){
                        linxiasamo_cover.setVisibility(View.VISIBLE);
                        promptly_sign_bg.setBackground(getResources().getDrawable(R.drawable.promptly_sign_bg));
                        linxiasamo.setBackground(getResources().getDrawable(R.drawable.search_5));
                        linxiasamo.setText("已签到");
                        TaskLogic.getTaskLogic().requestSuccessData(stepView, new AppContext.UserGold() {
                            @Override
                            public void gold(UserMessage userMessage) {
                                AppContext.getUserInfo(getActivity(), "", SaveShare.getValue(getActivity(), "userId"), new AppContext.UserGold() {
                                    @Override
                                    public void gold(UserMessage userMessage) {
                                        if(userMessage != null ){
                                            gold.setText(userMessage.gold == 0?"==":userMessage.gold+"");
                                            gold_RMB.setText("约"+Utils.doubleToString((double)userMessage.gold/10000)+"元");
                                        }
                                    }
                                });
                            }
                        });
                    }
                }else{
                    Intent intent1 = new Intent(getActivity(), LoginTypeActivity.class);
                    getActivity().startActivity(intent1);
                }

            } else if (i == R.id.sign_in_15) {
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                startActivity(intent);
            } else if (i == R.id.gold_money) {
                Intent intent = new Intent(getActivity(), LookOverDetailActivity.class);
                startActivity(intent);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void SignIn(AppSignInList appSignInList) {
        try {
            if(appSignInList.getData().isToDayIsSign()){
                linxiasamo_cover.setVisibility(View.VISIBLE);
                promptly_sign_bg.setBackground(getResources().getDrawable(R.drawable.promptly_sign_bg));
                linxiasamo.setBackground(getResources().getDrawable(R.drawable.search_5));
                linxiasamo.setText("已签到");
            }else{
                linxiasamo_cover.setVisibility(View.GONE);
                promptly_sign_bg.setBackground(getResources().getDrawable(R.drawable.promptly_sign_bg2));
                linxiasamo.setBackground(getResources().getDrawable(R.drawable.withdraw_search_5));
                linxiasamo.setText("立即签到");
            }
//        gold.setText((AppContext.userMessageData.gold+appSignInList.getData().getSignAtureVms().get(0).getGold())+"");
//        gold_RMB.setText("约"+Utils.doubleToString((AppContext.userMessageData.gold+appSignInList.getData().getSignAtureVms().get(0).getGold())/10000)+"元");
            sign_fate.setText(appSignInList.getData().getSignCount()+"天");
        } catch (Resources.NotFoundException e) {
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
            gold_RMB.setText("约***元");
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if(gold != null){
                AppContext.getUserInfo(getActivity(), "", SaveShare.getValue(getActivity(), "userId"), new AppContext.UserGold() {
                    @Override
                    public void gold(UserMessage userMessage) {
                        if(userMessage != null ){
                            gold.setText(userMessage.gold == 0?"==":userMessage.gold+"");
                            gold_RMB.setText("约"+Utils.doubleToString((double) userMessage.gold/10000)+"元");
                        }
                    }
                });
                TaskLogic.getTaskLogic().getAppTaskList(getActivity(), recyclerLayoutList);
                TaskLogic.getTaskLogic().initData(stepView,this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
