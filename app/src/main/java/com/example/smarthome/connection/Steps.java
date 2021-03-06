package com.example.smarthome.connection;

import com.google.gson.annotations.SerializedName;

public class Steps
{
    @SerializedName("id")
    private int id_step;

    @SerializedName("name")
    private String stepName;

    @SerializedName("id_scenar")
    private int id_scenario;

    @SerializedName("deviceId")
    private int id_device;

    @SerializedName("id_room")
    private int id_room;

    @SerializedName("status_isOn")
    private int isOn;

    @SerializedName("status_isActive")
    private int is_active;

    @SerializedName("status_humidity")
    private int humidity;

    @SerializedName("status_temperature")
    private float temperature;

    @SerializedName("status_intensity")
    private int intensity;

    @SerializedName("status")
    private int status;

//    public Steps(String stepName)
//    {
//        this.stepName = stepName;
//    }

    public int getId_step() {
        return id_step;
    }

    public String getStepName()
    {
        return stepName;
    }

    public int getId_scenario()
    {
        return id_scenario;
    }

    public int getId_device()
    {
        return id_device;
    }

    public int getId_room()
    {
        return id_room;
    }

    public int getIsOn()
    {
        return isOn;
    }

    public int getIs_active()
    {
        return is_active;
    }

    public int getHumidity()
    {
        return humidity;
    }

    public float getTemperature()
    {
        return temperature;
    }

    public int getIntensity()
    {
        return intensity;
    }

    public int getStatus()
    {
        return status;
    }
}
