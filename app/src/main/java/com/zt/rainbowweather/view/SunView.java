package com.zt.rainbowweather.view;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.zt.weather.R;

import java.util.Calendar;


/**
 * function：仿小米天气 日出日落动画控件
 * creator：hw
 * time: 2018/08/20 15:21
 */
public class SunView extends View {
    Paint mPathPaint;
    private int mWidth;
    private int mHeight;
    int mainColor;
    int trackColor;
    private Path mPathPath;
    private Paint mMotionPaint;
    private Paint TrackPaint;
    private Path mMotionPath;
    int controlX, controlY;
    float startX, startY;
    float endX, endY;
    private double rX;
    private double rY;
    private int[] mSunrise = new int[2];
    private int[] mSunset = new int[2];
    private Paint mSunPaint;
    private ValueAnimator valueAnimator;
    private float mProgress;
    private Paint mShadePaint;
    private Shader mPathShader;
    private Path path;
    private Paint paint;
    private float mCurrentProgress;
    private boolean isDraw = false;
    private DashPathEffect mDashPathEffect;
    private Paint mTextPaint;
    private LinearGradient mBackgroundShader;
    private int sunColor;
    private Paint mSunStrokePaint;
    private float svSunSize;
    private float svTextSize;
    private float textOffset;
    private float svPadding;
    private float svTrackWidth;
    private Bitmap mSunIcon; //太阳图片
    private Bitmap mSunupIcon;
    private Bitmap mSunsetIcon;

    private float mAngle;
    public SunView(Context context) {
        super(context);
        init(null);
    }

