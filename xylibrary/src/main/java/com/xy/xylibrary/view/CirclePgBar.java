package com.xy.xylibrary.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.constellation.xylibrary.R;
import com.xy.xylibrary.base.AppContext;
import com.xy.xylibrary.signin.FinishTask;
import com.xy.xylibrary.ui.activity.login.LoginRequest;
import com.xy.xylibrary.ui.activity.login.RequestSyntony;
import com.xy.xylibrary.ui.fragment.task.TaskType;
import com.xy.xylibrary.utils.SaveShare;
import com.xy.xylibrary.utils.ToastUtils;

import org.litepal.LitePal;

/**
 * Created by yang_zzheng on 2016/7/12
 * yangzhizheng2012@163.com
 */
public class CirclePgBar extends View {

    // 画实心圆的画笔
    private Paint mCirclePaint;
    // 画圆环的画笔
    private Paint mRingPaint;
    //画阴影
    private Paint shadowPaint;
    // 画字体的画笔
    private Paint mTextPaint;
    // 圆形颜色
    private int mCircleColor;
    // 圆环颜色
    private int mRingColor;
    // 半径
    private float mRadius;
    // 圆环半径
    private float mRingRadius;
    // 圆环宽度
    private float mStrokeWidth;
    // 圆心x坐标
    private int mXCenter;
    // 圆心y坐标
    private int mYCenter;
    // 字的长度
    private float mTxtWidth;
    // 字的高度
    private float mTxtHeight;
    // 总进度
    private int mTotalProgress = 100;
    // 当前进度
    private int mProgress;
    private int time = 8000;//每次进度的时间
    private ValueAnimator valueAnimator;
    private int fixed = 72;//固定值
    private int size;// 次数
    private boolean ISAnimator = true;//限制动画
    private Context context;
    private Bitmap mIcon; //中心图片
    private Paint mCentrePaint;
    private Bitmap lightBeam,lightBeam1,lightBeam2;
    private CircleSyntony circleSyntony;
    private boolean ISlightBeam = false;
//    private String ReadTask;//任务id
    private TaskType taskType; //任务详情

    public interface CircleSyntony{
        void CompleteSyntony();
    }

