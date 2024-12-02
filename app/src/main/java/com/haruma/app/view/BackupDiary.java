package com.haruma.app.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.haruma.app.R;
import com.haruma.app.service.FileHelper;
import com.haruma.app.service.JsonHelper;
import com.haruma.app.service.StorageHelper;
import com.haruma.app.viewmodel.BackupDiaryViewModel;

public class BackupDiary extends AppCompatActivity {

    private BackupDiaryViewModel viewModel;
    private ActivityResultLauncher<Intent> pickFileLauncher;
    private ActivityResultLauncher<Intent> saveFileLauncher;

    private BackupDiaryViewModel backupDiaryViewModel;

    private TextView txtPermission;

    private Button btnRequest;

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
        backupDiaryViewModel = new ViewModelProvider(this).get(BackupDiaryViewModel.class);
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
        btnRequest = findViewById(R.id.btnRequest);
        btnRequest.setOnClickListener(v -> {
            try {
                StorageHelper.requestStoragePermission(this);
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        Button pickFileButton = findViewById(R.id.btnPick);
        Button saveFileButton = findViewById(R.id.btnSave);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            pickFileButton.setOnClickListener(v -> pickFileLauncher.launch(FileHelper.pickFile()));
            saveFileButton.setOnClickListener(v -> saveFileLauncher.launch(FileHelper.saveFile()));
        }
    }
}
