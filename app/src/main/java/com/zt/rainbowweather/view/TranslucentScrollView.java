package com.zt.rainbowweather.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.xy.xylibrary.utils.SizeUtils;
import com.zt.weather.R;


/**
 * 上滑渐变
 */

public class TranslucentScrollView extends NestedScrollView {

    static final String TAG = "TranslucentScrollView";

    //伸缩视图
    private View zoomView;
    //伸缩视图初始高度
    private int zoomViewInitHeight = 0;
    // 记录首次按下位置
    private float mFirstPosition = 0;
    // 是否正在放大
    private Boolean mScaling = false;

    //渐变的视图
    private View transView;
    //渐变颜色
    private int transColor = Color.WHITE;
    //渐变开始位置
    private int transStartY = 50;
    //渐变结束位置
    private int transEndY = 250;

    //渐变开始默认位置，Y轴，50dp
    private final int DFT_TRANSSTARTY = 50;
    //渐变结束默认位置，Y轴，300dp
    private final int DFT_TRANSENDY = 260;
    private Scroller mScroller;
    private TranslucentScrollView.TranslucentChangedListener translucentChangedListener;

    public interface TranslucentChangedListener {
        /**
         * 透明度变化，取值范围0-255
         *
         * @param transAlpha
         */
        void onTranslucentChanged(int transAlpha);
    }

    public TranslucentScrollView(Context context) {
        super(context);
        mScroller = new Scroller(context);
    }

