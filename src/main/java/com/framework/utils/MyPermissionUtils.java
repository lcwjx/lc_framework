package com.framework.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.framework.base.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by binbin.liu on 2018/2/1.
 *
 * 依赖于baseActivity
 * 在baseActivity添加如下代码
 *
 *
 * private MyPermissionUtils.OnReponsePermissionListener mOnPermissionListener;
 *
 * public void setPermissionListener(MyPermissionUtils.OnReponsePermissionListener onPermissionListener) {
 * mOnPermissionListener = onPermissionListener;
 * }
 *
 * @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
 * grantResults) {
 * super.onRequestPermissionsResult(requestCode, permissions, grantResults);
 * if (null != mOnPermissionListener && requestCode == mOnPermissionListener.getRequestCode()) {
 * mOnPermissionListener.onResponse(permissions, grantResults);
 * }
 * }
 *
 *
 * 使用样例 copy
 MyPermissionUtils.requestPermissions(BoxActivity.this, 1121, MyPermissionUtils.STORAGE_PERMISSION, new MyPermissionUtils.OnRequestPermissionListener() {
@Override
public void onRequest(boolean isGranted) {
if(isGranted){
fileUploadDialog.show();
}
}
},CmnDialogManager.getPermissionHintDialog(BoxActivity.this,MyPermissionUtils.STORAGE_PERMISSION));


 同时多次调用requestPermission会导致第一次后的其他请求直接通过
 */

public final class MyPermissionUtils {

  private static Map<String, String> dangerousPermissionDes;
  public static String[] STORAGE_PERMISSION =
      new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE };

  /**
   *
   *
   * */
  @TargetApi(Build.VERSION_CODES.M)
  public static void requestPermissions(final Context context, int requestCode, String[] permissions,
      final OnRequestPermissionListener listener, final Dialog neverAskHintDialog) {
    if (context instanceof Activity) {
      if (null != listener) {
        ((BaseActivity) context).setPermissionResponseListener(new OnResponsePermissionListener(requestCode) {
          @Override
          public void onResponse(String[] permissions, int[] grantResults) {
            String[] deniedPermissions = getDeniedPermissions(context, permissions);
            if (deniedPermissions.length > 0) {
              boolean rationale = shouldShowRequestPermissionRationale(context, permissions);
              if (rationale) {
                //被拒绝以后
                listener.onRequest(false);
              } else {
                //被彻底拒绝以后
                if (null != neverAskHintDialog) {
                  neverAskHintDialog.show();
                }
              }
            }else{
              listener.onRequest(true);
            }
          }
        });
      }

      String[] deniedPermissions = getDeniedPermissions(context, permissions);
      if (deniedPermissions.length > 0) {
        ((Activity) context).requestPermissions(deniedPermissions, requestCode);
      } else {
        if (null != listener) {
          //请求成功
          listener.onRequest(true);
        }
      }
    } else {
      throw new RuntimeException("Context must be an Activity");
    }
  }

  /**
   * 获取请求权限中需要授权的权限
   */
  public static String[] getDeniedPermissions(final Context context, final String[] permissions) {
    List<String> deniedPermissions = new ArrayList<>();
    for (String permission : permissions) {
      if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
        deniedPermissions.add(permission);
      }
    }
    return deniedPermissions.toArray(new String[deniedPermissions.size()]);
  }

  /**
   * 是否有权限需要说明提示
   */
  public static boolean shouldShowRequestPermissionRationale(final Context context, final String... deniedPermissions) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
      return false;
    }
    boolean rationale;
    for (String permission : deniedPermissions) {
      rationale = ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission);
      if (rationale) {
        // 权限被主动关闭  或 询问过没有权限
        return true;
      }
    }
    //没有询问过 或 非 询问过有权限 或 询问过被彻底拒绝
    return false;
  }

  /**
   * 请求权限者的回调
   */
  public static abstract class OnRequestPermissionListener {
    public OnRequestPermissionListener() {
    }

    public abstract void onRequest(boolean isGranted);
  }

  /**
   * 获取申请权限结果的回调
   */

  public static abstract class OnResponsePermissionListener {
    private int requestCode;

    public OnResponsePermissionListener(int requestCode) {
      this.requestCode = requestCode;
    }

    public int getRequestCode() {
      return requestCode;
    }

    public abstract void onResponse(String[] permissions, int[] grantResults);
  }

  /**
   * 获取危险权限描述
   */
  public static String getPermissionDes(String[] permissions) {
    String permissionsDes = "";
    if (null == dangerousPermissionDes) {
      dangerousPermissionDes = new HashMap();

      dangerousPermissionDes.put(Manifest.permission.READ_CALENDAR, "日程权限");
      dangerousPermissionDes.put(Manifest.permission.WRITE_CALENDAR, "日程权限");

      dangerousPermissionDes.put(Manifest.permission.CAMERA, "相机权限");

      dangerousPermissionDes.put(Manifest.permission.BODY_SENSORS, "传感器权限");

      dangerousPermissionDes.put(Manifest.permission.RECORD_AUDIO, "录音权限");

      dangerousPermissionDes.put(Manifest.permission.READ_CONTACTS, "联系人权限");
      dangerousPermissionDes.put(Manifest.permission.WRITE_CONTACTS, "联系人权限");
      dangerousPermissionDes.put(Manifest.permission.GET_ACCOUNTS, "联系人权限");

      dangerousPermissionDes.put(Manifest.permission.ACCESS_FINE_LOCATION, "位置权限");
      dangerousPermissionDes.put(Manifest.permission.ACCESS_COARSE_LOCATION, "位置权限");

      dangerousPermissionDes.put(Manifest.permission.READ_PHONE_STATE, "电话权限");
      dangerousPermissionDes.put(Manifest.permission.CALL_PHONE, "电话权限");
      dangerousPermissionDes.put(Manifest.permission.READ_CALL_LOG, "电话权限");
      dangerousPermissionDes.put(Manifest.permission.WRITE_CALL_LOG, "电话权限");
      dangerousPermissionDes.put(Manifest.permission.ADD_VOICEMAIL, "电话权限");
      dangerousPermissionDes.put(Manifest.permission.USE_SIP, "电话权限");
      dangerousPermissionDes.put(Manifest.permission.PROCESS_OUTGOING_CALLS, "电话权限");

      dangerousPermissionDes.put(Manifest.permission.SEND_SMS, "短信权限");
      dangerousPermissionDes.put(Manifest.permission.RECEIVE_SMS, "短信权限");
      dangerousPermissionDes.put(Manifest.permission.READ_SMS, "短信权限");
      dangerousPermissionDes.put(Manifest.permission.RECEIVE_WAP_PUSH, "短信权限");
      dangerousPermissionDes.put(Manifest.permission.RECEIVE_MMS, "短信权限");

      dangerousPermissionDes.put(Manifest.permission.READ_EXTERNAL_STORAGE, "外部存储权限");
      dangerousPermissionDes.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, "外部存储权限");
    }

    Set<String> set = new HashSet<>();
    for (String permission : permissions) {
      String des = dangerousPermissionDes.get(permission);
      if (TextUtils.isEmpty(des)) {
        continue;
      } else {
        set.add(des);
      }
    }

    for (String des : set) {
      permissionsDes = permissionsDes + des ;
    }

    return permissionsDes;
  }
}
