package com.framework.http;

/**
 * 服务端异常
 *
 * @author lichen
 * @date 2017/11/13.
 */
public class ServerException extends RuntimeException {
  private String code;
  private String msg;

  public ServerException(String code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public String getCode() {
    return code;
  }

  public String getMsg() {
    return msg;
  }
}
