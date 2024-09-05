package com.example.baselineapp;
import android.app.Application;
import java.util.HashMap;

public final class Globals extends Application
{
    private static String code;
    private static double bloodOxVal;
    private static String bloodOxUnit;
    private static double tempVal;
    private static String tempUnit;
    private static double pulseVal;
    private static String pulseUnit;
    private static HashMap<String, String> map;


    private Globals(String profile)
    {
    }

    /*
         List of Keys that will appear in map:
            Email
            Full Name
            First Name
            Last Name
            Phone Number
            Password
    */
    public static void setInitialValues(String profile)
    {
        map = new HashMap<>();
        int beginIndex = 0;
        while(profile.indexOf(';') != -1)
        {
            int endIndex = profile.indexOf(':');
            //Iterate through profile string. Split off each key and each value. Add key-value pair to map.
            String key = profile.substring(beginIndex, endIndex);
            profile = profile.substring(endIndex+1);
            endIndex = profile.indexOf(';');
            String value = profile.substring(beginIndex, endIndex);
            profile = profile.substring(endIndex+1);
            map.put(key, value);
        }
        bloodOxVal = 0.0;
        bloodOxUnit = "%";
        tempVal = 0.0;
        tempUnit = "F";
        pulseVal = 0.0;
        pulseUnit = "bpm";
    }

    public static String getCode() {
        return code;
    }

    public static void setCode(String c) {
        code = c;
    }

    public static double getBloodOxVal() {return bloodOxVal;}
    public static String getBloodOxUnit() {return bloodOxUnit;}
    public static double getTempVal() {return tempVal;}
    public static String getTempUnit() {return tempUnit;}
    public static double getPulseVal() {return pulseVal;}
    public static String getPulseUnit() {return pulseUnit;}
    public static HashMap<String,String> getMap() {return map;}
}