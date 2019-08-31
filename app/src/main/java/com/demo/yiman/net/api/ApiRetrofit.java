package com.demo.yiman.net.api;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import com.demo.yiman.base.gson.BaseConverterFacory;
import com.demo.yiman.net.ApiConfig;
import com.demo.yiman.net.api.ApiServer;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiRetrofit {

    private static ApiRetrofit apiRetrofit;
    public static Retrofit retrofit;
    private OkHttpClient client;
    private ApiServer apiServer;
    private String TAG = "ApiRetrofit";


    /**
     * 请求访问
     * response拦截器
     */
    public Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long startTime = System.currentTimeMillis();
            Response response = chain.proceed(chain.request());
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            Log.e(TAG,"---------Request Start----------");
            Log.e(TAG,"|"+ request.toString()+request.headers().toString());
            Log.e(TAG,"|Response:"+ content);
            Log.e(TAG,"---------Request End:"+duration+"毫秒--------");
            return response.newBuilder().body(ResponseBody.create(mediaType,content)).build();
        }
    };

    /**
     * 构造
     * 供实例化
     */
    public ApiRetrofit(){
        client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URL)
                .addConverterFactory(BaseConverterFacory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        apiServer = retrofit.create(ApiServer.class);
    }
    /**
     * 单例
     */
    public static ApiRetrofit getInstance(){
        if (apiRetrofit == null){
            synchronized (Object.class){
                if (apiRetrofit == null){
                    apiRetrofit = new ApiRetrofit();
                }
            }
        }
        return apiRetrofit;
    }

    public ApiServer getApiService(){
       return apiServer;
    }

}
