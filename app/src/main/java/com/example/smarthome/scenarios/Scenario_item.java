package com.example.smarthome.scenarios;

public class Scenario_item extends android.app.Activity
{
    private int mImageResource;
    private String mtext;

    public Scenario_item()
    {

    }

    public Scenario_item(int imageResource, String text)
    {
        mImageResource = imageResource;
        mtext = text;
    }

    public int getImageResource()
    {
        return mImageResource;
    }

    public String getText()
    {
        return mtext;
    }
}
