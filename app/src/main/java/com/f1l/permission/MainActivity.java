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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.f1l.permission.util.F1LPermissionUtil;
import com.f1l.permission.util.PermissionTip;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    String[] permissions = new String[]{Manifest.permission.CAMERA};
    private F1LPermissionUtil.PermissionHandler mHandler = new F1LPermissionUtil.PermissionHandler() {
        @Override
        public void onGranted(int requestCode) {
            Toast.makeText(MainActivity.this, "用户已同意", Toast.LENGTH_SHORT).show();
            /*Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivity(intent);*/
        }

        @Override
        public boolean onDenied(int requestCode) {
            Toast.makeText(MainActivity.this, "拒绝不能使用该功能", Toast.LENGTH_SHORT).show();
            return true;
        }

        @Override
        public boolean onNeverAsk(int requestCode) {
            return false;
        }
    };

    private ListView listview;
    private Button btn1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = (ListView)findViewById(R.id.listview);
        btn1 = (Button)findViewById(R.id.btn1);
        final ArrayList <String> data = new ArrayList<>();
        for (String permission : PermissionTip.permissionMsgMap.keySet()) {
            data.add(permission);
        }
        listview.setAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,//只能有一个定义了id的TextView
                data));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                F1LPermissionUtil.requestPermission(MainActivity.this,new String[]{data.get(position)},position,mHandler);
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                F1LPermissionUtil.requestPermission(MainActivity.this,data.toArray(new String[data.size()]),998,mHandler);
            }
        });

    }

    @Override

    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {

        F1LPermissionUtil.onRequestPermissionsResult(this, permsRequestCode, permissions, grantResults, mHandler);

    }
}
