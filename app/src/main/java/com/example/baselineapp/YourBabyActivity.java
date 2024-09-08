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
                int birthYear = birthdate.getYear();

                int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
                int birthMonth = birthdate.getMonth();

                int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                int birthday = birthdate.getDayOfMonth();

                //Integer date codes for comparison
                int birthDateId = birthYear * 10000 + birthMonth*100 + birthday;
                int currentDateId = currentYear * 10000 + currentMonth*100 + currentDay;

                //If date is valid (before or equal to current day)
                if(currentDateId >= birthDateId)
                {
                    //Set birthdate and change activity
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