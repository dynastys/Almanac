package com.zt.rainbowweather.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.bumptech.glide.Glide;
import com.check.ox.sdk.LionListener;
import com.check.ox.sdk.LionStreamerView;
import com.kyleduo.switchbutton.SwitchButton;
import com.qq.e.ads.banner2.UnifiedBannerADListener;
import com.qq.e.ads.banner2.UnifiedBannerView;
import com.qq.e.comm.util.AdError;
import com.umeng.analytics.MobclickAgent;
import com.xy.xylibrary.base.BaseFragment;
import com.xy.xylibrary.utils.SaveShare;
import com.xy.xylibrary.utils.Utils;
import com.xy.xylibrary.view.CustomScrollViewPager;
import com.zt.rainbowweather.api.RequestSyntony;
import com.zt.rainbowweather.entity.Icons;
import com.zt.rainbowweather.entity.background.IsUserLight;
import com.zt.rainbowweather.presenter.DataCleanManager;
import com.zt.rainbowweather.presenter.GridItemDecoration;
import com.zt.rainbowweather.presenter.PangolinBannerAd;
import com.zt.rainbowweather.presenter.home.WeatherPageData;
import com.zt.rainbowweather.presenter.personal.ServerManager;
import com.zt.rainbowweather.presenter.request.AlmanacRequest;
import com.zt.rainbowweather.ui.activity.AboutActivity;
import com.zt.rainbowweather.ui.activity.AdviseMoreDetailActivity;
import com.zt.rainbowweather.ui.activity.AtlasActivity;
import com.zt.rainbowweather.ui.activity.LCIMConversationActivity;
import com.zt.rainbowweather.ui.service.CancelNoticeService;
import com.zt.rainbowweather.utils.ConstUtils;
import com.zt.rainbowweather.utils.ToastUtils;
import com.zt.rainbowweather.view.MineRowView;
import com.zt.weather.R;
import com.zt.xuanyin.Interface.AdProtogenesisListener;
import com.zt.xuanyin.controller.Ad;
import com.zt.xuanyin.controller.NativeAd;
import com.zt.xuanyin.entity.model.Native;

import org.greenrobot.eventbus.EventBus;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.utils.LCIMConstants;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author zw
 * @time 2019-3-8
 * 外接服务
 */
public class ServeFragment extends BaseFragment implements RequestSyntony<Icons>, LionListener {

