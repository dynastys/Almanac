package com.zt.rainbowweather.ui.fragment;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.umeng.analytics.MobclickAgent;
import com.xy.xylibrary.Interface.MyEdit;
import com.xy.xylibrary.base.BaseFragment;
import com.xy.xylibrary.utils.GlideUtil;
import com.xy.xylibrary.utils.Utils;
import com.xy.xylibrary.view.MyEditText;
import com.zt.rainbowweather.entity.background.IsUserLight;
import com.zt.rainbowweather.entity.calendar.DanXiangLi;
import com.zt.rainbowweather.entity.calendar.HuangLi;
import com.zt.rainbowweather.presenter.almanac.AlmanacLogic;
import com.zt.rainbowweather.ui.activity.AdviseMoreDetailActivity;
import com.zt.rainbowweather.ui.activity.DXiangLiActivity;
import com.zt.rainbowweather.ui.activity.YiJiActivity;
import com.zt.rainbowweather.utils.ConstUtils;
import com.zt.rainbowweather.utils.ShowPopUtils;
import com.zt.rainbowweather.utils.Util;
import com.zt.rainbowweather.view.ChangeTextViewSpace;
import com.zt.rainbowweather.view.FlowLayout;
import com.zt.rainbowweather.view.InfiniteViewPager;
import com.zt.weather.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author zw
 * @time 2019-3-8
 * 黄历
 */
