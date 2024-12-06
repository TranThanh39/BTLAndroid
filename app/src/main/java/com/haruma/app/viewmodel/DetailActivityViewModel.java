package com.haruma.app.viewmodel;

import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.haruma.app.dto.DatabaseHelper;
import com.haruma.app.model.Callback;
import com.haruma.app.model.Diary;

public class DetailActivityViewModel extends BaseObservable {

    private final Context context;

    private final Diary diary;

    private final DatabaseHelper db;

    private Callback callback;

    public DetailActivityViewModel(Context context, int id, Callback callback) {
        this.context = context;
        this.db = new DatabaseHelper(this.context);
        this.diary = db.findDiaryById(id);
        this.callback = callback;
    }

    public void onBack() {
        callback.run();
    }

    @Bindable
    public Diary getDiary() {
        return this.diary;
    }

}
