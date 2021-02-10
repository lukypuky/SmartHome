package com.example.smarthome.schemas;

public class SchemaItem
{
    private int mimageResource;
    private String mtext;

    public SchemaItem(int imageResource, String text)
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
