package com.framework.wiget.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.framework.R;
import com.framework.utils.ConvertUtils;


/**
 * @author binbin.liu
 * @date 2018/1/19
 */

public class BlueDotLoadingView extends FrameLayout {

  private ImageView ivDotOne;
  private ImageView ivDotTwo;

  public BlueDotLoadingView(@NonNull Context context) {
    super(context);
  }

  public BlueDotLoadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    View view = View.inflate(context, R.layout.view_blue_loading, this);
    ivDotOne = view.findViewById(R.id.ivDotOne);
    ivDotTwo = view.findViewById(R.id.ivDotTwo);
    Animation animOne = new TranslateAnimation(0, ConvertUtils.dp2px(35), 0, 0);
    animOne.setRepeatMode(Animation.REVERSE);
    animOne.setDuration(500);
    animOne.setRepeatCount(Integer.MAX_VALUE);
    //animOne.setInterpolator(new AccelerateInterpolator());
    Animation animTwo = new TranslateAnimation(0, -ConvertUtils.dp2px(35), 0, 0);
    animTwo.setRepeatMode(Animation.REVERSE);
    animTwo.setDuration(500);
    //animTwo.setInterpolator(new AccelerateInterpolator());
    animTwo.setRepeatCount(Integer.MAX_VALUE);
    ivDotOne.setAnimation(animOne);
    ivDotTwo.setAnimation(animTwo);
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    ivDotOne.getAnimation()
        .start();
    ivDotTwo.getAnimation()
        .start();
  }
}
