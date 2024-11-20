package com.haruma.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.haruma.app.databinding.ActivityMainBinding;
import com.haruma.app.model.Callback;
import com.haruma.app.view.RegisterActivity;
import com.haruma.app.view.RootActivity;
import com.haruma.app.viewmodel.LoginViewModel;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ActivityMainBinding mainBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_main);
        Map<String, Callback> callback = new HashMap<>();
        callback.put("onLogin", () -> {
            Intent intent = new Intent(MainActivity.this, RootActivity.class);
            startActivity(intent);
        });
        callback.put("onRegister", () -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        LoginViewModel loginViewModel = new LoginViewModel(this, callback);
        mainBinding.setLoginViewModel(loginViewModel);
    }
}