package com.xy.xylibrary.config;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
 import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.constellation.xylibrary.R;
import com.xy.xylibrary.base.BaseFragment;
import com.xy.xylibrary.ui.adapter.FragmentPagerAdapter;
import com.xy.xylibrary.utils.Utils;
import com.xy.xylibrary.view.AutoHeightViewPager;
import com.xy.xylibrary.view.ColumnHorizontalScrollView;
import com.xy.xylibrary.view.CustomScrollViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 栏目选择
 * */
public class ColumnHorizontalPackage<T> implements ViewPager.OnPageChangeListener{

    private Activity context;
    /**
     * 屏幕宽度
     */
    private int mScreenWidth = 0;
    /**
     * Item宽度
     */
    private int mItemWidth = 0;
    //栏目text
    private TextView columnTextView;

    /*栏目数据*/
//    private List<String> userColumnList = new ArrayList<>();
    private ArrayList<BaseFragment> fragments = new ArrayList<>();
    private ColumnHorizontalScrollView mColumnHorizontalScrollView;
    private CustomScrollViewPager viewpagerColumn;
    private ViewGroup mRadioGroup_content;
    private List<T> columnClassify = new ArrayList<>();
    private BaseFragment fragment;

    public ColumnHorizontalPackage(Activity context, ColumnHorizontalScrollView mColumnHorizontalScrollView,CustomScrollViewPager viewpagerColumn){
        this.context = context;
        this.mColumnHorizontalScrollView = mColumnHorizontalScrollView;
        this.viewpagerColumn = viewpagerColumn;

    }


