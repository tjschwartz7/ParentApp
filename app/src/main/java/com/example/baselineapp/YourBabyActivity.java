package com.example.baselineapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class YourBabyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_your_baby);

        //Code starts here
        //-----------------

        DatePicker birthdate = (DatePicker)findViewById(R.id.id_birthdatePicker);

        Button btn_cancelButton = (Button) findViewById(R.id.id_cancelButton);
        btn_cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(YourBabyActivity.this, Login2.class);
                startActivity(intent);
                finish();
            }
        });

        Button btn_saveButton = (Button) findViewById(R.id.id_saveButton);
        btn_saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(YourBabyActivity.this, Login2.class);
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                int birthyear = birthdate.getYear();

                int currentmonth = Calendar.getInstance().get(Calendar.MONTH);
                int birthmonth = birthdate.getMonth();

                int currentday = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                int birthday = birthdate.getDayOfMonth();

                if(birthyear <= currentYear &&
                        birthmonth <= currentmonth &&
                        birthday <= currentday)
                {
                    ((Globals)getApplication()).setBirthdate(birthdate);
                    startActivity(intent);
                    finish();
                }
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