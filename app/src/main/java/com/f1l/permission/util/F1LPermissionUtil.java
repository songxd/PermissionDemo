package com.f1l.permission.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.widget.Toast;

/**
 * Created by songxd
 * on 16/8/24.
 */
public class F1LPermissionUtil {

    /**
     * 请求权限
     *
     * @param permissions 权限列表
     * @param handler     回调
     */
    public static void requestPermission(final @NonNull Activity context,
                                         final @NonNull String[] permissions,
                                         final int permsRequestCode,
                                         PermissionHandler handler) {
        if (handler == null) new RuntimeException("permission handler is null");
        if (!isNeedCheck()) {
            handler.onGranted(permsRequestCode);
            return;
        }
        if (hasSelfPermissions(context, permissions)) {
            handler.onGranted(permsRequestCode);
        } else {
            ActivityCompat.requestPermissions(context, permissions, permsRequestCode);
        }
    }

    public static void onRequestPermissionsResult(final @NonNull Activity context, int permsRequestCode,
                                                  String[] permissions,
                                                  int[] grantResults,
                                                  PermissionHandler handler) {

        if (handler == null) new RuntimeException("permission handler is null");

        if (verifyPermissions(grantResults)) {
            handler.onGranted(permsRequestCode);
        } else {
            if (shouldShowRequestPermissionRationale(context, permissions)) {
                handler.onDenied(permsRequestCode);
            } else {
                handler.onNeverAsk(permsRequestCode);
            }
        }
    }


    private static boolean hasSelfPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (!(PermissionChecker.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED)) {
                return false;
            }
        }
        return true;
    }

    private static boolean verifyPermissions(int... grantResults) {
        if (grantResults.length == 0) {
            return false;
        }
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    /**
     * 第一次请求权限时，用户拒绝了，下一次：shouldShowRequestPermissionRationale() 返回 true，应该显示一些为什么需要这个权限的说明
     *
     * 第二次请求权限时，用户拒绝了，并选择了“不在提醒”的选项时：shouldShowRequestPermissionRationale() 返回 false
     *
     * 设备的策略禁止当前应用获取这个权限的授权：shouldShowRequestPermissionRationale() 返回 false
     *
     *
     */
    private static boolean shouldShowRequestPermissionRationale(Activity activity, String... permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }
        return false;
    }

    public interface PermissionHandler {

        void onGranted(int requestCode);

        void onDenied(int requestCode);

        void onNeverAsk(int requestCode);

    }

    public static boolean isNeedCheck() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1;
    }
}
