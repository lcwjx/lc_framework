package com.framework.http.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by lichen on 2018/5/28.
 */

public class BaseResponse<T> {

    /**
     * 请求成功.
     */
    private static final String SUCCESS = "00";


    /**
     * 泛型类.
     */
    @JSONField(name = "jsonBody")
    private T jsonBody;

    /**
     * 获取泛型类.
     *
     * @return 泛型类
     */
    public T getJsonBody() {
        return jsonBody;
    }

    /**
     * 设置泛型类.
     *
     * @param jsonBody 泛型类
     */
    public void setJsonBody(T jsonBody) {
        this.jsonBody = jsonBody;
    }

    /**
     * 请求是否成功.
     *
     * @return 成功--true，失败--false
     */
    public boolean isSuccess() {
        return String.valueOf(SUCCESS).equals(String.valueOf(retCode));
    }


    @JSONField(name = "retCode")
    private String retCode = "";
    @JSONField(name = "retDesc")
    private String retDesc = "";
    @JSONField(name = "version")
    private String version;
    @JSONField(name = "appId")
    private String appId;
    @JSONField(name = "deviceId")
    private String deviceId;
    @JSONField(name = "api")
    private String api;
    @JSONField(name = "method")
    private String method;
    @JSONField(name = "startTime")
    private long startTime;
    @JSONField(name = "endTime")
    private long endTime;

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * 获取返回信息
     *
     * @return
     */
    public String getRetDesc() {
        return retDesc;
    }

    /**
     * 设置返回信息
     *
     * @param retDesc
     */
    public void setRetDesc(String retDesc) {
        this.retDesc = retDesc;
    }

    /**
     * 获取返回码.
     *
     * @return 错误码
     */
    public String getRetCode() {
        return retCode;
    }

    /**
     * 设置返回码.
     *
     * @param retCode 错误码
     */
    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }
}
