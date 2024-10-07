package com.example.baselineapp.ui.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

            builder.setTitle("Logout Confirmation");
            builder.setMessage("Are you sure you want to log out?");

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            // Add the buttons.
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Globals.setLoggedIn(false); //Notify any listeners that we're logged out now

                    //Stop our services on logout
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            //Shut down all of our services
                            try {
                                getActivity().getApplication().stopService(Globals.getNotificationService());
                                getActivity().getApplication().stopService(Globals.getTCPServerService());
                                getActivity().getApplication().stopService(Globals.getUdpServerService());
                            }catch(Exception ex){
                                Log.e("Settings", ex.getMessage());
                            }


                        }
                    });

                    dialog.cancel();
                    Intent intent = new Intent(SettingsFragment.this.getActivity(), Login2.class);
                    startActivity(intent);
                }
            });

            // Create the AlertDialog.
            AlertDialog dialog = builder.create();
            dialog.show();
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