package com.example.smarthome.main;

public class RoomItem
{
    private int mimageResource;
    private String mtext1, mtext2;

    public RoomItem(int imageResource, String text1, String text2)
    {
        mimageResource = imageResource;
        mtext1 = text1;
        mtext2 = text2;
    }

    public int getImageResource()
    {
        return mimageResource;
    }

    public String getText1()
    {
        return mtext1;
    }

    public String getText2()
    {
        return mtext2;
    }

}
