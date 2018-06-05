package com.framework.http;


import com.framework.http.model.BaseResponse;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Api请求异常转换.
 *
 * @param <T> the type parameter
 * @author mars.yu
 * @date 2017/11/14
 */
public class ApiServerResultFunction<T> implements Function<BaseResponse<T>, BaseResponse<T>> {
  @Override
  public BaseResponse<T> apply(@NonNull BaseResponse<T> baseResponse) throws Exception {
    if (!baseResponse.isSuccess()) {
      throw new ServerException(baseResponse.getRetCode(), baseResponse.getRetDesc());
    }
    return baseResponse;
  }
}
