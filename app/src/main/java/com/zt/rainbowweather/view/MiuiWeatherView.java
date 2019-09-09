package com.zt.rainbowweather.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
 import android.widget.Scroller;
import android.widget.TextView;

import com.xy.xylibrary.utils.SaveShare;
import com.zt.rainbowweather.utils.Util;
import com.zt.weather.R;
import com.zt.rainbowweather.entity.WeatherBean;
import com.zt.rainbowweather.utils.Cubic;
import com.zt.rainbowweather.utils.WeatherUtils;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class MiuiWeatherView extends View {

    private static int DEFAULT_BULE = Color.WHITE;
    private static int DEFAULT_GRAY = Color.parseColor("#1fffffff");

    private int backgroundColor;
    private int minViewHeight; //控件的最低高度
    private int minPointHeight;//折线最低点的高度
    private int lineInterval; //折线线段长度
    private float pointRadius; //折线点的半径
    private float textSize; //字体大小
    private float pointGap; //折线单位高度差
    private int defaultPadding; //折线坐标图四周留出来的偏移量
    private float iconWidth;  //天气图标的边长
    private float indicateiconWidth;//指示图标长度
    private int viewHeight;
    private int viewWidth;
    private int screenWidth;
    private int screenHeight;

    private Paint linePaint; //线画笔
    private Paint textPaint; //文字画笔
    private Paint circlePaint; //圆点画笔
    private Paint p;
    private Paint numberTextPaint;
    private List<WeatherBean> data = new ArrayList<>(); //元数据
    private List<Pair<Integer, Integer>> weatherDatas = new ArrayList<>();  //对元数据中天气分组后的集合
    private List<Float> dashDatas = new ArrayList<>(); //不同天气之间虚线的x坐标集合
    private List<PointF> points = new ArrayList<>(); //折线拐点的集合
    //    private Map<String, Bitmap> icons = new HashMap<>(); //天气图标集合
    private int maxTemperature;//元数据中的最高和最低温度
    private int minTemperature;
    private Context context;
    private VelocityTracker velocityTracker;
    private Scroller scroller;
    private ViewConfiguration viewConfiguration;


    public MiuiWeatherView(Context context) {
        this(context, null);
    }

    public MiuiWeatherView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MiuiWeatherView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        scroller = new Scroller(context);
        viewConfiguration = ViewConfiguration.get(context);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MiuiWeatherView);
        minPointHeight = (int) ta.getDimension(R.styleable.MiuiWeatherView_min_point_height,
                dp2pxF(context, 40));
        lineInterval = (int) ta.getDimension(R.styleable.MiuiWeatherView_line_interval, dp2pxF
                (context, 60));
        backgroundColor = ta.getColor(R.styleable.MiuiWeatherView_background_color, Color.WHITE);
        ta.recycle();

        setBackgroundColor(backgroundColor);

        initSize(context);

        initPaint(context);

