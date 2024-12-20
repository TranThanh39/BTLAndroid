package com.haruma.app.view;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.os.Bundle;
import android.graphics.PorterDuff;
import android.widget.Button;
import android.widget.EditText;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.haruma.app.R;
import com.haruma.app.databinding.ActivityAddBinding;
import com.haruma.app.utility.AdapterSessionManager;
import com.haruma.app.utility.DatabaseHelper;
import com.haruma.app.model.Callback;
import com.haruma.app.model.Timetable;
import com.haruma.app.viewmodel.AddViewModel;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
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
            DatabaseHelper db = new DatabaseHelper(this);
            List<Timetable> myList = db.getAllTimeTable();
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
            SpannableString title = new SpannableString("Thêm thời gian biểu");
            title.setSpan(new ForegroundColorSpan(Color.WHITE), 0, title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            actionBar.setTitle(title);
        }
        callback.put("onCancel", () -> {
            finish();
        });
        Button btnPickDate = findViewById(R.id.btnPickDate);
        EditText edtDate = findViewById(R.id.edtDate);
        AddViewModel addViewModel = new AddViewModel(this, callback);
        btnPickDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String selectedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                        addViewModel.setDate(selectedDate);
                        edtDate.setText(selectedDate);
                    },
                    year, month, day
            );

            datePickerDialog.show();
        });
        mainBinding.setAddViewModel(addViewModel);
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
