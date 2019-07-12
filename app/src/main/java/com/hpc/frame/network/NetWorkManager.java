package com.hpc.frame.network;

import com.hpc.frame.BuildConfig;
import com.hpc.frame.network.interceptor.CaheInterceptor;
import com.hpc.frame.network.interceptor.NetWorkInterceptor;
import com.hpc.frame.network.interceptor.TokenInterceptor;
import com.hpc.frame.network.request.IBaseRequest;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author : HPC
 * @description : 网络管理连接器
 * @date : 2019/7/12 10:52
 */
public class NetWorkManager {

    private static NetWorkManager mInstance;
    private static Retrofit retrofit;

    public static NetWorkManager getInstance() {
        if (mInstance == null) {
            synchronized (NetWorkManager.class) {
                if (mInstance == null) {
                    mInstance = new NetWorkManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化必要对象和参数
     */
    public void init() {
        // 初始化okhttp
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.connectTimeout(9, TimeUnit.SECONDS);
        builder.addInterceptor(new NetWorkInterceptor());
        builder.addInterceptor(new TokenInterceptor());
        builder.addInterceptor(new CaheInterceptor());

        /*debugger模式打印日志*/
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }
        /*创建client*/
        OkHttpClient client = builder.build();

        // 初始化Retrofit
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(IBaseRequest.HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * @description : 获取请求对象
     * @author : HPC
     * @date : 2019/7/12 17:02
     * @return retrofit2.Retrofit
     */
    public static Retrofit getRetrofit() {
        return retrofit;
    }
}