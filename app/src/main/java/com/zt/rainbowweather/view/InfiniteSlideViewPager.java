package com.zt.rainbowweather.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.zt.rainbowweather.utils.State;

import java.util.List;

/**
 * 无限滑动viewpager
 * @author zw
 * */
public class InfiniteSlideViewPager extends ViewGroup {
    private VelocityTracker mVelocityTracker;

    private static final String TAG  = "TestPager";
    private float mDownX;
    private float mMotionX;
    private float mPointerOffset;
    private float mCurPageOffset;
    private int mTouchSlop;
    private int mMaxVelocity;
    private int mActivePointerId;

    private int mPagerCount;
    private int mShowCount;

    private int mLeftPos;
    private int mRightPos;
    private boolean mIsDragging;
    private boolean mIntercept;


    private State mState;

    private ValueAnimator animation;

    private List<View> mViewList;

    public InfiniteSlideViewPager(Context context, AttributeSet attr){
        super(context,attr);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();


        mMaxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();

        mState = State.RESET;
        mIsDragging = false;
        mIntercept = false;
        mShowCount = 0;
        mPointerOffset = 0;
        mCurPageOffset = 0;
    }


    public void setViewList(List<View> list){
        if(list == null)
            return;
        if(mViewList == null)
            mViewList = list;
        mPagerCount = mViewList.size();

        if(mPagerCount >= 3){
            mShowCount = 3;
            addView(list.get(mPagerCount - 1));
            addView(list.get(0));
            addView(list.get(1));


            mLeftPos = mPagerCount - 1;
            mRightPos = 1;
        }
        else if(mPagerCount == 1){
            mShowCount = 1;
            addView(list.get(0));
            mLeftPos = 0;
            mRightPos = 0;
        }
        else if(mPagerCount == 2){
            mShowCount = 2;
            addView(list.get(0));
            mLeftPos = 1;
            mRightPos = 1;
        }
    }


    @Override
    public void computeScroll(){

    }


    @Override
    public void onMeasure(int withMeasureSpec,int heightMeasureSpec){
        super.onMeasure(withMeasureSpec,heightMeasureSpec);
        if(mShowCount >= 3 || mShowCount == 1)
        {
            for(int i = 0;i<mShowCount;i++){
                View child = getChildAt(i);
                measureChild(child,
                        MeasureSpec.makeMeasureSpec(withMeasureSpec - getPaddingLeft() - getPaddingRight(),MeasureSpec.EXACTLY)
                        ,MeasureSpec.makeMeasureSpec(heightMeasureSpec-getPaddingTop()- getPaddingBottom(),MeasureSpec.EXACTLY));
            }
        }
        else if(mShowCount == 2){
            if(mState == State.RESET){
                View child = getChildAt(0);
                measureChild(child,
                        MeasureSpec.makeMeasureSpec(withMeasureSpec - getPaddingLeft() - getPaddingRight(),MeasureSpec.EXACTLY)
                        ,MeasureSpec.makeMeasureSpec(heightMeasureSpec-getPaddingTop()- getPaddingBottom(),MeasureSpec.EXACTLY));
            }
            else{
                for(int i = 0;i<mShowCount;i++){
                    View child = getChildAt(i);
                    measureChild(child,
                            MeasureSpec.makeMeasureSpec(withMeasureSpec - getPaddingLeft() - getPaddingRight(),MeasureSpec.EXACTLY)
                            ,MeasureSpec.makeMeasureSpec(heightMeasureSpec-getPaddingTop()- getPaddingBottom(),MeasureSpec.EXACTLY));
                }
            }
        }

    }

