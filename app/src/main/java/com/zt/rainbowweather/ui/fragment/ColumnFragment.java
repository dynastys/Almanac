package com.zt.rainbowweather.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.androidquery.callback.ImageOptions;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.DownloadStatusController;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.bytedance.sdk.openadsdk.TTImage;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.util.AdError;
import com.xy.xylibrary.base.BaseAdapter;
import com.xy.xylibrary.base.BaseFragment;
import com.xy.xylibrary.utils.GlideUtil;
import com.xy.xylibrary.utils.SaveShare;
import com.xy.xylibrary.view.CustomScrollViewPager;
import com.zt.rainbowweather.api.RequestSyntony;
import com.zt.rainbowweather.entity.news.Article;
import com.zt.rainbowweather.entity.news.MessageEvent;
import com.zt.rainbowweather.entity.news.ViewPageEvent;
import com.zt.rainbowweather.entity.weather.ViewPageScrollTo;
import com.zt.rainbowweather.presenter.ScrollLinearLayoutManager;
import com.zt.rainbowweather.presenter.StartAd;
import com.zt.rainbowweather.ui.activity.StartActivity;
import com.zt.weather.R;
import com.zt.rainbowweather.presenter.request.NewsRequest;
import com.zt.rainbowweather.ui.activity.AdviseMoreDetailActivity;
import com.zt.xuanyin.Interface.AdProtogenesisListener;
import com.zt.xuanyin.controller.Ad;
import com.zt.xuanyin.controller.NativeAd;
import com.zt.xuanyin.entity.model.Native;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

@SuppressLint("ValidFragment")
public class ColumnFragment extends BaseFragment implements RequestSyntony<Article>, AdProtogenesisListener,NativeExpressAD.NativeExpressADListener {

    Unbinder unbinder;
    @BindView(R.id.list_recycler)
    RecyclerView listRecycler;

    private BaseAdapter baseAdapter;
    private String text,ColumnName;
    private CustomScrollViewPager viewPager;
    private View rootView;
    @SuppressLint("UseSparseArrays")
    private Map<Integer, Article.DataBean> adMap = new HashMap<>();
    private List<Article.DataBean> beans = new ArrayList<>();
    private TextView textView;
    @SuppressLint("UseSparseArrays")
    private Map<Integer,TextView> textViewList = new HashMap<>();
    private int pageindex = 1;
    private TTAdNative mTTAdNative;
    private NativeExpressAD nativeExpressAD;

    public ColumnFragment() {
    }

    public void setviewPager(CustomScrollViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_column;
    }

