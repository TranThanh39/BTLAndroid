package com.haruma.app.viewmodel;

import android.content.Context;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.haruma.app.dto.DatabaseHelper;
import com.haruma.app.dto.UserSessionManager;
import com.haruma.app.model.Callback;
import com.haruma.app.BR;

import java.util.Objects;
import java.util.Map;

public class AddViewModel extends BaseObservable {

    private String name;
    private String date;
    private String note;
    private String startTime;
    private String endTime;

    private final Context context;
    private final Map<String, Callback> callback;

    public AddViewModel(Context context, Map<String, Callback> callback) {
        this.context = context;
        this.callback = callback;
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

    public void onAdd() {
        try {
            if (isValidInput()) {
                DatabaseHelper databaseHelper = new DatabaseHelper(context);
                int userId = UserSessionManager.getInstance().getCurrentUser().getUserId();
                databaseHelper.addDiary(name, date, note, startTime, endTime, false, 0);
                makeToast("Thêm hoạt động thành công");
                Objects.requireNonNull(this.callback.get("onAdd")).run();

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
