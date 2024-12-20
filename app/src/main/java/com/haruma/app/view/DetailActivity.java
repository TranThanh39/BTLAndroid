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
import com.haruma.app.databinding.ActivityDetailBinding;
import com.haruma.app.viewmodel.DetailViewModel;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ActivityDetailBinding mainBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_detail);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        // Hàm lambda được nhúng vào ViewModel để vào giao diện nhằm kết thúc Activity được khởi động trước đó
        DetailViewModel detailActivityViewModel = new DetailViewModel(this.getApplicationContext(), id, () -> {
            finish();
        });
        mainBinding.setDetailViewModel(detailActivityViewModel);
        mainBinding.setLifecycleOwner(this);
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
            SpannableString title = new SpannableString("Chi tiết thời gian biểu");
            title.setSpan(new ForegroundColorSpan(Color.WHITE), 0, title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            actionBar.setTitle(title);
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