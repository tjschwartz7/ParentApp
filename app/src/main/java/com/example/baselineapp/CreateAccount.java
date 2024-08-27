package com.example.baselineapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.apache.commons.validator.routines.EmailValidator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreateAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);

        //Code starts here
        //-----------------

        //The button that will be submit the user information
        //and switch the user into the next state.
        Button create_account_button = (Button) findViewById(R.id.id_createAccount);
        create_account_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText edt_firstNameParent = (EditText) findViewById(R.id.id_firstNameParent);
                EditText edt_lastNameParent = (EditText) findViewById(R.id.id_lastNameParent);
                EditText edt_emailParent = (EditText) findViewById(R.id.id_emailParent);
                EditText edt_phoneNumberParent = (EditText) findViewById(R.id.id_phoneNumberParent);
                EditText edt_passwordParent = (EditText) findViewById(R.id.id_passwordParent);
                EditText edt_passwordConfirm = (EditText) findViewById(R.id.id_passwordConfirm);

                String str_firstNameParent = edt_firstNameParent.getText().toString();
                String str_lastNameParent = edt_lastNameParent.getText().toString();
                String str_emailParent = edt_emailParent.getText().toString();
                String str_phoneNumberParent = edt_phoneNumberParent.getText().toString();
                String str_passwordParent = edt_passwordParent.getText().toString();
                String str_passwordConfirm = edt_passwordConfirm.getText().toString();

                //Make sure email is a valid email format.
                if(!EmailValidator.getInstance().isValid(str_emailParent))
                {
                    //TODO: Give User an error message then return. We probably want to build out a string of multiple errors and then return them all at once.
                    return;
                }
                //TODO: Need additional validations (no empty strings allowed, only numbers for phone number, passwordParent and passwordConfirm must match, etc.)

                String fileName = "ParentAppAccount.txt";
                try
                {
                    File root = new File(Environment.getExternalStorageDirectory()+File.separator+"ParentApp", "Accounts");
                    //File root = new File(Environment.getExternalStorageDirectory(), "Notes");
                    if (!root.exists())
                    {
                        root.mkdirs();
                    }
                    File accountFile = new File(root, fileName);

                    FileWriter writer = new FileWriter(accountFile,true);
                    String str_accountString = "First Name:" + str_firstNameParent + ";Last Name:"
                            + str_lastNameParent + ";Email:" + str_emailParent + ";Phone Number:"
                            + str_phoneNumberParent + ";Password:" + str_passwordParent + "\n";
                    writer.append(str_accountString);
                    writer.flush();
                    writer.close();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }

                Intent intent = new Intent(CreateAccount.this, Login2.class);
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
}