package com.example.baselineapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.Calendar;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        //Code starts here
        //-----------------

        EditText edt_firstNameBaby = (EditText)findViewById(R.id.id_firstNameBaby);
        EditText edt_emailParent = (EditText)findViewById(R.id.id_emailParent);
        EditText edt_phoneNumberParent = (EditText)findViewById(R.id.id_phoneNumberParent);
        EditText edt_firstNameParent = (EditText)findViewById(R.id.id_firstNameParent);
        EditText edt_lastNameParent = (EditText)findViewById(R.id.id_lastNameParent);
        TextView tv_babyBirthdayText = (TextView)findViewById(R.id.id_babyBirthdayText);
        DatePicker dp_birthdayPicker = (DatePicker)findViewById(R.id.id_birthdatePicker);

        edt_phoneNumberParent.addTextChangedListener(new TextWatcher()
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
                    edt_phoneNumberParent.setText(formattedNumber);
                    edt_phoneNumberParent.setSelection(formattedNumber.length());
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

        edt_firstNameBaby.setText(Globals.getMap().get("Baby First Name"));
        edt_emailParent.setText(Globals.getMap().get("Email"));
        edt_phoneNumberParent.setText(Globals.getMap().get("Phone Number"));
        edt_firstNameParent.setText(Globals.getMap().get("First Name"));
        edt_lastNameParent.setText(Globals.getMap().get("Last Name"));
        String str_babyBirthdayText = Globals.getMap().get("Baby First Name") + "'s Birthday";
        tv_babyBirthdayText.setText(str_babyBirthdayText);
        String str_babyBirthday = Globals.getMap().get("Baby Birthday");
        String[] yearMonthDay = str_babyBirthday.split("-");
        dp_birthdayPicker.updateDate(Integer.parseInt(yearMonthDay[0]), Integer.parseInt(yearMonthDay[1]), Integer.parseInt(yearMonthDay[2]));


        Button save_button = (Button) findViewById(R.id.id_saveButton);
        save_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO: Handle faulty inputs
                String str_babyFirstName = edt_firstNameBaby.getText().toString();
                String str_emailParent = edt_emailParent.getText().toString();
                String str_phoneNumberParent = edt_phoneNumberParent.getText().toString().replaceAll("[^\\d]", "");
                String str_firstNameParent = edt_firstNameParent.getText().toString();
                String str_lastNameParent = edt_lastNameParent.getText().toString();

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
                    str_errorMessage += "There was a problem with the email address you entered. Colons (:), semi-colons (;) and backslashes (\\) are not allowed.\n";
                }
                if(str_emailParent.isEmpty())
                {
                    str_errorMessage += "Please enter an email address.\n";
                }
                if(str_phoneNumberParent.length() != 10)
                {
                    str_errorMessage += "Please ensure a proper phone number has been entered. Phone numbers must follow the standard US telephone number format. Example: (123) 456-7890.\n";
                }
                //TODO: Need additional validations (no empty strings allowed, only numbers for phone number, passwordParent and passwordConfirm must match, etc.)

                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                int birthYear = dp_birthdayPicker.getYear();

                int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
                int birthMonth = dp_birthdayPicker.getMonth();

                int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                int birthDay = dp_birthdayPicker.getDayOfMonth();

                //Integer date codes for comparison
                int birthDateId = birthYear * 10000 + birthMonth*100 + birthDay;
                int currentDateId = currentYear * 10000 + currentMonth*100 + currentDay;

                //If date is valid (before or equal to current day)
                if(currentDateId < birthDateId)
                {
                    str_errorMessage += "Invalid baby birthday selected. Please pick a date on or before today in the calendar.";
                }



                //Check if there are any errors.
                if(!str_errorMessage.isEmpty())
                {
                    //TODO: Display Error Message String Somewhere on Page

                    return;
                }

                HashMap<String, String> writeMap = new HashMap<>();
                writeMap.put("Baby First Name", str_babyFirstName);
                writeMap.put("Email", str_emailParent);
                writeMap.put("Phone Number", str_phoneNumberParent);
                writeMap.put("First Name", str_firstNameParent);
                writeMap.put("Last Name", str_lastNameParent);

                ReaderWriter rw = new ReaderWriter();
                rw.writeDataToTextFile(v.getContext(), writeMap);
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button cancel_button = (Button) findViewById(R.id.id_cancelButton);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
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