//        initIcons();
        TextView tv = new TextView(getContext());
    }

    /**
     * 初始化默认数据
     */
    private void initSize(Context c) {
        screenWidth = getResources().getDisplayMetrics().widthPixels - (int)dp2pxF(context, 35);
        screenHeight = getResources().getDisplayMetrics().heightPixels;

        minViewHeight = 3 * minPointHeight;  //默认3倍
        pointRadius = dp2pxF(c, 2.5f);
        textSize = sp2pxF(c, 10);
        defaultPadding = (int) (0.5 * minPointHeight);  //默认0.5倍
        iconWidth = (1.0f / 2.0f) * lineInterval; //默认1/3倍
        indicateiconWidth = dp2pxF(context, 30);
    }

    /**
     * 计算折线单位高度差
     */
    private void calculatePontGap() {
        int lastMaxTem = -Integer.MAX_VALUE;
        int lastMinTem = Integer.MAX_VALUE;
        for (WeatherBean bean : data) {
            if (bean.temperature > lastMaxTem) {
                maxTemperature = bean.temperature;
                lastMaxTem = bean.temperature;
            }
            if (bean.temperature < lastMinTem) {
                minTemperature = bean.temperature;
                lastMinTem = bean.temperature;
            }
        }
        float gap = (maxTemperature - minTemperature) * 1.0f;
        gap = (gap == 0.0f ? 1.0f : gap);  //保证分母不为0
        pointGap = (viewHeight - minPointHeight - 2 * defaultPadding) / gap;
    }

    private void initPaint(Context c) {
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStrokeWidth(dp2px(c, 1));

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textSize);
        textPaint.setColor(Color.parseColor("#ffb2b9c3"));
        textPaint.setTextAlign(Paint.Align.CENTER);

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStrokeWidth(dp2pxF(c, 1));

        numberTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        numberTextPaint.setTextSize(sp2pxF(c, 10));
        numberTextPaint.setColor(Color.parseColor("#727272"));
        numberTextPaint.setStrokeWidth(dp2pxF(c, 10));
    }
    private int mWidth;
    private int mHeight;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = 24 * dp2px(context, 24);
        mHeight = h;
        initSize(getContext());
        calculatePontGap();
    }

    /**
     * 公开方法，用于设置元数据
     *
     * @param data
     */
    public void setData(List<WeatherBean> data) {
        if (data == null) {
            return;
        }
        this.data = data;
        notifyDataSetChanged();
    }

    public List<WeatherBean> getData() {
        return data;
    }

    public void notifyDataSetChanged() {
        if (data == null) {
            return;
        }
        weatherDatas.clear();
        points.clear();
        dashDatas.clear();

        initWeatherMap(); //初始化相邻的相同天气分组
        requestLayout();
        invalidate();
    }


    /**
     * 根据元数据中连续相同的天气数做分组,
     * pair中的first值为连续相同天气的数量，second值为对应天气
     */
    private void initWeatherMap() {
        weatherDatas.clear();
        int lastWeather = 0;
        int count = 0;
        for (int i = 0; i < data.size(); i++) {
            WeatherBean bean = data.get(i);
            if (i == 0) {
//                lastWeather = bean.weather;
                lastWeather = bean.weather.weatherId;
            }
            if(!TextUtils.isEmpty(bean.time) ){
                int sr = 5;
                int ss = 17;
               String sunrise = SaveShare.getValue(context, "sunrise");
                String sunset = SaveShare.getValue(context, "sunset");
                if(!TextUtils.isEmpty(sunrise) && !TextUtils.isEmpty(sunset)){
                    sr = Util.TurnDigital(sunrise.split(":")[0]);
                    ss = Util.TurnDigital(sunset.split(":")[0]);
                }
                if(Util.TurnDigital(bean.time.split(":")[0]) >= ss || Util.TurnDigital(bean.time.split(":")[0]) < sr){
                    if(bean.weather.weatherId == 100){
                        bean.weather.weatherId = 1000;
                    }
                    if(bean.weather.weatherId == 101){
                        bean.weather.weatherId = 1001;
                    }
                    if(bean.weather.weatherId == 102){
                        bean.weather.weatherId = 1002;
                    }
                    if(bean.weather.weatherId == 102){
                        bean.weather.weatherId = 1002;
                    }
                }
            }
            if (bean.weather.weatherId != lastWeather) {
                Pair<Integer, Integer> pair = new Pair<>(count, lastWeather);
                weatherDatas.add(pair);
                count = 1;
            } else {
                count++;
            }
            lastWeather = bean.weather.weatherId;

            if (i == data.size() - 1) {
                Pair<Integer, Integer> pair = new Pair<>(count, lastWeather);
                weatherDatas.add(pair);
            }
        }

        for (int i = 0; i < weatherDatas.size(); i++) {
            int c = weatherDatas.get(i).first;
            Integer w = weatherDatas.get(i).second;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (heightMode == MeasureSpec.EXACTLY) {
            viewHeight = Math.max(heightSize, minViewHeight);
        } else {
            viewHeight = minViewHeight;
        }
        int totalWidth = 0;
        if (data.size() > 1) {
            totalWidth = 2 * defaultPadding/3*2 + lineInterval * (data.size() - 1)+ (int)dp2pxF(context, 10f);
        }
        viewWidth = Math.max(screenWidth, totalWidth);  //默认控件最小宽度为屏幕宽度
        setMeasuredDimension(viewWidth, viewHeight);
        calculatePontGap();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (data.isEmpty()) {
            return;
        }
        drawAxis(canvas);
         drawLinesAndPoints(canvas);
//        drawTemperature(canvas);
        drawWeatherDash(canvas);
        drawWeatherIcon(canvas);
//        addColor(canvas);
    }

    /**
     * 画时间轴
     *
     * @param canvas
     */
    private void drawAxis(Canvas canvas) {
        canvas.save();
        linePaint.setColor(DEFAULT_GRAY);
        linePaint.setStrokeWidth(dp2px(getContext(), 1));
        canvas.drawLine(defaultPadding/3*2,
                viewHeight - defaultPadding,
                viewWidth - defaultPadding/3*2 - (int)dp2pxF(context, 10f),
                viewHeight - defaultPadding,
                linePaint);

        float centerY = viewHeight - defaultPadding + dp2pxF(getContext(), 15);
        float centerX;
        for (int i = 0; i < data.size(); i++) {
            String text = data.get(i).time;
            centerX = defaultPadding/3*2 + i * lineInterval;
            Paint.FontMetrics m = textPaint.getFontMetrics();
//            if(i == (data.size()-1)){
//                canvas.drawText(text, 0, text.length(), centerX, centerY - (m.ascent + m.descent) /
//                        2, textPaint);
//            }else{
                canvas.drawText(text, 0, text.length(), centerX, centerY - (m.ascent + m.descent) /
                        2, textPaint);
//            }

        }
        canvas.restore();
    }

    /**
     * 画折线和它拐点的园
     *
     * @param canvas
     */
    private void drawLinesAndPoints(Canvas canvas) {
        canvas.save();
        linePaint.setColor(DEFAULT_BULE);
        linePaint.setStrokeWidth(dp2pxF(getContext(), 1));
        linePaint.setStyle(Paint.Style.STROKE);

        Path linePath = new Path(); //用于绘制折线
        points.clear();
        int baseHeight = defaultPadding + minPointHeight;
        float centerX;
        float centerY;
        Point[] pointDatas = new Point[data.size()];

        for (int i = 0; i < data.size(); i++) {
            int tem = data.get(i).temperature;
            tem = tem - minTemperature;
            centerY = (int) (viewHeight - (baseHeight + tem * pointGap));
            centerX = defaultPadding/3*2 + i * lineInterval;
            pointDatas[i] = new Point((int)centerX, (int)centerY);
            points.add(new PointF(centerX, centerY));
            if (i == 0) {
                linePath.moveTo(centerX, centerY);
            } else {
              linePath.quadTo((centerX - 20) > 0 ?(centerX - 20) : centerX, centerY, centerX, centerY);
                linePath.lineTo(centerX, centerY);
            }
        }
        p = new Paint();
        Shader shader = new BitmapShader(BitmapFactory.decodeResource(this.getResources(),R.mipmap.bg_24),Shader.TileMode.REPEAT,Shader.TileMode.REPEAT);
        p.setShader(shader);
        Point startp = new Point();
        Point endp = new Point();
        for (int i = 0; i < pointDatas.length - 1; i++){
            startp = pointDatas[i];
            endp = pointDatas[i + 1];
            int wt = (startp.x + endp.x) / 2;
            Point p3 = new Point();
            Point p4 = new Point();

            p3.y = startp.y;
            p3.x = wt;
            p4.y = endp.y;
            p4.x = wt;
            Path path = new Path();
            path.moveTo(startp.x, startp.y);
            path.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
            canvas.drawPath(path, linePaint);
            Path path2 = new Path();
            path2.moveTo(startp.x , startp.y);
            path2.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
            path2.quadTo(endp.x, endp.y,endp.x,viewHeight - defaultPadding);
            path2.quadTo(endp.x,viewHeight - defaultPadding,startp.x,viewHeight - defaultPadding);
            path2.close();
            canvas.drawPath(path2, p);
        }
//        canvas.drawPath(linePath, linePaint); //画出折线
        //接下来画折线拐点的园
        float x, y = 0;
        int scrollX = getScrollX();  //范围控制在0 ~ viewWidth-screenWidth
        float f = (float) viewWidth/(viewWidth - screenWidth);
        x = points.get(0).x+scrollX*f;
        if(scrollX >= (viewWidth - screenWidth)){
            x = x - 10;
        }
//            y = points.get(0).y;
        if(x > points.get((points.size() - 1)).x){
            x = points.get((points.size() - 1)).x;
        }
        float j = (viewWidth - screenWidth) / data.size();
        float f1 = (points.get((points.size() - 1)).x - points.get(0).x)/points.size();//先获取高度差
        int f3 = (int) ( scrollX / j);
        if(f3 >= data.size() - 1){
            f3 = data.size() - 1;
        }
        for (int i = 0; i < data.size(); i++) {
            if(f3 == i){
                float f2 = scrollX % f1;
                y = points.get(i).y + f2/(f1 / 10) * (points.get((i+ 1) >= points.size()-1?points.size() -1:(i+ 1)).y - points.get((i) >= points.size()-1?points.size()-1:(i)).y)/10;
                Bitmap icon = getIcon(R.mipmap.indicate);
                 if(x >= ((viewWidth - icon.getWidth() - (int)dp2pxF(context, 10f)))){
                    x = x - icon.getWidth()/2;
                }
                //经过上述校正之后可以得到图标和文字的绘制区域
                RectF iconRect = new RectF(x - icon.getWidth()/2,
                        y - icon.getHeight() * 5/3,
                        x + icon.getWidth() * 3/2+(icon.getWidth())*(((float)data.get(i).weather.weather.length()/2)-1),
                        y);
//                canvas.drawBitmap(icon, (float) x-icon.getHeight()/2, (float)y-icon.getHeight(), null);
                canvas.drawBitmap(icon, null, iconRect, null);  //画图标
//            //画下方文字
            canvas.drawText(data.get(i).temperature +"°"+ data.get(i).weather.weather,
                    x - dp2pxF(getContext(), 8), y - icon.getHeight() * 2 / 3.0f, numberTextPaint);
             }
        }
//            //先画一个颜色为背景颜色的实心园覆盖掉折线拐角
//            circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
//            circlePaint.setColor(backgroundColor);
//            canvas.drawCircle(x, y,
//                    pointRadius + dp2pxF(getContext(), 1),
//                    circlePaint);
////            //再画出正常的空心园
//            circlePaint.setStyle(Paint.Style.FILL);
//            circlePaint.setColor(DEFAULT_BULE);
//            canvas.drawCircle(x, y,
//                    pointRadius,
//                    numberTextPaint);
        canvas.restore();
    }

    /**
     * 画温度描述值
     *
     * @param canvas
     */
    private void drawTemperature(Canvas canvas) {
        canvas.save();

        textPaint.setTextSize(1.2f * textSize); //字体放大一丢丢
        float centerX;
        float centerY;
        String text;
        for (int i = 0; i < points.size(); i++) {
            text = data.get(i).temperatureStr;
            centerX = points.get(i).x;
            centerY = points.get(i).y - dp2pxF(getContext(), 13);
            Paint.FontMetrics metrics = textPaint.getFontMetrics();
            canvas.drawText(text,
                    centerX,
                    centerY - (metrics.ascent + metrics.descent) / 2,
                    textPaint);
        }
        textPaint.setTextSize(textSize);
        canvas.restore();
    }
    List<Integer> points_x;
    List<Integer> points_y;
    private Paint colorPaint;

    private void addColor(Canvas canvas) {
        points_x = new LinkedList<>();
        points_y = new LinkedList<>();
        List<Point> points = new LinkedList<>();
        colorPaint = new Paint();
        colorPaint.setAlpha(125);
        colorPaint.setAntiAlias(true);
        colorPaint.setStyle(Paint.Style.FILL);
        Shader shader = new LinearGradient(0,0,0,mHeight/3*2+dp2px(context,10),
                Color.parseColor("#ffe666"),Color.parseColor("#ff6647"),Shader.TileMode.CLAMP);
        colorPaint.setShader(shader);

        // 存放最低温度
        int minTempDay = data.get(0).temperature;
        // 存放最高温度
        int maxTempDay =data.get(0).temperature;
        for (WeatherBean item : data) {
            if (item.temperature < minTempDay) {
                minTempDay = item.temperature;
            }
            if (item.temperature > maxTempDay) {
                maxTempDay = item.temperature;
            }
        }
        // 份数
        float parts = maxTempDay - minTempDay;
        float lengthToTop = dp2px(context, 30);
        //每一份的高度
        float partValue = mHeight / 3 / parts;
        for (int i = 0; i < data.size(); i++) {
            points.add(new Point((int)mWidth / 24 * i, (int)((maxTempDay -data.get(i).temperature) * partValue + lengthToTop + mHeight / 6)));
        }
        points.add(new Point(mWidth, (int)((maxTempDay - data.get(0).temperature) * partValue + lengthToTop + mHeight / 6)));
        Path path = new Path();
        points_x.clear();
        points_y.clear();
        for (int i = 0; i < points.size(); i++) {
            points_x.add(points.get(i).x);
            points_y.add(points.get(i).y);
        }
        List<Cubic> calculate_x = calculate(points_x);
        List<Cubic> calculate_y = calculate(points_y);
        path.moveTo(calculate_x.get(0).eval(0), calculate_y.get(0).eval(0));
        for (int i = 0; i < calculate_x.size(); i++) {

            for (int j = 1; j <= data.size(); j++) {
                float u = j / (float) data.size();
                path.lineTo(calculate_x.get(i).eval(u), calculate_y.get(i)
                        .eval(u));
            }
        }
        path.lineTo(mWidth,mHeight/3*2+dp2px(context,10));
        path.lineTo(0,mHeight/3*2+dp2px(context,10));
        canvas.drawPath(path,colorPaint);
    }
    private List<Cubic> calculate(List<Integer> x) {
        int n = x.size() - 1;
        float[] gamma = new float[n + 1];
        float[] delta = new float[n + 1];
        float[] D = new float[n + 1];
        int i;
        /*
         * We solve the equation [2 1 ] [D[0]] [3(x[1] - x[0]) ] |1 4 1 | |D[1]|
         * |3(x[2] - x[0]) | | 1 4 1 | | . | = | . | | ..... | | . | | . | | 1 4
         * 1| | . | |3(x[n] - x[n-2])| [ 1 2] [D[n]] [3(x[n] - x[n-1])]
         *
         * by using row operations to convert the matrix to upper triangular and
         * then back sustitution. The D[i] are the derivatives at the knots.
         */
        gamma[0] = 1.0f / 2.0f;
        for (i = 1; i < n; i++) {
            gamma[i] = 1 / (4 - gamma[i - 1]);
        }
        gamma[n] = 1 / (2 - gamma[n - 1]);
        delta[0] = 3 * (x.get(1) - x.get(0)) * gamma[0];
        for (i = 1; i < n; i++) {
            delta[i] = (3 * (x.get(i + 1) - x.get(i - 1)) - delta[i - 1])
                    * gamma[i];
        }
        delta[n] = (3 * (x.get(n) - x.get(n - 1)) - delta[n - 1]) * gamma[n];
        D[n] = delta[n];
        for (i = n - 1; i >= 0; i--) {
            D[i] = delta[i] - gamma[i] * D[i + 1];
        }

        /* now compute the coefficients of the cubics */
        List<Cubic> cubics = new LinkedList<Cubic>();
        for (i = 0; i < n; i++) {
            Cubic c = new Cubic(x.get(i), D[i], 3 * (x.get(i + 1) - x.get(i))
                    - 2 * D[i] - D[i + 1], 2 * (x.get(i) - x.get(i + 1)) + D[i]
                    + D[i + 1]);
            cubics.add(c);
        }
        return cubics;
    }
    /**
     * 画不同天气之间的虚线
     *
     * @param canvas
     */
    private void drawWeatherDash(Canvas canvas) {
        canvas.save();
        linePaint.setColor(DEFAULT_GRAY);
        linePaint.setStrokeWidth(dp2pxF(getContext(), 1f));
        linePaint.setAlpha(0xcc);

        //设置画笔画出虚线
        float[] f = {dp2pxF(getContext(), 5), dp2pxF(getContext(), 1)};  //两个值分别为循环的实线长度、空白长度
        PathEffect pathEffect = new DashPathEffect(f, 0);
        linePaint.setPathEffect(pathEffect);

        dashDatas.clear();
        int interval = 0;
        float startX, startY, endX, endY;
        endY = viewHeight - defaultPadding;

        //0坐标点的虚线手动画上
//        canvas.drawLine(defaultPadding/3*2,
//                points.get(0).y - pointRadius + dp2pxF(getContext(), 2),
//                defaultPadding/3*2,
//                endY,
//                linePaint);
        dashDatas.add((float) defaultPadding);

        for (int i = 0; i < weatherDatas.size(); i++) {
            interval += weatherDatas.get(i).first;
            if (interval > points.size() - 1) {
                interval = points.size() - 1;
            }
            startX = endX = defaultPadding/3*2 + interval * lineInterval;
            startY = points.get(interval).y + dp2pxF(getContext(), 2);
            dashDatas.add(startX);
            linePaint.setColor(DEFAULT_GRAY);
            canvas.drawLine(startX, startY - pointRadius , endX, endY, linePaint);
        }
        //这里注意一下，当最后一组的连续天气数为1时，是不需要计入虚线集合的，否则会多画一个天气图标
        //若不理解，可尝试去掉下面这块代码并观察运行效果
        if (weatherDatas.get(weatherDatas.size() - 1).first == 1
                && dashDatas.size() > 1) {
            dashDatas.remove(dashDatas.get(dashDatas.size() - 1));
        }
        linePaint.setPathEffect(null);
        linePaint.setAlpha(0xff);
        canvas.restore();
    }

    /**
     * 画天气图标和它下方文字
     * 若相邻虚线都在屏幕内，图标的x位置即在两虚线的中间
     * 若有一条虚线在屏幕外，图标的x位置即在屏幕边沿到另一条虚线的中间
     * 若两条都在屏幕外，图标x位置紧贴某一条虚线或屏幕中间
     *
     * @param canvas
     */
    private void drawWeatherIcon(Canvas canvas) {
        canvas.save();
        textPaint.setTextSize(0.9f * textSize); //字体缩小一丢丢

        boolean leftUsedScreenLeft = false;
        boolean rightUsedScreenRight = false;

        int scrollX = getScrollX();  //范围控制在0 ~ viewWidth-screenWidth
        float left, right;
        float iconX, iconY;
        float textY;     //文字的x坐标跟图标是一样的，无需额外声明
        iconY = viewHeight - (defaultPadding + minPointHeight / 2.0f);
        textY = iconY + iconWidth / 2.0f + dp2pxF(getContext(), 10);
        Paint.FontMetrics metrics = textPaint.getFontMetrics();
        for (int i = 0; i < dashDatas.size() - 1; i++) {
            left = dashDatas.get(i);
            right = dashDatas.get(i + 1);
            //以下校正的情况为：两条虚线都在屏幕内或只有一条在屏幕内
            if (left < scrollX &&    //仅左虚线在屏幕外
                    right < scrollX + screenWidth) {
                left = scrollX;
                leftUsedScreenLeft = true;
            }
            if (right > scrollX + screenWidth &&  //仅右虚线在屏幕外
                    left > scrollX) {
                right = scrollX + screenWidth;
                rightUsedScreenRight = true;
            }
            if (right - left > iconWidth) {    //经过上述校正之后左右距离还大于图标宽度
                iconX = left + (right - left) / 2.0f;
            } else {                          //经过上述校正之后左右距离小于图标宽度，则贴着在屏幕内的虚线
                if (leftUsedScreenLeft) {
                    iconX = right - iconWidth / 2.0f;
                } else {
                    iconX = left + iconWidth / 2.0f;
                }
            }
            //以下校正的情况为：两条虚线都在屏幕之外
            if (right < scrollX) {  //两条都在屏幕左侧，图标紧贴右虚线
                iconX = right - iconWidth / 2.0f;
            } else if (left > scrollX + screenWidth) {   //两条都在屏幕右侧，图标紧贴左虚线
                iconX = left + iconWidth / 2.0f;
            } else if (left < scrollX && right > scrollX + screenWidth) {  //一条在屏幕左一条在屏幕右，图标居中
                iconX = scrollX + (screenWidth / 2.0f);
            }
            Bitmap icon = getWeatherIcon(weatherDatas.get(i).second);

            //经过上述校正之后可以得到图标和文字的绘制区域
            RectF iconRect = new RectF(iconX - iconWidth / 2.0f,
                    iconY - iconWidth / 2.0f,
                    iconX + iconWidth / 2.0f,
                    iconY + iconWidth / 2.0f);

            canvas.drawBitmap(icon, null, iconRect, null);  //画图标
//            //画下方文字
//            canvas.drawText(WeatherUtils.getWeatherStatus(weatherDatas.get(i).second).weather,
//                    iconX, textY - (metrics.ascent + metrics.descent) / 2, textPaint);
            leftUsedScreenLeft = rightUsedScreenRight = false; //重置标志位
        }
        textPaint.setTextSize(textSize);
        canvas.restore();
    }


    public Bitmap getIcon(int resId) {
        Bitmap bmp;
        int outWdith, outHeight;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), resId, options);
        outWdith = options.outWidth;
        outHeight = options.outHeight;
        options.inSampleSize = 1;
        if (outWdith > iconWidth || outHeight > iconWidth) {
            int ratioW = Math.round(outWdith / iconWidth);
            int ratioH = Math.round(outHeight / iconWidth);
            options.inSampleSize = Math.max(ratioW, ratioH);
        }
        options.inJustDecodeBounds = false;
        bmp = BitmapFactory.decodeResource(getResources(), resId, options);
        return bmp;
    }

    private Bitmap getWeatherIcon(Integer weatherId) {
        int resId = WeatherUtils.getWeatherStatus(weatherId).iconRes;
        return getIcon(resId);
    }

    private float lastX = 0;
    private float lastY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!scroller.isFinished()) {  //fling还没结束
                    scroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = x - lastX;
                float dy = y - lastY;
                if (Math.abs(dx) > Math.abs(dy)) {//我需要这个事件
                    getParent().requestDisallowInterceptTouchEvent(true);
                    int deltaX = (int) (lastX - x);
                    if (getScrollX() + deltaX < 0) {    //越界恢复
                        scrollTo(0, 0);
                        return false;
                    } else if (getScrollX() + deltaX > viewWidth - screenWidth) {
                        scrollTo(viewWidth - screenWidth, 0);
                        return false;
                    }
                    scrollBy(deltaX, 0);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return false;
                }

                break;
            case MotionEvent.ACTION_UP:
                velocityTracker.computeCurrentVelocity(1000);  //计算1秒内滑动过多少像素
                int xVelocity = (int) velocityTracker.getXVelocity();
                if (Math.abs(xVelocity) > viewConfiguration.getScaledMinimumFlingVelocity()) {
                    //滑动速度可被判定为抛动
                    scroller.fling(getScrollX(), 0, -xVelocity, 0, 0, viewWidth - screenWidth, 0,
                            0);
                    invalidate();
                }
                break;
        }
        lastX = x;
        lastY = y;
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }

    //工具类
    public static int dp2px(Context c, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, c.getResources()
                .getDisplayMetrics());
    }

    public static int sp2px(Context c, float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, c.getResources()
                .getDisplayMetrics());
    }

    public static float dp2pxF(Context c, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, c.getResources()
                .getDisplayMetrics());
    }

    public static float sp2pxF(Context c, float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, c.getResources()
                .getDisplayMetrics());
    }
}
