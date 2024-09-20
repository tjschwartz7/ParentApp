package com.example.baselineapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Login2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login2);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        final VideoView logo = (VideoView) findViewById(R.id.id_logoVideo);
        logo.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.logoanimation);
        logo.start();

        TextInputEditText emailInput = findViewById(R.id.id_emailLoginInput);
        TextInputEditText passwordInput = findViewById(R.id.id_passwordLoginInput);
        //On Go button click, merely go to the MainActivity for now. Will implement appropriate login
        //functionality later.
        Button btn_goButton = (Button) findViewById(R.id.id_goButton);
        btn_goButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Login2.this, MainActivity.class);
                //Read from text file. Find line with matching email.
                String str_errorMessage = "";
                FileInputStream fis = null;
                try
                {
                    fis = v.getContext().openFileInput("AccountData.txt");
                }
                catch (FileNotFoundException e)
                {
                    throw new RuntimeException(e);
                }
                InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
                try (BufferedReader reader = new BufferedReader(isr)) {
                    String line = reader.readLine();
                    while (line != null)
                    {
                        int beginIndex = line.indexOf(':') + 1;
                        int endIndex = line.indexOf(';');
                        //Email address should be first in the string/line.
                        String storedEmail = line.substring(beginIndex, endIndex);
                        if(storedEmail.equals(emailInput.getText().toString()))
                        {
                            break;
                        }
                        line = reader.readLine();
                    }
                    if(line == null)
                    {
                        str_errorMessage += "Account does not exist.";
                    }
                    else
                    {
                        Globals.setInitialValues(line);
                        if(!Globals.getMap().get("Password").equals(passwordInput.getText().toString()))
                        {
                            str_errorMessage += "Wrong password.";
                        }
                    }

                }
                catch (IOException e)
                {
                    // Error occurred when opening raw file for reading.
                }
                if(str_errorMessage.isEmpty())
                {
                    //Start our NotificationSetup class
                    //This will do all of the setup work and eventually start our NotificationService class
                    //Which handles notification logic and sending later
                    System.out.println("Creating notification setup service... now!");
                    Globals.setNotificationService(new Intent( Login2.this, NotificationService. class ));
                    startService(new Intent( Login2.this, NotificationSetup. class ));


                    startActivity(intent);
                    finish();
                }
                else
                {
                    //TODO: Display error message to user.
                }
            }
        });
        Button btn_signUp = (Button) findViewById(R.id.id_signUp);
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Login2.this, CreateAccount.class);
                startActivity(intent);
                finish();
            }
        });

        Button btn_forgotPassword = (Button) findViewById(R.id.id_forgotPasswordButton);
        btn_forgotPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Login2.this, ForgotPasswordMethod.class);
                startActivity(intent);
                finish();
            }
        });

        // Check if the permission for POST_NOTIFICATION is provided or not
        if (ActivityCompat.checkSelfPermission(
                getApplicationContext(),
                android.Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
        )
        {

            //Request permissions
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                    101);
        }

        if(ActivityCompat.checkSelfPermission(
                getApplicationContext(),
                android.Manifest.permission.VIBRATE
        ) != PackageManager.PERMISSION_GRANTED)
        {
            //Request permissions
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.VIBRATE},
                    101);
        }

        if(ActivityCompat.checkSelfPermission(
                getApplicationContext(),
                android.Manifest.permission.FOREGROUND_SERVICE
        ) != PackageManager.PERMISSION_GRANTED)
        {
            //Request permissions
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.FOREGROUND_SERVICE},
                    101);
        }

        if(ActivityCompat.checkSelfPermission(
                getApplicationContext(),
                android.Manifest.permission.FOREGROUND_SERVICE_DATA_SYNC
        ) != PackageManager.PERMISSION_GRANTED)
        {
            //Request permissions
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.FOREGROUND_SERVICE_DATA_SYNC},
                    101);
        }


    }
}