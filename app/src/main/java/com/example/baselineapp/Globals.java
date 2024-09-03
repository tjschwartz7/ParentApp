package com.example.baselineapp;
import android.app.Application;

public class Globals extends Application
{

    private String code;

    private double bloodOxVal;
    private String bloodOxUnit;
    private double tempVal;
    private String tempUnit;
    private double pulseVal;
    private String pulseUnit;


    Globals()
    {
        bloodOxUnit = "%";
        tempUnit = "F";
        pulseUnit = "bpm";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getBloodOxVal() {return bloodOxVal;}
    public String getBloodOxUnit() {return bloodOxUnit;}

    public double getTempVal() {return tempVal;}
    public String getTempUnit() {return tempUnit;}

    public double getPulseVal() {return pulseVal;}

    public String getPulseUnit() {return pulseUnit;}
}