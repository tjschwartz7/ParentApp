package com.example.baselineapp.ui.dashboard;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.baselineapp.R;
import com.example.baselineapp.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);

        //Code starts here
        //-----------------


        //These are magical numbers for testing
        double dbl_bloodOxValue = 95;
        double dbl_tempValue = 102.7;
        double dbl_pulseValue = 68;

        //These are currently magical numbers from the fairy forest
        //They'll be coming from a saved text file later
        double dbl_bloodOxLowWarningThreshold = 88;
        double dbl_bloodOxLowCautionThreshold = 90;

        double dbl_pulseLowWarningThreshold = 65;
        double dbl_pulseLowCautionThreshold = 70;
        double dbl_pulseHighWarningThreshold = 195;
        double dbl_pulseHighCautionThreshold = 190;

        double dbl_tempLowWarningThreshold = 95;
        double dbl_tempLowCautionThreshold = 101;
        double dbl_tempHighWarningThreshold = 96.8;
        double dbl_tempHighCautionThreshold = 99.5;

        int color_healthyGreen = getResources().getColor(R.color.healthy_green, getActivity().getTheme());
        int color_cautionYellow = getResources().getColor(R.color.caution_yellow, getActivity().getTheme());
        int color_warningRed = getResources().getColor(R.color.warning_red, getActivity().getTheme());
        int color_black = getResources().getColor(R.color.black, requireContext().getTheme());
        int color_white = getResources().getColor(R.color.white, requireContext().getTheme());

        String str_bloodOxTextBoxValue = getString(R.string.str_dataValue, Double.toString(dbl_bloodOxValue), "%");
        String str_tempTextBoxValue = getString(R.string.str_dataValue, Double.toString(dbl_tempValue), "F");
        String str_pulseTextBoxValue = getString(R.string.str_dataValue, Double.toString(dbl_pulseValue), "bpm");

        if(dbl_bloodOxValue < dbl_bloodOxLowWarningThreshold)
        {
            binding.idBloodOxTextBox.setBackgroundColor(color_warningRed);
            binding.idBloodOxTextBox.setTextColor(color_white);
        }
        else if(dbl_bloodOxValue < dbl_bloodOxLowCautionThreshold)
        {
            binding.idBloodOxTextBox.setBackgroundColor(color_cautionYellow);
            binding.idBloodOxTextBox.setTextColor(color_black);
        }
        else
        {
            binding.idBloodOxTextBox.setBackgroundColor(color_healthyGreen);
            binding.idBloodOxTextBox.setTextColor(color_black);
        }

        if(dbl_pulseValue < dbl_pulseLowWarningThreshold || dbl_pulseValue > dbl_pulseHighWarningThreshold)
        {
            binding.idPulseTextBox.setBackgroundColor(color_warningRed);
            binding.idPulseTextBox.setTextColor(color_white);
        }
        else if(dbl_pulseValue < dbl_pulseLowCautionThreshold || dbl_pulseValue > dbl_pulseHighCautionThreshold)
        {
            binding.idPulseTextBox.setBackgroundColor(color_cautionYellow);
            binding.idPulseTextBox.setTextColor(color_black);
        }
        else
        {
            binding.idPulseTextBox.setBackgroundColor(color_healthyGreen);
            binding.idPulseTextBox.setTextColor(color_black);
        }

        if(dbl_tempValue < dbl_tempLowWarningThreshold || dbl_tempValue > dbl_tempHighWarningThreshold)
        {
            binding.idTempTextBox.setBackgroundColor(color_warningRed);
            binding.idTempTextBox.setTextColor(color_white);
        }
        else if(dbl_tempValue < dbl_tempLowCautionThreshold || dbl_tempValue > dbl_tempHighCautionThreshold)
        {
            binding.idTempTextBox.setBackgroundColor(color_cautionYellow);
            binding.idTempTextBox.setTextColor(color_black);
        }
        else
        {
            binding.idTempTextBox.setBackgroundColor(color_healthyGreen);
            binding.idTempTextBox.setTextColor(color_black);
        }

        binding.idBloodOxTextBox.setText(str_bloodOxTextBoxValue);
        binding.idPulseTextBox.setText(str_pulseTextBoxValue);
        binding.idTempTextBox.setText(str_tempTextBoxValue);

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