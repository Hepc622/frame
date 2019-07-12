//package com.hpc.frame.utils;
//
//import android.annotation.TargetApi;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Intent;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Handler;
//import android.os.Message;
//import android.support.annotation.RequiresApi;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.FileProvider;
//import android.util.Log;
//import android.widget.Toast;
//
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.FileProvider;
//
//import com.alibaba.fastjson.JSONObject;
//import com.sgkj.test.BuildConfig;
//import com.vtv.ampdg.BuildConfig;
//import com.vtv.ampdg.MainActivity;
//
//import java.io.File;
//
//import static android.content.ContentValues.TAG;
//
///**
// * app更新工具
// */
//public class UpdateUtils {
//    private static String[] PERMISSIONS_STORAGE = {
//            "android.permission.READ_EXTERNAL_STORAGE",
//            "android.permission.WRITE_EXTERNAL_STORAGE"};
//
//    private static final Integer REQUEST_EXTERNAL_STORAGE = 1;
//
//    private static void verifyStoragePermissions(Activity activity) {
//        try {
//            //检测是否有写的权限
//            int permission = ActivityCompat.checkSelfPermission(activity,
//                    "android.permission.WRITE_EXTERNAL_STORAGE");
//            if (permission != PackageManager.PERMISSION_GRANTED) {
//                // 没有写的权限，去申请写的权限，会弹出对话框
//                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 获取版本号
//     *
//     * @return
//     */
//    private static int getAppVersionCode(Activity activity) {
//        //获取packagemanager的实例
//        PackageManager packageManager = activity.getPackageManager();
//        //getPackageName()是你当前类的包名，0代表是获取版本信息
//        PackageInfo packInfo = null;
//        try {
//            packInfo = packageManager.getPackageInfo(activity.getPackageName(), 0);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return packInfo.versionCode;
//    }
//
//    /**
//     * 下载apk
//     *
//     * @return
//     */
//    private static void downloadApk(String url, Activity activity) {
//        /*下载文件并且安装*/
//        HttpUtils.downloadFile(url, activity, new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                if (msg.what == 0) {
//                    if (msg.obj != null) {
//                        File file = (File) msg.obj;
//                        installApk(activity, file);
//                    } else {
//                        Toast.makeText(activity, "应用更新失败", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
//    }
//
//    /**
//     * 安装apk
//     *
//     * @param activity
//     * @param file
//     */
//    private static void installApk(Activity activity, File file) {
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        //判断是否是AndroidN以及更高的版本
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            Uri contentUri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".fileProvider", file);
//            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
//        } else {
//            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        }
//        activity.startActivity(intent);
//    }
//
//    /**
//     * 检查是否需要更新升级
//     */
//    public static void checkVersion(Activity activity) {
//        JSONObject jsonObject = new JSONObject();
//        HttpUtils.postWithJsonNoRight("/version/getCurrentVersion", jsonObject, activity, new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                JSONObject res = (JSONObject) msg.obj;
//
//                String code = res.getString("code");
//                if (code.equals("0000")) {
//                    JSONObject map = res.getJSONObject("data");
//                    Integer must = map.getInteger("must");
//                    /*0为选择更新*/
//                    /*服务器发布的apk版本*/
//                    String serverVersion = map.getString("version");
//                    //当前版本code
//                    Integer versionCode = map.getInteger("code");
//
//                    /*当前app的apk版本*/
//                    Integer currentVersion = getAppVersionCode(activity);
//                    /*1为必须更新*/
//                    if (1 == must.intValue()) {
//                        if (currentVersion < versionCode) {
//                            /*下载并安卓apk*/
//                            verifyStoragePermissions(activity);
//                            downloadApk("/version/downloadApk", activity);
//                        } else {
//                            new Thread(() -> {
//                                try {
//                                    Thread.sleep(0L);
//                                    Intent intent = new Intent(activity, MainActivity.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    activity.startActivity(intent);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }).start();
//
//                        }
//                    } else {
//                        /**/
//                        verifyStoragePermissions(activity);
//                        if (currentVersion < versionCode) {
//                            /*提示是否需要更新并安装*/
//                            showUpdateDialog(activity);
//                        } else {
//                            new Thread(() -> {
//                                try {
//                                    Thread.sleep(0L);
//                                    Intent intent = new Intent(activity, MainActivity.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    activity.startActivity(intent);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }).start();
//                        }
//                    }
//                }
//            }
//        });
//
//    }
//
//    /*
//     *
//     * 弹出对话框通知用户更新程序
//     *
//     * 弹出对话框的步骤：
//     * 	1.创建alertDialog的builder.
//     *	2.要给builder设置属性, 对话框的内容,样式,按钮
//     *	3.通过builder 创建一个对话框
//     *	4.对话框show()出来
//     */
//    private static void showUpdateDialog(Activity activity) {
//        AlertDialog.Builder builer = new AlertDialog.Builder(activity);
//        builer.setTitle("版本升级");
//        //当点确定按钮时从服务器上下载 新的apk 然后安装
//        builer.setPositiveButton("确定", (dialog, which) -> {
//                    Log.i(TAG, "下载apk,更新");
//                    downloadApk("/version/downloadApk", activity);
//                }
//        );
//        //当点取消按钮时进行登录
//        builer.setNegativeButton("取消", (dialog, which) -> {
//            new Thread(() -> {
//                try {
//                    Thread.sleep(0L);
//                    Intent intent = new Intent(activity, MainActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    activity.startActivity(intent);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }).start();
//        });
//
//        AlertDialog dialog = builer.create();
//        dialog.show();
//    }
//}
