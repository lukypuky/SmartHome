package com.example.smarthome.main;

public class Room_item extends android.app.Activity
{
    private int imageResource;
    private String roomName, roomType;
    private int  id_household, id_room;

    public Room_item()
    {

    }

    public Room_item(int imageResource, String text1, int id_household)
    {
        this.imageResource = imageResource;
        this.roomName = text1;
        this.id_household = id_household;
    }

    public Room_item(int imageResource, String text1, String roomType, int id_household, int id_room)
    {
        this.imageResource = imageResource;
        this.roomName = text1;
        this.roomType = roomType;
        this.id_household = id_household;
        this.id_room = id_room;
    }

    public int getImageResource()
    {
        return imageResource;
    }

    public String getRoomName()
    {
        return roomName;
    }

    public String getRoomType()
    {
        return roomType;
    }

    public int getId_household()
    {
        return id_household;
    }

    public int getId_room()
    {
        return id_room;
    }
}
