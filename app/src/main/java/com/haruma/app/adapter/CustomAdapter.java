package com.haruma.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;

import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.haruma.app.R;
import com.haruma.app.dto.DatabaseHelper;
import com.haruma.app.model.ChangeCallback;
import com.haruma.app.model.Diary;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CustomAdapter extends ArrayAdapter<Diary> {
    private final Context context;
    private final int mResource;
    private final Map<String, ChangeCallback> callback;

    public CustomAdapter(@NonNull Context context, int resource, @NonNull List<Diary> objects, Map<String, ChangeCallback> callback) {
        super(context, resource, objects);
        this.context = context;
        this.mResource = resource;
        this.callback = callback;
    }

    public void setList(List<Diary> list) {
        this.clear();
        this.addAll(list);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(mResource, parent, false);
        }
        Diary diary = getItem(position);
        TextView tenhd = convertView.findViewById(R.id.tvTenhd);
        TextView ngay = convertView.findViewById(R.id.tvNgay);
        TextView ngaybd = convertView.findViewById(R.id.tvNgaybd);
        TextView ngaykt = convertView.findViewById(R.id.tvNgaykt);
        ImageButton iconMenu = convertView.findViewById(R.id.iconMenu);
        CheckBox chkStatus = convertView.findViewById(R.id.chkStatus);
        iconMenu.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), iconMenu);
            popupMenu.getMenuInflater().inflate(R.menu.option_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.btnSua) {
                    assert diary != null;
                    Objects.requireNonNull(this.callback.get("onChange")).run(diary.getDiaryId());
                } else if (item.getItemId() == R.id.btnXoa) {
                    assert diary != null;
                    Objects.requireNonNull(this.callback.get("onDelete")).run(diary.getDiaryId());
                } else if (item.getItemId() == R.id.btnXemChiTiet) {
                    assert diary != null;
                    Objects.requireNonNull(this.callback.get("onDetail")).run(diary.getDiaryId());
                }
                return true;
            });
            popupMenu.show();
        });
        if (diary != null) {
            tenhd.setText(diary.getName());
            ngay.setText(diary.getDay());
            ngaybd.setText(String.format("%sh", diary.getStartTime()));
            ngaykt.setText(String.format("%sh", diary.getEndTime()));
            if (diary.getStatus()) {
                chkStatus.setChecked(true);
            }
        }
        chkStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            try {
                DatabaseHelper db = new DatabaseHelper(this.context);
                assert diary != null;
                db.updateDiary(diary.getDiaryId(), diary.getName(), diary.getDay(), diary.getNote(), diary.getStartTime(), diary.getEndTime(), isChecked);
                Toast.makeText(this.context, String.format("Đã cập nhật trạng thái của nhật ký %d", diary.getDiaryId()), Toast.LENGTH_LONG).show();
                this.setList(db.getAllDiaries());
                this.notifyDataSetChanged();
            } catch (Exception ex) {
                Toast.makeText(this.context, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        return convertView;
    }
}

