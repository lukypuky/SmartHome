package com.example.smarthome.devices;

public class Device_item extends android.app.Activity
{
    private int imageResource;
    private String deviceName, deviceType;
    private int deviceId, isActive, isConnectedImage, intensity, humidity, isOn, connectivity;
    private double temperature;

    public Device_item()
    {

    }

    public Device_item(int imageResource, String deviceName, String deviceType, int deviceId, int isOn, int connectivity, int isActive, int isConnectedImage, int intensity, int humidity, double temperature)
    {
        this.imageResource = imageResource;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.deviceId = deviceId;
        this.isOn = isOn;
        this.connectivity = connectivity;
        this.isActive = isActive;
        this.isConnectedImage = isConnectedImage;
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

    public int getConnectivity()
    {
        return connectivity;
    }

    public int getIsActive()
    {
        return isActive;
    }

    public int getIsConnectedImage()
    {
        return isConnectedImage;
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