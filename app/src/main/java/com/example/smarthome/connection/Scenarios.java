package com.example.smarthome.connection;

import com.google.gson.annotations.SerializedName;

public class Scenarios
{
    @SerializedName("id")
    private int scenarioId;

    @SerializedName("name")
    private String scenarioName;

    @SerializedName("executingType")
    private String executingType;

    @SerializedName("sensorId")
    private String sensorId;

    @SerializedName("isExecutable")
    private int isExecutable;

    @SerializedName("id_room")
    private int id_room;

    @SerializedName("isRunning")
    private int isRunning;

    @SerializedName("value")
    private String scenarioValue;

    @SerializedName("time")
    private String time;

    @SerializedName("id_household")
    private int householdId;

    @SerializedName("info")
    private String info;

    @SerializedName("scenar_id")
    private int scenar_id;

    @SerializedName("status")
    private int status;

    public int getScenarioId()
    {
        return scenarioId;
    }

    public String getScenarioName()
    {
        return scenarioName;
    }

    public String getExecutingType()
    {
        return executingType;
    }

    public int getId_room()
    {
        return id_room;
    }

    public String getSensorId()
    {
        return sensorId;
    }

    public int getIsExecutable()
    {
        return isExecutable;
    }

    public int getIsRunning()
    {
        return isRunning;
    }

    public String getScenarioValue()
    {
        return scenarioValue;
    }

    public String getTime()
    {
        return time;
    }

    public String getInfo()
    {
        return info;
    }

    public int getScenar_id()
    {
        return scenar_id;
    }

    public int getStatus()
    {
        return status;
    }
}
