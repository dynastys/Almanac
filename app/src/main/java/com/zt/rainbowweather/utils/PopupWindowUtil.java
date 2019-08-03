package com.zt.rainbowweather.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.widget.PopupWindowCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.zt.weather.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class PopupWindowUtil {

    private static PopupWindowUtil popupWindowUtil;

    public static PopupWindowUtil getPopupWindowUtil() {
        if (popupWindowUtil == null) {
            synchronized (PopupWindowUtil.class) {
                if (popupWindowUtil == null) {
                    popupWindowUtil = new PopupWindowUtil();
                }
            }
        }
        return popupWindowUtil;
    }

    public interface PopupWindowPort {
        void setData(View view);
    }

    public PopupWindow setPopupWindowUtil(Activity context, int layout,boolean ISwind, RelativeLayout relativeLayout, PopupWindowPort popupWindowPort) {
        try {
            //获取LayoutInflater实例
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            //这里的main布局是在inflate中加入的哦，以前都是直接this.setContentView()的吧？呵呵
            //该方法返回的是一个View的对象，是布局中的根
            View view = inflater.inflate(layout, null, false);
            //下面我们要考虑了，我怎样将我的layout加入到PopupWindow中呢？？？很简单
            PopupWindow menuWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT); //后两个参数是width和height
            //menuWindow.showAsDropDown(layout); //设置弹出效果
            //menuWindow.showAsDropDown(null, 0, layout.getHeight());
            //设置如下四条信息，当点击其他区域使其隐藏，要在show之前配置
            menuWindow.setFocusable(true);
            menuWindow.setOutsideTouchable(true);
            menuWindow.update();
            menuWindow.setBackgroundDrawable(new BitmapDrawable());
            //根据指定View定位
//            int offsetX = Math.abs(menuWindow.getContentView().getMeasuredWidth()-relativeLayout.getWidth()) / 2;
//            int offsetY = -(relativeLayout.getHeight()- menuWindow.getContentView().getMeasuredWidth());
//            PopupWindowCompat.showAsDropDown(menuWindow, relativeLayout, offsetX, offsetY, Gravity.END);
//            PopupWindowCompat.showAsDropDown(menuWindow, relativeLayout, 0, 0, Gravity.RIGHT);
            int[] position = new int[2];
            relativeLayout.getLocationOnScreen(position);
            menuWindow.showAtLocation(relativeLayout, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, -position[1]); //设置layout在PopupWindow中显示的位置
            if(ISwind){
                popupWindowPort.setData(view);
            }
            return menuWindow;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
