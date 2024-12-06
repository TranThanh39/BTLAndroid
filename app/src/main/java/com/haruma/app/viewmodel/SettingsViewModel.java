package com.haruma.app.viewmodel;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.lifecycle.AndroidViewModel;

import com.haruma.app.BR; // Import BR for notifyPropertyChanged
import com.haruma.app.MainActivity;
import com.haruma.app.dto.UserSessionManager;
import com.haruma.app.model.Callback;
import com.haruma.app.model.User;
import com.haruma.app.view.BackupDiary;
import com.haruma.app.view.HelpActivity;

import java.util.Map;

public class SettingsViewModel extends AndroidViewModel {
    private final ObservableData observableData = new ObservableData();


    public SettingsViewModel(@NonNull Application application) {
        super(application);
    }

    public ObservableData getObservableData() {
        return observableData;
    }

    public static class ObservableData extends BaseObservable {
        private String fullName;
        private String className;
        private String userId;
        private String status;
        private String phoneNumber;
        private String email;

        public ObservableData() {
            User user = UserSessionManager.getInstance().getCurrentUser();
            setFullName(user.getUserDetail().getFullName());
            setClassName(user.getUserDetail().getClassName());
            setUserId(String.valueOf(user.getUserDetail().getUserId()));
            setStatus("Đang học");
            setPhoneNumber(user.getUserDetail().getPhoneNumber());
            setEmail(user.getEmail());
        }

        @Bindable
        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
            notifyPropertyChanged(BR.fullName);
        }

        @Bindable
        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
            notifyPropertyChanged(BR.className);
        }

        @Bindable
        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
            notifyPropertyChanged(BR.userId);
        }

        @Bindable
        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
            notifyPropertyChanged(BR.status);
        }

        @Bindable
        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            notifyPropertyChanged(BR.phoneNumber);
        }

        @Bindable
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
            notifyPropertyChanged(BR.email);
        }
    }

    public void onClickDangXuat() {
        Intent intent = new Intent(getApplication(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(intent);
        Toast.makeText(getApplication().getApplicationContext(), "Đăng xuất thành công", Toast.LENGTH_LONG).show();
    }

    public void onClickTroGiup() {
        Intent intent = new Intent(getApplication(), HelpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(intent);
    }

    public void onClickBackup() {
        Intent intent = new Intent(getApplication(), BackupDiary.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(intent);
    }
}

