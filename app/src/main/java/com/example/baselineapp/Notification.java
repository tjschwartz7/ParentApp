package com.example.baselineapp;

public class Notification
{
    String title;
    String body;
    String dateAndTime;

    public Notification(String title, String body, String dateAndTime)
    {
        // Print the components
        this.title = title;
        this.body = body;
        this.dateAndTime = dateAndTime;
    }

    public String getTitle(){return title;}
    public String getBody(){return body;}
    public String getDateAndTime(){return dateAndTime;}
}
