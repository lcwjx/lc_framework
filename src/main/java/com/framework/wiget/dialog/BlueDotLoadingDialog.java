package com.framework.wiget.dialog;

import android.content.Context;
import android.view.View;

import com.framework.R;
import com.framework.utils.WindowUtils;

/**
 * Created by lichen
 */

public class BlueDotLoadingDialog extends BaseDialog {

  public BlueDotLoadingDialog(Context context) {
    super(context);
    View view = View.inflate(context, R.layout.dialog_blue_dot_loading, null);
    init(view, 0, null);
    WindowUtils.setProgressDialogType(getWindow());
  }
}