    public void initData(BaseFragment fragment, ViewGroup mRadioGroup_content,ArrayList<BaseFragment> fragments,List<T> columnClassify) {
//        try {
            this.fragment = fragment;
            this.mRadioGroup_content = mRadioGroup_content;
            this.fragments.clear();//清空
            this.fragments = fragments;
            this.columnClassify = columnClassify;
//             if(columnClassify != null && columnClassify.size() > 0){
//                userColumnList.add(Arrays.asList(context.getResources().getStringArray(R.array.column)).get(0));
//                for (T dataBean:columnClassify) {
//                    userColumnList.add(dataBean.getName());
//                }
//            }else{
//                userColumnList.addAll(Arrays.asList(context.getResources().getStringArray(R.array.column)));
//            }
//        } catch (Resources.NotFoundException e) {
//            e.printStackTrace();
//        }
//
//        try {

//            for (int i = 0; i < userColumnList.size(); i++) {
//                //传递数据到fragment
//                Bundle data = new Bundle();
//                if(i == 0){
//                    data.putString("text", "" + i);
//                }else{
//                    data.putString("text", "" + columnClassify.get(i-1).getProductTypeId());
//                }
//                BaseFragment newfragment = new BaseFragment();
//                newfragment.setArguments(data);
//                //加载fragment
//                fragments.add(newfragment);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        try {
            mColumnHorizontalScrollView.setHorizontalScrollBarEnabled(false);
            //适配器的加载
            FragmentPagerAdapter mAdapetr = new FragmentPagerAdapter(fragment.getChildFragmentManager(), fragments);
            viewpagerColumn.setAdapter(mAdapetr);
            ////    关闭预加载
            viewpagerColumn.setOffscreenPageLimit(columnClassify.size());
//            mVp.setOffscreenPageLimit(mSelectedDatas.size());
            viewpagerColumn.setOnPageChangeListener(this);
            // 如果不设置，可能第三个页面以后就显示不出来了，因为offset就是默认值1了
            viewpagerColumn.setOffscreenPageLimit(mAdapetr.getCount());
            viewpagerColumn.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(final int position) {
//                    // 切换到当前页面，重置高度
                    viewpagerColumn.resetHeight(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            initTabColumn(mRadioGroup_content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化Column栏目项
     */
    private void initTabColumn(final ViewGroup mRadioGroup_content) {
        try {
            mScreenWidth = Utils.getWindowsWidth(context);
            mItemWidth = mScreenWidth / 7;// 一个Item宽度为屏幕的1/7
            mColumnHorizontalScrollView.setParam(context, mScreenWidth,
                    mRadioGroup_content, null, null, null,
                    null);
            //加载栏目数据
            for (int i = 0; i < columnClassify.size(); i++) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                        1.0f);
                params.leftMargin = (int) (18 / Utils.Screen(context));
                params.rightMargin = (int) (18 / Utils.Screen(context));
                columnTextView = new TextView(context);
                columnTextView.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                columnTextView.setTextScaleX(1);
                columnTextView.setTextSize(15);

                columnTextView.setLayoutParams(params);
                columnTextView.setPadding(5, 5, 5, 5);
                try {
                    columnTextView.setText(columnClassify.get(i)+"");
                } catch (Exception e) {
                    columnTextView.setText("");
                    columnTextView.setVisibility(View.GONE);
                    e.printStackTrace();
                }
                //字体颜色
                if (i == 0) {
                    TextPaint p = columnTextView.getPaint();
                    p.setFakeBoldText(true);//中文英文都可以粗体
                    columnTextView.setTextColor(context.getResources().getColor(R.color.main_bg4));
                    columnTextView.setCompoundDrawablesWithIntrinsicBounds(null,null,null,context.getResources().getDrawable(R.drawable.ic_remove));
                } else {
                    TextPaint p = columnTextView.getPaint();
                    p.setFakeBoldText(false);//中文英文都可以粗体
                    columnTextView.setTextColor(context.getResources().getColor(R.color.nb_text_common_h2));
                    columnTextView.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                }
                //点击变化逻辑
                columnTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
                            TextView localView = (TextView) mRadioGroup_content.getChildAt(i);
                            if (localView != v) {
                                localView.setTextColor(context.getResources().getColor(R.color.nb_text_common_h2));
                                localView.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                                localView.setSelected(false);
                                TextPaint p = localView.getPaint();
                                p.setFakeBoldText(false);//中文英文都可以粗体
                            } else {
                                localView.setTextColor(context.getResources().getColor(R.color.main_bg4));
                                localView.setCompoundDrawablesWithIntrinsicBounds(null,null,null,context.getResources().getDrawable(R.drawable.ic_remove));
                                localView.setSelected(true);
                                TextPaint p = localView.getPaint();
                                p.setFakeBoldText(true);//中文英文都可以粗体
                                viewpagerColumn.setCurrentItem(i);
                            }
                        }
                    }
                });
                mRadioGroup_content.addView(columnTextView, i, params);
            }

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 选择的Column里面的Tab
     */
    private void selectTab(int tab_postion) {
        try {
            for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
                TextView checkView = (TextView) mRadioGroup_content.getChildAt(tab_postion);
                if (i == tab_postion) {
                    checkView.setTextColor(context.getResources().getColor(R.color.main_bg4));
                    checkView.setCompoundDrawablesWithIntrinsicBounds(null,null,null,context.getResources().getDrawable(R.drawable.ic_remove));
                    checkView.setTextSize(15);
                    TextPaint p = checkView.getPaint();
                    p.setFakeBoldText(true);//中文英文都可以粗体
                    int k = checkView.getMeasuredWidth();
                    int l = checkView.getLeft();
                    int i2 = l + k / 2 - mScreenWidth / 2;
                    mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
                    mRadioGroup_content.getChildAt(i).setSelected(i == tab_postion);
                } else {
                    checkView = (TextView) mRadioGroup_content.getChildAt(i);
                    checkView.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                    TextPaint p = checkView.getPaint();
                    p.setFakeBoldText(false);//中文英文都可以粗体
                    checkView.setTextColor(context.getResources().getColor(R.color.nb_text_common_h2));
                    checkView.setTextSize(15);
                    mRadioGroup_content.getChildAt(i).setSelected(i == tab_postion);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        try {
            //根据滑动改变栏目
            selectTab(position);
            //viewpager切换
            viewpagerColumn.setCurrentItem(position);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
