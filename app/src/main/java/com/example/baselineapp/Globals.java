package com.example.baselineapp;
import android.app.Application;
import android.content.Intent;
import android.widget.DatePicker;

import java.util.HashMap;

import java.util.LinkedList;

public final class Globals extends Application
{
    private static double bloodOxVal;
    private static String bloodOxUnit;
    private static double tempVal;
    private static String tempUnit;
    private static double pulseVal;
    private static String pulseUnit;
    private static HashMap<String, String> map;

    public static Intent getNotificationService() {
        return NotificationService;
    }

    public static void setNotificationService(Intent notificationService) {
        NotificationService = notificationService;
    }

    private static Intent NotificationService;

    private static LinkedList<Notification> notifications = new LinkedList<Notification>();
    private static String str_code;

    private static boolean bool_warningActive = false;
    private static boolean bool_cautionActive = false;

    private static double dbl_bloodOxVal = 94;
    private static String str_bloodOxUnit = "%";
    private static double dbl_tempVal = 98.7;
    private static String str_tempUnit = "F";
    private static double dbl_pulseVal = 120;
    private static String str_pulseUnit = "bpm";

    private static double dbl_bloodOxLowWarningThreshold = 88;
    private static double dbl_bloodOxLowCautionThreshold = 90;

    private static double dbl_pulseLowWarningThreshold = 65;
    private static double dbl_pulseLowCautionThreshold = 70;
    private static double dbl_pulseHighWarningThreshold = 195;
    private static double dbl_pulseHighCautionThreshold = 190;

    private static double dbl_tempLowWarningThreshold = 95;
    private static double dbl_tempLowCautionThreshold = 96.8;
    private static double dbl_tempHighWarningThreshold = 101;
    private static double dbl_tempHighCautionThreshold = 99.5;

    private static DatePicker dp_birthdate;

    public static String getBabyFirstName() {
        return str_babyFirstName;
    }

    public static void setBabyFirstName(String str_babyFirstName) {
        Globals.str_babyFirstName = str_babyFirstName;
    }

    private static String str_babyFirstName;

    public static DatePicker getBirthdate() {
        return dp_birthdate;
    }

    public static void setBirthdate(DatePicker birthdate) {
        dp_birthdate = birthdate;
    }

    public static boolean isWarningActive() {
        return bool_warningActive;
    }

    public static void setWarningActive(boolean warningActive) {
        bool_warningActive = warningActive;
    }

    public Globals(){}
    public Globals(String profile){}

    public static boolean isCautionActive() {
        return bool_cautionActive;
    }

    public static void setCautionActive(boolean cautionActive) {
        bool_cautionActive = cautionActive;
    }
    public static String getCode() {
        return str_code;
    }

    public static double getBloodOxLowWarningThreshold() {
        return Globals.dbl_bloodOxLowWarningThreshold;
    }

    public static void setBloodOxLowWarningThreshold(double dbl_bloodOxLowWarningThreshold) {
        Globals.dbl_bloodOxLowWarningThreshold = dbl_bloodOxLowWarningThreshold;
    }

    public static double getBloodOxLowCautionThreshold() {
        return dbl_bloodOxLowCautionThreshold;
    }

    public static void setBloodOxLowCautionThreshold(double dbl_bloodOxLowCautionThreshold) {
        Globals.dbl_bloodOxLowCautionThreshold = dbl_bloodOxLowCautionThreshold;
    }

    public static double getPulseLowWarningThreshold() {
        return dbl_pulseLowWarningThreshold;
    }

    public static void setPulseLowWarningThreshold(double dbl_pulseLowWarningThreshold) {
        Globals.dbl_pulseLowWarningThreshold = dbl_pulseLowWarningThreshold;
    }

    public static double getPulseLowCautionThreshold() {
        return dbl_pulseLowCautionThreshold;
    }

    public static void setPulseLowCautionThreshold(double dbl_pulseLowCautionThreshold) {
        Globals.dbl_pulseLowCautionThreshold = dbl_pulseLowCautionThreshold;
    }

