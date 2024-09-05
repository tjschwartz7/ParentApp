package com.example.baselineapp;
import android.app.Application;
import java.util.HashMap;

public final class Globals extends Application
{
    private static HashMap<String, String> map;

    private String str_code;

    private boolean bool_warningActive = false;
    private boolean bool_cautionActive = false;

    private static double dbl_bloodOxVal = 89;
    private static String str_bloodOxUnit = "%";
    private static double dbl_tempVal = 98.7;
    private static String str_tempUnit = "F";
    private static double dbl_pulseVal = 205;
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

    public Globals()
    {
    }

    public boolean isWarningActive() {
        return bool_warningActive;
    }

    public void setWarningActive(boolean bool_warningActive) {
        this.bool_warningActive = bool_warningActive;
    }

    public boolean isCautionActive() {
        return bool_cautionActive;
    }

    public void setCautionActive(boolean bool_cautionActive) {
        this.bool_cautionActive = bool_cautionActive;
    }
    public String getCode() {
        return str_code;
    }

    public double getBloodOxLowWarningThreshold() {
        return dbl_bloodOxLowWarningThreshold;
    }

    public void setBloodOxLowWarningThreshold(double dbl_bloodOxLowWarningThreshold) {
        Globals.dbl_bloodOxLowWarningThreshold = dbl_bloodOxLowWarningThreshold;
    }

    public double getBloodOxLowCautionThreshold() {
        return dbl_bloodOxLowCautionThreshold;
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
    }

    public void setBloodOxLowCautionThreshold(double dbl_bloodOxLowCautionThreshold) {
        Globals.dbl_bloodOxLowCautionThreshold = dbl_bloodOxLowCautionThreshold;
    }

    public double getPulseLowWarningThreshold() {
        return dbl_pulseLowWarningThreshold;
    }

    public void setPulseLowWarningThreshold(double dbl_pulseLowWarningThreshold) {
        Globals.dbl_pulseLowWarningThreshold = dbl_pulseLowWarningThreshold;
    }

    public double getPulseLowCautionThreshold() {
        return dbl_pulseLowCautionThreshold;
    }

    public void setPulseLowCautionThreshold(double dbl_pulseLowCautionThreshold) {
        Globals.dbl_pulseLowCautionThreshold = dbl_pulseLowCautionThreshold;
    }

    public double getPulseHighWarningThreshold() {
        return dbl_pulseHighWarningThreshold;
    }

    public void setPulseHighWarningThreshold(double dbl_pulseHighWarningThreshold) {
        Globals.dbl_pulseHighWarningThreshold = dbl_pulseHighWarningThreshold;
    }

    public double getPulseHighCautionThreshold() {
        return dbl_pulseHighCautionThreshold;
    }

    public void setPulseHighCautionThreshold(double dbl_pulseHighCautionThreshold) {
        Globals.dbl_pulseHighCautionThreshold = dbl_pulseHighCautionThreshold;
    }

    public double getTempLowWarningThreshold() {
        return dbl_tempLowWarningThreshold;
    }

    public void setTempLowWarningThreshold(double dbl_tempLowWarningThreshold) {
        Globals.dbl_tempLowWarningThreshold = dbl_tempLowWarningThreshold;
    }

    public double getTempLowCautionThreshold() {
        return dbl_tempLowCautionThreshold;
    }

    public void setTempLowCautionThreshold(double dbl_tempLowCautionThreshold) {
        Globals.dbl_tempLowCautionThreshold = dbl_tempLowCautionThreshold;
    }

    public double getTempHighWarningThreshold() {
        return dbl_tempHighWarningThreshold;
    }

    public void setTempHighWarningThreshold(double dbl_tempHighWarningThreshold) {
        Globals.dbl_tempHighWarningThreshold = dbl_tempHighWarningThreshold;
    }

    public double getTempHighCautionThreshold() {
        return dbl_tempHighCautionThreshold;
    }

    public void setTempHighCautionThreshold(double dbl_tempHighCautionThreshold) {
        Globals.dbl_tempHighCautionThreshold = dbl_tempHighCautionThreshold;
    }

    public void setCode(String code){
        this.str_code = code;
    }

    public double getBloodOxVal() {return dbl_bloodOxVal;}
    public String getBloodOxUnit() {return str_bloodOxUnit;}
    public double getTempVal() {return dbl_tempVal;}
    public String getTempUnit() {return str_tempUnit;}
    public double getPulseVal() {return dbl_pulseVal;}
    public String getPulseUnit() {return str_pulseUnit;}
    public static HashMap<String,String> getMap() {return map;}
}