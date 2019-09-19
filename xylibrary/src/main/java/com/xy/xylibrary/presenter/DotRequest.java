package com.xy.xylibrary.presenter;

import android.content.Context;
import android.util.Log;

import com.xy.xylibrary.api.DotService;
import com.xy.xylibrary.ui.activity.login.LoginRequest;
import com.xy.xylibrary.utils.RomUtils;
import com.xy.xylibrary.utils.SaveShare;
import com.xy.xylibrary.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class DotRequest{

    private CompositeSubscription mSubscriptions = new CompositeSubscription();
    private static DotRequest dotRequest;
    private String url = "http://121.199.63.210:8001/";
    public static DotRequest getDotRequest(){
        if (dotRequest == null){
            synchronized (DotRequest.class){
                if (dotRequest == null){
                    dotRequest = new DotRequest();
                }
            }
        }
        return dotRequest;
    }

    /**
     * activity打点
     * @param context
     * @param page_name
     */
    public void getActivity(Context context,String page_name){
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("page_action", 1);
            requestData.put("page_name", page_name);
            requestData.put("date_tiem", System.currentTimeMillis());
            requestData.put("app_id", LoginRequest.AppID);
            requestData.put("channel_code", RomUtils.app_youm_code);
            requestData.put("user_id", SaveShare.getValue(context, "userId"));
            requestData.put("imei", Utils.getIMEI(context));
            requestData.put("app_ver", Utils.getVersionString());
            requestData.put("brand", Utils.getDeviceBrand());
            requestData.put("model", Utils.getDeviceModel());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),requestData.toString());
        mSubscriptions.add(DotConnextor.getConnextor(context).getAppService(DotService.class,url).AppDotPage(requestBody)
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object object) {

                    }
                })
        );
    }

    /**
     * icon打点
     * @param context
     * @param icon_id
     */
    public void getIcon(Context context,int icon_id){
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("icon_action", 1);
            requestData.put("icon_id", icon_id);
            requestData.put("date_tiem", System.currentTimeMillis());
            requestData.put("app_id", LoginRequest.AppID);
            requestData.put("channel_code", RomUtils.app_youm_code);
            requestData.put("user_id", SaveShare.getValue(context, "userId"));
            requestData.put("imei", Utils.getIMEI(context));
            requestData.put("app_ver", Utils.getVersionString());
            requestData.put("brand", Utils.getDeviceBrand());
            requestData.put("model", Utils.getDeviceModel());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),requestData.toString());
        mSubscriptions.add(DotConnextor.getConnextor(context).getAppService(DotService.class,url).AppDotIcon(requestBody)
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object object) {

                    }
                })
        );
    }

    /**
     * 首页设备激活打点
     * @param context
     */
    public void getDevice(Context context){
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("date_tiem", System.currentTimeMillis());
            requestData.put("app_id", LoginRequest.AppID);
            requestData.put("channel_code", RomUtils.app_youm_code);
            requestData.put("user_id", SaveShare.getValue(context, "userId"));
            requestData.put("imei", Utils.getIMEI(context));
            requestData.put("app_ver", Utils.getVersionString());
            requestData.put("brand", Utils.getDeviceBrand());
            requestData.put("model", Utils.getDeviceModel());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),requestData.toString());
        mSubscriptions.add(DotConnextor.getConnextor(context).getAppService(DotService.class,"http://121.199.63.210:8002/").AppActivate(requestBody)
                .subscribeOn(Schedulers.io())//判断是哪一个线程执行
                .observeOn(AndroidSchedulers.mainThread())//在主线程中输出
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object object) {

                    }
                })
        );
    }

}
