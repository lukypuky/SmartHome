package com.example.smarthome.connection;

import com.google.gson.annotations.SerializedName;

public class Users
{
    @SerializedName("id")
    private final int userId;

    @SerializedName("username")
    private final String userName;

    @SerializedName("email")
    private final String userEmail;

    @SerializedName("password")
    private final String userPassword;

    @SerializedName("role")
    private final int userRole;

    @SerializedName("id_household")
    private final int userHouseholdId;

    @SerializedName("status")
    private int userStatus;

    public Users(int userId, String userName, String userEmail, String userPassword, int userRole, int userHouseholdId)
    {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userRole = userRole;
        this.userHouseholdId = userHouseholdId;
    }

    public int getUserId() {
        return userId;
    }

    public int getUserRole() {
        return userRole;
    }

    public int getUserHouseholdId() {
        return userHouseholdId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public Integer getUserStatus()
    {
        return userStatus;
    }
}
