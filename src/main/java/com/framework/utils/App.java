package com.framework.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

/**
 * 应用全局信息.
 * Created by lichen on 2018/5/28.
 */

public class App {
    private static Context context;
    private static int     width;
    private static int     height;
    private static String  urlType;

    /**
     * 获取全局Context.
     *
     * @return the context
     */
    public static Context getContext() {
        return context;
    }

    /**
     * 设置全局context.
     *
     * @param context the context
     */
    public static void setContext(Context context) {
        App.context = context.getApplicationContext();
        setWidth(DeviceUtils.getWidth(context));
        setHeight(DeviceUtils.getHeight(context));
    }

    /**
     * 宽.
     *
     * @return 宽
     */
    public static int getWidth() {
        return DeviceUtils.getWidth(context);
    }

    /**
     * 宽.
     *
     * @param width 宽
     */
    public static void setWidth(int width) {
        App.width = width;
    }

    /**
     * 获取设备高.
     *
     * @return 高
     */
    public static int getHeight() {
        return DeviceUtils.getHeight(context);
    }

    /**
     * 设置设备高.
     *
     * @param height 高
     */
    public static void setHeight(int height) {
        App.height = height;
    }

    /**
     * 获取长的边界长度
     */
    public static int getLongBorderLength() {
        return width > height ? width : height;
    }

    /**
     * 获取短的边界长度
     */
    public static int getShortBorderLenth() {
        return width > height ? height : width;
    }

    /**
     * 是否是竖屏
     */

    public static boolean isVerticalScreen() {
        //获取设置的配置信息
        Configuration mConfiguration = context.getResources()
                .getConfiguration();
        //获取屏幕方向
        int ori = mConfiguration.orientation;
        if (ori == Configuration.ORIENTATION_LANDSCAPE) {
            return false;
        } else {
            return true;
        }
    }

    public static String getUrlType() {
        return urlType;
    }

    public static void setUrlType(String urlType) {
        App.urlType = urlType;
    }

    /**
     * 获取屏幕密度
     */
    public static float getDensity() {
        DisplayMetrics dm = new DisplayMetrics();
        ActivityStack.getInstance()
                .topActivity()
                .getWindowManager()
                .getDefaultDisplay()
                .getMetrics(dm);
        return dm.density;
    }

    /**
     * 获取屏幕密度  每英寸像素
     */
    public static int getDensityDpi() {
        DisplayMetrics dm = new DisplayMetrics();
        ActivityStack.getInstance()
                .topActivity()
                .getWindowManager()
                .getDefaultDisplay()
                .getMetrics(dm);
        return dm.densityDpi;
    }
}
