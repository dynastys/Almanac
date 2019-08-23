package com.xy.xylibrary.ui.activity.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.constellation.xylibrary.R;
import com.xy.xylibrary.base.BaseActivity;
import com.xy.xylibrary.utils.GlideUtil;
import com.xy.xylibrary.utils.SaveShare;
import com.xy.xylibrary.utils.ToastUtils;
import com.xy.xylibrary.utils.UpdateDialog;
import com.xy.xylibrary.utils.Utils;
import com.xy.xylibrary.view.MagicButton;

import org.json.JSONObject;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText mobile_login;
    private EditText yanzhengma;
    private Button getyanzhengma1;
    private Button login_btn;
    private CardView viewCard;
    private TextView shopLoginTv;
    private MagicButton magicLoginBtn;
    private CircleImageView imgAvatar;
    private TextView wechatName;
    private UserMessage userMessageData;
    private int countSeconds = 60;//倒计时秒数
    private Context mContext;
    private String usersuccess;
    //    private LoginRequest loginRequest;
    private String type;
    private int code;
    private String vCode = "";

    @SuppressLint("HandlerLeak")
    private Handler mCountHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (countSeconds > 0) {
                --countSeconds;
                getyanzhengma1.setText("(" + countSeconds + ")后获取验证码");
                mCountHandler.sendEmptyMessageDelayed(0, 1000);
            } else {
                countSeconds = 60;
                getyanzhengma1.setText("请重新获取验证码");
            }
        }
    };
    private String userinfomsg;

    @Override
    protected Activity getContext() {
        mContext = LoginActivity.this;
        type = getIntent().getStringExtra("type");
        return LoginActivity.this;
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_login;
    }

    @Override
    protected void loadViewLayout() {
        try {
            setIsUserLightMode(true);
            mobile_login = findViewById(R.id.mobile_login);
            yanzhengma = findViewById(R.id.yanzhengma);
            getyanzhengma1 = findViewById(R.id.getyanzhengma1);
            login_btn = findViewById(R.id.login_btn);
            viewCard = findViewById(R.id.view_card);
            shopLoginTv = findViewById(R.id.shop_login_tv);
            magicLoginBtn = findViewById(R.id.magic_login_btn);
            imgAvatar = findViewById(R.id.img_avatar);
            wechatName = findViewById(R.id.wechat_name);
            getyanzhengma1.setOnClickListener(this);
            login_btn.setOnClickListener(this);
            ViewGroup.LayoutParams layoutParams = shopLoginTv.getLayoutParams();
            layoutParams.height = Utils.getStatusBarHeight(LoginActivity.this);
            shopLoginTv.setLayoutParams(layoutParams);

            switch (type) {
                case "微信":
                    login_btn.setText("绑定手机号");
                    code = 4;
                    userMessageData = LitePal.findLast(UserMessage.class);
                    if (userMessageData != null) {
                        wechatName.setText(TextUtils.isEmpty(userMessageData.nickname) ? "" : userMessageData.nickname);
                        if (!TextUtils.isEmpty(userMessageData.headimgurl)) {
                            GlideUtil.getGlideUtil().setImages(LoginActivity.this,userMessageData.headimgurl, imgAvatar,0);
                        }

                    }

                    break;
                case "手机":
    //                wechatName.setVisibility(View.GONE);
    //                imgAvatar.setVisibility(View.GONE);
                    login_btn.setText("登录");
                    code = 1;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @Override
    public void onClick(View v) {
        try {
            int i = v.getId();
            if (i == R.id.getyanzhengma1) {
                if (countSeconds == 60) {
                    String mobile = mobile_login.getText().toString();
                    getMobiile(mobile);
                } else {
                    ToastUtils.showLong("不能重复发送验证码");
                }

            } else if (i == R.id.login_btn) {
                login();

            } else if (i == R.id.magic_login_btn) {
                QuitDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }


    //获取信息进行登录
    public void login() {
        String mobile = null;
        String verifyCode = null;
        try {
            mobile = mobile_login.getText().toString().trim();
            verifyCode = yanzhengma.getText().toString().trim();
            if (TextUtils.isEmpty(mobile) || TextUtils.isEmpty(verifyCode)) {
                new AlertDialog.Builder(mContext).setTitle("提示").setMessage("手机号码或验证码不能为空！").setCancelable(true).show();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (verifyCode.equals(vCode)) {
//                LitePal.deleteAll(UserMessage.class);
                LoginRequest.getWeatherRequest().getQueryEntryData(LoginActivity.this, mobile, verifyCode, userMessageData == null?"":TextUtils.isEmpty(userMessageData.openid) ? "" : userMessageData.openid, new RequestSyntony<Phone>() {
                    @Override
                    public void onCompleted() {
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Phone phone) {
                        if (phone.getData() != null) {
                            List<UserMessage> list = new ArrayList<>();
                            if(userMessageData == null){
                                userMessageData = new UserMessage();
                            }
                            userMessageData.uesrid = phone.getData().getId();
                            userMessageData.mobile = phone.getData().getMobile();
                            userMessageData.name = phone.getData().getName();
                            userMessageData.passWord = phone.getData().getPassWord();
                            userMessageData.wxid = phone.getData().getWxid();
                            userMessageData.img = phone.getData().getImg();
                            userMessageData.gold = phone.getData().getGold();
                            userMessageData.vCode = phone.getData().getVCode();
                            userMessageData.active = phone.getData().getActive();
                            userMessageData.createTime = phone.getData().getCreateTime();
                            userMessageData.updateTime = phone.getData().getUpdateTime();
                            userMessageData.isDelete = phone.getData().isIsDelete();
                            list.add(userMessageData);
                            LitePal.saveAll(list);
                            SaveShare.saveValue(LoginActivity.this, "userId", phone.getData().getId());
                            SaveShare.saveValue(LoginActivity.this, "Phone", phone.getData().getMobile() + "");
                        }
                    }
                });
            } else {
                ToastUtils.showLong("验证码不匹配");
                yanzhengma.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //获取验证码信息，判断是否有手机号码
    private void getMobiile(String mobile) {
        try {
            if ("".equals(mobile)) {
                new AlertDialog.Builder(mContext).setTitle("提示").setMessage("手机号码不能为空").setCancelable(true).show();
            } else if (isMobileNO(mobile) == false) {
                new AlertDialog.Builder(mContext).setTitle("提示").setMessage("请输入正确的手机号码").setCancelable(true).show();
            } else {
                requestVerifyCode(mobile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取验证码信息，进行验证码请求
    private void requestVerifyCode(String mobile) {
        try {
            LoginRequest.getWeatherRequest().getQueryRegisteData(LoginActivity.this, mobile, new RequestSyntony<RegisteJson>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onNext(RegisteJson registeJson) {
                    if (registeJson != null && !TextUtils.isEmpty(registeJson.getData())) {
                        vCode = registeJson.getData();
                    }
                }
            });
            startCountBack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //使用正则表达式判断电话号码
    public static boolean isMobileNO(String tel) {
        Pattern p = Pattern.compile("^(13[0-9]|15([0-3]|[5-9])|14[5,7,9]|17[1,3,5,6,7,8]|18[0-9])\\d{8}$");
        Matcher m = p.matcher(tel);
        System.out.println(m.matches() + "---");
        return m.matches();
    }

    //获取验证码信息,进行计时操作
    private void startCountBack() {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getyanzhengma1.setText(countSeconds + "");
                mCountHandler.sendEmptyMessage(0);
            }
        });
    }


    @Override
    public boolean onKeyDown(final int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK://监控/拦截/屏蔽返回键
                QuitDialog();
                return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void QuitDialog() {
        try {
            final UpdateDialog confirmDialog = new UpdateDialog(LoginActivity.this, "提示", "绑定", "以后再说", "退出将导致无法登录");
            confirmDialog.show();
            confirmDialog.setClicklistener(new UpdateDialog.ClickListenerInterface() {
                @Override
                public void doConfirm() {
                    confirmDialog.dismiss();
                }

                @Override
                public void doCancel() {
    //                LitePal.deleteAll(WeChat.DataBean.class);
                    confirmDialog.dismiss();
                    finish();
                    ToastUtils.showLong("登录失败");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void granted() {

    }

    @Override
    public void denied(List<String> deniedList) {

    }
}
