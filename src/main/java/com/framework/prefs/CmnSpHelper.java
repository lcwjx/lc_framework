package com.framework.prefs;

import android.content.Context;
import android.text.TextUtils;

import com.framework.R;
import com.framework.utils.App;
import com.framework.utils.EncodeUtils;
import com.framework.utils.EncryptUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lichen on 2018/5/28.
 */

public class CmnSpHelper {

    private static CmnSpHelper cmnSpHelper;
    private SPUtils mPrefs;
    private String accessToken;

    private CmnSpHelper(Context context) {
        mPrefs = SPUtils.getInstance(context, CmnSpConstant.SP_NAME);
    }

    public static CmnSpHelper getInstance(Context context) {
        synchronized (CmnSpHelper.class) {
            if (null == cmnSpHelper) {
                cmnSpHelper = new CmnSpHelper(context);
            }
            return cmnSpHelper;
        }
    }

    /**
     * 初始化
     */
    public void initBaseUrl(String baseUrl) {
        if (TextUtils.isEmpty(getBaseUrlApi())) {
            setBaseUrlApi(baseUrl);
        }
    }

    /**
     * 初始化
     */
    public void initBaseUrlType(String urlType) {
        if (TextUtils.isEmpty(getBaseUrlType())) {
            setBaseUrlType(urlType);
        }
    }

    /**
     * 保存token
     */
    public void setAccessToken(final String token, String tokenKey) {
        setAccessToken(token, null, tokenKey);
    }

    /**
     * 保存token
     * 有子线程保存完成回调，一般不需要用到
     * 增加保存至内存。
     */
    public void setAccessToken(final String token, final TaskListener customEncryHelper, final String TokenKey) {
        if (TextUtils.isEmpty(token)) {
            if (customEncryHelper != null) {
                customEncryHelper.isSuccess(false);
            }
            accessToken = "";
            mPrefs.put(CmnSpConstant.Key.ACCESS_TOKEN, "");
            return;
        }
        accessToken = token;

        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                mPrefs.put(CmnSpConstant.Key.ACCESS_TOKEN, new String(EncodeUtils.base64Encode(EncryptUtils.encryptDES(token.getBytes(), TokenKey.getBytes()))));
                e.onNext(true);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean b) throws Exception {
                        if (customEncryHelper != null) {
                            customEncryHelper.isSuccess(true);
                        }
                    }
                });
    }

    /**
     * 获取token
     * 有缓存，取缓存
     * 没缓存，取sp
     */
    public String getAccessToken() {

        //先判断，缓存是否有
        if (!TextUtils.isEmpty(accessToken)) {
            return accessToken;
        }

        String token = mPrefs.getString(CmnSpConstant.Key.ACCESS_TOKEN);
        if (TextUtils.isEmpty(token)) {
            return "";
        }
        String tokenCache = new String(EncryptUtils.decryptDES(EncodeUtils.base64Decode(token), CmnSpConstant.Key.ACCESS_TOKEN_KEY.getBytes()));

        accessToken = tokenCache;
        return tokenCache;
    }

    public String getPhoneNo() {
        return mPrefs.getString(CmnSpConstant.Key.PHONE_NO);
    }

    public void setPhoneNo(String phoneNo) {
        mPrefs.put(CmnSpConstant.Key.PHONE_NO, phoneNo);
    }

    /**
     * 获取uid
     */
    public String getUid() {
        return mPrefs.getString(CmnSpConstant.Key.UID);
    }

    /**
     * 保存uid
     */
    public void setUid(String uid) {
        mPrefs.put(CmnSpConstant.Key.UID, uid);
    }

    /**
     * 清除存储的token和uid
     */
    public void clearSession() {
        mPrefs.remove(CmnSpConstant.Key.ACCESS_TOKEN, true);
        mPrefs.remove(CmnSpConstant.Key.UID, true);
    }

    /**
     * 获取手机是否横竖屏设置
     */
    public boolean getOritentationStatus() {
        return mPrefs.getBoolean(CmnSpConstant.Key.SET_ORITETATION_STATUS, App.getContext()
                .getResources()
                .getBoolean(R.bool.cmn_landscape_only));
    }

    /**
     * 设置手机横竖屏
     *
     * @param oritentationStatus false表示竖屏 true表示横屏
     */
    public void setOritentationStatus(boolean oritentationStatus) {
        mPrefs.put(CmnSpConstant.Key.SET_ORITETATION_STATUS, oritentationStatus);
    }

    /**
     * 获取日志模式 true > debug, false > release
     */
    public boolean isLoggerDebug() {
        return mPrefs.getBoolean(CmnSpConstant.Key.IS_LOGGER_DEBUG, false);
    }

    /**
     * 设置日志模式
     *
     * @param isDebug true > debug, false > release
     */
    public void setLoggerDebug(boolean isDebug) {
        mPrefs.put(CmnSpConstant.Key.IS_LOGGER_DEBUG, isDebug);
    }

    /**
     * 保存wifi下自动升级
     */
    public void setIsWifiUpdate(boolean flag) {
        mPrefs.put(CmnSpConstant.Key.IS_WIFI_UPDATE, flag);
    }

    /**
     * 获取wifi自动升级
     */
    public boolean isWifiUpdate() {
        return mPrefs.getBoolean(CmnSpConstant.Key.IS_WIFI_UPDATE, true);
    }

    /**
     * 获取base url api
     *
     * @return base url api
     */
    public String getBaseUrlApi() {
        return mPrefs.getString(CmnSpConstant.Key.BASE_URL_API, "");
    }

    /**
     * 设置base url api
     *
     * @param baseUrl api
     */
    public void setBaseUrlApi(String baseUrl) {
        mPrefs.put(CmnSpConstant.Key.BASE_URL_API, baseUrl);
    }

    /**
     * @return 获取运行环境 dev , uat , prod
     */
    public String getBaseUrlType() {
        return mPrefs.getString(CmnSpConstant.Key.BASE_URL_TYPE, "");
    }

    /**
     * @param baseUrlType 运行环境 dev , uat , prod
     */
    public void setBaseUrlType(String baseUrlType) {
        mPrefs.put(CmnSpConstant.Key.BASE_URL_TYPE, baseUrlType);
    }

    /**
     * 设置本地时间与服务器时间差
     */
    public void setTimeOffset(long offset) {
        mPrefs.put(CmnSpConstant.Key.NATIVE_SERVER_TIME_OFFSET, offset);
    }

    /**
     * 获取本地时间与服务器时间差
     */
    public long getTimeOffset() {
        return mPrefs.getLong(CmnSpConstant.Key.NATIVE_SERVER_TIME_OFFSET, 0);
    }
}
