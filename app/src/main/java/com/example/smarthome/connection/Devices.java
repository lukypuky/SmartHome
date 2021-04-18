package com.example.smarthome.connection;

import com.google.gson.annotations.SerializedName;

public class Devices
{
    @SerializedName("id")
    private int deviceId;

    @SerializedName("type")
    private String deviceType;

    @SerializedName("name")
    private String deviceName;

    @SerializedName("id_room")
    private int idRoom;

    @SerializedName("connectivity")
    private int connectivity;

    @SerializedName("status_isOn")
    private int isOn;

    @SerializedName("status_isActive")
    private int is_active;

    @SerializedName("status_intensity")
    private int intensity;

    @SerializedName("status_humidity")
    private int humidity;

    @SerializedName("status_temperature")
    private double temperature;

    @SerializedName("status")
    private int deviceStatus;

    public Integer getDeviceId()
    {
        return deviceId;
    }

    public String getDeviceType()
    {
        return deviceType;
    }

    public String getDeviceName()
    {
        return deviceName;
    }

    public int getIdRoom()
    {
        return idRoom;
    }

    public int getConnectivity()
    {
        return connectivity;
    }

    public int getIsOn()
    {
        return isOn;
    }

    public int getIs_active()
    {
        return is_active;
    }

    public int getIntensity()
    {
        return intensity;
    }

    public int getHumidity()
    {
        return humidity;
    }

    public double getTemperature()
    {
        return temperature;
    }

    public int getDeviceStatus()
    {
        return deviceStatus;
    }


}


