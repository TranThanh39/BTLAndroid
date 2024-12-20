package com.haruma.app.viewmodel;

import android.content.Context;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.haruma.app.BR;
import com.haruma.app.utility.DatabaseHelper;
import com.haruma.app.utility.UserSessionManager;
import com.haruma.app.model.Callback;
import com.haruma.app.model.User;
import com.haruma.app.service.ExceptionHelper;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class LoginViewModel extends BaseObservable {

    private String email;

    private String password;

    private final Context context;

    private final Map<String, Callback> callback;

    private DatabaseHelper databaseHelper;

    public LoginViewModel(Context context, Map<String, Callback> callback) {
        this.context = context;
        this.callback = callback;
        this.databaseHelper = new DatabaseHelper(this.context);
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    private void makeToast(String message) {
        Toast.makeText(this.context, message, Toast.LENGTH_LONG).show();
    }

    private boolean validateEmailAndPassword(String email, String password) throws Exception {
        if (password == null || password.isEmpty()) {
            throw new Exception("Mật khẩu không được rỗng");
        }
        if (email == null || email.isEmpty()) {
            throw new Exception("Email không được rỗng");
        }
        if (password.length() < 6) {
            throw new Exception("Mật khẩu không được ít hơn 6 kí tự");
        }
        if (!Pattern.compile("^.+@.+\\..+$").matcher(email).matches()) {
            throw new Exception("Email không hợp lệ");
        }
        return true;
    }

    public void onLogin() {
        try {
            User user = new User(this.getEmail(), this.getPassword());
            if (this.validateEmailAndPassword(user.getEmail(), user.getPassword())) {
                User currentUser = this.databaseHelper.loginUser(user.getEmail(), user.getPassword());
                ExceptionHelper.assertTest(currentUser != null, "Không tìm thấy tài khoản");
                UserSessionManager.getInstance().saveUser(currentUser);
                makeToast("Đăng nhập thành công");
                Objects.requireNonNull(this.callback.get("onLogin")).run();
            }
            else {
                makeToast("Đăng nhập thất bại");
            }
        } catch (Exception e) {
            makeToast("Lỗi xảy ra: " + e.getMessage());
        }
    }

    public void onRegister() {
        try {
            Objects.requireNonNull(this.callback.get("onRegister")).run();
        } catch (Exception e) {
            makeToast("Lỗi xảy ra: " + e.getMessage());
        }
    }

}
