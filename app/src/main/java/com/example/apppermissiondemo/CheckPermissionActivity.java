package com.example.apppermissiondemo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.apppermissiondemo.startlog.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class CheckPermissionActivity extends AppCompatActivity {

    private static final String TAG = "CPA";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private List<String> permissionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_permission);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (LogUtils.isStartLog()) {
                Log.d(TAG, "Build.VERSION.SDK_INT=" + Build.VERSION.SDK_INT);
            }
            CheckSelfPermisson();
        }
    }

    // APP正常工作的必要权限,以后需要其他权限动态申请
    private void CheckSelfPermisson() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            showAlertDialog(permissions);
        } else {
            startMainActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(CheckPermissionActivity.this, "必须同意所有的权限APP才能正常使用", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            startMainActivity();
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 跳转的intent
     */
    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 当APP没有必要的权限时，展示温馨提示，要求用户同意权限，否则退出APP
     * @param permissions 需要请求的的权限的数组
     */
    private void showAlertDialog(final String[] permissions) {
        new AlertDialog.Builder(CheckPermissionActivity.this).setTitle("亲").setMessage("需要同意请求的权限才能正常工作哟！").setPositiveButton("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivityCompat.requestPermissions(CheckPermissionActivity.this, permissions, PERMISSION_REQUEST_CODE); // 开启请求权限
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        }).show();
    }
}
