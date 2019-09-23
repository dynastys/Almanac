package com.xy.xylibrary.signin;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.constellation.xylibrary.R;
import com.xy.xylibrary.utils.SizeUtils;

import java.util.ArrayList;
import java.util.List;

import static com.xy.xylibrary.utils.DensityUtil.getScreenWidth;

/**
 * description: 自定义签到View.
 */
public class ActiveView extends View {

    /**
     * 动画执行的时间 230毫秒
     */
    private final static int ANIMATION_TIME = 230;
    /**
     * 动画执行的间隔次数
     */
    private final static int ANIMATION_INTERVAL = 10;
    /**
     * 线段的高度
     */
    private float mCompletedLineHeight = SizeUtils.dip2px(getContext(), 4f);

    /**
     * 图标宽度
     */
    private float mIconWidth = SizeUtils.dip2px(getContext(), 26f);
    /**
     * 图标的高度
     */
    private float mIconHeight = SizeUtils.dip2px(getContext(), 26f);
    /**
     * UP宽度
     */
    private float mUpWidth = SizeUtils.dip2px(getContext(), 23.5f);
    /**
     * up的高度
     */
    private float mUpHeight = SizeUtils.dip2px(getContext(), 12f);

    /**
     * 线段长度
     */
    private float mLineWidth = SizeUtils.dip2px(getContext(), 21f);

    /**
     * 已经完成的图标
     */
    private Drawable mCompleteIcon1,mCompleteIcon2,mCompleteIcon3;
    /**
     * 正在进行的图标
     */
    private Drawable mAttentionIcon1,mAttentionIcon2,mAttentionIcon3,mAttentionIcon4,mAttentionIcon5,mAttentionIcon6;
    /**
     * 默认的图标
     */
    private Drawable mDefaultIcon1,mDefaultIcon2,mDefaultIcon3;
    /**
     * UP图标
     */
    private Drawable mUpIcon;
    /**
     * 图标中心点Y
     */
    private float mCenterY;
    /**
     * 线段的左上方的Y
     */
    private float mLeftY;
    /**
     * 线段的右下方的Y
     */
    private float mRightY;

    /**
     * 数据源
     */
    private List<StepBean> mStepBeanList;
    private int mStepNum = 0;

    /**
     * 图标中心点位置
     */
    private List<Float> mCircleCenterPointPositionList;
    /**
     * 未完成的线段Paint
     */
    private Paint mUnCompletedPaint;
    /**
     * 完成的线段paint
     */
    private Paint mCompletedPaint;
    /**
     * 未完成颜色
     */
    private int mUnCompletedLineColor = ContextCompat.getColor(getContext(), R.color.undone_segment);
    /**
     * 积分颜色
     */
    private int mUnCompletedTextColor = ContextCompat.getColor(getContext(), R.color.integral_color);

    /**
     * 天数颜色
     */
    private int mUnCompletedDayTextColor = ContextCompat.getColor(getContext(), R.color.number_of_color);

    /**
     * up魅力值颜色
     */
    private int mCurrentTextColor = ContextCompat.getColor(getContext(), R.color.white);
    /**
     * 完成的颜色
     */
    private int mCompletedLineColor = ContextCompat.getColor(getContext(), R.color.segment);

    private Paint mTextNumberPaint;

    private ValueAnimator valueAnimator;

    private Paint mTextDayPaint;

    /**
     * 是否执行动画
     */
    private boolean isAnimation = false;

    /**
     * 记录重绘次数
     */
    private int mCount = 0;

    /**
     * 执行动画线段每次绘制的长度，线段的总长度除以总共执行的时间乘以每次执行的间隔时间
     */
    private float mAnimationWidth = (mLineWidth / ANIMATION_TIME) * ANIMATION_INTERVAL;

    /**
     * 执行动画的位置
     */
    private int mPosition;
    private int[] mMax;
    private int widthMeasureSpec;

    public ActiveView(Context context) {
        this(context, null);
    }

