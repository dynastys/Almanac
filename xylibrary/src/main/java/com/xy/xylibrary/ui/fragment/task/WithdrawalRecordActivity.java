package com.xy.xylibrary.ui.fragment.task;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.constellation.xylibrary.R;
import com.umeng.analytics.MobclickAgent;
import com.xy.xylibrary.Interface.BaseAdapterListener;
import com.xy.xylibrary.base.BaseActivity;
import com.xy.xylibrary.base.BaseAdapter;
import com.xy.xylibrary.config.ScrollLinearLayoutManager;
import com.xy.xylibrary.ui.activity.login.LoginRequest;
import com.xy.xylibrary.ui.activity.login.RequestSyntony;
import com.xy.xylibrary.ui.activity.login.UserActiveInfo;
import com.xy.xylibrary.ui.activity.task.LookOverDetailActivity;
import com.xy.xylibrary.ui.activity.task.LookOverDetailLogic;
import com.xy.xylibrary.ui.activity.task.WithdrawalRecord;
import com.xy.xylibrary.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WithdrawalRecordActivity extends BaseActivity implements View.OnClickListener{


    private TextView withdrawalRecordListBar;
    private ImageView withdrawalRecordFinishMyWalletHead;
    private TextView withdrawalRecordMyWallet;
    private TextView WithdrawalRecord;
    private RecyclerView withdrawalRecordDetailList;

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
        return WithdrawalRecordActivity.this;
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_withdrawal_record;
    }

    @Override
    protected void loadViewLayout() {
        withdrawalRecordListBar = findViewById(R.id.withdrawal_record_list_bar);
        withdrawalRecordFinishMyWalletHead = findViewById(R.id.withdrawal_record_finish_my_wallet_head);
        withdrawalRecordMyWallet = findViewById(R.id.withdrawal_record_my_wallet);
        WithdrawalRecord = findViewById(R.id.Withdrawal_record);
        withdrawalRecordDetailList = findViewById(R.id.withdrawal_record_detail_list);
        ViewGroup.LayoutParams layoutParams = withdrawalRecordListBar.getLayoutParams();
        layoutParams.height = Utils.getStatusBarHeight(WithdrawalRecordActivity.this);
        withdrawalRecordListBar.setLayoutParams(layoutParams);
        withdrawalRecordFinishMyWalletHead.setOnClickListener(WithdrawalRecordActivity.this);
    }

    @Override
    protected void bindViews() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        try {
            LoginRequest.getWeatherRequest().getWithdrawalRecordData(WithdrawalRecordActivity.this, new RequestSyntony<com.xy.xylibrary.ui.activity.task.WithdrawalRecord>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(WithdrawalRecord withdrawalRecord) {
                   if(withdrawalRecord != null && withdrawalRecord.getData() != null && withdrawalRecord.getData().getWithdrawalsInfoVms().size() > 0){
                       ScrollLinearLayoutManager setScrollEnable = new ScrollLinearLayoutManager(WithdrawalRecordActivity.this);
                       setScrollEnable.setScrollEnable(false);
                       withdrawalRecordDetailList.setLayoutManager(setScrollEnable);
                       BaseAdapter baseAdapter = new BaseAdapter<>(R.layout.recycler_item_look_gold, withdrawalRecord.getData().getWithdrawalsInfoVms(), new BaseAdapterListener<com.xy.xylibrary.ui.activity.task.WithdrawalRecord.DataBean.WithdrawalsInfoVmsBean>() {
                           @Override
                           public void convertView(BaseViewHolder viewHolder, com.xy.xylibrary.ui.activity.task.WithdrawalRecord.DataBean.WithdrawalsInfoVmsBean item) {
                               viewHolder.setText(R.id.look_gold_title_tv,"用户名");
                               viewHolder.setText(R.id.look_gold_details,  item.getName());
                               viewHolder.setText(R.id.look_gold_money,   item.getGold()+"金币");
                               viewHolder.setText(R.id.look_gold_time, Utils.times(item.getUpdateTime()));
                           }
                       });

                       withdrawalRecordDetailList.addItemDecoration(new DividerItemDecoration(WithdrawalRecordActivity.this, 1));
                       withdrawalRecordDetailList.setAdapter(baseAdapter);
                   }
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
        if (i == R.id.withdrawal_record_finish_my_wallet_head) {
            finish();
        }
    }
}
