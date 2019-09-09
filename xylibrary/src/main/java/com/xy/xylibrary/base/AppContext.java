package com.xy.xylibrary.base;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.constellation.xylibrary.R;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xy.xylibrary.signin.FinishTask;
import com.xy.xylibrary.signin.SignIn;
import com.xy.xylibrary.ui.activity.login.LoginActivity;
import com.xy.xylibrary.ui.activity.login.LoginRequest;
import com.xy.xylibrary.ui.activity.login.RequestSyntony;
import com.xy.xylibrary.ui.activity.login.UserInfo;
import com.xy.xylibrary.ui.activity.login.UserMessage;
import com.xy.xylibrary.utils.SaveShare;
import com.xy.xylibrary.utils.ToastUtils;
import com.yilan.sdk.user.YLUser;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zw on 2019/8/8 0008.
 */
public class AppContext{

    private static AppContext sContext = null;
    private static String WEIXIN_APP_ID = "wxe9fd87db95f74d66";
     public static IWXAPI mWxApi;
    public static boolean ISLOGIN;
    public static UserMessage userMessageData;
    /**
     * 微信登陆
     */
    public static void registToWX(Context context) {
        //AppConst.WEIXIN.APP_ID是指你应用在微信开放平台上的AppID，记得替换。
        mWxApi = WXAPIFactory.createWXAPI(context, WEIXIN_APP_ID, true);
        // 将该app注册到微信
        mWxApi.registerApp(WEIXIN_APP_ID);

    }

    public static void wxLogin() {
        if (!AppContext.mWxApi.isWXAppInstalled()) {
            ToastUtils.showLong("您还未安装微信客户端");
            return;
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "diandi_wx_login";
        AppContext.mWxApi.sendReq(req);
    }

    public interface UserGold{
        void gold(UserMessage userMessage);
    }
    /**
     * 获取用户信息
     * */
   public static void getUserInfo(final Context context, String appid, final String uesrid, final UserGold userGold){
       LoginRequest.getWeatherRequest().getUserInfoData(context, appid, uesrid, new RequestSyntony<UserInfo>() {
           @Override
           public void onCompleted() {

           }

           @Override
           public void onError(Throwable e) {

           }

           @Override
           public void onNext(UserInfo userInfo) {
               userMessageData = LitePal.findLast(UserMessage.class);
               if(userMessageData == null){
                   userMessageData = new UserMessage();
               }
               if(userInfo != null && userInfo.getData() != null){
                   List<UserMessage> list = new ArrayList<>();
                   userMessageData.uesrid = userInfo.getData().getId();
                   userMessageData.mobile = userInfo.getData().getMobile();
                   userMessageData.name = userInfo.getData().getName();
                   userMessageData.passWord = userInfo.getData().getPassWord();
                   userMessageData.wxid = userInfo.getData().getWxid();
                   userMessageData.img = userInfo.getData().getImg();
                   userMessageData.gold = userInfo.getData().getGold();
                   userMessageData.active = userInfo.getData().getActive();
                   userMessageData.createTime = userInfo.getData().getCreateTime();
                   userMessageData.updateTime = userInfo.getData().getUpdateTime();
                   userMessageData.isDelete = userInfo.getData().isIsDelete();
                   userMessageData.openid = userInfo.getData().getWxid();
                   if(!TextUtils.isEmpty(userInfo.getData().getWxid()) && !TextUtils.isEmpty(SaveShare.getValue(context,"nickname"))){
                       userMessageData.nickname = SaveShare.getValue(context,"nickname");
                       userMessageData.headimgurl = SaveShare.getValue(context,"headimgurl");
                   }
                   list.add(userMessageData);
                   LitePal.saveAll(list);
                   SaveShare.saveValue(context, "userId", userInfo.getData().getId());
                   SaveShare.saveValue(context, "Phone", userInfo.getData().getMobile() + "");
                   YLUser.getInstance().login(userMessageData.name, userMessageData.headimgurl, userMessageData.mobile+"", userMessageData.uesrid);
               }else{
                   YLUser.getInstance().logout();
                   if(!TextUtils.isEmpty(SaveShare.getValue(context, "userId"))){
                       ToastUtils.showLong("登录失效，请重新登录");
                   }
                   LitePal.deleteAll(UserMessage.class);
                   userMessageData = new UserMessage();
                   SaveShare.saveValue(context, "userId", "");
                   SaveShare.saveValue(context, "Phone",  "");
               }
               if(userGold != null && !TextUtils.isEmpty(userMessageData.uesrid)){
                   userGold.gold(userMessageData);
               }
           }
       });
    }

    /**
     * 任务完成
     *
     * @param appid
     * @param id       任务id
     * @param isDouble 是否翻倍
     */
    public static void FinishTask(Context context, String appid, String id, boolean isDouble) {
        LoginRequest.getWeatherRequest().getFinishTaskData(context, appid,  "", id, isDouble, new RequestSyntony<FinishTask>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(FinishTask finishTask) {
                try {
                    if(finishTask.getData() > 0){
                        ToastUtils.setView(R.layout.toast_show);
                        View view = ToastUtils.getView();
                        ((TextView) view.findViewById(R.id.add_money)).setText("+" + finishTask.getData());
                        ToastUtils.showLong("");
                        ToastUtils.setView(null);
                        Log.e("FinishTask", "onNext: " + finishTask.getData());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static AppContext context() {
        return sContext;
    }

}
