package com.zt.rainbowweather.presenter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class ScrollLinearLayoutManager extends LinearLayoutManager {

    private boolean isScrollEnable = true;
    private double speedRatio;
    public ScrollLinearLayoutManager(Context context) {
        super(context);
    }

    public ScrollLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }


    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }
    @Override
    public boolean canScrollVertically() {
        return isScrollEnable;
    }
    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int a = super.scrollHorizontallyBy((int)(speedRatio*dx), recycler, state);//屏蔽之后无滑动效果，证明滑动的效果就是由这个函数实现
        if(a == (int)(speedRatio*dx)){
            return dx;
        }
        return a;
    }

    public void setSpeedRatio(double speedRatio){
        this.speedRatio = speedRatio;
    }

    /**
     * 设置 RecyclerView 是否可以垂直滑动
     * @param isEnable
     */
    public void setScrollEnable(boolean isEnable) {
        this.isScrollEnable = isEnable;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
 }