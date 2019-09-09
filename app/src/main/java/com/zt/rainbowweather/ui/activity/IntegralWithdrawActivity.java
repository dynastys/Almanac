package com.zt.rainbowweather.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xy.xylibrary.base.BaseActivity;
import com.xy.xylibrary.request.RequestConnextor;
import com.xy.xylibrary.ui.activity.login.Exit;
import com.xy.xylibrary.ui.activity.login.LoginTypeActivity;
import com.xy.xylibrary.ui.activity.login.RequestSyntony;
import com.xy.xylibrary.ui.activity.login.UserMessage;
import com.xy.xylibrary.ui.activity.login.WeChat;
import com.xy.xylibrary.ui.fragment.task.TaskType;
import com.xy.xylibrary.utils.GlideUtil;
import com.xy.xylibrary.utils.SaveShare;
import com.xy.xylibrary.utils.ToastUtils;
import com.xy.xylibrary.utils.UpdateDialog;
import com.xy.xylibrary.utils.Utils;
import com.yilan.sdk.common.util.ToastUtil;
import com.zt.weather.R;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class IntegralWithdrawActivity extends BaseActivity implements RequestSyntony<Exit> {


    @BindView(R.id.integral_withdraw_tv)
    TextView integralWithdrawTv;
    @BindView(R.id.finish_file_head)
    ImageView finishFileHead;
    @BindView(R.id.file_head_title)
    TextView fileHeadTitle;
    @BindView(R.id.file_co_option)
    TextView fileCoOption;
    @BindView(R.id.integral_withdraw_image)
    CircleImageView integralWithdrawImage;
    @BindView(R.id.quit_login_slay_sign)
    LinearLayout quitLoginSlaySign;
    @BindView(R.id.versions_lay_sign)
    LinearLayout versionsLaySign;
    @BindView(R.id.versions_tv)
    TextView versionsTv;
    @BindView(R.id.nickname)
    TextView nickname;
    @BindView(R.id.quit_login)
    TextView quitLogin;
    private String phone;
    private UserMessage phoneDta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Activity getContext() {
        return IntegralWithdrawActivity.this;
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_integral_withdraw;
    }

    @Override
    protected void loadViewLayout() {
        ViewGroup.LayoutParams layoutParams = integralWithdrawTv.getLayoutParams();
        layoutParams.height = Utils.getStatusBarHeight(IntegralWithdrawActivity.this);
        integralWithdrawTv.setLayoutParams(layoutParams);
        finishFileHead.setVisibility(View.VISIBLE);
        fileHeadTitle.setVisibility(View.VISIBLE);
        fileHeadTitle.setText(getResources().getString(R.string.setting));
        versionsTv.setText(Utils.getAppInfo(IntegralWithdrawActivity.this));
        wxLogin();
    }

    @Override
    protected void bindViews() {
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
    }

    @Override
    protected void setListener() {
    }

    @OnClick({R.id.finish_file_head, R.id.quit_login_slay_sign, R.id.versions_lay_sign})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.finish_file_head:
                finish();
                break;
            case R.id.quit_login_slay_sign:
                if (TextUtils.isEmpty(phone)) {
                    phoneDta = LitePal.findLast(UserMessage.class);
                    if (phoneDta != null) {
                        LitePal.deleteAll(UserMessage.class);
                        onResume();
                    } else {
                        Intent intent1 = new Intent(IntegralWithdrawActivity.this, LoginTypeActivity.class);
                        startActivity(intent1);
                    }
                } else {
                    Cancellation();

                }

                break;
            case R.id.versions_lay_sign:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(versionsTv != null){
            wxLogin();
        }
    }
    //弹出框
    public void Cancellation(){
        final UpdateDialog confirmDialog = new UpdateDialog(IntegralWithdrawActivity.this, "提示", "确定", "以后再说", "退出之后就不能回去金币了哦！是否退出?");
        confirmDialog.show();
        confirmDialog.setClicklistener(new UpdateDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                try {
                    confirmDialog.dismiss();
                    SaveShare.saveValue(IntegralWithdrawActivity.this, "userId", "");
                    SaveShare.saveValue(IntegralWithdrawActivity.this, "Phone", "");
                    LitePal.deleteAll(UserMessage.class);
                    LitePal.deleteAll(TaskType.class);

                    onResume();
                    ToastUtils.setView(null);
                    ToastUtils.showLong("退出成功");
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                RequestConnextor.getConnextor().Cancel(IntegralWithdrawActivity.this);
            }

            @Override
            public void doCancel() {
                confirmDialog.dismiss();
            }
        });
    }
    public void wxLogin() {
        try {
            phoneDta = LitePal.findLast(UserMessage.class);
            phone = SaveShare.getValue(IntegralWithdrawActivity.this, "Phone");
            if (phoneDta != null && !TextUtils.isEmpty(phoneDta.headimgurl)) {
                GlideUtil.getGlideUtil().setImages(IntegralWithdrawActivity.this, phoneDta.headimgurl, integralWithdrawImage,1);
            }
            if (!TextUtils.isEmpty(phone)) {
                quitLogin.setText(getResources().getString(R.string.quit_login));
                if(TextUtils.isEmpty(phoneDta.nickname)){
                    nickname.setText(phoneDta.name);
                }else{
                    nickname.setText(phoneDta.nickname);
                }
            } else if (!TextUtils.isEmpty(phone)) {
                if(TextUtils.isEmpty(phoneDta.nickname)){
                    nickname.setText(phoneDta.name);
                }else{
                    nickname.setText(phoneDta.nickname);
                }
                quitLogin.setText(getResources().getString(R.string.quit_login));
            } else {
                nickname.setText("未登录");
                integralWithdrawImage.setImageResource(R.mipmap.defa_head);
                quitLogin.setText("登录");
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(Exit exit) {


    }

    @Override
    public void granted() {

    }

    @Override
    public void denied(List<String> deniedList) {

    }
}
