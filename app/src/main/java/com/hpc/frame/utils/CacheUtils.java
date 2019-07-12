package com.hpc.frame.utils;

import android.content.SharedPreferences;

import com.alibaba.fastjson.JSONObject;

import static com.hpc.frame.BaseApplication.context;


/**
 * 缓存工具类
 */
public class CacheUtils {

    public static void pushCache(String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sp", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
        editor.commit();
    }

    public static String getCache(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sp", context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static JSONObject getCacheUser() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sp", context.MODE_PRIVATE);
        String user = sharedPreferences.getString("user", "{}");
        JSONObject json = JSONObject.parseObject(user);
        return json;
    }

    /**
     * 获取用户名
     *
     * @return 用户名
     */
    public static String getCacheUserName() {
        JSONObject cacheUser = getCacheUser();
        return cacheUser.getString("userName");
    }

    public static String getCacheToken() {
        JSONObject json = getCacheUser();
        return json.getString("token");
    }

    public static void removeUser() {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("sp", context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("user", null);
            editor.apply();
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
