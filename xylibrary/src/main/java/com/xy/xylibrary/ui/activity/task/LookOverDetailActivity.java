package com.xy.xylibrary.ui.activity.task;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.constellation.xylibrary.R;
import com.umeng.analytics.MobclickAgent;
import com.xy.xylibrary.base.AppContext;
import com.xy.xylibrary.base.BaseActivity;
import com.xy.xylibrary.presenter.DotRequest;
import com.xy.xylibrary.ui.activity.login.LoginRequest;
import com.xy.xylibrary.ui.activity.login.RequestSyntony;
import com.xy.xylibrary.ui.activity.login.UserActiveInfo;
import com.xy.xylibrary.ui.activity.login.UserMessage;
import com.xy.xylibrary.ui.fragment.task.WithdrawDepositActivity;
import com.xy.xylibrary.utils.SaveShare;
import com.xy.xylibrary.utils.ToastUtils;
import com.xy.xylibrary.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class LookOverDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView listBar;
    private ImageView finishMyWalletHead;
    private TextView myWallet;
    private TextView myWalletWithdraw;
    private TextView myGold;
    private LinearLayout myLinMoney;
    private RelativeLayout myWalletRel;
    private LinearLayout benefitsOfGold;
    private TextView detail;
    private RecyclerView recyclerLayoutDetailList;
    private TextView look_over_add_up_gold, look_over_taday_gold;
    private TextView look_over_RMB, look_over_gold;

    private List<String> lists = new ArrayList<>();
    private UserMessage userMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Activity getContext() {
        return LookOverDetailActivity.this;
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_look_over_detail;
    }

    @Override
    protected void loadViewLayout() {
        DotRequest.getDotRequest().getActivity(getContext(),"任务-钱包");
        try {
            look_over_gold = findViewById(R.id.look_over_gold);
            look_over_RMB = findViewById(R.id.look_over_RMB);
            look_over_taday_gold = findViewById(R.id.look_over_taday_gold);
            look_over_add_up_gold = findViewById(R.id.look_over_add_up_gold);
            listBar = findViewById(R.id.list_bar);
            finishMyWalletHead = findViewById(R.id.finish_my_wallet_head);
            myWallet = findViewById(R.id.my_wallet);
            myWalletWithdraw = findViewById(R.id.my_wallet_withdraw);
            myGold = findViewById(R.id.my_gold);
            myLinMoney = findViewById(R.id.my_lin_money);
            myWalletRel = findViewById(R.id.my_wallet_rel);
            benefitsOfGold = findViewById(R.id.benefits_of_gold);
            detail = findViewById(R.id.detail);
            recyclerLayoutDetailList = findViewById(R.id.recycler_layout_detail_list);
            ViewGroup.LayoutParams layoutParams = listBar.getLayoutParams();
            layoutParams.height = Utils.getStatusBarHeight(LookOverDetailActivity.this);
            listBar.setLayoutParams(layoutParams);
            finishMyWalletHead.setOnClickListener(LookOverDetailActivity.this);
            myWalletWithdraw.setOnClickListener(LookOverDetailActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        try {

            AppContext.getUserInfo(LookOverDetailActivity.this, "", SaveShare.getValue(LookOverDetailActivity.this, "userId"), new AppContext.UserGold() {
                @Override
                public void gold(UserMessage userMessage) {
                    try {
                        if (userMessage != null) {
                            userMessages = userMessage;
                            look_over_gold.setText(userMessage.gold + "");
                            look_over_RMB.setText("约" + Utils.doubleToString((double)userMessage.gold / 10000) + "元");
                            look_over_add_up_gold.setText(userMessage.gold + "");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            LoginRequest.getWeatherRequest().getUserActiveInfoData(LookOverDetailActivity.this, "", "", new RequestSyntony<UserActiveInfo>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(UserActiveInfo userActiveInfo) {
                    if (userActiveInfo != null && userActiveInfo.getData() != null && userActiveInfo.getData().getUserActiveVms().size() > 0) {
                        look_over_taday_gold.setText(userActiveInfo.getData().getToDayGold() + "");
                        LookOverDetailLogic.getLookOverDetailLogic().setGoldDetail(LookOverDetailActivity.this, recyclerLayoutDetailList, userActiveInfo.getData().getUserActiveVms());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void bindViews() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initData();
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

    @Override
    public void onResume() {
        super.onResume();
        if(look_over_gold != null){
            AppContext.getUserInfo(LookOverDetailActivity.this, "", SaveShare.getValue(LookOverDetailActivity.this, "userId"), new AppContext.UserGold() {
                @Override
                public void gold(UserMessage userMessage) {
                    try {
                        if (userMessage != null) {
                            userMessages = userMessage;
                            look_over_gold.setText(userMessage.gold + "");
                            look_over_RMB.setText("约" + Utils.doubleToString((double)userMessage.gold / 10000) + "元");
                            look_over_add_up_gold.setText(userMessage.gold + "");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        MobclickAgent.onPageStart("MainActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this); //统计时长
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.finish_my_wallet_head) {
            finish();
        } else if (i == R.id.my_wallet_withdraw) {
            if (userMessages != null) {
                intentActivity(WithdrawDepositActivity.class);
            }else {
                ToastUtils.showLong("请先登录哦！");
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        userMessages = null;
        MobclickAgent.onPageEnd("MainActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)，必须保证 onPageEnd 在 onPause 之前调用，因为SDK会在 onPause 中保存onPageEnd统计到的页面数据。
        MobclickAgent.onPause(this);
    }
}
