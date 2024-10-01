package com.example.baselineapp;

import android.content.Context;
import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import org.apache.commons.validator.routines.EmailValidator;

import org.apache.commons.validator.routines.EmailValidator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // Switch to the main thread to update the UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Update the UI components safely on the main thread
                            final VideoView logo = (VideoView) findViewById(R.id.id_logoVideo);
                            logo.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.logoanimation);
                            logo.start();
                        }
                    });
                } catch (Exception e) {
                    Log.e("ThreadError", "Error in thread: " + e.getMessage());
                }
            }
        });

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // Check if the permission for POST_NOTIFICATION is provided or not
                if (ActivityCompat.checkSelfPermission(
                        getApplicationContext(),
                        android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
                )
                {

                    //Request permissions
                    ActivityCompat.requestPermissions(Login2.this,
                            new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                            101);
                }

                if(ActivityCompat.checkSelfPermission(
                        getApplicationContext(),
                        android.Manifest.permission.VIBRATE
                ) != PackageManager.PERMISSION_GRANTED)
                {
                    //Request permissions
                    ActivityCompat.requestPermissions(Login2.this,
                            new String[]{android.Manifest.permission.VIBRATE},
                            101);
                }

                if(ActivityCompat.checkSelfPermission(
                        getApplicationContext(),
                        android.Manifest.permission.FOREGROUND_SERVICE
                ) != PackageManager.PERMISSION_GRANTED)
                {
                    //Request permissions
                    ActivityCompat.requestPermissions(Login2.this,
                            new String[]{android.Manifest.permission.FOREGROUND_SERVICE},
                            101);
                }

                if(ActivityCompat.checkSelfPermission(
                        getApplicationContext(),
                        android.Manifest.permission.FOREGROUND_SERVICE_DATA_SYNC
                ) != PackageManager.PERMISSION_GRANTED)
                {
                    //Request permissions
                    ActivityCompat.requestPermissions(Login2.this,
                            new String[]{Manifest.permission.FOREGROUND_SERVICE_DATA_SYNC},
                            101);
                }
            }
        });

        //TODO: CHECK THIS!!!
        //ReaderWriter rw = new ReaderWriter();
        //rw.readTextFileAndInitiallyPopulateGlobals(this);

        //TODO: CHECK THIS!!!
        //ReaderWriter rw = new ReaderWriter();
        //rw.readTextFileAndInitiallyPopulateGlobals(this);

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
                String str_emailInput = emailInput.getText().toString();
                String str_passwordInput = passwordInput.getText().toString();
                try
                {
                    fis = v.getContext().openFileInput("AccountData.txt");
                }
                catch (FileNotFoundException e)
                {
                    try (FileOutputStream fos = v.getContext().openFileOutput("AccountData.txt", Context.MODE_PRIVATE))
                    {
                        fos.write("".getBytes());
                        fis = v.getContext().openFileInput("AccountData.txt");
                    }
                    catch(IOException a)
                    {
                        a.printStackTrace();
                        throw new RuntimeException();
                    }
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
                        if(storedEmail.equals(str_emailInput))
                        {
                            break;
                        }
                        line = reader.readLine();
                    }
                    if(line == null)
                    {
                        str_errorMessage += "- Account with this email address does not exist.\n";
                    }
                    else
                    {
                        Globals.setInitialValues(line);
                        if(!Globals.getMap().get("Password").equals(str_passwordInput))
                        {
                            str_errorMessage += "- Wrong password.\n";
                        }
                    }
                }
                catch (IOException e)
                {
                    // Error occurred when opening raw file for reading.
                    e.printStackTrace();
                }

                if(str_emailInput.isEmpty())
                {
                    str_errorMessage += "- Please enter an email address.\n";
                }
                if(str_passwordInput.isEmpty())
                {
                    str_errorMessage += "- Please enter a password.\n";
                }

                //Check if there are any errors.
                if(!str_errorMessage.isEmpty())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                    builder.setTitle("Input Issues");
                    str_errorMessage = "Please fix the following issues: \n" + str_errorMessage;
                    builder.setMessage(str_errorMessage);
                    // Add the buttons.
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User taps OK button.
                            dialog.cancel();
                        }
                    });

                    // Create the AlertDialog.
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    return;
                }

                //Start our NotificationSetup class
                //This will do all of the setup work and eventually start our NotificationService class
                //Which handles notification logic and sending later
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {

                        boolean notificationServiceIsRunning = isMyServiceRunning(NotificationService.class, getApplicationContext());
                        if(!notificationServiceIsRunning)
                        {
                            Globals.setNotificationService(new Intent( Login2.this, NotificationService. class ));
                            startService(new Intent( Login2.this, NotificationSetup. class ));
                        }

                        boolean TCPServiceIsRunning = isMyServiceRunning(TCPServerService.class, getApplicationContext());
                        if(!TCPServiceIsRunning)
                        {
                            Globals.setTCPServerService(new Intent( Login2.this, TCPServerService. class ));
                            startService(new Intent( Login2.this, TCPServerService. class ));
                        }

                        boolean UDPServiceIsRunning = isMyServiceRunning(UDPServerService.class, getApplicationContext());
                        if(!UDPServiceIsRunning)
                        {
                            Log.d("UDP Testing", "Creating UDP service!");
                            Globals.setUdpServerService(new Intent( Login2.this, UDPServerService. class ));
                            startService(new Intent( Login2.this, UDPServerService. class ));
                        }

                    }
                });

                Globals.setLoggedIn(true);


                //ReaderWriter rw = new ReaderWriter();
                //rw.testPrintTextFile(v.getContext());
                startActivity(intent);
                finish();
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

    }

    public boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}