public class AlmanacFragment extends BaseFragment implements MyEdit, CalendarView.OnCalendarSelectListener, CalendarView.OnYearChangeListener, View.OnClickListener, AlmanacLogic.RequestBack, NestedScrollView.OnScrollChangeListener, View.OnTouchListener {

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
    @BindView(R.id.suitable)
    ImageView suitable;
    @BindView(R.id.suitable_content)
    RecyclerView suitableContent;
    @BindView(R.id.avoid)
    ImageView avoid;
    @BindView(R.id.avoid_content)
    RecyclerView avoidContent;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.almanac_list_recycler)
    RecyclerView almanacListRecycler;
    @BindView(R.id.almanac_more)
    LinearLayout almanacMore;
    @BindView(R.id.year_lin)
    LinearLayout yearLin;
    @BindView(R.id.dynamic_state)
    ImageView dynamicState;
    @BindView(R.id.linearView)
    LinearLayout linearView;
    @BindView(R.id.yi_ji_lin)
    LinearLayout yiJiLin;
    @BindView(R.id.chanxiangli_tv)
    ChangeTextViewSpace chanxiangliTv;
    @BindView(R.id.sui_id)
    TextView suiId;
    @BindView(R.id.NongLiMonthDay)
    TextView NongLiMonthDay;
    @BindView(R.id.shengxiao)
    TextView shengxiao;
    @BindView(R.id.danxiangli_tv)
    TextView danxiangliTv;
    @BindView(R.id.novel_input_box)
    MyEditText novelInputBox;
    @BindView(R.id.used_search_rec)
    FlowLayout usedSearchRec;
    @BindView(R.id.almanac_information_rec)
    RecyclerView almanacInformationRec;
    @BindView(R.id.replace_lin)
    LinearLayout replaceLin;
    @BindView(R.id.almanac_news_lin)
    LinearLayout almanacNewsLin;
    @BindView(R.id.ad_image_banner)
    ImageView adImageBanner;
    @BindView(R.id.ad_image_banner_clear)
    ImageView adImageBannerClear;
    @BindView(R.id.tv_ad_flag)
    TextView tvAdFlag;
    @BindView(R.id.ad_lin)
    LinearLayout adLin;
    @BindView(R.id.dynamic_state_rel)
    LinearLayout dynamicStateRel;
    @BindView(R.id.suitable_lin)
    LinearLayout suitableLin;
    @BindView(R.id.avoid_lin)
    LinearLayout avoidLin;
    @BindView(R.id.almanac_GDT_ad)
    RelativeLayout almanacGDTAd;
    @BindView(R.id.banner_container)
    FrameLayout bannerContainer;
    @BindView(R.id.even_more)
    TextView evenMore;
    @BindView(R.id.calendarView1)
    CalendarView calendarView1;
    @BindView(R.id.nestedScrollView1)
    NestedScrollView nestedScrollView1;
    @BindView(R.id.myViewPager)
    InfiniteViewPager mMyPager;
    @BindView(R.id.danxiangli_tv_details)
    TextView danxiangliTvDetails;


    private int mYear;
    private AlmanacLogic almanacLogic;
    private DanXiangLi danXiangLis;
    private DanXiangLi.DataBean bean;
    private ArrayList<View> test = new ArrayList<>();
    private ListView listView;
    private int scrollY;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_almanac;
    }

    @Override
    protected void initData(View view) {
        try {
            if (!ConstUtils.almanac_icon) {
                almanacMore.setVisibility(View.GONE);
            }
            if (!ConstUtils.almanac_news) {
                almanacNewsLin.setVisibility(View.GONE);
            }
            dynamicStateRel.setOnClickListener(v -> {
                danXiangLis.setData(bean);
                MobclickAgent.onEvent(getActivity(), "yun_shi_danxiangli", "yun_shi_danxiangli");
                DXiangLiActivity.startActivity(getActivity(), danXiangLis);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View view0;
    private boolean ISClick;
    private String str;

    private void setViewpagerData(String s, boolean isClick) {//calendar.getYear() + "-" + calendar.getMonth() + "-" + calendar.getDay()
        str = s;
//        mMyPager.setCurrentItemOfData(s, false);
        if (view0 != null) {
            if (isClick) {
                ISClick = isClick;
                mMyPager.setNewData(s);
            } else {
                mMyPager.setCurrentItemOfData(s, false);
            }
        } else {
            List<String> dataList = new ArrayList<>();
            dataList.add(s);
            mMyPager.setData(dataList, new InfiniteViewPager.ViewHolderCreator() {
                @Override
                public InfiniteViewPager.ViewHolder create() {
                    view0 = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_fortune_ji_yi, null, false);
                    return new InfiniteViewHolder(view0);
                }
            });
            mMyPager.setOnCurrentPageChangeListener(onject -> {
                new Thread(() -> {
                    if(almanacLogic != null && !TextUtils.isEmpty(onject.toString())){
                        almanacLogic.getDanXiangLiData(getActivity(), onject.toString());
                    }
                }).start();
                if (!TextUtils.isEmpty(onject.toString())) {
                    calendarView1.scrollToCalendar(Integer.parseInt(onject.toString().split("-")[0]), Integer.parseInt(onject.toString().split("-")[1]), ((Integer.parseInt(onject.toString().split("-")[2])) <= 0 ? 30 : (Integer.parseInt(onject.toString().split("-")[2]))));
                    mCalendarView.scrollToCalendar(Integer.parseInt(onject.toString().split("-")[0]), Integer.parseInt(onject.toString().split("-")[1]), ((Integer.parseInt(onject.toString().split("-")[2])) <= 0 ? 30 : (Integer.parseInt(onject.toString().split("-")[2]))));
                }
            });
            mMyPager.setOnNeedAddDataCallback(new InfiniteViewPager.OnNeedAddDataCallback<String>() {
                @Override
                public String addFirst(int position, String s) {
                    return Util.AddDay(s, -1);
                }

                @Override
                public String addLast(int position, String s) {
                    return Util.AddDay(s, 1);
                }
            });
        }
    }


    class InfiniteViewHolder extends InfiniteViewPager.ViewHolder<View, String> {

        LinearLayout yearLin;
        TextView suiId;
        TextView NongLiMonthDay;
        TextView shengxiao;
        RecyclerView suitableContent;
        RecyclerView avoidContent;

        public InfiniteViewHolder(View view) {
            super(view);
            suiId = view.findViewById(R.id.sui_id);
            NongLiMonthDay = view.findViewById(R.id.NongLiMonthDay);
            shengxiao = view.findViewById(R.id.shengxiao);
            suitableContent = view.findViewById(R.id.suitable_content);
            avoidContent = view.findViewById(R.id.avoid_content);
        }

        @Override
        public void update(InfiniteViewPager.ViewHolder<View, String> holder, String s1) {
            try {
                String finalS = s1;
                almanacLogic.getHuangLiData(getActivity(), s1, huangLi -> {
                    Log.e("setViewpagerData", "s: " + s1);
                    almanacLogic.setSuitable(getActivity(), "宜", huangLi, suitableContent);
                    almanacLogic.setSuitable(getActivity(), "忌", huangLi, avoidContent);
                    suiId.setText(huangLi.getData().getSui_ci().get(1) + " 【" + huangLi.getData().getShengXiao() + "】 " + huangLi.getData().getSui_ci().get(2) + " " + huangLi.getData().getSui_ci().get(0));
                    NongLiMonthDay.setText(huangLi.getData().getNongLiMonth() + Util.LunarCalendarSize(huangLi.getData().getNongLiMonth()) + huangLi.getData().getNongLiDay());
                    shengxiao.setText(huangLi.getData().getStar());
                    bean.sui_ci_shengxiao = huangLi.getData().getSui_ci().get(1) + " 【" + huangLi.getData().getShengXiao() + "】 " + huangLi.getData().getSui_ci().get(2) + " " + huangLi.getData().getSui_ci().get(0);
                    bean.week = "周" + huangLi.getData().getWeek();
                    ISClick = false;
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void initListener() {
        try {
            mTextMonthDay.setOnClickListener(v -> {
                if (!mCalendarLayout.isExpand()) {
                    mCalendarView.showYearSelectLayout(mYear);
                    calendarView1.showYearSelectLayout(mYear);
                    return;
                }
                mCalendarView.showYearSelectLayout(mYear);
                calendarView1.showYearSelectLayout(mYear);
                mTextLunar.setVisibility(View.GONE);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mYear));
            });
            flCurrent.setOnClickListener(v -> mCalendarView.scrollToCurrent());
            data.setOnClickListener(v -> ShowPopUtils.getInstance().showPopFormBottom(getActivity(), calendarLin, date -> {
                data.setText(date.split("月")[0] + "月");
                calendarView1.scrollToCalendar(Integer.parseInt(date.split("年")[0]), Integer.parseInt(date.split("年")[1].split("月")[0]), Integer.parseInt(date.split("年")[1].split("月")[1].split("日")[0]));
                mCalendarView.scrollToCalendar(Integer.parseInt(date.split("年")[0]), Integer.parseInt(date.split("年")[1].split("月")[0]), Integer.parseInt(date.split("年")[1].split("月")[1].split("日")[0]));
            }));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        assert rootView != null;
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        if (mTextLunar != null) {
            try {
                mTextLunar.setVisibility(View.VISIBLE);
                mTextYear.setVisibility(View.VISIBLE);
                data.setText(String.valueOf(calendar.getYear()) + "年" + calendar.getMonth() + "月");
                mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
                mTextYear.setText(String.valueOf(calendar.getYear()));
                mTextLunar.setText(calendar.getLunar());
                bean.month = calendar.getMonth() + "月";
                bean.monthY = Util.MonthEnglish(calendar.getMonth());
                bean.NongLiDay = calendar.getDay() + "";
                mYear = calendar.getYear();
                if (view0 == null || isClick) {
//                    calendarView1.scrollToCalendar(calendar.getYear(),  calendar.getMonth(), calendar.getDay());
//                    mCalendarView.scrollToCalendar(calendar.getYear(),  calendar.getMonth(), calendar.getDay());
                    almanacLogic.BannerAd(getActivity(), adLin, almanacGDTAd, bannerContainer, adImageBanner);
                    setViewpagerData(calendar.getYear() + "-" + calendar.getMonth() + "-" + calendar.getDay(), isClick);
                    new Thread(() -> {
                        almanacLogic.getDanXiangLiData(getActivity(), calendar.getYear() + "-" + calendar.getMonth() + "-" + calendar.getDay());
                    }).start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            MobclickAgent.onEvent(getActivity(), "yun_shi");
        }
        if (isVisibleToUser && actionBarSize != null) {
            EventBus.getDefault().post(new IsUserLight(false));
            if (danXiangLis == null) {
//                ObjectAnimator.ofFloat(mCalendarLayout, "translationY", 0).start();
//                nestedScrollView.fling(mCalendarView.getHeight()- mCalendarLayout.getHeight());//添加上这句滑动才有效
//                nestedScrollView.scrollTo(0, mCalendarView.getHeight() - 200);
                nestedScrollView.setOnScrollChangeListener(AlmanacFragment.this);
                nestedScrollView1.setOnScrollChangeListener(AlmanacFragment.this);
                nestedScrollView.setOnTouchListener(AlmanacFragment.this);

                nestedScrollView.smoothScrollBy(0, mCalendarView.getHeight() - mCalendarLayout.getHeight() +30);
                danXiangLis = new DanXiangLi();
                bean = new DanXiangLi.DataBean();
                ViewGroup.LayoutParams layoutParams = actionBarSize.getLayoutParams();
                layoutParams.height = Utils.getStatusBarHeight(getActivity());
                actionBarSize.setLayoutParams(layoutParams);
                calendarView1.setOnCalendarSelectListener(this);
                calendarView1.setOnYearChangeListener(this);
                mCalendarView.setOnCalendarSelectListener(this);
                mCalendarView.setOnYearChangeListener(this);

                data.setText(String.valueOf(mCalendarView.getCurYear()) + "年" + mCalendarView.getCurMonth() + "月");
                mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
                mYear = mCalendarView.getCurYear();
                mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
                mTextLunar.setText("今日");
                mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
                almanacLogic = AlmanacLogic.getAlmanacLogic();
                almanacLogic.setRequestBack(AlmanacFragment.this);
                almanacLogic.setServiceData(almanacListRecycler);
                almanacLogic.setFestivalData(getActivity(), Utils.getDayOfWeekByDate(), mCalendarView, calendarView1);
                GlideUtil.getGlideUtil().setGifImages(getActivity(), R.mipmap.dynamic, dynamicState);
                chanxiangliTv.setSpacing(15);
                chanxiangliTv.setText("DANXIANGLI");//
                Show(novelInputBox);
                IMEClose(novelInputBox);
                novelInputBox.Clear(AlmanacFragment.this);
                almanacLogic.setNoveInputBox(getActivity(), novelInputBox, usedSearchRec, almanacInformationRec);
//                nestedScrollView.smoothScrollBy(0, mCalendarView.getHeight() - mCalendarLayout.getHeight() + 30);
            }
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
        if (novelInputBox != null) {
            IMEClose(novelInputBox);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("AlmanacFragment");
        if (novelInputBox != null) {
            IMEClose(novelInputBox);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void HuangLi(HuangLi huangLi) {
//        suiId.setText(huangLi.getData().getSui_ci().get(1) + " 【" + huangLi.getData().getShengXiao() + "】 " + huangLi.getData().getSui_ci().get(2) + " " + huangLi.getData().getSui_ci().get(0));
//        NongLiMonthDay.setText(huangLi.getData().getNongLiMonth() + Util.LunarCalendarSize(huangLi.getData().getNongLiMonth()) + huangLi.getData().getNongLiDay());
//        shengxiao.setText(huangLi.getData().getStar());
//        bean.sui_ci_shengxiao = huangLi.getData().getSui_ci().get(1) + " 【" + huangLi.getData().getShengXiao() + "】 " + huangLi.getData().getSui_ci().get(2) + " " + huangLi.getData().getSui_ci().get(0);
//        bean.week = "周" + huangLi.getData().getWeek();
    }

    @Override
    public void DanXiangLi(DanXiangLi danXiangLi) {
        bean.setAuthor_name(danXiangLi.getData().getAuthor_name());
        bean.setAuthor_type(danXiangLi.getData().getAuthor_type());
        bean.setContent(danXiangLi.getData().getContent());
        bean.setDate(danXiangLi.getData().getDate());
        bean.setExtract(danXiangLi.getData().getExtract());
        bean.setProduction(danXiangLi.getData().getProduction());
        bean.setType(danXiangLi.getData().getType());
        danxiangliTv.setText(danXiangLi.getData().getContent());
        danxiangliTvDetails.setText(danXiangLi.getData().getExtract());
    }


    @Override
    public void MyEditText() {
    }

    @Override
    public void OnTouch() {
    }

    @Override
    public void query(String content) {
    }

    private boolean ISUP = true;

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ISUP = true;
                Log.e("=====", "上滑-scrollY：3" + scrollY + "-oldScrollY：" + ISUP);
                break;
            case MotionEvent.ACTION_MOVE:
                ISUP = true;
                //移动
                break;
            case MotionEvent.ACTION_UP:
                ISUP = false;
                //抬起
                if (scrollY >= (mCalendarView.getHeight() / 3) && scrollY < mCalendarView.getHeight()) {
                    ObjectAnimator.ofFloat(mCalendarLayout, "translationY", 0).setDuration(100).start();
                    nestedScrollView.scrollTo(0, mCalendarView.getHeight() - mCalendarLayout.getHeight() + 30);
                } else if (scrollY < (mCalendarView.getHeight() / 3)) {
                    ObjectAnimator.ofFloat(mCalendarLayout, "translationY", -mCalendarLayout.getHeight()).setDuration(100).start();
                    nestedScrollView.scrollTo(0, 0);
                }
                break;
        }
        return false;
    }

    @Override
    public void onScrollChange(NestedScrollView nestedScrollView, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

        try {
            if(almanacLogic == null){
                return;
            }
            almanacLogic.Message();
            almanacLogic.BannerShow(adLin.getLocalVisibleRect(new Rect()));
            mCalendarView.setmWeekPagerVisibility(getActivity(), scrollY);
            this.scrollY = scrollY;
            if (scrollY < (mCalendarView.getHeight()) / 3) {
                ObjectAnimator.ofFloat(mCalendarLayout, "translationY", -mCalendarLayout.getHeight()).setDuration(100).start();
                if (!ISUP) {
                    nestedScrollView.scrollTo(0, 0);
    //                ISUP = true;
                }
            } else if (scrollY >= (mCalendarView.getHeight()) / 3 && scrollY < mCalendarView.getHeight()) {
                ObjectAnimator.ofFloat(mCalendarLayout, "translationY", 0).setDuration(100).start();
                if (!ISUP) {
                    nestedScrollView.scrollTo(0, mCalendarView.getHeight() - mCalendarLayout.getHeight() + 30);
    //                ISUP = true;
                }
            } else if (scrollY > mCalendarView.getHeight()) {
                ObjectAnimator.ofFloat(mCalendarLayout, "translationY", -mCalendarLayout.getHeight()).setDuration(100).start();
            }
//        if(scrollY < mCalendarView.getHeight()/2){
//            nestedScrollView.setScaleY(0);
//        }else{
//
//        }
//        mCalendarLayout.setContentView(mCalendarView);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.ad_image_banner_clear)
    public void onClick() {
        adLin.setVisibility(View.GONE);
    }

    @OnClick({R.id.suitable_lin, R.id.avoid_lin, R.id.even_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.suitable_lin:
                if (almanacLogic.getHuangLis() != null) {
                    Intent intent = new Intent(getActivity(), YiJiActivity.class);
                    intent.putExtra("huangLis", almanacLogic.getHuangLis());
                    intent.putExtra("yi_ji", "宜");
                    Objects.requireNonNull(getActivity()).overridePendingTransition(com.constellation.xylibrary.R.anim.in_from_right, com.constellation.xylibrary.R.anim.out_to_left);
                    getActivity().startActivity(intent);
                }

                break;
            case R.id.avoid_lin:
                if (almanacLogic.getHuangLis() != null) {
                    Intent intent1 = new Intent(getActivity(), YiJiActivity.class);
                    intent1.putExtra("huangLis", almanacLogic.getHuangLis());
                    intent1.putExtra("yi_ji", "忌");
                    Objects.requireNonNull(getActivity()).overridePendingTransition(com.constellation.xylibrary.R.anim.in_from_right, com.constellation.xylibrary.R.anim.out_to_left);
                    getActivity().startActivity(intent1);
                }

                break;
            case R.id.even_more:
                AdviseMoreDetailActivity.startActivity(getActivity(), "周公解梦", "http://api.xytq.qukanzixun.com/zgjm","0");
                break;
        }
    }
}
