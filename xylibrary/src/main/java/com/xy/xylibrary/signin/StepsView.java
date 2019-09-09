package com.xy.xylibrary.signin;

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

import com.constellation.xylibrary.R;
import com.xy.xylibrary.utils.SizeUtils;
import com.xy.xylibrary.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.xy.xylibrary.utils.DensityUtil.getScreenWidth;

/**
 * description: 自定义签到View.
 */
public class StepsView extends View {

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
    private Drawable mCompleteIcon;
    /**
     * 正在进行的图标
     */
    private Drawable mAttentionIcon;
    /**
     * 默认的图标
     */
    private Drawable mDefaultIcon;
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

    public StepsView(Context context) {
        this(context, null);
    }

    public StepsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
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
        mCompleteIcon = ContextCompat.getDrawable(getContext(), R.drawable.sign_in_activate);
        //正在进行的icon
        mAttentionIcon = ContextCompat.getDrawable(getContext(), R.drawable.sign_in_default);
        //未完成的icon
        mDefaultIcon = ContextCompat.getDrawable(getContext(), R.drawable.sign_in_default);
        //UP的icon
        mUpIcon = ContextCompat.getDrawable(getContext(), R.drawable.jifendikuai);

    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.widthMeasureSpec = 11 * getScreenWidth() / 12;
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setChange();
    }

    private void setChange() {
        //图标的中中心Y点
        mCenterY = SizeUtils.dip2px(getContext(), 28f) + mIconHeight / 2;
        //获取左上方Y的位置，获取该点的意义是为了方便画矩形左上的Y位置
        mLeftY = mCenterY - (mCompletedLineHeight / 2);
        //获取右下方Y的位置，获取该点的意义是为了方便画矩形右下的Y位置
        mRightY = mCenterY + mCompletedLineHeight / 2;

        //计算图标中心点
        mCircleCenterPointPositionList.clear();
        //第一个点距离父控件左边14.5dp
        float size = mIconWidth / 2 + SizeUtils.dip2px(getContext(), 8f);
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
            if (isAnimation) {
                drawSign(canvas);
            } else {
                drawUnSign(canvas);
            }
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

            if (i == mPosition && mCount == ANIMATION_TIME) {
                //当前需要绘制
                mCompleteIcon.setBounds(rect);
                mCompleteIcon.draw(canvas);
            } else {
                if (stepsBean.getState() == StepBean.STEP_UNDO) {
                    mDefaultIcon.setBounds(rect);
                    mDefaultIcon.draw(canvas);
                } else if (stepsBean.getState() == StepBean.STEP_CURRENT) {
                    mAttentionIcon.setBounds(rect);
                    mAttentionIcon.draw(canvas);
                } else if (stepsBean.getState() == StepBean.STEP_COMPLETED) {
                    mCompleteIcon.setBounds(rect);
                    mCompleteIcon.draw(canvas);
                }
            }

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

    /**
     * 绘制初始状态的view
     */
    @SuppressLint("DrawAllocation")
    private void drawUnSign(Canvas canvas) {
        Log.e("drawSign", "drawSign: "+widthMeasureSpec );
        if(widthMeasureSpec > 0){
            mLineWidth = (widthMeasureSpec - (7*mIconWidth))/7;
        }
        for (int i = 0; i < mCircleCenterPointPositionList.size(); i++) {
            //绘制线段
            float preComplectedXPosition = mCircleCenterPointPositionList.get(i) + mIconWidth / 2;
            if (i != mCircleCenterPointPositionList.size() - 1) {
                //最后一条不需要绘制
                if (mStepBeanList.get(i + 1).getState() == StepBean.STEP_COMPLETED) {
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

            if (stepsBean.getState() == StepBean.STEP_UNDO) {
                mDefaultIcon.setBounds(rect);
                mDefaultIcon.draw(canvas);
            } else if (stepsBean.getState() == StepBean.STEP_CURRENT) {
                mAttentionIcon.setBounds(rect);
                mAttentionIcon.draw(canvas);
            } else if (stepsBean.getState() == StepBean.STEP_COMPLETED) {
                mCompleteIcon.setBounds(rect);
                mCompleteIcon.draw(canvas);
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