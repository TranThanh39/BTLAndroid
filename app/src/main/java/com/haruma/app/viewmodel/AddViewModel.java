package com.haruma.app.viewmodel;

import android.content.Context;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.haruma.app.utility.DatabaseHelper;
import com.haruma.app.model.Callback;
import com.haruma.app.BR;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
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

        // Thiết lập giá trị ngày mặc định là ngày hiện tại
        if (this.date == null || this.date.isEmpty()) {
            this.date = getCurrentDate();
        }
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
                if (!isValidDate(date)) {
                    makeToast("Ngày không hợp lệ. Vui lòng nhập theo định dạng dd/MM/yyyy và đảm bảo ngày hợp lệ.");
                    return;
                }
                if (!isValidTime(startTime) || !isValidTime(endTime)) {
                    makeToast("Thời gian không hợp lệ. Vui lòng nhập theo định dạng HH:mm với giờ từ 0-23 và phút từ 0-59.");
                    return;
                }
                if (!isStartTimeBeforeEndTime(startTime, endTime)) {
                    makeToast("Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc.");
                    return;
                }
                DatabaseHelper databaseHelper = new DatabaseHelper(context);
                databaseHelper.addTimeTable(name, date, note, startTime, endTime, false, 0);
                makeToast("Thêm thời gian biểu thành công");
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

    private boolean isValidDate(String date) {
        String datePattern = "^([0-2][0-9]|3[0-1])/(0[1-9]|1[0-2])/([1-9][0-9]{3})$";
        if (!date.matches(datePattern)) {
            return false;
        }

        String[] parts = date.split("/");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        if (year < 1970) {
            return false;
        }

        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (isLeapYear(year)) {
            daysInMonth[1] = 29; // Tháng 2 có 29 ngày nếu là năm nhuận
        }

        return month >= 1 && month <= 12 && day >= 1 && day <= daysInMonth[month - 1];
    }

    // Kiêm tra xem có phải năm nhuận không
    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    private boolean isValidTime(String time) {
        String timePattern = "^([0-9]|1[0-9]|2[0-3]):([0-5]?[0-9])$";
        return time != null && time.matches(timePattern);
    }

    private void makeToast(String message) {
        Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show();
    }

    private boolean isStartTimeBeforeEndTime(String startTime, String endTime) {
        String[] startParts = startTime.split(":");
        String[] endParts = endTime.split(":");

        int startHour = Integer.parseInt(startParts[0]);
        int startMinute = Integer.parseInt(startParts[1]);
        int endHour = Integer.parseInt(endParts[0]);
        int endMinute = Integer.parseInt(endParts[1]);

        int startTotalMinutes = startHour * 60 + startMinute;
        int endTotalMinutes = endHour * 60 + endMinute;

        return startTotalMinutes < endTotalMinutes;
    }


    // Lấy ngày hiện tại theo định dạng dd/MM/yyyy
    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(Calendar.getInstance().getTime());
    }
}
