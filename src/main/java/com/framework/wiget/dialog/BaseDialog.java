package com.framework.wiget.dialog;

/**
 * Created by lichen on 2018/5/28.
 */

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.framework.R;


/**
 * Created by lichen
 * 2016/5/25.
 * <p/>
 * 通用的dialog
 * 需要在自定义的dialogview中设置tag值，tag值为FUNC数组中的值，
 * init中的numberCallBack是回调的个数，用来设置监听，这里的DialogCallBack未抽成接口是方便各处使用
 * 如果需要定义固定的dialog可以在这里添加各种方法
 * <p/>
 * 必须在 view中添加func标记  这样才能完成回调，不需要每个dialog都写一个回调接口
 */
public class BaseDialog extends Dialog {

    private static final String[] FUNC = { "func1", "func2", "func3", "func4", "func5" };
    private View mView;
    private boolean flag = true;
    private Context context;

    public BaseDialog(Context context) {
        this(context, R.style.cmn_Dialog);
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    public void init(View view, int numberCallBack, final DialogCallBack callBack) {
        mView = view;
        if (mView != null) {
            setContentView(mView);
        }
        addListener(numberCallBack, callBack);
    }

    public void init(View view) {
        mView = view;
        if (mView != null) {
            setContentView(mView);
        }
        init(view, 0, null);
    }

    public void init(int res, int numberCallBack, final DialogCallBack callBack) {
        mView = View.inflate(context, res, null);
        setContentView(mView);
        addListener(numberCallBack, callBack);
    }

    public void clearListner() {
        flag = false;
    }

    //参数1是需要回调的个数（3个按钮就是三），参数二是回调（实现dialogCallBack的func方法）
    private void addListener(int numberCallBack, final DialogCallBack callBack) {
        flag = true;
        if (mView != null && callBack != null) {
            if (numberCallBack >= 1) {
                mView.findViewWithTag(FUNC[0])
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (flag) {
                                    callBack.func1();
                                }
                            }
                        });
            }
            if (numberCallBack >= 2) {
                mView.findViewWithTag(FUNC[1])
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (flag) {
                                    callBack.func2();
                                }
                            }
                        });
            }
            if (numberCallBack >= 3) {
                mView.findViewWithTag(FUNC[2])
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (flag) {
                                    callBack.func3();
                                }
                            }
                        });
            }
            if (numberCallBack >= 4) {
                mView.findViewWithTag(FUNC[3])
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (flag) {
                                    callBack.func4();
                                }
                            }
                        });
            }
            if (numberCallBack >= 5) {
                mView.findViewWithTag(FUNC[4])
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (flag) {
                                    callBack.func5();
                                }
                            }
                        });
            }
        }
    }
}

