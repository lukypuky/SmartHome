package com.example.smarthome.scenarios;

public class Step_item extends android.app.Activity
{
    private final String stepName;

    public Step_item(String stepName)
    {
        this.stepName = stepName;
    }

    public String getStepName()
    {
        return stepName;
    }
}
