package com.zt.rainbowweather.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;

import com.zt.rainbowweather.presenter.ChineseCalendar;

import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;


public class Util {

    /**
     * 数字对应的汉字
     */
    public static final String[] lunarNumbers = {"零","一","二","三","四","五","六","七","八","九"};

    /**
     * 农历的月份
     */
    public static final String[] lunarMonths = {"一","二","三","四","五","六","七","八","九","十","十一","十二"};

    public static final String[] lunarDays = {	"初一","初二","初三","初四","初五","初六","初七","初八","初九","初十",
                                                "十一","十二","十三","十四","十五","十六","十七","十八","十九","廿十",
                                                "廿一","廿二","廿三","廿四","廿五","廿六","廿七","廿八","廿九","三十"};

    public static HashMap<Integer, String[]> twelveMonthWithLeapCache = new HashMap<Integer, String[]>();
    private static final String UNKNOWN = "0";

    private static final String DENIED = "denied";

    public static int getMonthLeapByYear(int year){
        return ChineseCalendar.getMonthLeapByYear(year);
    }

    public static String getVersionString() {
        String mVersionString = Build.VERSION.RELEASE;
        return mVersionString;
    }

    public static String getDeviceBrand() {
        String mDeviceBrand = Build.BRAND;
        return mDeviceBrand;
    }

    public static String getDeviceModel() {
        String mDeviceModel = Build.MODEL;
        return mDeviceModel;
    }

