package com.example.smarthome.connection;

import com.google.gson.annotations.SerializedName;

public class Post
{
    @SerializedName("id")
    private int userId;

    @SerializedName("username")
    private String userName;

    @SerializedName("email")
    private  String userEmail;

    @SerializedName("password")
    private String userPassword;

    @SerializedName("role")
    private int userRole;

    @SerializedName("id_household")
    private int userHouseholdId;


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
}
