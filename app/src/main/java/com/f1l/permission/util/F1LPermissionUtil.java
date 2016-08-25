package com.f1l.permission.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.f1l.permission.R;

import java.util.ArrayList;

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
            onGranted(context,permsRequestCode,handler);
            return;
        }
        ArrayList<String> rPermissions = new ArrayList<>(); //未通过的权限集合
        {
            //通过的权限不需要再申请了,把未通过的存入新的集合
            for (String permission : permissions) {
                if (!hasSelfPermissions(context, permission)) {
                    rPermissions.add(permission);
                }
            }
        }
        if (rPermissions.size() == 0) {
            onGranted(context,permsRequestCode,handler);
        } else {
            ActivityCompat.requestPermissions(context, rPermissions.toArray(new String[rPermissions.size()]), permsRequestCode);
        }
    }

    public static void onRequestPermissionsResult(final @NonNull Activity context, int permsRequestCode,
                                                  String[] permissions,
                                                  int[] grantResults,
                                                  PermissionHandler handler) {

        if (handler == null) new RuntimeException("permission handler is null");

        if (verifyPermissions(grantResults)) {
            onGranted(context,permsRequestCode,handler);
        } else {
            if (shouldShowRequestPermissionRationale(context, permissions)) {
                onDenied(context,permsRequestCode,handler);
            } else {
                onNeverAsk(context,permsRequestCode,permissions,handler);
            }
        }
    }

    private static boolean hasSelfPermissions(Context context, String permission) {
        return PermissionChecker.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private static boolean hasSelfPermissions(Context context, String... permissions) {
        boolean hasSelfPermissions = true;
        for (String permission : permissions) {
            if (!(PermissionChecker.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED)) {
                hasSelfPermissions = false;
            }
        }
        return hasSelfPermissions;
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

        void onGranted(int permsRequestCode);

        /**
         * 多组权限申请时,只要有一个未允许就会执行
         * @param permsRequestCode
         * @return 为true 走使用者自定义逻辑
         */
        boolean onDenied(int permsRequestCode);

        /**
         * 多组权限申请时,未允许的权限中,全部都选择不再询问时才执行
         * @param permsRequestCode
         * @return 为true 走使用者自定义逻辑 默认打开权限设置提示并回调 {@link #onDenied(int)}
         */
        boolean onNeverAsk(int permsRequestCode);

    }

    private static boolean isNeedCheck() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    private static void onGranted(Activity activity ,int permsRequestCode , PermissionHandler handler) {
        handler.onGranted(permsRequestCode);
    }

    private static void onDenied(Activity activity ,int permsRequestCode , PermissionHandler handler) {
        if (handler.onDenied(permsRequestCode)) {
            return;
        }
    }

    private static void onNeverAsk(final Activity activity ,final int permsRequestCode ,final String[] permissions, final PermissionHandler handler) {
        if (handler.onNeverAsk(permsRequestCode)) {
            return;
        }
        new AlertDialog.Builder(activity)
                .setTitle("权限申请")
                .setMessage(String.format(activity.getResources().getString(R.string.permission_tip),PermissionTip.buildTip(permissions)))
                .setPositiveButton("去开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                        intent.setData(uri);
                        activity.startActivity(intent);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.onDenied(permsRequestCode);
                    }
                })
                .setCancelable(false)
                .show();

    }
}
