package com.example.baselineapp;
import android.app.Application;
import android.content.Intent;
import android.app.NotificationChannel;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;

import com.example.baselineapp.ui.dashboard.DashboardFragment;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.LinkedList;

public final class Globals extends Application
{
    public Globals(){}


    //---------------------------------------------------------
    //TCP Connection data
    private static Boolean bool_isConnected  = false;

    private static Boolean bool_sendShutdownCommand = false;

    private static Boolean bool_sendPowerEnableCommand = false;

    //---------------------------------------------------------
    //UDP Connection data

    private static Boolean bool_udpIsConnected = false;


    //---------------------------------------------------------
    //Instance data
    private static Boolean bool_loggedIn;
    private static Intent NotificationService;
    private static Intent TcpServerService;
    private static Intent UdpServerService;


    //---------------------------------------------------------
    //Baby State Data

    private static boolean bool_warningActive = false;
    private static boolean bool_cautionActive = false;

    //---------------------------------------------------------
    //Baby Vitals Data
    private static double dbl_bloodOxVal;
    private static String str_bloodOxUnit = "%";
    private static double dbl_tempVal;
    private static String str_tempUnit = "F";
    private static double dbl_pulseVal;
    private static String str_pulseUnit = "bpm";

    //---------------------------------------------------------
    //Baby Metadata
    private static String str_babyFirstName;
    private static DatePicker dp_birthdate;

    //---------------------------------------------------------
    //Baby Threshold Data

    private static double dbl_bloodOxLowWarningThreshold = 0;
    private static double dbl_bloodOxLowCautionThreshold = 0;

    private static double dbl_pulseLowWarningThreshold = 0;
    private static double dbl_pulseLowCautionThreshold = 0;
    private static double dbl_pulseHighWarningThreshold = 0;
    private static double dbl_pulseHighCautionThreshold = 0;

    private static double dbl_tempLowWarningThreshold = 0;
    private static double dbl_tempLowCautionThreshold = 0;
    private static double dbl_tempHighWarningThreshold = 0;
    private static double dbl_tempHighCautionThreshold = 0;

    //---------------------------------------------------------
    //File data
    private static HashMap<String, String> map;

    //---------------------------------------------------------
    //Notifications
    private static LinkedList<Notification> notifications = new LinkedList<Notification>();

    //---------------------------------------------------------
    //Security
    private static String str_code;

    //---------------------------------------------------------
    //FUNCTIONS
    //---------------------------------------------------------
    //TCP Connection Information

    public static Boolean getShutdownCommandStatus() {
        return bool_sendShutdownCommand;
    }

    public static void sendShutdownCommand(Boolean sendShutdownCommand) {
        Globals.bool_sendShutdownCommand = sendShutdownCommand;
    }

    public static Boolean getPowerEnableStatus() {
        return bool_sendPowerEnableCommand;
    }

    public static void sendPowerEnableCommand(Boolean sendPowerEnableCommand) {
        Globals.bool_sendPowerEnableCommand = sendPowerEnableCommand;
    }

    public static Boolean getClientIsConnected() {
        return bool_isConnected;
    }

    public static void setClientIsConnected(Boolean isConnected) {
        Globals.bool_isConnected = isConnected;
    }

    //---------------------------------------------------------
    //UDP Connection Information


    public static Boolean getUDPIsConnected() {
        return bool_udpIsConnected;
    }

    public static void setUDPIsConnected(Boolean bool_udpIsConnected) {
        Globals.bool_udpIsConnected = bool_udpIsConnected;
    }

    //---------------------------------------------------------
    //Instance Information
    public static Boolean userLoggedIn() {
        return bool_loggedIn;
    }

    public static void setLoggedIn(Boolean loggedIn) {
        bool_loggedIn = loggedIn;
    }
    public static Intent getNotificationService() {
        return NotificationService;
    }
    public static Intent getTCPServerService() {
        return TcpServerService;
    }
    public static void setTCPServerService(Intent tcpServerService) {
        TcpServerService = tcpServerService;
    }

    public static void setNotificationService(Intent notificationService) {
        NotificationService = notificationService;
    }

    public static Intent getUdpServerService() {
        return UdpServerService;
    }

