package com.xy.xylibrary.ui.fragment.task;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.constellation.xylibrary.R;
import com.xy.xylibrary.Interface.MyEdit;
import com.xy.xylibrary.base.AppContext;
import com.xy.xylibrary.base.BaseActivity;
import com.xy.xylibrary.ui.activity.login.UserMessage;
import com.xy.xylibrary.ui.activity.task.LookOverDetailActivity;
import com.xy.xylibrary.utils.ToastUtils;
import com.xy.xylibrary.utils.Utils;
import com.xy.xylibrary.view.MyEditText;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WithdrawDepositActivity extends BaseActivity implements View.OnClickListener,MyEdit {


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
    private TextView withdrawalInstructions;
    private int money = 100000;
    private UserMessage userMessageData;
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
        money10 = findViewById(R.id.money_10);
        money20 = findViewById(R.id.money_20);
        money30 = findViewById(R.id.money_30);
        inputBoxWechatID = findViewById(R.id.input_box_wechat_ID);
        linxiasamo = findViewById(R.id.withdrawal_linxiasamo);
        withdrawalInstructions = findViewById(R.id.withdrawal_instructions);
        ViewGroup.LayoutParams layoutParams = withdrawDepositListBar.getLayoutParams();
        layoutParams.height = Utils.getStatusBarHeight(WithdrawDepositActivity.this);
        withdrawDepositListBar.setLayoutParams(layoutParams);
        withdrawDepositFinishMyWalletHead.setOnClickListener(WithdrawDepositActivity.this);
        money10.setOnClickListener(WithdrawDepositActivity.this);
        money20.setOnClickListener(WithdrawDepositActivity.this);
        money30.setOnClickListener(WithdrawDepositActivity.this);
        linxiasamo.setOnClickListener(WithdrawDepositActivity.this);
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
            if(!TextUtils.isEmpty(inputBoxWechatID.getText())){
                if(money > userMessageData.gold){
                    ToastUtils.showLong("提现余额不足");
                }else{

                }
            }else{
                ToastUtils.showLong("请输入提现微信号哦！");
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
    public void onResume() {
        super.onResume();
         if (inputBoxWechatID != null) {
            IMEClose(inputBoxWechatID);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
         if (inputBoxWechatID != null) {
            IMEClose(inputBoxWechatID);
        }
    }
}