    @Override
    public void onLayout(boolean b,int left,int top,int right,int bottom){
        Log.e(TAG,"onLayout");
        int with = getMeasuredWidth();
        int height = getMeasuredHeight();

        final int childLeft = getPaddingLeft();
        final int childRight = with - getPaddingRight();
        final int childBottom = height - getPaddingBottom();
        final int childTop = getPaddingTop();
        if(mShowCount >= 3){
            for(int i = 0;i<mShowCount;i++){
                View child = getChildAt(i);
                child.layout(childLeft+(i-1)*with,childTop,childRight+(i-1)*with,childBottom);
            }
        }
        else if(mShowCount == 2){
            if(mState == State.RESET){
                View child = getChildAt(0);
                child.layout(childLeft,childTop,childRight,childBottom);
            }
            else if(mState == State.PULL_LEFT){
                for(int i = 0;i<mShowCount;i++){
                    View child = getChildAt(i);
                    child.layout(childLeft+i*with,childTop,childRight+i*with,childBottom);
                }
            }
            else if(mState == State.PULL_RIGHT){
                getChildAt(0).layout(childLeft,childTop,childRight,childBottom);
                getChildAt(1).layout(childLeft-with,childTop,childRight-with,childBottom);
            }
        }
        else if(mShowCount == 1){
            View child = getChildAt(0);
            child.layout(childLeft,childTop,childRight,childBottom);
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event){
        Log.e(TAG,"onInterceptTouchEvent");

        int action =  event.getActionMasked();
        int pointerIndex;

        if(animation != null && animation.isRunning()){
            mIntercept = true;
            animation.cancel();
            mIntercept = false;
        }

        switch (action){
            case MotionEvent.ACTION_DOWN: {
                Log.e(TAG,"I_ACTION_DOWN");
                mActivePointerId = event.getPointerId(0);
                pointerIndex = event.findPointerIndex(mActivePointerId);
                mDownX = event.getX(pointerIndex);

                mPointerOffset = 0;
                if(mIsDragging)
                    mMotionX = mDownX;

                break;
            }
            case MotionEvent.ACTION_MOVE: {
                Log.e(TAG,"I_ACTION_MOVE");
                pointerIndex = event.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0)
                    return false;
                float x = event.getX(pointerIndex);
                Log.e(TAG,"x"+x);
                startDrag(x);
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN:{
                pointerIndex = event.getActionIndex();
                if(pointerIndex < 0)
                    return false;
                mActivePointerId = event.getPointerId(pointerIndex);

                mDownX = event.getX(pointerIndex);
                if(mIsDragging)
                    mMotionX = mDownX;
                break;
            }
            case MotionEvent.ACTION_POINTER_UP:{
                onSecondPointerUp(event);
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mActivePointerId = -1;
                mIsDragging = false;
                break;
        }

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        return mIsDragging;
    }

    @Override
    public void dispatchDraw(Canvas canvas){
        if(mPagerCount >= 2){
            if(mState == State.RESET)
            {
                super.dispatchDraw(canvas);
            }
            else if(mState == State.PULL_LEFT){
                canvas.translate(mCurPageOffset,0);
                super.dispatchDraw(canvas);
            }
            else if(mState == State.PULL_RIGHT){
                canvas.translate(mCurPageOffset,0);
                super.dispatchDraw(canvas);
            }
        }
        else if(mPagerCount == 1){
            if(mState == State.RESET)
            {
                super.dispatchDraw(canvas);
            }
            else if(mState == State.PULL_LEFT){
                canvas.save();
                canvas.translate(getWidth()+mCurPageOffset,0);
                canvas.clipRect(getLeft(),getTop(),getLeft()-mCurPageOffset,getHeight());
                super.dispatchDraw(canvas);
                canvas.restore();
                canvas.translate(mCurPageOffset,0);
                canvas.clipRect(getLeft()-mCurPageOffset,getTop(),getWidth(),getHeight());
                super.dispatchDraw(canvas);
            }
            else if(mState == State.PULL_RIGHT){
                canvas.save();
                canvas.translate(mCurPageOffset - getWidth(),0);
                canvas.clipRect(getWidth() - mCurPageOffset,getTop(),getWidth(),getHeight());
                super.dispatchDraw(canvas);
                canvas.restore();
                canvas.translate(mCurPageOffset,0);
                canvas.clipRect(getLeft(),getTop(),getWidth() - mCurPageOffset,getHeight());
                super.dispatchDraw(canvas);
            }
        }
    }


    private void onSecondPointerUp(MotionEvent event){
        int pointIndex = event.getActionIndex();
        int pointId = event.getPointerId(pointIndex);
        if(pointId == mActivePointerId){
            int nowPointIndex = pointIndex == 0 ? 1:0;
            mActivePointerId = event.getPointerId(nowPointIndex);
            mPointerOffset = 0;
            mMotionX = mDownX = event.getX(nowPointIndex);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        Log.e(TAG,"onTouchEvent");
        int action = event.getActionMasked();
        int pointIndex;


        if(mVelocityTracker == null)
        {
            mVelocityTracker = VelocityTracker.obtain();
        }

        mVelocityTracker.addMovement(event);

        switch (action){
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = event.getPointerId(0);
                mPointerOffset = 0;
                break;
            case MotionEvent.ACTION_MOVE: {
                Log.e(TAG, "ACTION_MOVE");
                pointIndex = event.findPointerIndex(mActivePointerId);
                if (pointIndex < 0)
                    return false;

                final float x = event.getX(pointIndex);
                Log.e(TAG, "x" + x);
                Log.e(TAG, "mDownX" + mDownX);
                startDrag(x);

                if (mIsDragging) {
                    float oldPointerOffset = mPointerOffset;
                    State oldState = mState;
                    mPointerOffset = x - mMotionX;
                    mCurPageOffset = mPointerOffset - oldPointerOffset + mCurPageOffset;
                    mState = mCurPageOffset < 0 ? State.PULL_LEFT:State.PULL_RIGHT;

                    Log.e(TAG,"mMotionX"+mMotionX);
                    Log.e(TAG, "oldPointerOffset" + oldPointerOffset);
                    Log.e(TAG, "mCurPageOffset" + mCurPageOffset);
                    Log.e(TAG, "mPointerOffset" + mPointerOffset);

                    if(mPagerCount == 2 && oldState != mState){
                        if(oldState == State.RESET)
                        {
                            addView(mViewList.get(mLeftPos));
                        }
                        else{
                            requestLayout();
                        }
                    }


                    if(mCurPageOffset > getWidth()){
                        if(mPagerCount >= 3)
                        {
                            mRightPos = (mRightPos - 1 + mPagerCount) % mPagerCount;
                            mLeftPos = (mLeftPos - 1 + mPagerCount) % mPagerCount;
                            View view = mViewList.get(mLeftPos);
                            removeViewAt(2);
                            addView(view, 0);
                        }
                        else if(mPagerCount == 2){
                            mRightPos = (mRightPos - 1 + mPagerCount) % mPagerCount;
                            mLeftPos = (mLeftPos - 1 + mPagerCount) % mPagerCount;
                            View view = getChildAt(1);
                            removeViewAt(1);
                            addView(view, 0);
                        }
                        mCurPageOffset -= getWidth();
                    }
                    else if(mCurPageOffset < -getWidth()) {
                        if(mPagerCount >= 3) {
                            mRightPos = (mRightPos + 1) % mPagerCount;
                            mLeftPos = (mLeftPos + 1) % mPagerCount;
                            View view = mViewList.get(mRightPos);
                            removeViewAt(0);
                            addView(view, 2);
                        }
                        else if(mPagerCount == 2){
                            mRightPos = (mRightPos + 1) % mPagerCount;
                            mLeftPos = (mLeftPos + 1) % mPagerCount;
                            View view = getChildAt(0);
                            removeViewAt(0);
                            addView(view, 1);
                        }

                        mCurPageOffset += getWidth();
                    }

                    invalidate();
                }
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN:{
                Log.e(TAG,"ACTION_POINTER_DOWN");
                pointIndex = event.getActionIndex();
                if(pointIndex < 0)
                    return false;
                Log.e(TAG,"mActivePointerId"+mActivePointerId);
                mActivePointerId = event.getPointerId(pointIndex);
                Log.e(TAG,"mActivePointerId"+mActivePointerId);
                mDownX = event.getX(pointIndex);
                mPointerOffset = 0;
                if(mIsDragging)
                    mMotionX = mDownX;
                break;

            }
            case MotionEvent.ACTION_POINTER_UP:{
                Log.e(TAG,"ACTION_POINTER_UP");
                onSecondPointerUp(event);
                break;
            }
            case MotionEvent.ACTION_UP: {
                Log.e(TAG, "ACTION_UP");
                if(mIsDragging){
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000,mMaxVelocity);
                    int initialVelocity = (int) velocityTracker.getXVelocity(mActivePointerId);

                    Log.e(TAG,"initialVelocity"+initialVelocity);
                    boolean quickScroll = Math.abs(initialVelocity) > 4000;
                    if((mCurPageOffset > 0 && initialVelocity > 4000) || (mCurPageOffset < 0 && initialVelocity <-4000)
                            || (mCurPageOffset > getWidth()/2 && initialVelocity < 4000 && initialVelocity > 0)
                            || (mCurPageOffset <- getWidth()/2 && initialVelocity>-4000 && initialVelocity < 0)) {
                        startAnimation(true,quickScroll);
                    }
                    else{
                        startAnimation(false,quickScroll);
                    }
                }
                break;
            }
        }
        return true;
    }


    private void startAnimation(boolean nextPage,boolean quickScroll){
        if(nextPage){
            animation = ValueAnimator.ofFloat(Math.abs(mCurPageOffset),getWidth());
            animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurPageOffset = mState == State.PULL_LEFT ? -(Float) animation.getAnimatedValue():(Float) animation.getAnimatedValue();
                    Log.e(TAG,"mCurPageOffset"+mCurPageOffset);
                    invalidate();
                }
            });

            animation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                }
            });


            animation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if(!mIntercept){
                        Log.e(TAG,"onAnimationEnd");
                        State oldState = mState;
                        mState = State.RESET;
                        mIsDragging = false;

                        if(mPagerCount >= 3){
                            if(oldState == State.PULL_LEFT){
                                mRightPos = (mRightPos + 1) % mPagerCount;
                                mLeftPos = (mLeftPos + 1) % mPagerCount;
                                View view = mViewList.get(mRightPos);
                                removeViewAt(0);
                                addView(view, 2);
                            }
                            else if(oldState == State.PULL_RIGHT){
                                mRightPos = (mRightPos - 1 + mPagerCount) % mPagerCount;
                                mLeftPos = (mLeftPos - 1 + mPagerCount) % mPagerCount;
                                View view = mViewList.get(mLeftPos);
                                removeViewAt(2);
                                addView(view, 0);
                            }
                        }
                        else if(mPagerCount == 2){
                            if(oldState == State.PULL_LEFT){
                                mRightPos = (mRightPos + 1) % mPagerCount;
                                mLeftPos = (mLeftPos + 1) % mPagerCount;
                                removeViewAt(0);
                            }
                            else if(oldState == State.PULL_RIGHT){
                                mRightPos = (mRightPos - 1 + mPagerCount) % mPagerCount;
                                mLeftPos = (mLeftPos - 1 + mPagerCount) % mPagerCount;
                                removeViewAt(0);
                            }
                        }
                        mCurPageOffset = 0;
                    }
                }
            });

        }
        else{
            animation = ValueAnimator.ofFloat(mCurPageOffset,0f);
            animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurPageOffset = (Float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            animation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if(!mIntercept){
                        Log.e(TAG,"XXX");
                        State oldState = mState;
                        mState = State.RESET;
                        mIsDragging = false;
                        if(mPagerCount == 2) {
                            if (oldState == State.PULL_LEFT) {
                                removeViewAt(1);
                            } else if (oldState == State.PULL_RIGHT) {
                                removeViewAt(1);
                            }
                        }
                    }
                }
            });
        }

        long animationTime;
        //animationTime = (long) (1000 * Math.min((getWidth() - Math.abs(mCurPageOffset)),Math.abs(mCurPageOffset))/getWidth());

        if(quickScroll && nextPage)
            animationTime = (long)(400 * ((getWidth() - Math.abs(mCurPageOffset)))/getWidth());
        else if(!quickScroll) {
            animationTime = (long) (1000 * Math.min((getWidth() - Math.abs(mCurPageOffset)),Math.abs(mCurPageOffset))/getWidth());
        }
        else{
            animationTime = (long)(400 * ( Math.abs(mCurPageOffset))/getWidth());
        }

//        animationTime = quickScroll?
//                (long)(400 * ((getWidth() - Math.abs(mCurPageOffset)))/getWidth())
//                :(long) (1000 * Math.min((getWidth() - Math.abs(mCurPageOffset)),Math.abs(mCurPageOffset))/getWidth());

        animation.setDuration(animationTime);
        animation.start();
    }


    private void startDrag(float x) {
        if (Math.abs(x - mDownX) > mTouchSlop && !mIsDragging) {
            mMotionX = x - mDownX < 0 ? mDownX - mTouchSlop : mDownX + mTouchSlop;
            mIsDragging = true;
        }
    }
}


