package com.framework.wiget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.framework.R;


/**
 * Created by lichen on 2017/11/1.
 */

public class CusTitleView extends RelativeLayout implements View.OnClickListener {

    private String centerText;
    private int backSrc;
    private boolean backIsShow;

    /**
     * 左侧文字样式
     */
    private static final int LEFT_TEXT_DEFAULT_STYLE = R.style.text_15_63718D;
    /**
     * 右侧文字样式
     */
    private static final int RIGHT_TEXT_DEFAULT_STYLE = R.style.text_15_63718D;
    /**
     * 右侧文字样式
     */
    private static final int CENTER_TEXT_DEFAULT_STYLE = R.style.text_18_black;
    private int centerTextAppearance;
    private String leftText;
    private int leftTextAppearance;
    private String rightText;
    private int rightTextAppearance;

    private OnTitleClickListener mOnTitleClickListener;
    private ImageView ivLeftClose;
    private TextView tvRightText;
    private TextView titleCenterText;
    private TextView tvLeftText;

    public CusTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFromAttributes(context, attrs);
        initView(context);
    }

    public CusTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFromAttributes(context, attrs);
        initView(context);
    }

    private void initFromAttributes(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CusTitleView);
            centerText = typedArray.getString(R.styleable.CusTitleView_title_center_text);
            centerTextAppearance = typedArray.getResourceId(R.styleable.CusTitleView_title_center_text_appearance, CENTER_TEXT_DEFAULT_STYLE);
            backSrc = typedArray.getResourceId(R.styleable.CusTitleView_title_back_src, R.mipmap.icon_left_back);
            backIsShow = typedArray.getBoolean(R.styleable.CusTitleView_title_back_isShow, true);
            leftText = typedArray.getString(R.styleable.CusTitleView_title_left_text);
            leftTextAppearance = typedArray.getResourceId(R.styleable.CusTitleView_title_left_text_appearance, LEFT_TEXT_DEFAULT_STYLE);
            rightText = typedArray.getString(R.styleable.CusTitleView_title_right_text);
            rightTextAppearance = typedArray.getResourceId(R.styleable.CusTitleView_title_right_text_appearance, RIGHT_TEXT_DEFAULT_STYLE);
            typedArray.recycle();
        }
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.cus_title_view, this, true);
        ivLeftClose = view.findViewById(R.id.iv_left_close);
        tvRightText = view.findViewById(R.id.tv_right_text);
        titleCenterText = view.findViewById(R.id.title_center_text);
        tvLeftText = view.findViewById(R.id.tv_left_text);


        setCenterText(centerText);
        setcenterTextAppearance(context, centerTextAppearance);
        if (backIsShow) {
            setLeftImage(backSrc);
        } else {
            ivLeftClose.setVisibility(GONE);
        }
        if ("".equals(leftText)) {
            tvLeftText.setVisibility(GONE);
        } else {
            tvLeftText.setVisibility(VISIBLE);
            setLeftText(context, leftText);

        }
        if ("".equals(rightText)) {
            tvRightText.setVisibility(GONE);
        } else {
            tvRightText.setVisibility(VISIBLE);
            setRightText(context, rightText);

        }
        ivLeftClose.setOnClickListener(this);
        tvRightText.setOnClickListener(this);
        tvLeftText.setOnClickListener(this);

    }

    public void setRightText(Context context, String rightText) {
        tvRightText.setTextAppearance(context, rightTextAppearance);
        tvRightText.setText(rightText);
    }


    public void setLeftText(Context context, String leftText) {
        tvLeftText.setTextAppearance(context, leftTextAppearance);
        tvLeftText.setText(leftText);
    }

    public void setLeftImage(int backSrc) {
        ivLeftClose.setImageResource(backSrc);
    }

    public void setcenterTextAppearance(Context context, int centerTextAppearance) {
        titleCenterText.setTextAppearance(context, centerTextAppearance);
    }

    public void setCenterText(String centerText) {
        titleCenterText.setText(centerText);
    }

    public String getTitleText() {
        return titleCenterText.getText().toString();
    }

    public void setOnTitleClickListener(OnTitleClickListener listener) {
        mOnTitleClickListener = listener;
    }


    public interface OnTitleClickListener {
        void onLeftClick(View view);

        void onRightClick(View view);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_left_close || view.getId() == R.id.tv_left_text) {
            if (mOnTitleClickListener != null) {
                mOnTitleClickListener.onLeftClick(view);
                return;
            }
            sendKeyBackEvent();
        } else if (view.getId() == R.id.tv_right_text) {
            if (mOnTitleClickListener != null) {
                mOnTitleClickListener.onRightClick(view);
            }
        }
    }


    public void sendKeyBackEvent() {
        final Context context = getContext();
        if (context instanceof Activity) {
            KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
            ((Activity) context).onKeyDown(KeyEvent.KEYCODE_BACK, keyEvent);
        }
    }
}
