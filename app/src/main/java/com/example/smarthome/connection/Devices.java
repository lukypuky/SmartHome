package com.example.smarthome.connection;

import com.google.gson.annotations.SerializedName;

public class Devices
{
    @SerializedName("id")
    private Integer deviceId;

    @SerializedName("type")
    private String deviceType;

    @SerializedName("name")
    private String deviceName;

    @SerializedName("id_room")
    private Integer idRoom;

    @SerializedName("isOn")
    private Integer isOn;

    @SerializedName("status_isActive")
    private Integer is_active;

    @SerializedName("status_intensity")
    private Integer intensity;

    @SerializedName("status_humidity")
    private Integer humidity;

    @SerializedName("status_temperature")
    private double temperature;

    @SerializedName("status")
    private Integer deviceStatus;

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

    public Integer getIdRoom()
    {
        return idRoom;
    }

    public Integer getIsOn()
    {
        return isOn;
    }

    public Integer getIs_active()
    {
        return is_active;
    }

    public Integer getIntensity()
    {
        return intensity;
    }

    public Integer getHumidity()
    {
        return humidity;
    }

    public double getTemperature()
    {
        return temperature;
    }

    public Integer getDeviceStatus()
    {
        return deviceStatus;
    }


}


