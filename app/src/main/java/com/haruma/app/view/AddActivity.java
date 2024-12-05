package com.haruma.app.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.haruma.app.MainActivity;
import com.haruma.app.R;
import com.haruma.app.databinding.ActivityAddBinding;
import com.haruma.app.databinding.ActivityMainBinding;
import com.haruma.app.model.Callback;
import com.haruma.app.viewmodel.AddViewModel;
import com.haruma.app.viewmodel.LoginViewModel;

import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ActivityAddBinding mainBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_add);
        Map<String, Callback> callback = new HashMap<>();

        callback.put("onAdd", () -> {
            finish();
        });
        callback.put("onCancel", () -> {
            finish();
        });
        AddViewModel addViewModel = new AddViewModel(this, callback);
        mainBinding.setAddViewModel(addViewModel);
    }
}
