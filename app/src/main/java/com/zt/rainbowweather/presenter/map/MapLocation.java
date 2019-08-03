package com.zt.rainbowweather.presenter.map;

import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.AppOpsManagerCompat;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.xy.xylibrary.utils.SaveShare;
import com.zt.rainbowweather.BasicApplication;
import com.zt.rainbowweather.entity.City;
import com.zt.rainbowweather.utils.ConstUtils;
import com.zt.rainbowweather.utils.UpdateDialog;

import org.litepal.LitePal;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 定位
 */
public class MapLocation implements AMapLocationListener {

    private static MapLocation mapLocation;
    //声明mlocationClient对象
    private static AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    private AMapLocationClientOption mLocationOption = null;
    private Context context;
    private Dismiss dismiss;
    private City locatedCity;
    private static List<City> citys = new ArrayList<>();

    public static MapLocation getMapLocation() {
        if (mapLocation == null) {
            synchronized (MapLocation.class) {
                if (mapLocation == null) {
                    mapLocation = new MapLocation();
                }
            }
        }
        return mapLocation;
    }

    public void locate(Context context) {
        try {
            this.context = context;
            locatedCity = null;
            citys.clear();
            mlocationClient = new AMapLocationClient(context);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置返回地址信息，默认为true
            mLocationOption.setNeedAddress(true);
            //设置定位监听
            mlocationClient.setLocationListener(MapLocation.this);
            //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        //设置定位间隔,单位毫秒,默认为2000ms
//        mLocationOption.setInterval(2000);
            //获取一次定位结果：
            //该方法默认为false。
            mLocationOption.setOnceLocation(true);
            //获取最近3s内精度最高的一次定位结果：
//       设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
//        mLocationOption.setOnceLocationLatest(true);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            //启动定位
            mlocationClient.startLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startLocation() {
        if (mlocationClient != null) {
            mlocationClient.startLocation();
        } else {
            locate(context);
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        try {
            if (locatedCity == null && !TextUtils.isEmpty(aMapLocation.getCity())) {
                locatedCity = new City();
//                aMapLocation.setCity("深圳");
//                aMapLocation.setDistrict("宝安区");
//                aMapLocation.setStreet("五一大道");
                locatedCity.affiliation = aMapLocation.getDistrict() + " " + aMapLocation.getStreet();
                if (aMapLocation != null && aMapLocation.getLatitude() != 0 && aMapLocation
                        .getLongitude() != 0) {
                    locatedCity.longitude = aMapLocation.getLongitude();
                    locatedCity.latitude = aMapLocation.getLatitude();
                    locatedCity.name = aMapLocation.getCity();
                    locatedCity.affiliation = aMapLocation.getDistrict() + " " + aMapLocation.getStreet();
                } else {
                    locatedCity.affiliation = locatedCity.name = ConstUtils.LOCATE_FAILED;
                }
                locatedCity.isLocate = "1";
                locatedCity.isChecked = "1";
                assert aMapLocation != null;

                citys = LitePal.findAll(City.class);
                List<City> cities = LitePal.where("name=?", aMapLocation.getCity()).find(City.class);
                if (cities == null || cities.size() == 0 && citys.size() == 0) {
//                    locatedCity.save();
//                    citys.add(locatedCity);
                //    return;
                } else {
                    LitePal.deleteAll(City.class, "name=?", aMapLocation.getCity());
                    List<City> isLocateCity = LitePal.where("isLocate=?", "1").find(City.class);
                    if(isLocateCity != null && isLocateCity.size() > 0){
//                        LitePal.deleteAll(City.class, "isLocate=?", "1");
                        isLocateCity.get(0).isChecked = "0";
                        isLocateCity.get(0).isLocate = "0";
                    }
                    ContentValues values = new ContentValues();
                    values.put("isChecked", "0");
                    LitePal.updateAll(City.class, values, "isChecked = ?", "1");
                    ContentValues isLocate = new ContentValues();
                    isLocate.put("isLocate", "0");
                    LitePal.updateAll(City.class, isLocate, "isLocate = ?", "1");
                    cities = null;
                    citys = LitePal.findAll(City.class);
//                    if(isLocateCity != null && isLocateCity.size() > 0){
//                        citys.add(0,isLocateCity.get(0));
//                    }

                }

                if (cities == null || cities.size() == 0) {
//                    ContentValues values2 = new ContentValues();
//                    values2.put("longitude", aMapLocation.getLongitude());
//                    values2.put("latitude", aMapLocation.getLatitude());
//                    values2.put("affiliation", aMapLocation.getDistrict() + " " + aMapLocation.getStreet());
                    City city = new City();
                    city.isChecked = "1";
                    city.longitude = aMapLocation.getLongitude();
                    city.latitude = aMapLocation.getLatitude();
                    city.name = aMapLocation.getCity();
                    city.affiliation = aMapLocation.getDistrict() + " " + aMapLocation.getStreet();
                    city.isLocate = "1";
                    citys.add(0, city);
                    LitePal.deleteAll(City.class);
//                    city.save();
                }
//                if(cityList != null && cityList.size() > 0){
                    for (int i = 0; i < citys.size(); i++) {
                        City c = new City();
                        c.isChecked = citys.get(i).isChecked;
                        c.longitude = citys.get(i).longitude;
                        c.latitude = citys.get(i).latitude;
                        c.name = citys.get(i).name;
                        c.affiliation = citys.get(i).affiliation;
                        c.isLocate = citys.get(i).isLocate;
                        c.save();
                    }
////                    LitePal.saveAll(cityList);
//                }
                Log.e("latitude", "onLocationChanged: ");
                BasicApplication.setLocatedCity(locatedCity);
            }else{
                List<City> cities = LitePal.findAll(City.class);
                if(cities != null && cities.size() > 0){
                    BasicApplication.setLocatedCity(cities.get(0));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static List<City> getCitys() {
        return citys != null && citys.size() > 0 ? citys : LitePal.findAll(City.class);
    }

    public void onStop() {
        mlocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
    }

    public void onDestroy() {
        mlocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }

    public void setData() {
        if (LitePal.findAll(City.class) == null || LitePal.findAll(City.class).size() == 0) {
            try {
                City locatedCity = new City();
                locatedCity.name = "杭州市";
                locatedCity.isLocate = "1";
                locatedCity.isChecked = "1";
                locatedCity.affiliation = "杭州";
                List<City> cities = LitePal.where("name=?", "杭州").find(City.class);
                if (cities == null || cities.size() == 0) {
                    locatedCity.save();
                }
                List<City> nameCity = LitePal.where("name=?", locatedCity.name).find(City.class);
                ContentValues values = new ContentValues();
                values.put("isChecked", "0");
                LitePal.updateAll(City.class, values, "isChecked = ?", "1");
                if (cities == null || cities.size() == 0) {
                    nameCity.get(0).isChecked = "1";
                    nameCity.get(0).save();
                }
                BasicApplication.setLocatedCity(locatedCity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 检查定位服务、权限
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkLocationPermission(Activity context, Dismiss dismiss) {
        this.dismiss = dismiss;
        if (TextUtils.isEmpty(SaveShare.getValue(context, "location"))) {
            if (!isLocServiceEnable(context)) {//检测是否开启定位服务
                showLocErrorDialog(context, 0);
            } else {//检测用户是否将当前应用的定位权限拒绝
//                int checkResult = checkOp(context, 2, AppOpsManager.OPSTR_FINE_LOCATION);//其中2代表AppOpsManager.OP_GPS，如果要判断悬浮框权限，第二个参数需换成24即AppOpsManager。OP_SYSTEM_ALERT_WINDOW及，第三个参数需要换成AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW
//                int checkResult2 = checkOp(context, 1, AppOpsManager.OPSTR_FINE_LOCATION);
//                if (ContextCompat.checkSelfPermission(context, Manifest.permission_group.LOCATION) ==
//                        PackageManager.PERMISSION_DENIED) {
                showLocErrorDialog(context, 1);
//                }else{
//                    dismiss.dismiss();
//                }
            }
        }
    }

    /**
     * 手机是否开启位置服务，如果没有开启那么所有app将不能使用定位功能
     */
    public static boolean isLocServiceEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

    /**
     * 检查权限列表
     *
     * @param context
     * @param op       这个值被hide了，去AppOpsManager类源码找，如位置权限  AppOpsManager.OP_GPS==2
     * @param opString 如判断定位权限 AppOpsManager.OPSTR_FINE_LOCATION
     * @return @see 如果返回值 AppOpsManagerCompat.MODE_IGNORED 表示被禁用了
     */
    public int checkOp(Context context, int op, String opString) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            Object object = context.getSystemService(Context.APP_OPS_SERVICE);
//            Object object = context.getSystemService("appops");
            Class c = object.getClass();
            try {
                Class[] cArg = new Class[3];
                cArg[0] = int.class;
                cArg[1] = int.class;
                cArg[2] = String.class;
                Method lMethod = c.getDeclaredMethod("checkOp", cArg);
                return (Integer) lMethod.invoke(object, op, Binder.getCallingUid(), context.getPackageName());
            } catch (Exception e) {
                e.printStackTrace();
                if (Build.VERSION.SDK_INT >= 23) {
                    return AppOpsManagerCompat.noteOp(context, opString, context.getApplicationInfo().uid,
                            context.getPackageName());
                }

            }
        }
        return -1;
    }

    /**
     * 无法定位对话框
     *
     * @param activity 上下文
     * @param state    权限状态0，未开启服务 1，未开启权限
     * @return 对话框
     */
    public void showLocErrorDialog(Activity activity, int state) {
        try {
            SaveShare.saveValue(context, "skip", "skip");
            final UpdateDialog confirmDialog = new UpdateDialog(activity, "提示", "打开定位", "取消", "获取本地天气需要打开定位哦！");
            confirmDialog.show();
            confirmDialog.setClicklistener(new UpdateDialog.ClickListenerInterface() {
                @Override
                public void doConfirm() {
                    SaveShare.saveValue(context, "skip", "skip");
                    SaveShare.saveValue(context, "finish", "finish");
                    confirmDialog.dismiss();
                    Intent intent = new Intent();
                    if (state == 0) {
                        //定位服务页面
                        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    } else {
                        //应用详情页面
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + activity.getPackageName()));
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        activity.startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        //如果页面无法打开，进入设置页面
                        intent.setAction(Settings.ACTION_SETTINGS);
                        try {
                            activity.startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void doCancel() {
                    confirmDialog.dismiss();
                    dismiss.dismiss();
                    SaveShare.saveValue(context, "finish", "finish");
                    SaveShare.saveValue(context, "location", "location");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public interface Dismiss {
        void dismiss();
    }
}
