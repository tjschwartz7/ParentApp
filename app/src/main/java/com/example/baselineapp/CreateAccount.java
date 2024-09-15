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

public class CreateAccount extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);

        EditText phoneNumberEditText = findViewById(R.id.id_phoneNumberParent);
        EditText firstNameEditText = findViewById(R.id.id_firstNameParent);
        EditText lastNameEditText = findViewById(R.id.id_lastNameParent);
        EditText passwordEditText = findViewById(R.id.id_passwordParent);
        EditText passwordConfirmEditText = findViewById(R.id.id_passwordConfirm);
        EditText emailEditText = findViewById(R.id.id_emailParent);

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

        firstNameEditText.addTextChangedListener(new TextWatcher()
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
                    // Format the string (limit to 20 characters)
                    if (currentText.length() > 20)
                    {
                        currentText = currentText.substring(0, 20);
                    }

                    isFormatting = true;
                    firstNameEditText.setText(currentText);
                    firstNameEditText.setSelection(currentText.length());
                    isFormatting = false;
                }
            }

            public void afterTextChanged(Editable s)
            {
            }
        });

        lastNameEditText.addTextChangedListener(new TextWatcher()
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
                    // Format the string (limit to 20 characters)
                    if (currentText.length() > 20)
                    {
                        currentText = currentText.substring(0, 20);
                    }

                    isFormatting = true;
                    lastNameEditText.setText(currentText);
                    lastNameEditText.setSelection(currentText.length());
                    isFormatting = false;
                }
            }

            public void afterTextChanged(Editable s)
            {
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher()
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
                    // Format the string (limit to 20 characters)
                    if (currentText.length() > 20)
                    {
                        currentText = currentText.substring(0, 20);
                    }

                    isFormatting = true;
                    passwordEditText.setText(currentText);
                    passwordEditText.setSelection(currentText.length());
                    isFormatting = false;
                }
            }

            public void afterTextChanged(Editable s)
            {
            }
        });

        passwordConfirmEditText.addTextChangedListener(new TextWatcher()
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
                    // Format the string (limit to 20 characters)
                    if (currentText.length() > 20)
                    {
                        currentText = currentText.substring(0, 20);
                    }

                    isFormatting = true;
                    passwordConfirmEditText.setText(currentText);
                    passwordConfirmEditText.setSelection(currentText.length());
                    isFormatting = false;
                }
            }

            public void afterTextChanged(Editable s)
            {
            }
        });

        emailEditText.addTextChangedListener(new TextWatcher()
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
                    // Format the string (limit to 100 characters)
                    if (currentText.length() > 100)
                    {
                        currentText = currentText.substring(0, 100);
                    }

                    isFormatting = true;
                    emailEditText.setText(currentText);
                    emailEditText.setSelection(currentText.length());
                    isFormatting = false;
                }
            }

            public void afterTextChanged(Editable s)
            {
            }
        });


        //The button that will be submit the user information
        //and switch the user into the next state.
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
                if(!str_emailParent.equals(str_passwordConfirm))
                {
                    str_errorMessage += "Passwords do not match.";
                }

                //Phone number validation


                //Check if there are any errors.
                if(str_errorMessage.isEmpty())
                {
                    //TODO: Display Error Message String Somewhere on Page

                    return;
                }

                try
                {
                    String str_filename = "AccountData.txt";

                    String str_accountString = "Email:" + str_emailParent +
                            ";Full Name:" + str_fullNameParent +
                            ";First Name:" + str_firstNameParent +
                            ";Last Name:" + str_lastNameParent +
                            ";Phone Number:" + str_phoneNumberParent +
                            ";Password:" + str_passwordParent + ";\n";

                    try (FileOutputStream fos = v.getContext().openFileOutput(str_filename, Context.MODE_PRIVATE))
                    {
                        fos.write(str_accountString.getBytes());
                    }

                    //Following code gets the file, opens it, and then logs the first line of the file as output. Used for testing purposes. Comment out the block if you are not testing it.
                    //File file = new File(v.getContext().getFilesDir(), str_filename);
                    FileInputStream fis = v.getContext().openFileInput("AccountData.txt");
                    InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
                    StringBuilder stringBuilder = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(isr)) {
                        String line = reader.readLine();
                        while (line != null) {
                            stringBuilder.append(line).append('\n');
                            line = reader.readLine();
                        }
                    }
                    catch (IOException e) {
                        // Error occurred when opening raw file for reading.
                    }
                    finally {
                        String contents = stringBuilder.toString();
                        String tag = "CreateAccount";
                        Log.d(tag, contents);
                    }
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }

                Intent intent = new Intent(CreateAccount.this, YourBabyActivity.class);
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