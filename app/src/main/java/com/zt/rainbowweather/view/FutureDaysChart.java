package com.zt.rainbowweather.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import com.zt.weather.R;
import com.zt.rainbowweather.entity.OutLookWeather;
import java.util.List;


/**
 * Created by tb on 2017/5/13.
 * 未来若干天天气温度图表
 */

public class FutureDaysChart extends View {
    private static final String TAG = "FutureDaysChart";
    private static int DEFAULT_BULE = 0XFF469DF9;
    private static int DEFAULT_Y = 0XFFFEAF50;


    /**
     * 图表的具体高度（单位：dp）
     */
    public static final int CHART_HEIGHT = 130;
    /**
     * 曲线的平滑系数
     */
    private static final float LINE_SMOOTHNESS = 0.2f;
    /**
     * 具体绘制图表的时候每个图表所占的实际高度
     */
    private float eachChartHeight;
    /**
     * 图表数据的个数
     */
    private int lineSize = 0;
    /**
     * 图表的高度
     */
    private float chartHeight;
    /**
     * 两个图表的单位值所占的像素高度
     */
    private float perHeightTop, perHeightBottom;
    /**
     * 绘制图表留出的上下padding值(给文字留出的绘制空间)
     */
    private float padding;
    /**
     * 图表坐标点的半径
     */
    private float pointRaidus;
    /**
     * 白天温度和夜晚温度的最小最大值（注意：这里是两根曲线，所以有两套最大最小值）
     */
    private float minHigh, minLow, maxHigh, maxLow;
    /**
     * 数据集合
     */
    private List<OutLookWeather> datas;
    /**
     * 是否是平滑曲线，默认折线
     */
    private boolean isCubic = false;
    /**
     * 图表的线的paint
     */
    private Paint linePaint = new Paint();
    /**
     * 图表的坐标点的paint
     */
    private Paint pointPaint = new Paint();
    /**
     * 图表的坐标点的值的paint
     */
    private TextPaint labelPaint = new TextPaint();
    /**
     * 绘制曲线的路径
     */
    private Path path = new Path();
    private int backgroundColor;
    public FutureDaysChart(Context context) {
        this(context, null);
    }

