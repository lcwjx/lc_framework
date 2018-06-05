package com.framework.http;

import com.framework.prefs.CmnSpHelper;
import com.framework.utils.App;

/**
 * 请求管理器
 * Created by lichen on 2018/5/28.
 */

public class RetrofitFactory {

    private static CommonRetrofitFactory INSTANCE;

    public static CommonRetrofitFactory getInstance() {
        synchronized (RetrofitFactory.class) {
            if (INSTANCE == null) {
                INSTANCE = CommonRetrofitFactory.getInstance(CmnSpHelper.getInstance(App.getContext())
                        .getBaseUrlApi());
            }
            return INSTANCE;
        }
    }
}