    public SunView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SunView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SunView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        //初始化属性
        final Context context = getContext();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SunView);
        // FIXME 这个地方如果xml属性不给值则拿不到默认值
        mainColor = array.getColor(R.styleable.SunView_svMainColor, 0x67B2FD);
        trackColor = array.getColor(R.styleable.SunView_svTrackColor, 0x67B2FD);
        sunColor = array.getColor(R.styleable.SunView_svSunColor, 0x00D3FE);
        svSunSize = array.getDimension(R.styleable.SunView_svSunRadius, 10);
        svTextSize = array.getDimension(R.styleable.SunView_svTextSize, 18);
        textOffset = array.getDimension(R.styleable.SunView_svTextOffset, 10);
        svPadding = array.getDimension(R.styleable.SunView_svPadding, 10);
        svTrackWidth = array.getDimension(R.styleable.SunView_svTrackWidth, 3);
        mSunIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_sun);
        mSunupIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.sunrise_icon);
        mSunsetIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.sunset_icon);
        array.recycle();

        // 渐变路径的画笔
        Paint pathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pathPaint.setColor(mainColor);
        pathPaint.setStyle(Paint.Style.FILL);
        mPathPaint = pathPaint;
        // 渐变路径
        mPathPath = new Path();
        // 渐变遮罩的画笔
        Paint shadePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        shadePaint.setColor(Color.parseColor("#00000000"));
        shadePaint.setStyle(Paint.Style.FILL);
        mShadePaint = shadePaint;
        // 运动轨迹画笔
        Paint motionPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        motionPaint.setColor(trackColor);
        motionPaint.setStrokeCap(Paint.Cap.ROUND);
        motionPaint.setStrokeWidth(svTrackWidth);
        motionPaint.setStyle(Paint.Style.STROKE);
        mMotionPaint = motionPaint;
        TrackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        TrackPaint.setColor(Color.parseColor("#9f000000"));
        TrackPaint.setStrokeCap(Paint.Cap.ROUND);
        TrackPaint.setStrokeWidth(3);
        TrackPaint.setStyle(Paint.Style.FILL);
        // 运动轨迹
        mMotionPath = new Path();
        // 太阳画笔
        Paint sunPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        sunPaint.setColor(sunColor);
        sunPaint.setStyle(Paint.Style.FILL);
        mSunPaint = sunPaint;
        // 太阳边框画笔
        Paint sunStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        sunStrokePaint.setColor(Color.WHITE);
        sunStrokePaint.setStyle(Paint.Style.FILL);
        mSunStrokePaint = sunStrokePaint;
        // 日出日落时间画笔
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(trackColor);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(svTextSize);
        mTextPaint = textPaint;
        mDashPathEffect = new DashPathEffect(new float[]{6, 12}, 0);
        path = new Path();
        paint = new Paint();
        //设置画笔颜色
        paint.setColor(Color.WHITE);
        //STROKE                //描边
        //FILL                  //填充
        //FILL_AND_STROKE       //描边加填充
        //设置画笔模式
        paint.setStyle(Paint.Style.STROKE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        if(!isDraw){
            mWidth = getWidth();
            mHeight = getHeight();
            controlX = mWidth/2;
            controlY = 0-mHeight/2;
            startX = svPadding;
            startY = mHeight-svPadding;
            endX = mWidth-svPadding;
            endY = mHeight-svPadding;
            rX = svPadding;
            rY = mHeight-svPadding;
//            // 渐变路径
//            mPathShader = new LinearGradient(mWidth/2, svPadding, mWidth/2, endY,
//                    mainColor, Color.WHITE, Shader.TileMode.CLAMP);
//            mPathPaint.setShader(mPathShader);
            mPathPath.moveTo(startX, startY);
            mPathPath.quadTo(controlX, controlY, endX, endY);
            // 运动轨迹
            mMotionPath.moveTo(startX, startY);
            mMotionPath.quadTo(controlX, controlY, endX, endY);
            //        // 画一条虚线表示未运动到的轨迹
            isDraw = true;
        }

        // 按遮挡关系画
        // 画渐变
//        canvas.drawPath(mPathPath, mPathPaint);
        // 画已经运动过去的轨迹
        mMotionPaint.setStyle(Paint.Style.STROKE);
        mMotionPaint.setPathEffect(null);
        canvas.drawPath(mMotionPath, mMotionPaint);
        // 画一个矩形遮住未运动到的渐变和轨迹
        mShadePaint.setShader(mBackgroundShader);
//        canvas.drawRect((float) rX, 0, mWidth, mHeight, mShadePaint);

        // 画一条虚线表示未运动到的轨迹
//        mMotionPaint.setPathEffect(mDashPathEffect);
//        canvas.drawPath(mMotionPath, mMotionPaint);

        // 画日出日落文字
        if (mSunrise.length != 0||mSunset.length != 0){
            mTextPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(" "+(mSunrise[0]<10? "0"+mSunrise[0]: mSunrise[0])
                    +":"+(mSunrise[1]<10? "0"+mSunrise[1]: mSunrise[1]), startX+textOffset+mSunupIcon.getWidth(), startY + mSunupIcon.getWidth()/3, mTextPaint);
            mTextPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(" "+(mSunset[0]<10? "0"+mSunset[0]: mSunset[0])
                    +":"+(mSunset[1]<10? "0"+mSunset[1]: mSunset[1]), endX-textOffset, endY+ mSunsetIcon.getHeight()/2, mTextPaint);
            canvas.drawBitmap(mSunupIcon, (float) startX + mSunsetIcon.getWidth(), (float)startY-mSunupIcon.getHeight()/2, mSunPaint);
            canvas.drawBitmap(mSunsetIcon, (float) endX - mSunsetIcon.getWidth()*3/2- textOffset*2, (float)endY-mSunsetIcon.getHeight()/2, mSunPaint);
        }

        // 画端点
        mMotionPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(startX, startY, svTrackWidth*2, mMotionPaint);
        canvas.drawCircle(endX, endY, svTrackWidth*2, mMotionPaint);
        paint.setStrokeWidth(5);
        // 运动轨迹
//        path.moveTo((float) rX-10, (float)rY+3);
//        path.quadTo((float) rX, (float)rY ,(float) rX, (float)rY);
//        paint.setPathEffect(null);
//        canvas.drawPath(path, paint);
        RectF oval = new RectF((float) rX, (float)rY, mWidth, mHeight);
        mShadePaint.setColor(Color.parseColor("#ffff00"));
        //画透明背景用圆的角度来控制
//        canvas.clipRect(startX, 0,(float) rX, (float)rY, Region.Op.INTERSECT);
//        canvas.drawArc(oval, 200, mAngle, true, mShadePaint);
//        canvas.drawArc(oval,200,mAngle,true,mShadePaint);//画圆弧，这个时候，绘制没有经过圆心
        // 画太阳
//        canvas.drawCircle((float) rX, (float)rY, svSunSize*6/5, mSunStrokePaint);
//        canvas.drawCircle((float) rX, (float)rY, svSunSize, mSunPaint);
        SetArc(canvas);
        canvas.drawBitmap(mSunIcon, (float) rX-mSunIcon.getHeight()/2, (float)rY-mSunIcon.getHeight()/2, mSunPaint);
        canvas.restore();
    }

    /**
    * 圆弧
    * */
    private void SetArc(Canvas canvas){
        double rXs;
        double rYs;
        for (int i = 0; i < ((1-mProgress)/0.01); i++) {
            rXs = startX * Math.pow(1 - (mProgress + i * 0.01), 2) + 2 * controlX * (mProgress + i * 0.01) * (1 - (mProgress + i * 0.01)) + endX * Math.pow((mProgress + i * 0.01), 2);
            rYs = startY * Math.pow(1 - (mProgress + i * 0.01), 2) + 2 * controlY * (mProgress + i * 0.01) * (1 - (mProgress + i * 0.01)) + endY * Math.pow((mProgress + i * 0.01), 2);
            canvas.drawCircle((float)rXs, (float)rYs, 3, TrackPaint);
        }
    }

    /**
     * 设置当前进度，并更新太阳中心点的位置
     * @param t 范围：[0~1]
     */
    private void setProgress(float t){
        mProgress = t;
        rX = startX * Math.pow(1 - t, 2) + 2 * controlX * t * (1 - t) + endX * Math.pow(t, 2);
        rY = startY * Math.pow(1 - t, 2) + 2 * controlY * t * (1 - t) + endY * Math.pow(t, 2);
        // 只更新需要画的区域
        invalidate((int)rX, 0, (int)(mWidth-svPadding), (int)(mHeight-svPadding));
        invalidate();
    }

    /**
     * 设置当前时间(请先设置日出日落时间)
     */
    public void setCurrentTime(int hour, int minute){
        if (mSunrise.length != 0||mSunset.length != 0){
            float p0 = mSunrise[0]*60+mSunrise[1];// 起始分钟数
            float p1 = hour*60+minute-p0;// 当前时间总分钟数
            float p2 = mSunset[0]*60+mSunset[1]-p0;// 日落到日出总分钟数
            float progress = p1/p2;
            mProgress = progress;
            motionAnimation();
        }
    }

    /**
     * 设置日出时间
     */
    public void setSunrise(int hour, int minute){
        mSunrise[0] = hour;
        mSunrise[1] = minute;
    }

    /**
     * 设置日落时间
     */
    public void setSunset(int hour, int minute){
        mSunset[0] = hour;
        mSunset[1] = minute;
    }

    /**
     * 太阳轨迹动画
     */
    public void motionAnimation(){
        if (valueAnimator == null){
            mCurrentProgress = 0f;
            // 确保太阳不会出界
            if (mProgress<0){
                mProgress=0;
            }
            if (mProgress>1){
                mProgress=1;
            }
            final ValueAnimator animator = ValueAnimator.ofFloat(mCurrentProgress, mProgress);
            animator.setDuration((long) (2500*(mProgress-mCurrentProgress)));
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    //每次要绘制的圆弧角度
                    mAngle = (float) animator.getAnimatedValue();
                    Object val = animator.getAnimatedValue();
                    if (val instanceof Float){
                        setProgress((Float) val);
                    }
                }
            });
            valueAnimator = animator;
        } else {
            valueAnimator.cancel();
            valueAnimator.setFloatValues(mCurrentProgress, mProgress);
        }
        valueAnimator.start();
        // 保存当前的进度，下一次调用setCurrentTime()即可以从上次进度运动到当前进度(小米效果)
        mCurrentProgress = mProgress;
    }
}
