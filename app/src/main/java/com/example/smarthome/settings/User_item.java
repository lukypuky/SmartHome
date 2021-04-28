package com.example.smarthome.settings;

public class User_item
{
    private final int settingUserId;
    private final String settingsUserName;
    private final String settingsUserEmail;
    private final String settingsUserPassword;
    private final int settingsUserRole;

    public User_item(int settingUserId,String settingsUserName, String settingsUserEmail, String settingsUserPassword, int settingsUserRole)
    {
        this.settingUserId = settingUserId;
        this.settingsUserName = settingsUserName;
        this.settingsUserEmail = settingsUserEmail;
        this.settingsUserPassword = settingsUserPassword;
        this.settingsUserRole = settingsUserRole;
    }

    public int getSettingUserId()
    {
        return settingUserId;
    }

    public String getSettingsUserName()
    {
        return settingsUserName;
    }

    public String getSettingsUserEmail()
    {
        return settingsUserEmail;
    }

    public String getSettingsUserPassword()
    {
        return settingsUserPassword;
    }

    public int getSettingsUserRole()
    {
        return settingsUserRole;
    }
}
