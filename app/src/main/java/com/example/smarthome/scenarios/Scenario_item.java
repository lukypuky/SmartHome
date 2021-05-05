package com.example.smarthome.scenarios;

public class Scenario_item extends android.app.Activity
{
    private int mImageResource;
    private String scenarioName;
    private int scenarioId;

    public Scenario_item()
    {

    }

    public Scenario_item(int scenarioId)
    {
        this.scenarioId = scenarioId;
    }

    public Scenario_item(int imageResource, String scenarioName, int scenarioId)
    {
        this.mImageResource = imageResource;
        this.scenarioName = scenarioName;
        this.scenarioId = scenarioId;
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
}
