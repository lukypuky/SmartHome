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

    public void setRoomId(int roomId)
    {
        this.roomId = roomId;
    }

    public void setRoomName(String roomName)
    {
        this.roomName = roomName;
    }

    public void setRoomType(String roomType)
    {
        this.roomType = roomType;
    }

    public void setIdHousehold(int idHousehold)
    {
        this.idHousehold = idHousehold;
    }
}
