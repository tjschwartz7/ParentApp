package com.example.baselineapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.VideoView;
import android.window.SplashScreen;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(Splash.this, Login2.class);
            startActivity(intent);
            finish();
        }, 3500);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // Switch to the main thread to update the UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Update the UI components safely on the main thread
                            final VideoView splash = (VideoView) findViewById(R.id.id_splashVideo);
                            splash.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.splashanimation);
                            splash.start();
                            splash.setOnCompletionListener (mediaPlayer -> splash.start());
                        }
                    });
                } catch (Exception e) {
                    Log.e("ThreadError", "Error in thread: " + e.getMessage());
                }
            }
        });

    }
}