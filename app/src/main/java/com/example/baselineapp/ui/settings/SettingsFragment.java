package com.example.baselineapp.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.baselineapp.ChangePassword;
import com.example.baselineapp.DebugConsole;
import com.example.baselineapp.Globals;
import com.example.baselineapp.Login2;
import com.example.baselineapp.NotificationSettings;
import com.example.baselineapp.databinding.FragmentSettingsBinding;
import com.example.baselineapp.ProfileActivity;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        //Code starts here
        //-----------------

        binding.idProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsFragment.this.getActivity(), ProfileActivity.class);
            startActivity(intent);
        });

        binding.idChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsFragment.this.getActivity(), ChangePassword.class);
            startActivity(intent);
        });

        binding.idLogout.setOnClickListener(v -> {
            Globals.setLoggedIn(false);
            getActivity().getApplication().stopService(Globals.getNotificationService());
            getActivity().getApplication().stopService(Globals.getTCPServerService());

            Intent intent = new Intent(getActivity(), Login2.class);
            startActivity(intent);
        });

        binding.idNotificationSettingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsFragment.this.getActivity(), NotificationSettings.class);
            startActivity(intent);
        });

        binding.idDebugConsole.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsFragment.this.getActivity(), DebugConsole.class);
            startActivity(intent);
        });



        //-----------------
        View root = binding.getRoot();



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}