package com.zt.rainbowweather.utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.callback.ImageOptions;
import com.bumptech.glide.Glide;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.bytedance.sdk.openadsdk.TTImage;
import com.bytedance.sdk.openadsdk.TTNativeAd;

import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.qq.e.ads.banner2.UnifiedBannerADListener;
import com.qq.e.ads.banner2.UnifiedBannerView;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.comm.util.AdError;
import com.xy.xylibrary.base.AppContext;
import com.xy.xylibrary.ui.activity.login.UserMessage;
import com.xy.xylibrary.ui.fragment.task.TaskLogic;
import com.xy.xylibrary.ui.fragment.task.TaskType;
import com.xy.xylibrary.utils.GlideUtil;
import com.xy.xylibrary.utils.SaveShare;
import com.zt.rainbowweather.presenter.PangolinBannerAd;
import com.zt.rainbowweather.ui.activity.MainActivity;
import com.zt.xuanyin.Interface.AdProtogenesisListener;
import com.zt.xuanyin.controller.Ad;
import com.zt.xuanyin.controller.NativeAd;
import com.zt.xuanyin.entity.model.Native;
import com.zt.weather.R;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class AdDialog extends Dialog implements NativeExpressAD.NativeExpressADListener{

    private Activity context;
    private String title;
    private ClickListenerInterface clickListenerInterface;
    private int type;
    private TTAdNative mTTAdNative;
    private NativeExpressAD nativeExpressAD;
    private LinearLayout tv_title_d_lin;
    private TextView tv_title_d;
    private TextView tv_from_ad;
    private ImageView iv_image_ad;
    private FrameLayout iv_listitem_video_ad;
    private LinearLayout tv_from_ad_lin;
    private LinearLayout adLin;
    private FrameLayout bannerContainer;
    private RelativeLayout GDTAd;
    private ImageView adImageBanner;
    private  ImageView finish_task_dialog_cancel;
    private TTRewardVideoAd mttRewardVideoAd;


    public interface ClickListenerInterface {
        public void doConfirm();
        public void doCancel();
    }

    public AdDialog(Activity context, String title,int type) {
        super(context,  R.style.LoadingDialogTheme);
        this.context = context;
        this.title = title;
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.finish_task_dialog, null);
        setContentView(view);
        TextView tvTitle = view.findViewById(R.id.finish_task_title_tv);
        TextView tvConfirm = view.findViewById(R.id.finish_task_tv);
        Button hint_content = view.findViewById(R.id.finish_task_see_btn);
        ImageView index_details_img = view.findViewById(R.id.finish_task_img);
        finish_task_dialog_cancel = view.findViewById(R.id.finish_task_dialog_cancel);
        adLin = view.findViewById(R.id.ad_lin);
        bannerContainer = view.findViewById(R.id.banner_container);
        GDTAd = view.findViewById(R.id.GDT_ad);
        adImageBanner = view.findViewById(R.id.ad_image_banner);
        tv_title_d_lin = view.findViewById(R.id.tv_title_ad_lin);
        tv_title_d = view.findViewById(R.id.tv_title_ad);
        tv_from_ad = view.findViewById(R.id.tv_from_ad);
        iv_image_ad = view.findViewById(R.id.iv_image_ad);
        iv_listitem_video_ad = view.findViewById(R.id.iv_listitem_video_ad);
        tv_from_ad_lin = view.findViewById(R.id.tv_from_ad_lin);
        //申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdSdk.getAdManager().requestPermissionIfNecessary(context);
//        BannerAd(adLin,bannerContainer,GDTAd,adImageBanner);
        VideoAd();

        new CountDownTimer(5 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                TaskType taskType3 = LitePal.where("tasktype = ?", "11").findFirst(TaskType.class);
                TaskLogic.getTaskLogic().FinishTask2(context,"",taskType3.taskId,false);
                finish_task_dialog_cancel.setVisibility(View.VISIBLE);
            }
        }.start();
//        hint_content.setText(notes);
        if(!TextUtils.isEmpty(title)){
            tvTitle.setText(title);
        }else{
            tvTitle.setVisibility(View.GONE);
        }
//        if(image != 0){
//            index_details_img.setImageResource(image);
//        }
//        if(context != null && notes != 0){
//            String str = "夏季制冷时，相对湿度以<font color='#56CCF6'>40%—80%</font>为宜。";
//            String str2 = "指数值3到4，一般为多云天气，此时紫外线强度较弱，预报等级为<font color='#56CCF6'>二级</font>；";
//            switch (type){
//                case 0:
//                    tvConfirm.setText(Html.fromHtml(str));
//                    break;
//                case 1:
//                    tvConfirm.setText(Html.fromHtml(str2));
//                    break;
//                case 2:
//                    tvConfirm.setText(context.getResources().getText(notes));
//                    break;
//            }
//        }else{
//            tvConfirm.setText("加载错误");
//        }
        hint_content.setOnClickListener(new clickListener());
        finish_task_dialog_cancel.setOnClickListener(new clickListener());
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
        dialogWindow.setWindowAnimations(R.style.animate_dialog);
        setCanceledOnTouchOutside(false);
