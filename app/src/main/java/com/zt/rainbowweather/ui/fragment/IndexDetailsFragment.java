package com.zt.rainbowweather.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.chad.library.adapter.base.BaseViewHolder;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.comm.util.AdError;
import com.umeng.analytics.MobclickAgent;
import com.xy.xylibrary.base.BaseFragment;
import com.xy.xylibrary.utils.SaveShare;
import com.zt.rainbowweather.api.RequestSyntony;
import com.zt.rainbowweather.entity.OutLookWeather;
import com.zt.rainbowweather.entity.advise.AdviseTitleBean;
import com.zt.rainbowweather.entity.news.Article;
import com.zt.rainbowweather.entity.news.ViewPageEvent;
import com.zt.rainbowweather.presenter.request.NewsRequest;
import com.zt.rainbowweather.ui.activity.AdviseDetailActivity;
import com.zt.rainbowweather.ui.activity.AdviseMoreDetailActivity;
import com.zt.rainbowweather.ui.activity.IndexDetailsActivity;
import com.zt.rainbowweather.ui.adapter.AdviseDetailAdapter;
import com.zt.rainbowweather.utils.BarUtils;
import com.zt.weather.R;
import com.zt.xuanyin.Interface.AdProtogenesisListener;
import com.zt.xuanyin.controller.Ad;
import com.zt.xuanyin.controller.NativeAd;
import com.zt.xuanyin.entity.model.Native;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 指数详情
 * */
public class IndexDetailsFragment extends BaseFragment  implements RequestSyntony<Article>,AdProtogenesisListener,NativeExpressAD.NativeExpressADListener {

    @BindView(R.id.index_rv_detail)
    RecyclerView rvDetail;
    Unbinder unbinder;

