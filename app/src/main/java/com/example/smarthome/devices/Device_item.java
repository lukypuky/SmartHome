package com.example.smarthome.devices;

import java.io.Serializable;

public class Device_item extends android.app.Activity implements Serializable
{
    private int imageResource;
    private String deviceName, deviceType;
    private int deviceId, isActive, isConnectedImage, intensity, humidity, isOn, connectivity, deviceRoomId;
    private float temperature;

    public Device_item()
    {

    }

    public Device_item(String deviceType)
    {
        this.deviceType = deviceType;
    }

    public Device_item(String deviceName, int deviceId, String deviceType, int deviceRoomId)
    {
        this.deviceName = deviceName;
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.deviceRoomId = deviceRoomId;
    }

    public Device_item(int imageResource, String deviceName, String deviceType, int deviceId, int isOn, int connectivity, int isActive, int isConnectedImage, int intensity, int humidity, float temperature)
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

    @Override
    public String toString()
    {
        return deviceName;
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

    public float getTemperature()
    {
        return temperature;
    }

    public int getDeviceRoomId()
    {
        return deviceRoomId;
    }
}