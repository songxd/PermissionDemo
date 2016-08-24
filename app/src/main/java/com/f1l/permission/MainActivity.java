package com.f1l.permission;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.f1l.permission.util.F1LPermissionUtil;

public class MainActivity extends AppCompatActivity {


    String[] permissions = new String[]{Manifest.permission.CAMERA};
    private F1LPermissionUtil.PermissionHandler mHandler = new F1LPermissionUtil.PermissionHandler() {
        @Override
        public void onGranted(int requestCode) {
            Toast.makeText(MainActivity.this, "用户已同意", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivity(intent);
        }

        @Override
        public void onDenied(int requestCode) {
            Toast.makeText(MainActivity.this, "拒绝不能使用该功能", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNeverAsk(int requestCode) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("权限申请")
                    .setMessage("在设置-应用-权限中开始摄像头权限，以保证功能的正常使用")
                    .setPositiveButton("去开启", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);

                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .setCancelable(false)
                    .show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        F1LPermissionUtil.requestPermission(this, permissions, 001, mHandler);
    }

    @Override

    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {

        F1LPermissionUtil.onRequestPermissionsResult(this, permsRequestCode, permissions, grantResults, mHandler);

    }
}
