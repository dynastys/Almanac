package com.xy.xylibrary.utils;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.xy.xylibrary.service.DownService;

import java.io.File;
import java.io.IOException;


/**
 * 下载工具类集合  http://img.xy1732.cn/update/ttnz/app-VIVO-release1.0.1.apk
 **/
@SuppressLint("NewApi")
public class DownLoadUtils {

    // sd开路径
    public static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    // 设置两个字段
    public static String downloadPath;
    private Context context;
    private String userAgent;

    public DownLoadUtils(Context context) {
        super();
        this.context = context;
    }

    @SuppressLint("NewApi")
    public void downloadFile(final Context context, final String url) {
        userAgent = Utils.getDefaultUserAgentString(context);
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        DownReceiver(context, url);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            System.out.println("+++++下载开始+++++");
        } catch (Exception e) {
        }

    }
    /** 
      * 判断是否是8.0系统,是的话需要获取此权限，判断开没开，没开的话处理未知应用来源权限问题,否则直接安装 
      */
    public void checkIsAndroidO(Activity context, String path) {
         if(Build.VERSION.SDK_INT >= 26){
            boolean b = context.getPackageManager().canRequestPackageInstalls();
            if(b){
                doApk(path);
            }else{
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, 12);
            }
        }else{
            doApk(path);
        }
    }
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressLint("NewApi")
    private void DownReceiver(final Context context, final String url) {
        try {
            // 创建一个下载存储位置
            String downloadStringName = url.substring(url.lastIndexOf("/") + 1, url.length());
            downloadPath = PATH + "/" + downloadStringName;
            System.out.println("开始下载，下载的地址-------->" + downloadPath);
            File file = new File(downloadPath);
            // 下载
            DownloadManager downloadManager = (DownloadManager) (context
                    .getSystemService(Context.DOWNLOAD_SERVICE));
            Uri downloadUri = Uri.parse(url);
            Request request = new Request(downloadUri);
            request.setDestinationUri(Uri.fromFile(file));
            long id = downloadManager.enqueue(request);
            //启动下载service
            Intent intentOne = new Intent(context, DownService.class);
            intentOne.putExtra("file", downloadPath);
            context.startService(intentOne);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public String getVersion(Context context) {
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
     * 获取指定包名的版本号
     *
     * @param context
     *            本应用程序上下文
     * @param packageName
     *            你想知道版本信息的应用程序的包名
     * @return
     * @throws Exception
     */
    public String getVersionName(Context context, String packageName) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        return version;
    }

    // 获取下载的apk包名
    public String getDownLoadPackageName(Context context, String path) {
        String packageName;
        packageName = "";
        try {
            // 获取packageManager
            PackageManager pm = context.getPackageManager();
            // 获取包存放的信息
            PackageInfo packageArchiveInfo = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
            if (packageArchiveInfo != null) {
                // 获取application
                ApplicationInfo appInfo = packageArchiveInfo.applicationInfo;
                packageName = appInfo.packageName;
                System.out.println("***************" + packageName);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return packageName;
    }

    /**
     * 获取apk包的信息：版本号，名称，图标等
     *
     * @param absPath  apk包的绝对路径
     * @param context 
     */
    public void apkInfo(String absPath, Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = pm.getPackageArchiveInfo(absPath, PackageManager.GET_ACTIVITIES);
        if (pkgInfo != null) {
            ApplicationInfo appInfo = pkgInfo.applicationInfo;
            /* 必须加这两句，不然下面icon获取是default icon而不是应用包的icon */
            appInfo.sourceDir = absPath;
            appInfo.publicSourceDir = absPath;
            String appName = pm.getApplicationLabel(appInfo).toString();// 得到应用名 
            String packageName = appInfo.packageName; // 得到包名 
            String version = pkgInfo.versionName; // 得到版本信息 
            /* icon1和icon2其实是一样的 */
            Drawable icon1 = pm.getApplicationIcon(appInfo);// 得到图标信息 
            Drawable icon2 = appInfo.loadIcon(pm);
            String pkgInfoStr = String.format("PackageName:%s, Vesion: %s, AppName: %s", packageName, version, appName);
            Log.i("TAG", String.format("PkgInfo: %s", pkgInfoStr));
        }
    }

    public void doApk(String path) {
        try {
            setPermission(path);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            File file = (new File(path));
            //版本在7.0以上是不能直接通过uri访问的
            if (Build.VERSION.SDK_INT > 23) {
                // 由于没有在Activity环境下启动Activity,设置下面的标签
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                Uri apkUri = FileProvider.getUriForFile(context, "com.zt.weather.fileProvider", file);
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            } else {
                // 跳转到系统安装页面
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setDataAndType(Uri.fromFile(file),
                        "application/vnd.android.package-archive");
                // 会返回结果,回调方法onActivityResul
            }
            context.startActivity(intent);


        } catch (Exception e) {
            System.out.println("安装错误------>" + e.getMessage());
        }

    }

    /**
     * 提升读写权限
     * @param filePath 文件路径
     * @return
     * @throws IOException
     */
    public static void setPermission(String filePath) {
        String command = "chmod " + "777" + " " + filePath;
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
