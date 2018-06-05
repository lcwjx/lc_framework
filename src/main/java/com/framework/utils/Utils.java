package com.framework.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;

public final class Utils {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param  context
     */
    public static void init(@NonNull final Context context) {
        Utils.context = context.getApplicationContext();
    }

    /**
     * 获取Application
     *
     * @return Application
     */
    public static Context getApp() {
        if (context != null) {
            return context;
        }
        throw new NullPointerException("u should init first");
    }
}
