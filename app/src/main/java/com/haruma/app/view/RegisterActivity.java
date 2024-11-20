package com.haruma.app.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.haruma.app.MainActivity;
import com.haruma.app.R;
import com.haruma.app.databinding.ActivityMainBinding;
import com.haruma.app.databinding.ActivityRegisterBinding;
import com.haruma.app.model.Callback;
import com.haruma.app.viewmodel.RegisterViewModel;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ActivityRegisterBinding activityRegisterBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_register);
        Map<String, Callback> callback = new HashMap<>();
        callback.put("onLogin", () -> {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
        });
        callback.put("onRegister", () -> {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
        });
        RegisterViewModel registerViewModel = new RegisterViewModel(this, callback);
        activityRegisterBinding.setRegisterViewModel(registerViewModel);
    }
}