package com.example.smarthome.main;

public class Device_item extends android.app.Activity
{
    private int mimageResource;
    private String mtext;

    public Device_item()
    {

    }

    public Device_item(int imageResource, String text)
    {
        mimageResource = imageResource;
        mtext = text;
    }

    public int getImageResource()
    {
        return mimageResource;
    }

    public String getText()
    {
        return mtext;
    }
}