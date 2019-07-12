//package com.hpc.frame.utils;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.widget.Toast;
//
//import com.alibaba.fastjson.JSONObject;
//import com.vtv.ampdg.login.LoginActivity;
//import com.zyao89.view.zloading.ZLoadingDialog;
//import com.zyao89.view.zloading.Z_TYPE;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
//import okhttp3.FormBody;
//import okhttp3.MediaType;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//
///**
// * http请求类
// */
//public class HttpUtils {
//    private static final int CONNECT_TIMEOUT = 60;
//    private static final int READ_TIMEOUT = 60;
//    private static final int WRITE_TIMEOUT = 60;
//
//    private static OkHttpClient getOkHttpClinet() {
//        return new OkHttpClient.Builder()
//                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS) //连接超时
//                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS) //读取超时
//                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//写超时
//                .build();
//    }
//
//    public static void get(final String url, final Activity activity, final Handler handler) {
//        boolean beforeRequest = checkBeforeRequest(activity, url);
//        if (beforeRequest) {
//            ZLoadingDialog dialog = new ZLoadingDialog(activity);
//            dialog.setLoadingBuilder(Z_TYPE.CIRCLE)//设置类型
//                    .setLoadingColor(Color.BLACK)//颜色
//                    .setHintText("Loading...")
//                    .setCanceledOnTouchOutside(false)
//                    .show();
//            new Thread(() -> {
//                OkHttpClient client = getOkHttpClinet();
//                Request request = new Request.Builder()
//                        .addHeader("token", CacheUtils.getCacheToken(activity))
//                        .url(CacheUtils.getServerLink(activity) + url)
//                        .build();
//                try {
//                    Response response = client.newCall(request).execute();
//                    if (response.isSuccessful()) {
//                        dialog.dismiss();
//                        Message message = new Message();
//                        message.obj = JSONObject.parseObject(response.body().string(), HashMap.class);
//                        message.what = 0;
//                        handler.sendMessage(message);
//                    } else {
//                        Looper.prepare();
//                        int code = response.code();
//                        if (code >= 500) {
//                            Toast.makeText(activity, "系统繁忙", Toast.LENGTH_SHORT).show();
//                        } else if (code > 300) {
//                            Toast.makeText(activity, "请求路径不存在", Toast.LENGTH_SHORT).show();
//                        }
//                        Looper.loop();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Looper.prepare();
//                    Toast.makeText(activity, "系统繁忙", Toast.LENGTH_SHORT).show();
//                    Looper.loop();
//                }
//            }).start();
//        }
//    }
//
//    public static void postWithForm(final String url, final Map<String, String> map,
//                                    final Activity activity, final Handler handler) {
//        /*检查网咯和权限*/
//        boolean beforeRequest = checkBeforeRequest(activity, url);
//        if (beforeRequest) {
//            ZLoadingDialog dialog = new ZLoadingDialog(activity);
//            dialog.setLoadingBuilder(Z_TYPE.CIRCLE)//设置类型
//                    .setLoadingColor(Color.BLACK)//颜色
//                    .setHintText("Loading...")
//                    .setCanceledOnTouchOutside(false)
//                    .show();
//            new Thread(() -> {
//                OkHttpClient client = getOkHttpClinet();
//                FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
//                for (String key : map.keySet()) {
//                    String value = map.get(key);
//                    formBody.add(key, value == null ? "" : value);
//                }
//                Request request = new Request.Builder()
//                        .addHeader("token", CacheUtils.getCacheToken())
//                        .url(CacheUtils.getServerLink() + url)
//                        .post(formBody.build())
//                        .build();
//                try {
//                    Response response = client.newCall(request).execute();
//                    if (response.isSuccessful()) {
//                        dialog.dismiss();
//                        Message message = new Message();
//                        message.obj = JSONObject.parseObject(response.body().string(), HashMap.class);
//                        handler.sendMessage(message);
//                    } else {
//                        Looper.prepare();
//                        int code = response.code();
//                        if (code >= 500) {
//                            Toast.makeText(activity, "系统繁忙", Toast.LENGTH_SHORT).show();
//                        } else if (code > 300) {
//                            Toast.makeText(activity, "请求路径不存在", Toast.LENGTH_SHORT).show();
//                        }
//                        Looper.loop();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Looper.prepare();
//                    Toast.makeText(activity, "系统繁忙", Toast.LENGTH_SHORT).show();
//                    Looper.loop();
//                }
//            }).start();
//        }
//    }
//
//    public static void postWithJson(final String url, final JSONObject json,
//                                    final Activity activity, final Handler handler) {
//        /*检查网咯和权限*/
//        boolean beforeRequest = checkBeforeRequest(activity, url);
//        if (beforeRequest) {
//            ZLoadingDialog dialog = new ZLoadingDialog(activity);
//            dialog.setLoadingBuilder(Z_TYPE.CIRCLE)//设置类型
//                    .setLoadingColor(Color.BLACK)//颜色
//                    .setHintText("Loading...")
//                    .setCanceledOnTouchOutside(false)
//                    .show();
//            new Thread(() -> {
//                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//                OkHttpClient client = getOkHttpClinet();
//                RequestBody body = RequestBody.create(JSON, json.toJSONString());
//                Request request = new Request.Builder()
//                        .addHeader("token", CacheUtils.getCacheToken(activity))
//                        .url(CacheUtils.getServerLink(activity) + url)
//                        .post(body)
//                        .build();
//                try {
//                    Response response = client.newCall(request).execute();
//                    if (response.isSuccessful()) {
//                        dialog.dismiss();
//                        Message message = new Message();
//                        message.obj = JSONObject.parseObject(response.body().string());
//                        handler.sendMessage(message);
//                    } else {
//                        dialog.dismiss();
//                        Looper.prepare();
//                        int code = response.code();
//                        if (code >= 500) {
//                            Toast.makeText(activity, "系统繁忙", Toast.LENGTH_SHORT).show();
//                        } else if (code > 300) {
//                            Toast.makeText(activity, "请求路径不存在", Toast.LENGTH_SHORT).show();
//                        }
//                        Looper.loop();
//                    }
//                } catch (Exception e) {
//                    dialog.dismiss();
//                    e.printStackTrace();
//                    Looper.prepare();
//                    Toast.makeText(activity, "系统繁忙" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    Looper.loop();
//                }
//            }).start();
//        }
//    }
//
//    /**
//     * 不需要权限校验
//     *
//     * @param url
//     * @param json
//     * @param activity
//     * @param handler
//     */
//    public static void postWithJsonNoRight(final String url, final JSONObject json,
//                                           final Activity activity, final Handler handler) {
//        ZLoadingDialog dialog = new ZLoadingDialog(activity);
//        dialog.setLoadingBuilder(Z_TYPE.CIRCLE)//设置类型
//                .setLoadingColor(Color.BLACK)//颜色
//                .setHintText("Loading...")
//                .setCanceledOnTouchOutside(false)
//                .show();
//        new Thread(() -> {
//            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//            OkHttpClient client = getOkHttpClinet();
//            RequestBody body = RequestBody.create(JSON, json.toJSONString());
//            Request request = new Request.Builder()
//                    .url(CacheUtils.getServerLink(activity) + url)
//                    .post(body)
//                    .build();
//            try {
//                Response response = client.newCall(request).execute();
//                if (response.isSuccessful()) {
//                    dialog.dismiss();
//                    Message message = new Message();
//                    message.obj = JSONObject.parseObject(response.body().string());
//                    handler.sendMessage(message);
//                } else {
//                    dialog.dismiss();
//                    Looper.prepare();
//                    int code = response.code();
//                    if (code >= 500) {
//                        Toast.makeText(activity, "系统繁忙", Toast.LENGTH_SHORT).show();
//                    } else if (code > 300) {
//                        Toast.makeText(activity, "请求路径不存在", Toast.LENGTH_SHORT).show();
//                    }
//                    Looper.loop();
//                }
//            } catch (Exception e) {
//                dialog.dismiss();
//                e.printStackTrace();
//                Looper.prepare();
//                Toast.makeText(activity, "系统繁忙" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                Looper.loop();
//            }
//        }).start();
//    }
//
//
//    /**
//     * 请求前作检查必要项
//     * 1 网络
//     * 2 请求地址是否正确
//     * 3 是否已登录
//     * 4 是否有权限
//     *
//     * @return
//     */
//    private static boolean checkBeforeRequest(Activity activity, String url) {
//        /*判断是否已链接网络*/
//        boolean network = Utils.checkNetwork(activity);
//        if (!network) {
//            return false;
//        }
//        /*检查url*/
//        String server_link = CacheUtils.getServerLink(activity);
//        if (server_link == null || "".equals(server_link)) {
//            return false;
//        }
//        /*检查token是否有登录*/
//        String token = CacheUtils.getCacheToken(activity);
//        if (token.equals("") && !url.contains("login")) {
//            Toast.makeText(activity, "请重新登陆", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(activity, LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            activity.startActivity(intent);
//            return false;
//        }
//        /*检查权限*/
//        // TODO
//        return true;
//    }
//
//    /**
//     * create by: HPC
//     * description: 下载文件
//     * create time: 2019/4/3
//     */
//    public static void downloadFile(String url, Activity activity, Handler handler) {
//        String server_link = CacheUtils.getServerLink(activity);
//        if ("".equals(server_link)) {
//            return;
//        }
//        ZLoadingDialog dialog = new ZLoadingDialog(activity);
//        dialog.setLoadingBuilder(Z_TYPE.CIRCLE)//设置类型
//                .setLoadingColor(Color.BLACK)//颜色
//                .setHintText("下载中...")
//                .setCanceledOnTouchOutside(false)
//                .show();
//        new Thread(() -> {
//            OkHttpClient okHttpClient = getOkHttpClinet();
//            Request request = new Request.Builder()
//                    .url(server_link + url)
//                    .build();
//            Response response = null;
//            try {
//                response = okHttpClient.newCall(request).execute();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//             File file = null;
//            try {
//                byte[] bytes  = response.body().bytes();
//                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                    //获取文件名字
//                    String header = response.header("Content-Disposition", "attachment;filename=AMPDG_update.apk");
//                    String fileName = header.split(";")[1].split("=")[1];
//                    file = new File(Environment.getExternalStorageDirectory(), fileName);
//                    FileOutputStream fos = new FileOutputStream(file);
//                    fos.write(bytes, 0, bytes.length);
//                    fos.close();
//
//
//                } else {
//                    Toast.makeText(activity, "内存卡未挂载", Toast.LENGTH_SHORT).show();
//                }
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            /*关闭加载框*/
//            dialog.dismiss();
//            Message message = new Message();
//            message.obj = file;
//            message.what = 0;
//            handler.sendMessage(message);
//        }).start();
//    }
//
//
//}
