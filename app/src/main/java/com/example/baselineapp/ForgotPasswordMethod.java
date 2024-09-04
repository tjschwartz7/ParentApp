package com.example.baselineapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class ForgotPasswordMethod extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password_method);

        //Code starts here
        //-----------------

        //This button will send a code,
        //and transfer the user to the next page.
        //There, the user will enter the passcode they received.
        Button btn_sendCode = (Button) findViewById(R.id.id_sendCodeButton);
        btn_sendCode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordMethod.this, ForgotPasswordPasscode.class);
                EditText userInput = (EditText) findViewById(R.id.id_methodInput);

                //Get user input (could be email or phone)
                //TODO Assuming input is email for now; add logic to handle phone numbers
                String[] userEmail = {userInput.getText().toString()};

                //Initialize random object
                Random rand = new Random();

                //Get random 6 digit code
                String code = String.valueOf(rand.nextInt(899999) + 100000);

                //Set the global code variable for verification when the user enters the code
                //on the next page
                ((Globals) getApplication()).setCode(code);

                //TODO Remove debug
                System.out.println("User email is: " + userEmail[0]);

                //Send email with subject header
                composeEmail(userEmail, "Change Password - Code is " + code);
                startActivity(intent);
                finish();
            }
        });

        Button btn_backButton = (Button) findViewById(R.id.id_backButton);
        btn_backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordMethod.this, Login2.class);
                startActivity(intent);
                finish();
            }
        });

        //-----------------
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void composeEmail(String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}