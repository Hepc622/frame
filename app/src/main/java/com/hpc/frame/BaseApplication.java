package com.hpc.frame;

import android.app.Application;
import android.content.Context;

import com.hpc.frame.network.NetWorkManager;
import com.tencent.bugly.Bugly;

/**
 * 基础application 用户初始化工具
 */
public class BaseApplication extends Application {
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        /*初始化网络请求*/
        initNetwork();
        /*升级检测*/
        initUpdate();
    }
    /**
     * 初始化升级更新
     */
    private void initUpdate() {
        Bugly.init(context, "d48bdb0331", false);
    }

    /**
     * 初始化网络
     */
    private void initNetwork() {
        NetWorkManager.getInstance().init();
    }
}
