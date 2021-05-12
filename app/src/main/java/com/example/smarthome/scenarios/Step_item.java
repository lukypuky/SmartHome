package com.example.smarthome.scenarios;

public class Step_item extends android.app.Activity
{
    private final String stepName;
    private final int id_step, id_scenario, id_device, id_room, isOn, isActive, humidity, intensity;
    private final float temperature;

    public Step_item(int id_step, String stepName, int id_scenario, int id_device, int id_room, int isOn, int isActive, int humidity, float temperature, int intensity)
    {
        this.id_step = id_step;
        this.stepName = stepName;
        this.id_scenario = id_scenario;
        this.id_device = id_device;
        this.id_room = id_room;
        this.isOn = isOn;
        this.isActive = isActive;
        this.humidity = humidity;
        this.temperature = temperature;
        this.intensity = intensity;
    }

    public int getId_step()
    {
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

    public int getId_room() {
        return id_room;
    }

    public int getIsOn()
    {
        return isOn;
    }

    public int getIsActive()
    {
        return isActive;
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
}