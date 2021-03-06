package com.xy.xylibrary.indicate;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.core.Controller;
import com.app.hubert.guide.listener.OnGuideChangedListener;
import com.app.hubert.guide.listener.OnLayoutInflatedListener;
import com.app.hubert.guide.listener.OnPageChangedListener;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;
import com.app.hubert.guide.model.RelativeGuide;
import com.constellation.xylibrary.R;
import com.xy.xylibrary.utils.SaveShare;
import com.xy.xylibrary.utils.ToastUtils;


/**
 * 指示页
 * */
public class IndicatePage {

    private static IndicatePage indicatePage;

    public static IndicatePage getIndicatePage() {
        if (indicatePage == null) {
            synchronized (IndicatePage.class){
                if (indicatePage == null) {
                    indicatePage = new IndicatePage();
                }
            }
        }
        return indicatePage;
    }

    public void setNewbieGuide(Fragment fragment, final Context context, final View view1, final View view2, final View view3){
        if(!TextUtils.isEmpty(SaveShare.getValue(context,"Indicate"))){
            return;
        }
       SaveShare.saveValue(context,"Indicate","Indicate");
        Animation enterAnimation = new AlphaAnimation(0f, 1f);
        enterAnimation.setDuration(600);
        enterAnimation.setFillAfter(true);
        Animation exitAnimation = new AlphaAnimation(1f, 0f);
        exitAnimation.setDuration(600);
        exitAnimation.setFillAfter(true);
        NewbieGuide.with(fragment)
                .setLabel("guide1")
                .alwaysShow(true)//总是显示，调试时可以打开
                .setOnGuideChangedListener(new OnGuideChangedListener() {
                    @Override
                    public void onShowed(Controller controller) {

                     }

                    @Override
                    public void onRemoved(Controller controller) {

                     }
                })
                .setOnPageChangedListener(new OnPageChangedListener() {
                    @Override
                    public void onPageChanged(int page) {
                        //引导页切换，page 为当前页位置，从 0 开始
//                        ToastUtils.showLong( "引导页切换");
                     }
                })
                .addGuidePage(//添加一页引导页
                        GuidePage.newInstance()//创建一个实例
                                .addHighLight(view1, new RelativeGuide(R.layout.view_guide,
                                        Gravity.RIGHT, 100))//添加高亮的 view
                                .addHighLight(view1, HighLight.Shape.OVAL,5)
                                .setLayoutRes(R.layout.view_guide)//设置引导页布局
                                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                                    @Override
                                    public void onLayoutInflated(View view, Controller controller) {
                                        //引导页布局填充后回调，用于初始化
                                        ImageView tv = view.findViewById(R.id.iv);
                                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        layoutParams.leftMargin = view1.getLeft()+view1.getWidth()/5;
                                        layoutParams.topMargin = view1.getTop() + view1.getHeight();
                                        tv.setLayoutParams(layoutParams);
                                        tv.setImageDrawable(context.getResources().getDrawable(R.drawable.weather_warning));
                                    }
                                })
                                .setEnterAnimation(enterAnimation)//进入动画
                                .setExitAnimation(exitAnimation)//退出动画
                )
                .addGuidePage(
                        GuidePage.newInstance()
//                                .addHighLight(view2, new RelativeGuide(R.layout.view_guide,
//                                        Gravity.RIGHT, 100))//添加高亮的 view
                                .addHighLight(view2, HighLight.Shape.CIRCLE, 20)
                                .setLayoutRes(R.layout.view_guide, R.id.iv)//引导页布局，点击跳转下一页或者消失引导层的控件 id
                                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                                    @Override
                                    public void onLayoutInflated(View view, Controller controller) {
                                        //引导页布局填充后回调，用于初始化
                                        ImageView tv = view.findViewById(R.id.iv);
                                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        layoutParams.leftMargin = view2.getLeft();
                                        layoutParams.topMargin = view2.getTop() * 2 + view2.getHeight()*2;
                                        tv.setLayoutParams(layoutParams);
                                        tv.setImageDrawable(context.getResources().getDrawable(R.drawable.weather_conditions));
                                    }
                                })
//                                .setEverywhereCancelable(false)//是否点击任意地方跳转下一页或者消失引导层，默认 true
//                                .setBackgroundColor(context.getResources().getColor(R.color.white))//设置背景色，建议使用有透明度的颜色
                                .setEnterAnimation(enterAnimation)//进入动画
                                .setExitAnimation(exitAnimation)//退出动画
                )
                .addGuidePage(
                        GuidePage.newInstance()
                                .addHighLight(view3, new RelativeGuide(R.layout.view_guide,
                                        Gravity.RIGHT, 100))//添加高亮的 view
                                .addHighLight(view3, HighLight.Shape.OVAL,5)
                                .setLayoutRes(R.layout.view_guide, R.id.iv)//引导页布局，点击跳转下一页或者消失引导层的控件 id
                                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                                    @Override
                                    public void onLayoutInflated(View view, Controller controller) {
                                        //引导页布局填充后回调，用于初始化
                                        ImageView tv = view.findViewById(R.id.iv);
                                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        layoutParams.leftMargin = view3.getLeft()+view3.getWidth()/5;
                                        layoutParams.topMargin = view3.getTop()-view3.getHeight()*2;
                                        tv.setLayoutParams(layoutParams);
                                        tv.setImageDrawable(context.getResources().getDrawable(R.drawable.today_weather_conditions));
                                    }
                                })
