package com.example.smarthome.connection;

import com.google.gson.annotations.SerializedName;

public class Login
{
    @SerializedName("status")
    private int status;

    @SerializedName("info")
    private String info;

    @SerializedName("role")
    private  int role;

    @SerializedName("username")
    private String username;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("id_household")
    private int householdId;

    @SerializedName("household_name")
    private String householdName;

    public Login(int status, int role, String username, int userId, int householdId, String householdName)
    {
        this.status = status;
        this.role = role;
        this.username = username;
        this.userId = userId;
        this.householdId = householdId;
        this.householdName = householdName;
    }

    public int getStatus()
    {
        return status;
    }

    public String getInfo()
    {
        return info;
    }

    public int getRole()
    {
        return role;
    }

    public int getHouseholdId()
    {
        return householdId;
    }

    public String getHouseholdName()
    {
        return householdName;
    }

    public String getUsername()
    {
        return username;
    }

    public int getUserId()
    {
        return userId;
    }
}
