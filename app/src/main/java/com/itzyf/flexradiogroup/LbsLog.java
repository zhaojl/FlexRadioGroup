package com.itzyf.flexradiogroup;

import java.util.List;
import java.util.Map;
import java.util.Set;

public final class LbsLog {

    public static final boolean DEBUG = true;
    public static final String APP_TAG = "app";

    private static boolean LOGV_ON = true;

    public static void v(String msg) {
        if (LOGV_ON) {
            android.util.Log.v(APP_TAG, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (LOGV_ON) {
            android.util.Log.v(APP_TAG, tag + " -- " + msg);
        }
    }

    public static void d(String msg) {
        android.util.Log.d(APP_TAG, msg);
    }

    public static void d(String tag, String msg) {
        if (msg.length() > 4000) {
            android.util.Log.d(APP_TAG, tag + " -- " + msg.substring(0, 4000));
            d(tag, msg.substring(4000));
        } else {
            android.util.Log.d(APP_TAG, tag + " -- " + msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        android.util.Log.d(APP_TAG, tag + " -- " + msg, tr);
    }

    public static void d(Class<?> c, String msg) {
        android.util.Log.d(APP_TAG, c.getSimpleName() + " -- " + msg);
    }

    public static void i(String msg) {
        android.util.Log.i(APP_TAG, msg);
    }

    public static void i(String tag, String msg) {
        android.util.Log.i(APP_TAG, tag + " -- " + msg);
    }

    public static void w(String msg) {
        android.util.Log.w(APP_TAG, msg);
    }

    public static void w(String tag, String msg) {
        android.util.Log.w(APP_TAG, tag + " -- " + msg);
    }

    public static void w(String tag, Throwable tr) {
        android.util.Log.w(APP_TAG, tr);
    }

    public static void w(String tag, String msg, Throwable tr) {
        android.util.Log.w(APP_TAG, tag + " -- " + msg, tr);
    }

    public static void w(Class<?> c, String msg, Throwable tr) {
        android.util.Log.w(APP_TAG, c.getSimpleName() + " -- " + msg, tr);
    }

    public static void w(Class<?> c, String msg) {
        android.util.Log.d(APP_TAG, c.getSimpleName() + " -- " + msg);
    }

    public static void e(String msg) {
        android.util.Log.e(APP_TAG, msg);
    }

    public static void e(String tag, String msg) {
        android.util.Log.e(APP_TAG, tag + " -- " + msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        android.util.Log.e(APP_TAG, tag + " -- " + msg, tr);
    }

    public static void printResponseHeaders(Map<String, List<String>> headers) {
        if (null == headers || 0 == headers.size()) {
            return;
        }
        Set<String> keys = headers.keySet();
        for (String key : keys) {
            List<String> valueList = headers.get(key);
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append(key).append(":");
            for (int i = 0; i < valueList.size(); i++) {
                if (i > 0) {
                    strBuilder.append(";");
                }
                strBuilder.append(valueList.get(i));
            }
            d(strBuilder.toString());
        }
    }
}
