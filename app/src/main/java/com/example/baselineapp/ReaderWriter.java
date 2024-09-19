package com.example.baselineapp;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.example.baselineapp.Globals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReaderWriter
{
    public ReaderWriter()
    {

    }

    public boolean writeDataToTextFile(String path, HashMap<String, String> writeMap)
    {
        boolean success = false;
        boolean found = false;

        writeMap.forEach((key, value) ->
        {
            Globals.getMap().replace(key, value);
        });

        String newData = "Email:" + Globals.getMap().get("Email") +
                ";Full Name:" + Globals.getMap().get("Full Name") +
                ";First Name:" + Globals.getMap().get("First Name") +
                ";Last Name:" + Globals.getMap().get("Last Name") +
                ";Phone Number:" + Globals.getMap().get("Phone Number") +
                ";Password:" + Globals.getMap().get("Password") +
                ";Baby Birthday:" + Globals.getMap().get("Baby Birthday");

        for(int i = 1; i <= 10; i++)
        {
            newData += ";Notification " + i + " Title:" + Globals.getMap().get("Notification " + i + " Title").replace('\n', '$') +
                    ";Notification " + i + " Body:" + Globals.getMap().get("Notification " + i + " Body").replace('\n', '$');
        }
        newData += ";\n";

        List<String> lines = new ArrayList<>();

        String email = Globals.getMap().get("Email");

        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(path);
            //fis = v.getContext().openFileInput("AccountData.txt");
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        try (BufferedReader reader = new BufferedReader(isr)) {
            int i = 0;
            String line = reader.readLine();
            while (line != null)
            {
                int beginIndex = line.indexOf(':') + 1;
                int endIndex = line.indexOf(';');
                //Email address should be first in the string/line.
                String storedEmail = line.substring(beginIndex, endIndex);
                if(storedEmail.equals(email))
                {
                    //Overwrite the line here
                    line = newData;
                    found = true;
                }
                i++;
                lines.add(line);
                line = reader.readLine();
            }
            lines.add(line);
            if(found)
            {
                success = true;
            }
            else
            {
                try(FileOutputStream fos = new FileOutputStream(path, false))
                //try (FileOutputStream fos = v.getContext().openFileOutput("AccountData.txt", Context.MODE_PRIVATE))
                {
                    for(String l: lines)
                    {
                        //Write each line back to the text file.
                        fos.write(l.getBytes());
                    }
                }
            }
        }
        catch (IOException e)
        {
            // Error occurred when opening raw file for reading.
        }
        return success;
    }

    //TODO: CHECK WRITE FUNCTIONALITY!!! MAKE SURE FIRST LINE IS CURRENTLY SIGNED IN ACCOUNT!!!
    public boolean readTextFileAndInitiallyPopulateGlobals(String path)
    {
        boolean success = false;

        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(path);
            //fis = v.getContext().openFileInput("AccountData.txt");
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        try (BufferedReader reader = new BufferedReader(isr))
        {
            String line = reader.readLine();
            if (line != null)
            {
                line = reader.readLine();
                success = true;
                Globals.setInitialValues(line);
            }
            else
            {
                //Display error message?
            }
        }
        catch (IOException e)
        {
            // Error occurred when opening raw file for reading.
        }
        return success;
    }

    public boolean readTextFileAndPopulateGlobals(String path)
    {
        boolean success = false;

        String email = Globals.getMap().get("Email");

        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(path);
            //fis = v.getContext().openFileInput("AccountData.txt");
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        try (BufferedReader reader = new BufferedReader(isr))
        {
            String line = reader.readLine();
            while (line != null)
            {
                int beginIndex = line.indexOf(':') + 1;
                int endIndex = line.indexOf(';');
                //Email address should be first in the string/line.
                String storedEmail = line.substring(beginIndex, endIndex);
                if (storedEmail.equals(email))
                {
                    break;
                }
                line = reader.readLine();
            }
            if (line != null)
            {
                success = true;
                Globals.setInitialValues(line);
            }
            else
            {
                //Display error message?
            }
        }
        catch (IOException e)
        {
            // Error occurred when opening raw file for reading.
        }
        return success;
    }

    public void testPrintTextFile(String path)
    {
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(path);
            //fis = v.getContext().openFileInput("AccountData.txt");
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        try (BufferedReader reader = new BufferedReader(isr))
        {
            String line = reader.readLine();
            while (line != null)
            {
                Log.d("TEXT FILE LINE", line);
                line = reader.readLine();
            }
        }
        catch (IOException e)
        {
            // Error occurred when opening raw file for reading.
        }
    }
}
