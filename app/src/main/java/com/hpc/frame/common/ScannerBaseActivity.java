package com.hpc.frame.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.device.scanner.configuration.PropertyID;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.hpc.frame.R;


public abstract class ScannerBaseActivity<T> extends BaseActivity<T> {
    /*是否开启扫描*/
    private boolean isStartScanner = false;
    private final static String SCAN_ACTION = ScanManager.ACTION_DECODE;//default action
    private Vibrator mVibrator;
    private ScanManager mScanManager;
    private SoundPool soundpool = null;
    private SoundPool success = null;
    private SoundPool fail = null;
    private int soundid;
    private int successSoundId;
    private int failSoundId;
    private String barcodeStr;
    private boolean isScaning = false;

    private BroadcastReceiver mScanReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initScanReceiver();
    }

    /**
     * 扫描数据
     *
     * @param barcodeStr
     */
    public void scanData(String barcodeStr) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        //如果是开启了扫描模式采取做对应的处理
        if (isStartScanner) {
            scannerOnResume();
        }
    }

    /**
     * 扫描activity的OnResume
     */
    private void scannerOnResume() {
        super.onResume();
        try {
            // TODO Auto-generated method stub
            initScan();
            IntentFilter filter = new IntentFilter();
            int[] idbuf = new int[]{PropertyID.WEDGE_INTENT_ACTION_NAME, PropertyID.WEDGE_INTENT_DATA_STRING_TAG};
            String[] value_buf = mScanManager.getParameterString(idbuf);
            if (value_buf != null && value_buf[0] != null && !value_buf[0].equals("")) {
                filter.addAction(value_buf[0]);
            } else {
                filter.addAction(SCAN_ACTION);
            }
            registerReceiver(mScanReceiver, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        //如果开启了扫描才去调用
        if (isStartScanner) {
            scannerOnPause();
        }
    }

    /**
     * 扫描activity的OnPause
     */
    private void scannerOnPause() {
        try {
            if (mScanReceiver != null) {
                unregisterReceiver(mScanReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initScan() {
        mScanManager = new ScanManager();
        mScanManager.openScanner();
        mScanManager.switchOutputMode(0);
        soundpool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 100); // MODE_RINGTONE
        success = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 100); // MODE_RINGTONE
        fail = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 100); // MODE_RINGTONE
        soundid = soundpool.load("/etc/Scan_new.ogg", 1);
        successSoundId = success.load(this, R.raw.success, 1);
        failSoundId = fail.load(this, R.raw.fail, 1);
    }

    /**
     * 初始化扫描接收服务
     */
    private void initScanReceiver() {
        if (mScanReceiver == null) {
            try {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mScanReceiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    // TODO Auto-generated method stub
                    isScaning = false;
                    playScan();
                    mVibrator.vibrate(100);

                    byte[] barcode = intent.getByteArrayExtra(ScanManager.DECODE_DATA_TAG);
                    int barcodelen = intent.getIntExtra(ScanManager.BARCODE_LENGTH_TAG, 0);
                    byte temp = intent.getByteExtra(ScanManager.BARCODE_TYPE_TAG, (byte) 0);
                    barcodeStr = new String(barcode, 0, barcodelen);
                    Log.i("debug", "----codetype--" + temp + "code" + barcodeStr);
                    scanData(barcodeStr);
                }

            };
        }
    }

    /**
     * 播放语音
     *
     * @param soundId 音频id
     */
    private void play(int soundId) {
        soundpool.play(soundId, 1, 1, 0, 0, 1);
    }

    /**
     * 播放扫描成功声音
     */
    public void playScan() {
        play(soundid);
    }

    /**
     * 播放请求成功语音
     */
    public void playSuccess() {
        play(successSoundId);
    }

    /**
     * 播放请求失败语音
     */
    public void playFail() {
        play(failSoundId);
    }

}