    @Override
    protected void initData(View view) {
        try {
            mTTAdNative = TTAdSdk.getAdManager().createAdNative(getActivity());
            //申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
            TTAdSdk.getAdManager().requestPermissionIfNecessary(getActivity());
            ScrollLinearLayoutManager setScrollEnable = new ScrollLinearLayoutManager(getActivity());
            setScrollEnable.setScrollEnable(false);
            listRecycler.setLayoutManager(setScrollEnable);
            baseAdapter = new BaseAdapter<>(R.layout.item_advise_detail_one, beans, (viewHolder, item) -> {
                try {
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
                                        GlideUtil.getGlideUtil().setImages(getActivity(), item.ttFeedAd.getIcon().getImageUrl(), viewHolder.getView(R.id.iv_title), 0);
                                    }
                                }
                            }else{
                                GlideUtil.getGlideUtil().setImages(getActivity(), item.getArticle_imgs().get(0), viewHolder.getView(R.id.iv_title), 0);
                            }
                            viewHolder.setText(R.id.tv_title, item.getTitle())
                                    .setText(R.id.tv_from, item.getFrom_name());
                            break;
                        case 2:
                            viewHolder.getView(R.id.iv_title_r_rel).setVisibility(View.VISIBLE);
                            GlideUtil.getGlideUtil().setImages(getActivity(),item.getArticle_imgs().get(0), viewHolder.getView(R.id.iv_title_r), 0);
                            viewHolder.setText(R.id.tv_title_r, item.getTitle())
                                    .setText(R.id.tv_from_r, item.getFrom_name());
                            break;
                        case 4:
                            viewHolder.getView(R.id.tv_title_x_lin).setVisibility(View.VISIBLE);
                            if(item.ttFeedAd != null){
                                ttFeedAd = item.ttFeedAd;
                                bindData(viewHolder, item.ttFeedAd,item.nativelogic);
                                if (ttFeedAd.getImageList() != null && ttFeedAd.getImageList().size() >= 3) {
                                    TTImage image1 = ttFeedAd.getImageList().get(0);
                                    TTImage image2 = ttFeedAd.getImageList().get(1);
                                    TTImage image3 = ttFeedAd.getImageList().get(2);
                                    if (image1 != null && image1.isValid()) {
                                        GlideUtil.getGlideUtil().setImages(getActivity(),image1.getImageUrl(), viewHolder.getView(R.id.iv_image_x1), 0);
                                    }
                                    if (image2 != null && image2.isValid()) {
                                        GlideUtil.getGlideUtil().setImages(getActivity(), image2.getImageUrl(), viewHolder.getView(R.id.iv_image_x2), 0);
                                    }
                                    if (image3 != null && image3.isValid()) {
                                        GlideUtil.getGlideUtil().setImages(getActivity(), image3.getImageUrl(), viewHolder.getView(R.id.iv_image_x3), 0);
                                    }
                                }
                            }else{
                                GlideUtil.getGlideUtil().setImages(getActivity(),item.getArticle_imgs().get(0), viewHolder.getView(R.id.iv_image_x1), 0);
                                GlideUtil.getGlideUtil().setImages(getActivity(), item.getArticle_imgs().get(1), viewHolder.getView(R.id.iv_image_x2), 0);
                                GlideUtil.getGlideUtil().setImages(getActivity(), item.getArticle_imgs().get(2), viewHolder.getView(R.id.iv_image_x3), 0);

                            }
                            viewHolder.setText(R.id.tv_title_x, item.getTitle())
                                    .setText(R.id.tv_from_x, item.getFrom_name());
                            break;
                        case 3:
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
                                            GlideUtil.getGlideUtil().setImages(getActivity(),image.getImageUrl(), viewHolder.getView(R.id.iv_image_d), 0);
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
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Log.e("toutiao", "initData: "+item.getFrom_name() );
                            }else{
                                try {
                                    if(item.nativeExpressADView != null){
                                        frameLayout.setVisibility(View.VISIBLE);
                                        viewHolder.getView(R.id.iv_image_d).setVisibility(View.GONE);
                                        viewHolder.getView(R.id.tv_title_d).setVisibility(View.GONE);
                                        viewHolder.getView(R.id.tv_from_d_lin).setVisibility(View.GONE);
                                        viewHolder.setText(R.id.tv_from_d, item.getFrom_name());
                                        if (frameLayout.getChildCount() > 0) {
                                            frameLayout.removeAllViews();
                                        }
            //                            if (item.nativeExpressADView.getBoundData().getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
            //                                item.nativeExpressADView.setMediaListener(mediaListener);
            //                            }
                                        try {
                                            // 广告可见才会产生曝光，否则将无法产生收益。
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
                                        GlideUtil.getGlideUtil().setImages(getActivity(),item.getArticle_imgs().get(0), viewHolder.getView(R.id.iv_image_d), 0);
                                        viewHolder.setText(R.id.tv_title_d, item.getTitle())
                                                .setText(R.id.tv_from_d, item.getFrom_name());
                                        Log.e("toutiao", "initData: "+item.getFrom_name() );
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (item.nativelogic != null) {
                        item.nativelogic.AdShow(null);
                        textViewList.put(viewHolder.getAdapterPosition(),viewHolder.getView(R.id.tv_title));
                    }
                    if (beans.size() == viewHolder.getAdapterPosition() + 1) {
                        textView = viewHolder.getView(R.id.tv_title);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            baseAdapter.setOnItemClickListener((adapter, view1, position) -> {
                try {
                    if (beans.get(position).getFrom_name().equals("广告") && beans.get(position).nativelogic != null) {
                        beans.get(position).nativelogic.OnClick(null);
                    } else {
                        AdviseMoreDetailActivity.startActivity(getActivity(), beans.get(position).getTitle(), beans.get(position).getHtml_url());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            // 滑动最后一个Item的时候回调onLoadMoreRequested方法
            baseAdapter.setOnLoadMoreListener(() -> {

            }, listRecycler);
            // 滑动最后一个Item的时候回调onLoadMoreRequested方法
            listRecycler.setItemAnimator(new DefaultItemAnimator());
            listRecycler.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getActivity()), 1));
            listRecycler.setAdapter(baseAdapter);
            listRecycler.setHasFixedSize(true);
            listRecycler.setNestedScrollingEnabled(false);
            if (SaveShare.getValue(getActivity(), "Channelid").equals(text)) {
                SaveShare.saveValue(getActivity(), "Channelid","");
                beans.clear();
                if (viewPager != null) {
                    viewPager.setObjectForPosition(rootView, 0);
                }
                RequestData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void RequestData(){
        NewsRequest.getNewsRequest().getNewsListData(getActivity(),text,pageindex,10,ColumnFragment.this);
    }

    private boolean isVisible(View v) {
        return v != null && v.getLocalVisibleRect(new Rect());
    }

    @Override
    protected void initListener() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && listRecycler != null && !SaveShare.getValue(getActivity(), "Channelid").equals(text)) {
            try {
                showLoadingDialog("");
                EventBus.getDefault().post(new ViewPageScrollTo(0));
                beans.clear();
                adMap.clear();
                RequestData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Bundle args = getArguments();
//          获取栏目号（有时候获取不到，需要第二次获取）
            text = args != null ? args.getString("text") : "1";
            ColumnName = args != null ? args.getString("ColumnName") : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        if (viewPager != null) {
            viewPager.setObjectForPosition(rootView, 0);
        }
        return rootView;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Message(MessageEvent messageEvent) {
        try {
            EventBus.getDefault().post(new ViewPageEvent(0));
            String name = SaveShare.getValue(getActivity(),"ColumnName");
            if(textView != null && isVisible(textView) && ColumnName.equals(name)){
                textView = null;
                if(adMap.size() == 1){
                    if(adMap.get(4).nativelogic != null){
                        adMap.get(4).nativelogic.AdShow(null);
                    }
                }
                RequestData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        try {
            EventBus.getDefault().unregister(this);
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
            if(beans.size() == 0){
                pageindex = 1;
                baseAdapter.setNewData(article.getData());
            }else{
                baseAdapter.addData(article.getData());
                pageindex++;
            }
            beans.addAll(article.getData());
            dismissLoadingDialog();
            if (viewPager != null) {
                viewPager.setObjectForPosition(rootView, 0);
            }
            EventBus.getDefault().post(new ViewPageEvent(0));
            String ISAD = SaveShare.getValue(getActivity(), "ISAD");
            if (!TextUtils.isEmpty(ISAD) && ISAD.equals("1")) {
                //            // 获得信息流广告对象
                Ad.getAd().NativeAD(getActivity(), "98f8e423-02e0-49f5-989f-af46f5c59203", "80b46cdb-e90f-47fd-aeca-b0b7c00272ff", "67C53558D3E3485EA681EA21735A5003", this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onADReady(Native aNative,NativeAd nativelogic) {
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
                dateBean.setTitle(aNative.title + "\n" + aNative.desc);
                if(adMap.size() > 0){
                    baseAdapter.addData( beans.size() - 11, dateBean);
                    beans.add(beans.size() - 11, dateBean);
                    adMap.put(beans.size() - 10, dateBean);
                }else{
                    baseAdapter.addData( 4, dateBean);
                    beans.add(4, dateBean);
                    adMap.put(4, dateBean);
                }
                if (viewPager != null) {
                    viewPager.setObjectForPosition(rootView, 0);
                }
                EventBus.getDefault().post(new ViewPageEvent(0));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onAdFailedToLoad(String s) {
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
                            baseAdapter.addData( beans.size() - 11, dateBean);
                            beans.add(beans.size() - 11, dateBean);
                            adMap.put(beans.size() - 10, dateBean);
                        }else{
                            baseAdapter.addData( 4, dateBean);
                            beans.add(4, dateBean);
                            adMap.put(4, dateBean);
                        }
                        if (viewPager != null) {
                            viewPager.setObjectForPosition(rootView, 0);
                        }
                        EventBus.getDefault().post(new ViewPageEvent(0));
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindData(final BaseViewHolder adViewHolder, TTFeedAd ad,NativeAd nativelogic) {
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

    @Override
    public void onADLoaded(List<NativeExpressADView> list) {
        try {
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
                    baseAdapter.addData( beans.size() - 11, dateBean);
                    beans.add(beans.size() - 11, dateBean);
                    adMap.put(beans.size() - 10, dateBean);
                }else{
                    baseAdapter.addData( 4, dateBean);
                    beans.add(4, dateBean);
                    adMap.put(4, dateBean);
                }
                if (viewPager != null) {
                    viewPager.setObjectForPosition(rootView, 0);
                }
                EventBus.getDefault().post(new ViewPageEvent(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
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
}
