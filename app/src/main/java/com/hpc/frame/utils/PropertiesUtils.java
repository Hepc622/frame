package com.hpc.frame.utils;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {
    private static Properties properties = null;

    private static void readProperties() {
        try {
            InputStream in = PropertiesUtils.class.getResourceAsStream("/assets/appConfig.properties");
            properties = new Properties();
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            Log.e("PropertiesUtils", "get nothing");
            e.printStackTrace();
        }
    }

    public static String getValue(String key) {
        if (properties == null) {
            readProperties();
        }
        return properties.getProperty(key);
    }
}