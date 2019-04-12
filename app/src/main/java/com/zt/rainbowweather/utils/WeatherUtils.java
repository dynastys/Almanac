package com.zt.rainbowweather.utils;


import com.zt.rainbowweather.entity.WeatherUtilBean;
import com.chenguang.weather.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by hcg on 2018/5/3.
 */

public class WeatherUtils {
    public static WeatherUtilBean getWeatherStatus(int weatherId) {
        WeatherUtilBean bean = new WeatherUtilBean();
        bean.weatherId = weatherId;
        switch (weatherId) {
            case 100:
                bean.weather = "晴";
                bean.iconRes = R.mipmap.a100;
                break;
            case 101:
                bean.weather = "多云";
                bean.iconRes = R.mipmap.a101;
                break;
            case 102:
                bean.weather = "少云";
                bean.iconRes = R.mipmap.a102;
                break;
            case 103:
                bean.weather = "晴间多云";
                bean.iconRes = R.mipmap.a103;
                break;
            case 104:
                bean.weather = "阴";
                bean.iconRes = R.mipmap.a104;
                break;
            case 200:
                bean.weather = "有风";
                bean.iconRes = R.mipmap.a200;
            case 201:
                bean.weather = "平静";
                bean.iconRes = R.mipmap.a201;
                break;
            case 202:
                bean.weather = "微风";
                bean.iconRes = R.mipmap.a202;
                break;
            case 203:
                bean.weather = "和风";
                bean.iconRes = R.mipmap.a203;
                break;
            case 204:
                bean.weather = "清风";
                bean.iconRes = R.mipmap.a204;
                break;
            case 205:
                bean.weather = "强风/劲风";
                bean.iconRes = R.mipmap.a205;
                break;
            case 206:
                bean.weather = "疾风";
                bean.iconRes = R.mipmap.a206;
            case 207:
                bean.weather = "大风";
                bean.iconRes = R.mipmap.a207;
                break;
            case 208:
                bean.weather = "烈风";
                bean.iconRes = R.mipmap.a208;
                break;
            case 209:
                bean.weather = "风暴";
                bean.iconRes = R.mipmap.a209;
                break;
            case 210:
                bean.weather = "狂爆风";
                bean.iconRes = R.mipmap.a211;
                break;
            case 211:
                bean.weather = "飓风";
                bean.iconRes = R.mipmap.a211;
                break;
            case 212:
                bean.weather = "龙卷风";
                bean.iconRes = R.mipmap.a212;
            case 213:
                bean.weather = "热带风暴";
                bean.iconRes = R.mipmap.a213;
                break;
            case 300:
                bean.weather = "阵雨";
                bean.iconRes = R.mipmap.a300;
                break;
            case 301:
                bean.weather = "强阵雨";
                bean.iconRes = R.mipmap.a301;
                break;
            case 302:
                bean.weather = "雷阵雨";
                bean.iconRes = R.mipmap.a302;
                break;
            case 303:
                bean.weather = "强雷阵雨";
                bean.iconRes = R.mipmap.a303;
                break;
            case 304:
                bean.weather = "雷阵雨伴有冰雹";
                bean.iconRes = R.mipmap.a304;
            case 305:
                bean.weather = "小雨";
                bean.iconRes = R.mipmap.a305;
                break;
            case 306:
                bean.weather = "中雨";
                bean.iconRes = R.mipmap.a306;
                break;
            case 307:
                bean.weather = "大雨";
                bean.iconRes = R.mipmap.a307;
                break;
            case 308:
                bean.weather = "极端降雨";
                bean.iconRes = R.mipmap.a307;
            case 309:
                bean.weather = "毛毛雨/细雨";
                bean.iconRes = R.mipmap.a309;
                break;
            case 310:
                bean.weather = "暴雨";
                bean.iconRes = R.mipmap.a310;
                break;
            case 311:
                bean.weather = "大暴雨";
                bean.iconRes = R.mipmap.a311;
                break;
            case 312:
                bean.weather = "特大暴雨";
                bean.iconRes = R.mipmap.a312;
            case 313:
                bean.weather = "冻雨";
                bean.iconRes = R.mipmap.a313;
                break;
            case 314:
                bean.weather = "小到中雨";
                bean.iconRes = R.mipmap.a314;
                break;
            case 315:
                bean.weather = "中到大雨";
                bean.iconRes = R.mipmap.a315;
                break;
            case 316:
                bean.weather = "大到暴雨";
                bean.iconRes = R.mipmap.a316;
            case 317:
                bean.weather = "暴雨到大暴雨";
                bean.iconRes = R.mipmap.a317;
                break;
            case 318:
                bean.weather = "大暴雨到特大暴雨";
                bean.iconRes = R.mipmap.a318;
                break;
            case 399:
                bean.weather = "雨";
                bean.iconRes = R.mipmap.a399;
                break;
            case 400:
                bean.weather = "小雪";
                bean.iconRes = R.mipmap.a400;
            case 401:
                bean.weather = "中雪";
                bean.iconRes = R.mipmap.a401;
                break;
            case 402:
                bean.weather = "大雪";
                bean.iconRes = R.mipmap.a402;
                break;
            case 403:
                bean.weather = "暴雪";
                bean.iconRes = R.mipmap.a403;
                break;
            case 404:
                bean.weather = "雨夹雪";
                bean.iconRes = R.mipmap.a404;
            case 405:
                bean.weather = "雨雪天气";
                bean.iconRes = R.mipmap.a405;
                break;
            case 406:
                bean.weather = "阵雨夹雪";
                bean.iconRes = R.mipmap.a406;
                break;
            case 407:
                bean.weather = "阵雪";
                bean.iconRes = R.mipmap.a407;
                break;
            case 408:
                bean.weather = "小到中雪";
                bean.iconRes = R.mipmap.a408;
            case 409:
                bean.weather = "中到大雪";
                bean.iconRes = R.mipmap.a409;
                break;
            case 410:
                bean.weather = "大到暴雪";
                bean.iconRes = R.mipmap.a410;
            case 499:
                bean.weather = "雪";
                bean.iconRes = R.mipmap.a499;
                break;
            case 500:
                bean.weather = "薄雾";
                bean.iconRes = R.mipmap.a500;
            case 501:
                bean.weather = "雾";
                bean.iconRes = R.mipmap.a501;
                break;
            case 502:
                bean.weather = "霾";
                bean.iconRes = R.mipmap.a502;
            case 503:
                bean.weather = "扬沙";
                bean.iconRes = R.mipmap.a503;
                break;
            case 504:
                bean.weather = "浮尘";
                bean.iconRes = R.mipmap.a504;
            case 507:
                bean.weather = "沙尘暴";
                bean.iconRes = R.mipmap.a507;
                break;
            case 508:
                bean.weather = "强沙尘暴";
                bean.iconRes = R.mipmap.a508;
            case 509:
                bean.weather = "浓雾";
                bean.iconRes = R.mipmap.a509;
                break;
            case 510:
                bean.weather = "强浓雾";
                bean.iconRes = R.mipmap.a510;
            case 511:
                bean.weather = "中度霾";
                bean.iconRes = R.mipmap.a511;
                break;
            case 512:
                bean.weather = "重度霾";
                bean.iconRes = R.mipmap.a512;
            case 513:
                bean.weather = "严重霾";
                bean.iconRes = R.mipmap.a513;
                break;
            case 514:
                bean.weather = "大雾";
                bean.iconRes = R.mipmap.a514;
            case 515:
                bean.weather = "特强浓雾";
                bean.iconRes = R.mipmap.a515;
                break;
            case 900:
                bean.weather = "热";
                bean.iconRes = R.mipmap.a900;
                break;
            case 901:
                bean.weather = "冷";
                bean.iconRes = R.mipmap.a901;
                break;
            case 999:
            default:
                bean.weather = "未知";
                bean.iconRes = R.mipmap.a999;
                break;
        }
        return bean;
    }

    public static String getAqi(int aqi) {
        if (aqi >= 10) {
            return "优";
        } else if (aqi >= 5) {
            return "良";
        } else if (aqi >= 3) {
            return "轻度";
        } else if (aqi >= 1) {
            return "中度";
        } else if (aqi >= 0.5) {
            return "重度";
        } else if (aqi >= 0) {
            return "严重";
        }
        return "";
    }

    /**
     * 根据当前日期获得是星期几
     * time=yyyy-MM-dd
     * @return
     */
    public static String getWeek(String time) {
        String Week = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int wek=c.get(Calendar.DAY_OF_WEEK);

        if (wek == 1) {
            Week += "星期日";
        }
        if (wek == 2) {
            Week += "星期一";
        }
        if (wek == 3) {
            Week += "星期二";
        }
        if (wek == 4) {
            Week += "星期三";
        }
        if (wek == 5) {
            Week += "星期四";
        }
        if (wek == 6) {
            Week += "星期五";
        }
        if (wek == 7) {
            Week += "星期六";
        }
        return Week;
    }

}
