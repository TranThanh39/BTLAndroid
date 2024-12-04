package com.haruma.app.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.haruma.app.dto.DatabaseHelper;
import com.haruma.app.model.Diary;
import com.haruma.app.service.JsonHelper;
import com.haruma.app.service.StorageHelper;
import com.haruma.app.service.UriHelper;

import java.util.List;
import java.util.Objects;

public class BackupDiaryViewModel extends AndroidViewModel {

    private final DatabaseHelper databaseHelper;
    private final MutableLiveData<List<Diary>> diariesData = new MutableLiveData<>();

    private final MutableLiveData<String> permissionStatus = new MutableLiveData<>();

    private final Context context;

    public BackupDiaryViewModel(Application application) {
        super(application);
        this.context = application.getApplicationContext();
        this.databaseHelper = new DatabaseHelper(this.context);
    }

    public LiveData<List<Diary>> getAllDiaries() {
        List<Diary> diaries = databaseHelper.getAllDiaries();
        diariesData.setValue(diaries);
        return diariesData;
    }

    public void readJson(Context context, Uri fileUri) {
        String filePath = UriHelper.resolveUri(context, fileUri);
        JsonHelper.readJson(filePath, Objects.requireNonNull(diariesData.getValue()).getClass());
    }

    public void writeJson(Context context, Uri fileUri) {
        JsonHelper.writeJson(UriHelper.resolveUri(context, fileUri), databaseHelper.getAllDiaries());
    }

    public LiveData<String> getPermissionStatus() {
        return permissionStatus;
    }

    public void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                permissionStatus.setValue("Cho phép");
            } else {
                permissionStatus.setValue("Từ chối");
            }
        } else {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                permissionStatus.setValue("Cho phép");
            } else {
                permissionStatus.setValue("Từ chối");
            }
        }
    }
}

