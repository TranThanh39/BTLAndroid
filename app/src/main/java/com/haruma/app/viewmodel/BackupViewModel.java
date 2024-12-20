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

import com.haruma.app.model.Timetable;
import com.haruma.app.utility.DatabaseHelper;
import com.haruma.app.utility.UserSessionManager;
import com.haruma.app.service.JsonHelper;
import com.haruma.app.service.UriHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BackupViewModel extends AndroidViewModel {

    private final DatabaseHelper databaseHelper;

    private final MutableLiveData<String> permissionStatus;

    private final Context context;



    public BackupViewModel(Application application) {
        super(application);
        this.permissionStatus = new MutableLiveData<>();
        this.context = application.getApplicationContext();
        this.databaseHelper = new DatabaseHelper(this.context);
    }

    @SuppressLint("DefaultLocale")
    public void readJson(Context context, Uri fileUri) {
        try {
            String filePath = UriHelper.resolveUri(context, fileUri);
            List<Map<String, Object>> myList = JsonHelper.readJson(filePath);
            this.onChangeTimetable(makeList(myList));
            Toast.makeText(this.context, String.format("Đã thêm %d thời gian biểu vào cơ sở dữ liệu", myList.size()), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this.context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private List<Timetable> makeList(List<Map<String, Object>> rawList) {
        List<Timetable> timetableList = new ArrayList<>();
        for (Map<String, Object> rawEntry : rawList) {
            Timetable timetable = new Timetable();
            timetable.setTimeTableId(((Double) Objects.requireNonNull(rawEntry.get("timeTableId"))).intValue());
            timetable.setDay((String) rawEntry.get("day"));
            timetable.setName((String) rawEntry.get("name"));
            timetable.setNote((String) rawEntry.get("note"));
            timetable.setStartTime((String) rawEntry.get("startTime"));
            timetable.setEndTime((String) rawEntry.get("endTime"));
            timetable.setUserId(((Double) Objects.requireNonNull(rawEntry.get("userId"))).intValue());
            timetableList.add(timetable);
        }
        return timetableList;
    }


    public void writeJson(Context context, Uri fileUri) {
        try {
            JsonHelper.writeJson(UriHelper.resolveUri(context, fileUri), databaseHelper.getAllTimeTable());
            Toast.makeText(this.context, "Sao lưu dữ liệu thành công", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this.context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void onChangeTimetable(List<Timetable> timetableList) {
        this.databaseHelper.clearTimeTable();
        for (Timetable t : timetableList) {
            this.databaseHelper.addTimeTable(t.getName(), t.getDay(), t.getNote(), t.getStartTime(), t.getEndTime(), t.getStatus(),
                    UserSessionManager.getInstance().getCurrentUser().getUserId());
        }
    }

    public LiveData<String> getPermissionStatus() {
        return permissionStatus;
    }

    public void checkPermissions() {
        try {

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
        } catch (Exception e) {
            Toast.makeText(this.context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

