package com.framework.base;

import android.support.annotation.StringRes;


public interface MvpView {
  void showLoading(boolean cancelable, Object tag);

  void hideLoading();

  void openActivityOnTokenExpire();

  void showPageMessage(@StringRes int resId);

  void showPageMessage(String message);

  void showToastMessage(String message);

  void showToastMessage(@StringRes int resId);
}
