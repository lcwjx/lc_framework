package com.framework.base;

import com.framework.R;
import com.framework.http.BaseException;
import com.framework.utils.App;
import com.framework.utils.StringUtils;

/**
 * Created by lichen on 2018/5/28.
 */

public class BasePresenter<V extends MvpView> implements MvpPresenter<V> {
    private V mMvpView;
    private boolean mTokenExpired;

    public BasePresenter() {

    }

    @Override
    public void onAttach(V mvpView) {
        this.mMvpView = mvpView;
    }

    @Override
    public void onDetach() {
        mMvpView = null;
    }

    @Override
    public void handleApiException(BaseException exception) {
        if (String.valueOf(BaseException.ANALYTIC_SERVER_DATA_ERROR)
                .equals(exception.getCode())) {
            exception.setMsg(App.getContext()
                    .getString(R.string.cmn_exception_parse_data));
        } else if (String.valueOf(BaseException.CONNECT_ERROR)
                .equals(exception.getCode())) {
            exception.setMsg(App.getContext()
                    .getString(R.string.cmn_exception_network_connect_fail));
        } else if (String.valueOf(BaseException.TIME_OUT_ERROR)
                .equals(exception.getCode())) {
            exception.setMsg(App.getContext()
                    .getString(R.string.cmn_exception_network_connect_timeout));
        } else if (String.valueOf(BaseException.NETWORK_ERROR)
                .equals(exception.getCode())) {
            exception.setMsg(App.getContext()
                    .getString(R.string.cmn_exception_network_error));
        } else if (String.valueOf(BaseException.UN_KNOWN_ERROR)
                .equals(exception.getCode())) {
            exception.setMsg(App.getContext()
                    .getString(R.string.cmn_exception_un_know));
        } else {
            if (StringUtils.isEmpty(exception.getMsg())) {
                exception.setMsg(App.getContext()
                        .getString(R.string.cmn_exception_un_know));
            }
        }
    }

    @Override
    public void setUserAsLoggedOut() {

    }

    @Override
    public boolean isTokenExpired() {
        return mTokenExpired;
    }

    public V getMvpView() {
        return mMvpView;
    }
}