    public TranslucentScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    public TranslucentScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
    }

    public void setTranslucentChangedListener(TranslucentScrollView.TranslucentChangedListener translucentChangedListener) {
        this.translucentChangedListener = translucentChangedListener;
    }

    /**
     * 设置伸缩视图
     *
     * @param zoomView
     */
    public void setPullZoomView(View zoomView) {
        this.zoomView = zoomView;
        zoomViewInitHeight = zoomView.getLayoutParams().height;
        if (zoomViewInitHeight == LayoutParams.MATCH_PARENT || zoomViewInitHeight == WindowManager.LayoutParams.WRAP_CONTENT) {
            zoomView.post(new Runnable() {
                @Override
                public void run() {
                    zoomViewInitHeight = TranslucentScrollView.this.zoomView.getHeight();
                }
            });
        }
    }
    //调用此方法滚动到目标位置  duration滚动时间
    public void smoothScrollToSlow(int fx, int fy, int duration) {
        int dx = fx - getScrollX();//mScroller.getFinalX();  普通view使用这种方法
        int dy = fy - getScrollY();  //mScroller.getFinalY();
        smoothScrollBySlow(dx, dy, duration);
    }

    //调用此方法设置滚动的相对偏移
    public void smoothScrollBySlow(int dx, int dy, int duration) {

        //设置mScroller的滚动偏移量
        mScroller.startScroll(getScrollX(), getScrollY(), dx, dy, duration);//scrollView使用的方法（因为可以触摸拖动）
//        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy, duration);  //普通view使用的方法
        invalidate();//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
    }
    @Override
    public void computeScroll() {
        //先判断mScroller滚动是否完成
        if (mScroller.computeScrollOffset()) {

            //这里调用View的scrollTo()完成实际的滚动
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());

            //必须调用该方法，否则不一定能看到滚动效果
            postInvalidate();
        }
        super.computeScroll();
    }
    /**
     * 设置渐变视图
     *
     * @param transView 渐变的视图
     */
    public void setTransView(View transView) {
        setTransView(transView, getResources().getColor(R.color.colorPrimary), SizeUtils.dip2px(getContext(), DFT_TRANSSTARTY), SizeUtils.dip2px(getContext(), DFT_TRANSENDY));
    }

    /**
     * 设置渐变颜色
     *
     * @param colorRes 渐变的颜色
     */
    public void setTransColor(@ColorInt int colorRes) {
        this.transColor = colorRes;
    }

    /**
     * 设置渐变视图
     *
     * @param transView  渐变的视图
     * @param transColor 渐变颜色
     * @param transEndY  渐变结束位置
     */
    public void setTransView(View transView, @ColorInt int transColor, int transStartY, int transEndY) {
        this.transView = transView;
        //初始视图-透明
        this.transView.setBackgroundColor(ColorUtils.setAlphaComponent(transColor, 0));
        this.transStartY = transStartY;
        this.transEndY = transEndY;
        this.transColor = transColor;
        if (transStartY > transEndY) {
            throw new IllegalArgumentException("transStartY 不得大于 transEndY .. ");
        }
    }

    /**
     * 获取透明度
     *
     * @return
     */
    private int getTransAlpha() {
        float scrollY = getScrollY();
        if (transStartY != 0) {
            if (scrollY <= transStartY) {
                return 0;
            } else if (scrollY >= transEndY) {
                return 255;
            } else {
                return (int) ((scrollY - transStartY) / (transEndY - transStartY) * 255);
            }
        } else {
            if (scrollY >= transEndY) {
                return 255;
            }
            return (int) ((transEndY - scrollY) / transEndY * 255);
        }
    }

    /**
     * 重置ZoomView
     */
    private void resetZoomView() {
        final ViewGroup.LayoutParams lp = zoomView.getLayoutParams();
        final float h = zoomView.getLayoutParams().height;// ZoomView当前高度

        // 设置动画
        ValueAnimator anim = ObjectAnimator.ofFloat(0.0F, 1.0F).setDuration(200);

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                lp.height = (int) (h - (h - zoomViewInitHeight) * cVal);
                zoomView.setLayoutParams(lp);
            }
        });
        anim.start();
    }
    /**
     * @param l    Current horizontal scroll origin. 当前滑动的x轴距离
     * @param t    Current vertical scroll origin. 当前滑动的y轴距离
     * @param oldl Previous horizontal scroll origin. 上一次滑动的x轴距离
     * @param oldt Previous vertical scroll origin. 上一次滑动的y轴距离
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        int transAlpha = getTransAlpha();
        if (transView != null) {
            Log.d(TAG, "[onScrollChanged .. in ], 透明度 == " + transAlpha);
            transView.setBackgroundColor(ColorUtils.setAlphaComponent(transColor, transAlpha));
        }
        if (translucentChangedListener != null) {
            translucentChangedListener.onTranslucentChanged(transAlpha);
        }
        if (mOnObservableScrollViewScrollChanged != null) {
            mOnObservableScrollViewScrollChanged.onObservableScrollViewScrollChanged(l, t, oldl, oldt);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (zoomView != null) {
            ViewGroup.LayoutParams params = zoomView.getLayoutParams();
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    //手指离开后恢复图片
                    mScaling = false;
                    resetZoomView();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!mScaling) {
                        if (getScrollY() == 0) {
                            mFirstPosition = event.getY();
                        } else {
                            break;
                        }
                    }

                    int distance = (int) ((event.getY() - mFirstPosition) * 0.6);
                    if (distance < 0) {
                        break;
                    }
                    mScaling = true;
                    params.height = zoomViewInitHeight + distance;

                    Log.d(TAG, "params.height == " + params.height + ", zoomViewInitHeight == " + zoomViewInitHeight + ", distance == " + distance);
                    zoomView.setLayoutParams(params);
                    return true;
            }
        }

        return super.onTouchEvent(event);
    }
    private OnHoldTabScrollViewScrollChanged mOnObservableScrollViewScrollChanged;

    public void setOnObservableScrollViewScrollChanged(OnHoldTabScrollViewScrollChanged mOnObservableScrollViewScrollChanged) {
        this.mOnObservableScrollViewScrollChanged = mOnObservableScrollViewScrollChanged;
    }
    public interface OnHoldTabScrollViewScrollChanged {
        void onObservableScrollViewScrollChanged(int l, int t, int oldl, int oldt);
    }

}

