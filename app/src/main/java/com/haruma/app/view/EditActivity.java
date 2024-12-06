package com.haruma.app.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.haruma.app.R;
import com.haruma.app.databinding.ActivityEditBinding;
import com.haruma.app.dto.AdapterSessionManager;
import com.haruma.app.dto.DatabaseHelper;
import com.haruma.app.model.Callback;
import com.haruma.app.model.Diary;
import com.haruma.app.viewmodel.EditViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ActivityEditBinding mainBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_edit);
        Map<String, Callback> callback = new HashMap<>();
        callback.put("onEdit", () -> {
            DatabaseHelper db = new DatabaseHelper(this);
            List<Diary> myList = db.getAllDiaries();
            AdapterSessionManager.getInstance().getCustomAdapter().setList(myList);
            AdapterSessionManager.getInstance().getCustomAdapter().notifyDataSetChanged();
            finish();
        });
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
            SpannableString title = new SpannableString("Sửa hoạt động");
            title.setSpan(new ForegroundColorSpan(Color.WHITE), 0, title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            actionBar.setTitle(title);
        }
        callback.put("onCancel", () -> {
            finish();
        });
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        EditViewModel editViewModel = new EditViewModel(this, callback, id);
        mainBinding.setEditViewModel(editViewModel);
        mainBinding.setLifecycleOwner(this);
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
