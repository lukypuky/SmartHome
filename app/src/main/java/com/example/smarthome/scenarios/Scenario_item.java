package com.example.smarthome.scenarios;

public class Scenario_item extends android.app.Activity
{
    private int mImageResource;
    private String mtext;
    private int scenarioId;

    public Scenario_item()
    {

    }

    public Scenario_item(int scenarioId)
    {
        this.scenarioId = scenarioId;
    }

    public Scenario_item(int imageResource, String text, int scenarioId)
    {
        this.mImageResource = imageResource;
        this.mtext = text;
        this.scenarioId = scenarioId;
    }

    public int getImageResource()
    {
        return mImageResource;
    }

    public String getText()
    {
        return mtext;
    }

    public int getScenarioId()
    {
        return scenarioId;
    }
}
