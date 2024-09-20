package com.example.baselineapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class NotificationSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification_settings);

        //Code starts here
        //-----------------


        //WARNINGS
        double dbl_pulseHighWarningThreshold  = Globals.getPulseHighWarningThreshold();
        double dbl_pulseLowWarningThreshold   = Globals.getPulseLowWarningThreshold();
        double dbl_tempHighWarningThreshold   = Globals.getTempHighWarningThreshold();
        double dbl_tempLowWarningThreshold    = Globals.getTempLowWarningThreshold();
        double dbl_bloodOxLowWarningThreshold = Globals.getBloodOxLowWarningThreshold();

        ((EditText)findViewById(R.id.id_pulseWarningUpperBound)).setText(String.valueOf(dbl_pulseHighWarningThreshold));
        ((EditText)findViewById(R.id.id_pulseWarningLowerBound)).setText(String.valueOf(dbl_pulseLowWarningThreshold));
        ((EditText)findViewById(R.id.id_tempWarningUpperBound)).setText(String.valueOf(dbl_tempHighWarningThreshold));
        ((EditText)findViewById(R.id.id_tempWarningLowerBound)).setText(String.valueOf(dbl_tempLowWarningThreshold));
        ((EditText)findViewById(R.id.id_bloodOxWarningLowerBound)).setText(String.valueOf(dbl_bloodOxLowWarningThreshold));

        //CAUTIONS
        double dbl_pulseHighCautionThreshold  = Globals.getPulseHighCautionThreshold();
        double dbl_pulseLowCautionThreshold   = Globals.getPulseLowCautionThreshold();
        double dbl_tempHighCautionThreshold   = Globals.getTempHighCautionThreshold();
        double dbl_tempLowCautionThreshold    = Globals.getTempLowCautionThreshold();
        double dbl_bloodOxLowCautionThreshold = Globals.getBloodOxLowCautionThreshold();

        ((EditText)findViewById(R.id.id_pulseCautionUpperBound)).setText(String.valueOf(dbl_pulseHighCautionThreshold));
        ((EditText)findViewById(R.id.id_pulseCautionLowerBound)).setText(String.valueOf(dbl_pulseLowCautionThreshold));
        ((EditText)findViewById(R.id.id_tempCautionUpperBound)).setText(String.valueOf(dbl_tempHighCautionThreshold));
        ((EditText)findViewById(R.id.id_tempCautionLowerBound)).setText(String.valueOf(dbl_tempLowCautionThreshold));
        ((EditText)findViewById(R.id.id_bloodOxCautionLowerBound)).setText(String.valueOf(dbl_bloodOxLowCautionThreshold));

        Button cancel_button = (Button) findViewById(R.id.id_cancelButton);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(NotificationSettings.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button save_button = (Button) findViewById(R.id.id_saveButton);
        save_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                {
                    //WARNINGS
                    //Initialize our bounds to their previous values:
                    //if user enters an empty string, we will reset them
                    //to their previous value.
                    double dbl_bloodOxWarningLowerBound = dbl_bloodOxLowWarningThreshold;
                    double dbl_pulseWarningLowerBound = dbl_pulseLowWarningThreshold;
                    double dbl_pulseWarningUpperBound = dbl_pulseHighWarningThreshold;
                    double dbl_tempWarningLowerBound = dbl_tempLowWarningThreshold;
                    double dbl_tempWarningUpperBound = dbl_tempHighWarningThreshold;

                    //TODO Set conditions for what are acceptable bounds for user to enter
                    //For example, we shouldn't let the user set the low bound threshold
                    //for blood ox at 0%.

                    //Create a temporary scope for these strings
                    {
                        //Get string value of user input
                        String str_bloodOxLowerBound = ((EditText) findViewById(R.id.id_bloodOxWarningLowerBound)).getText().toString();
                        //If user enters an empty string, leave at initialized value
                        if (!str_bloodOxLowerBound.isEmpty())
                            //Otherwise, set to the new value!
                            dbl_bloodOxWarningLowerBound = Double.parseDouble(str_bloodOxLowerBound);

                        String str_pulseLowerBound = ((EditText) findViewById(R.id.id_pulseWarningLowerBound)).getText().toString();
                        if (!str_pulseLowerBound.isEmpty())
                            dbl_pulseWarningLowerBound = Double.parseDouble(str_pulseLowerBound);

                        String str_pulseUpperBound = ((EditText) findViewById(R.id.id_pulseWarningUpperBound)).getText().toString();
                        if (!str_pulseUpperBound.isEmpty())
                            dbl_pulseWarningUpperBound = Double.parseDouble(str_pulseUpperBound);

                        String str_tempLowerBound = ((EditText) findViewById(R.id.id_tempWarningLowerBound)).getText().toString();
                        if (!str_tempLowerBound.isEmpty())
                            dbl_tempWarningLowerBound = Double.parseDouble(str_tempLowerBound);

                        String str_tempUpperBound = ((EditText) findViewById(R.id.id_tempWarningUpperBound)).getText().toString();
                        if (!str_tempUpperBound.isEmpty())
                            dbl_tempWarningUpperBound = Double.parseDouble(str_tempUpperBound);
                    }
                    Globals.setBloodOxLowWarningThreshold(dbl_bloodOxWarningLowerBound);
                    Globals.setPulseLowWarningThreshold(dbl_pulseWarningLowerBound);
                    Globals.setPulseHighWarningThreshold(dbl_pulseWarningUpperBound);
                    Globals.setTempLowWarningThreshold(dbl_tempWarningLowerBound);
                    Globals.setTempHighWarningThreshold(dbl_tempWarningUpperBound);
                }


                {
                    //CAUTION
                    //Initialize our bounds to their previous values:
                    //if user enters an empty string, we will reset them
                    //to their previous value.
                    double dbl_bloodOxCautionLowerBound = dbl_bloodOxLowCautionThreshold;
                    double dbl_pulseCautionLowerBound = dbl_pulseLowCautionThreshold;
                    double dbl_pulseCautionUpperBound = dbl_pulseHighCautionThreshold;
                    double dbl_tempCautionLowerBound = dbl_tempLowCautionThreshold;
                    double dbl_tempCautionUpperBound = dbl_tempHighCautionThreshold;

                    //TODO Set conditions for what are acceptable bounds for user to enter
                    //For example, we shouldn't let the user set the low bound threshold
                    //for blood ox at 0%.

                    //Create a temporary scope for these strings
                    {
                        //Get string value of user input
                        String str_bloodOxLowerBound = ((EditText) findViewById(R.id.id_bloodOxCautionLowerBound)).getText().toString();
                        //If user enters an empty string, leave at initialized value
                        if (!str_bloodOxLowerBound.isEmpty())
                            //Otherwise, set to the new value!
                            dbl_bloodOxCautionLowerBound = Double.parseDouble(str_bloodOxLowerBound);

                        String str_pulseLowerBound = ((EditText) findViewById(R.id.id_pulseCautionLowerBound)).getText().toString();
                        if (!str_pulseLowerBound.isEmpty())
                            dbl_pulseCautionLowerBound = Double.parseDouble(str_pulseLowerBound);

                        String str_pulseUpperBound = ((EditText) findViewById(R.id.id_pulseCautionUpperBound)).getText().toString();
                        if (!str_pulseUpperBound.isEmpty())
                            dbl_pulseCautionUpperBound = Double.parseDouble(str_pulseUpperBound);

                        String str_tempLowerBound = ((EditText) findViewById(R.id.id_tempCautionLowerBound)).getText().toString();
                        if (!str_tempLowerBound.isEmpty())
                            dbl_tempCautionLowerBound = Double.parseDouble(str_tempLowerBound);

                        String str_tempUpperBound = ((EditText) findViewById(R.id.id_tempCautionUpperBound)).getText().toString();
                        if (!str_tempUpperBound.isEmpty())
                            dbl_tempCautionUpperBound = Double.parseDouble(str_tempUpperBound);
                    }
                    Globals.setBloodOxLowCautionThreshold(dbl_bloodOxCautionLowerBound);
                    Globals.setPulseLowCautionThreshold(dbl_pulseCautionLowerBound);
                    Globals.setPulseHighCautionThreshold(dbl_pulseCautionUpperBound);
                    Globals.setTempLowCautionThreshold(dbl_tempCautionLowerBound);
                    Globals.setTempHighCautionThreshold(dbl_tempCautionUpperBound);
                }
                Intent intent = new Intent(NotificationSettings.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //-----------------

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}