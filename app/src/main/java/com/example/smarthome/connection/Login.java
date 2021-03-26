package com.example.smarthome.connection;

import com.google.gson.annotations.SerializedName;

public class Login
{
    @SerializedName("status")
    private  int status;

    @SerializedName("role")
    private int role;

    @SerializedName("username")
    private String username;

    @SerializedName("user_id")
    private final int userId;

    @SerializedName("email")
    private String userEmail;

    @SerializedName("id_household")
    private final int householdId;

    @SerializedName("household_name")
    private final String householdName;

    public Login(int userId, String username, String userEmail, int householdId, String householdName, int role)
    {
        this.userId = userId;
        this.username = username;
        this.userEmail = userEmail;
        this.householdId = householdId;
        this.householdName = householdName;
        this.role = role;
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

    public String getUserEmail()
    {
        return userEmail;
    }
}
