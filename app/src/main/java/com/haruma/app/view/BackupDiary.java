package com.haruma.app.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.haruma.app.R;
import com.haruma.app.service.FileHelper;
import com.haruma.app.service.StorageHelper;
import com.haruma.app.viewmodel.BackupDiaryViewModel;

public class BackupDiary extends AppCompatActivity {

    private BackupDiaryViewModel viewModel;
    private ActivityResultLauncher<Intent> pickFileLauncher;
    private ActivityResultLauncher<Intent> saveFileLauncher;

    private TextView txtPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_diary);
        viewModel = new ViewModelProvider(this).get(BackupDiaryViewModel.class);
        pickFileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri fileUri = data.getData();
                            if (fileUri != null) {
                                viewModel.readJson(this, fileUri);
                            }
                        }
                    }
                });
        txtPermission = findViewById(R.id.txtPermission);
        BackupDiaryViewModel backupDiaryViewModel = new ViewModelProvider(this).get(BackupDiaryViewModel.class);
        backupDiaryViewModel.getPermissionStatus().observe(this, status -> {
            txtPermission.setText(status);
        });
        backupDiaryViewModel.checkPermissions();

        saveFileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri fileUri = data.getData();
                            if (fileUri != null) {
                                viewModel.writeJson(this, fileUri);
                            }
                        }
                    }
                });
        Button btnRequest = findViewById(R.id.btnRequest);
        btnRequest.setOnClickListener(v -> {
            try {
                StorageHelper.requestStoragePermission(this);
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        Button pickFileButton = findViewById(R.id.btnPick);
        Button saveFileButton = findViewById(R.id.btnSave);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow);

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            pickFileButton.setOnClickListener(v -> pickFileLauncher.launch(FileHelper.pickFile()));
            saveFileButton.setOnClickListener(v -> saveFileLauncher.launch(FileHelper.saveFile()));
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
