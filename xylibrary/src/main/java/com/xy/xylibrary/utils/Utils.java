package com.xy.xylibrary.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;


import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.CLIPBOARD_SERVICE;


/**
 * Created by zw on 2017/9/12.
 * 基本的工具类
 */
public class Utils {
    @SuppressLint("StaticFieldLeak")
    private static Application sApplication;

    public static Application getContext() {
        if (sApplication != null) return sApplication;
        throw new NullPointerException("u should init first");
    }

    /**
     * Init utils.
     * <p>Init it in the class of Application.</p>
     *
     * @param app application
     */
    public static void init(@NonNull final Application app) {
        Utils.sApplication = app;
    }
    /**
     * 获取剪切板数据
     */
    public static String getClipboard(Context context) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData data = cm.getPrimaryClip();
        if (data != null) {
            ClipData.Item item = data.getItemAt(0);
            return item.getText().toString();
        }
        return "";

    }

    /**
     * 判断字符串是否是数字，
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 数值换算
     */
    public static String NumericalConversion(int number) {
        if (number < 10000) {
            return number + "";
        } else {
            DecimalFormat df = new DecimalFormat("#.00");
            return df.format(((double) number / 10000)) + "万";
        }
    }

    /**
     * 获取版本名称
     */
    public static String getAppInfo(Context context) {
        try {
            String pkName = context.getPackageName();
            String versionName = context.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            int versionCode = context.getPackageManager()
                    .getPackageInfo(pkName, 0).versionCode;
            return versionName;
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 获取当前本地apk的版本
     *
     * @param mContext
     * @return
     */
    public static int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 检查是否有网络
     */
    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info != null) {
            return info.isAvailable();
        }
        return false;
    }

    private static final String DENIED = "denied";
    private static String UNKNOWN = "UNKNOWN";

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

    @SuppressLint("MissingPermission")
    private static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * 获取导航栏高度，有些没有虚拟导航栏的手机也能获取到，建议先判断是否有虚拟按键
     */
    public static int getNavigationBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        return resourceId > 0 ?
                context.getResources().getDimensionPixelSize(resourceId) :
                0;
    }

    /****************
     *
     * 发起添加群流程。群号：二班、永恒⌒&gt;　(51568960) 的 key 为： YcN9utg8USO6MNBENC0hq1l27ONr9h0O
     * 调用 joinQQGroup(YcN9utg8USO6MNBENC0hq1l27ONr9h0O) 即可发起手Q客户端申请加群 二班、永恒⌒&gt;　(51568960)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public static boolean joinQQGroup(Context context, String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    /**
     * 跳转应用市场
     */
    public static void showAppMarket(Context context) {
        try {
            String mAddress = "market://details?id=" + context.getPackageName();
            Intent marketIntent = new Intent("android.intent.action.VIEW");
            marketIntent.setData(Uri.parse(mAddress));
            context.startActivity(marketIntent);
        } catch (Exception e) {
//            ToastUtil.showToast("亲，您未安装任何应用商店，无法评论。");
        }
    }

    /**
     * 判断手机是否含有虚拟按键  99%
     *
     * @return
     */
    public static boolean hasVirtualNavigationBar(Context context) {
        boolean hasSoftwareKeys = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display d = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

            DisplayMetrics realDisplayMetrics = new DisplayMetrics();
            d.getRealMetrics(realDisplayMetrics);

            int realHeight = realDisplayMetrics.heightPixels;
            int realWidth = realDisplayMetrics.widthPixels;

            DisplayMetrics displayMetrics = new DisplayMetrics();
            d.getMetrics(displayMetrics);

            int displayHeight = displayMetrics.heightPixels;
            int displayWidth = displayMetrics.widthPixels;

            hasSoftwareKeys = (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            hasSoftwareKeys = !hasMenuKey && !hasBackKey;
        }

        return hasSoftwareKeys;
    }

    /**
     * 根据手机的分辨率from dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /*获取Ua*/
    public static String getDefaultUserAgentString(Context context) {
        WebView webview = new WebView(context);
        // 得到WebSettings对象
        WebSettings settings = webview.getSettings();
        // 如果访问的页面中有JavaScript，则WebView必须设置支持JavaScript，否则显示空白页面
        webview.getSettings().setJavaScriptEnabled(true);
        // 获取到UserAgentString
        String userAgent = settings.getUserAgentString();
        return userAgent;
    }

    //小米刘海屏适配
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }
    /**
     * 检查手机上是否安装了指定的软件
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        List<String> packageNames = new ArrayList<String>();

        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        // 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }


    /**
     * 获取状态栏高度 m1
     */
    public static int getStatusBarHeight() {
        int statusBarHeight = 0;
        int resourceId = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = Resources.getSystem().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 获取状态栏高度 m2
     */
    public static int getStatusBarHeight(Activity activity) {
        if (activity == null) return getStatusBarHeight();
        Rect rectangle = new Rect();
        Window window = activity.getWindow();
        if (window != null) {
            window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
            return rectangle.top > 0 ? rectangle.top : getStatusBarHeight();
        } else {
            return getStatusBarHeight();
        }
    }

    /**
     * 状态栏改变颜色
     */
    @TargetApi(19)
    public static void setStatusBarColor(Activity activity, int color, boolean ISCOLOR) {
        int statusBarHeight = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
        }
        Log.e("statusBarHeight", "setStatusBarColor: " + statusBarHeight);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                ViewGroup decorViewGroup = (ViewGroup) activity.getWindow().getDecorView();
                if (ISCOLOR) {
                    //获取自己布局的根视图
                    View rootView = ((ViewGroup) (decorViewGroup.findViewById(android.R.id.content))).getChildAt(0);
//                //预留状态栏位置
                    rootView.setFitsSystemWindows(true);
                }
                //添加状态栏高度的视图布局，并填充颜色
                View statusBarTintView = new View(activity);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        statusBarHeight);
                params.gravity = Gravity.TOP;
                statusBarTintView.setLayoutParams(params);
