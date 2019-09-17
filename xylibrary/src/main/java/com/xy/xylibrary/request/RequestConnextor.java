package com.xy.xylibrary.request;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.tencent.smtt.sdk.WebSettings;
import com.xy.xylibrary.utils.AESUtils;
import com.xy.xylibrary.utils.SaveShare;
import com.xy.xylibrary.utils.Utils;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zw on 2017/9/12.
 * 网络请求
 */

public class RequestConnextor<T> {

    private T appService;
    private T appService2;
    private OkHttpClient okhttp;
    private static RequestConnextor connextor;
    private static Context context;
    private static String url = "https://api.heweather.net/";
    public static RequestConnextor getConnextor(Context contexts) {
        if (connextor == null) {
            synchronized (RequestConnextor.class){
                if (connextor == null) {
                    connextor = new RequestConnextor();
                }
            }
        }
        return connextor;
    }

    public void setContext(Context context) {
        RequestConnextor.context = context;
    }

    public T getAppService(Class<T> T) {

        if(!url.equals("https://api.heweather.net/") || appService == null){
            url = "https://api.heweather.net/";
            appService = RetrofitRxjavaGet(T);
        }
        return appService;
    }

    public T getAppService(Class<T> T,String url) {
        this.url = url;
        appService2 = RetrofitRxjavaGet(T);
        return appService2;
    }

    //离线状态
  static  Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            try {
                Request request = chain.request()
                        .newBuilder()
                        .removeHeader("User-Agent")//移除旧的
                        .addHeader("User-Agent", WebSettings.getDefaultUserAgent(context))//添加真正的头部
                        .build();
                /**
                 * 未联网获取缓存数据
                 */
                if (!Utils.isNetworkAvailable(context)) {
                    //在20秒缓存有效，此处测试用，实际根据需求设置具体缓存有效时间
                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxStale(365, TimeUnit.DAYS)
                            .build();
                    request = request.newBuilder()
                            .cacheControl(cacheControl)
                            .build();
                }
                return chain.proceed(request);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    };

    private static class TokenHeaderInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            String utoken = SaveShare.getValue(context, "utoken");
            Log.e("requestData", utoken);
            Request originalRequest = chain.request();
            if(TextUtils.isEmpty(utoken)){
                return chain.proceed(originalRequest);
            }else{
                Request updateRequest = originalRequest.newBuilder() .header("utoken", utoken.trim()).build();
                return chain.proceed(updateRequest);
            }
        }
    }

    //正常缓存，减少服务器请求
    static Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            if (!Utils.isNetworkAvailable(context)) {
                CacheControl.Builder cacheBuilder = new CacheControl.Builder();
                cacheBuilder.maxStale(30, TimeUnit.DAYS);
                CacheControl cacheControls = cacheBuilder.build();
                Toast.makeText(context,"请检测网络！", Toast.LENGTH_LONG).show();
                Request request = chain.request();
                request = request.newBuilder()
                        .cacheControl(cacheControls)
                        .build();
                Response response = chain.proceed(request);
                //获取头部信息
                String cacheControl =request.cacheControl().toString();
                return response.newBuilder()
                        .removeHeader("Pragma")//清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .header("Cache-Control", cacheControl)
//
                        .build();//
            } else {
                Request request = chain.request();
                Response response = chain.proceed(request);
                 //获取头部信息
                String cacheControl =request.cacheControl().toString();
                return response.newBuilder()
                        .removeHeader("Pragma")//清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .header("Cache-Control", cacheControl)
//                        .header("utoken", AESUtils.getInstance().encrypt2(System.currentTimeMillis()+""))
                        .build();
            }

        }
    };

    private void ignoreSSLCheck() {
         SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }}, new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }

        HostnameVerifier hv1 = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        String workerClassName = "okhttp3.OkHttpClient";
        try {
            Class workerClass = Class.forName(workerClassName);
            Field hostnameVerifier = workerClass.getDeclaredField("hostnameVerifier");
            hostnameVerifier.setAccessible(true);
            hostnameVerifier.set(okhttp, hv1);

            Field sslSocketFactory = workerClass.getDeclaredField("sslSocketFactory");
            sslSocketFactory.setAccessible(true);
            sslSocketFactory.set(okhttp, sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private T RetrofitRxjavaGet(Class<T> T){
        /*
        * 在retrofit2.0中是没有日志功能的。但是retrofit2.0中依赖OkHttp，所以也就能够通过OkHttp中的interceptor来实现实际的底层的请求和响应日志。
        * 在这里我们需要修改上一个retrofit实例，为其自定自定义的OkHttpClient。代码如下：
        * Retrofit与RxJava结合
        * .addNetworkInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Response response = chain.proceed(chain.request());
                return response.newBuilder().body(new ProgressResponseBody(response.body())).build();
            }
        })
        * */

        File httpCacheDirectory = new File(Environment.getExternalStorageDirectory(), "Novel");//这里为了方便直接把文件放在了SD卡根目录的HttpCache中，一般放在context.getCacheDir()中
        int cacheSize = 10 * 1024 * 1024;//设置缓存文件大小为10M
        Cache cache = new Cache(httpCacheDirectory, cacheSize);
        okhttp = new OkHttpClient.Builder()
          .addInterceptor(interceptor)//离线
//          .followRedirects(true)
          .connectTimeout(10, TimeUnit.SECONDS)
          .readTimeout(10, TimeUnit.SECONDS)
          .writeTimeout(10, TimeUnit.SECONDS)
          .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)//添加自定义缓存拦截器（后面讲解），注意这里需要使用.addNetworkInterceptor
          .addNetworkInterceptor(new TokenHeaderInterceptor())
          .cache(cache)//把缓存添加进来
          .build();
//        Request request = new Request.Builder().url(url).removeHeader("User-Agent").addHeader("User-Agent",
//                "Dalvik/2.1.0 (Linux; U; Android 7.1.2; MI 5C MIUI/V10.2.1.0.NCJCNXM)").build();
//        okhttp.newCall(request).enqueue();
        ignoreSSLCheck();
        return new Retrofit.Builder().client(okhttp)
                .validateEagerly(true)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(url)
                .build()
                .create(T);
    }


}
