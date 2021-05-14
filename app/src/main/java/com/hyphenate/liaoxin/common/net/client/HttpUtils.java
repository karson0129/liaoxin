package com.hyphenate.liaoxin.common.net.client;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.hyphenate.liaoxin.DemoApplication;
import com.hyphenate.liaoxin.common.constant.UserConstant;
import com.hyphenate.liaoxin.common.db.PrefUtils;
import com.hyphenate.liaoxin.common.net.callback.ResultCallBack;
import com.hyphenate.liaoxin.common.net.interceptor.LoggingInterceptor;
import com.hyphenate.liaoxin.common.net.request.BaseRequest;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {

    private static String TAG = "HttpUtils";

    private static HttpUtils utils;

    private MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

    private static OkHttpClient client;

    private HttpUtils(){
        initClient();
    }

    public static HttpUtils getInstance(){
        if (utils == null){
            synchronized (HttpUtils.class){
                if (utils == null){
                    utils = new HttpUtils();
                }
            }
        }
        return utils;
    }

    private void initClient(){
        client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(new LoggingInterceptor())
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {

                        if ( !TextUtils.isEmpty(PrefUtils.getString(DemoApplication.getInstance(), UserConstant.Token,"")) ){
                            Request request = chain.request()
                                    .newBuilder()
                                    .addHeader("token",PrefUtils.getString(DemoApplication.getInstance(), UserConstant.Token,""))
                                    .build();
                            return chain.proceed(request);
                        }else {
                            Request request = chain.request();
                            Response response = chain.proceed(request);
                            return response;
                        }
                    }
                })
                .retryOnConnectionFailure(false)
                .build();
    }

    public void post(String url, String str,ResultCallBack callBack){
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(mediaType, str))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                callBack.onFailure(call,e);
            }

            @Override
            public void onResponse(Call call, Response response) {
                Log.d(TAG, response.protocol() + " " +response.code() + " " + response.message());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    Log.d(TAG, headers.name(i) + ":" + headers.value(i));
                }
                try {
                    String str = response.body().string();
                    Log.d(TAG, "onResponse: " + str);
                    BaseRequest request1 = new Gson().fromJson(str,BaseRequest.class);

                    if (request1 != null && request1.returnCode == 0 && response.code() == 200){
                        callBack.onSuccessResponse(call,response,str);
                    }else {
                        callBack.onFailure(call,null);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
