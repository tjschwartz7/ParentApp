package com.example.baselineapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.apache.commons.validator.routines.EmailValidator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class CreateAccount extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);

        EditText phoneNumberEditText = findViewById(R.id.id_phoneNumberParent);

        phoneNumberEditText.addTextChangedListener(new TextWatcher()
        {
            private boolean isFormatting;
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (!isFormatting)
                {
                    String currentText = s.toString();
                    String cleanText = currentText.replaceAll("[^\\d]", ""); // Remove non-digit characters

                    // Format the number as (XXX) XXX-XXXX
                    String formattedNumber = formatPhoneNumber(cleanText);

                    isFormatting = true;
                    phoneNumberEditText.setText(formattedNumber);
                    phoneNumberEditText.setSelection(formattedNumber.length());
                    isFormatting = false;
                }
            }

            public void afterTextChanged(Editable s)
            {
            }

            private String formatPhoneNumber(String text)
            {
                if (text.length() > 10)
                {
                    return "(" + text.substring(0, 3) + ") " + text.substring(3, 6) + "-" + text.substring(6, 10);
                }
                else if (text.length() > 6)
                {
                    return "(" + text.substring(0, 3) + ") " + text.substring(3, 6) + "-" + text.substring(6);
                }
                else if (text.length() > 3)
                {
                    return "(" + text.substring(0, 3) + ") " + text.substring(3);
                }
                else if (!text.isEmpty())
                {
                    return "(" + text;
                }
                else
                {
                    return text;
                }
            }
        });

        //Cancel button --> Takes user back to login activity
        Button cancel_button = (Button) findViewById(R.id.id_cancelButton);
        cancel_button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(CreateAccount.this, Login2.class);
                startActivity(intent);
                finish();
            }
        });

        //The button that will submit the user information and send the user to the next activity.
        Button create_account_button = (Button) findViewById(R.id.id_createAccount);
        create_account_button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                EditText edt_firstNameParent = (EditText) findViewById(R.id.id_firstNameParent);
                EditText edt_lastNameParent = (EditText) findViewById(R.id.id_lastNameParent);
                EditText edt_emailParent = (EditText) findViewById(R.id.id_emailParent);
                EditText edt_phoneNumberParent = (EditText) findViewById(R.id.id_phoneNumberParent);
                EditText edt_passwordParent = (EditText) findViewById(R.id.id_passwordParent);
                EditText edt_passwordConfirm = (EditText) findViewById(R.id.id_passwordConfirm);

                String str_firstNameParent = edt_firstNameParent.getText().toString();
                String str_lastNameParent = edt_lastNameParent.getText().toString();
                String str_fullNameParent = str_firstNameParent + " " + str_lastNameParent;
                String str_emailParent = edt_emailParent.getText().toString();
                String str_phoneNumberParent = edt_phoneNumberParent.getText().toString().replaceAll("[^\\d]", "");
                String str_passwordParent = edt_passwordParent.getText().toString();
                String str_passwordConfirm = edt_passwordConfirm.getText().toString();

                String str_errorMessage = "";

                //TODO: Check if email address already exists
                //Email validation
                if(!EmailValidator.getInstance().isValid(str_emailParent))
                {
                    str_errorMessage += "There was a problem with the email address you entered. Please make sure that your email address was typed correctly.\n";
                }
                //Secondary email validation (: and ; characters are not allowed)
                if(!isValid(str_emailParent))
                {
                    str_errorMessage += "There was a problem with the email address you entered. Colons (:) and semi-colons (;) are not allowed.\n";
                }
                if(str_emailParent.isEmpty())
                {
                    str_errorMessage += "Please enter an email address.\n";
                }
                if(str_phoneNumberParent.length() != 10)
                {
                    str_errorMessage += "Please ensure a proper phone number has been entered.\n";
                }
                //TODO: Need additional validations (no empty strings allowed, only numbers for phone number, passwordParent and passwordConfirm must match, etc.)
                //Password validation (: and ; are not allowed, password must be at least 10 characters)
                if(!str_passwordParent.equals(str_passwordConfirm))
                {
                    str_errorMessage += "Passwords do not match. Please reenter your password.";
                }

                //Phone number validation


                //Check if there are any errors.
                if(!str_errorMessage.isEmpty())
                {
                    //TODO: Display Error Message String Somewhere on Page

                    return;
                }

                String str_accountString = "Email:" + str_emailParent +
                        ";Full Name:" + str_fullNameParent +
                        ";First Name:" + str_firstNameParent +
                        ";Last Name:" + str_lastNameParent +
                        ";Phone Number:" + str_phoneNumberParent +
                        ";Password:" + str_passwordParent;


                Intent intent = new Intent(CreateAccount.this, YourBabyActivity.class);
                Bundle bundle_forBabyActivity = new Bundle();
                intent.putExtra("Account String", str_accountString);
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
    //Takes a string and ensures that specific characters do not occur
    private boolean isValid(String str_stringToValidate)
    {
        char[] char_arr = str_stringToValidate.toCharArray();
        //Must check for these characters: ; : \\ \n
        for (char c : char_arr)
        {
            if (c == ':' || c == ';' || c == '\\' || c == '\n')
            {
                return false;
            }
        }
        return true;
    }
}