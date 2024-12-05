package com.haruma.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;

import android.widget.ArrayAdapter;

import com.haruma.app.R;
import com.haruma.app.model.ChangeCallback;
import com.haruma.app.model.Diary;

import java.util.List;
import java.util.Map;

public class CustomAdapter extends ArrayAdapter<Diary> {
    private Context mContext;
    private int mResource;
    private Map<String, ChangeCallback> callback;

    public CustomAdapter(@NonNull Context context, int resource, @NonNull List<Diary> objects, Map<String, ChangeCallback> callback) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        this.callback = callback;
    }

    public void setList(List<Diary> list) {
        this.clear();
        this.addAll(list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mResource, parent, false);
        }
        Diary diary = getItem(position);
        TextView mahd = convertView.findViewById(R.id.tvMahd);
        TextView tenhd = convertView.findViewById(R.id.tvTenhd);
        TextView ngay = convertView.findViewById(R.id.tvNgay);
        TextView ngaybd = convertView.findViewById(R.id.tvNgaybd);
        TextView ngaykt = convertView.findViewById(R.id.tvNgaykt);
        ImageButton iconMenu = convertView.findViewById(R.id.iconMenu);
        iconMenu.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), iconMenu);
            popupMenu.getMenuInflater().inflate(R.menu.option_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.btnSua) {
                    this.callback.get("onChange").run(diary.getDiaryId());
                } else if (item.getItemId() == R.id.btnXoa) {
                    this.callback.get("onDelete").run(diary.getDiaryId());
                } else if (item.getItemId() == R.id.btnXemChiTiet) {
                    this.callback.get("onDetail").run(diary.getDiaryId());
                }
                return true;
            });
            popupMenu.show();
        });
        if (diary != null) {
            mahd.setText(String.valueOf(diary.getDiaryId()));
            tenhd.setText(diary.getName());
            ngay.setText(diary.getDay());
            ngaybd.setText(diary.getStartTime());
            ngaykt.setText(diary.getEndTime());
        }

        return convertView;
    }
}

