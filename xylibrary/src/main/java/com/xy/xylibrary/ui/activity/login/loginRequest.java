package com.xy.xylibrary.ui.activity.login;

import android.content.Context;
import android.text.TextUtils;

import com.xy.xylibrary.signin.ActiveValue;
import com.xy.xylibrary.signin.AppInviteList;
import com.xy.xylibrary.signin.AppSignInList;
import com.xy.xylibrary.signin.AppTaskList;
import com.xy.xylibrary.signin.CompleteActive;
import com.xy.xylibrary.signin.FinishTask;
import com.xy.xylibrary.signin.InvitedUsers;
import com.xy.xylibrary.signin.SignIn;
import com.xy.xylibrary.ui.activity.task.WithdrawDeposit;
import com.xy.xylibrary.ui.activity.task.WithdrawalRecord;
import com.xy.xylibrary.utils.AESUtils;
import com.xy.xylibrary.utils.RomUtils;
import com.xy.xylibrary.utils.SaveShare;
import org.json.JSONException;
import org.json.JSONObject;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class LoginRequest {

    private CompositeSubscription mSubscriptions = new CompositeSubscription();
    private static LoginRequest loginRequest;
//    private String Url = "http://api.integrals.xingyuntianqi.com/";
     private String Url =  "http://47.110.52.151:8012/";//测试

    private String SignID = "A5AE4ED9-214C-4082-902A-3A8E31996417";//签到ID
//    private String userID = "e7a3695d-c7e7-4821-8ac3-e8a05eb9c698";//    用户ID
    private String TaskID = "28CC8264-B564-4986-8F77-D08645B73533";//任务ID
    public static String AppID = "08C1948F-48FC-4FD6-899E-EDA7672B2250"; // appid
    private String multitaskingID = "28CC8264-B564-4986-8F77-D08615B73533";
    public static LoginRequest getWeatherRequest() {
        if(loginRequest == null){
         synchronized (LoginRequest.class){
             if(loginRequest == null){
                 loginRequest = new LoginRequest();
             }
         }
        }
        return loginRequest;
    }

    /**
     * 手机登录
     * */
    public void getQueryEntryData(Context context,String mobile,String vCode,String wxID,final RequestSyntony<Phone> requestSyntony){
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("mobile", mobile);//"1333333333"
            requestData.put("vCode", vCode);//"123"
            requestData.put("wxID",wxID);
            requestData.put("appId",AppID);//"62B0E5C9-F486-48B3-8B57-11F00B676F3E"
            requestData.put("channel_code", RomUtils.app_youm_code);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSubscriptions.add(LoginConnextor.getConnextor(context).getAppService(LoginApi.class,Url).QueryEntryList(JSONObjectData(context,requestData.toString()))
                 .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<Phone>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(Phone phone) {
                        requestSyntony.onNext(phone);
                    }
                })
        );
    }

    /**
     * 验证码
     * */
    public void getQueryRegisteData(Context context,String mobile,final RequestSyntony<RegisteJson> requestSyntony){
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("mobile", mobile);
            requestData.put("appId",AppID);//"62B0E5C9-F486-48B3-8B57-11F00B676F3E"
        } catch (JSONException e) {
            e.printStackTrace();
        }


        mSubscriptions.add(LoginConnextor.getConnextor(context).getAppService(LoginApi.class,Url).QueryRegisteList(JSONObjectData(context,requestData.toString()))
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<RegisteJson>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(RegisteJson registeJson) {
                        requestSyntony.onNext(registeJson);
                    }
                })
        );
    }

    /**
     * 绑定手机号
     * */
    public void getBindingPhoneData(Context context,String mobile,String vCode,String userId,final RequestSyntony<Phone> requestSyntony){
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("mobile", mobile);//"1333333333"
            requestData.put("vCode", vCode);//"123"
            requestData.put("userId", userId);
            requestData.put("appId",AppID);//"62B0E5C9-F486-48B3-8B57-11F00B676F3E"
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSubscriptions.add(LoginConnextor.getConnextor(context).getAppService(LoginApi.class,Url).BindingPhone(JSONObjectData(context,requestData.toString()))
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<Phone>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(Phone phone) {
                        requestSyntony.onNext(phone);
                    }
                })
        );
    }

    /**
     * 微信登录
     * */
    public void getWeChatLoginData(Context context,String code,final RequestSyntony<WeChatLogin> requestSyntony){
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("code", code);
            requestData.put("appId",AppID);//"62B0E5C9-F486-48B3-8B57-11F00B676F3E"
            requestData.put("channel_code", RomUtils.app_youm_code);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSubscriptions.add(LoginConnextor.getConnextor(context).getAppService(LoginApi.class,Url).WeChatLogin(JSONObjectData(context,requestData.toString()))
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<WeChatLogin>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(WeChatLogin weChat) {
                        requestSyntony.onNext(weChat);
                    }
                })
        );
    }
    /**
     * *获取用户信息 访问时带请求头Toke访问验证身份
     * */
    public void getUsersMessageData(Context context,String location,final RequestSyntony<WeChat> requestSyntony){
        mSubscriptions.add(LoginConnextor.getConnextor(context).getAppService(LoginApi.class,"http://api.xytq.qukanzixun.com/").UsersMessage()
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<WeChat>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(WeChat weChat) {
                        requestSyntony.onNext(weChat);
                    }
                })
        );
    }

    /**
     * *注册用户绑定微信号
     * */
    public void getBindWechatData(Context context, String verifyCode, String code, final RequestSyntony<BindWechat> requestSyntony){
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("code", code);//"021NN4NV0bJPP22xBpPV0V2kNV0NN4Nm"
            if(!TextUtils.isEmpty(SaveShare.getValue(context, "userId"))){
                requestData.put("userId", SaveShare.getValue(context, "userId"));
            }
            requestData.put("appId",AppID);//"62B0E5C9-F486-48B3-8B57-11F00B676F3E"
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSubscriptions.add(LoginConnextor.getConnextor(context).getAppService(LoginApi.class,Url).BindWechat(JSONObjectData(context,requestData.toString()))
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<BindWechat>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(BindWechat bindWechat) {
                        requestSyntony.onNext(bindWechat);
                    }
                })
        );
    }

    /**
     * *获取APP任务
     * */
    public void getAppTaskListData(Context context,String appId,String userId,String taskType,final RequestSyntony<AppTaskList> requestSyntony){
        JSONObject requestData = new JSONObject();
        try {
            if(!TextUtils.isEmpty(SaveShare.getValue(context, "userId"))){
                requestData.put("userId", SaveShare.getValue(context, "userId"));
            }
            requestData.put("taskType", 0);
            requestData.put("appId",AppID);//"62B0E5C9-F486-48B3-8B57-11F00B676F3E"
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //测试
        mSubscriptions.add(LoginConnextor.getConnextor(context).getAppService(LoginApi.class,Url).AppTaskList(JSONObjectData(context,requestData.toString()))
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<AppTaskList>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(AppTaskList appTaskList) {
                        requestSyntony.onNext(appTaskList);
                    }
                })
        );
    }

    /**
     * *完成任务
     * */
    public void getFinishTaskData(Context context,String appId,String userId,String id,boolean IsDouble,final RequestSyntony<FinishTask> requestSyntony){
        JSONObject requestData = new JSONObject();
        try {
            if(!TextUtils.isEmpty(SaveShare.getValue(context, "userId"))){
                requestData.put("userId", SaveShare.getValue(context, "userId"));
            }
            requestData.put("MultitaskingID", id);
            requestData.put("IsDouble", IsDouble);
            requestData.put("appId",AppID);//"62B0E5C9-F486-48B3-8B57-11F00B676F3E"
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSubscriptions.add(LoginConnextor.getConnextor(context).getAppService(LoginApi.class,Url).FinishTask(JSONObjectData(context,requestData.toString()))
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<FinishTask>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(FinishTask finishTask) {
                        requestSyntony.onNext(finishTask);
                    }
                })
        );
    }

    /**
     * *获取APP邀请奖励列表
     * */
    public void getAppInviteListData(Context context,String appId,final RequestSyntony<AppInviteList> requestSyntony){
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("appId", appId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSubscriptions.add(LoginConnextor.getConnextor(context).getAppService(LoginApi.class,Url).AppInviteList(JSONObjectData(context,requestData.toString()))
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<AppInviteList>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(AppInviteList appInviteList) {
                        requestSyntony.onNext(appInviteList);
                    }
                })
        );
    }


    /**
     * *获取APP签到列表
     * */
    public void getAppSignInListData(Context context,String appId,String userId,final RequestSyntony<AppSignInList> requestSyntony){
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("appId", AppID);
            if(!TextUtils.isEmpty(SaveShare.getValue(context, "userId"))){
                requestData.put("userId", SaveShare.getValue(context, "userId"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSubscriptions.add(LoginConnextor.getConnextor(context).getAppService(LoginApi.class,Url).AppSignInList(JSONObjectData(context,requestData.toString()))
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<AppSignInList>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(AppSignInList appSignInList) {
                        requestSyntony.onNext(appSignInList);
                    }
                })
        );
    }

    /**
     * *获取APP活跃值列表
     * */
    public void getAllActiveRewardsListData(Context context,final RequestSyntony<ActiveValue> requestSyntony){
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("appId", AppID);
            if(!TextUtils.isEmpty(SaveShare.getValue(context, "userId"))){
                requestData.put("userId", SaveShare.getValue(context, "userId"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSubscriptions.add(LoginConnextor.getConnextor(context).getAppService(LoginApi.class,Url).AllActiveRewardsList(JSONObjectData(context,requestData.toString()))
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<ActiveValue>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(ActiveValue activeValue) {
                        requestSyntony.onNext(activeValue);
                    }
                })
        );
    }

    /**
     * *APP活跃值完成
     * */
    public void getCompleteActiveRewardsData(Context context, final String id, final RequestSyntony<CompleteActive> requestSyntony){
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("appId", AppID);
            if(!TextUtils.isEmpty(SaveShare.getValue(context, "userId"))){
                requestData.put("userId", SaveShare.getValue(context, "userId"));
            }
            requestData.put("ActiveRewardsID", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSubscriptions.add(LoginConnextor.getConnextor(context).getAppService(LoginApi.class,Url).CompleteActiveRewards(JSONObjectData(context,requestData.toString()))
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<CompleteActive>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(CompleteActive completeActive) {
                        requestSyntony.onNext(completeActive);
                    }
                })
        );
    }

    /**
     * *签到
     * */
    public void getSignInData(Context context,String appId,int Multiple,String userId,String SignId,final RequestSyntony<SignIn> requestSyntony){
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("appId", AppID);
            if(!TextUtils.isEmpty(SaveShare.getValue(context, "userId"))){
                requestData.put("userId", SaveShare.getValue(context, "userId"));
            }
            requestData.put("signAtureID", SignId);
            requestData.put("Multiple", Multiple);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSubscriptions.add(LoginConnextor.getConnextor(context).getAppService(LoginApi.class,Url).SignIn(JSONObjectData(context,requestData.toString()))
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<SignIn>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(SignIn signIn) {
                        requestSyntony.onNext(signIn);
                    }
                })
        );
    }

    /**
     * *邀请用户
     * */
    public void getInvitedUsersData(Context context,String invitationId,String appId,String userId,String mobile,final RequestSyntony<InvitedUsers> requestSyntony){
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("invitationId", invitationId);
            if(!TextUtils.isEmpty(SaveShare.getValue(context, "userId"))){
                requestData.put("userId", SaveShare.getValue(context, "userId"));
            }
            requestData.put("appId", appId);
            requestData.put("mobile", mobile);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSubscriptions.add(LoginConnextor.getConnextor(context).getAppService(LoginApi.class,Url).InvitedUsers(JSONObjectData(context,requestData.toString()))
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<InvitedUsers>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(InvitedUsers invitedUsers) {
                        requestSyntony.onNext(invitedUsers);
                    }
                })
        );
    }

