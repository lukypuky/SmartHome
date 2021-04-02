package com.example.smarthome.devices;

public class Device_item extends android.app.Activity
{
    private int imageResource;
    private String deviceName, deviceType;
    private int deviceId, isActive, isActiveImage, intensity, humidity, isOn;
    private double temperature;

    public Device_item()
    {

    }

    public Device_item(int imageResource, String deviceName, String deviceType, int deviceId, int isOn, int isActive, int isActiveImage, int intensity, int humidity, double temperature)
    {
        this.imageResource = imageResource;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.deviceId = deviceId;
        this.isOn = isOn;
        this.isActive = isActive;
        this.isActiveImage = isActiveImage;
        this.intensity = intensity;
        this.humidity = humidity;
        this.temperature = temperature;
    }

    public int getImageResource()
    {
        return imageResource;
    }

    public String getDeviceName()
    {
        return deviceName;
    }

    public String getDeviceType()
    {
        return deviceType;
    }

    public int getDeviceId()
    {
        return deviceId;
    }

    public int getIsOn()
    {
        return isOn;
    }

    public int getIsActive()
    {
        return isActive;
    }

    public int getIsActiveImage()
    {
        return isActiveImage;
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
}