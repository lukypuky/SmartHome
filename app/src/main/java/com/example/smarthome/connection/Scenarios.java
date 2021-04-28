package com.example.smarthome.connection;

import com.google.gson.annotations.SerializedName;

public class Scenarios
{
    @SerializedName("id")
    private int scenarioID;

    @SerializedName("name")
    private String scenarioName;

    @SerializedName("executingType")
    private String executingType;

    @SerializedName("sensorId")
    private String sensorId;

    @SerializedName("isRunning")
    private int isRunning;

    @SerializedName("status")
    private String status;

    @SerializedName("time")
    private String time;

    @SerializedName("id_household")
    private int householdId;

    public int getScenarioID()
    {
        return scenarioID;
    }

    public String getScenarioName()
    {
        return scenarioName;
    }

    public String getExecutingType()
    {
        return executingType;
    }

    public String getSensorId()
    {
        return sensorId;
    }

    public int getIsRunning()
    {
        return isRunning;
    }

    public String getStatus()
    {
        return status;
    }

    public String getTime()
    {
        return time;
    }
}
