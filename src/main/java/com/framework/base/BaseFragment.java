package com.framework.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import butterknife.Unbinder;

/**
 * Created by binbin.liu.o on 2017/12/27.
 */

public class BaseFragment extends Fragment implements MvpView {

  private BaseActivity mActivity;
  private Unbinder        mUnBinder;

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
  public void onResume() {
    super.onResume();
    IntentFilter connectFilter = new IntentFilter();
    connectFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    getBaseActivity().registerReceiver(mConnectReceiver, connectFilter);
  }

  @Override
  public void onPause() {
    super.onPause();
    getBaseActivity().unregisterReceiver(mConnectReceiver);
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof BaseActivity) {
      BaseActivity activity = (BaseActivity) context;
      this.mActivity = activity;
      activity.onFragmentAttached();
    }
  }

  @Override
  public void onDetach() {
    mActivity = null;
    super.onDetach();
  }

  @Override
  public void onDestroy() {
    if (mUnBinder != null) {
      mUnBinder.unbind();
      mUnBinder = null;
    }
    super.onDestroy();
  }

  @Override
  public void showLoading(boolean cancelable, Object tag) {
    if (mActivity != null) {
      mActivity.showLoading(cancelable, tag);
    }
  }

  @Override
  public void hideLoading() {
    if (mActivity != null) {
      mActivity.hideLoading();
    }
  }

  @Override
  public void openActivityOnTokenExpire() {
    if (mActivity != null) {
      mActivity.openActivityOnTokenExpire();
    }
  }

  @Override
  public void showPageMessage(@StringRes int resId) {

  }

  @Override
  public void showPageMessage(String message) {

  }

  @Override
  public void showToastMessage(String message) {
    if (mActivity != null) {
      mActivity.showToastMessage(message);
    }
  }

  @Override
  public void showToastMessage(@StringRes int resId) {
    if (mActivity != null) {
      mActivity.showToastMessage(resId);
    }
  }

  public BaseActivity getBaseActivity() {
    return mActivity;
  }

  public void setUnBinder(Unbinder unBinder) {
    mUnBinder = unBinder;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    mConnectReceiver = null;
  }

  /**
   * 打开网络
   */
  public void showNetworkDisconnectView() {
  }

  /**
   * 关闭网络
   */
  public void hideNetworkDisconnectView() {
  }

  public interface Callback {

    void onFragmentAttached();

    void onFragmentDetached(String tag);
  }
}
