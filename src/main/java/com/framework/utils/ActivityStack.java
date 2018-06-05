package com.framework.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Process;

import java.util.Stack;

/**
 * Activity stack 管理器
 * Created by lichen on 2018/5/28.
 */

public class ActivityStack {

    private Stack<Activity> mActivities = new Stack<Activity>();
    private static final String TAG = "ActivityStack";
    private static ActivityStack INSTANCE;

    private ActivityStack() {
    }

    public static ActivityStack getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ActivityStack();
        }
        return INSTANCE;
    }

    public void popActivity() {
        if (mActivities != null && !mActivities.isEmpty()) {
            Activity activity = mActivities.pop();
            if (activity != null) {
                activity.finish();
                activity = null;
            }
        }
    }

    /**
     * 把指定页面移除出栈
     *
     * @param activity
     */
    public void popActivity(Activity activity) {
        if (mActivities != null && !mActivities.isEmpty() && mActivities.contains(activity)) {
            mActivities.remove(activity);
            if (activity != null) {
                activity.finish();
                activity = null;
            }
        }
    }

    /**
     * @return 栈顶页面
     */
    public Activity topActivity() {
        return mActivities.lastElement();
    }

    /**
     * 添加activity进stack
     *
     * @param activity
     */
    public void pushActivity(Activity activity) {
        mActivities.add(activity);
    }

    /**
     * 关闭所有页面
     *
     * @param isForceClose 是否杀死应用进程
     */
    public void popAllActivity(boolean isForceClose) {
        while (mActivities.size() > 0) {
            popActivity();
        }
        if (isForceClose) {
            Process.killProcess(Process.myPid());
            System.exit(-1);
        }
    }

    /**
     * 清除当前页面之外的所有哦页面
     *
     * @param isForceClose 是否杀死应用进程
     */
    public void popAllActivityExceptTop(boolean isForceClose) {
        while (mActivities.size() > 1) {
            //popActivity();
            Activity activity = mActivities.get(0);
            mActivities.remove(activity);
            if (activity != null) {
                activity.finish();
                activity = null;
            }
        }
        if (isForceClose) {
            Process.killProcess(Process.myPid());
            System.exit(-1);
        }
    }

    /**
     * 结束进程并重启app(会有黑屏现象)
     */
    public void reStartApp(Context mContext, Class<?> className) {
        Intent intent = new Intent(mContext, className);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);

        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void popActivityUntilCls(Class<?> clz) {
        while (mActivities.size() > 1) {
            if (topActivity().getClass() != clz) {
                popActivity();
            } else {
                break;
            }
        }
    }

    public int size() {
        return mActivities.size();
    }

    public Activity activityAt(int position) {
        return position < mActivities.size() ? mActivities.elementAt(position) : null;
    }
}
