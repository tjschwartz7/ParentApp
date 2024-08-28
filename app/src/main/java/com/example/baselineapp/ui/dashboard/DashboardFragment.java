package com.example.baselineapp.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

        double dbl_bloodOxValue = 95;
        double dbl_tempValue = 102.7;
        double dbl_pulseValue = 68;

        int color_healthyGreen = getResources().getColor(R.color.healthy_green, getActivity().getTheme());
        int color_cautionYellow = getResources().getColor(R.color.caution_yellow, getActivity().getTheme());
        int color_warningRed = getResources().getColor(R.color.warning_red, getActivity().getTheme());
        int color_black = getResources().getColor(R.color.black, requireContext().getTheme());
        int color_white = getResources().getColor(R.color.white, requireContext().getTheme());

        String str_bloodOxTextBoxValue = getString(R.string.str_dataValue, Double.toString(dbl_bloodOxValue), "%");
        String str_tempTextBoxValue = getString(R.string.str_dataValue, Double.toString(dbl_tempValue), "F");
        String str_pulseTextBoxValue = getString(R.string.str_dataValue, Double.toString(dbl_pulseValue), "bpm");

        if(dbl_bloodOxValue < 88)
        {
            binding.idBloodOxTextBox.setBackgroundColor(color_warningRed);
            binding.idBloodOxTextBox.setTextColor(color_white);
        }
        else if(dbl_bloodOxValue < 90)
        {
            binding.idBloodOxTextBox.setBackgroundColor(color_cautionYellow);
            binding.idBloodOxTextBox.setTextColor(color_black);
        }
        else
        {
            binding.idBloodOxTextBox.setBackgroundColor(color_healthyGreen);
            binding.idBloodOxTextBox.setTextColor(color_black);
        }

        if(dbl_pulseValue < 65 || dbl_pulseValue > 195)
        {
            binding.idPulseTextBox.setBackgroundColor(color_warningRed);
            binding.idPulseTextBox.setTextColor(color_white);
        }
        else if(dbl_pulseValue < 70 || dbl_pulseValue > 190)
        {
            binding.idPulseTextBox.setBackgroundColor(color_cautionYellow);
            binding.idPulseTextBox.setTextColor(color_black);
        }
        else
        {
            binding.idPulseTextBox.setBackgroundColor(color_healthyGreen);
            binding.idPulseTextBox.setTextColor(color_black);
        }

        if(dbl_tempValue < 95 || dbl_tempValue > 101)
        {
            binding.idTempTextBox.setBackgroundColor(color_warningRed);
            binding.idTempTextBox.setTextColor(color_white);
        }
        else if(dbl_tempValue < 96.8 || dbl_tempValue > 99.5)
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