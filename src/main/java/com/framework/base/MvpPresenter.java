package com.framework.base;

import com.framework.http.BaseException;

/**
 * Created by lichen on 2018/5/28.
 */

public interface MvpPresenter<V extends MvpView> {

    void onAttach(V mvpView);

    void onDetach();

    void handleApiException(BaseException exception);

    void setUserAsLoggedOut();

    boolean isTokenExpired();
}
