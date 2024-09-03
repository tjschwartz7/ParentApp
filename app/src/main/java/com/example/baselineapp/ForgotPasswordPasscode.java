package com.example.baselineapp;

import android.app.Application;
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

public class ForgotPasswordPasscode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password_passcode);
        //Code starts here
        //-----------------

        Button btn_continueButton = (Button) findViewById(R.id.id_continueButton);
        btn_continueButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordPasscode.this, ForgotPasswordChange.class);

                String userEnteredPasscode = ((EditText) findViewById(R.id.id_codeInput)).getText().toString();
                String actualPasscode = ((Globals) getApplication()).getCode();

                //If user entered valid passcode
                if(userEnteredPasscode.compareTo(actualPasscode) == 0)
                {
                    //Allow them to continue
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