package com.framework.constant;

/**
 * Created by lichen on 2018/5/30.
 */

public class BaseUrlEnum {

    private static final String DEV = "dev";
    private static final String QA = "qa";
    private static final String PRD = "prd";
    private static final String SIT = "sit";


    public static BaseUrlEnums getEnvironmentParamsInfo(String environment) {
        switch (environment) {
            case DEV:
                return BaseUrlEnums.dev;
            case QA:
                return BaseUrlEnums.qa;
            case PRD:
                return BaseUrlEnums.prd;
            case SIT:
                return BaseUrlEnums.sit;
            default:
                break;
        }
        return null;
    }

    /**
     * The enum Base url enums.
     */
    public enum BaseUrlEnums {

        dev {
            @Override
            public String getBaseApiUrl() {
                return "http://gateway-qa.hopewoo.cn/";
            }

            @Override
            public String getImageApiUrl() {
                return "http://images-qa.hopewoo.cn";
            }

            @Override
            public String getDomainUrl() {
                return "http://rich-qa.hopewoo.cn/";
            }
        },
        qa {
            @Override
            public String getBaseApiUrl() {
                return "http://gateway-qa.hopewoo.cn/";
            }

            @Override
            public String getImageApiUrl() {
                return "http://images-qa.hopewoo.cn";
            }

            @Override
            public String getDomainUrl() {
                return "http://rich-qa.hopewoo.cn/";
            }
        },
        prd {
            @Override
            public String getBaseApiUrl() {
                return "https://gateway.hopewoo.com/";
            }

            @Override
            public String getImageApiUrl() {
                return "https://images.hopewoo.com";
            }

            @Override
            public String getDomainUrl() {
                return "https://rich.hopewoo.com/";
            }
        },
        sit {
            @Override
            public String getBaseApiUrl() {
                return "http://gateway-sit.hopewoo.com.cn:8080/";
            }

            @Override
            public String getImageApiUrl() {
                return "http://images-sit.hopewoo.com.cn:8080";
            }

            @Override
            public String getDomainUrl() {
                return "http://rich-sit.hopewoo.com.cn:8080/";
            }
        };

        /**
         * Gets base api url.
         *
         * @return the base api url
         */
        public abstract String getBaseApiUrl();

        public abstract String getImageApiUrl();

        public abstract String getDomainUrl();

    }
}
