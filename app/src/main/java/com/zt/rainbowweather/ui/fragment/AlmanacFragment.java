package com.zt.rainbowweather.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chenguang.weather.R;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.umeng.analytics.MobclickAgent;
import com.xy.xylibrary.base.BaseFragment;
import com.xy.xylibrary.utils.Utils;
import com.zt.rainbowweather.api.TakePhotoPopWinListener;
import com.zt.rainbowweather.utils.ShowPopUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author zw
 * @time 2019-3-8
 * 黄历
 */
public class AlmanacFragment extends BaseFragment implements CalendarView.OnCalendarSelectListener, CalendarView.OnYearChangeListener, View.OnClickListener{
    @BindView(R.id.the_left)
    ImageView theLeft;
    @BindView(R.id.data)
    TextView data;
    @BindView(R.id.the_right)
    ImageView theRight;
    @BindView(R.id.tv_month_day)
    TextView mTextMonthDay;
    @BindView(R.id.tv_year)
    TextView mTextYear;
    @BindView(R.id.tv_lunar)
    TextView mTextLunar;
    @BindView(R.id.tv_current_day)
    TextView mTextCurrentDay;
    @BindView(R.id.fl_current)
    FrameLayout flCurrent;
    @BindView(R.id.rl_tool)
    RelativeLayout rlTool;
    @BindView(R.id.calendarView)
    CalendarView mCalendarView;
    @BindView(R.id.calendarLayout)
    CalendarLayout mCalendarLayout;
    @BindView(R.id.calendar_lin)
    LinearLayout calendarLin;
    @BindView(R.id.actionBarSize)
    TextView actionBarSize;
    Unbinder unbinder;

    private int mYear;
    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_almanac;
    }

    @Override
    protected void initData(View view) {
        ViewGroup.LayoutParams layoutParams = actionBarSize.getLayoutParams();
        layoutParams.height = Utils.getStatusBarHeight(getActivity());
        actionBarSize.setLayoutParams(layoutParams);
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnYearChangeListener(this);
        data.setText(String.valueOf(mCalendarView.getCurYear())+"年"+ mCalendarView.getCurMonth() + "月");
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
        List<Calendar> schemes = new ArrayList<>();
        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();

    }

    @Override
    protected void initListener() {
        mTextMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCalendarLayout.isExpand()) {
                    mCalendarView.showYearSelectLayout(mYear);
                    return;
                }
                mCalendarView.showYearSelectLayout(mYear);
                mTextLunar.setVisibility(View.GONE);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mYear));
            }
        });
        flCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();
            }
        });
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopUtils.getInstance().showPopFormBottom(getActivity(), calendarLin, new TakePhotoPopWinListener() {
                    @Override
                    public void onClick(String date) {
                        data.setText(date.split("月")[0]+"月");
                    }
                });
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        if(mTextLunar != null){
            mTextLunar.setVisibility(View.VISIBLE);
            mTextYear.setVisibility(View.VISIBLE);
            mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
            mTextYear.setText(String.valueOf(calendar.getYear()));
            mTextLunar.setText(calendar.getLunar());
            mYear = calendar.getYear();
        }
    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("AlmanacFragment"); //统计页面("MainScreen"为页面名称，可自定义)
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("AlmanacFragment");
    }

}
