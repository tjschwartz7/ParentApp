package com.example.baselineapp.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.baselineapp.Login2;
import com.example.baselineapp.MainActivity;
import com.example.baselineapp.R;
import com.example.baselineapp.databinding.FragmentSettingsBinding;
import com.example.baselineapp.ui.ProfileActivity;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        binding.idProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsFragment.this.getActivity(), ProfileActivity.class);
            startActivity(intent);
        });

        binding.idLogout.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsFragment.this.getActivity(), Login2.class);
            startActivity(intent);
        });

        View root = binding.getRoot();



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}