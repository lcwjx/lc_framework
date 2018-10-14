package com.framework.listener;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.View;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import java.util.concurrent.TimeUnit;

/**
 * Created by gengbiao.fan.o on 2017/12/6.
 */

public class RxView {
  /**
   * 默认防抖动时间 单位毫秒
   */
  private static final int delay_click = 500;

  /**
   * 获取observale 默认时间500ms
   */
  @NonNull
  public static void setOnClick(@NonNull View view, final RxClickListener observer) {
    new ViewClickObservable(view).throttleFirst(delay_click, TimeUnit.MILLISECONDS)
            .subscribe(new BaseRxBindingObserver<Object>(view) {
              @Override
              protected void onClick(View t) {
                observer.onClick(t);
              }
            });
  }

  /**
   * 获取observale 默认时间500ms
   */
  @CheckResult
  @NonNull
  public static Observable<Object> clicks(@NonNull View view) {
    return new ViewClickObservable(view).throttleFirst(delay_click, TimeUnit.MILLISECONDS);
  }

  public abstract static class BaseRxBindingObserver<T> implements Observer<T> {

    private View view;

    protected BaseRxBindingObserver(View view) {
      this.view = view;
    }

    @Override
    public void onSubscribe(Disposable d) {
      //onNext(null);
    }

    @Override
    public void onNext(T value) {
      onClick(view);
    }

    @Override
    public void onError(Throwable e) {}

    @Override
    public void onComplete() {}

    protected abstract void onClick(View t);
  }
}
