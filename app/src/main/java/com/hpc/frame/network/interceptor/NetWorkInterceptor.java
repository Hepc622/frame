package com.hpc.frame.network.interceptor;

import android.app.Activity;
import android.widget.Toast;

import com.hpc.frame.utils.NetworkUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.hpc.frame.BaseApplication.context;

/**
 * @author : HPC
 * @description : 网络检测拦截器
 * @date : 2019/7/12 15:12
 */
public class NetWorkInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        /*检查网络是否可用如果可用放行*/
        if (NetworkUtil.isNetworkAvailable()) {
            return chain.proceed(request);
        } else {
            ((Activity) context).runOnUiThread(() -> Toast.makeText(context, "当前无网络! 请检查网咯", Toast.LENGTH_SHORT).show());
            return null;
        }
    }
}
