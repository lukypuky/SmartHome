package com.example.smarthome.connection;

import com.google.gson.annotations.SerializedName;

public class Registration
{
    @SerializedName("status")
    private int status;

    public int getStatus()
    {
        return status;
    }
}
