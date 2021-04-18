package com.example.smarthome.connection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.smarthome.main.Room_adapter;
import com.example.smarthome.main.Room_item;
import com.example.smarthome.settings.Dark_mode;

public class SessionManagement
{
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";

    //login
    String SESSION_LOGIN_USER_ID = "user_id";
    String SESSION_LOGIN_NAME = "user_name";
    String SESSION_LOGIN_EMAIL = "user_email";
    String SESSION_LOGIN_HOUSEHOLD_ID = "household_id";
    String SESSION_LOGIN_HOUSEHOLD_NAME = "household_name";
    String SESSION_LOGIN_ROLE = "user_role";

    //dark mode
    String SESSION_DARK_MODE = "dark_mode";

    //room_item;
    String SESSION_ROOM_NAME = "room_name";
    String SESSION_ROOM_TYPE = "room_type";
    String SESSION_ROOM_ID = "id_room";

    @SuppressLint("CommitPrefEdits")
    public SessionManagement(Context context)
    {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveLoginSession(Login login)
    {
        int userId = login.getUserId();
        String userName = login.getUsername();
        String userEmail = login.getUserEmail();
        int householdId = login.getHouseholdId();
        String householdName = login.getHouseholdName();
        int userRole = login.getRole();

        editor.putInt(SESSION_LOGIN_USER_ID, userId).commit();
        editor.putString(SESSION_LOGIN_NAME, userName).commit();
        editor.putString(SESSION_LOGIN_EMAIL, userEmail).commit();
        editor.putInt(SESSION_LOGIN_HOUSEHOLD_ID, householdId).commit();
        editor.putString(SESSION_LOGIN_HOUSEHOLD_NAME, householdName).commit();
        editor.putInt(SESSION_LOGIN_ROLE, userRole).commit();
    }

    public void saveRoomSession(Room_item ri)
    {
        String room_name = ri.getRoomName();
        int room_type = ri.getIntRoomType();
        int room_id = ri.getId_room();

        editor.putString(SESSION_ROOM_NAME,room_name).commit();
        editor.putInt(SESSION_ROOM_TYPE, room_type).commit();
        editor.putInt(SESSION_ROOM_ID, room_id).commit();
    }

    public void saveDarkModeSession(Dark_mode dark_mode)
    {
        boolean darkMode = dark_mode.isDark_mode();

        editor.putBoolean(SESSION_DARK_MODE, darkMode).commit();
    }

    public Login getLoginSession()
    {
        int loginUserId = sharedPreferences.getInt(SESSION_LOGIN_USER_ID,0);
        String loginUserName = sharedPreferences.getString(SESSION_LOGIN_NAME,"");
        String loginEmail = sharedPreferences.getString(SESSION_LOGIN_EMAIL,"");
        int loginHouseholdId = sharedPreferences.getInt(SESSION_LOGIN_HOUSEHOLD_ID,0);
        String loginHouseholdName = sharedPreferences.getString(SESSION_LOGIN_HOUSEHOLD_NAME,"");
        int loginRole = sharedPreferences.getInt(SESSION_LOGIN_ROLE,0);

        return new Login(loginUserId, loginUserName, loginEmail, loginHouseholdId, loginHouseholdName, loginRole );
    }

    public Room_item getRoomSession()
    {
        String roomItemName = sharedPreferences.getString(SESSION_ROOM_NAME, "");
        int roomItemType = sharedPreferences.getInt(SESSION_ROOM_TYPE, 0);
        int roomItemId = sharedPreferences.getInt(SESSION_ROOM_ID, 0);

        return new Room_item(roomItemName, roomItemType, roomItemId);
    }

    public Dark_mode getDarkModeSession()
    {
        boolean dark_mode = sharedPreferences.getBoolean(SESSION_DARK_MODE, false);
        return new Dark_mode(dark_mode);
    }

    public int getSession()
    {
        //vrat pouzivatela ktory ma ulozenu session
        return sharedPreferences.getInt(SESSION_LOGIN_USER_ID, 0);
    }

    public void removeSession()
    {
        editor.putInt(SESSION_LOGIN_USER_ID, 0).commit();
    }
}
