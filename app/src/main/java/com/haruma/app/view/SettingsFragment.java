package com.haruma.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.haruma.app.R;
import com.haruma.app.viewmodel.SettingsViewModel;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    private Button backupButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        backupButton = rootView.findViewById(R.id.btnBackup);
        backupButton.setOnClickListener(v -> {
            Intent intent = new Intent(rootView.getContext(), BackupDiary.class);
            startActivity(intent);
        });
        return rootView;
    }
}
