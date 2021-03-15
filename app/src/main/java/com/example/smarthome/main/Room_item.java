package com.example.smarthome.main;

public class Room_item extends android.app.Activity
{
    private int mimageResource;
    private String roomName;
    private int  id_household, id_room;

    public Room_item()
    {

    }

    public Room_item(int imageResource, String text1, int id_household)
    {
        this.mimageResource = imageResource;
        this.roomName = text1;
        this.id_household = id_household;
    }

    public Room_item(int imageResource, String text1, int id_household, int id_room)
    {
        this.mimageResource = imageResource;
        this.roomName = text1;
        this.id_household = id_household;
        this.id_room = id_room;
    }

    public int getMimageResource()
    {
        return mimageResource;
    }

    public String getRoomName()
    {
        return roomName;
    }

    public int getId_household()
    {
        return id_household;
    }

    public int getId_room()
    {
        return id_room;
    }

    public void setMimageResource(int mimageResource)
    {
        this.mimageResource = mimageResource;
    }

    public void setRoomName(String roomName)
    {
        this.roomName = roomName;
    }
}