    public ActiveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ActiveView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private boolean ISAnimator = false;
    private boolean ISAnimator2 = false;
    private boolean ISAnimator3 = false;
    public void setProgress(List<StepBean> stepsBeanList) {
        try {
            this.size = 2;
            mStepBeanList = stepsBeanList;
            mStepNum = mStepBeanList.size();
            setChange();//重新绘制
            valueAnimator = ValueAnimator.ofInt(0, 100000);
            valueAnimator.setDuration(50000000);
            valueAnimator.setRepeatMode(ValueAnimator.RESTART);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        postInvalidate();
                }
            });
            valueAnimator.start();
            size++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * init
     */
    private void init() {
        mStepBeanList = new ArrayList<>();
        mCircleCenterPointPositionList = new ArrayList<>();
        //未完成文字画笔
        mUnCompletedPaint = new Paint();
        mUnCompletedPaint.setAntiAlias(true);
        mUnCompletedPaint.setColor(mUnCompletedLineColor);
        mUnCompletedPaint.setStrokeWidth(2);
        mUnCompletedPaint.setStyle(Paint.Style.FILL);

        //已完成画笔文字
        mCompletedPaint = new Paint();
        mCompletedPaint.setAntiAlias(true);
        mCompletedPaint.setColor(mCompletedLineColor);
        mCompletedPaint.setStrokeWidth(2);
        mCompletedPaint.setStyle(Paint.Style.FILL);

        //number paint
        mTextNumberPaint = new Paint();
        mTextNumberPaint.setAntiAlias(true);
        mTextNumberPaint.setColor(mUnCompletedTextColor);
        mTextNumberPaint.setStyle(Paint.Style.FILL);
        mTextNumberPaint.setTextSize(SizeUtils.sp2px(getContext(), 10f));

        //number paint
        mTextDayPaint = new Paint();
        mTextDayPaint.setAntiAlias(true);
        mTextDayPaint.setColor(mUnCompletedDayTextColor);
        mTextDayPaint.setStyle(Paint.Style.FILL);
        mTextDayPaint.setTextSize(SizeUtils.sp2px(getContext(), 12f));

        //已经完成的icon
        mCompleteIcon1 = ContextCompat.getDrawable(getContext(), R.drawable.active_treasure_box_open1);
        mCompleteIcon2 = ContextCompat.getDrawable(getContext(), R.drawable.active_treasure_box_open2);
        mCompleteIcon3 = ContextCompat.getDrawable(getContext(), R.drawable.active_treasure_box_open3);
        //正在进行的icon
        mAttentionIcon1 = ContextCompat.getDrawable(getContext(), R.drawable.active__default1);
        mAttentionIcon2 = ContextCompat.getDrawable(getContext(), R.drawable.active__default2);
        mAttentionIcon3 = ContextCompat.getDrawable(getContext(), R.drawable.active__default3);
        mAttentionIcon4 = ContextCompat.getDrawable(getContext(), R.drawable.active__default4);
        mAttentionIcon5 = ContextCompat.getDrawable(getContext(), R.drawable.active__default5);
        mAttentionIcon6 = ContextCompat.getDrawable(getContext(), R.drawable.active__default6);
        //未完成的icon
        mDefaultIcon1 = ContextCompat.getDrawable(getContext(), R.drawable.active_treasure_box1);
        mDefaultIcon2 = ContextCompat.getDrawable(getContext(), R.drawable.active_treasure_box2);
        mDefaultIcon3 = ContextCompat.getDrawable(getContext(), R.drawable.active_treasure_box3);
        //UP的icon
        mUpIcon = ContextCompat.getDrawable(getContext(), R.drawable.jifendikuai);
        widthMeasureSpec = (int) ((getScreenWidth() - SizeUtils.dip2px(getContext(), 60f) - 3 * mIconWidth));
        mLineWidth = (widthMeasureSpec) / size;
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.widthMeasureSpec = (int) ((getScreenWidth() - SizeUtils.dip2px(getContext(), 60f) - 3 * mIconWidth));
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setChange();
    }

    private void setChange() {
        //图标的中中心Y点
        mCenterY = SizeUtils.dip2px(getContext(), 8f) + mIconHeight / 2;
        //获取左上方Y的位置，获取该点的意义是为了方便画矩形左上的Y位置
        mLeftY = mCenterY - (mCompletedLineHeight / 2);
        //获取右下方Y的位置，获取该点的意义是为了方便画矩形右下的Y位置
        mRightY = mCenterY + mCompletedLineHeight / 2;

        //计算图标中心点
        mCircleCenterPointPositionList.clear();
        //第一个点距离父控件左边14.5dp
        float size = mIconWidth / 2 + SizeUtils.dip2px(getContext(), 15f);
        mCircleCenterPointPositionList.add(size);

        for (int i = 1; i < mStepNum; i++) {
            //从第二个点开始，每个点距离上一个点为图标的宽度加上线段的23dp的长度
            size = size + mIconWidth + mLineWidth;
            mCircleCenterPointPositionList.add(size);
        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mStepBeanList.size() != 0) {
//            if (isAnimation) {
//                drawSign(canvas);
//            } else {
                drawUnSign(canvas);
//            }
        }
    }

    /**
     * 绘制签到(伴随签到动画)
     */
    @SuppressLint("DrawAllocation")
    private void drawSign(Canvas canvas) {
        for (int i = 0; i < mCircleCenterPointPositionList.size(); i++) {
            //绘制线段
            float preComplectedXPosition = mCircleCenterPointPositionList.get(i);
            if (i != mCircleCenterPointPositionList.size() - 1) {
                //最后一条不需要绘制
                if (mStepBeanList.get(i + 1).getState() == StepBean.STEP_COMPLETED) {
                    //下一个是已完成，当前才需要绘制
                    canvas.drawRect(preComplectedXPosition, mLeftY, preComplectedXPosition + mLineWidth + mIconWidth / 2,
                            mRightY, mCompletedPaint);
                } else {
                    //其余绘制灰色
                    //当前位置执行动画
                    if (i == mPosition - 1) {
                        //绿色开始绘制的地方,
                        float endX = preComplectedXPosition + mAnimationWidth * (mCount / ANIMATION_INTERVAL);
                        //绘制
                        canvas.drawRect(preComplectedXPosition, mLeftY, endX + mIconWidth / 2, mRightY, mCompletedPaint);
                        //绘制
                        canvas.drawRect(endX, mLeftY, preComplectedXPosition + mLineWidth + mIconWidth / 2,
                                mRightY, mUnCompletedPaint);
                    } else {
                        canvas.drawRect(preComplectedXPosition, mLeftY, preComplectedXPosition + mLineWidth + mIconWidth / 2,
                                mRightY, mUnCompletedPaint);
                    }
                }
            }

            //绘制图标
            float currentComplectedXPosition = mCircleCenterPointPositionList.get(i);
            Rect rect = new Rect((int) (currentComplectedXPosition - mIconWidth / 2),
                    (int) (mCenterY - mIconHeight / 2),
                    (int) (currentComplectedXPosition + mIconWidth / 2),
                    (int) (mCenterY + mIconHeight / 2));

            StepBean stepsBean = mStepBeanList.get(i);
            switch (i){
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
            }
//            if (i == mPosition && mCount == ANIMATION_TIME) {
//                //当前需要绘制
//                mCompleteIcon.setBounds(rect);
//                mCompleteIcon.draw(canvas);
//            } else {
//                if (stepsBean.getState() == StepBean.STEP_UNDO) {
//                    mDefaultIcon.setBounds(rect);
//                    mDefaultIcon.draw(canvas);
//                } else if (stepsBean.getState() == StepBean.STEP_CURRENT) {
//                    mAttentionIcon.setBounds(rect);
//                    mAttentionIcon.draw(canvas);
//                } else if (stepsBean.getState() == StepBean.STEP_COMPLETED) {
//                    mCompleteIcon.setBounds(rect);
//                    mCompleteIcon.draw(canvas);
//                }
//            }

            //绘制图标
            if (stepsBean.getState() == StepBean.STEP_COMPLETED || (i == mPosition
                    && mCount == ANIMATION_TIME)) {
                //已经完成了或者是当前动画完成并且需要当前位置需要改变
                if (stepsBean.getNumber() != 0) {
                    //是up的需要橙色
                    mTextNumberPaint.setColor(mCurrentTextColor);
                } else {
                    //普通完成的颜色
                    mTextNumberPaint.setColor(mCompletedLineColor);
                }
            } else {
                //还没签到的，颜色均为灰色
                mTextNumberPaint.setColor(mUnCompletedLineColor);
            }

//            //绘制UP
//            if (stepsBean.getNumber() != 0) {
//                //需要UP才进行绘制
//                Rect rectUp =
//                        new Rect((int) (currentComplectedXPosition - mUpWidth / 2),
//                                (int) (mCenterY - mIconHeight / 2 - SizeUtils.dip2px(getContext(), 8f) - mUpHeight),
//                                (int) (currentComplectedXPosition + mUpWidth / 2),
//                                (int) (mCenterY - mIconHeight / 2 - SizeUtils.dip2px(getContext(), 1f)));
//                mUpIcon.setBounds(rectUp);
//                mUpIcon.draw(canvas);
//            }

            //0表示不需要显示积分，非0表示需要消失积分
            if (stepsBean.getNumber() != 0) {
                canvas.drawText("+" + stepsBean.getNumber(),
                        currentComplectedXPosition - mIconWidth / 2,
                        mCenterY / 2 - SizeUtils.dip2px(getContext(), 0.5f),
                        mTextNumberPaint);
            }
            //天数文字
            canvas.drawText(stepsBean.getDay(),
                    currentComplectedXPosition - mIconWidth / 2 + SizeUtils.dip2px(getContext(), 5f),
                    mCenterY + SizeUtils.dip2px(getContext(), 30f),
                    mTextDayPaint);
        }

        //记录重绘次数
        mCount = mCount + ANIMATION_INTERVAL;
        if (mCount <= ANIMATION_TIME) {
            //引起重绘
            postInvalidate();
        } else {
            //重绘完成
            isAnimation = false;
            mCount = 0;
        }
    }

    private int size = 2;

    public void setSize(int size) {
        this.size = size;
    }

    /**
     * 绘制初始状态的view
     */
    @SuppressLint("DrawAllocation")
    private void drawUnSign(Canvas canvas) {
        if (widthMeasureSpec > 0) {
            mLineWidth = (widthMeasureSpec) / size;
        } else {
            widthMeasureSpec = (int) ((getScreenWidth() - SizeUtils.dip2px(getContext(), 60f) - 3 * mIconWidth));
            mLineWidth = (widthMeasureSpec) / size;
        }
        Log.e("drawSign", "mLineWidth:111 " + mLineWidth);
        for (int i = 0; i < mCircleCenterPointPositionList.size(); i++) {
            //绘制线段
            float preComplectedXPosition = mCircleCenterPointPositionList.get(i) + mIconWidth / 2;
            if (i != mCircleCenterPointPositionList.size() - 1) {
                //最后一条不需要绘制
                if (mStepBeanList.get(i + 1).getState() == StepBean.STEP_COMPLETED || mStepBeanList.get(i + 1).getState() == StepBean.STEP_CURRENT) {
                    //下一个是已完成，当前才需要绘制
                    canvas.drawRect(preComplectedXPosition, mLeftY, preComplectedXPosition + mLineWidth,
                            mRightY, mCompletedPaint);
                } else {
                    //其余绘制灰色
                    canvas.drawRect(preComplectedXPosition, mLeftY, preComplectedXPosition + mLineWidth,
                            mRightY, mUnCompletedPaint);
                }
            }

            //绘制图标
            float currentComplectedXPosition = mCircleCenterPointPositionList.get(i);
            Rect rect = new Rect((int) (currentComplectedXPosition - mIconWidth / 2),
                    (int) (mCenterY - mIconHeight / 2),
                    (int) (currentComplectedXPosition + mIconWidth / 2),
                    (int) (mCenterY + mIconHeight / 2));

            StepBean stepsBean = mStepBeanList.get(i);
            switch (i){
                case 0:
                    if (stepsBean.getState() == StepBean.STEP_UNDO) {
                        mDefaultIcon1.setBounds(rect);
                        mDefaultIcon1.draw(canvas);
                    } else if (stepsBean.getState() == StepBean.STEP_CURRENT) {
                        if (ISAnimator2) {
                            ISAnimator2 = false;
                            mAttentionIcon1.setBounds(rect);
                            mAttentionIcon1.draw(canvas);
                        } else {
                            ISAnimator2 = true;
                            mAttentionIcon2.setBounds(rect);
                            mAttentionIcon2.draw(canvas);
                        }

                    } else if (stepsBean.getState() == StepBean.STEP_COMPLETED) {
                        mCompleteIcon1.setBounds(rect);
                        mCompleteIcon1.draw(canvas);
                    }
                    break;
                case 1:
                    if (stepsBean.getState() == StepBean.STEP_UNDO) {
                        mDefaultIcon2.setBounds(rect);
                        mDefaultIcon2.draw(canvas);
                    } else if (stepsBean.getState() == StepBean.STEP_CURRENT) {
                        if (ISAnimator3) {
                            ISAnimator3 = false;
                            mAttentionIcon3.setBounds(rect);
                            mAttentionIcon3.draw(canvas);
                        } else {
                            ISAnimator3 = true;
                            mAttentionIcon4.setBounds(rect);
                            mAttentionIcon4.draw(canvas);
                        }

                    } else if (stepsBean.getState() == StepBean.STEP_COMPLETED) {
                        mCompleteIcon2.setBounds(rect);
                        mCompleteIcon2.draw(canvas);
                    }
                    break;
                case 2:
                    if (stepsBean.getState() == StepBean.STEP_UNDO) {
                        mDefaultIcon3.setBounds(rect);
                        mDefaultIcon3.draw(canvas);
                    } else if (stepsBean.getState() == StepBean.STEP_CURRENT) {
                        if (ISAnimator) {
                            ISAnimator = false;
                            mAttentionIcon5.setBounds(rect);
                            mAttentionIcon5.draw(canvas);
                        } else {
                            ISAnimator = true;
                            mAttentionIcon6.setBounds(rect);
                            mAttentionIcon6.draw(canvas);
                        }

                    } else if (stepsBean.getState() == StepBean.STEP_COMPLETED) {
                        mCompleteIcon3.setBounds(rect);
                        mCompleteIcon3.draw(canvas);
                    }
                    break;
            }


            //绘制增加的分数数目
            if (stepsBean.getState() == StepBean.STEP_COMPLETED) {
                //已经完成了
                if (stepsBean.getNumber() != 0) {
                    //是up的需要橙色
                    mTextNumberPaint.setColor(mCurrentTextColor);
                } else {
                    //普通完成的颜色
                    mTextNumberPaint.setColor(mCompletedLineColor);
                }
            } else {
                //还没签到的，颜色均为灰色
                mTextNumberPaint.setColor(mUnCompletedLineColor);
            }

//            //绘制UP
//            if (stepsBean.getNumber() != 0) {
//                //需要UP才进行绘制
//                Rect rectUp =
//                        new Rect((int) (currentComplectedXPosition - mUpWidth / 2),
//                                (int) (mCenterY - mIconHeight / 2 - SizeUtils.dip2px(getContext(), 8f) - mUpHeight),
//                                (int) (currentComplectedXPosition + mUpWidth / 2),
//                                (int) (mCenterY - mIconHeight / 2 - SizeUtils.dip2px(getContext(), 1f)));
//                mUpIcon.setBounds(rectUp);
//                mUpIcon.draw(canvas);
//            }

            //0表示不需要显示积分，非0表示需要消失积分
            if (stepsBean.getNumber() != 0) {
                //积分文字
                canvas.drawText("+" + stepsBean.getNumber(),
                        currentComplectedXPosition - mIconWidth / 2,
                        mCenterY / 2 - SizeUtils.dip2px(getContext(), 0.5f),
                        mTextNumberPaint);
            }
            //天数文字
            canvas.drawText(stepsBean.getDay(),
                    currentComplectedXPosition - mIconWidth / 2 + SizeUtils.dip2px(getContext(), 5f),
                    mCenterY + SizeUtils.dip2px(getContext(), 30f),
                    mTextDayPaint);
        }
    }

    /**
     * 设置流程步数
     *
     * @param stepsBeanList 流程步数
     */
    public void setStepNum(List<StepBean> stepsBeanList) {
        if (stepsBeanList == null && stepsBeanList.size() == 0) {
            return;
        }
        mStepBeanList = stepsBeanList;
        mStepNum = mStepBeanList.size();
        setChange();//重新绘制
        //引起重绘
        postInvalidate();
    }

    /**
     * 执行签到动画
     *
     * @param position 执行的位置
     */
    public void startSignAnimation(int position) {
        //线条从灰色变为绿色
        isAnimation = true;
        mPosition = position;
        //引起重绘
        postInvalidate();
    }
}