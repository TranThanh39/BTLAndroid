package com.haruma.app.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import com.haruma.app.R;
import com.haruma.app.databinding.ActivityAddBinding;
import com.haruma.app.databinding.ActivityBackupDiaryBinding;
import com.haruma.app.service.FileHelper;
import com.haruma.app.service.StorageHelper;
import com.haruma.app.viewmodel.AddViewModel;
import com.haruma.app.viewmodel.BackupDiaryViewModel;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class BackupDiary extends AppCompatActivity {
    private ActivityResultLauncher<Intent> pickFileLauncher;
    private ActivityResultLauncher<Intent> saveFileLauncher;
    private ActivityResultLauncher<Intent> storagePermissionLauncher;

    private TextView txtPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_diary);
        ActivityBackupDiaryBinding mainBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_backup_diary);
        BackupDiaryViewModel viewModel = new BackupDiaryViewModel(getApplication());
        mainBinding.setBackupDiaryViewModel(viewModel);
        pickFileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            new AlertDialog.Builder(this)
                                    .setMessage("Nếu bạn xác nhận, toàn bộ nhật ký hiện tại sẽ bị xóa sổ và tạo lại nhật kí mới")
                                    .setTitle("Bạn có muốn sửa toàn bộ nhật ký?")
                                    .setPositiveButton("Có", (dialog, which) -> {
                                        Uri fileUri = data.getData();
                                        if (fileUri != null) {
                                            viewModel.readJson(this, fileUri);
                                        }
                                        dialog.dismiss();
                                    })
                                    .setNegativeButton("Không", (dialog, which) -> {
                                        dialog.dismiss();
                                    })
                                    .show();
                        }
                    }
                }
        );
        txtPermission = findViewById(R.id.txtPermission);
        viewModel.getPermissionStatus().observe(this, status -> {
            txtPermission.setText(status);
        });
        viewModel.checkPermissions();

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
                StorageHelper.requestStoragePermission(this, storagePermissionLauncher);
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        storagePermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    try {
                        if (StorageHelper.checkStoragePermission(this)) {
                            Toast.makeText(this, "Đã cấp quyền", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Quyền đã bị từ chối", Toast.LENGTH_SHORT).show();
                        }
                        viewModel.checkPermissions();
                    } catch (Exception e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        Button pickFileButton = findViewById(R.id.btnPick);
        Button saveFileButton = findViewById(R.id.btnSave);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            Drawable backArrow = ContextCompat.getDrawable(this, R.drawable.ic_back_arrow);
            if (backArrow != null) {
                backArrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                actionBar.setHomeAsUpIndicator(backArrow);
            }
            SpannableString title = new SpannableString("Sao lưu dữ liệu");
            title.setSpan(new ForegroundColorSpan(Color.WHITE), 0, title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            actionBar.setTitle(title);
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