   public void setCircleSyntony(CircleSyntony circleSyntony){
        this.circleSyntony = circleSyntony;
   }
    public CirclePgBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
//        ReadTask = SaveShare.getValue(context,"TaskId");
        // 获取自定义的属性
        initAttrs(context, attrs);
        initVariable();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.TasksCompletedView, 0, 0);
        mRadius = typeArray.getDimension(R.styleable.TasksCompletedView_radius, 80);
        mStrokeWidth = typeArray.getDimension(R.styleable.TasksCompletedView_strokeWidth, 10);
        mCircleColor = typeArray.getColor(R.styleable.TasksCompletedView_circleColor, 0xFFFFFFFF);
        mRingColor = typeArray.getColor(R.styleable.TasksCompletedView_ringColor, 0xFFFFFFFF);
        mRingRadius = mRadius - mStrokeWidth - mStrokeWidth/2 ;
        lightBeam1 = BitmapFactory.decodeResource(getResources(), R.drawable.light_beam);
        lightBeam2 = BitmapFactory.decodeResource(getResources(), R.drawable.light_beam2);
    }

    private void initVariable() {
        taskType = LitePal.where("tasktype = ?","1").findFirst(TaskType.class);
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(getResources().getColor(R.color.white));
        mCirclePaint.setStyle(Paint.Style.FILL);

        mRingPaint = new Paint();
        mRingPaint.setAntiAlias(true);
        mRingPaint.setColor(getResources().getColor(R.color.broken_line_x));
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setStrokeWidth(mStrokeWidth);

        shadowPaint = new Paint();
        shadowPaint.setAntiAlias(true);
        shadowPaint.setColor(getResources().getColor(R.color.white_A30));
        shadowPaint.setStyle(Paint.Style.STROKE);
        shadowPaint.setStrokeWidth(mStrokeWidth/3*2);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setARGB(255, 255, 255, 255);
        mTextPaint.setTextSize(mRadius / 2);


        mCentrePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCentrePaint.setColor(getResources().getColor(R.color.broken_line_s));
        mCentrePaint.setStyle(Paint.Style.FILL);

        FontMetrics fm = mTextPaint.getFontMetrics();
        mTxtHeight = (int) Math.ceil(fm.descent - fm.ascent);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        try {
            mXCenter = getWidth() / 2;
            mYCenter = getHeight() / 2;
            canvas.drawCircle(mXCenter, mYCenter, mRadius, mCirclePaint);
            mIcon = BitmapFactory.decodeResource(getResources(), R.drawable.treasure_box);
            if(ISlightBeam){
                ISlightBeam = false;
                lightBeam = lightBeam1;
            }else{
                ISlightBeam = true;
                lightBeam = lightBeam2;
            }
            if(lightBeam != null){
                canvas.drawBitmap(lightBeam, (float) mXCenter - lightBeam.getWidth()/ 2, (float) mYCenter - lightBeam.getHeight()/ 2, mCentrePaint);
            }
            if(mIcon != null){
                canvas.drawBitmap(mIcon, (float) mXCenter - mIcon.getWidth()/ 2, (float) mYCenter - mIcon.getHeight()/ 2, mCentrePaint);
            }
            if (mProgress > 0) {
                RectF oval = new RectF();
                oval.left = (mXCenter - mRingRadius);
                oval.top = (mYCenter - mRingRadius);
                oval.right = mRingRadius * 2 + (mXCenter - mRingRadius);
                oval.bottom = mRingRadius * 2 + (mYCenter - mRingRadius);
                //绘制走完的进度线
                LinearGradient mLinearGradient = new LinearGradient(oval.left,oval.top,oval.right,oval.bottom,
                        new int[]{0xFFFFD32C,0xFFFF5D5D}, null,
                        Shader.TileMode.REPEAT);
                mRingPaint.setShader(mLinearGradient);
                canvas.drawArc(oval, -90, mProgress, false, mRingPaint); //
                RectF oval1 = new RectF();
                oval1.left = (mXCenter - mRadius);
                oval1.top = (mYCenter - mRadius);
                oval1.right = mRadius * 2 + (mXCenter - mRadius);
                oval1.bottom = mRadius * 2 + (mYCenter - mRadius);
                canvas.drawArc(oval1, -90, 360, false, shadowPaint); //
    //                        canvas.drawCircle(mXCenter, mYCenter, mRadius + mStrokeWidth / 2, mRingPaint);
                String txt = (int) (((float) mProgress / 360) * 100) + "%";
                if ((mProgress / 360) * 100 >= 100) {
                    ISAnimator = false;
                    txt = "完成";
                    mProgress = 0;
                    if(!TextUtils.isEmpty(taskType.taskId)){
                        if(taskType.tasksize == 1){
                            taskType.ISStartTask = true;
                            taskType.save();
                            SaveShare.saveValue(context,"JB", "");
                        }else{
                            AppContext.FinishTask(context,"",taskType.taskId,false);
//                            if(taskType.taskfinishsize >= taskType.tasksize){
//                                taskType.ISStartTask = true;
//                                taskType.save();
//                            }
                        }
                        taskType.taskfinishsize++;
                    }

                    if(taskType.schedule != 0){
                        circleSyntony.CompleteSyntony();
                    }
                    taskType.schedule = 0;
                    mIcon = BitmapFactory.decodeResource(getResources(), R.drawable.treasure_box_open);
                    canvas.drawBitmap(mIcon, (float) mXCenter - mIcon.getWidth()/ 2, (float) mYCenter - mIcon.getHeight()/ 2, mCentrePaint);
                    if (taskType.tasksize == taskType.taskfinishsize) {
                        taskType.ISfinish = true;
                        ToastUtils.showLong("今天任务已经完成了哦！");
    //                    Toast.makeText(context, "任务完成！", Toast.LENGTH_LONG).show();
                    } else {
//                        size = 0;
    //                    ISAnimator = true;
                        setProgress(0);
    //                    Toast.makeText(context, "任务完成次数" + TaskType.taskfinishsize, Toast.LENGTH_LONG).show();
                    }
                    taskType.save();
                }
                mTxtWidth = mTextPaint.measureText(txt, 0, txt.length());
    //            canvas.drawText(txt, mXCenter - mTxtWidth / 2, mYCenter + mTxtHeight / 4, mTextPaint);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    /**
//     * 任务完成
//     *
//     * @param appid
//     * @param id       任务id
//     * @param isDouble 是否翻倍
//     */
//    public void FinishTask(Context context, String appid, String id, boolean isDouble) {
//        LoginRequest.getWeatherRequest().getFinishTaskData(context, appid,  "", id, isDouble, new RequestSyntony<FinishTask>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(FinishTask finishTask) {
//                ToastUtils.setView(R.layout.toast_show);
//                View view = ToastUtils.getView();
//                ((TextView) view.findViewById(R.id.add_money)).setText("+" + finishTask.getData());
//                ToastUtils.showLong("");
//                ToastUtils.setView(null);
//
//            }
//        });
//    }

    public void setRestore(){
        try {
            size = 0;
            ISAnimator = true;
            if(mProgress < 360 && taskType.schedule == 5){
                taskType.schedule = 4;
                mProgress = taskType.schedule * fixed;
            }
            setProgress(taskType.schedule);
            taskType.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setProgress(int progress) {
        try {
            Log.e("progress", "progress: l:" + progress);
//        mProgress = progress;
            //                invalidate();
            if(taskType.schedule == 5){
                taskType.schedule = 4;
            }
            if (ISAnimator && taskType.tasksize > taskType.taskfinishsize) {
                if(progress == 0){
                    taskType.schedule = size + 1;
                    valueAnimator = ValueAnimator.ofInt(size * fixed, size * fixed + fixed);
                }else{
                    taskType.schedule = progress + 1;
                    size = progress;
                    valueAnimator = ValueAnimator.ofInt(size * fixed, size * fixed + fixed);
                }
                valueAnimator.setDuration(time);
                valueAnimator.setInterpolator(new LinearInterpolator());
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        ISAnimator = false;
                        mProgress = (int) valueAnimator.getAnimatedValue();
                        Log.e("progress", "progress: l:" + mProgress + "-----" + (size * fixed));
                        if (mProgress >= (size * fixed)) {
                            ISAnimator = true;
                        }
                        invalidate();
                    }
                });
                valueAnimator.start();
                size++;
            }
            taskType.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}