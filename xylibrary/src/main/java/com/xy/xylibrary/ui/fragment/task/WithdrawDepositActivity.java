package com.xy.xylibrary.ui.fragment.task;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.constellation.xylibrary.R;
import com.umeng.analytics.MobclickAgent;
import com.xy.xylibrary.Interface.BaseAdapterListener;
import com.xy.xylibrary.Interface.MyEdit;
import com.xy.xylibrary.base.AppContext;
import com.xy.xylibrary.base.BaseActivity;
import com.xy.xylibrary.base.BaseAdapter;
import com.xy.xylibrary.config.ScrollLinearLayoutManager;
import com.xy.xylibrary.ui.activity.login.LoginRequest;
import com.xy.xylibrary.ui.activity.login.RequestSyntony;
import com.xy.xylibrary.ui.activity.login.UserMessage;
import com.xy.xylibrary.ui.activity.task.LookOverDetailActivity;
import com.xy.xylibrary.ui.activity.task.WithdrawDeposit;
import com.xy.xylibrary.ui.activity.task.WithdrawalRecord;
import com.xy.xylibrary.utils.AutoVerticalScrollTextViewUtil;
import com.xy.xylibrary.utils.ToastUtils;
import com.xy.xylibrary.utils.Utils;
import com.xy.xylibrary.view.AutoVerticalScrollTextView;
import com.xy.xylibrary.view.MyEditText;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WithdrawDepositActivity extends BaseActivity implements View.OnClickListener,MyEdit,AppContext.UserGold {


    private TextView withdrawDepositListBar;
    private ImageView withdrawDepositFinishMyWalletHead;
    private TextView withdrawDepositMyWallet;
    private TextView withdrawDepositMyGold;
    private TextView withdrawDepositLookOverGold;
    private TextView withdrawDepositLookOverRMB;
    private LinearLayout withdrawDepositMyLinMoney;
    private TextView money10;
    private TextView money20;
    private TextView money30;
    private MyEditText inputBoxWechatID;
    private Button linxiasamo;
    private TextView withdraw_deposit_gold_money;
    private AutoVerticalScrollTextView withdrawal_keyword;
    private int money = 100000;
    private UserMessage userMessageData;


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MainActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this); //统计时长
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MainActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)，必须保证 onPageEnd 在 onPause 之前调用，因为SDK会在 onPause 中保存onPageEnd统计到的页面数据。
        MobclickAgent.onPause(this);
    }

    @Override
    protected Activity getContext() {
        return WithdrawDepositActivity.this;
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_withdraw_deposit;
    }

    @Override
    protected void loadViewLayout() {
        withdrawDepositListBar = findViewById(R.id.withdraw_deposit_list_bar);
        withdrawDepositFinishMyWalletHead = findViewById(R.id.withdraw_deposit_finish_my_wallet_head);
        withdrawDepositMyWallet = findViewById(R.id.withdraw_deposit_my_wallet);
        withdrawDepositMyGold = findViewById(R.id.withdraw_deposit_my_gold);
        withdrawDepositLookOverGold = findViewById(R.id.withdraw_deposit_look_over_gold);
        withdrawDepositLookOverRMB = findViewById(R.id.withdraw_deposit_look_over_RMB);
        withdrawDepositMyLinMoney = findViewById(R.id.withdraw_deposit_my_lin_money);
        withdrawal_keyword = findViewById(R.id.withdrawal_keyword);
        money10 = findViewById(R.id.money_10);
        money20 = findViewById(R.id.money_20);
        money30 = findViewById(R.id.money_30);
        inputBoxWechatID = findViewById(R.id.input_box_wechat_ID);
        linxiasamo = findViewById(R.id.withdrawal_linxiasamo);
        withdraw_deposit_gold_money = findViewById(R.id.withdraw_deposit_gold_money);
        ViewGroup.LayoutParams layoutParams = withdrawDepositListBar.getLayoutParams();
        layoutParams.height = Utils.getStatusBarHeight(WithdrawDepositActivity.this);
        withdrawDepositListBar.setLayoutParams(layoutParams);
        withdrawDepositFinishMyWalletHead.setOnClickListener(WithdrawDepositActivity.this);
        money10.setOnClickListener(WithdrawDepositActivity.this);
        money20.setOnClickListener(WithdrawDepositActivity.this);
        money30.setOnClickListener(WithdrawDepositActivity.this);
        linxiasamo.setOnClickListener(WithdrawDepositActivity.this);
        withdraw_deposit_gold_money.setOnClickListener(WithdrawDepositActivity.this);
     }

    @Override
    protected void bindViews() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        try {
            inputBoxWechatID.Clear(WithdrawDepositActivity.this);
            userMessageData = LitePal.findLast(UserMessage.class);
            if(userMessageData != null){
                 withdrawDepositLookOverGold.setText(userMessageData.gold+"");
                withdrawDepositLookOverRMB.setText("约"+Utils.doubleToString((double)userMessageData.gold/10000)+"元");
            }else {
                withdrawDepositLookOverGold.setText("==");
                withdrawDepositLookOverRMB.setText("约***元");
            }
            LoginRequest.getWeatherRequest().getWithdrawalRecordCarouselData(this, new RequestSyntony<WithdrawalRecord>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(WithdrawalRecord withdrawalRecord) {
                    // 初始化AutoVerticalScrollTextView控制器
                    AutoVerticalScrollTextViewUtil autoVerticalScrollTextViewUtil = new AutoVerticalScrollTextViewUtil(withdrawal_keyword, withdrawalRecord.getData().getWithdrawalsInfoVms());
                    // 设置滚动的时间间隔
                    autoVerticalScrollTextViewUtil.setDuration(5000);
                    // 开启滚动
                    autoVerticalScrollTextViewUtil.start();

                }
            });
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.withdraw_deposit_finish_my_wallet_head) {
            finish();
        }else if(i == R.id.withdrawal_linxiasamo){
            if(!TextUtils.isEmpty(inputBoxWechatID.getText())) {
                if (money > userMessageData.gold) {
                    ToastUtils.showLong("提现余额不足");
                } else {
                    LoginRequest.getWeatherRequest().getWithdrawDepositData(WithdrawDepositActivity.this, inputBoxWechatID.getText().toString(), money, new RequestSyntony<WithdrawDeposit>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(WithdrawDeposit withdrawDeposit) {
                            try {
                                if (withdrawDeposit != null && withdrawDeposit.isIsSuccess()) {
                                    ToastUtils.setView(null);
                                    ToastUtils.showLong("提取成功");
                                    AppContext.getUserInfo(WithdrawDepositActivity.this, "", "", WithdrawDepositActivity.this);
                                } else {
                                    ToastUtils.setView(null);
                                    ToastUtils.showLong("提取失败");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }else{
                ToastUtils.showLong("请输入微信号哦");
            }

        }else if(i == R.id.money_10){
            money10.setBackground(getResources().getDrawable(R.drawable.withdraw_search_10));
            money20.setBackground(getResources().getDrawable(R.drawable.search_bg_5));
            money30.setBackground(getResources().getDrawable(R.drawable.search_bg_5));
            money = 100000;
        }else if(i == R.id.money_20){
            money10.setBackground(getResources().getDrawable(R.drawable.search_bg_5));
            money20.setBackground(getResources().getDrawable(R.drawable.withdraw_search_10));
            money30.setBackground(getResources().getDrawable(R.drawable.search_bg_5));
            money = 200000;
        }else if(i == R.id.money_30){
            money10.setBackground(getResources().getDrawable(R.drawable.search_bg_5));
            money20.setBackground(getResources().getDrawable(R.drawable.search_bg_5));
            money30.setBackground(getResources().getDrawable(R.drawable.withdraw_search_10));
            money = 300000;
        }else if(i == R.id.withdraw_deposit_gold_money){
            Intent intent = new Intent(WithdrawDepositActivity.this,WithdrawalRecordActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void MyEditText() {

    }

    @Override
    public void OnTouch() {

    }

    @Override
    public void query(String content) {

    }



    @Override
    public void gold(UserMessage userMessage) {
         if(userMessage != null){
            withdrawDepositLookOverGold.setText(userMessage.gold+"");
            withdrawDepositLookOverRMB.setText("约"+Utils.doubleToString((double)userMessage.gold/10000)+"元");
        }else {
            withdrawDepositLookOverGold.setText("==");
            withdrawDepositLookOverRMB.setText("约***元");
        }
    }
}
