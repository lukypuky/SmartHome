package com.example.smarthome.scenarios;

public class Scenario_item extends android.app.Activity
{
    private int mimageResource;
    private String mtext;

    public Scenario_item()
    {

    }

    public Scenario_item(int imageResource, String text)
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
