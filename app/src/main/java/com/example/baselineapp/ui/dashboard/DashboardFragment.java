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

import com.example.baselineapp.Globals;
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
        double dbl_bloodOxValue = ((Globals) getActivity().getApplication()).getBloodOxVal();
        double dbl_tempValue = ((Globals) getActivity().getApplication()).getTempVal();
        double dbl_pulseValue = ((Globals) getActivity().getApplication()).getPulseVal();

        //Get all of our data from the Globals class where its maintained
        double dbl_bloodOxLowWarningThreshold = ((Globals) getActivity().getApplication()).getBloodOxLowWarningThreshold();
        double dbl_bloodOxLowCautionThreshold = ((Globals) getActivity().getApplication()).getBloodOxLowCautionThreshold();

        double dbl_pulseLowWarningThreshold = ((Globals) getActivity().getApplication()).getPulseLowWarningThreshold();
        double dbl_pulseLowCautionThreshold = ((Globals) getActivity().getApplication()).getPulseLowCautionThreshold();
        double dbl_pulseHighWarningThreshold = ((Globals) getActivity().getApplication()).getPulseHighWarningThreshold();
        double dbl_pulseHighCautionThreshold = ((Globals) getActivity().getApplication()).getPulseHighCautionThreshold();

        double dbl_tempLowWarningThreshold = ((Globals) getActivity().getApplication()).getTempLowWarningThreshold();
        double dbl_tempLowCautionThreshold = ((Globals) getActivity().getApplication()).getTempLowCautionThreshold();
        double dbl_tempHighWarningThreshold = ((Globals) getActivity().getApplication()).getTempHighWarningThreshold();
        double dbl_tempHighCautionThreshold = ((Globals) getActivity().getApplication()).getTempHighCautionThreshold();

        int color_healthyGreen = getResources().getColor(R.color.healthy_green, getActivity().getTheme());
        int color_cautionYellow = getResources().getColor(R.color.caution_yellow, getActivity().getTheme());
        int color_warningRed = getResources().getColor(R.color.warning_red, getActivity().getTheme());
        int color_black = getResources().getColor(R.color.black, requireContext().getTheme());
        int color_white = getResources().getColor(R.color.white, requireContext().getTheme());

        String str_bloodOxTextBoxValue = getString(R.string.str_dataValue, Double.toString(dbl_bloodOxValue), "%");
        String str_tempTextBoxValue = getString(R.string.str_dataValue, Double.toString(dbl_tempValue), "F");
        String str_pulseTextBoxValue = getString(R.string.str_dataValue, Double.toString(dbl_pulseValue), "bpm");

        if(dbl_bloodOxValue <= dbl_bloodOxLowWarningThreshold)
        {
            binding.idBloodOxTextBox.setBackgroundColor(color_warningRed);
            binding.idBloodOxTextBox.setTextColor(color_white);
        }
        else if(dbl_bloodOxValue <= dbl_bloodOxLowCautionThreshold)
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