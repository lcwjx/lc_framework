package com.framework.constant;

/**
 * Created by lichen on 2018/6/13.
 */

public class RequestParamsConstant {

    /**
     * baseRequest params key
     */
    public class BaseParamsKey {
        /**
         * 访问服务的api
         */
        public static final String API = "api";
        /**
         * 访问服务的method
         */
        public static final String METHOD = "method";
        /**
         * 后台服务的版本号
         */
        public static final String VERSION = "version";
        /**
         * 访问具体的服务的参数，以json形式
         */
        public static final String PARAMS = "params";
        /**
         * devived
         */
        public static final String DEVICE_ID = "deviceId";
        /**
         * appId
         */
        public static final String APP_ID = "appId";

    }

    /**
     * common params key
     */
    public class ParamsKay {
        /**
         * ip地址
         */
        public static final String IP_ADD = "ipAdd";
        /**
         * 会员id
         */
        public static final String MEMBER_ID = "memberId";

    }

}
