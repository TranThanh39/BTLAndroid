package com.haruma.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.haruma.app.R;
import com.haruma.app.model.Timetable;

import java.util.List;

public class DashboardAdapter extends ArrayAdapter<Timetable> {
    private final Context context;
    private final int mResource;

    public DashboardAdapter(@NonNull Context context, int resource, @NonNull List<Timetable> objects) {
        super(context, resource, objects);
        this.context = context;
        this.mResource = resource;
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(mResource, parent, false);
        }
        Timetable timetable = getItem(position);
        TextView tenhd = convertView.findViewById(R.id.tvTenhd);
        TextView ngay = convertView.findViewById(R.id.tvNgay);
        TextView ngaybd = convertView.findViewById(R.id.tvNgaybd);
        TextView ngaykt = convertView.findViewById(R.id.tvNgaykt);
        if (timetable != null) {
            tenhd.setText(timetable.getName());
            ngay.setText(timetable.getDay());
            ngaybd.setText(String.format("%sh", timetable.getStartTime()));
            ngaykt.setText(String.format("%sh", timetable.getEndTime()));
        }
        return convertView;
    }
}

