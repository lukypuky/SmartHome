package com.example.smarthome.main;

public class Room_item extends android.app.Activity
{
    private int imageResource;
    private String roomName, roomType;
    private int  id_household, id_room, intRoomType;

    public Room_item()
    {

    }

    public Room_item(String roomName, int intRoomType, int id_room)
    {
        this.roomName = roomName;
        this.intRoomType = intRoomType;
        this.id_room = id_room;
    }

    public Room_item(int imageResource, String roomName, String roomType, int id_household, int id_room)
    {
        this.imageResource = imageResource;
        this.roomName = roomName;
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

    public int getIntRoomType()
    {
        return intRoomType;
    }
}
