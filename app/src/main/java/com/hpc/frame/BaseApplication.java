package com.hpc.frame;

import android.app.Application;
import android.content.Context;

import com.hpc.frame.network.NetWorkManager;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * 基础application 用户初始化工具
 */
public class BaseApplication extends Application {
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        /*初始化网络请求*/
        NetWorkManager.getInstance().init();
        context = getApplicationContext();
        /*升级检测*/
        CrashReport.initCrashReport(context, "d48bdb0331", false);
    }
}
