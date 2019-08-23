package com.zt.rainbowweather.presenter;

import android.app.Activity;
import android.text.TextUtils;

import com.xy.xylibrary.utils.DownLoadUtils;
import com.xy.xylibrary.utils.SaveShare;
import com.xy.xylibrary.utils.Utils;
import com.zt.rainbowweather.api.RequestSyntony;
import com.zt.rainbowweather.entity.news.LatestVersion;
import com.zt.rainbowweather.presenter.request.NewsRequest;
import com.zt.rainbowweather.ui.service.DownLoadService;
import com.zt.rainbowweather.utils.ToastUtils;
import com.zt.rainbowweather.utils.UpdateDialog;
import com.zt.rainbowweather.utils.utils;


public class UpdatePort implements RequestSyntony<LatestVersion> {

    private static UpdatePort updatePort;
    private Activity mContext;
    public static UpdatePort getUpdatePort() {
        if(updatePort == null){
            synchronized (UpdatePort.class){
                if (updatePort == null){
                    updatePort = new UpdatePort();
                }
            }
        }
        return updatePort;
    }

    //更新
    public void UpdateDialog(final Activity mContext) {
        this.mContext = mContext;
        NewsRequest.getNewsRequest().getUpdateData(mContext, UpdatePort.this);

    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(LatestVersion latestVersion) {
        try {
            if(latestVersion.getDate().getVersion_code() > Utils.getVersionCode(mContext)){
                SaveShare.saveValue(mContext,"version_code", ""+latestVersion.getDate().getVersion_code());
                final UpdateDialog confirmDialog = new UpdateDialog(mContext, "有新版本啦！(V"+ latestVersion.getDate().getVersion_name()+")", "立即更新","以后再说", latestVersion.getDate().getRelease_notes());
                confirmDialog.show();
                confirmDialog.setClicklistener(new UpdateDialog.ClickListenerInterface() {
                    @Override
                    public void doConfirm() {
                        DownLoadUtils downLoadUtils = new DownLoadUtils(mContext);
                        confirmDialog.dismiss();
                        ToastUtils.showLong("更新中。。。");
                        String file = SaveShare.getValue(mContext, "file");
                        if (TextUtils.isEmpty(file)) {
                            utils.Download(mContext,   latestVersion.getDate().getSource_file_url());
                         } else {
                            if ("com.zt.weather".equals(downLoadUtils.getDownLoadPackageName(mContext, file))) {
                                if (downLoadUtils.getVersion(mContext).equals(downLoadUtils.getVersionName(mContext, "com.zt.weather"))) {
    //                                downLoadUtils.checkIsAndroidO(mContext,file);
                                    utils.doApk(mContext,file);
                                } else {
                                    utils.Download(mContext,latestVersion.getDate().getSource_file_url());
                                }
                            } else {
                                utils.Download(mContext,latestVersion.getDate().getSource_file_url());
                            }
                        }
                    }

                    @Override
                    public void doCancel() {
                        confirmDialog.dismiss();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
