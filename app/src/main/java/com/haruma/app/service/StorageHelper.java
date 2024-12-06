package com.haruma.app.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import androidx.activity.result.ActivityResultLauncher;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.haruma.app.model.Callback;

public class StorageHelper {

    public static void requestStoragePermission(Activity activity, ActivityResultLauncher<Intent> resultLauncher) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    1
            );
        } else {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                    android.net.Uri.parse("package:" + activity.getPackageName()));
            resultLauncher.launch(intent);
        }
    }

    public static boolean checkStoragePermission(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            return ContextCompat.checkSelfPermission(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE) == android.content.pm.PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == android.content.pm.PackageManager.PERMISSION_GRANTED;
        } else {
            return Environment.isExternalStorageManager();
        }
    }
}
