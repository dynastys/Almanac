package com.xy.xylibrary.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义返回banner
 * */
public class MagicButton extends View {

    // 八个点，用于绘制线段
    private PointF mPoint0;
    private PointF mPoint1;
    private PointF mPoint2;
    private PointF mPoint3;
    private PointF mPoint4;
    private PointF mPoint5;
    private PointF mPoint6;
    private PointF mPoint7;

    // 箭头到对勾对应的四个点
    private PointF mArrawToTickPoint1, mArrawToTickPoint2, mArrawToTickPoint3, mArrawToTickPoint4;

    // 对勾到箭头对应的四个点
    private PointF mTickToArrawPoint1, mTickToArrawPoint2, mTickToArrawPoint3, mTickToArrawPoint4;

    // 角度，对应于位移偏差，用于动态改变线段的落点
    private float mProgress = 0;

    // view的宽高
    private int mViewWidth;
    private int mViewHeight;

    // view的中心
    private int mViewCenterX;
    private int mViewCenterY;

    // 半径
    private int mRadius;

    // 是否绘制箭头标志位
    private boolean mIsDrawArrow = true;

    /**
     * 画实心圆
     */
    private Paint mCirclePaint;

    /**
     * 画符号
     */
    private Paint mSymbolPaint;

    public MagicButton(Context context, int color) {
        super(context);
        init(color);
    }

    public MagicButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(Color.parseColor("#ffffff"));
    }

    private void init(int color) {
        // 画背景圆
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(Color.parseColor("#4f999999"));

        // 画符号#ffa9a9a9
        mSymbolPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSymbolPaint.setColor(color);
        mSymbolPaint.setStyle(Paint.Style.STROKE);
        mSymbolPaint.setStrokeCap(Paint.Cap.ROUND);
        mSymbolPaint.setStrokeJoin(Paint.Join.ROUND);
    }

    public MagicButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(Color.parseColor("#ffffff"));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MagicButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(Color.parseColor("#ffffff"));
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mViewWidth = measureWidth(widthMeasureSpec);
        mViewHeight = measureHeight(heightMeasureSpec);
        mViewCenterX = mViewWidth / 2;
        mViewCenterY = mViewHeight / 2;
        mRadius = mViewCenterX <= mViewCenterY ? mViewCenterX : mViewCenterY;
        mSymbolPaint.setStrokeWidth(mRadius * 2 / 18);
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (mViewWidth != 0) {
            // 为八个点定坐标
            mPoint0 = new PointF(mRadius / 2, mRadius / 2);
            mPoint1 = new PointF(mRadius / 2 * 3, mRadius / 2);
            mPoint2 = new PointF(mRadius / 2, mRadius / 2 * 2);
            mPoint3 = new PointF(mRadius / 2 * 3, mRadius / 2 * 2);
            mPoint4 = new PointF(mRadius / 2, mRadius / 2 * 3);
            mPoint5 = new PointF(mRadius / 2 * 3, mRadius / 2 * 3);
            mPoint6 = new PointF(mRadius / 2 * 2, mRadius / 2);
            mPoint7 = new PointF(mRadius / 2 * 2, mRadius / 2 * 3);
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = 500;
        if (specMode == MeasureSpec.AT_MOST) {
            result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        return result;
    }


    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = 500;
        if (specMode == MeasureSpec.AT_MOST) {
            result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 先绘制背景
        canvas.drawCircle(mViewCenterX, mViewCenterY, mRadius, mCirclePaint);
        if (mIsDrawArrow) {
            if (mTickToArrawPoint1 == null) {
                // 刚开始直接绘制箭头
                drawLine(canvas, mPoint2, mPoint3);
                drawLine(canvas, mPoint2, mPoint6);
                drawLine(canvas, mPoint2, mPoint7);
            } else {
                // 偏移过程绘制对勾到箭头过程
                drawLine(canvas, mPoint2, mTickToArrawPoint1);
                drawLine(canvas, mPoint7, mTickToArrawPoint2);
                drawLine(canvas, mTickToArrawPoint3, mTickToArrawPoint4);
            }
        } else {
            // 偏移过程绘制箭头到对勾过程
            drawLine(canvas, mPoint2, mArrawToTickPoint1);
            drawLine(canvas, mPoint7, mArrawToTickPoint2);
            drawLine(canvas, mArrawToTickPoint3, mArrawToTickPoint4);
        }

    }

    // 计算对勾到箭头过程对应落点的坐标值
    private void tickToArraw() {

        mProgress = 1 - mProgress;

        double length = Math.sqrt(2) / 2 * mRadius - mProgress * Math.sqrt(2) / 2 * mRadius;

        mTickToArrawPoint1 = new PointF((float) (mRadius + (mRadius / 2 - length * Math.sqrt(2) / 2)), (float) (mRadius * 3 / 2 - (mRadius / 2 - length * Math.sqrt(2) / 2)));

        mTickToArrawPoint2 = new PointF((float) (mRadius * 5 / 4 + mRadius / 4 * mProgress), (float) mRadius);

        mTickToArrawPoint3 = new PointF((float) (mRadius * 3 / 2 - mRadius / 2 * mProgress), (float) mRadius / 2);

        mTickToArrawPoint4 = new PointF((float) (mRadius * 5 / 4 + mRadius / 4 * mProgress), (float) mRadius);


    }

    // 计算箭头到对勾过程对应落点的坐标值
    private void arrawToTick() {

        double length = mProgress * Math.sqrt(2) / 2 * mRadius;

        mArrawToTickPoint1 = new PointF((float) (mRadius * 3 / 2 - length * Math.sqrt(2) / 2), (float) (mRadius + length * Math.sqrt(2) / 2));

        mArrawToTickPoint2 = new PointF((float) (mRadius * 3 / 2 - mRadius / 4 * mProgress), (float) mRadius);

        mArrawToTickPoint3 = new PointF((float) (mRadius + mRadius / 2 * mProgress), (float) mRadius / 2);

        mArrawToTickPoint4 = new PointF((float) (mRadius * 3 / 2 - mRadius / 4 * mProgress), (float) mRadius);

    }

    /**
     * 设置button图标变化的进度
     *
     * @param isDrawArrow 绘制监听
     * @param progress    进度
     */
    public void setProgress(boolean isDrawArrow, float progress) {
        mIsDrawArrow = isDrawArrow;
        mProgress = progress;
        if (isDrawArrow) {
            tickToArraw();
        } else {
            arrawToTick();
        }
        invalidate();
    }

    public void setBgLucency(){
        mCirclePaint.setColor(Color.parseColor("#00000000"));
        invalidate();
    }
    public void setBgRestore(){
        mCirclePaint.setColor(Color.parseColor("#4f999999"));
        invalidate();
    }


    // 绘制线段
    private void drawLine(Canvas canvas, PointF start, PointF end) {
        if (start.x != 0 && end.x != 0) {
            canvas.drawLine(start.x, start.y, end.x, end.y, mSymbolPaint);
        }
    }
}
