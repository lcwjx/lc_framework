package com.framework.wiget.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.framework.R;
import com.framework.utils.WindowUtils;


/**
 * Created by lichen on 2018/5/28.
 */

public class ConfirmDialog extends BaseDialog{


    private TextView tvTitle;
    private TextView          tvContent;
    private TextView          tvConfirm;
    private TextView          tvCancel;
    private OnConfirmListener mListener;

    public ConfirmDialog(@NonNull Context context) {
        super(context);
        View view = View.inflate(context, R.layout.dialog_confirm, null);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvContent = view.findViewById(R.id.tvContent);
        tvConfirm = view.findViewWithTag(R.id.tvConfirm);
        tvCancel = view.findViewWithTag(R.id.tvCancel);
        init(view, 2, new DialogCallBack() {
            @Override
            public void func1() {
                super.func1();
                if (null != mListener) {
                    mListener.onCancel();
                }
                dismiss();
            }

            @Override
            public void func2() {
                super.func2();
                if (null != mListener) {
                    mListener.onConfirm();
                }
                dismiss();
            }
        });
        setCancelable(false);
        WindowUtils.setNotifyDialogType(getWindow());
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setContent(String content) {
        tvContent.setText(content);
    }

    public void setConfirmText(String confirm) {
        tvConfirm.setText(confirm);
    }

    public void setCancleText(String cancel) {
        tvCancel.setText(cancel);
    }

    public void setOnClickListener(OnConfirmListener listener) {
        mListener = listener;
    }

    public abstract static class OnConfirmListener {
        public abstract void onConfirm();

        public void onCancel() {
        }
    }
}
