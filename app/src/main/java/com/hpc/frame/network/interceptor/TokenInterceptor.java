package com.hpc.frame.network.interceptor;

import android.app.Activity;
import android.widget.Toast;

import com.hpc.frame.BaseApplication;
import com.hpc.frame.utils.CacheUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.hpc.frame.BaseApplication.context;

/**
 * @author : HPC
 * @description :  token拦截器
 * @date : 2019/7/12 15:55
 */
public class TokenInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        /*判断是否有token，没有token提示去登录*/
        String token = CacheUtils.getCacheToken();
        if (token != null) {
            /*添加token*/
            Request.Builder builder = request.newBuilder().addHeader("token", token);
            request = builder.build();
            return chain.proceed(request);
        } else {
            ((Activity) context).runOnUiThread(() -> Toast.makeText(context, "无效token,请重新登录", Toast.LENGTH_SHORT).show());
            return null;
        }
    }
}
