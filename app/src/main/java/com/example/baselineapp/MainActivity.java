package com.example.baselineapp;

import static androidx.core.content.ContextCompat.getSystemService;

import static com.example.baselineapp.NotificationService.NOTIFICATION_CHANNEL_ID;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.baselineapp.databinding.ActivityMainBinding;


//Note: button onClick functions execute in the main thread.
//Keep them short or the app will noticeably slow down!
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.id_navView);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_settings, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.idNavView, navController);

        //Code goes here

        // Check if the permission for POST_NOTIFICATION is provided or not
        if (ActivityCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
        )
        {

            //Request permissions
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    101);
        }

        if(ActivityCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.VIBRATE
        ) != PackageManager.PERMISSION_GRANTED)
        {
            //Request permissions
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.VIBRATE},
                    101);
        }

        if(ActivityCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.FOREGROUND_SERVICE
        ) != PackageManager.PERMISSION_GRANTED)
        {
            //Request permissions
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.FOREGROUND_SERVICE},
                    101);
        }

        if(ActivityCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.FOREGROUND_SERVICE_DATA_SYNC
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
    }

    @Override
    protected void onStop ()
    {
        super .onStop() ;
    }

}

