package com.framework.http;

import android.support.annotation.NonNull;

import com.framework.http.model.BaseResponse;
import com.framework.http.manager.HttpRxManager;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 对请求成功返回数据处理
 *
 * @author lichen
 * @date 2017/12/08
 */
public abstract class HttpObserver<T> implements Observer<BaseResponse<T>> {
  /**
   * 当前请求标识
   */
  private Object tag;

  /**
   * @param tag 当前请求标识
   */
  public HttpObserver(Object tag) {
    this.tag = tag;
  }

  @Override
  public void onSubscribe(@NonNull Disposable disposable) {
    onStart(disposable);
    HttpRxManager.getInstance()
        .put(tag, disposable);
  }

  @Override
  public void onNext(@NonNull BaseResponse<T> baseResponse) {
    onSuccess(baseResponse);
    HttpRxManager.getInstance()
        .remove(tag);
  }

  @Override
  public void onError(@NonNull Throwable e) {
    if (e instanceof BaseException) {
      onFail((BaseException) e);
    } else {
      onFail(new BaseException(e, String.valueOf(BaseException.UN_KNOWN_ERROR)));
    }
    HttpRxManager.getInstance()
        .remove(tag);
  }

  /**
   * 开始请求.
   *
   * @param d the d
   */
  protected void onStart(Disposable d) {}

  ;

  /**
   * 请求失败.
   *
   * @param e 异常
   */
  protected abstract void onFail(BaseException e);

  /**
   * 请求成功.
   *
   * @param response 成功响应
   */
  protected abstract void onSuccess(BaseResponse<T> response);

  @Override
  public void onComplete() {

  }
}
