package com.zt.rainbowweather.ui.adapter;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.androidquery.callback.ImageOptions;
import com.bumptech.glide.Glide;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.bytedance.sdk.openadsdk.TTImage;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.zt.weather.R;
import com.xy.xylibrary.utils.GlideUtil;
import com.zt.rainbowweather.entity.advise.AdviseDetailBean;
import com.zt.rainbowweather.entity.news.Article;
import com.zt.xuanyin.controller.NativeAd;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hcg on 2018/5/5.
 */

public class AdviseDetailAdapter extends BaseMultiItemQuickAdapter<Article.DataBean, BaseViewHolder> {
    public static final int TYPE_ONE = 1000010;
    public static final int TYPE_THREE = 1000011;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public AdviseDetailAdapter( List<Article.DataBean>  data) {
        super(data);
        addItemType(TYPE_ONE, R.layout.item_advise_detail_one);
//        addItemType(TYPE_THREE, R.layout.item_advise_detail_three);
    }

    @Override
    protected void convert(BaseViewHolder helper, Article.DataBean item) {
      bindOneData(helper, item);

    }


    private void bindOneData(BaseViewHolder viewHolder, Article.DataBean item) {
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
                            if (image != null && image.isValid()){
                                GlideUtil.getGlideUtil().setImages(mContext, item.ttFeedAd.getIcon().getImageUrl(), viewHolder.getView(R.id.iv_title), 0);
                            }
                        }
                    }else{
                        GlideUtil.getGlideUtil().setImages(mContext, item.getArticle_imgs().get(0), viewHolder.getView(R.id.iv_title), 0);
                    }
                    viewHolder.setText(R.id.tv_title, item.getTitle())
                            .setText(R.id.tv_from, item.getFrom_name());
                    break;
                case 2:
                    viewHolder.getView(R.id.iv_title_r_rel).setVisibility(View.VISIBLE);
                    GlideUtil.getGlideUtil().setImages(mContext,item.getArticle_imgs().get(0), viewHolder.getView(R.id.iv_title_r), 0);
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
                                    GlideUtil.getGlideUtil().setImages(mContext,image1.getImageUrl(), viewHolder.getView(R.id.iv_image_x1), 0);
                                }
                                if (image2 != null && image2.isValid()) {
                                    GlideUtil.getGlideUtil().setImages(mContext, image2.getImageUrl(), viewHolder.getView(R.id.iv_image_x2), 0);
                                }
                                if (image3 != null && image3.isValid()) {
                                    GlideUtil.getGlideUtil().setImages(mContext, image3.getImageUrl(), viewHolder.getView(R.id.iv_image_x3), 0);
                                }
                            }
                        }else{
                            GlideUtil.getGlideUtil().setImages(mContext,item.getArticle_imgs().get(0), viewHolder.getView(R.id.iv_image_x1), 0);
                            GlideUtil.getGlideUtil().setImages(mContext, item.getArticle_imgs().get(1), viewHolder.getView(R.id.iv_image_x2), 0);
                            GlideUtil.getGlideUtil().setImages(mContext, item.getArticle_imgs().get(2), viewHolder.getView(R.id.iv_image_x3), 0);

                        }
                        viewHolder.setText(R.id.tv_title_x, item.getTitle())
                                .setText(R.id.tv_from_x, item.getFrom_name());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
                                    GlideUtil.getGlideUtil().setImages(mContext,image.getImageUrl(), viewHolder.getView(R.id.iv_image_d), 0);
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
                                GlideUtil.getGlideUtil().setImages(mContext,item.getArticle_imgs().get(0), viewHolder.getView(R.id.iv_image_d), 0);
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
            if (item.nativelogic != null) {
                item.nativelogic.AdShow(null);
            }
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

