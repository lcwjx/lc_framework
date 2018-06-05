package com.framework.http;

import android.net.ParseException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import org.json.JSONException;
import retrofit2.HttpException;

/**
 * 异常处理封装
 *
 * @author mars.yu
 * @date 2017/11/13.
 */
public class ExceptionHandler {

  /**
   * 处理异常.
   *
   * @param e the e
   * @return 异常基类
   */
  public static BaseException handleException(Throwable e) {
    BaseException ex;
    //HTTP错误
    if (e instanceof HttpException) {
      HttpException httpExc = (HttpException) e;
      ex = new BaseException(e, String.valueOf(httpExc.code()), httpExc.message());
      return ex;
      //服务器返回的错误
    } else if (e instanceof ServerException) {
      ServerException serverExc = (ServerException) e;
      ex = new BaseException(serverExc, serverExc.getCode(), serverExc.getMsg());
      return ex;
      //解析数据错误
    } else if (e instanceof JSONException || e instanceof ParseException) {
      ex = new BaseException(e, String.valueOf(BaseException.ANALYTIC_SERVER_DATA_ERROR));
      return ex;
      //连接网络错误
    } else if (e instanceof ConnectException) {
      ex = new BaseException(e, String.valueOf(BaseException.CONNECT_ERROR));
      return ex;
      //网络超时
    } else if (e instanceof SocketTimeoutException) {
      ex = new BaseException(e, String.valueOf(BaseException.TIME_OUT_ERROR));
      return ex;
      // 网络错误
    } else if (e instanceof UnknownHostException) {
      ex = new BaseException(e, String.valueOf(BaseException.NETWORK_ERROR));
      return ex;
    } else {  //未知错误
      ex = new BaseException(e, String.valueOf(BaseException.UN_KNOWN_ERROR));
      return ex;
    }
  }
}