    private AdviseTitleBean mAdvise;
    private AdviseDetailAdapter mAdapter;
    private HeaderViewHolder headerViewHolder;
    private LinearLayoutManager linearLayoutManager;
    private int[] ID = new int[]{26,28,29,30,31,32,27,33,34,35,36,37,38,40,39,32};
    private int[] icon = new int[]{R.mipmap.comf,R.mipmap.drsg,R.mipmap.flu,R.mipmap.sport,
            R.mipmap.trav,R.mipmap.uv,R.mipmap.cw,R.mipmap.air,
            R.mipmap.ac,R.mipmap.ag,R.mipmap.gl,R.mipmap.mu,
            R.mipmap.airc,R.mipmap.ptfc,R.mipmap.fsh,R.mipmap.spi};
    private Map<Integer,Integer> map;
    private NativeAd nativelogic;
    private int pageindex = 1;
    private boolean ISSHOW = true;
    private String IndexSize = "0";
    private TTAdNative mTTAdNative;
    private NativeExpressAD nativeExpressAD;

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("AdviseDetailActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(getActivity()); //统计时长
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("AdviseDetailActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)，必须保证 onPageEnd 在 onPause 之前调用，因为SDK会在 onPause 中保存onPageEnd统计到的页面数据。
        MobclickAgent.onPause(getActivity());
    }

    private void addHeader() {
        try {
            headerViewHolder = new HeaderViewHolder();
            headerViewHolder.tvHeaderTitle.setText(mAdvise.title);
            headerViewHolder.tvHeaderContent.setText(mAdvise.headerSummary);
            Log.e("mAdvise", "addHeader: "+ map.size());
            if(headerViewHolder.ivHeader != null && map != null){
                headerViewHolder.ivHeader.setImageResource(map.get(Integer.parseInt(mAdvise.indexId)));
            }
            Glide.with(getActivity()).load(mAdvise.headerImgUrl).into(headerViewHolder.ivBg);
            mAdapter.addHeaderView(headerViewHolder.rootView);
            View footer = getLayoutInflater().inflate(R.layout.advise_footer,
                    (ViewGroup) rvDetail.getParent(), false);
            mAdapter.addFooterView(footer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("CheckResult")
    private void getData() {
        mTTAdNative = TTAdSdk.getAdManager().createAdNative(getActivity());
        //申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdSdk.getAdManager().requestPermissionIfNecessary(getActivity());
        NewsRequest.getNewsRequest().getNewsListData(getActivity(),mAdvise.indexId,pageindex,20,IndexDetailsFragment.this);
    }

    private void showAdviseDetail(Article article) {
        try {
            if(article.getData() != null && article.getData().size() > 0){
                pageindex++;
                mAdapter.setNewData(article.getData());
                mAdapter.setOnItemClickListener((adapter, view, position) -> {
                    if(position == 1 && nativelogic != null){
                        nativelogic.OnClick(null);
                    }else{
                        AdviseMoreDetailActivity.startActivity(getActivity(), article.getData().get(position).getTitle(), article.getData().get(position).getHtml_url(),"1");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_index_details;
    }

    @Override
    protected void initData(View view) {
        try {
            try {
                Bundle args = getArguments();
//          获取栏目号（有时候获取不到，需要第二次获取）
                assert args != null;
                mAdvise = (AdviseTitleBean) args.getSerializable("advise");
                IndexSize = args.getString("IndexSize");
             } catch (Exception e) {
                e.printStackTrace();
            }
            if(ISSHOW && IndexSize.equals(SaveShare.getValue(getActivity(),"IndexSize"))){
                showLoadingDialog("");
                ISSHOW = false;
                map = new HashMap<>();
                for (int i = 0; i < ID.length; i++) {
                    map.put(ID[i],icon[i]);
                }
                rvDetail.setLayoutManager(linearLayoutManager = new LinearLayoutManager(getActivity()));
                rvDetail.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration
                        .VERTICAL));
                rvDetail.setAdapter(mAdapter = new AdviseDetailAdapter(null));
                addHeader();
                getData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && rvDetail != null && ISSHOW) {
            showLoadingDialog("");
            ISSHOW = false;
            map = new HashMap<>();
            for (int i = 0; i < ID.length; i++) {
                map.put(ID[i],icon[i]);
            }
            rvDetail.setLayoutManager(linearLayoutManager = new LinearLayoutManager(getActivity()));
            rvDetail.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration
                    .VERTICAL));
            rvDetail.setAdapter(mAdapter = new AdviseDetailAdapter(null));
            addHeader();
            getData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }
    @Override
    public void onCompleted() {
        dismissLoadingDialog();
    }

    @Override
    public void onError(Throwable e) {
    }

    @Override
    public void onNext(Article article) {
        showAdviseDetail(article);
        String ISAD = SaveShare.getValue(getActivity(), "ISAD");
        if (!TextUtils.isEmpty(ISAD) && ISAD.equals("1")) {
            // 获得信息流广告对象
            nativelogic = Ad.getAd().NativeAD(getActivity(), "98f8e423-02e0-49f5-989f-af46f5c59203", "a6f5748d-f822-489b-811c-b08ba658c096", "67C53558D3E3485EA681EA21735A5003", this);
        }
    }



    @Override
    public void onADReady(Native aNative, NativeAd nativelogic) {
        try {
            if (!TextUtils.isEmpty(nativelogic.nativeObject.sdk_code)) {
                if(nativelogic.nativeObject.sdk_code.equals("GDT_SDK")) {
                    refreshAd();
                }else if(nativelogic.nativeObject.sdk_code.equals("TOUTIAO_SDK")) {
                    loadListAd(nativelogic);
                }
            } else {
                Article.DataBean dateBean = new Article.DataBean();
                dateBean.nativelogic = nativelogic;
                dateBean.setFrom_name("广告");
                dateBean.setList_show_type(1);
                dateBean.setArticle_imgs(aNative.infoIcon);
                dateBean.setTitle(aNative.title +"\n" + aNative.desc);
                mAdapter.addData(1,dateBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onAdFailedToLoad(String s) {
        nativelogic = null;
    }

    @Override
    public void onADLoaded(List<NativeExpressADView> list) {
        if (list == null || list.isEmpty()) {
            return;
        }

        for (NativeExpressADView ad : list) {
            Article.DataBean dateBean = new Article.DataBean();
            dateBean.nativeExpressADView = ad;
            dateBean.setFrom_name("广告");
//            dateBean.nativelogic = nativelogic;
            dateBean.setList_show_type(3);
            mAdapter.addData(1,dateBean);
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

    class HeaderViewHolder {
        private final Unbinder bind;
        View rootView;
        @BindView(R.id.iv_bg)
        ImageView ivBg;
        @BindView(R.id.iv_header)
        ImageView ivHeader;
        @BindView(R.id.tv_header_title)
        TextView tvHeaderTitle;
        @BindView(R.id.tv_header_content)
        TextView tvHeaderContent;
        @BindView(R.id.ll_content)
        RelativeLayout llContent;

        public HeaderViewHolder() {
            rootView = getLayoutInflater().inflate(R.layout.advise_header_view,
                    (ViewGroup) rvDetail.getParent(), false);
            bind = ButterKnife.bind(this, rootView);
        }

        public void unbind() {
            if (bind != null) {
                bind.unbind();
            }
        }
    }
    /*GDTAD*/
    private void refreshAd(){
        try {
            /**
             *  如果选择支持视频的模版样式，请使用{@link Constants#NativeExpressSupportVideoPosID}
             */
            nativeExpressAD = new NativeExpressAD(getActivity(), new ADSize(ADSize.FULL_WIDTH, ADSize.AUTO_HEIGHT), "1109529834", "3050278581035653", this);
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
                        mAdapter.addData(1,dateBean);

                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public int getRvDy() {
        //获取第一个可见的item的位置
        int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = linearLayoutManager.findViewByPosition
                (firstVisibleItemPosition);
        //计算出还未移出屏幕的高度
        int itemTop = firstVisiableChildView.getTop();
        //由于每个item的高度并不相同，所以就循环相加获取总高度
        int sumHeight = 0;
        for (int i = 0; i < firstVisibleItemPosition; i++) {
            sumHeight += linearLayoutManager.findViewByPosition(firstVisibleItemPosition)
                    .getHeight();
        }
        //减去该Item还未移出屏幕的部分可得出滑动的距离
        return sumHeight - itemTop;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
