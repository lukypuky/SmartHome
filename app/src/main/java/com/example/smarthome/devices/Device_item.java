package com.example.smarthome.devices;

public class Device_item extends android.app.Activity
{
    private int imageResource;
    private String deviceName, deviceType;
    private int deviceId, isActive, isActiveImage;

    public Device_item()
    {

    }

    public Device_item(int imageResource, String deviceName, String deviceType, int deviceId, int isActive, int isActiveImage)
    {
        this.imageResource = imageResource;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.deviceId = deviceId;
        this.isActive = isActive;
        this.isActiveImage = isActiveImage;
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

    public int getIsActive()
    {
        return isActive;
    }

    public int getIsActiveImage()
    {
        return isActiveImage;
    }
}