//                statusBarTintView.setBackgroundColor(color);
                decorViewGroup.addView(statusBarTintView);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static int getInternalDimensionSize(Resources res, String key) {
        int result = 0;
        int resourceId = res.getIdentifier(key, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }


    /**
     * 请求授权 READ_EXTERNAL_STORAGE
     */
    public static void requestPermission_STORAGE(Context context) {
        try {
            int osVersion = Integer.valueOf(Build.VERSION.SDK);
            if (osVersion < 22) {

            } else {
                //READ_EXTERNAL_STORAGE
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) { //表示未授权时
                    //进行授权
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求授权 CAMERA
     */
    public static boolean requestPermission_CAMERA(Context context) {
        int osVersion = Integer.valueOf(Build.VERSION.SDK);
        if (osVersion < 22) {
            return true;
        } else {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) { //表示未授权时
                //进行授权
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, 1);
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 请求授权 STATE
     */
    public static boolean requestPermission_STATE(Context context) {
        int osVersion = Integer.valueOf(Build.VERSION.SDK);
        if (osVersion < 22) {
            return true;
        } else {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) { //表示未授权时
                //进行授权
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            } else {
                return true;
            }
        }
        return false;
    }

    //隐藏虚拟键盘
    public static void HideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }


    /**
     * double转String,保留小数点后两位
     *
     * @param num
     * @return
     */
    public static String doubleToString(double num) {
        //使用0.00不足位补0，#.##仅保留有效位
        return new DecimalFormat("0.00").format(num);
    }

    /**
     * 获取当月的 天数
     * int currentMaxDays = getCurrentMonthDay();
     * System.out.println("本月天数：" + currentMaxDays);
     */
    public static int getCurrentMonthDay() {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 根据年 月 获取对应的月份 天数
     * int maxDaysByDate = getDaysByYearMonth(2012, 11);
     * System.out.println("2012年11月天数：" + maxDaysByDate);
     */
    public static int getDaysByYearMonth(int year, int month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 根据日期 找到对应日期的 星期
     * String week = getDayOfWeekByDate("2012-12-25");
     * System.out.println("2012-12-25是：" + week);
     */
    public static String getDayOfWeekByDate() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = sDateFormat.format(new Date());
        return date;
    }

    /**
     * 日期变化
     */
    public static String getDateChange(String date, int type) {
        String dates = "2018年5月";
        String[] YEAR = date.split("年");//获取年
        String[] MONTH = YEAR[1].split("月");//获取月份
        switch (type) {
            case 0://-
                if (MONTH[0].equals("1")) {
                    dates = (Integer.parseInt(YEAR[0]) - 1) + "年12月";
                } else {
                    dates = YEAR[0] + "年" + (Integer.parseInt(MONTH[0]) - 1) + "月";
                }
                break;
            case 1://+
                if (MONTH[0].equals("12")) {
                    dates = (Integer.parseInt(YEAR[0]) + 1) + "年1月";
                } else {
                    dates = YEAR[0] + "年" + (Integer.parseInt(MONTH[0]) + 1) + "月";
                }
                break;
        }

        return dates;
    }

    /**
     * 根据日期 找到对应日期是星期几
     * String week = getDayOfWeekByDate("2012-12-25");
     * System.out.println("2012-12-25是：星期" + week);
     */
    public static String getDayOfWeek(String date) {
        String dayOfweek = "";
        try {
            SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
            Date myDate = myFormatter.parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(myDate);
            int dayOfWeeks = c.get(Calendar.DAY_OF_WEEK);
            switch (dayOfWeeks) {
                case 1:
                    dayOfweek = "星期日";
                    System.out.println("星期日");
                    break;
                case 2:
                    dayOfweek = "星期一";
                    System.out.println("星期一");
                    break;
                case 3:
                    dayOfweek = "星期二";
                    System.out.println("星期二");
                    break;
                case 4:
                    dayOfweek = "星期三";
                    System.out.println("星期三");
                    break;
                case 5:
                    dayOfweek = "星期四";
                    System.out.println("星期四");
                    break;
                case 6:
                    dayOfweek = "星期五";
                    System.out.println("星期五");
                    break;
                case 7:
                    dayOfweek = "星期六";
                    System.out.println("星期六");
                    break;
            }
        } catch (Exception e) {
            System.out.println("错误!");
        }
        return dayOfweek;
    }

    /**
     * 获取屏幕的宽度
     */
    public final static int getWindowsWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取前n天日期、后n天日期
     *
     * @param distanceDay 前几天 如获取前7天日期则传-7即可；如果后7天则传7
     * @return
     */
    public static String getOldDate(int distanceDay) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) - distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dft.format(endDate);
    }

    /**
     * 获取年月日
     */
    public static int getMonth(int number) {
        Calendar c = Calendar.getInstance();//
        int mYear = 0;
        switch (number) {
            case 1:
                mYear = c.get(Calendar.YEAR); // 获取当前年份
                break;
            case 2:
                mYear = c.get(Calendar.MONTH) + 1;// 获取当前月份
                break;
            case 3:
                mYear = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
                break;
        }

        return mYear;
    }

    /**
     * 时间戳转日期
     */
    public static String[] timetodate(String time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(time));
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");//这里的格式可换"yyyy年-MM月dd日-HH时mm分ss秒"等等格式
        String[] date = sf.format(calendar.getTime()).split("-");
        return date;

    }


    /**
     * 获取日期
     */
    public static String getMonths(int type) {
        String date = "2018-05-25·周三";
        switch (type) {
            case 0://昨天
                date = getOldDate(1) + "·" + getDayOfWeek(getOldDate(1));
                break;
            case 1://今天
                date = getDayOfWeekByDate() + "·" + getDayOfWeek(getDayOfWeekByDate());
                break;
        }
        return date;
    }


    /**
     * 屏幕常亮
     */
    public static PowerManager.WakeLock ScreenBrigth(Context context) {
        PowerManager powerManager = (PowerManager) context
                .getSystemService(Context.POWER_SERVICE);
        return powerManager.newWakeLock(
                PowerManager.FULL_WAKE_LOCK, "My Lock");
    }

    /**
     * 获取屏幕的宽和高
     */
    public static int getScreenHeight(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        return metrics.heightPixels;
    }

    /**
     * 屏幕适配
     */
    public static float Screen(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        float bl = (float) 1080 / width;
        return bl;
    }

    /**
     * 修改状态栏颜色，支持4.4以上版本
     *
     * @param activity
     * @param
     */
    public static void setStatusBarColor(Activity activity, int colorId) {
        StatusBarUtil.setStatusBarColor(activity, colorId);
    }

    /**
     * 时间转换
     * */
    public static String times(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(time);
            String createTime = sdf2.format(date);
           return createTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

}