    public static String getIMSI(Context context) {
        String mIMSI = UNKNOWN;
        if (context.checkCallingOrSelfPermission(
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return DENIED;
        }
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            mIMSI = UNKNOWN;
            return mIMSI;
        }
        String imsi = tm.getSubscriberId();
        if (TextUtils.isEmpty(imsi)) {
            mIMSI = UNKNOWN;
            return mIMSI;
        }
        mIMSI = imsi.substring(0,5);
        return mIMSI;
    }
    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * RecyclerView 移动到当前位置，
     *
     * @param manager   设置RecyclerView对应的manager
     * @param mRecyclerView  当前的RecyclerView
     * @param n  要跳转的位置
     */
    public static void MoveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int n) {


        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
        }

    }

    /**
     * 获取序列号
     * */
    public static void getPhoneSign(Context context){
        //序列号（sn）
      TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String sn = tm.getSimSerialNumber();
        Log.e("sn", "getPhoneSign: "+sn );
        String serial = null;
        try {
            Class<?> c =Class.forName("android.os.SystemProperties");
            Method get =c.getMethod("get", String.class);
            serial = (String)get.invoke(c, "ro.serialno");
            Log.e("sn", "getPhoneSign: serial"+serial);
            Log.e("sn", "getPhoneSign: serial aa"+ android.os.Build.SERIAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String AddDay(String s,int i){
        try {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            Date today = f.parse(s);
            Calendar c = Calendar.getInstance();
            c.setTime(today);
            c.add(Calendar.DAY_OF_MONTH, i);// 今天+1天
            Date tomorrow = c.getTime();
            return f.format(tomorrow);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(i > 0){
            return s.split("-")[0] + "-" + s.split("-")[1] + "-" + ((Integer.parseInt(s.split("-")[2]) + 1) > 30 ? 1 : (Integer.parseInt(s.split("-")[2]) + 1));
        }else{
            return s.split("-")[0] + "-" + s.split("-")[1] + "-" + ((Integer.parseInt(s.split("-")[2]) - 1) <= 0 ? 30 : (Integer.parseInt(s.split("-")[2]) - 1));
        }
    }
    /**
     * 获取mac
     * */
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public static String getMACAddress(Context context) {
        String mac = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            mac = getNewMac();
        }
        if(TextUtils.isEmpty(mac)){
            // TODO:将结果保存起来；
            mac = UNKNOWN;
            if (context.checkCallingOrSelfPermission(
                    Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED) {
                WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = manager.getConnectionInfo();
                mac = info.getMacAddress();
            } else {
                mac = DENIED;
            }
        }
        return mac ;
    }
    /**
     * 通过网络接口取
     * 记得添加网络权限
     * <uses-permission android:name="android.permission.INTERNET" />
     *
     * @return mac 地址字符串
     */
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private static String getNewMac() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = new byte[0];
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    macBytes = nif.getHardwareAddress();
                }
                if (macBytes == null) {
                    return null;
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // androidID获取
    public static String getAndroidId(Context context) {
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return  androidId;
    }
    /**
     * 获取imei
     * */
    public static String getIMEI(Context context) {
        String mIMEI = UNKNOWN;
        if (context.checkCallingOrSelfPermission(
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return DENIED;
        }
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            mIMEI = UNKNOWN;
        }
        String imei = tm.getDeviceId();
        if (TextUtils.isEmpty(imei)) {
            mIMEI = UNKNOWN;
        }
        mIMEI = imei;

        return mIMEI;
    }

    /**
     * 转数字
     */
    public static int TurnDigital(String digit){
        if(digit.startsWith("0")){
            if(digit.equals("00")){
                return 0;
            }
            return Integer.parseInt(digit.replace("0",""));
        }
        return Integer.parseInt(digit);
    }

    /*月份转英文*/
    public static String MonthEnglish(int month){
        switch (month){
            case 1:
                return "JANUARY";
            case 2:
                return "FEBRUARY";
            case 3:
                return "MARCH";
            case 4:
                return "APRIL";
            case 5:
                return "MAY";
            case 6:
                return "JUNE";
            case 7:
                return "JULY";
            case 8:
                return "AUGUST";
            case 9:
                return "SEPTEMBER";
            case 10:
                return "OCTOBER";
            case 11:
                return "NOVEMBER";
            case 12:
                return "DECEMBER";
        }
        return "JANUARY";
    }

    /*农历大小月份*/
    public static String LunarCalendarSize(String month){
        String DX = "(小)";
        switch (month) {
            case "一月":
            case "三月":
            case "五月":
            case "七月":
            case "八月":
            case "十月":
            case "十二月":
                DX = "(大)";
                break;
        }
        return DX;
    }
    /**
     * View渐现动画效果
     */
    public static void setShowAnimation(View view, int duration)
    {
        if (null == view || duration < 0)
        {
            return;
        }
        AlphaAnimation mShowAnimation = new AlphaAnimation(0.8f, 1.0f);
        mShowAnimation.setDuration(duration);
        mShowAnimation.setFillAfter(true);
        view.startAnimation(mShowAnimation);
    }

    public static Bitmap rsBlur(Context context, Bitmap source, int radius){
         int width = Math.round(source.getWidth()/4);
        int height = Math.round(source.getHeight()/4);

        Bitmap inputBmp = Bitmap.createScaledBitmap(source,width,height,false);

        RenderScript renderScript =  RenderScript.create(context);

        // Allocate memory for Renderscript to work with

        final Allocation input = Allocation.createFromBitmap(renderScript,inputBmp);
        final Allocation output = Allocation.createTyped(renderScript,input.getType());

        // Load up an instance of the specific script that we want to use.
        ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        scriptIntrinsicBlur.setInput(input);

        // Set the blur radius
        scriptIntrinsicBlur.setRadius(radius);

        // Start the ScriptIntrinisicBlur
        scriptIntrinsicBlur.forEach(output);

        // Copy the output to the blurred bitmap
        output.copyTo(inputBmp);

        renderScript.destroy();


        return inputBmp;
    }

    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }
    /**
     * 通过月份的索引获取当月对应的天数
     * @param year
     * 			年份
     * @param monthSway
     * 			索引从1开始
     * @param isGregorian
     * 			是否是公历
     * @return
     * 			月份包含的天数
     */
    public static int getSumOfDayInMonth(int year, int monthSway, boolean isGregorian){
        if(isGregorian){
            return getSumOfDayInMonthForGregorianByMonth(year, monthSway);
        }else{
            return getSumOfDayInMonthForLunarByMonthSway(year, monthSway);
        }
    }

    /**
     * 获取公历year年month月的天数
     * @param year
     * 			年
     * @param month
     * 			月，从1开始计数
     * @return
     * 			月份包含的天数
     */
    public static int getSumOfDayInMonthForGregorianByMonth(int year, int month){
        return new GregorianCalendar(year, month, 0).get(Calendar.DATE);
    }

    /**
     * 获取农历year年monthSway月的天数
     * @param year
     * 			年
     * @param monthSway
     * 			月，包含闰月，如闰五月，monthSway为1代表1月，5代表五月，6代表闰五月
     * @return
     */
    public static int getSumOfDayInMonthForLunarByMonthSway(int year, int monthSway){
        int monthLeap = ChineseCalendar.getMonthLeapByYear(year);
        int monthLunar = convertMonthSwayToMonthLunar(monthSway, monthLeap);
        return ChineseCalendar.daysInChineseMonth(year, monthLunar);
    }

    public static int getSumOfDayInMonthForLunarByMonthLunar(int year, int monthLunar){
        return ChineseCalendar.daysInChineseMonth(year, monthLunar);
    }

    /**
     * 根据已知的闰月月份获取monthSway指向的月份天数
     * @param year
     * 			年
     * @param monthSway
     * 			月，包含闰月，如闰五月，monthSway为1代表1月，5代表五月，6代表闰五月
     * @param monthLeap
     * 			闰月，如闰五月则为5
     * @return
     * 			monthSway指向的月份天数
     */
    public static int getSumOfDayInMonthForLunarLeapYear(int year, int monthSway, int monthLeap){
        int month = convertMonthSwayToMonthLunar(monthSway, monthLeap);
        return ChineseCalendar.daysInChineseMonth(year, month);
    }

    /**
     * 根据year的阿拉伯数字生成汉字
     * @param year
     * 			year in number format, e.g. 1970
     * @return
     * 			year in Hanzi format, e.g. 一九七零年
     */
    public static String getLunarNameOfYear(int year){

        StringBuilder sb = new StringBuilder();
        int divider = 10;
        int digital = 0;

        while(year > 0){
            digital = year % divider;
            sb.insert(0, lunarNumbers[digital]);
            year = year / 10;
        }
    //		sb.append("年");
        return sb.toString();
    }

    public static int ScreenHeight(Activity context){
        WindowManager manager = context.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        return height;
    }
    /**
     * 获取月份的农历中文
     * @param month
     * 			month in number format, e.g. 1
     * 			month should be in range of [1, 12]
     * @return
     * 			month in Hanzi format, e.g. 一月
     */
    public static String getLunarNameOfMonth(int month){
        if(month > 0 && month < 13){
            return lunarMonths[month - 1];
        }else{
            throw new IllegalArgumentException("month should be in range of [1, 12] month is " + month);
        }
    }

    /**
     * 获取农历的日的中文
     * @param day
     * 			day in number format, e.g. 1
     * 			day should be in range of [1, 30]
     * @return
     * 			month in Hanzi format, e.g. 初一
     */
    public static String getLunarNameOfDay(int day){
        if(day > 0 && day < 31){
            return lunarDays[day - 1];
        }else{
            throw new IllegalArgumentException("day should be in range of [1, 30] day is " + day);
        }
    }

    /**
     * 获取农历的月份的中文，可包含闰月
     * @param monthLeap
     * 			the leap month
     * 			month should be in range of [0, 12], 0 if not leap
     * @return
     */
    public static String[] getLunarMonthsNamesWithLeap(int monthLeap){

        if(monthLeap == 0){
            return lunarMonths;
        }

        if(monthLeap < -12 || monthLeap > 0){
            throw new IllegalArgumentException("month should be in range of [-12, 0]");
        }

        int monthLeapAbs = Math.abs(monthLeap);

        String[] monthsOut = twelveMonthWithLeapCache.get(monthLeapAbs);
        if(monthsOut != null && monthsOut.length == 13){
            return monthsOut;
        }

        monthsOut = new String[13];

        System.arraycopy(lunarMonths, 0, monthsOut, 0, monthLeapAbs);
        monthsOut[monthLeapAbs] = "闰" + getLunarNameOfMonth(monthLeapAbs);
        System.arraycopy(lunarMonths, monthLeapAbs, monthsOut, monthLeapAbs + 1, lunarMonths.length - monthLeapAbs);

        twelveMonthWithLeapCache.put(monthLeapAbs, monthsOut);
        return monthsOut;
    }

    /**
     * 农历中，根据闰月、月份，获取月份view应该选择显示的游标值
     * @param monthLunar
     * 			小于0为闰月。取值范围是[-12,-1] + [1,12]
     * @param monthLeap
     * 			已知的闰月。取值范围是[-12,-1] + 0
     * 			0代表无闰月
     * @return
     */
    public static int convertMonthLunarToMonthSway(int monthLunar, int monthLeap){

        if(monthLeap > 0){
            throw new IllegalArgumentException("convertChineseMonthToMonthSway monthLeap should be in range of [-12, 0]");
        }

        if(monthLeap == 0){
            return monthLunar;
        }

        if(monthLunar == monthLeap){//闰月
            return -monthLunar + 1;
        }else if(monthLunar < -monthLeap + 1){
            return monthLunar;
        }else{
            return monthLunar + 1;
        }
    }

    /**
     * 农历根据月份的游标值和闰月值，获取月份的值，负值为闰月
     * @param monthSway
     * 				在NumberPicker中的value，取值范围[1,12] + 13
     * @param monthLeap
     * 				已知的闰月。取值范围是[-12,-1] + 0
     * 				0代表无闰月
     * @return
     * 				返回ChineseCalendar中需要的month，如果是闰月，传入负值
     * 				返回值的范围是[-12,-1] + [1,12]
     */
    public static int convertMonthSwayToMonthLunar(int monthSway, int monthLeap){

        if(monthLeap > 0){
            throw new IllegalArgumentException("convertChineseMonthToMonthSway monthLeap should be in range of [-12, 0]");
        }

        if(monthLeap == 0){
            return monthSway;
        }
        //有闰月
        if(monthSway == -monthLeap + 1){//闰月
            return monthLeap;
        }else if(monthSway < -monthLeap + 1){
            return monthSway;
        }else{
            return monthSway - 1;
        }
    }

    /**
     * 农历根据年份和月份游标值，获取月份的值。负值为闰月
     * @param monthSway
     * 				农历月份view的游标值
     * @param year
     * 				农历年份
     * @return
     */
    public static int convertMonthSwayToMonthLunarByYear(int monthSway, int year){
        int monthLeap = getMonthLeapByYear(year);
        return convertMonthSwayToMonthLunar(monthSway, monthLeap);
    }
}