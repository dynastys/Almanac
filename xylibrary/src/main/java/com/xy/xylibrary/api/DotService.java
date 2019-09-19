package com.xy.xylibrary.api;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface DotService {

    @POST("log/app-page-action")
    Observable<String> AppDotPage(@Body RequestBody body);

    @POST("log/app-icon-action")
    Observable<String> AppDotIcon(@Body RequestBody body);

    @POST("api/v1/device-actocate")
    Observable<Object> AppActivate(@Body RequestBody body);
}
