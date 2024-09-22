package com.example.baselineapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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
        EditText edt_babyFirstName = (EditText)findViewById(R.id.id_firstNameBaby);

        Button btn_cancelButton = (Button) findViewById(R.id.id_cancelButton);
        btn_cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(YourBabyActivity.this, Login2.class);
                startActivity(intent);
                finish();
            }
        });

        Button btn_saveButton = (Button) findViewById(R.id.id_saveButton);
        btn_saveButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(YourBabyActivity.this, Login2.class);
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                int birthYear = birthdate.getYear();

                int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
                int birthMonth = birthdate.getMonth();

                int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                int birthDay = birthdate.getDayOfMonth();

                //Integer date codes for comparison
                int birthDateId = birthYear * 10000 + birthMonth*100 + birthDay;
                int currentDateId = currentYear * 10000 + currentMonth*100 + currentDay;

                String str_babyFirstName = edt_babyFirstName.getText().toString();

                //If date is valid (before or equal to current day)
                if(currentDateId >= birthDateId)
                {
                    //Create the account based on user information previously entered
                    try
                    {
                        String str_filename = "AccountData.txt";

                        String str_accountString = getIntent().getExtras().getString("Account String") +
                                ";Baby Birthday:" + birthYear + "-" + birthMonth + "-" + birthDay +
                                ";Baby First Name:" + str_babyFirstName;

                        for(int i = 1; i <= 10; i++)
                        {
                            str_accountString += ";Notification " + i + " Title:" + "" +
                                    ";Notification " + i + " Body:" + "";
                        }
                        str_accountString += ";\n";

                        try (FileOutputStream fos = v.getContext().openFileOutput(str_filename, Context.MODE_APPEND))
                        {
                            fos.write(str_accountString.getBytes());
                        }


                        //Following code gets the file, opens it, and then logs the first line of the file as output. Used for testing purposes. Comment out the block if you are not testing it.
                        /*
                        FileInputStream fis = v.getContext().openFileInput(str_filename);
                        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
                        StringBuilder stringBuilder = new StringBuilder();
                        try (BufferedReader reader = new BufferedReader(isr))
                        {
                            String line = reader.readLine();
                            while (line != null)
                            {
                                stringBuilder.append(line).append('\n');
                                line = reader.readLine();
                            }
                        }
                        catch (IOException e)
                        {
                            // Error occurred when opening raw file for reading.
                            e.printStackTrace();
                        }
                        finally {
                            String contents = stringBuilder.toString();
                            String tag = "CreateAccount";
                            Log.d(tag, contents);
                        }
                        */
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                    finish();
                }
                else
                {
                    //TODO: Display an error message to the user here stating that the date selected is not valid
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