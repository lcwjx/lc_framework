package com.framework.router;

import android.app.Activity;
import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;
import java.util.Map;

/**
 * @author haibin.yuan.o.
 * @date 2018/1/2.
 * - Description：
 */

public class RouterUtils {

  /**
   * 构建标准的路由请求
   */
  public static Object navigation(String path) {
    return ARouter.getInstance()
        .build(path)
        .navigation();
  }

  /**
   * 构建标准的路由请求，startActivityForResult
   * navigation的第一个参数必须是Activity，第二个参数则是RequestCode
   */
  public static void startActivityForResult(String path, Activity activity, int code) {
    ARouter.getInstance()
        .build(path)
        .navigation(activity, code);
  }

  /**
   * 带flag跳转
   *
   * @param path url
   * @param intent flag
   */
  public static void startActivityWithFlag(String path, int intent) {
    ARouter.getInstance()
        .build(path)
        .withFlags(intent)
        .navigation();
  }

  /**
   * 带参数跳转
   */
  public static Object navigation(String path, Map<String, String> params) {
    Postcard postcard = ARouter.getInstance()
        .build(path);
    for (String key : params.keySet()) {
      postcard.withString(key, params.get(key));
    }
    return postcard.navigation();
  }

  public void startActivityForCallback() {
  }
}