//    /**
//     * *用户退出注销 更改用户token 和最后一次登录时间
//     * */
//    public void getBindWechatData(Context context,final RequestSyntony<Exit> requestSyntony){
//        mSubscriptions.add(LoginConnextor.getConnextor(context).getAppService(LoginApi.class,"http://api.xytq.qukanzixun.com/").Cancel()
//                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
//                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
//                .subscribe(new Observer<Exit>() {
//                    @Override
//                    public void onCompleted() {
//                        requestSyntony.onCompleted();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        requestSyntony.onError(e);
//                    }
//
//                    @Override
//                    public void onNext(Exit exit) {
//                        requestSyntony.onNext(exit);
//                    }
//                })
//        );
//    }
    /**
     * 封装的请求加密数据
     * */
     private RequestBody JSONObjectData(Context context,String requestData){
         RequestBody requestBody = null;
         try {
              SaveShare.saveValue(context, "utoken",AESUtils.getInstance().encrypt2(System.currentTimeMillis()+""));
             JSONObject request = new JSONObject();
             try {
                 request.put("requestModel",  AESUtils.getInstance().encrypt2(requestData.toString()));
             } catch (JSONException e) {
                 e.printStackTrace();
             }
              requestBody = RequestBody.create(MediaType.parse("application/json"),request.toString());
             return requestBody;
         } catch (Exception e) {
             e.printStackTrace();
         }
         return requestBody;
     }
    /**
     * 获取用户信息
     *
     */
    public void getUserInfoData(Context context,String appid,String uesrid,final RequestSyntony<UserInfo> requestSyntony){
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("appId", AppID);
            if(!TextUtils.isEmpty(SaveShare.getValue(context, "userId"))){
                requestData.put("userId", SaveShare.getValue(context, "userId"));
            }
         } catch (JSONException e) {
            e.printStackTrace();
        }
        mSubscriptions.add(LoginConnextor.getConnextor(context).getAppService(LoginApi.class,Url).UserInfo(JSONObjectData(context,requestData.toString()))
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<UserInfo>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(UserInfo userInfo) {
                        requestSyntony.onNext(userInfo);
                    }
                })
        );
    }

    /**
     * 获取用户活跃记录
     *
     */
    public void getUserActiveInfoData(Context context,String appid,String uesrid,final RequestSyntony<UserActiveInfo> requestSyntony){
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("appId", AppID);
            if(!TextUtils.isEmpty(SaveShare.getValue(context, "userId"))){
                requestData.put("userId", SaveShare.getValue(context, "userId"));
            }
//            requestData.put("multitaskingID", multitaskingID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSubscriptions.add(LoginConnextor.getConnextor(context).getAppService(LoginApi.class,Url).UserActiveInfo(JSONObjectData(context,requestData.toString()))
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<UserActiveInfo>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(UserActiveInfo userInfo) {
                        requestSyntony.onNext(userInfo);
                    }
                })
        );
    }

    /**
     * 提现
     *
     */
    public void getWithdrawDepositData(Context context,String desc,int gold,final RequestSyntony<WithdrawDeposit> requestSyntony){
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("appId", AppID);
            if(!TextUtils.isEmpty(SaveShare.getValue(context, "userId"))){
                requestData.put("userId", SaveShare.getValue(context, "userId"));
            }
            requestData.put("gold", gold);
            requestData.put("WxNumber", desc);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSubscriptions.add(LoginConnextor.getConnextor(context).getAppService(LoginApi.class,Url).WithdrawDeposit(JSONObjectData(context,requestData.toString()))
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<WithdrawDeposit>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(WithdrawDeposit withdrawDeposit) {
                        requestSyntony.onNext(withdrawDeposit);
                    }
                })
        );
    }

    /**
     * 获取用户提现记录
     *
     */
    public void getWithdrawalRecordData(Context context,final RequestSyntony<WithdrawalRecord> requestSyntony){
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("appId", AppID);
            if(!TextUtils.isEmpty(SaveShare.getValue(context, "userId"))){
                requestData.put("userId", SaveShare.getValue(context, "userId"));
            }
//            requestData.put("multitaskingID", multitaskingID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSubscriptions.add(LoginConnextor.getConnextor(context).getAppService(LoginApi.class,Url).WithdrawalRecord(JSONObjectData(context,requestData.toString()))
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<WithdrawalRecord>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(WithdrawalRecord withdrawalRecord) {
                        requestSyntony.onNext(withdrawalRecord);
                    }
                })
        );
    }

    /**
     * 获取50条提现记录，用作轮播
     *
     */
    public void getWithdrawalRecordCarouselData(Context context,final RequestSyntony<WithdrawalRecord> requestSyntony){
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("appId", AppID);
            if(!TextUtils.isEmpty(SaveShare.getValue(context, "userId"))){
                requestData.put("userId", SaveShare.getValue(context, "userId"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSubscriptions.add(LoginConnextor.getConnextor(context).getAppService(LoginApi.class,Url).WithdrawalRecordCarousel(JSONObjectData(context,requestData.toString()))
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<WithdrawalRecord>() {
                    @Override
                    public void onCompleted() {
                        requestSyntony.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSyntony.onError(e);
                    }

                    @Override
                    public void onNext(WithdrawalRecord withdrawalRecord) {
                        requestSyntony.onNext(withdrawalRecord);
                    }
                })
        );
    }
}
