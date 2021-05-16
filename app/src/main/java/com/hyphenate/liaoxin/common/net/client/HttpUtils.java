package com.hyphenate.liaoxin.common.net.client;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.hyphenate.cloud.HttpResponse;
import com.hyphenate.liaoxin.DemoApplication;
import com.hyphenate.liaoxin.common.constant.UserConstant;
import com.hyphenate.liaoxin.common.db.PrefUtils;
import com.hyphenate.liaoxin.common.net.callback.ResultCallBack;
import com.hyphenate.liaoxin.common.net.interceptor.LoggingInterceptor;
import com.hyphenate.liaoxin.common.net.request.BaseRequest;

import org.json.JSONObject;

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

    public void post(final Context context,String url, String str, ResultCallBack callBack){
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(mediaType, str))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailure(call,e);
                    }
                });
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
                    ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (request1 != null && request1.returnCode == 0 && response.code() == 200){
                                callBack.onSuccessResponse(call,str);
                            }else {
                                callBack.onFailure(call,null);
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

//    public static String getToken(String url, String appid, String secret) throws Exception {
//        String resultStr = null;
//        @SuppressWarnings("resource")
//        DefaultHttpClient httpClient = new DefaultHttpClient();
//        HttpPost post = new HttpPost(url);
//        //JsonParser jsonparer =JsonParser;// 初始化解析json格式的对象
//        // 接收参数json列表
//        JSONObject jsonParam = new JSONObject();
//        jsonParam.put("grant_type", "client_credentials");
//        jsonParam.put("client_id", appid);
//        jsonParam.put("client_secret", secret);
//        StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");// 解决中文乱码问题
//        entity.setContentEncoding("UTF-8");
//        entity.setContentType("application/json");
//
//        post.setEntity(entity);
//        // 请求结束，返回结果
//        HttpResponse res = httpClient.execute(post);
//        // 如果服务器成功地返回响应
//        String responseContent = null; // 响应内容
//        HttpEntity httpEntity = res.getEntity();
//        responseContent = EntityUtils.toString(httpEntity, "UTF-8");
//
//        //System.out.println( responseContent);
//        //JsonObject json = JsonParser.parse(responseContent);
//        JSONObject json = JSONObject.parseObject(responseContent);
//        // .getAsJsonObject();
//        if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//            if (json.get("errcode") != null){
//                //resultStr = json.get("errcode").getAsString();
//            } else {// 正常情况下
//                resultStr = json.get("access_token").toString();
//            }
//        }
//         // 关闭连接 ,释放资源
//        httpClient.close();
//        return resultStr;
//    }

}
