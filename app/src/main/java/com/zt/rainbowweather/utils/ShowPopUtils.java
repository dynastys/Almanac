package com.zt.rainbowweather.utils;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.zt.rainbowweather.api.TakePhotoPopWinListener;
import com.zt.rainbowweather.view.TakePhotoPopWin;


public class ShowPopUtils {
    private static ShowPopUtils showPopUtils;

    public static ShowPopUtils getInstance() {
        if(showPopUtils == null){
            synchronized (ShowPopUtils.class){
                if (showPopUtils == null){
                    showPopUtils = new ShowPopUtils();
                }
            }
        }
        return showPopUtils;
    }
    public void showPopFormBottom(final Activity context, LinearLayout mainView,TakePhotoPopWinListener takePhotoPopWinListener) {
        TakePhotoPopWin takePhotoPopWin = new TakePhotoPopWin(context,takePhotoPopWinListener);
//        设置Popupwindow显示位置（从底部弹出）
        takePhotoPopWin.showAtLocation(mainView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        final WindowManager.LayoutParams[] params = {context.getWindow().getAttributes()};
//        //当弹出Popupwindow时，背景变半透明
//        params[0].alpha = 0.7f;
//        context.getWindow().setAttributes(params[0]);
        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        takePhotoPopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

                params[0] = context.getWindow().getAttributes();
                params[0].alpha = 1f;
                context.getWindow().setAttributes(params[0]);
            }
        });

//        takePhotoPopWin.lis
    }
}
