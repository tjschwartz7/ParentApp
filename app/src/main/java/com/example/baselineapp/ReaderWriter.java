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

    public void writeDataToTextFile(Context c, HashMap<String, String> writeMap)
    {
        FileInputStream fis = null;
        try
        {
            fis = c.openFileInput("AccountData.txt");
        }
        catch (FileNotFoundException e)
        {
            try (FileOutputStream fos = c.openFileOutput("AccountData.txt", Context.MODE_PRIVATE))
            {
                fos.write("".getBytes());
                fis = c.openFileInput("AccountData.txt");
            }
            catch(IOException a)
            {
                a.printStackTrace();
                throw new RuntimeException();
            }
        }

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
                ";Baby Birthday:" + Globals.getMap().get("Baby Birthday") +
                ";Baby First Name:" + Globals.getMap().get("Baby First Name");

        for(int i = 1; i <= 10; i++)
        {
            newData += ";Notification " + i + " Title:" + Globals.getMap().get("Notification " + i + " Title").replace('\n', '$') +
                    ";Notification " + i + " Body:" + Globals.getMap().get("Notification " + i + " Body").replace('\n', '$');
        }
        newData += ";\n";

        List<String> lines = new ArrayList<>();

        String email = Globals.getMap().get("Email");

        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        try (BufferedReader reader = new BufferedReader(isr)) {
            String line = reader.readLine();
            while (line != null)
            {
                line = line + "\n";
                int beginIndex = line.indexOf(':') + 1;
                int endIndex = line.indexOf(';');
                //Email address should be first in the string/line.
                String storedEmail = line.substring(beginIndex, endIndex);
                if(storedEmail.equals(email))
                {
                    //Overwrite the line here
                    line = newData;
                    found = true;
                    //If we have already added a line, move this line to the top to indicate that it is the signed in account.
                    if(!lines.isEmpty())
                    {
                        lines.add(lines.get(0));
                        lines.set(0, line);
                        line = reader.readLine();
                        continue;
                    }
                }
                lines.add(line);
                line = reader.readLine();
            }
            lines.add(line);

            //We only need to write if we find the account data.
            // This SHOULD always hit, since the only time this is going to be called is after the user is signed in.
            if(found)
            {
                try (FileOutputStream fos = c.openFileOutput("AccountData.txt", Context.MODE_PRIVATE))
                {
                    for(String l: lines)
                    {
                        if(l != null)
                        {
                            //Write each line back to the text file.
                            fos.write(l.getBytes());
                        }
                    }
                }
            }
        }
        catch (IOException e)
        {
            // Error occurred when opening raw file for reading.
            e.printStackTrace();
        }
    }

    public void readTextFileAndInitiallyPopulateGlobals(Context c)
    {
        FileInputStream fis = null;
        try
        {
            fis = c.openFileInput("AccountData.txt");
        }
        catch (FileNotFoundException e)
        {
            try (FileOutputStream fos = c.openFileOutput("AccountData.txt", Context.MODE_APPEND))
            {
                fos.write("".getBytes());
                fis = c.openFileInput("AccountData.txt");
            }
            catch(IOException a)
            {
                a.printStackTrace();
                throw new RuntimeException();
            }
        }
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        try (BufferedReader reader = new BufferedReader(isr))
        {
            String line = reader.readLine();
            if (line != null)
            {
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
            e.printStackTrace();
        }
    }

    public boolean readTextFileAndPopulateGlobals(Context c)
    {
        boolean success = false;

        String email = Globals.getMap().get("Email");

        FileInputStream fis = null;
        try
        {
            fis = c.openFileInput("AccountData.txt");
        }
        catch (FileNotFoundException e)
        {
            try (FileOutputStream fos = c.openFileOutput("AccountData.txt", Context.MODE_PRIVATE))
            {
                fos.write("".getBytes());
                fis = c.openFileInput("AccountData.txt");
            }
            catch(IOException a)
            {
                a.printStackTrace();
                throw new RuntimeException();
            }
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
            e.printStackTrace();
        }
        return success;
    }

    public boolean scanTextFileForEmail(Context c, String str_email)
    {
        boolean found = false;

        FileInputStream fis = null;
        try
        {
            fis = c.openFileInput("AccountData.txt");
        }
        catch (FileNotFoundException e)
        {
            try (FileOutputStream fos = c.openFileOutput("AccountData.txt", Context.MODE_PRIVATE))
            {
                fos.write("".getBytes());
                fis = c.openFileInput("AccountData.txt");
            }
            catch(IOException a)
            {
                a.printStackTrace();
                throw new RuntimeException();
            }
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
                if (storedEmail.equals(str_email))
                {
                    found = true;
                    break;
                }
                line = reader.readLine();
            }
        }
        catch (IOException e)
        {
            // Error occurred when opening raw file for reading.
            e.printStackTrace();
        }
        return found;
    }

    public void testPrintTextFile(Context c)
    {
        FileInputStream fis = null;
        try
        {
            fis = c.openFileInput("AccountData.txt");
        }
        catch (FileNotFoundException e)
        {
            try (FileOutputStream fos = c.openFileOutput("AccountData.txt", Context.MODE_PRIVATE))
            {
                fos.write("".getBytes());
                fis = c.openFileInput("AccountData.txt");
            }
            catch(IOException a)
            {
                a.printStackTrace();
                throw new RuntimeException();
            }
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
            e.printStackTrace();
        }
    }
}
