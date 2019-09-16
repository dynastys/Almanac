package com.zt.rainbowweather.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.qq.e.ads.banner2.UnifiedBannerADListener;
import com.qq.e.ads.banner2.UnifiedBannerView;
import com.qq.e.comm.util.AdError;
import com.zt.rainbowweather.presenter.PangolinBannerAd;
import com.zt.weather.R;
import com.zt.xuanyin.Interface.AdProtogenesisListener;
import com.zt.xuanyin.controller.Ad;
import com.zt.xuanyin.controller.NativeAd;
import com.zt.xuanyin.entity.model.Native;

import java.util.Objects;

import butterknife.BindView;


public class FinishTaskDialog extends Dialog {

    private Activity context;
    private String title;
    private ClickListenerInterface clickListenerInterface;
    private int notes;
    private int image;
    private int type;
    public interface ClickListenerInterface {
        public void doConfirm();
        public void doCancel();
    }

    public FinishTaskDialog(Activity context, String title, int notes, int img, int type) {
        super(context, R.style.LoadingDialogTheme);
        this.context = context;
        this.title = title;
        this.notes = notes;
        this.image = img;
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
        ImageView finish_task_dialog_cancel = view.findViewById(R.id.finish_task_dialog_cancel);
        LinearLayout adLin = view.findViewById(R.id.ad_lin);
        FrameLayout bannerContainer = view.findViewById(R.id.banner_container);
        RelativeLayout GDTAd = view.findViewById(R.id.GDT_ad);
        ImageView adImageBanner = view.findViewById(R.id.ad_image_banner);
        BannerAd(adLin,bannerContainer,GDTAd,adImageBanner);
//        hint_content.setText(notes);
        if(!TextUtils.isEmpty(title)){
            tvTitle.setText(title);
        }else{
            tvTitle.setVisibility(View.GONE);
        }
        if(image != 0){
            index_details_img.setImageResource(image);
        }
        if(context != null && notes != 0){
            String str = "夏季制冷时，相对湿度以<font color='#56CCF6'>40%—80%</font>为宜。\n 冬季采暖时，应控制在<font color='#56CCF6'>30%—60%</font>。\n 老人和小孩适合的室内湿度为<font color='#56CCF6'>45%—50%</font>。\n 哮喘等呼吸道系统疾病的患者适宜的室内湿度在<font color='#56CCF6'>40%—50%</font>之间。";
            String str2 = "指数值3到4，一般为多云天气，此时紫外线强度较弱，预报等级为<font color='#56CCF6'>二级</font>；5到6，一般为少云天气，此时紫外线强度较强，预报等级为<font color='#56CCF6'>三级</font>；7到9，一般为晴天无云，此时紫外线强度很强，预报等级为<font color='#56CCF6'>四级</font>；达到或超过10，多为夏季晴日，紫外线强度特别强，预报等级为<font color='#56CCF6'>五级</font>。";
            switch (type){
                case 0:
                    tvConfirm.setText(Html.fromHtml(str));
                    break;
                case 1:
                    tvConfirm.setText(Html.fromHtml(str2));
                    break;
                case 2:
                    tvConfirm.setText(context.getResources().getText(notes));
                    break;
            }
        }else{
            tvConfirm.setText("加载错误");
        }
        hint_content.setOnClickListener(new clickListener());
        finish_task_dialog_cancel.setOnClickListener(new clickListener());
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
        dialogWindow.setWindowAnimations(R.style.animate_dialog);
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
                clickListenerInterface.doCancel();
            } else if (id == R.id.finish_task_dialog_cancel){
                clickListenerInterface.doCancel();
            }
        }

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
                        GDTBanner(context,GDTAd);
                        bannerContainer.setVisibility(View.GONE);
                        adLin.setVisibility(View.GONE);
                    } else if (nativelogic.nativeObject.sdk_code.equals("TOUTIAO_SDK")) {
                        adLin.setVisibility(View.GONE);
                        GDTAd.setVisibility(View.GONE);
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
        banner.setRefresh(30);
        relatAd.addView(banner);
        /* 发起广告请求，收到广告数据后会展示数据     */
        banner.loadAD();

        return banner;
    }

}
