package com.framework.http.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.reactivex.disposables.Disposable;

/**
 * Created by lichen on 2018/5/28.
 */

public class HttpRxManager {
    private Map<Object, Disposable> tagMap = new HashMap<>();
    private static HttpRxManager INSTANCE;

    private HttpRxManager() {

    }

    public static HttpRxManager getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpRxManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpRxManager();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 保存当前请求
     *
     * @param tag        请求标识
     * @param disposable the disposable
     */
    public void put(Object tag, Disposable disposable) {
        tagMap.put(tag, disposable);
    }

    /**
     * 移除当前请求
     *
     * @param tag 请求标识
     */
    public void remove(Object tag) {
        if (tagMap.containsKey(tag)) {
            Disposable disposable = tagMap.get(tag);
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
        tagMap.remove(tag);
    }

    /**
     * 取消当前请求
     *
     * @param tag 请求标识
     */
    public void cancel(Object tag) {
        if (tagMap.containsKey(tag)) {
            Disposable disposable = tagMap.get(tag);
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
        tagMap.remove(tag);
    }

    /**
     * 取消所有请求
     */
    public void cancelAll() {
        Set<Object> keySet = tagMap.keySet();
        for (Object key : keySet) {
            Disposable disposable = tagMap.get(key);
            disposable.dispose();
        }
        tagMap.clear();
    }
}