//                                .setEverywhereCancelable(false)//是否点击任意地方跳转下一页或者消失引导层，默认 true
//                                .setBackgroundColor(context.getResources().getColor(R.color.white))//设置背景色，建议使用有透明度的颜色
                                .setEnterAnimation(enterAnimation)//进入动画
                                .setExitAnimation(exitAnimation)//退出动画
                )
                .show();
    }

    public void setGoldNewbieGuide(Fragment fragment, final Context context, final View view1, final View recyclerLayoutList){
        if(!TextUtils.isEmpty(SaveShare.getValue(context,"Indicate2"))){
            return;
        }
        SaveShare.saveValue(context,"Indicate2","Indicate2");
        Animation enterAnimation = new AlphaAnimation(0f, 1f);
        enterAnimation.setDuration(600);
        enterAnimation.setFillAfter(true);

        Animation exitAnimation = new AlphaAnimation(1f, 0f);
        exitAnimation.setDuration(600);
        exitAnimation.setFillAfter(true);
        NewbieGuide.with(fragment)
                .setLabel("guide1")
                .alwaysShow(true)//总是显示，调试时可以打开
                .setOnGuideChangedListener(new OnGuideChangedListener() {
                    @Override
                    public void onShowed(Controller controller) {

                    }

                    @Override
                    public void onRemoved(Controller controller) {

                    }
                })
                .setOnPageChangedListener(new OnPageChangedListener() {
                    @Override
                    public void onPageChanged(int page) {
                        //引导页切换，page 为当前页位置，从 0 开始
//                        ToastUtils.showLong( "引导页切换");
                    }
                })
                .addGuidePage(//添加一页引导页
                        GuidePage.newInstance()//创建一个实例
                                .addHighLight(view1, new RelativeGuide(R.layout.view_guide,
                                        Gravity.RIGHT, 100))//添加高亮的 view
                                .addHighLight(view1, HighLight.Shape.OVAL,5)
                                .setLayoutRes(R.layout.view_guide)//设置引导页布局
                                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                                    @Override
                                    public void onLayoutInflated(View view, Controller controller) {
                                        //引导页布局填充后回调，用于初始化
                                        ImageView tv = view.findViewById(R.id.iv);
                                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        layoutParams.leftMargin = view1.getWidth()/3;
                                        layoutParams.topMargin = view1.getTop()+ view1.getHeight();
                                        tv.setLayoutParams(layoutParams);
                                        tv.setImageDrawable(context.getResources().getDrawable(R.drawable.sign_hint));

                                    }
                                })
                                .setEnterAnimation(enterAnimation)//进入动画
                                .setExitAnimation(exitAnimation)//退出动画
                )
                .addGuidePage(
                        GuidePage.newInstance()
                                .addHighLight(recyclerLayoutList, new RelativeGuide(R.layout.view_guide,
                                        Gravity.RIGHT, 100))//添加高亮的 view
                                .addHighLight(recyclerLayoutList, HighLight.Shape.OVAL,5)
                                .setLayoutRes(R.layout.view_guide, R.id.iv)//引导页布局，点击跳转下一页或者消失引导层的控件 id
                                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                                    @Override
                                    public void onLayoutInflated(View view, Controller controller) {
                                        //引导页布局填充后回调，用于初始化
                                        ImageView tv = view.findViewById(R.id.iv);
                                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        layoutParams.leftMargin = recyclerLayoutList.getWidth()/5;
                                        layoutParams.topMargin = recyclerLayoutList.getTop()+ recyclerLayoutList.getWidth()+ recyclerLayoutList.getWidth()/5;
                                        tv.setLayoutParams(layoutParams);
                                        tv.setImageDrawable(context.getResources().getDrawable(R.drawable.finish_task_hint));
                                    }
                                })
//                                .setEverywhereCancelable(false)//是否点击任意地方跳转下一页或者消失引导层，默认 true
//                                .setBackgroundColor(context.getResources().getColor(R.color.white))//设置背景色，建议使用有透明度的颜色
                                .setEnterAnimation(enterAnimation)//进入动画
                                .setExitAnimation(exitAnimation)//退出动画
                )
                .show();
    }
}