    public static double getPulseHighWarningThreshold() {
        return dbl_pulseHighWarningThreshold;
    }

    public static void setPulseHighWarningThreshold(double dbl_pulseHighWarningThreshold) {
        Globals.dbl_pulseHighWarningThreshold = dbl_pulseHighWarningThreshold;
    }

    public static double getPulseHighCautionThreshold() {
        return dbl_pulseHighCautionThreshold;
    }

    public static void setPulseHighCautionThreshold(double dbl_pulseHighCautionThreshold) {
        Globals.dbl_pulseHighCautionThreshold = dbl_pulseHighCautionThreshold;
    }

    public static double getTempLowWarningThreshold() {
        return dbl_tempLowWarningThreshold;
    }

    public static void setTempLowWarningThreshold(double dbl_tempLowWarningThreshold) {
        Globals.dbl_tempLowWarningThreshold = dbl_tempLowWarningThreshold;
    }

    public static double getTempLowCautionThreshold() {
        return dbl_tempLowCautionThreshold;
    }

    public static void setTempLowCautionThreshold(double dbl_tempLowCautionThreshold) {
        Globals.dbl_tempLowCautionThreshold = dbl_tempLowCautionThreshold;
    }

    public static double getTempHighWarningThreshold() {
        return dbl_tempHighWarningThreshold;
    }

    public static void setTempHighWarningThreshold(double dbl_tempHighWarningThreshold) {
        Globals.dbl_tempHighWarningThreshold = dbl_tempHighWarningThreshold;
    }

    public static double getTempHighCautionThreshold() {
        return dbl_tempHighCautionThreshold;
    }

    public static void setTempHighCautionThreshold(double dbl_tempHighCautionThreshold) {
        Globals.dbl_tempHighCautionThreshold = dbl_tempHighCautionThreshold;
    }

    public static void setCode(String code) {
        str_code = code;
    }

    public static double getBloodOxVal() {return dbl_bloodOxVal;}
    public static String getBloodOxUnit() {return str_bloodOxUnit;}

    public static double getTempVal() {return dbl_tempVal;}
    public static String getTempUnit() {return str_tempUnit;}

    public static double getPulseVal() {return dbl_pulseVal;}

    public static void debugOnlySetVitals(double bloodOx, double pulse, double temp)
    {
        dbl_bloodOxVal = bloodOx;
        dbl_pulseVal = pulse;
        dbl_tempVal = temp;
    }

    /*
         List of Keys that will appear in map:
            Email
            Full Name
            First Name
            Last Name
            Phone Number
            Password
            Baby Birthday
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

    public static String getPulseUnit() {return str_pulseUnit;}

    public static LinkedList<Notification> getNotifications() {return notifications;}

    public static void addNotification(String title, String body)
    {

        //Adds notification to the front of the LL
        notifications.addFirst(new Notification(title, body));

        //We only maintain 10 notifications at a time, max
        if(notifications.size() >= 10)
        {
            //Remove the oldest notification
            notifications.removeLast();
        }

    }

    public static String getNotificationString(int index)
    {
        return notifications.get(index).title + "\n" + notifications.get(index).body + "\n";
    }


    public static int getNumNotifications() {return notifications.size();}

    public static void setWarningThresholds(                     double temp_high, double pulse_high,
                                     double bloodOx_low,  double temp_low,  double pulse_low)
    {
        setBloodOxLowWarningThreshold(bloodOx_low);
        setTempLowWarningThreshold(temp_high);
        setTempLowWarningThreshold(temp_low);
        setPulseHighWarningThreshold(pulse_high);
        setPulseLowWarningThreshold(pulse_low);
    }

    public static HashMap<String,String> getMap() {return map;}
    public static void setCautionThresholds(                     double temp_high, double pulse_high,
                                     double bloodOx_low,  double temp_low,  double pulse_low)
    {
        setBloodOxLowCautionThreshold(bloodOx_low);
        setTempLowCautionThreshold(temp_high);
        setTempLowCautionThreshold(temp_low);
        setPulseHighCautionThreshold(pulse_high);
        setPulseLowCautionThreshold(pulse_low);
    }
}