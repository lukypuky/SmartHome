package com.example.smarthome.scenarios;

public class Scenario_item extends android.app.Activity
{
    private int mImageResource;
    private String scenarioName, scenarioType, sensorId, value, time;
    private int scenarioId, scenarioExecutable, isRunning, id_room;;

    public Scenario_item()
    {

    }

    public Scenario_item(int scenarioId)
    {
        this.scenarioId = scenarioId;
    }

    public Scenario_item(int scenarioId, String scenarioName, String scenarioType, int id_room, String sensorId, int scenarioExecutable, int isRunning, String value, String time)
    {
        this.scenarioId = scenarioId;
        this.scenarioName = scenarioName;
        this.scenarioType = scenarioType;
        this.id_room = id_room;
        this.sensorId = sensorId;
        this.scenarioExecutable = scenarioExecutable;
        this.isRunning = isRunning;
        this.value = value;
        this.time = time;
    }

    public Scenario_item(int imageResource, int scenarioId, String scenarioName, String scenarioType, int id_room, String sensorId, int scenarioExecutable, int isRunning, String status, String time)
    {
        this.mImageResource = imageResource;
        this.scenarioId = scenarioId;
        this.scenarioName = scenarioName;
        this.scenarioType = scenarioType;
        this.id_room = id_room;
        this.sensorId = sensorId;
        this.scenarioExecutable = scenarioExecutable;
        this.isRunning = isRunning;
        this.value = status;
        this.time = time;
    }

    public int getImageResource()
    {
        return mImageResource;
    }

    public String getScenarioName()
    {
        return scenarioName;
    }

    public int getScenarioId()
    {
        return scenarioId;
    }

    public String getScenarioType()
    {
        return scenarioType;
    }

    public int getId_room()
    {
        return id_room;
    }

    public int getScenarioExecutable()
    {
        return scenarioExecutable;
    }

    public String getSensorId()
    {
        return sensorId;
    }

    public String getValue()
    {
        return value;
    }

    public String getTime()
    {
        return time;
    }

    public int getIsRunning()
    {
        return isRunning;
    }
}
