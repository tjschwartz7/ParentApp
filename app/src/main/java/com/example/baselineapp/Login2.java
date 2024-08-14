package com.example.baselineapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
        //On Go button click, merely go to the MainActivity for now. Will implement appropriate login
        //functionality later.
        Button btn_goButton = (Button) findViewById(R.id.id_goButton);
        btn_goButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Login2.this, MainActivity.class);
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
}