package com.framework.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Looper;

import com.framework.R;
import com.framework.utils.ActivityStack;
import com.framework.utils.App;
import com.framework.utils.AppUtils;
import com.framework.utils.ToastUtils;
import com.framework.utils.Utils;


/**
 * Created by lichen on 2018/5/28.
 */

public class BaseApplication extends Application implements Thread.UncaughtExceptionHandler, Application.ActivityLifecycleCallbacks {

    /**
     * 1秒
     */
    private static final long ONE_SECOND = 1;

    /**
     * 异常值 名称
     */
    private static final String UN_CATCH_EXCEPTION = "un_catch_exception";

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        //这两个属性需要放在最前面
        // 初始化全局context和一些必要参数
        Utils.init(getApplicationContext());
        App.setContext(getApplicationContext());
        // 同步debug变量
        AppUtils.syncIsDebug(getApplicationContext());

//        ARouter.openLog();  // 开启日志
//        ARouter.printStackTrace(); // 打印日志的时候打印线程堆栈
//        ARouter.openDebug();
//        ARouter.init(this);

        // 监听所有activity声明周期
        registerActivityLifecycleCallbacks(this);

        // 未知异常捕捉
        Thread.setDefaultUncaughtExceptionHandler(AppUtils.isDebug() ? Thread.getDefaultUncaughtExceptionHandler() : this);

    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
//        Logger.e(throwable, UN_CATCH_EXCEPTION);
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                ToastUtils.showShort(getString(R.string.cmn_anr_message));
                Looper.loop();
            }
        }.start();
        try {
            Thread.sleep(ONE_SECOND * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ActivityStack.getInstance()
                .popAllActivity(true);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        ActivityStack.getInstance()
                .pushActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        ActivityStack.getInstance()
                .popActivity(activity);
    }
}
