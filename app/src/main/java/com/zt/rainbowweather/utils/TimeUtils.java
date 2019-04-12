package com.zt.rainbowweather.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.zt.rainbowweather.utils.ConstUtils.DAY;
import static com.zt.rainbowweather.utils.ConstUtils.HOUR;
import static com.zt.rainbowweather.utils.ConstUtils.MIN;
import static com.zt.rainbowweather.utils.ConstUtils.MSEC;
import static com.zt.rainbowweather.utils.ConstUtils.SEC;

/**
 * Created by liyu on 2016/11/10.
 */

public class TimeUtils {
    private TimeUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }




    /**
     * 将时间戳转为时间字符串
     * <p>格式为用户自定义</p>
     *
     * @param milliseconds 毫秒时间戳
     * @param format       时间格式
     * @return 时间字符串
     */
    public static String milliseconds2String(long milliseconds, SimpleDateFormat format) {
        return format.format(new Date(milliseconds));
    }


    /**
     * 将时间字符串转为时间戳
     * <p>格式为用户自定义</p>
     *
     * @param time   时间字符串
     * @param format 时间格式
     * @return 毫秒时间戳
     */
    public static long string2Milliseconds(String time, SimpleDateFormat format) {
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 毫秒时间戳单位转换（单位：unit）
     *
     * @param milliseconds 毫秒时间戳
     * @param unit
     * @return unit时间戳
     */
    private static long milliseconds2Unit(long milliseconds, ConstUtils.TimeUnit unit) {
        switch (unit) {
            case MSEC:
                return milliseconds / MSEC;
            case SEC:
                return milliseconds / SEC;
            case MIN:
                return milliseconds / MIN;
            case HOUR:
                return milliseconds / HOUR;
            case DAY:
                return milliseconds / DAY;
        }
        return -1;
    }



    /**
     * 获取两个时间差（单位：unit）
     * <p>time1和time2格式都为format</p>
     *
     * @param time0  时间字符串1
     * @param time1  时间字符串2
     * @param unit
     * @param format 时间格式
     * @return unit时间戳
     */
    public static long getIntervalTime(String time0, String time1, ConstUtils.TimeUnit unit, SimpleDateFormat format) {
        return milliseconds2Unit(Math.abs(string2Milliseconds(time0, format)
                - string2Milliseconds(time1, format)), unit);
    }

    /**
     * 获取当前时间在某段时间内的百分比位置
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public static float getTimeDiffPercent(String beginTime, String endTime) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date now = null;
        Date begin = null;
        Date end = null;
        try {
            now = df.parse(df.format(new Date()));
            begin = df.parse(beginTime);
            end = df.parse(endTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (float) (now.getTime() - begin.getTime()) / (float) (end.getTime() - begin.getTime());
    }

}
