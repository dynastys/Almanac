package com.xy.xylibrary.ui.activity.login;

import com.xy.xylibrary.signin.AppInviteList;
import com.xy.xylibrary.signin.AppSignInList;
import com.xy.xylibrary.signin.AppTaskList;
import com.xy.xylibrary.signin.FinishTask;
import com.xy.xylibrary.signin.InvitedUsers;
import com.xy.xylibrary.signin.SignIn;
import com.xy.xylibrary.ui.activity.task.WithdrawDeposit;
import com.xy.xylibrary.ui.activity.task.WithdrawalRecord;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

public interface LoginApi {
    /**
     * 获取用户信息 访问时带请求头Toke访问验证身份
     */
    @GET("v1/users")
    Observable<WeChat> UsersMessage();

    /**
     * 验证码
     */
    @POST("api/v1/GetVCode")
    Observable<RegisteJson> QueryRegisteList(@Body RequestBody body);

    /**
     * 手机登录
     * */
    @POST("api/v1/VcodeLogin")
    Observable<Phone> QueryEntryList(@Body RequestBody body);
//    @GET("api/v1/VcodeLogin")
//    Observable<Phone> QueryEntryList(@Query("mobile") String mobile,@Query("vCode") String vCode,@Query("appId") String appId);

    /**
     * 绑定手机号
     */
    @POST("api/v1/BindMobile")
    Observable<Phone> BindingPhone(@Body RequestBody body);

    /**
     * 微信登录
     */
    @POST("api/v1/GetWxID")
    Observable<WeChatLogin> WeChatLogin(@Body RequestBody body);


    /**
     * 注册用户绑定微信号
     */
    @POST("api/v1/BindWX")
    Observable<BindWechat> BindWechat(@Body RequestBody body);


    /**
     * 用户退出注销 更改用户token 和最后一次登录时间
     */
    @GET("v1/user/exit")
    Observable<Exit> Cancel();

    /**
     * 获取APP邀请奖励列表
     * */
    @POST("api/v1/GetAllInvitationByAppID")
    Observable<AppInviteList> AppInviteList(@Body RequestBody body);

    /**
     * 获取APP任务
     */
    @POST("api/v1/GetAllMultitaskingByAppID")
    Observable<AppTaskList> AppTaskList(@Body RequestBody body);

    /**
     * 完成任务
     */
    @POST("api/v1/CompleteTasks")
    Observable<FinishTask> FinishTask(@Body RequestBody body);

    /**
     * 获取APP签到列表
     */
    @POST("api/v1/GetAllSignAtureByAppID")
    Observable<AppSignInList> AppSignInList(@Body RequestBody body);

    /**
     * 签到
     */
    @POST("api/v1/CompleteSignAture")
    Observable<SignIn> SignIn(@Body RequestBody body);

    /**
     * 邀请用户
     */
    @POST("api/v1/Invitation")
    Observable<InvitedUsers> InvitedUsers(@Body RequestBody body);

    /**
     * 获取用户信息
     */
    @POST("api/v1/GetUser")
    Observable<UserInfo> UserInfo(@Body RequestBody body);

    /**
     * 获取用户活跃记录
     */
    @POST("api/v1/GetUserActiveInfo")
    Observable<UserActiveInfo> UserActiveInfo(@Body RequestBody body);

    /**
     * 提现
     */
    @POST("api/v1/Withdrawals")
    Observable<WithdrawDeposit> WithdrawDeposit(@Body RequestBody body);

    /**
     * 获取用户提现记录
     */
    @POST("api/v1/GetWithdrawalsByUserID")
    Observable<WithdrawalRecord> WithdrawalRecord(@Body RequestBody body);

    /**
     * 获取50条提现记录，用作轮播
     */
    @POST("api/v1/GetAllWithdrawals")
    Observable<WithdrawalRecord> WithdrawalRecordCarousel(@Body RequestBody body);
}
