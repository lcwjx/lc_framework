package com.framework.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;


import com.framework.R;
import com.framework.http.manager.HttpRxManager;
import com.framework.listener.OnCancelListener;
import com.framework.router.RouterConstants;
import com.framework.router.RouterUtils;
import com.framework.utils.ActivityStack;
import com.framework.utils.MyPermissionUtils;
import com.framework.utils.StatusBarUtils;
import com.framework.wiget.dialog.BlueDotLoadingDialog;
import com.framework.wiget.dialog.manager.CmnDialogManager;

import butterknife.Unbinder;

/**
 * Created by lichen on 2018/5/28.
 */

public class BaseActivity extends RxAppCompatActivity implements MvpView, BaseFragment.Callback {
    private Unbinder mUnBinder;
    private BlueDotLoadingDialog mProgressDialog;
    private MyPermissionUtils.OnResponsePermissionListener mPermissionResponseListener;

    private BroadcastReceiver mConnectReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(final Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                boolean isDisconnected = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
                if (isDisconnected) {
                    showNetworkDisconnectView();
                } else {
                    hideNetworkDisconnectView();
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter connectFilter = new IntentFilter();
        connectFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mConnectReceiver, connectFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mConnectReceiver);
    }

    @Override
    protected void onDestroy() {
        if (mUnBinder != null) {
            mUnBinder.unbind();
            mUnBinder = null;
        }
        super.onDestroy();
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.hide();
            }
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        mConnectReceiver = null;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setStatusBar();
    }

    public void setUnBinder(Unbinder unBinder) {
        mUnBinder = unBinder;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (outState == null) {
            outState = new Bundle();
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.fontScale = 1.0f;
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    protected void setStatusBar() {
        setDarkStatusIcon(true);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            StatusBarUtils.setColorNoTranslucent(this, getResources().getColor(R.color.white_ffffff));
        } else {
            StatusBarUtils.setColor(this, getResources().getColor(R.color.white_ffffff),
                    StatusBarUtils.DEFAULT_STATUS_BAR_ALPHA);
        }
        //处理小米魅族状态栏问题
        StatusBarUtils.MIUISetStatusBarLightMode(getWindow(), true);
        StatusBarUtils.FlymeSetStatusBarLightMode(getWindow(), true);
    }

    public void setDarkStatusIcon(boolean bDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            View decorView = getWindow().getDecorView();
            if (decorView != null) {
                int vis = decorView.getSystemUiVisibility();
                if (bDark) {
                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                decorView.setSystemUiVisibility(vis);
            }
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.cmn_slide_right_in, R.anim.cmn_slide_left_out);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.cmn_slide_left_in, R.anim.cmn_slide_right_out);
    }

    @Override
    public void showLoading(boolean cancelable, Object tag) {
        hideLoading();
        if (mProgressDialog == null) {
            mProgressDialog = CmnDialogManager.getLoadingDialog(this, cancelable);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
        mProgressDialog.setOnCancelListener(new OnCancelListener(tag) {
            @Override
            public void onCancel(DialogInterface dialogInterface, Object tag) {
                HttpRxManager.getInstance()
                        .cancel(tag);
            }
        });
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    @Override
    public void openActivityOnTokenExpire() {
        ActivityStack.getInstance()
                .popAllActivity(false);
        RouterUtils.navigation(RouterConstants.LOGIN_ACTIVITY);
    }

    @Override
    public void showPageMessage(int resId) {

    }

    @Override
    public void showPageMessage(String message) {

    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void showToastMessage(int resId) {

    }

    /**
     * 无网络时的处理
     */
    public void showNetworkDisconnectView() {
    }

    /**
     * 有网络状态下的处理
     */
    public void hideNetworkDisconnectView() {
    }

    public void hookData(Bundle bundle) {
    }

    /**
     * 配合permission utils 使用
     */
    public void setPermissionResponseListener(MyPermissionUtils.OnResponsePermissionListener permissionResponseListener) {
        mPermissionResponseListener = permissionResponseListener;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (null != mPermissionResponseListener && requestCode == mPermissionResponseListener.getRequestCode()) {
            mPermissionResponseListener.onResponse(permissions, grantResults);
        }
    }

    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                finish();
                return true;
            } else {
                try {
                    getSupportFragmentManager().popBackStackImmediate();
                } catch (Exception e) {
                }
                return true;
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
