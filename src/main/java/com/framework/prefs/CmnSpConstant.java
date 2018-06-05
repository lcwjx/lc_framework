package com.framework.prefs;

/**
 * Created by lichen on 2018/5/28.
 */

public class CmnSpConstant {

    /**
     * 全局sp名称.
     */
    public static final String SP_NAME = "sp_hope";

    /**
     * 全局sp对应Key.
     */
    public static class Key {
        /**
         * access_token.
         */
        public static final String ACCESS_TOKEN = "access_token";
        /**
         * access_token.
         */
        public static final String ACCESS_TOKEN_KEY = "hopehope";

        /**
         * phone_no.
         */
        public static final String PHONE_NO = "phone_no";
        /**
         * uid.
         */
        public static final String UID = "uid";

        /**
         * 判断日志模式，默认 true > debug
         */
        public static final String IS_LOGGER_DEBUG = "is_logger_debug";

        /**
         * 横竖屏
         */
        public static final String SET_ORITETATION_STATUS = "set_oritetation_status";
        /**
         * The constant 判断WiFi自动升级的key.
         */
        public static final String IS_WIFI_UPDATE = "isWifiUpdate";

        /**
         * base url type , base url类型 . 返回 dev , uat , prod
         */
        public static final String BASE_URL_TYPE = "base_url_type";

        /**
         * base url
         */
        public static final String BASE_URL_API = "base_url_api";

        /**
         *
         */
        public static final String NATIVE_SERVER_TIME_OFFSET = "native_sever_time_offset";
    }
}
