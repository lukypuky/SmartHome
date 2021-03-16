package com.example.smarthome.connection;

import com.google.gson.annotations.SerializedName;

public class Login
{
    @SerializedName("status")
    private final int status;

    @SerializedName("role")
    private final int role;

    @SerializedName("username")
    private final String username;

    @SerializedName("user_id")
    private final int userId;

    @SerializedName("id_household")
    private final int householdId;

    @SerializedName("household_name")
    private final String householdName;

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