    public FutureDaysChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FutureDaysChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FutureDaysChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int
            defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MiuiWeatherView);

        backgroundColor = ta.getColor(R.styleable.MiuiWeatherView_background_color, Color.WHITE);
        init(context);
    }

    private void init(Context context) {
        pointRaidus = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, getResources()
                .getDisplayMetrics());

        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeJoin(Paint.Join.ROUND);// 笔刷图形样式
        linePaint.setStrokeCap(Paint.Cap.ROUND);// 设置画笔转弯的连接风格
        linePaint.setDither(true);//防抖动
        linePaint.setShader(null);
        linePaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0.5f,
                getResources().getDisplayMetrics()));
        linePaint.setColor(DEFAULT_Y);

        pointPaint.setAntiAlias(true);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setColor(DEFAULT_Y);

        labelPaint.setAntiAlias(true);
        labelPaint.setColor(Color.WHITE);
        labelPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15,
                getResources().getDisplayMetrics()));

        float labelHeight = labelPaint.getFontMetrics().bottom - labelPaint.getFontMetrics().top;
        padding = labelHeight * 3f;

        chartHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, CHART_HEIGHT,
                context.getResources().getDisplayMetrics());
        eachChartHeight = chartHeight / 2f - padding;
    }

    public void setCubic(boolean isCubic) {
        this.isCubic = isCubic;
        postInvalidate();
    }

    public void setDatas(List<OutLookWeather> datas) {
        this.datas = datas;

        lineSize = datas.size();

        minHigh = datas.get(0).getHighTemperature();
        maxHigh = datas.get(0).getHighTemperature();

        minLow = datas.get(0).getLowTemperature();
        maxLow = datas.get(0).getLowTemperature();
        for (int i = 1; i < datas.size(); i++) {
            float fh = datas.get(i).getHighTemperature();
            if (minHigh > fh) {
                minHigh = fh;
            }
            if (maxHigh < fh) {
                maxHigh = fh;
            }
            float fl = datas.get(i).getLowTemperature();
            if (minLow > fl) {
                minLow = fl;
            }
            if (maxLow < fl) {
                maxLow = fl;
            }
        }

        //转换比例，找出最大和最小进行换算每个梯度所占像素
        perHeightTop = (eachChartHeight / (maxHigh - minHigh)==0?1:(maxHigh - minHigh));
        perHeightBottom = (eachChartHeight / (maxLow - minLow))==0?1:(maxLow - minLow);

        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (datas == null || datas.size() == 0) {
            return;
        }

        float prePreviousPointX = Float.NaN;
        float prePreviousPointY = Float.NaN;
        float previousPointX = Float.NaN;
        float previousPointY = Float.NaN;
        float currentPointX = Float.NaN;
        float currentPointY = Float.NaN;
        float nextPointX = Float.NaN;
        float nextPointY = Float.NaN;

        float eachWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                ScrollFutureDaysWeatherView.ITEM_WIDTH, getContext().getResources()
                        .getDisplayMetrics());
        path.reset();
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint
                .FILTER_BITMAP_FLAG));

        //画上面的
        for (int i = 0; i < lineSize; i++) {
            float x = eachWidth / 2f + i * eachWidth;
            float y = chartHeight / 2f - padding / 2 - (datas.get(i).getHighTemperature() -
                    minHigh) * perHeightTop + 50;
             linePaint.setColor(DEFAULT_Y);
            //先画一个颜色为背景颜色的实心园覆盖掉折线拐角
            pointPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            pointPaint.setColor(backgroundColor);
            canvas.drawCircle(x, y,
                    pointRaidus + dp2pxF(getContext(), 1),
                    pointPaint);
            pointPaint.setColor(DEFAULT_Y);
            pointPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(x, y, pointRaidus, pointPaint);


            if (isCubic) {
                if (Float.isNaN(currentPointX)) {
                    currentPointX = eachWidth / 2f + i * eachWidth;
                    currentPointY = chartHeight / 2f - padding / 2 - (datas.get(i)
                            .getHighTemperature() - minHigh) * perHeightTop + 50;
                }
                if (Float.isNaN(previousPointX)) {
                    if (i > 0) {
                        previousPointX = eachWidth / 2f + (i - 1) * eachWidth;
                        previousPointY = chartHeight / 2f - padding / 2 - (datas.get(i - 1)
                                .getHighTemperature() - minHigh) * perHeightTop;
                    } else {
                        previousPointX = currentPointX;
                        previousPointY = currentPointY;
                    }
                }

                if (Float.isNaN(prePreviousPointX)) {
                    if (i > 1) {
                        prePreviousPointX = eachWidth / 2f + (i - 2) * eachWidth;
                        prePreviousPointY = chartHeight / 2f - padding / 2 - (datas.get(i - 2)
                                .getHighTemperature() - minHigh) * perHeightTop;
                    } else {
                        prePreviousPointX = previousPointX;
                        prePreviousPointY = previousPointY;
                    }
                }

                // nextPoint is always new one or it is equal currentPoint.
                if (i < lineSize - 1) {
                    nextPointX = eachWidth / 2f + (i + 1) * eachWidth;
                    nextPointY = chartHeight / 2f - padding / 2 - (datas.get(i + 1)
                            .getHighTemperature() - minHigh) * perHeightTop;
                } else {
                    nextPointX = currentPointX;
                    nextPointY = currentPointY;
                }

                if (i == 0) {
                    // Move to start point.
//                    currentPointY = chartHeight / 2f - padding / 2 - (datas.get(i)
//                            .getHighTemperature() - minHigh) * perHeightTop;
                    path.moveTo(currentPointX, currentPointY);
                } else {
                    // Calculate control points.
                    final float firstDiffX = (currentPointX - prePreviousPointX);
                    final float firstDiffY = (currentPointY - prePreviousPointY);
                    final float secondDiffX = (nextPointX - previousPointX);
                    final float secondDiffY = (nextPointY - previousPointY);
                    final float firstControlPointX = previousPointX + (LINE_SMOOTHNESS *
                            firstDiffX);
                    final float firstControlPointY = previousPointY + (LINE_SMOOTHNESS *
                            firstDiffY);
                    final float secondControlPointX = currentPointX - (LINE_SMOOTHNESS *
                            secondDiffX);
                    final float secondControlPointY = currentPointY - (LINE_SMOOTHNESS *
                            secondDiffY);

                    path.cubicTo(firstControlPointX, firstControlPointY+50, secondControlPointX,
                            secondControlPointY+50, currentPointX, currentPointY+50);
                }

                // Shift values by one back to prevent recalculation of values that have
                // been already calculated.
                prePreviousPointX = previousPointX;
                prePreviousPointY = previousPointY;
                previousPointX = currentPointX;
                previousPointY = currentPointY;
                currentPointX = nextPointX;
                currentPointY = nextPointY;
            } else {
                if (i == 0) {
                    path.moveTo(x, y);
                } else {
                    path.lineTo(x, y);
                }
            }

            String label = datas.get(i).getHighTemperature() + "°";
            canvas.drawText(label, x - labelPaint.measureText(label) / 2, y - pointRaidus * 4,
                    labelPaint);
        }
        canvas.drawPath(path, linePaint);

        path.reset();

        prePreviousPointX = Float.NaN;
        prePreviousPointY = Float.NaN;
        previousPointX = Float.NaN;
        previousPointY = Float.NaN;
        currentPointX = Float.NaN;
        currentPointY = Float.NaN;
        nextPointX = Float.NaN;
        nextPointY = Float.NaN;
        //画下面的
        for (int i = 0; i < lineSize; i++) {
            float x = eachWidth / 2f + i * eachWidth;
            float y = chartHeight - padding / 2 - (datas.get(i).getLowTemperature() - minLow) *
                    perHeightBottom - 50;

            linePaint.setColor(DEFAULT_BULE);
            //先画一个颜色为背景颜色的实心园覆盖掉折线拐角
            pointPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            pointPaint.setColor(backgroundColor);
            canvas.drawCircle(x, y,
                    pointRaidus + dp2pxF(getContext(), 1),
                    pointPaint);
            pointPaint.setColor(DEFAULT_BULE);
            pointPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(x, y, pointRaidus, pointPaint);


            if (isCubic) {
                if (Float.isNaN(currentPointX)) {
                    currentPointX = eachWidth / 2f + i * eachWidth;
                    currentPointY = chartHeight - padding / 2 - (datas.get(i).getLowTemperature()
                            - minLow) * perHeightBottom - 50;
                }
                if (Float.isNaN(previousPointX)) {
                    if (i > 0) {
                        previousPointX = eachWidth / 2f + (i - 1) * eachWidth;
                        previousPointY = chartHeight - padding / 2 - (datas.get(i - 1)
                                .getLowTemperature() - minLow) * perHeightBottom;
                    } else {
                        previousPointX = currentPointX;
                        previousPointY = currentPointY;
                    }
                }

                if (Float.isNaN(prePreviousPointX)) {
                    if (i > 1) {
                        prePreviousPointX = eachWidth / 2f + (i - 2) * eachWidth;
                        prePreviousPointY = chartHeight - padding / 2 - (datas.get(i - 2)
                                .getLowTemperature() - minLow) * perHeightBottom;
                    } else {
                        prePreviousPointX = previousPointX;
                        prePreviousPointY = previousPointY;
                    }
                }

                // nextPoint is always new one or it is equal currentPoint.
                if (i < lineSize - 1) {
                    nextPointX = eachWidth / 2f + (i + 1) * eachWidth;
                    nextPointY = chartHeight - padding / 2 - (datas.get(i + 1).getLowTemperature
                            () - minLow) * perHeightBottom;
                } else {
                    nextPointX = currentPointX;
                    nextPointY = currentPointY;
                }

                if (i == 0) {
                    // Move to start point.
                    path.moveTo(currentPointX, currentPointY);
                } else {
                    // Calculate control points.
                    final float firstDiffX = (currentPointX - prePreviousPointX);
                    final float firstDiffY = (currentPointY - prePreviousPointY);
                    final float secondDiffX = (nextPointX - previousPointX);
                    final float secondDiffY = (nextPointY - previousPointY);
                    final float firstControlPointX = previousPointX + (LINE_SMOOTHNESS *
                            firstDiffX);
                    final float firstControlPointY = previousPointY + (LINE_SMOOTHNESS *
                            firstDiffY);
                    final float secondControlPointX = currentPointX - (LINE_SMOOTHNESS *
                            secondDiffX);
                    final float secondControlPointY = currentPointY - (LINE_SMOOTHNESS *
                            secondDiffY);
                    path.cubicTo(firstControlPointX, firstControlPointY -50, secondControlPointX,
                            secondControlPointY -50, currentPointX, currentPointY -50);
                }

                // Shift values by one back to prevent recalculation of values that have
                // been already calculated.
                prePreviousPointX = previousPointX;
                prePreviousPointY = previousPointY;
                previousPointX = currentPointX;
                previousPointY = currentPointY;
                currentPointX = nextPointX;
                currentPointY = nextPointY;
            } else {
                if (i == 0) {
                    path.moveTo(x, y);
                } else {
                    path.lineTo(x, y);
                }
            }

            String label = datas.get(i).getLowTemperature() + "°";
            float labelHeight = labelPaint.getFontMetrics().bottom - labelPaint.getFontMetrics()
                    .top;
            canvas.drawText(label, x - labelPaint.measureText(label) / 2, y + labelHeight,
                    labelPaint);
        }
        canvas.drawPath(path, linePaint);
    }
    public static float dp2pxF(Context c, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, c.getResources()
                .getDisplayMetrics());
    }
}
