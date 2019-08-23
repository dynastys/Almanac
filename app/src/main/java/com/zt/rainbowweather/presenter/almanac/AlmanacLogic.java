package com.zt.rainbowweather.presenter.almanac;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.callback.ImageOptions;
import com.bumptech.glide.Glide;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.bytedance.sdk.openadsdk.TTImage;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qq.e.ads.banner2.UnifiedBannerADListener;
import com.qq.e.ads.banner2.UnifiedBannerView;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.comm.util.AdError;
import com.xy.xylibrary.view.MyEditText;
import com.zt.rainbowweather.presenter.PangolinBannerAd;
import com.zt.weather.R;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.xy.xylibrary.base.BaseAdapter;
import com.xy.xylibrary.utils.GlideUtil;
import com.xy.xylibrary.utils.SaveShare;
import com.xy.xylibrary.utils.Utils;
import com.zt.rainbowweather.api.RequestSyntony;
import com.zt.rainbowweather.entity.Icons;
import com.zt.rainbowweather.entity.calendar.CrazyDreamRecommend;
import com.zt.rainbowweather.entity.calendar.DanXiangLi;
import com.zt.rainbowweather.entity.calendar.Festival;
import com.zt.rainbowweather.entity.calendar.HuangLi;
import com.zt.rainbowweather.entity.news.Article;
import com.zt.rainbowweather.presenter.ScrollLinearLayoutManager;
import com.zt.rainbowweather.presenter.request.AlmanacRequest;
import com.zt.rainbowweather.presenter.request.NewsRequest;
import com.zt.rainbowweather.ui.activity.AdviseMoreDetailActivity;
import com.zt.rainbowweather.ui.activity.YiJiActivity;
import com.zt.rainbowweather.utils.ConstUtils;
import com.zt.rainbowweather.utils.SizeUtils;
import com.zt.rainbowweather.utils.Util;
import com.zt.rainbowweather.utils.utils;
import com.zt.rainbowweather.view.FlowLayout;
import com.zt.xuanyin.Interface.AdProtogenesisListener;
import com.zt.xuanyin.controller.Ad;
import com.zt.xuanyin.controller.NativeAd;
import com.zt.xuanyin.entity.model.Native;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlmanacLogic implements BaseQuickAdapter.OnItemClickListener, RequestSyntony<Article>, AdProtogenesisListener,NativeExpressAD.NativeExpressADListener {

    private static AlmanacLogic almanacLogic;

    private Map<String, Calendar> map = new HashMap<>();
    private String[] suitables = new String[]{"安神位", " ", " ", " ", " ", " ", " ", " "};
    private String[] avoids = new String[]{"搬家", " ", " ", " ", " ", " ", " ", " "};
    private BaseAdapter baseAdapter;
    private Activity context;

    private List<Icons.DataBean> linksBeans = new ArrayList<>();
    private RequestBack requestBack;
    private AlmanacRequest almanacRequest;
    private CalendarView mCalendarView;
    private List<String> flowlist = new ArrayList<>();
    private String text;
    private boolean ISfirst = false;
    private List<Article.DataBean> beans = new ArrayList<>();
    private BaseAdapter listbaseAdapter;
    private HuangLi huangLis;
    private Map<Integer, Article.DataBean> adMap = new HashMap<>();
    private List<TextView> textViewList = new ArrayList<>();
    private int AdSize = 10;
    private Thread thread;
    private List<Integer> integerList = new ArrayList<>();
    private TextView textView,textView2;
    private int pageindex = 1;
    private NativeAd nativelogic;
    private TTAdNative mTTAdNative;
    private NativeExpressAD nativeExpressAD;

    public static AlmanacLogic getAlmanacLogic() {
        if (almanacLogic == null) {
            synchronized (AlmanacLogic.class) {
                if (almanacLogic == null) {
                    almanacLogic = new AlmanacLogic();
                }
            }
        }
        return almanacLogic;
    }

    public void setRequestBack(RequestBack requestBack) {
        this.requestBack = requestBack;
        almanacRequest = AlmanacRequest.getAlmanacRequest();
    }

    public void setFestivalData(Context context, String date, CalendarView mCalendarView,CalendarView calendarView1) {
        this.mCalendarView = mCalendarView;
        try {
            List<Festival.DataBean> list = LitePal.findAll(Festival.DataBean.class);
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    String[] strings = list.get(i).getDate().split("T")[0].split("-");
                    if (list.get(i).getWork_type().equals("休")) {
                        map.put(getSchemeCalendar(Util.TurnDigital(strings[0]), Util.TurnDigital(strings[1]), Util.TurnDigital(strings[2]), 0xFFF44727, "假").toString(),
                                getSchemeCalendar(Util.TurnDigital(strings[0]), Util.TurnDigital(strings[1]), Util.TurnDigital(strings[2]), 0xFFF44727, "假"));
                    } else {
                        map.put(getSchemeCalendar(Util.TurnDigital(strings[0]), Util.TurnDigital(strings[1]), Util.TurnDigital(strings[2]), 0xFF00B0E6, list.get(i).getWork_type()).toString(),
                                getSchemeCalendar(Util.TurnDigital(strings[0]), Util.TurnDigital(strings[1]), Util.TurnDigital(strings[2]), 0xFF00B0E6, list.get(i).getWork_type()));
                    }
                }
                //此方法在巨大的数据量上不影响遍历性能，推荐使用
                mCalendarView.setSchemeDate(map);
                calendarView1.setSchemeDate(map);
            } else {
                getAlmanacData(context, date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取节日数据
     */
    public void getAlmanacData(Context context, String date) {
        try {
            List<Festival.DataBean> list = LitePal.findAll(Festival.DataBean.class);
            if (list != null && list.size() > 0) {
                LitePal.deleteAll(Festival.DataBean.class);
            }
            if (almanacRequest == null) {
                almanacRequest = new AlmanacRequest();
            }
            almanacRequest.getFestivalData(context, date, new RequestSyntony<Festival>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Festival festival) {
                    LitePal.saveAll(festival.getData());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isVisible(View v) {
        if (v == null) {
            return false;
        }
        return v.getLocalVisibleRect(new Rect());
    }

    public void Message() {
        try {
            if (isVisible(textView)) {
                textView = null;
                NewsRequest.getNewsRequest().getNewsListData(context, "41", pageindex, 10, AlmanacLogic.this);
            }
            if(textView2 != null && isVisible(textView2) ){
                textView2 = null;
                if(adMap.size() == 1 && adMap.get(4).nativelogic != null){
                    adMap.get(4).nativelogic.AdShow(null);
                }
             }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(Article article) {
        try {
            if (article.getData() != null && article.getData().size() > 0) {
                AdSize = article.getData().size();
                if (beans.size() == 0) {
                    listbaseAdapter.setNewData(article.getData());
                } else {
                    listbaseAdapter.addData(article.getData());
                }
                pageindex++;
                beans.addAll(article.getData());

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //加载更多完成
                        listbaseAdapter.loadMoreComplete();
//                        listbaseAdapter.loadMoreEnd();
                    }
                }, 100);
            } else {
                //加载完成
                listbaseAdapter.loadMoreEnd();
            }
//            for (int i = 0; i < 2; i++) {
            String ISAD = SaveShare.getValue(context, "ISAD");
            if (!TextUtils.isEmpty(ISAD) && ISAD.equals("1")) {
                //                // 获得信息流广告对象
                Ad.getAd().NativeAD(context, "98f8e423-02e0-49f5-989f-af46f5c59203", "2b937c83-dd4f-4936-b7e5-3b66dbae0cca", "67C53558D3E3485EA681EA21735A5003", this);
            }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onADReady(Native aNative, NativeAd nativeAd) {
        try {
            if (!TextUtils.isEmpty(nativeAd.nativeObject.sdk_code)) {
                 if(nativeAd.nativeObject.sdk_code.equals("GDT_SDK")) {
                     refreshAd();
                }else if(nativeAd.nativeObject.sdk_code.equals("TOUTIAO_SDK")) {
                    loadListAd(nativeAd);
                }
            } else {
                Article.DataBean dateBean = new Article.DataBean();
                dateBean.nativelogic = nativeAd;
                dateBean.setList_show_type(1);
                dateBean.setFrom_name("广告");
                dateBean.setArticle_imgs(aNative.infoIcon);
                dateBean.setTitle(aNative.title + "\n" + aNative.desc);
                if(adMap.size() > 0){
                    listbaseAdapter.addData( beans.size() - AdSize - 1, dateBean);
                    beans.add(beans.size() - AdSize - 1, dateBean);
                    adMap.put(beans.size() - AdSize, dateBean);
                }else{
                    listbaseAdapter.addData( 1, dateBean);
                    beans.add(1, dateBean);
                    adMap.put(1, dateBean);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAdFailedToLoad(String s) {

    }

    @Override
    public void onADLoaded(List<NativeExpressADView> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        int count = beans.size();
        for (NativeExpressADView ad : list) {
            Article.DataBean dateBean = new Article.DataBean();
            dateBean.nativeExpressADView = ad;
            dateBean.setFrom_name("广告");
//            dateBean.nativelogic = nativelogic;
            dateBean.setList_show_type(3);
            if(adMap.size() > 0){
                listbaseAdapter.addData( beans.size() - AdSize - 1, dateBean);
                beans.add(beans.size() - AdSize - 1, dateBean);
                adMap.put(beans.size() - AdSize, dateBean);
            }else{
                listbaseAdapter.addData( 1, dateBean);
                beans.add(1, dateBean);
                adMap.put(1, dateBean);
            }

        }
    }

    @Override
    public void onRenderFail(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onRenderSuccess(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADExposure(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADClicked(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADClosed(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADLeftApplication(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onNoAD(AdError adError) {

    }

    public interface RequestBack {

        void HuangLi(HuangLi huangLi);

        void DanXiangLi(DanXiangLi danXiangLi);
    }

    public HuangLi getHuangLis() {
        return huangLis;
    }

    public interface InfiiteBack{

        void HuangLi(HuangLi huangLi);
    }

    /**
     * 获取黄历数据 参数：日期(yyyy-MM-dd) 返回：当天数据
     *
     * @param date 2019-5-29
     */
    public void getHuangLiData(Context context, String date,InfiiteBack infiiteBack) {
        try {
            almanacRequest.getHuangLiData(context, date, new RequestSyntony<HuangLi>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(HuangLi huangLi) {
                    huangLis = huangLi;
                    requestBack.HuangLi(huangLi);
                    infiiteBack.HuangLi(huangLi);


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean aBoolean = true;

    public void BannerShow(boolean b) {
        if (nativelogic != null && b && aBoolean) {
            aBoolean = false;
            nativelogic.AdShow(null);
        }
    }

    public void BannerAd(Activity context, LinearLayout adLin, RelativeLayout GDTAd, FrameLayout bannerContainer, ImageView adImageBanner) {//
        try {
            this.context = context;
            String ISAD = SaveShare.getValue(context, "ISAD");
            if (!TextUtils.isEmpty(ISAD) && ISAD.equals("1")) {
                // 获得原生banner广告对象
                nativelogic = Ad.getAd().NativeAD(context, "98f8e423-02e0-49f5-989f-af46f5c59203", "d79789c6-3683-4ae2-9fdf-d365d2eb7bcf", "67C53558D3E3485EA681EA21735A5003", new AdProtogenesisListener() {
                    @Override
                    public void onADReady(Native url, NativeAd nativelogic) {
                        if (TextUtils.isEmpty(nativelogic.nativeObject.sdk_code)) {
                            Glide.with(context).load(url.src).into(adImageBanner);
                            adLin.setVisibility(View.VISIBLE);
                            bannerContainer.setVisibility(View.GONE);
                            GDTAd.setVisibility(View.GONE);
                        }else if(nativelogic.nativeObject.sdk_code.equals("GDT_SDK")) {
                            GDTBanner(GDTAd);
                            adLin.setVisibility(View.GONE);
                            bannerContainer.setVisibility(View.GONE);
                        }else if(nativelogic.nativeObject.sdk_code.equals("TOUTIAO_SDK")) {
                            adLin.setVisibility(View.GONE);
                            GDTAd.setVisibility(View.GONE);
                            PangolinBannerAd.getPangolinBannerAd().BannerAD(context,bannerContainer,nativelogic);
                        }
                    }

                    @Override
                    public void onAdFailedToLoad(String error) {
                        adLin.setVisibility(View.GONE);
                    }
                });
                adImageBanner.setOnClickListener(view -> {
                    //点击上报
                    nativelogic.OnClick(null);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private UnifiedBannerView GDTBanner(RelativeLayout relatAd){
        // 创建 Banner 广告 AdView 对象
        // appId : 在 http://e.qq.com/dev/ 能看到的 app 唯一字符串
        // posId : 在 http://e.qq.com/dev/ 生成的数字串，并非 appid 或者 appkey
        UnifiedBannerView banner = new UnifiedBannerView(context,  nativelogic.nativeObject.appid, nativelogic.nativeObject.posid, new UnifiedBannerADListener() {
            @Override
            public void onNoAD(AdError adError) {
//                Log.i("AD_DEMO", "BannerNoAD，eCode=" + adError.getErrorCode());
                try {
                    relatAd.setVisibility(View.GONE);
//                    if (nativelogic != null) {
//                        nativelogic.OnRequest(adError.getErrorCode()+"",adError.getErrorMsg());
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onADReceive() {
                try {
                    relatAd.setVisibility(View.VISIBLE);
                    if (nativelogic != null) {
                        nativelogic.OnRequest("0","msg");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("AD_DEMO", "onADReceive");
            }

            @Override
            public void onADExposure() {
                try {
                    if(nativelogic != null){
                        nativelogic.AdShow(relatAd);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("AD_DEMO", "onADExposure");
            }

            @Override
            public void onADClosed() {
                Log.i("AD_DEMO", "onADClosed");
            }

            @Override
            public void onADClicked() {
                Log.i("AD_DEMO", "onADClicked");
                try {
                    if(nativelogic != null){
                        nativelogic.OnClick(relatAd);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onADLeftApplication() {
                Log.i("AD_DEMO", "onADLeftApplication");
            }

            @Override
            public void onADOpenOverlay() {

            }

            @Override
            public void onADCloseOverlay() {

            }
        });
        //设置广告轮播时间，为0或30~120之间的数字，单位为s,0标识不自动轮播
        banner.setRefresh(30);

        relatAd.addView(banner);
        /* 发起广告请求，收到广告数据后会展示数据     */
        banner.loadAD();

        return banner;
    }
    /**
     * 获取单向历数据
     *
     * @param date 2019-5-29
     */
    public void getDanXiangLiData(Context context, String date) {

        almanacRequest.getDanXiangLiData(context, date, new RequestSyntony<DanXiangLi>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(DanXiangLi danXiangLi) {
                requestBack.DanXiangLi(danXiangLi);
            }
        });
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        calendar.addScheme(new Calendar.Scheme());
        calendar.addScheme(0xFF008800, "假");
        calendar.addScheme(0xFF008800, "节");
        return calendar;
    }

    public void setSuitable(Activity context, String ISYIJI,HuangLi huangLi, RecyclerView recyclerView) {
        this.context = context;
        List<String> list = new ArrayList<>();
        List<String> list1 = new ArrayList<>();
        try {
            switch (ISYIJI) {
                case "宜":
                    list1.clear();
                    for (int i = 0; i < huangLi.getData().getYi().size(); i++) {
                        if (huangLi.getData().getYi().get(i).getValues().size() >= 8) {
                            if(list1.size() == 7){
                                break;
                            }
                            for (int j = 0; j < 7 - list1.size(); j++) {
                                list1.add(huangLi.getData().getYi().get(i).getValues().get(j));
                            }
                        } else {
                            for (int j = 0; j < huangLi.getData().getYi().get(i).getValues().size(); j++) {
                                if(list1.size() == 7){
                                    break;
                                }
                                list1.add(huangLi.getData().getYi().get(i).getValues().get(j));
                            }

                        }
                    }
                    list1.add("...");
                    int size1 = list1.size();
                    for (int i = 0; i < (8 - size1); i++) {
                        list1.add(" ");
                    }

                    recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
                    BaseAdapter  baseAdapter2 = new BaseAdapter<>(R.layout.yi_ji_item, list1, (viewHolder, item) -> {
                        viewHolder.setText(R.id.yi_ji_tv, item);
                    });
                    baseAdapter2.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            if(huangLi != null){
                                Intent intent = new Intent(context, YiJiActivity.class);
                                intent.putExtra("huangLis", huangLi);
                                intent.putExtra("yi_ji","宜");
                                context.overridePendingTransition(com.constellation.xylibrary.R.anim.in_from_right, com.constellation.xylibrary.R.anim.out_to_left);
                                context.startActivity(intent);
                            }
                        }
                    });
                    recyclerView.setAdapter(baseAdapter2);
                    break;
                case "忌":
                    list.clear();
                    for (int i = 0; i < huangLi.getData().getJi().size(); i++) {
                        if (huangLi.getData().getJi().get(i).getValues().size() >= 8) {
                            if(list.size() == 7){
                                break;
                            }
                            for (int j = 0; j < 7 - list.size(); j++) {
                                list.add(huangLi.getData().getJi().get(i).getValues().get(j));
                            }
                        } else {
                            for (int j = 0; j < huangLi.getData().getJi().get(i).getValues().size(); j++) {
                                if(list.size() == 7){
                                    break;
                                }
                                list.add(huangLi.getData().getJi().get(i).getValues().get(j));
                            }
                        }
                    }
                    list.add("...");
                    int size = list.size();
                    for (int j = 0; j < (8 - size); j++) {
                        list.add(" ");
                    }
                    recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
                    BaseAdapter  baseAdapter1 = new BaseAdapter<>(R.layout.ji_item, list, (viewHolder, item) -> {
                        viewHolder.setText(R.id.ji_tv, item);
                    });
                    baseAdapter1.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            if(huangLi != null){
                                Intent intent = new Intent(context, YiJiActivity.class);
                                intent.putExtra("huangLis", huangLi);
                                intent.putExtra("yi_ji","忌");
                                context.overridePendingTransition(com.constellation.xylibrary.R.anim.in_from_right, com.constellation.xylibrary.R.anim.out_to_left);
                                context.startActivity(intent);
                            }

                        }
                    });
                    recyclerView.setAdapter(baseAdapter1);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setNoveInputBox(Activity context,MyEditText novelInputBox, FlowLayout usedSearchRec, RecyclerView listRecycler) {
        try {
            mTTAdNative = TTAdSdk.getAdManager().createAdNative(context);
            //申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
            TTAdSdk.getAdManager().requestPermissionIfNecessary(context);
            //绑定输入法的回车键请求数据
            novelInputBox.setOnEditorActionListener((arg0, arg1, arg2) -> {
                if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
                    RequestData(novelInputBox);
                }
                return false;
            });
            usedSearchRec.removeAllViews();
            AlmanacRequest.getAlmanacRequest().getCrazyDreamRecommendData(context, new RequestSyntony<CrazyDreamRecommend>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(CrazyDreamRecommend crazyDreamRecommend) {
                    flowlist.clear();
                    flowlist.add("猪");
                    flowlist.add("狗");
                    flowlist.add("死人");
                    flowlist.add("僵尸");
                    flowlist.add("狗头");
                    //为布局添加内容
                    for (int i = 0; i < crazyDreamRecommend.getData().size(); i++) {
                        addTextView(crazyDreamRecommend.getData().get(i).getName(), usedSearchRec, novelInputBox);
                    }
                }
            });

            if (ConstUtils.almanac_news) {
                ScrollLinearLayoutManager setScrollEnable = new ScrollLinearLayoutManager(context);
                setScrollEnable.setScrollEnable(false);
                listRecycler.setLayoutManager(setScrollEnable);
                listbaseAdapter = new BaseAdapter<>(R.layout.item_advise_detail_one, beans, (viewHolder, item) -> {
                    TTFeedAd ttFeedAd;
                    switch (item.getList_show_type()) {
                        case 0:
                        case 1:
                            viewHolder.getView(R.id.iv_title_rel).setVisibility(View.VISIBLE);
                            if(item.ttFeedAd != null){
                                bindData(viewHolder, item.ttFeedAd,item.nativelogic);
                                if (item.ttFeedAd.getImageList() != null && !item.ttFeedAd.getImageList().isEmpty()) {
                                    TTImage image = item.ttFeedAd.getImageList().get(0);
                                    if (image != null && image.isValid()) {
                                        viewHolder.getView(R.id.iv_title).setVisibility(View.VISIBLE);
                                        GlideUtil.getGlideUtil().setImages(context, item.ttFeedAd.getIcon().getImageUrl(), viewHolder.getView(R.id.iv_title), 0);
                                    }
                                }else{
                                    viewHolder.getView(R.id.iv_title).setVisibility(View.GONE);
                                }
                            }else{
                                if(item.getArticle_imgs() != null && item.getArticle_imgs().size() > 0){
                                    viewHolder.getView(R.id.iv_title).setVisibility(View.VISIBLE);
                                    GlideUtil.getGlideUtil().setImages(context, item.getArticle_imgs().get(0), viewHolder.getView(R.id.iv_title), 0);
                                }
                            }
                            viewHolder.setText(R.id.tv_title, item.getTitle())
                                    .setText(R.id.tv_from, item.getFrom_name());
                            break;
                        case 2:
                            viewHolder.getView(R.id.iv_title_r_rel).setVisibility(View.VISIBLE);
                            if(item.getArticle_imgs() != null && item.getArticle_imgs().size() > 0){
                                GlideUtil.getGlideUtil().setImages(context,item.getArticle_imgs().get(0), viewHolder.getView(R.id.iv_title_r), 0);
                            }
                            viewHolder.setText(R.id.tv_title_r, item.getTitle())
                                    .setText(R.id.tv_from_r, item.getFrom_name());
                            break;
                        case 4:
                            try {
                                viewHolder.getView(R.id.tv_title_x_lin).setVisibility(View.VISIBLE);
                                if(item.ttFeedAd != null){
                                    ttFeedAd = item.ttFeedAd;
                                    bindData(viewHolder, item.ttFeedAd,item.nativelogic);
                                    if (ttFeedAd.getImageList() != null && ttFeedAd.getImageList().size() >= 3) {
                                        TTImage image1 = ttFeedAd.getImageList().get(0);
                                        TTImage image2 = ttFeedAd.getImageList().get(1);
                                        TTImage image3 = ttFeedAd.getImageList().get(2);
                                        if (image1 != null && image1.isValid()) {
                                            GlideUtil.getGlideUtil().setImages(context,image1.getImageUrl(), viewHolder.getView(R.id.iv_image_x1), 0);
                                        }
                                        if (image2 != null && image2.isValid()) {
                                            GlideUtil.getGlideUtil().setImages(context, image2.getImageUrl(), viewHolder.getView(R.id.iv_image_x2), 0);
                                        }
                                        if (image3 != null && image3.isValid()) {
                                            GlideUtil.getGlideUtil().setImages(context, image3.getImageUrl(), viewHolder.getView(R.id.iv_image_x3), 0);
                                        }
                                    }
                                }else{
                                    if(item.getArticle_imgs() != null && item.getArticle_imgs().size() > 0){
                                        GlideUtil.getGlideUtil().setImages(context,item.getArticle_imgs().get(0), viewHolder.getView(R.id.iv_image_x1), 0);
                                        GlideUtil.getGlideUtil().setImages(context, item.getArticle_imgs().get(1), viewHolder.getView(R.id.iv_image_x2), 0);
                                        GlideUtil.getGlideUtil().setImages(context, item.getArticle_imgs().get(2), viewHolder.getView(R.id.iv_image_x3), 0);
                                    }

                                }
                                viewHolder.setText(R.id.tv_title_x, item.getTitle())
                                        .setText(R.id.tv_from_x, item.getFrom_name());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case 3:
                            try {
                                FrameLayout frameLayout = ((FrameLayout)viewHolder.getView(R.id.iv_listitem_video));
                                viewHolder.getView(R.id.tv_title_d_lin).setVisibility(View.VISIBLE);
                                viewHolder.getView(R.id.iv_image_d).setVisibility(View.VISIBLE);
                                frameLayout.setVisibility(View.GONE);
                                if(item.ttFeedAd != null){
                                    try {
                                        ttFeedAd = item.ttFeedAd;
                                        bindData(viewHolder, item.ttFeedAd,item.nativelogic);
                                        if (ttFeedAd.getImageList() != null && !ttFeedAd.getImageList().isEmpty()) {
                                            TTImage image = ttFeedAd.getImageList().get(0);
                                            if (image != null && image.isValid()) {
                                                GlideUtil.getGlideUtil().setImages(context,image.getImageUrl(), viewHolder.getView(R.id.iv_image_d), 0);
                                            }
                                        }
                                        View video = ttFeedAd.getAdView();
                                        if (video != null) {
                                            ttFeedAd.setVideoAdListener(new TTFeedAd.VideoAdListener() {
                                                @Override
                                                public void onVideoLoad(TTFeedAd ad) {

                                                }

                                                @Override
                                                public void onVideoError(int errorCode, int extraCode) {

                                                }

                                                @Override
                                                public void onVideoAdStartPlay(TTFeedAd ad) {

                                                }

                                                @Override
                                                public void onVideoAdPaused(TTFeedAd ad) {

                                                }

                                                @Override
                                                public void onVideoAdContinuePlay(TTFeedAd ad) {

                                                }
                                            });
                                            if (video.getParent() == null) {
                                                frameLayout.removeAllViews();
                                                frameLayout.addView(video);
                                                frameLayout.setVisibility(View.VISIBLE);
                                                viewHolder.getView(R.id.iv_image_d).setVisibility(View.GONE);
                                            }
                                        }
                                        viewHolder.setText(R.id.tv_title_d, item.getTitle())
                                                .setText(R.id.tv_from_d, item.getFrom_name());
                                        Log.e("toutiao", "initData: "+item.getFrom_name() );
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }else{
                                    try {
                                        if(item.nativeExpressADView != null){
                                            frameLayout.setVisibility(View.VISIBLE);
                                            viewHolder.getView(R.id.iv_image_d).setVisibility(View.GONE);
                                            viewHolder.getView(R.id.tv_title_d).setVisibility(View.GONE);
                                            viewHolder.setText(R.id.tv_from_d, item.getFrom_name());
                                            viewHolder.getView(R.id.tv_from_d_lin).setVisibility(View.GONE);
                                            if (frameLayout.getChildCount() > 0) {
                                                frameLayout.removeAllViews();
                                            }
        //                            if (item.nativeExpressADView.getBoundData().getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
        //                                item.nativeExpressADView.setMediaListener(mediaListener);
        //                            }
                                            try {
                                                // 广告可见才会产生曝光，否则将无法产生收益。NativeExpressADView
                                                frameLayout.addView((NativeExpressADView)item.nativeExpressADView);
                                            } catch (Exception e) {
                                                if (frameLayout.getChildCount() > 0) {
                                                    frameLayout.removeAllViews();
                                                }
                                                frameLayout.addView((NativeExpressADView)item.nativeExpressADView);
                                                e.printStackTrace();
                                            }
                                            ((NativeExpressADView)item.nativeExpressADView).render();
                                        }else{
                                            if(item.getArticle_imgs() != null && item.getArticle_imgs().size() > 0){
                                                GlideUtil.getGlideUtil().setImages(context,item.getArticle_imgs().get(0), viewHolder.getView(R.id.iv_image_d), 0);
                                            }
                                            viewHolder.setText(R.id.tv_title_d, item.getTitle())
                                                    .setText(R.id.tv_from_d, item.getFrom_name());
                                            Log.e("toutiao", "initData: "+item.getFrom_name() );
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                    if (adMap.size() > 1 && item.nativelogic != null) {
                        item.nativelogic.AdShow(null);
                        textViewList.add(viewHolder.getView(R.id.tv_title));
                    }
                    if(viewHolder.getAdapterPosition() == 4){
                        textView2 = viewHolder.getView(R.id.tv_title);
                    }
                    if (beans.size() == viewHolder.getAdapterPosition() + 1) {
                        textView = viewHolder.getView(R.id.tv_title);
                    }

                });
                listbaseAdapter.setOnItemClickListener((adapter, view1, position) -> {
                    if (beans.get(position) != null && beans.get(position).getFrom_name().equals("广告")) {
                        beans.get(position).nativelogic.OnClick(null);
                    } else {
                        AdviseMoreDetailActivity.startActivity(context, beans.get(position).getTitle(), beans.get(position).getHtml_url(),"1");
                    }
                });
                // 滑动最后一个Item的时候回调onLoadMoreRequested方法
                listbaseAdapter.setOnLoadMoreListener(() -> {
    //            NewsRequest.getNewsRequest().getNewsListData(context, "1", 1, 20, AlmanacLogic.this);
                }, listRecycler);
                // 滑动最后一个Item的时候回调onLoadMoreRequested方法
                listRecycler.setItemAnimator(new DefaultItemAnimator());
    //        listRecycler.addItemDecoration(new DividerItemDecoration(context, 1));
                listRecycler.setAdapter(listbaseAdapter);
                NewsRequest.getNewsRequest().getNewsListData(context, "41", pageindex, 10, AlmanacLogic.this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 动态添加布局
     *
     * @param str
     */
    private void addTextView(String str, FlowLayout folowLayoutHot, MyEditText novelInputBox) {
//        View child = LayoutInflater.from(SeekActivity.this).inflate(R.layout.item_seek,null);
        try {
            TextView child = new TextView(context);
            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(Utils.getWindowsWidth(context) / 7, SizeUtils.dp2px(27));
            params.setMargins(20, 20, 20, 20);
            child.setPadding(20, 8, 20, 8);
            child.setLayoutParams(params);
            child.setMaxEms(10);
            child.setMaxLines(1);
            child.setGravity(Gravity.CENTER);
            child.setEllipsize(TextUtils.TruncateAt.END);
            child.setBackgroundResource(R.drawable.shape_tv_blue);
            child.setText(str);
            child.setTextSize(12);
            child.setTextColor(context.getResources().getColor(R.color.seek));
            initEvents(child, novelInputBox);//监听
            folowLayoutHot.addView(child);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 为每个view 添加点击事件
     */
    private void initEvents(final TextView tv, final MyEditText novelInputBox) {
        tv.setOnClickListener(v -> {
            AdviseMoreDetailActivity.startActivity(context, "周公解梦", "http://121.199.42.243:8001/zgjm/search?keyword=" + tv.getText().toString(),"0");
//            novelInputBox.setText(tv.getText().toString());
//                InitData(types,tv.getText().toString(), sort, ISload, type, column_id);
        });
    }


    //保存搜索记录，并进行搜索
    private void RequestData(MyEditText novelInputBox) {
        if (!TextUtils.isEmpty(novelInputBox.getText().toString())) {
            AdviseMoreDetailActivity.startActivity(context, "周公解梦", "http://121.199.42.243:8001/zgjm/search?keyword=" + novelInputBox.getText().toString(),"0");
        } else {
            AdviseMoreDetailActivity.startActivity(context, "周公解梦", "http://121.199.42.243:8001/zgjm/search?keyword=美女","0");
//            ToastUtils.showLong("输入内容为空哦！");
        }
    }

    public void setServiceData(RecyclerView ListRecycler) {
        try {
            if (ConstUtils.almanac_icon) {
                ListRecycler.setNestedScrollingEnabled(false);
                ListRecycler.setLayoutManager(new GridLayoutManager(context, 4));
    //        GridItemDecoration divider = new GridItemDecoration.Builder(context)
    //                .setHorizontalSpan(R.dimen.dp_1)
    //                .setVerticalSpan(R.dimen.dp_1)
    //                .setColorResource(R.color.nb_divider_narrow)
    //                .setShowLastLine(true)
    //                .build();
    //        ListRecycler.addItemDecoration(divider);
                AlmanacRequest.getAlmanacRequest().getGainIconData(context, 1, "", new RequestSyntony<Icons>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Icons icons) {
                        linksBeans.addAll(icons.getData());
                        baseAdapter.setNewData(icons.getData());
                    }
                });
                baseAdapter = new BaseAdapter<>(R.layout.popup_recycler_item, linksBeans, (viewHolder, item) -> {
                    try {
                        viewHolder.setText(R.id.popup_recycler_tv, item.getTitle());
                        GlideUtil.getGlideUtil().setImages(context, item.getCover(), viewHolder.getView(R.id.popup_recycler_image));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                baseAdapter.setOnItemClickListener((adapter, view, position) -> {
                    switch (linksBeans.get(position).getIcon_type_id()) {
                        case 0://跳转
                            AdviseMoreDetailActivity.startActivity(context, linksBeans.get(position).getTitle(), linksBeans.get(position).getLink(),"0");
                            break;
                        case 1://下载
                            utils.Download(context, linksBeans.get(position).getLink());
                            break;
                        default:
                    }
                });
                ListRecycler.setAdapter(baseAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    /*GDTAD*/
    private void refreshAd(){
        try {
            /**
             *  如果选择支持视频的模版样式，请使用{@link Constants#NativeExpressSupportVideoPosID}
             */
            nativeExpressAD = new NativeExpressAD(context, new ADSize(ADSize.FULL_WIDTH, ADSize.AUTO_HEIGHT), "1109529834", "3050278581035653", this);
            Map<String, String> tags = new HashMap<>();
            tags.put("native_express_tag_1", "native_express_value_1");
            tags.put("native_express_tag_2", "native_express_value_2");
            nativeExpressAD.setTag(tags);
//        nativeExpressAD.setVideoOption(new VideoOption.Builder()
//                .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.WIFI) // 设置什么网络环境下可以自动播放视频
//                .setAutoPlayMuted(true) // 设置自动播放视频时，是否静音
//                .build()); // setVideoOption是可选的，开发者可根据需要选择是否配置
//        nativeExpressAD.setMaxVideoDuration(getMaxVideoDuration());
            nativeExpressAD.loadAD(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 加载feed广告
     */
    private void loadListAd(NativeAd nativelogic) {
        try {
            //feed广告请求类型参数
            AdSlot adSlot = new AdSlot.Builder()
                    .setCodeId("923044298")
                    .setSupportDeepLink(true)
                    .setImageAcceptedSize(640, 320)
                    .setAdCount(1)
                    .build();
            //调用feed广告异步请求接口
            mTTAdNative.loadFeedAd(adSlot, new TTAdNative.FeedAdListener() {
                @Override
                public void onError(int code, String message) {

                }

                @Override
                public void onFeedAdLoad(List<TTFeedAd> ads) {
                    if (ads == null || ads.isEmpty()) {
                        return;
                    }
                    int count = beans.size();
                    for (TTFeedAd ad : ads) {
                        Article.DataBean dateBean = new Article.DataBean();
                        dateBean.ttFeedAd = ad;
                        dateBean.nativelogic = nativelogic;
                        dateBean.setFrom_name(TextUtils.isEmpty(ad.getSource()) ? "广告" : ad.getSource());
                        Log.e("toutiao", "onFeedAdLoad: " + dateBean.getFrom_name());
                        switch (ad.getImageMode()){
                            case 3://大图
                                dateBean.setList_show_type(3);
                                break;
                            case 2://小图
                                dateBean.setList_show_type(1);
                                break;
                            case 4://组图
                                dateBean.setList_show_type(4);
                                break;
                            case 5://视频
                                dateBean.setList_show_type(3);
                                break;
                        }
                        dateBean.setTitle(ad.getTitle() + "\n" + ad.getDescription());
                        if(adMap.size() > 0){
                            listbaseAdapter.addData( beans.size() - AdSize - 1, dateBean);
                            beans.add(beans.size() - AdSize - 1, dateBean);
                            adMap.put(beans.size() - AdSize, dateBean);
                        }else{
                            listbaseAdapter.addData( 1, dateBean);
                            beans.add(1, dateBean);
                            adMap.put(1, dateBean);
                        }



                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindData(final BaseViewHolder adViewHolder, TTFeedAd ad, NativeAd nativelogic) {
        try {
            //可以被点击的view, 也可以把convertView放进来意味item可被点击
            List<View> clickViewList = new ArrayList<>();
            clickViewList.add(adViewHolder.itemView);
            //触发创意广告的view（点击下载或拨打电话）
            List<View> creativeViewList = new ArrayList<>();
//        creativeViewList.add(adViewHolder.mCreativeButton);
            //如果需要点击图文区域也能进行下载或者拨打电话动作，请将图文区域的view传入
//            creativeViewList.add(convertView);
            //重要! 这个涉及到广告计费，必须正确调用。convertView必须使用ViewGroup。
            ad.registerViewForInteraction((ViewGroup) adViewHolder.itemView, clickViewList, creativeViewList, new TTNativeAd.AdInteractionListener() {
                @Override
                public void onAdClicked(View view, TTNativeAd ad) {
                    if (ad != null && nativelogic != null) {
                        nativelogic.OnClick(null);
                    }
                }

                @Override
                public void onAdCreativeClick(View view, TTNativeAd ad) {
    //                if (ad != null) {
    //                    TToast.show(mContext, "广告" + ad.getTitle() + "被创意按钮被点击");
    //                }
                }

                @Override
                public void onAdShow(TTNativeAd ad) {
                    if (ad != null && nativelogic != null) {
                        nativelogic.AdShow(null);
                    }
                }
            });
            TTImage icon = ad.getIcon();
            if (icon != null && icon.isValid()) {
                ImageOptions options = new ImageOptions();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Button adCreativeButton = adViewHolder.mCreativeButton;
//        switch (ad.getInteractionType()) {
//            case TTAdConstant.INTERACTION_TYPE_DOWNLOAD:
//                //如果初始化ttAdManager.createAdNative(getApplicationContext())没有传入activity 则需要在此传activity，否则影响使用Dislike逻辑
//                if (mContext instanceof Activity) {
//                    ad.setActivityForDownloadApp((Activity) mContext);
//                }
//                adCreativeButton.setVisibility(View.VISIBLE);
//                adViewHolder.mStopButton.setVisibility(View.VISIBLE);
//                adViewHolder.mRemoveButton.setVisibility(View.VISIBLE);
//                bindDownloadListener(adCreativeButton, adViewHolder, ad);
//                //绑定下载状态控制器
//                bindDownLoadStatusController(adViewHolder, ad);
//                break;
//            case TTAdConstant.INTERACTION_TYPE_DIAL:
//                adCreativeButton.setVisibility(View.VISIBLE);
//                adCreativeButton.setText("立即拨打");
//                adViewHolder.mStopButton.setVisibility(View.GONE);
//                adViewHolder.mRemoveButton.setVisibility(View.GONE);
//                break;
//            case TTAdConstant.INTERACTION_TYPE_LANDING_PAGE:
//            case TTAdConstant.INTERACTION_TYPE_BROWSER:
////                    adCreativeButton.setVisibility(View.GONE);
//                adCreativeButton.setVisibility(View.VISIBLE);
//                adCreativeButton.setText("查看详情");
//                adViewHolder.mStopButton.setVisibility(View.GONE);
//                adViewHolder.mRemoveButton.setVisibility(View.GONE);
//                break;
//            default:
//                adCreativeButton.setVisibility(View.GONE);
//                adViewHolder.mStopButton.setVisibility(View.GONE);
//                adViewHolder.mRemoveButton.setVisibility(View.GONE);
//                TToast.show(mContext, "交互类型异常");
//        }
    }

//    private void bindDownLoadStatusController(AdViewHolder adViewHolder, final TTFeedAd ad) {
//        final DownloadStatusController controller = ad.getDownloadStatusController();
//        adViewHolder.mStopButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (controller != null) {
//                    controller.changeDownloadStatus();
//                    TToast.show(mContext, "改变下载状态");
//                    Log.d(TAG, "改变下载状态");
//                }
//            }
//        });
//
//        adViewHolder.mRemoveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (controller != null) {
//                    controller.cancelDownload();
//                    TToast.show(mContext, "取消下载");
//                    Log.d(TAG, "取消下载");
//                }
//            }
//        });
//    }

    private void bindDownloadListener(final Button adCreativeButton, TTFeedAd ad) {
        TTAppDownloadListener downloadListener = new TTAppDownloadListener() {
            @Override
            public void onIdle() {
//                if (!isValid()) {
//                    return;
//                }
                adCreativeButton.setText("开始下载");
//                adViewHolder.mStopButton.setText("开始下载");
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
//                if (!isValid()) {
//                    return;
//                }
                if (totalBytes <= 0L) {
                    adCreativeButton.setText("下载中 percent: 0");
                } else {
                    adCreativeButton.setText("下载中 percent: " + (currBytes * 100 / totalBytes));
                }
//                adViewHolder.mStopButton.setText("下载中");
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
//                if (!isValid()) {
//                    return;
//                }
                if (totalBytes <= 0L) {
                    adCreativeButton.setText("下载中 percent: 0");
                } else {
                    adCreativeButton.setText("下载暂停 percent: " + (currBytes * 100 / totalBytes));
                }
//                adViewHolder.mStopButton.setText("下载暂停");
            }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
//                if (!isValid()) {
//                    return;
//                }
                adCreativeButton.setText("重新下载");
//                adViewHolder.mStopButton.setText("重新下载");
            }

            @Override
            public void onInstalled(String fileName, String appName) {
//                if (!isValid()) {
//                    return;
//                }
                adCreativeButton.setText("点击打开");
//                adViewHolder.mStopButton.setText("点击打开");
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
//                if (!isValid()) {
//                    return;
//                }
                adCreativeButton.setText("点击安装");
//                adViewHolder.mStopButton.setText("点击安装");
            }

//            @SuppressWarnings("BooleanMethodIsAlwaysInverted")
//            private boolean isValid() {
//                return mTTAppDownloadListenerMap.get(adViewHolder) == this;
//            }
        };
        //一个ViewHolder对应一个downloadListener, isValid判断当前ViewHolder绑定的listener是不是自己
        ad.setDownloadListener(downloadListener); // 注册下载监听器
//        mTTAppDownloadListenerMap.put(adViewHolder, downloadListener);
    }

}
