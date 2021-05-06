package com.example.smarthome.scenarios;

public class Scenario_item extends android.app.Activity
{
    private int mImageResource;
    private String scenarioName, scenarioType;
    private int scenarioId, scenarioExecutable;

    public Scenario_item()
    {

    }

    public Scenario_item(int scenarioId)
    {
        this.scenarioId = scenarioId;
    }

    public Scenario_item(int imageResource, String scenarioName, int scenarioId, String scenarioType, int scenarioExecutable)
    {
        this.mImageResource = imageResource;
        this.scenarioName = scenarioName;
        this.scenarioId = scenarioId;
        this.scenarioType = scenarioType;
        this.scenarioExecutable = scenarioExecutable;
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

    public int getScenarioExecutable()
    {
        return scenarioExecutable;
    }
}
