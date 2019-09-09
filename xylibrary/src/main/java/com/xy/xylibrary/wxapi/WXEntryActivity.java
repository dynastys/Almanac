package com.xy.xylibrary.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.xy.xylibrary.base.AppContext;
import com.xy.xylibrary.ui.activity.login.BindWechat;
import com.xy.xylibrary.ui.activity.login.LoginActivity;
import com.xy.xylibrary.ui.activity.login.LoginConnextor;
import com.xy.xylibrary.ui.activity.login.LoginRequest;
import com.xy.xylibrary.ui.activity.login.LoginTypeActivity;
import com.xy.xylibrary.ui.activity.login.RequestSyntony;
import com.xy.xylibrary.ui.activity.login.UserMessage;
import com.xy.xylibrary.ui.activity.login.WeChat;
import com.xy.xylibrary.ui.activity.login.WeChatLogin;
import com.xy.xylibrary.utils.SaveShare;
import com.xy.xylibrary.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler, RequestSyntony<WeChatLogin> {
    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private static final int RETURN_MSG_TYPE_SHARE = 2;
    private UserMessage userMessageData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果没回调onResp，八成是这句没有写
        AppContext.mWxApi.handleIntent(getIntent(), this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    //app发送消息给微信，处理返回消息的回调
    @Override
    public void onResp(BaseResp resp) {
        try {
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    if (RETURN_MSG_TYPE_SHARE == resp.getType()) ToastUtils.showLong("分享失败");
                    else ToastUtils.showLong("登录失败");
                    break;
                case BaseResp.ErrCode.ERR_OK:
                    switch (resp.getType()) {
                        case RETURN_MSG_TYPE_LOGIN:
                            //拿到了微信返回的code,立马再去请求access_token
                            String code = ((SendAuth.Resp) resp).code;
                            WeChatLogin(code);
//                            Log.e("code", "onResp: "+code );
                            break;
                        case RETURN_MSG_TYPE_SHARE:
                            ToastUtils.showLong("微信分享成功");
                            finish();
                            break;
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //LoginActivity的login方法
    public void WeChatLogin(String verifyCode) {
        try {
            if (AppContext.ISLOGIN) {
                LoginRequest.getWeatherRequest().getWeChatLoginData(WXEntryActivity.this, verifyCode, WXEntryActivity.this);
            } else {
                userMessageData = LitePal.findLast(UserMessage.class);
                LoginRequest.getWeatherRequest().getBindWechatData(WXEntryActivity.this, verifyCode, userMessageData.uesrid, new RequestSyntony<BindWechat>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(BindWechat bindWechat) {
                        if (bindWechat != null && bindWechat.getData() != null) {
                            List<UserMessage> list = new ArrayList<>();
                            userMessageData.openid = bindWechat.getData().getOpenid();
                            userMessageData.nickname = bindWechat.getData().getNickname();
                            userMessageData.sex = bindWechat.getData().getSex();
                            userMessageData.headimgurl = bindWechat.getData().getHeadimgurl();
                            userMessageData.unionid = bindWechat.getData().getUnionid();
                            SaveShare.saveValue(WXEntryActivity.this, "nickname", userMessageData.nickname);
                            SaveShare.saveValue(WXEntryActivity.this, "headimgurl", userMessageData.headimgurl);
                            list.add(userMessageData);
                            LitePal.saveAll(list);
                        } else {
                            ToastUtils.showLong("绑定微信失败");
                        }

                        finish();

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        Log.e("TAG", "onError: " + e.getMessage());
        ToastUtils.showLong("登录失败");
        finish();
    }

    @Override
    public void onNext(WeChatLogin weChat) {
        try {
            if (weChat != null && weChat.getData() != null) {
                LitePal.deleteAll(UserMessage.class);
                List<UserMessage> list = new ArrayList<>();
                if (userMessageData == null) {
                    userMessageData = new UserMessage();
                }
                userMessageData.openid = weChat.getData().getOpenid();
                userMessageData.nickname = weChat.getData().getNickname();
                userMessageData.sex = weChat.getData().getSex();
                userMessageData.headimgurl = weChat.getData().getHeadimgurl();
                userMessageData.unionid = weChat.getData().getUnionid();
                userMessageData.uesrid = weChat.getData().getUserVm().getId();
                if (weChat.getData().getUserVm().getMobile() == 0) {
                    Intent intent = new Intent(WXEntryActivity.this, LoginActivity.class);
                    intent.putExtra("type", "微信");
                    startActivity(intent);
                } else {
                    if (weChat.getData().getUserVm() != null) {
                        userMessageData.uesrid = weChat.getData().getUserVm().getId();
                        userMessageData.mobile = weChat.getData().getUserVm().getMobile();
                        userMessageData.name = weChat.getData().getUserVm().getName();
                        userMessageData.passWord = weChat.getData().getUserVm().getPassWord();
                        userMessageData.wxid = weChat.getData().getUserVm().getWxid();
                        userMessageData.openid = weChat.getData().getUserVm().getWxid();
                        userMessageData.img = weChat.getData().getUserVm().getImg();
                        userMessageData.gold = weChat.getData().getUserVm().getGold();
//            userMessageData.vCode = weChat.getData().getUserVm().getVCode();
                        userMessageData.active = weChat.getData().getUserVm().getActive();
                        userMessageData.createTime = weChat.getData().getUserVm().getCreateTime();
                        userMessageData.updateTime = weChat.getData().getUserVm().getUpdateTime();
                        userMessageData.isDelete = weChat.getData().getUserVm().isIsDelete();
                        SaveShare.saveValue(WXEntryActivity.this, "userId", weChat.getData().getUserVm().getId());
                        SaveShare.saveValue(WXEntryActivity.this, "Phone", weChat.getData().getUserVm().getMobile() + "");
                    }
                    ToastUtils.showLong("登录成功");
                }
                list.add(userMessageData);
                LitePal.saveAll(list);
                finish();
            } else {
                ToastUtils.showLong("登录失败");
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        SaveShare.saveValue(WXEntryActivity.this, "userId", phone.getData().getId());
//        SaveShare.saveValue(WXEntryActivity.this, "Phone", phone.getData().getMobile()+"");
    }
}
