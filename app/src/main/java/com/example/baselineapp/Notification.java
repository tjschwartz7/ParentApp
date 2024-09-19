package com.example.baselineapp;

public class Notification
{
    String title;
    String body;

    public Notification(String title, String body)
    {
        this.title = title;
        this.body = body;
    }

    public String getTitle(){return title;}
    public String getBody(){return body;}
}