    @BindView(R.id.finish_file_head)
    ImageView finishFileHead;
    @BindView(R.id.file_head_title)
    TextView fileHeadTitle;
    @BindView(R.id.file_co_option)
    TextView fileCoOption;
    @BindView(R.id.list_recycler)
    RecyclerView ListRecycler;
    Unbinder unbinder;
    @BindView(R.id.recycler_service)
    RecyclerView recyclerService;
    @BindView(R.id.list_bar)
    TextView listBar;
    @BindView(R.id.TMBrView)
    LionStreamerView TMBrView;
    @BindView(R.id.serve_txt)
    TextView serveTxt;
    @BindView(R.id.img_avatar)
    CircleImageView imgAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_fans)
    TextView tvFans;
    @BindView(R.id.tablayout_service_vp)
    TabLayout tablayoutServiceVp;
    @BindView(R.id.viewpager_service)
    CustomScrollViewPager viewpagerService;
    @BindView(R.id.sw_night_mode)
    SwitchButton swNightMode;
    @BindView(R.id.open_notification_bar)
    MineRowView openNotificationBar;
    @BindView(R.id.skin_is_set)
    MineRowView skinIsSet;
    @BindView(R.id.clear_cache)
    MineRowView clearCache;
    @BindView(R.id.message_feedback)
    MineRowView messageFeedback;
    @BindView(R.id.about_us)
    MineRowView aboutUs;
    @BindView(R.id.cache_tv)
    TextView cacheTv;
    @BindView(R.id.service_bar)
    TextView serviceBar;
    @BindView(R.id.arrow)
    ImageView arrow;
    @BindView(R.id.serve_txt_lin)
    LinearLayout serveTxtLin;
    @BindView(R.id.tablayout_service_lin)
    LinearLayout tablayoutServiceLin;
    @BindView(R.id.widget)
    MineRowView widget;
    @BindView(R.id.banner_container)
    FrameLayout bannerContainer;
    @BindView(R.id.GDT_ad)
    RelativeLayout GDTAd;
    @BindView(R.id.ad_image_banner)
    ImageView adImageBanner;
    @BindView(R.id.ad_image_banner_clear)
    ImageView adImageBannerClear;
    @BindView(R.id.tv_ad_flag)
    TextView tvAdFlag;
    @BindView(R.id.ad_lin)
    LinearLayout adLin;

    private ServerManager serverManager;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_service;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData(View view) {
        try {
            ViewGroup.LayoutParams layoutParams = serviceBar.getLayoutParams();
            layoutParams.height = Utils.getStatusBarHeight(getActivity());
            serviceBar.setLayoutParams(layoutParams);
            fileHeadTitle.setText("服务");
            String ID = SaveShare.getValue(getActivity(), "ID");
            if (TextUtils.isEmpty(ID)) {
                tvFans.setText(System.currentTimeMillis() + "");
                SaveShare.saveValue(getActivity(), "ID", System.currentTimeMillis() + "");
            } else {
                tvFans.setText(ID);
            }
            if (!ConstUtils.personal_center_icon1) {
                serveTxtLin.setVisibility(View.GONE);
            }
            if (!ConstUtils.personal_center_icon2) {
                tablayoutServiceLin.setVisibility(View.GONE);
            }
            BannerAd();
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
        try {
            if (isVisibleToUser) {
                MobclickAgent.onEvent(getActivity(), "my");
            }
            if (cacheTv != null) {
                String cacheSize = DataCleanManager.getTotalCacheSize(Objects.requireNonNull(getActivity()));
                cacheTv.setText(cacheSize);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isVisibleToUser && recyclerService != null) {
            EventBus.getDefault().post(new IsUserLight(false));
            if (serverManager == null) {
                serverManager = ServerManager.getServerManager();
                if (ConstUtils.personal_center_icon1) {
                    recyclerService.setLayoutManager(new GridLayoutManager(getActivity(), 4));
                    ListRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 4));
                    GridItemDecoration divider = new GridItemDecoration.Builder(getActivity())
                            .setHorizontalSpan(R.dimen.dp_1)
                            .setVerticalSpan(R.dimen.dp_1)
                            .setColorResource(R.color.nb_divider_narrow)
                            .setShowLastLine(true)
                            .build();
                    ListRecycler.addItemDecoration(divider);
                    recyclerService.addItemDecoration(divider);
                    AlmanacRequest.getAlmanacRequest().getGainIconData(getActivity(), 3, "", ServeFragment.this);
                }
                swNightMode.setChecked(true);
                String ISAD = SaveShare.getValue(getActivity(), "ISAD");
//                if (!TextUtils.isEmpty(ISAD) && ISAD.equals("1")) {
//                    //推啊
//                    TMBrView.setAdListener(ServeFragment.this);
//                    TMBrView.loadAd(273299);
//                }
                if (ConstUtils.personal_center_icon2) {
                    serverManager.FragmentsData((AppCompatActivity) getActivity(), tablayoutServiceVp, viewpagerService);
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
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
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ServiceFragment"); //统计页面("MainScreen"为页面名称，可自定义)
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ServiceFragment");
    }

    private NativeAd nativelogic;
    private boolean aBoolean = true;

    public void BannerShow(boolean b) {
        if (nativelogic != null && b && aBoolean) {
            aBoolean = false;
            nativelogic.AdShow(null);
        }
    }

    public void BannerAd() {//
        // 获得原生banner广告对象
        nativelogic = Ad.getAd().NativeAD(getActivity(), "98f8e423-02e0-49f5-989f-af46f5c59203", "3ab86803-f1bf-4f18-86d8-1ca3f7d7962e", "67C53558D3E3485EA681EA21735A5003", new AdProtogenesisListener() {
            @Override
            public void onADReady(Native url, NativeAd nativelogic) {
                try {
                    if (TextUtils.isEmpty(nativelogic.nativeObject.sdk_code)) {
                        Glide.with(Objects.requireNonNull(getActivity())).load(url.src).into(adImageBanner);
                        adLin.setVisibility(View.VISIBLE);
                        bannerContainer.setVisibility(View.GONE);
                        GDTAd.setVisibility(View.GONE);
                    } else if (nativelogic.nativeObject.sdk_code.equals("GDT_SDK")) {
                        GDTBanner(GDTAd);
                        bannerContainer.setVisibility(View.GONE);
                        adLin.setVisibility(View.GONE);
                    } else if (nativelogic.nativeObject.sdk_code.equals("TOUTIAO_SDK")) {
                        adLin.setVisibility(View.GONE);
                        GDTAd.setVisibility(View.GONE);
                        PangolinBannerAd.getPangolinBannerAd().BannerAD(getActivity(), bannerContainer, nativelogic);
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

    private UnifiedBannerView GDTBanner(RelativeLayout relatAd) {
        // 创建 Banner 广告 AdView 对象
        // appId : 在 http://e.qq.com/dev/ 能看到的 app 唯一字符串
        // posId : 在 http://e.qq.com/dev/ 生成的数字串，并非 appid 或者 appkey
        UnifiedBannerView banner = new UnifiedBannerView(getActivity(), nativelogic.nativeObject.appid, nativelogic.nativeObject.posid, new UnifiedBannerADListener() {
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
        banner.setRefresh(30);

        relatAd.addView(banner);
        /* 发起广告请求，收到广告数据后会展示数据     */
        banner.loadAD();

        return banner;
    }
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(Icons serviceList) {
        ServerManager.getServerManager().setProgramaData(getActivity(), serveTxt, serviceList, ListRecycler);
    }

    @Override
    public void onReceiveAd() {
    }

    @Override
    public void onFailedToReceiveAd() {
    }

    @Override
    public void onLoadFailed() {
    }

    @Override
    public void onCloseClick() {
    }

    @Override
    public void onAdClick() {
    }

    @Override
    public void onAdExposure() {
    }

    @OnClick({R.id.sw_night_mode, R.id.open_notification_bar, R.id.skin_is_set, R.id.clear_cache, R.id.message_feedback, R.id.about_us, R.id.widget})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sw_night_mode:
            case R.id.open_notification_bar:
                try {
                    String notification = SaveShare.getValue(getActivity(), "ISNotification");
                    if (TextUtils.isEmpty(notification)) {
                        swNightMode.setChecked(true);
                        SaveShare.saveValue(getActivity(), "ISNotification", "1");
                        ToastUtils.showLong("通知栏开启");
                        if (WeatherPageData.notification != null) {
                            Intent intent = new Intent(getActivity(), CancelNoticeService.class);
                            intent.putExtra("notification", WeatherPageData.notification);
                            Objects.requireNonNull(getActivity()).startService(intent);
                        } else {
                            swNightMode.setChecked(false);
                            SaveShare.saveValue(getActivity(), "ISNotification", "");
                            ToastUtils.showLong("通知栏开启失败");
                        }
                    } else {
                        swNightMode.setChecked(false);
                        SaveShare.saveValue(getActivity(), "ISNotification", "");
                        Intent intent = new Intent(getActivity(), CancelNoticeService.class);
                        Objects.requireNonNull(getActivity()).stopService(intent);
                        ToastUtils.showLong("通知栏关闭");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.skin_is_set:
                Intent intent = new Intent(getActivity(), AtlasActivity.class);
                Objects.requireNonNull(getActivity()).overridePendingTransition(com.constellation.xylibrary.R.anim.in_from_right, com.constellation.xylibrary.R.anim.out_to_left);
                startActivity(intent);
                break;
            case R.id.clear_cache:
                ServerManager.getServerManager().showIOSActionSheetDialog(getActivity(), cacheTv);

                break;
            case R.id.message_feedback:
                try {
                    String Tom = SaveShare.getValue(getActivity(), "Tom");
                    if (TextUtils.isEmpty(Tom)) {
                        SaveShare.saveValue(getActivity(), "Tom", "Tom" + System.currentTimeMillis());
                        Tom = "Tom" + System.currentTimeMillis();
                    }
                    LCChatKit.getInstance().open(Tom, new AVIMClientCallback() {
                        @Override
                        public void done(AVIMClient avimClient, AVIMException e) {
                            if (null == e) {
                                Intent intent1 = new Intent(getActivity(), LCIMConversationActivity.class);
                                intent1.putExtra(LCIMConstants.PEER_ID, "Jerry");
                                startActivity(intent1);
                            } else {
                                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.about_us:
                Intent intent2 = new Intent(getActivity(), AboutActivity.class);
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                startActivity(intent2);
                break;
            case R.id.widget:
                AdviseMoreDetailActivity.startActivity(getActivity(), "小部件开启设置", "http://api.xytq.qukanzixun.com/XinGYunTianQi.html");
                break;
        }
    }
}
