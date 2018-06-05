package com.framework.wiget.dialog.manager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import com.framework.R;
import com.framework.utils.ActivityStack;
import com.framework.utils.App;
import com.framework.utils.MyPermissionUtils;
import com.framework.wiget.dialog.BlueDotLoadingDialog;
import com.framework.wiget.dialog.ConfirmDialog;


/**
 * Created by lichen on 2018/5/28.
 */

public class CmnDialogManager {
    //确认取消dialog，可以根据自己的喜好负载
    public static ConfirmDialog getConfirmDialog(Context context, String title, String content,
                                                 ConfirmDialog.OnConfirmListener listener) {
        ConfirmDialog dialog = new ConfirmDialog(context);
        dialog.setTitle(title);
        dialog.setContent(content);
        dialog.setOnClickListener(listener);
        return dialog;
    }

    public static BlueDotLoadingDialog getLoadingDialog(Context context, boolean isCancelAble) {
        BlueDotLoadingDialog dialog = new BlueDotLoadingDialog(context);
        dialog.setCancelable(isCancelAble);
        return dialog;
    }

    //确认取消dialog，可以根据自己的喜好负载
    public static ConfirmDialog getPermissionHintDialog(Context context, String[] permissions) {
        ConfirmDialog dialog = new ConfirmDialog(context);
        dialog.setContent(context.getString(R.string.permission_hint_perfix) + MyPermissionUtils.getPermissionDes(permissions));
        dialog.setOnClickListener(new ConfirmDialog.OnConfirmListener() {
            @Override
            public void onConfirm() {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", App.getContext()
                        .getPackageName(), null);
                intent.setData(uri);
                ActivityStack.getInstance()
                        .topActivity()
                        .startActivity(intent);
            }
        });
        return dialog;
    }
}
