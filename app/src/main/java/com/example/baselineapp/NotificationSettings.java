package com.example.baselineapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

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

        double dbl_pulseHighWarningThreshold  = ((Globals) getApplication()).getPulseHighWarningThreshold();
        double dbl_pulseLowWarningThreshold   = ((Globals) getApplication()).getPulseLowWarningThreshold();
        double dbl_tempHighWarningThreshold   = ((Globals) getApplication()).getTempHighWarningThreshold();
        double dbl_tempLowWarningThreshold    = ((Globals) getApplication()).getTempLowWarningThreshold();
        double dbl_bloodOxLowWarningThreshold = ((Globals) getApplication()).getBloodOxLowWarningThreshold();

        ((EditText)findViewById(R.id.id_pulseUpperBound)).setText(String.valueOf(dbl_pulseHighWarningThreshold));
        ((EditText)findViewById(R.id.id_pulseLowerBound)).setText(String.valueOf(dbl_pulseLowWarningThreshold));
        ((EditText)findViewById(R.id.id_tempUpperBound)).setText(String.valueOf(dbl_tempHighWarningThreshold));
        ((EditText)findViewById(R.id.id_tempLowerBound)).setText(String.valueOf(dbl_tempLowWarningThreshold));
        ((EditText)findViewById(R.id.id_bloodOxLowerBound)).setText(String.valueOf(dbl_bloodOxLowWarningThreshold));

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

                //Initialize our bounds to their previous values:
                //if user enters an empty string, we will reset them
                //to their previous value.
                double dbl_bloodOxLowerBound = dbl_bloodOxLowWarningThreshold;
                double dbl_pulseLowerBound = dbl_pulseLowWarningThreshold;
                double dbl_pulseUpperBound = dbl_pulseHighWarningThreshold;
                double dbl_tempLowerBound = dbl_tempLowWarningThreshold;
                double dbl_tempUpperBound = dbl_tempHighWarningThreshold;

                //TODO Set conditions for what are acceptable bounds for user to enter
                //For example, we shouldn't let the user set the low bound threshold
                //for blood ox at 0%.

                //Get string value of user input
                String str_bloodOxLowerBound = ((EditText) findViewById(R.id.id_bloodOxLowerBound)).getText().toString();
                //If user enters an empty string, leave at initialized value
                if(str_bloodOxLowerBound != "")
                    //Otherwise, set to the new value!
                    dbl_bloodOxLowerBound = Double.parseDouble(str_bloodOxLowerBound);

                String str_pulseLowerBound = ((EditText) findViewById(R.id.id_pulseLowerBound)).getText().toString();
                if(str_pulseLowerBound != "")
                    dbl_pulseLowerBound = Double.parseDouble(str_pulseLowerBound);

                String str_pulseUpperBound = ((EditText) findViewById(R.id.id_pulseUpperBound)).getText().toString();
                if(str_pulseUpperBound != "")
                    dbl_pulseUpperBound = Double.parseDouble(str_pulseUpperBound);

                String str_tempLowerBound = ((EditText) findViewById(R.id.id_tempLowerBound)).getText().toString();
                if(str_tempLowerBound != "")
                    dbl_tempLowerBound = Double.parseDouble(str_tempLowerBound);

                String str_tempUpperBound = ((EditText) findViewById(R.id.id_tempUpperBound)).getText().toString();
                if(str_tempUpperBound != "")
                    dbl_tempUpperBound = Double.parseDouble(str_tempUpperBound);


                System.out.println("Blood ox lower bound = "+dbl_bloodOxLowerBound);
                ((Globals)getApplication()).setBloodOxLowWarningThreshold(dbl_bloodOxLowerBound);
                ((Globals)getApplication()).setPulseLowWarningThreshold(dbl_pulseLowerBound);
                ((Globals)getApplication()).setPulseHighWarningThreshold(dbl_pulseUpperBound);
                ((Globals)getApplication()).setTempLowWarningThreshold(dbl_tempLowerBound);
                ((Globals)getApplication()).setTempHighWarningThreshold(dbl_tempUpperBound);

                System.out.println("Blood ox lower bound (After) = " + ((Globals)getApplication()).getBloodOxLowWarningThreshold());

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