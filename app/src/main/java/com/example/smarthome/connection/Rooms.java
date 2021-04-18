package com.example.smarthome.connection;

import com.google.gson.annotations.SerializedName;

public class Rooms
{
    @SerializedName("id")
    private int roomId;

    @SerializedName("name")
    private final String roomName;

    @SerializedName("type")
    private final String roomType;

    @SerializedName("id_household")
    private final int idHousehold;

    @SerializedName("status")
    private int roomStatus;

    public Rooms(String roomName, String roomType, int idHousehold)
    {
        this.roomName = roomName;
        this.roomType = roomType;
        this. idHousehold = idHousehold;
    }

    public int getRoomId()
    {
        return roomId;
    }

    public String getRoomName()
    {
        return roomName;
    }

    public String getRoomType()
    {
        return roomType;
    }

    public int getIdHousehold()
    {
        return idHousehold;
    }

    public int getRoomStatus()
    {
        return roomStatus;
    }
}
