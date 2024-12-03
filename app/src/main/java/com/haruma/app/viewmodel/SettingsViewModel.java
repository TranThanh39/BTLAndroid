package com.haruma.app.viewmodel;

import android.content.Context;
import android.content.Intent;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.haruma.app.MainActivity;

public class SettingsViewModel extends BaseObservable {
    private final Context context;
    private String fullName;
    private String className;
    private String userId;
    private String status;
    private String phoneNumber;
    private String email;

    @Bindable
    public String getFullName() {
        return fullName;
    }

    @Bindable
    public String getClassName() {
        return className;
    }

    @Bindable
    public String getUserId() {
        return userId;
    }

    @Bindable
    public String getStatus() {
        return status;
    }

    @Bindable
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public SettingsViewModel(Context context){
        this.context = context;
    }

    public void onClickDangXuat(){
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear stack để ngăn quay lại bằng nút Back
        context.startActivity(intent);
    }
}
