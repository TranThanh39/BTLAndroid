package com.haruma.app.viewmodel;

import android.content.Context;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.haruma.app.BR;
import com.haruma.app.dto.DatabaseHelper;
import com.haruma.app.dto.UserSessionManager;
import com.haruma.app.model.Callback;
import com.haruma.app.model.Diary;

import java.util.Map;
import java.util.Objects;

public class EditViewModel extends BaseObservable {

    private String name;
    private String date;
    private String note;
    private String startTime;
    private String endTime;

    private final Context context;

    private Diary diary;

    private DatabaseHelper db;

    private final Map<String, Callback> callback;

    public EditViewModel(Context context, Map<String, Callback> callback, int id) {
        this.context = context;
        this.callback = callback;
        this.db = new DatabaseHelper(this.context);
        this.diary = db.findDiaryById(id);
        setValue();
    }

    private void setValue() {
        setName(diary.getName());
        setNote(diary.getNote());
        setDate(diary.getDay());
        setEndTime(diary.getEndTime());
        setStartTime(diary.getStartTime());
    }

    @Bindable
    public String getId() {
        return String.valueOf(this.diary.getDiaryId());
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        notifyPropertyChanged(BR.date);
    }

    @Bindable
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
        notifyPropertyChanged(BR.note);
    }

    @Bindable
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
        notifyPropertyChanged(BR.startTime);
    }

    @Bindable
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
        notifyPropertyChanged(BR.endTime);
    }

    public void onEdit() {
        try {
            if (isValidInput()) {
                db.updateDiary(diary.getDiaryId(), name, date, note, startTime, endTime, diary.getStatus());
                makeToast("Sửa hoạt động thành công");
                Objects.requireNonNull(this.callback.get("onEdit")).run();

            } else {
                makeToast("Vui lòng điền đầy đủ thông tin.");
            }
        } catch (Exception e) {
            makeToast("Lỗi xảy ra: " + e.getMessage());
        }
    }


    public void onCancel() {
        Objects.requireNonNull(this.callback.get("onCancel")).run();
    }

    private boolean isValidInput() {
        return name != null && !name.isEmpty() &&
                date != null && !date.isEmpty() &&
                startTime != null && !startTime.isEmpty() &&
                endTime != null && !endTime.isEmpty();
    }

    private void makeToast(String message) {
        Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show();
    }
}
