package com.framework.prefs;

/**
 * @author haibin.yuan.o.
 * @date 2017/12/28.
 * - Description：
 */

public interface TaskListener {
  /**
   * 耗时操作是否完成
   *
   * @return true
   */
  void isSuccess(boolean isSuccess);
}
