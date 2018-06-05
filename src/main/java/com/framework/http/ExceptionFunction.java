package com.framework.http;


import com.framework.http.model.BaseResponse;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * 异常处理转换
 *
 * @param <T> the type parameter
 * @author mars.yu
 * @date 2017/11/13.
 */
public class ExceptionFunction<T> implements Function<Throwable, Observable<BaseResponse<T>>> {
  @Override
  public Observable<BaseResponse<T>> apply(@NonNull Throwable throwable) throws Exception {
    return Observable.error(ExceptionHandler.handleException(throwable));
  }
}
