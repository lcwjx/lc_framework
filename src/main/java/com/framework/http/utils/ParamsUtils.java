package com.framework.http.utils;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.framework.constant.RequestParamsConstant;
import com.framework.utils.DeviceUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by lichen on 2017/12/4.
 */
public class ParamsUtils {

    public static final String JAVA_VERSION = "1.0.0";//后台的版本，和前端无关
    public static final String APP_ID = "11";//后台的版本，和前端无关


    /**
     * map转换为json.
     *
     * @param map the map
     * @return the string
     */
    public static String mapToJson(Map<String, Object> map) {
        String json = JSON.toJSONString(map);
        return json;
    }

    public static Map<String, Object> getParams(Map<String, Object> map, String api, String method, String params) {
        map.put("api", api);
        map.put("method", method);
        map.put("version", "1");
        map.put("params", params);
        map.put("deviceId", "865383039836972");
        map.put("appId", "11");
        return map;
    }

    public static String sortParams(Map<String, Object> map) {
        StringBuilder build = new StringBuilder();

        List<String> list = new ArrayList<String>(map.keySet());

        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.toLowerCase().compareTo(o2.toLowerCase());
            }
        });

        for (String key : list) {
            Object value = map.get(key);
            if (value instanceof String && TextUtils.isEmpty((CharSequence) value)) {
                continue;
            }
            if (null == value) {
                continue;
            }
            build.append(key.toLowerCase());
            build.append("=");
            if (value instanceof Map) {
                build.append(JSON.toJSONString(value));
            } else {
                build.append(value);
            }
            if (!TextUtils.equals(key, list.get(list.size() - 1))) {
                build.append("&");
            }
        }
        return build.toString();
    }

    public static Map<String, Object> sortMap(Map<String, Object> map) {


        //指定排序器
        TreeMap<String, Object> treeMap = new TreeMap<String, Object>(new Comparator<String>() {

            /*
             * int compare(Object o1, Object o2) 返回一个基本类型的整型，
             * 返回负数表示：o1 小于o2，
             * 返回0 表示：o1和o2相等，
             * 返回正数表示：o1大于o2。
             */
            @Override
            public int compare(String o1, String o2) {

                //指定排序器按照降序排列
                return o1.toLowerCase().compareTo(o2.toLowerCase());
            }
        });


        for (String key : map.keySet()) {
            treeMap.put(key, map.get(key));
        }
        return treeMap;
    }

    /**
     * @param filedMap
     * @param api
     * @param method
     * @return
     */
    public static RequestBody getRequestBody(Map<String, Object> filedMap, String api, String method) {
        String params = JSON.toJSONString(filedMap);
        Map<String, Object> mainFiledMap = new HashMap<>();
        mainFiledMap = ParamsUtils.getParams(mainFiledMap, api, method, params);
        return createGET(mainFiledMap);
    }

    public static RequestBody createGET(Map<String, Object> filedMap) {
        String paramUrl = "";
        StringBuilder urlBuilder = new StringBuilder();
        Iterator<Map.Entry<String, Object>> iterator = filedMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> next = iterator.next();
            String key = next.getKey();
            Object value = next.getValue();
            try {
                urlBuilder.append(
                        "&" + key + "=" + URLEncoder.encode(String.valueOf(value), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                urlBuilder.append("&" + key + "=" + value);
            }
        }
        if (!urlBuilder.toString().equals("")) {
            paramUrl = urlBuilder.toString().replaceFirst("&", "?");
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), paramUrl);
        return requestBody;
    }

    /**
     *
     * @param filedMap
     * @param api
     * @param method
     * @return
     */
    public static Map<String, String> packParams(HashMap<String, Object> filedMap, String api, String method) {
        String params = JSON.toJSONString(filedMap);
        Map<String, String> mainFiledMap = new HashMap<>();
        mainFiledMap.put(RequestParamsConstant.BaseParamsKey.API, api);
        mainFiledMap.put(RequestParamsConstant.BaseParamsKey.METHOD, method);
        mainFiledMap.put(RequestParamsConstant.BaseParamsKey.VERSION, JAVA_VERSION);
        mainFiledMap.put(RequestParamsConstant.BaseParamsKey.PARAMS, params);
        mainFiledMap.put(RequestParamsConstant.BaseParamsKey.DEVICE_ID, DeviceUtils.getAndroidID());
        mainFiledMap.put(RequestParamsConstant.BaseParamsKey.APP_ID, APP_ID);
        return mainFiledMap;
    }
}
