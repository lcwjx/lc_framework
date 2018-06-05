package com.framework.listener;

/**
 * Created by lichen on 2017/12/6.
 */

import android.view.View;
import android.view.View.OnClickListener;

import com.framework.enums.Notification;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

final class ViewClickObservable extends Observable<Object> {
  private final View view;

  ViewClickObservable(View view) {
    this.view = view;
  }

  @Override
  protected void subscribeActual(Observer<? super Object> observer) {
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.setOnClickListener(listener);
  }

  static final class Listener extends MainThreadDisposable implements OnClickListener {
    private final View                     view;
    private final Observer<? super Object> observer;

    Listener(View view, Observer<? super Object> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override
    public void onClick(View v) {
      if (!isDisposed()) {
        observer.onNext(Notification.INSTANCE);
      }
    }

    @Override
    protected void onDispose() {
      view.setOnClickListener(null);
    }
  }
}

