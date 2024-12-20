package com.haruma.app.viewmodel;

import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.haruma.app.utility.DatabaseHelper;
import com.haruma.app.model.Callback;
import com.haruma.app.model.Timetable;

public class DetailViewModel extends BaseObservable {

    private final Context context;

    private final Timetable timetable;

    private final DatabaseHelper db;

    private Callback callback;

    public DetailViewModel(Context context, int id, Callback callback) {
        this.context = context;
        this.db = new DatabaseHelper(this.context);
        this.timetable = db.findTimeTableById(id);
        this.callback = callback;
    }

    public void onBack() {
        callback.run();
    }

    @Bindable
    public Timetable getTimetable() {
        return this.timetable;
    }

}
