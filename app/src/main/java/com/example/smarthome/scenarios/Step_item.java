package com.example.smarthome.scenarios;

public class Step_item extends android.app.Activity
{
    private final String stepName;
    private final int id_step;

    public Step_item(int id_step, String stepName)
    {
        this.id_step = id_step;
        this.stepName = stepName;
    }

    public int getId_step()
    {
        return id_step;
    }

    public String getStepName()
    {
        return stepName;
    }
}
