package com.framework.listener;

import android.content.DialogInterface;

public abstract class OnCancelListener implements DialogInterface.OnCancelListener {
  private Object mTag;

  public OnCancelListener(Object tag) {
    this.mTag = tag;
  }

  @Override
  public void onCancel(DialogInterface dialogInterface) {
    onCancel(dialogInterface, mTag);
  }

  public abstract void onCancel(DialogInterface dialogInterface, Object tag);
}