package com.example.baselineapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.VideoView;
import android.window.SplashScreen;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

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

        //Start our NotificationSetup class
        //This will do all of the setup work and eventually start our NotificationService class
        //Which handles notification logic and sending later
        System.out.println("Creating notification setup service... now!");
        startService(new Intent( this, NotificationSetup. class ));


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(Splash.this, Login2.class);
            startActivity(intent);
            finish();
        }, 2500);
    }
}