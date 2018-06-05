package com.framework.http;

import com.framework.utils.AppUtils;
import com.framework.utils.LogUtils;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * 请求管理器
 * Created by lichen on 2018/5/28.
 */

public class CommonRetrofitFactory {

    /**
     * 默认超时30s
     */
    private static final long TIMEOUT = 30;
    /**
     * api base url.
     */
    private static String BASE_API_URL;
    /**
     * box base url.
     */
    private static CommonRetrofitFactory INSTANCE;
    /**
     * 接口管理缓存
     */
    private Map<Class, Object> cacheService;
    private OkHttpClient.Builder okHttpClientBuilder;
    private HttpLoggingInterceptor httpLoggingInterceptor;

    public CommonRetrofitFactory(String baseUrlApi) {
        cacheService = new HashMap<>();

        BASE_API_URL = baseUrlApi;

        httpLoggingInterceptor = new HttpLoggingInterceptor("network_log");

        okHttpClientBuilder = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request()
                        .newBuilder();
                return chain.proceed(builder.build());
            }
        })
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS);
        LogUtils.getConfig().setLogSwitch(AppUtils.isDebug());
    }

    public static CommonRetrofitFactory getInstance(String baseUrlApi) {
        synchronized (CommonRetrofitFactory.class) {
            if (INSTANCE == null) {
                INSTANCE = new CommonRetrofitFactory(baseUrlApi);
            }
            return INSTANCE;
        }
    }

    private static SSLSocketFactory getSSLSocketFactory() throws Exception {
        //创建一个不验证证书链的证书信任管理器。
        final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
                    throws CertificateException {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
                    throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[0];
            }
        }};

        // Install the all-trusting trust manager
        final SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        // Create an ssl socket factory with our all-trusting manager
        return sslContext.getSocketFactory();
    }

    /**
     * 域名验证.
     *
     * @param hostUrls 域名列表
     * @return the hostname verifier
     */
    private static HostnameVerifier getHostnameVerifier(final String[] hostUrls) {

        HostnameVerifier TRUSTED_VERIFIER = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                boolean ret = false;
                for (String host : hostUrls) {
                    if (host.equalsIgnoreCase(hostname)) {
                        ret = true;
                    }
                }
                return ret;
            }
        };

        return TRUSTED_VERIFIER;
    }

    /**
     * 获取接口管理.
     *
     * @param <T>          接口类型
     * @param serviceClass 接口 class
     * @return 接口
     */
    public <T> T getService(Class<T> serviceClass) {
        return getService(serviceClass, true);
    }

    /**
     * 获取接口管理.
     *
     * @param <T>          接口类型
     * @param serviceClass 接口 class
     * @param https        是否是https
     * @return 接口
     */
    public <T> T getService(Class<T> serviceClass, boolean https) {
        if (cacheService.containsKey(serviceClass)) {
            return (T) cacheService.get(serviceClass);
        } else {
            okHttpClientBuilder = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request.Builder builder = chain.request()
                            .newBuilder();
                    return chain.proceed(builder.build());
                }
            })
                    .addInterceptor(httpLoggingInterceptor)
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS);

            T t = new Retrofit.Builder().baseUrl(BASE_API_URL)
                    .addConverterFactory(FastJsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClientBuilder.build())
                    .build()
                    .create(serviceClass);
            cacheService.put(serviceClass, t);
            return t;
        }
    }
}
