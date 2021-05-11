package com.example.smarthome.connection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.smarthome.main.Room_adapter;
import com.example.smarthome.main.Room_item;
import com.example.smarthome.scenarios.Scenario_item;
import com.example.smarthome.settings.Dark_mode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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

    //scenario_item
    String SESSION_SCENARIO_ID = "scenario_id";
    String SESSION_SCENARIO_NAME = "scenario_name";
    String SESSION_SCENARIO_EXECTURING_TYPE = "scenario_executing_type";
    String SESSION_SCENARIO_ROOM_ID = "scenario_room_id";
    String SESSION_SCENARIO_SENSOR_ID = "scenario_sensor_id";
    String SESSION_SCENARIO_IS_EXECUTABLE = "scenario_is_executable";
    String SESSION_SCENARIO_IS_RUNNING = "scenario_is_running";
    String SESSION_SCENARIO_STATUS = "scenario_status";
    String SESSION_SCENARIO_TIME = "scenario_time";

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

    public void saveScenarioSession(Scenario_item si)
    {
        int scenarioId = si.getScenarioId();
        String scenarioName = si.getScenarioName();
        String scenarioExecutingType = si.getScenarioType();
        int scenarioRoomId = si.getId_room();
        String scenarioSensorId = si.getSensorId();
        int scenarioIsExecutable = si.getScenarioExecutable();
        int scenarioIsRunning = si.getIsRunning();
        String scenarioStatus = si.getStatus();
        String scenarioTime = si.getTime();

        editor.putInt(SESSION_SCENARIO_ID, scenarioId).commit();
        editor.putString(SESSION_SCENARIO_NAME, scenarioName).commit();
        editor.putString(SESSION_SCENARIO_EXECTURING_TYPE, scenarioExecutingType).commit();
        editor.putInt(SESSION_SCENARIO_ROOM_ID, scenarioRoomId).commit();
        editor.putString(SESSION_SCENARIO_SENSOR_ID, scenarioSensorId).commit();
        editor.putInt(SESSION_SCENARIO_IS_EXECUTABLE, scenarioIsExecutable).commit();
        editor.putInt(SESSION_SCENARIO_IS_RUNNING, scenarioIsRunning).commit();
        editor.putString(SESSION_SCENARIO_STATUS, scenarioStatus).commit();
        editor.putString(SESSION_SCENARIO_TIME, scenarioTime).commit();
    }

//    public static void saveRoomArrayList(Context context, List<Room_item> ri)
//    {
//        Gson gson = new Gson();
//        String jsonString = gson.toJson(ri);
//
//        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editorPref = pref.edit();
//
//        editorPref.putString("room_list", jsonString);
//        editorPref.apply();
//    }
//
//    public static List<Room_item> getRoomArrayList(Context context)
//    {
//        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
//        String jsonString = pref.getString("room_list", "");
//
//        Gson gson = new Gson();
//        Type type = new TypeToken<ArrayList<Room_item>>() {}.getType();
//        List<Room_item> room = gson.fromJson(jsonString, type);
//
////        ArrayList<Room_item> ri;
////        Gson gson = new Gson();
////        String json = sharedPreferences.getString("room_list", null);
////        Type type = new TypeToken<ArrayList<Room_item>>() {}.getType();
////
////        ri = gson.fromJson(json,type);
//        return room;
//    }

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

    public Scenario_item getScenarioSession()
    {
        int scenarioItemId = sharedPreferences.getInt(SESSION_SCENARIO_ID,0);
        String scenarioItemName = sharedPreferences.getString(SESSION_SCENARIO_NAME, "");
        String scenarioItemType = sharedPreferences.getString(SESSION_SCENARIO_EXECTURING_TYPE, "");
        int scenarioItemRoomId = sharedPreferences.getInt(SESSION_SCENARIO_ROOM_ID,0);
        String scenarioItemSensorId = sharedPreferences.getString(SESSION_SCENARIO_SENSOR_ID, "");
        int scenarioItemIsExecutable = sharedPreferences.getInt(SESSION_SCENARIO_IS_EXECUTABLE,0);
        int scenarioItemIsRunning = sharedPreferences.getInt(SESSION_SCENARIO_IS_RUNNING,0);
        String scenarioItemStatus = sharedPreferences.getString(SESSION_SCENARIO_STATUS,"");
        String scenarioItemTime = sharedPreferences.getString(SESSION_SCENARIO_TIME,"");

        return new Scenario_item(scenarioItemId, scenarioItemName, scenarioItemType, scenarioItemRoomId, scenarioItemSensorId, scenarioItemIsExecutable, scenarioItemIsRunning, scenarioItemStatus, scenarioItemTime);
    }

    public int getSession()
    {
        //vrat pouzivatela ktory ma ulozenu session
        return sharedPreferences.getInt(SESSION_LOGIN_USER_ID, 0);
    }

    public void removeScenarioSession()
    {
        editor.putInt(SESSION_SCENARIO_ID, 0).commit();
        editor.putString(SESSION_SCENARIO_NAME, "").commit();
        editor.putString(SESSION_SCENARIO_EXECTURING_TYPE, "").commit();
        editor.putString(SESSION_SCENARIO_SENSOR_ID, "").commit();
        editor.putInt(SESSION_SCENARIO_IS_EXECUTABLE, 0).commit();
        editor.putInt(SESSION_SCENARIO_IS_RUNNING, 0).commit();
        editor.putString(SESSION_SCENARIO_STATUS, "").commit();
        editor.putString(SESSION_SCENARIO_TIME, "").commit();
    }

    public void removeSession()
    {
        editor.putInt(SESSION_LOGIN_USER_ID, 0).commit();
    }
}