//        setCanceleable(false);调用这个方法时，按对话框以外的地方不起作用。按返回键也不起作用
    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int id = v.getId();
            if (id == R.id.login_see_btn) {
                if(clickListenerInterface != null){
                    clickListenerInterface.doCancel();
                }
            } else if (id == R.id.finish_task_dialog_cancel){
                if(clickListenerInterface != null){
                    clickListenerInterface.doCancel();
                }
            }
        }
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
            Log.e("AD", "loadListAd: "  );
            mTTAdNative = TTAdSdk.getAdManager().createAdNative(context);
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
                    Log.e("AD", "onError: "  );
                }

                @Override
                public void onFeedAdLoad(List<TTFeedAd> ads) {
                    Log.e("AD", "onFeedAdLoad: "  );
                    if (ads == null || ads.isEmpty()) {
                        return;
                    }
                     for (TTFeedAd ad : ads) {
//                        Article.DataBean dateBean = new Article.DataBean();
//                        dateBean.ttFeedAd = ad;
//                        dateBean.nativelogic = nativelogic;
//                        dateBean.setFrom_name(TextUtils.isEmpty(ad.getSource()) ? "广告" : ad.getSource());
                         finish_task_dialog_cancel.setVisibility(View.GONE);
                        switch (ad.getImageMode()){
                            case 3://大图
                                bindData(tv_title_d_lin, ad,nativelogic);
                                tv_title_d.setText(ad.getTitle() + "\n" + ad.getDescription());
                                tv_from_ad.setText(TextUtils.isEmpty(ad.getSource()) ? "广告" : ad.getSource());
                                TTImage image = ad.getImageList().get(0);
                                if (image != null && image.isValid()) {
                                    GlideUtil.getGlideUtil().setImages(context,image.getImageUrl(), iv_image_ad, 0);
                                }
                                iv_listitem_video_ad.setVisibility(View.GONE);
                                if (nativelogic != null) {
                                    nativelogic.AdShow(null);
                                }
                                tv_title_d_lin.setOnClickListener(v -> nativelogic.OnClick(null));
                                break;
                            case 5://视频
                                View video = ad.getAdView();
                                if (video != null) {
                                    ad.setVideoAdListener(new TTFeedAd.VideoAdListener() {
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
                                        iv_listitem_video_ad.removeAllViews();
                                        iv_listitem_video_ad.addView(video);
                                        iv_listitem_video_ad.setVisibility(View.VISIBLE);
                                        iv_image_ad.setVisibility(View.GONE);
                                    }
                                }
                                tv_title_d.setText(ad.getTitle() + "\n" + ad.getDescription());
                                tv_from_ad.setText(TextUtils.isEmpty(ad.getSource()) ? "广告" : ad.getSource());
//                                if(aNative.infoIcon.size() > 0){
//                                    GlideUtil.getGlideUtil().setImages(context,aNative.infoIcon.get(0), iv_image_ad, 0);
//                                }
//                                iv_listitem_video_ad.setVisibility(View.GONE);
//                                if (nativelogic != null) {
//                                    nativelogic.AdShow(null);
//                                }
//                                tv_title_d_lin.setOnClickListener(v -> nativelogic.OnClick(null));
                                break;
                        }

                     }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindData(final LinearLayout adViewHolder, TTFeedAd ad, NativeAd nativelogic) {
        try {
            //可以被点击的view, 也可以把convertView放进来意味item可被点击
            List<View> clickViewList = new ArrayList<>();
            clickViewList.add(adViewHolder);
            //触发创意广告的view（点击下载或拨打电话）
            List<View> creativeViewList = new ArrayList<>();
//        creativeViewList.add(adViewHolder.mCreativeButton);
            //如果需要点击图文区域也能进行下载或者拨打电话动作，请将图文区域的view传入
//            creativeViewList.add(convertView);
            //重要! 这个涉及到广告计费，必须正确调用。convertView必须使用ViewGroup。
            ad.registerViewForInteraction((ViewGroup) adViewHolder, clickViewList, creativeViewList, new TTNativeAd.AdInteractionListener() {
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

    }
    private void VideoAd(){
        tv_title_d_lin.setVisibility(View.VISIBLE);
        nativelogic = Ad.getAd().NativeAD(context, "98f8e423-02e0-49f5-989f-af46f5c59203", "80b46cdb-e90f-47fd-aeca-b0b7c00272ff", "67C53558D3E3485EA681EA21735A5003", new AdProtogenesisListener() {
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
 //                        Article.DataBean dateBean = new Article.DataBean();
//                        dateBean.nativelogic = nativelogic;
//                        dateBean.setFrom_name("广告");
//                        dateBean.setList_show_type(1);
//                        dateBean.setArticle_imgs(aNative.infoIcon);
//                        dateBean.setTitle(aNative.title + "\n" + aNative.desc);
                        tv_title_d.setText(aNative.title + "\n" + aNative.desc);
                        if(aNative.infoIcon.size() > 0){
                            GlideUtil.getGlideUtil().setImages(context,aNative.infoIcon.get(0), iv_image_ad, 0);
                        }
                        iv_listitem_video_ad.setVisibility(View.GONE);
                        if (nativeAd != null) {
                            nativeAd.AdShow(null);
                        }
                        tv_title_d_lin.setOnClickListener(v -> nativeAd.OnClick(null));
                    }

                } catch (Exception e) {
                    Log.e("AD", "Exception: sssssssaaaa"+e.getMessage()  );
                    e.printStackTrace();
                }
            }

            @Override
            public void onAdFailedToLoad(String s) {

            }
        });

    }
    private NativeAd nativelogic;
    private void BannerAd(LinearLayout adLin,FrameLayout bannerContainer, RelativeLayout GDTAd,ImageView adImageBanner) {//
        // 获得原生banner广告对象
        nativelogic = Ad.getAd().NativeAD(context, "98f8e423-02e0-49f5-989f-af46f5c59203", "3ab86803-f1bf-4f18-86d8-1ca3f7d7962e", "67C53558D3E3485EA681EA21735A5003", new AdProtogenesisListener() {
            @Override
            public void onADReady(Native url, NativeAd nativelogic) {
                try {
                    if (TextUtils.isEmpty(nativelogic.nativeObject.sdk_code)) {
                        Glide.with(Objects.requireNonNull(context)).load(url.src).into(adImageBanner);
                        adLin.setVisibility(View.VISIBLE);
                        bannerContainer.setVisibility(View.GONE);
                        GDTAd.setVisibility(View.GONE);
                    } else if (nativelogic.nativeObject.sdk_code.equals("GDT_SDK")) {
                        bannerContainer.setVisibility(View.GONE);
                        adLin.setVisibility(View.GONE);
                        GDTBanner(context,GDTAd);

                    } else if (nativelogic.nativeObject.sdk_code.equals("TOUTIAO_SDK")) {
                        adLin.setVisibility(View.GONE);
                        GDTAd.setVisibility(View.GONE);
                        adLin.setVisibility(View.VISIBLE);
                        PangolinBannerAd.getPangolinBannerAd().BannerAD(context, bannerContainer, nativelogic);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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

    private UnifiedBannerView GDTBanner(Activity activity,RelativeLayout relatAd) {
        // 创建 Banner 广告 AdView 对象
        // appId : 在 http://e.qq.com/dev/ 能看到的 app 唯一字符串
        // posId : 在 http://e.qq.com/dev/ 生成的数字串，并非 appid 或者 appkey
        UnifiedBannerView banner = new UnifiedBannerView(activity, nativelogic.nativeObject.appid, nativelogic.nativeObject.posid, new UnifiedBannerADListener() {
            @Override
            public void onNoAD(AdError adError) {
                Log.i("AD_DEMO", "BannerNoAD，eCode=" + adError.getErrorCode());
                try {
                    relatAd.setVisibility(View.GONE);
                    if (nativelogic != null) {
                        nativelogic.OnRequest(adError.getErrorCode() + "", adError.getErrorMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onADReceive() {
                try {
                    relatAd.setVisibility(View.VISIBLE);
                    if (nativelogic != null) {
                        nativelogic.OnRequest("0", "msg");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("AD_DEMO", "onADReceive");
            }

            @Override
            public void onADExposure() {
                Log.i("AD_DEMO", "onADExposure");
                try {
                     if (nativelogic != null) {
                        nativelogic.AdShow(relatAd);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onADClosed() {
                Log.i("AD_DEMO", "onADClosed");

            }

            @Override
            public void onADClicked() {
                Log.i("AD_DEMO", "onADClicked");
                try {
                    if (nativelogic != null) {
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
        banner.setRefresh(0);
        relatAd.addView(banner);
        /* 发起广告请求，收到广告数据后会展示数据     */
        banner.loadAD();

        return banner;
    }

    @Override
    public void onADLoaded(List<NativeExpressADView> list) {
        try {
            if (list == null || list.isEmpty()) {
                return;
            }
            for (NativeExpressADView ad : list) {
//                Article.DataBean dateBean = new Article.DataBean();
//                dateBean.nativeExpressADView = ad;
//                dateBean.setFrom_name("广告");
//                //            dateBean.nativelogic = nativelogic;
//                dateBean.setList_show_type(3);
                iv_listitem_video_ad.setVisibility(View.VISIBLE);
                iv_image_ad.setVisibility(View.GONE);
                tv_title_d.setVisibility(View.GONE);
                tv_from_ad_lin.setVisibility(View.GONE);

                 if (iv_listitem_video_ad.getChildCount() > 0) {
                    iv_listitem_video_ad.removeAllViews();
                }
                //                            if (item.nativeExpressADView.getBoundData().getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
                //                                item.nativeExpressADView.setMediaListener(mediaListener);
                //                            }
                try {
                    // 广告可见才会产生曝光，否则将无法产生收益。
                    iv_listitem_video_ad.addView(ad);
                } catch (Exception e) {
                    if (iv_listitem_video_ad.getChildCount() > 0) {
                        iv_listitem_video_ad.removeAllViews();
                    }
                    iv_listitem_video_ad.addView(ad);
                    e.printStackTrace();
                }
                ad.render();
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


    /**
     * 激励视频广告
     */
    public void loadVideoAd(String codeId, int orientation) {
        try {
            //step1:初始化sdk
            TTAdManager ttAdManager = TTAdSdk.getAdManager();
            //step2:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
            TTAdSdk.getAdManager().requestPermissionIfNecessary(context);
            //step3:创建TTAdNative对象,用于调用广告请求接口
            TTAdNative mTTAdNative = ttAdManager.createAdNative(context);
            //step4:创建广告请求参数AdSlot,具体参数含义参考文档
            AdSlot adSlot = new AdSlot.Builder()
                    .setCodeId(codeId)
                    .setSupportDeepLink(true)
                    .setImageAcceptedSize(1080, 1920)
                    .setRewardName("看视频翻倍") //奖励的名称
                    .setRewardAmount(100)  //奖励的数量
                    .setUserID(SaveShare.getValue(context, "Phone"))//用户id,必传参数
                    .setOrientation(orientation) //必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                    .build();
            //step5:请求广告
            mTTAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
                @Override
                public void onError(int code, String message) {
                    Log.e("VideoAd", "onError: ");
                }

                //视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
                @Override
                public void onRewardVideoCached() {
                    Log.e("VideoAd", "onRewardVideoCached: ");
                }

                //视频广告的素材加载完毕，比如视频url等，在此回调后，可以播放在线视频，网络不好可能出现加载缓冲，影响体验。
                @Override
                public void onRewardVideoAdLoad(TTRewardVideoAd ad) {
                    Log.e("VideoAd", "onRewardVideoAdLoad: ");
                    mttRewardVideoAd = ad;
                    //                mttRewardVideoAd.setShowDownLoadBar(false);
                    mttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {

                        @Override
                        public void onAdShow() {
                            Log.e("VideoAd", "onAdShow: ");
                        }

                        @Override
                        public void onAdVideoBarClick() {
                            Log.e("VideoAd", "onAdVideoBarClick: ");
                        }

                        public void onSkippedVideo() {
                        }

                        @Override
                        public void onAdClose() {
                            try {
//                                if (!TextUtils.isEmpty(taskID)) {
//                                    FinishTask(context, "", taskID, ISVideo);
//                                } else {
//                                    requestSuccessData(null, new AppContext.UserGold() {
//                                        @Override
//                                        public void gold(UserMessage userMessage) {
//                                            SignInDialog(context, taskFragment);
//                                            if (userGold != null) {
//                                                AppContext.getUserInfo(context, "", SaveShare.getValue(context, "userId"), userGold);
//                                            }
//                                        }
//                                    });
//                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            loadVideoAd("923044756", TTAdConstant.VERTICAL);
                        }

                        //视频播放完成回调
                        @Override
                        public void onVideoComplete() {
                        }

                        @Override
                        public void onVideoError() {
                            Log.e("VideoAd", "onVideoError: ");
                        }

                        //视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励梳理，rewardName：奖励名称
                        @Override
                        public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {

                        }
                    });
                    mttRewardVideoAd.setDownloadListener(new TTAppDownloadListener() {
                        @Override
                        public void onIdle() {

                        }

                        @Override
                        public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {

                        }

                        @Override
                        public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                        }

                        @Override
                        public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                        }

                        @Override
                        public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                        }

                        @Override
                        public void onInstalled(String fileName, String appName) {
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
