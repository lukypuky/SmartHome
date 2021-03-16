package com.example.smarthome.connection;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement
{
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";

    String SESSION_STATUS = "status";
    String SESSION_USER_ID = "user_id";
    String SESSION_ROLE = "role";
    String SESSION_USER_NAME = "user_name";
    String SESSION_HOUSEHOLD_ID = "household_id";
    String SESSION_HOUSEHOLD_NAME = "household_name";

    public SessionManagement(Context context)
    {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(Login login)
    {
        //uloz session pouzivatela, ked sa user prihlasi (ulozi sa status z DB response)
        int status = login.getStatus();
        int role = login.getRole();
        int userId = login.getUserId();
        String userName = login.getUsername();
        int householdId = login.getHouseholdId();
        String householdName = login.getHouseholdName();

        editor.putInt(SESSION_STATUS, status).commit();
        editor.putInt(SESSION_ROLE, role).commit();
        editor.putInt(SESSION_USER_ID, userId).commit();
        editor.putString(SESSION_USER_NAME, userName).commit();
        editor.putInt(SESSION_HOUSEHOLD_ID, householdId).commit();
        editor.putString(SESSION_HOUSEHOLD_NAME, householdName).commit();
    }

    public Login getLoginSession()
    {
        int loginStatus = sharedPreferences.getInt(SESSION_STATUS, 0);
        int loginRole = sharedPreferences.getInt(SESSION_ROLE,0);
        int loginUserId = sharedPreferences.getInt(SESSION_USER_ID,0);
        String loginUserName = sharedPreferences.getString(SESSION_USER_NAME,"");
        int loginHouseholdId = sharedPreferences.getInt(SESSION_HOUSEHOLD_ID,0);
        String loginHouseholdName = sharedPreferences.getString(SESSION_HOUSEHOLD_NAME,"");

        return new Login(loginStatus, loginRole, loginUserName, loginUserId, loginHouseholdId, loginHouseholdName );
    }

    public int getSession()
    {
        //vrat pouzivatela ktory ma ulozenu session
        return sharedPreferences.getInt(SESSION_STATUS, 0);
    }

    public void removeSession()
    {
//        editor.putInt(String.valueOf(SESSION_KEY), 0).commit();
        editor.putInt(SESSION_STATUS, 0).commit();
    }
}
