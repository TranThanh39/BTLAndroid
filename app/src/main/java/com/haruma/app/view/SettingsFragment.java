package com.haruma.app.view;

import static com.haruma.app.BR.SettingsViewModel;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haruma.app.R;
import com.haruma.app.databinding.FragmentSettingsBinding;
import com.haruma.app.viewmodel.SettingsViewModel;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding fragmentSettingsBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentSettingsBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_settings, container, false);

        fragmentSettingsBinding.setSettingsViewModel(new SettingsViewModel(requireContext()));
        fragmentSettingsBinding.setLifecycleOwner(this);

        return fragmentSettingsBinding.getRoot();
    }
}
