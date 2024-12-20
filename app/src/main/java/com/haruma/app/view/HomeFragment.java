package com.haruma.app.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.haruma.app.R;
import com.haruma.app.adapter.CustomAdapter;
import com.haruma.app.utility.AdapterSessionManager;
import com.haruma.app.utility.DatabaseHelper;
import com.haruma.app.model.ChangeCallback;
import com.haruma.app.model.Timetable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ListView listView = rootView.findViewById(R.id.myListView);
        DatabaseHelper db = new DatabaseHelper(getContext());
        List<Timetable> myList = db.getAllTimeTable();
        Map<String, ChangeCallback> myCallback = new HashMap<>();
        myCallback.put("onChange", (id) -> {
            Intent intent = new Intent(rootView.getContext(), EditActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });
        myCallback.put("onDelete", (id) -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa thời gian biểu hiện tại này không?")
                    .setPositiveButton("Xác nhận", (dialog, which) -> {
                        try {
                            db.deleteTimetable(id);
                            List<Timetable> updatedList = db.getAllTimeTable();
                            CustomAdapter adapter = AdapterSessionManager.getInstance().getCustomAdapter();
                            if (adapter != null) {
                                adapter.setList(updatedList);
                            }
                            Toast.makeText(getContext(), "Xóa thành công!", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Lỗi khi xóa: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Hủy", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        });
        myCallback.put("onDetail", (id) -> {
            Intent intent = new Intent(rootView.getContext(), DetailActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });

        CustomAdapter adapter = new CustomAdapter(rootView.getContext(), R.layout.timetable_list_tile, myList, myCallback);
        listView.setAdapter(adapter);
        AdapterSessionManager.getInstance().setCustomAdapter(adapter);
        FloatingActionButton fab = rootView.findViewById(R.id.fab);

        //Chuyển hướng màn hình tới trang thêm khi nhấn nút "+"
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), AddActivity.class);
            startActivity(intent);
        });
        return rootView;
    }
}

