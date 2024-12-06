package com.haruma.app.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.haruma.app.dto.DatabaseHelper;
import com.haruma.app.dto.UserSessionManager;
import com.haruma.app.model.Diary;
import com.haruma.app.service.JsonHelper;
import com.haruma.app.service.UriHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BackupDiaryViewModel extends AndroidViewModel {

    private final DatabaseHelper databaseHelper;

    private final MutableLiveData<String> permissionStatus = new MutableLiveData<>();

    private final Context context;


    public BackupDiaryViewModel(Application application) {
        super(application);
        this.context = application.getApplicationContext();
        this.databaseHelper = new DatabaseHelper(this.context);
    }

    @SuppressLint("DefaultLocale")
    public void readJson(Context context, Uri fileUri) {
        try {
            String filePath = UriHelper.resolveUri(context, fileUri);
            List<Map<String, Object>> myList = JsonHelper.readJson(filePath);
            this.onChangeDiary(makeList(myList));
            Toast.makeText(this.context, String.format("Đã thêm %d nhật ký vào cơ sở dữ liệu", myList.size()), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this.context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private List<Diary> makeList(List<Map<String, Object>> rawList) {
        List<Diary> diaryList = new ArrayList<>();
        for (Map<String, Object> rawEntry : rawList) {
            Diary diary = new Diary();
            diary.setDiaryId(((Double) Objects.requireNonNull(rawEntry.get("diaryId"))).intValue());
            diary.setDay((String) rawEntry.get("day"));
            diary.setName((String) rawEntry.get("name"));
            diary.setNote((String) rawEntry.get("note"));
            diary.setStartTime((String) rawEntry.get("startTime"));
            diary.setEndTime((String) rawEntry.get("endTime"));
            diary.setUserId(((Double) Objects.requireNonNull(rawEntry.get("userId"))).intValue());
            diaryList.add(diary);
        }

        return diaryList;
    }

    public void writeJson(Context context, Uri fileUri) {
        JsonHelper.writeJson(UriHelper.resolveUri(context, fileUri), databaseHelper.getAllDiaries());
    }

    public void onChangeDiary(List<Diary> diaries) {
        this.databaseHelper.clearDiary();
        for (Diary d : diaries) {
            this.databaseHelper.addDiary(d.getName(), d.getDay(), d.getNote(), d.getStartTime(), d.getEndTime(),
                    UserSessionManager.getInstance().getCurrentUser().getUserId());
        }
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

