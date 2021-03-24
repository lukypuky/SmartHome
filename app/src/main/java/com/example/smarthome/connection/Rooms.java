package com.example.smarthome.connection;

import com.google.gson.annotations.SerializedName;

public class Rooms
{
    @SerializedName("id")
    private Integer roomId;

    @SerializedName("name")
    private String roomName;

    @SerializedName("type")
    private String roomType;

    @SerializedName("id_household")
    private Integer idHousehold;

    @SerializedName("status")
    private Integer roomStatus;

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

    public Integer getRoomStatus()
    {
        return roomStatus;
    }
}
