package com.framework.http;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 请求头拦截器
 * Created by lichen on 2018/8/23.
 */

public class HeaderInterceptor implements Interceptor {
    private Map<String, Object> headerMaps = new TreeMap();

    public HeaderInterceptor(Map<String, Object> headerMaps) {
        this.headerMaps = headerMaps;
    }

    public Response intercept(Chain chain) throws IOException {
        Request.Builder request = chain.request().newBuilder();
        if (this.headerMaps != null && this.headerMaps.size() > 0) {
            Iterator var3 = this.headerMaps.entrySet().iterator();

            while (var3.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry) var3.next();
                request.addHeader((String) entry.getKey(), (String) entry.getValue());
            }
        }

        return chain.proceed(request.build());
    }
}
