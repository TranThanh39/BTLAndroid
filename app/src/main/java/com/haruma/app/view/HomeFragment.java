package com.haruma.app.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.haruma.app.R;
import com.haruma.app.adapter.CustomAdapter;
import com.haruma.app.dto.AdapterSessionManager;
import com.haruma.app.dto.DatabaseHelper;
import com.haruma.app.model.ChangeCallback;
import com.haruma.app.model.Diary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ListView listView = rootView.findViewById(R.id.myListView);
        DatabaseHelper db = new DatabaseHelper(rootView.getContext());
        List<Diary> myList = db.getAllDiaries();
        Map<String, ChangeCallback> myCallback = new HashMap<>();
        myCallback.put("onChange", (id) -> {

        });
        myCallback.put("onDelete", (id) -> {

        });
        myCallback.put("onDetail", (id) -> {

        });
        CustomAdapter adapter = new CustomAdapter(rootView.getContext(), R.layout.diary_list_tile, myList, myCallback);
        listView.setAdapter(adapter);
        AdapterSessionManager.getInstance().setCustomAdapter(adapter);
        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), AddActivity.class);
            startActivity(intent);
        });
        return rootView;
    }
}

