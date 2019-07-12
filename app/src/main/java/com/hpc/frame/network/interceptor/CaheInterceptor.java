package com.hpc.frame.network.interceptor;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.hpc.frame.BaseApplication;
import com.hpc.frame.utils.NetworkUtil;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @description :  缓存拦截器
 * @author : HPC
 * @date : 2019/7/12 16:30
 */
public class CaheInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (NetworkUtil.isNetworkAvailable()) {
            Response response = chain.proceed(request);
            // read from cache for 60 s
            int maxAge = 60;
            String cacheControl = request.cacheControl().toString();
            Log.e("Tamic", "60s load cahe" + cacheControl);
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
        } else {
            ((Activity) BaseApplication.context).runOnUiThread(() -> Toast.makeText(BaseApplication.context, "当前无网络! 为你智能加载缓存", Toast.LENGTH_SHORT).show());
            Log.e("Tamic", " no network load cahe");
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
            Response response = chain.proceed(request);
            //set cahe times is 3 days
            int maxStale = 60 * 60 * 24 * 3;
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
    }
}