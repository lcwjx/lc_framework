package com.framework.utils;

import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.framework.R;


/**
 * Created by lichen on 2018/5/28.
 */

public class WindowUtils {

    /**
     * 底部的window样式
     */

    public static void setBottomDialogType(Window window) {
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setWindowAnimations(R.style.cmn_animinandout);
        window.setAttributes(params);
    }

    //固定高宽的
    public static void setHeightDialog(Window window, int height) {
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (App.getWidth() * 0.8);
        params.height = height;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }

    /**
     * 提示dialog样式
     */

    public static void setNotifyDialogType(Window window) {
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (App.getShortBorderLenth() * 0.84);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }

    /**
     * 居中dialog样式
     */

    public static void setProgressDialogType(Window window) {
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.dimAmount = 0f;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }
}
