package com.zt.rainbowweather.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chenguang.weather.R;
import com.zt.rainbowweather.api.TakePhotoPopWinListener;
import com.zt.rainbowweather.presenter.ChineseCalendar;

import java.util.Calendar;

import cn.carbs.android.indicatorview.library.IndicatorView;


/**
 * zw
 * 自定义PopupWindow
 */
public class TakePhotoPopWin extends PopupWindow  implements View.OnClickListener, IndicatorView.OnIndicatorChangedListener{

    private Context mContext;

    private View view;
    private IndicatorView mIndicatorView;
    private GregorianLunarCalendarView mGLCView;
    private TextView tvDate;
    private TextView cancel,confirm;
    private int Index;
    private TakePhotoPopWinListener takePhotoPopWinListener;

    private void InitView(){
        mGLCView = view.findViewById(R.id.calendar_view);
        mIndicatorView = view.findViewById(R.id.indicator_view);
        cancel = view.findViewById(R.id.cancel);
        confirm = view.findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);
        mIndicatorView.setOnIndicatorChangedListener(this);
        tvDate = view.findViewById(R.id.button_get_data);
        mGLCView.init();
        GregorianLunarCalendarView.CalendarData calendarData = mGLCView.getCalendarData();
        Calendar calendar = calendarData.getCalendar();

        tvDate.setText( calendar.get(Calendar.YEAR) + "年"
                + (calendar.get(Calendar.MONTH) + 1) + "月"
                + calendar.get(Calendar.DAY_OF_MONTH)+ "日");

        mGLCView.setOnDateChangedListener(new GregorianLunarCalendarView.OnDateChangedListener(){
            @SuppressLint("WrongConstant")
            @Override
            public void onDateChanged(GregorianLunarCalendarView.CalendarData calendarData) {
                Calendar calendar = calendarData.getCalendar();
                String showToast =calendar.get(ChineseCalendar.CHINESE_YEAR) + "年"
                        + (calendar.get(ChineseCalendar.CHINESE_MONTH)) + "月"
                        + calendar.get(ChineseCalendar.CHINESE_DATE);
                if (Index == 0) {
                    showToast =calendar.get(ChineseCalendar.CHINESE_YEAR) + "年"
                            + (calendar.get(ChineseCalendar.CHINESE_MONTH)) + "月"
                            + calendar.get(ChineseCalendar.CHINESE_DATE);
                } else if (Index == 1) {
                    showToast = calendar.get(Calendar.YEAR) + "年"
                            + (calendar.get(Calendar.MONTH) + 1) + "月"
                            + calendar.get(Calendar.DAY_OF_MONTH)+ "日";
                }
                tvDate.setText(showToast);
            }
        });
    }


    @SuppressLint("SetTextI18n")
    public TakePhotoPopWin(final Context mContext,final TakePhotoPopWinListener takePhotoPopWinListener) {
        this.mContext = mContext;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.date_pop, null);
        this.takePhotoPopWinListener = takePhotoPopWinListener;
        InitView();
        // 设置外部可点击
        this.setOutsideTouchable(true);

        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.take_photo_anim);

    }
    private void toGregorianMode() {
        mGLCView.toGregorianMode();
    }

    private void toLunarMode() {
        mGLCView.toLunarMode();
    }

    @Override
    public void onClick(View view) {
     switch (view.getId()){
         case R.id.cancel:
             dismiss();
             break;
         case R.id.confirm:
             GregorianLunarCalendarView.CalendarData calendarData = mGLCView.getCalendarData();
             Calendar calendar = calendarData.getCalendar();
            String showToast = calendar.get(Calendar.YEAR) + "年"
                     + (calendar.get(Calendar.MONTH) + 1) + "月"
                     + calendar.get(Calendar.DAY_OF_MONTH)+ "日";
             takePhotoPopWinListener.onClick(showToast);
             dismiss();
             break;
     }
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onIndicatorChanged(int oldSelectedIndex, int newSelectedIndex) {
        Log.e("TAG", "onIndicatorChanged: "+newSelectedIndex );
        if (newSelectedIndex == 0) {
            Index = 0;
            toGregorianMode();
            GregorianLunarCalendarView.CalendarData calendarData = mGLCView.getCalendarData();
            Calendar calendar = calendarData.getCalendar();
            tvDate.setText(calendar.get(ChineseCalendar.CHINESE_YEAR) + "年"
                    + (calendar.get(ChineseCalendar.CHINESE_MONTH)) + "月"
                    + calendar.get(ChineseCalendar.CHINESE_DATE));

        } else if (newSelectedIndex == 1) {
            Index = 1;
            toLunarMode();
            GregorianLunarCalendarView.CalendarData calendarData = mGLCView.getCalendarData();
            Calendar calendar = calendarData.getCalendar();
            tvDate.setText( calendar.get(Calendar.YEAR) + "年"
                    + (calendar.get(Calendar.MONTH) + 1) + "月"
                    + calendar.get(Calendar.DAY_OF_MONTH)+ "日");
        }
    }
}