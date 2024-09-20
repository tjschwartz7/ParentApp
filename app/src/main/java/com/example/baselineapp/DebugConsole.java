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

import com.example.baselineapp.ui.notifications.NotificationsFragment;

public class DebugConsole extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_debug_console);

        ((EditText)findViewById(R.id.id_bloodOxygenEdit)).setText(String.valueOf(((Globals)getApplication()).getBloodOxVal()));
        ((EditText)findViewById(R.id.id_pulseEdit)).setText(String.valueOf(((Globals)getApplication()).getPulseVal()));
        ((EditText)findViewById(R.id.id_tempEdit)).setText(String.valueOf(((Globals)getApplication()).getTempVal()));


        Button save_button = (Button) findViewById(R.id.id_saveButton);
        save_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                double dbl_bloodOx = ((Globals)getApplication()).getBloodOxVal();
                double dbl_pulse = ((Globals)getApplication()).getPulseVal();
                double dbl_temp = ((Globals)getApplication()).getTempVal();

                //Get string value of user input
                String str_bloodOx = ((EditText) findViewById(R.id.id_bloodOxygenEdit)).getText().toString();

                //If user enters an empty string, leave at initialized value
                if (!str_bloodOx.isEmpty())
                    //Otherwise, set to the new value!
                    dbl_bloodOx = Double.parseDouble(str_bloodOx);

                //Get string value of user input
                String str_pulse = ((EditText) findViewById(R.id.id_pulseEdit)).getText().toString();

                //If user enters an empty string, leave at initialized value
                if (!str_pulse.isEmpty())
                    //Otherwise, set to the new value!
                    dbl_pulse = Double.parseDouble(str_pulse);

                //Get string value of user input
                String str_temp = ((EditText) findViewById(R.id.id_tempEdit)).getText().toString();

                //If user enters an empty string, leave at initialized value
                if (!str_temp.isEmpty())
                    //Otherwise, set to the new value!
                    dbl_temp = Double.parseDouble(str_temp);

                //DEBUG ONLY
                Globals.debugOnlySetVitals(dbl_bloodOx, dbl_pulse, dbl_temp);

                Intent intent = new Intent(DebugConsole.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button cancel_button = (Button) findViewById(R.id.id_cancelButton);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(DebugConsole.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}