    public static void setUdpServerService(Intent udpServerService) {
        UdpServerService = udpServerService;
    }

    //---------------------------------------------------------
    //Baby Vital Data

    public static double getBloodOxVal() {return dbl_bloodOxVal;}
    public static String getBloodOxUnit() {return str_bloodOxUnit;}

    public static double getTempVal() {return dbl_tempVal;}
    public static String getTempUnit() {return str_tempUnit;}

    public static double getPulseVal() {return dbl_pulseVal;}

    public static void setBloodOxVal(double bloodOxVal) {
        Globals.dbl_bloodOxVal = bloodOxVal;
    }

    public static void setTempVal(double tempVal) {
        Globals.dbl_tempVal = tempVal;
    }

    public static void setPulseVal(double pulseVal) {
        Globals.dbl_pulseVal = pulseVal;
    }
    public static String getPulseUnit() {return str_pulseUnit;}
    public static void debugOnlySetVitals(double bloodOx, double pulse, double temp)
    {
        dbl_bloodOxVal = bloodOx;
        dbl_pulseVal = pulse;
        dbl_tempVal = temp;
    }

    //---------------------------------------------------------
    //Baby Metadata
    public static String getBabyFirstName() {
        return str_babyFirstName;
    }

    public static void setBabyFirstName(String str_babyFirstName) {
        Globals.str_babyFirstName = str_babyFirstName;
    }

    public static DatePicker getBirthdate() {
        return dp_birthdate;
    }

    public static void setBirthdate(DatePicker birthdate) {
        dp_birthdate = birthdate;
    }

    //---------------------------------------------------------
    //Baby State Information
    public static boolean isWarningActive() {
        return bool_warningActive;
    }

    public static void setWarningActive(boolean warningActive) {
        bool_warningActive = warningActive;
    }

    public static boolean isCautionActive() {
        return bool_cautionActive;
    }

    public static void setCautionActive(boolean cautionActive) {
        bool_cautionActive = cautionActive;
    }

    //---------------------------------------------------------
    //Baby Threshold Information
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

    //---------------------------------------------------------
    //File Information
    /*
         List of Keys that will appear in map:
            Email
            Full Name
            First Name
            Last Name
            Phone Number
            Password
            Baby Birthday
            Baby First Name
            Notification 1 Title
            Notification 1 Body
            Notification 2 Title
            Notification 2 Body
            ...
            Notification 10 Title
            Notification 10 Body
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
        dbl_bloodOxVal = 0.0;
        str_bloodOxUnit = "%";
        dbl_tempVal = 0.0;
        str_tempUnit = "F";
        dbl_pulseVal = 0.0;
        str_pulseUnit = "bpm";
        //Replace all occurrences of $ with \n for notifications.
        for(int i = 1; i <= 10; i++)
        {
            String title = Globals.getMap().get("Notification " + i + " Title").replace('$', '\n');
            String body = Globals.getMap().get("Notification " + i + " Body").replace('$', '\n');
            Globals.getMap().put("Notification " + i + " Title", title);
            Globals.getMap().put("Notification " + i + " Body", body);
            //Adds notification to the front of the LL
            addNotificationInitial(title, body);
        }
    }

    public static HashMap<String,String> getMap() {return map;}

    //---------------------------------------------------------
    //Notifications
    public static LinkedList<Notification> getNotifications() {return notifications;}

    public static void addNotification(String title, String body, Context c)
    {
        if(map == null)
        {
            map = new HashMap<>();
        }

        //Adds notification to the front of the LL
        notifications.addFirst(new Notification(title, body));

        //We only maintain 10 notifications at a time, max
        if(notifications.size() >= 10)
        {
            //Remove the oldest notification
            notifications.removeLast();
        }
        int i = 1;
        for(Notification notif:notifications)
        {
            map.put("Notification " + i + " Title", notif.getTitle());
            map.put("Notification " + i + " Body", notif.getBody());
            i++;
        }
        ReaderWriter rw = new ReaderWriter();
        rw.writeDataToTextFile(c, Globals.getMap());
    }

    public static void addNotificationInitial(String title, String body)
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

    //---------------------------------------------------------
    //Security
    public static String getCode() {
        return str_code;
    }
    public static void setCode(String code) {
        str_code = code;
    }
}