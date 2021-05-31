package com.example.smarthome.connection;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api
{
    @Headers({"auth-key: d4e2ad09-b1c3-4d70-9a9a-0e6149302486"})
    @GET("users.php")
    Call<List<Users>> getUser(
            @Query("id") int userId,
            @Query("id_household") int id_household
    );

    @Headers({"auth-key: d4e2ad09-b1c3-4d70-9a9a-0e6149302486"})
    @GET("users.php")
    Call<List<Users>> getUsers(
            @Query("id_household") int id_household
    );

    //edit profilu
    @FormUrlEncoded
    @HTTP(method = "PUT", path = "users.php", hasBody = true)
    @Headers({"auth-key: d4e2ad09-b1c3-4d70-9a9a-0e6149302486"})
    Call<Users> editProfile(
            @Field("id") int id_user,
            @Field("username") String userName,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("password") String password,
            @Field("role") int role,
            @Field("id_household") int id_household
    );

    //vrati vsetky miestnosti na zaklade ID domacnosti
    @Headers({"auth-key: d4e2ad09-b1c3-4d70-9a9a-0e6149302486"})
    @GET("rooms.php")
    Call<List<Rooms>> getRooms(
            @Query("id_household") int id_household);

    //vlozenie miestnosti do databazy
    @FormUrlEncoded
    @Headers({"auth-key: d4e2ad09-b1c3-4d70-9a9a-0e6149302486"})
    @POST("rooms.php")
    Call<Rooms> postRoom(
            @Field("name") String roomName,
            @Field("type") String roomType,
            @Field("id_household") int id_household
    );

    //odstrani miestnost
    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "rooms.php", hasBody = true)
    @Headers({"auth-key: d4e2ad09-b1c3-4d70-9a9a-0e6149302486"})
    Call<Rooms> deleteRoom(
            @Field("id") int id_room,
            @Field("id_household") int id_household
    );

    //edit miestnosti
    @FormUrlEncoded
    @HTTP(method = "PUT", path = "rooms.php", hasBody = true)
    @Headers({"auth-key: d4e2ad09-b1c3-4d70-9a9a-0e6149302486"})
    Call<Rooms> editRoom(
            @Field("id") int id_room,
            @Field("name") String roomName,
            @Field("type") String  roomType,
            @Field("id_household") int id_household
    );

    //prihlasenie
    @FormUrlEncoded
    @HTTP(method = "POST", path = "login.php", hasBody = true)
    @Headers({"auth-key: d4e2ad09-b1c3-4d70-9a9a-0e6149302486"})
    Call<Login> loginUser(
            @Field("email") String userEmail,
            @Field("password") String password
    );

    //registracia
    @FormUrlEncoded
    @Headers({"auth-key: d4e2ad09-b1c3-4d70-9a9a-0e6149302486"})
    @POST("registration.php")
    Call<Registration> postUser(
            @Field("username") String userName,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("household_name") String householdName,
            @Field("password") String password
    );

    //pridanie pouzivatela Adminom
    @FormUrlEncoded
    @Headers({"auth-key: d4e2ad09-b1c3-4d70-9a9a-0e6149302486"})
    @POST("users.php")
    Call<Users> postUserByAdmin(
            @Field("username") String userName,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("password") String password,
            @Field("role") int role,
            @Field("id_household") int id_household
    );

    //getne vsetky devices pre danu miestnost
    @Headers({"auth-key: d4e2ad09-b1c3-4d70-9a9a-0e6149302486"})
    @GET("devices.php")
    Call<List<Devices>> getDevices(
            @Query("id_room") int id_room);

    //getne konkretne device
    @Headers({"auth-key: d4e2ad09-b1c3-4d70-9a9a-0e6149302486"})
    @GET("devices.php")
    Call<List<Devices>> getDevice(
            @Query("id_room") int id_room,
            @Query("id") int id_device
    );

    //insert zariadenia
    @FormUrlEncoded
    @Headers({"auth-key: d4e2ad09-b1c3-4d70-9a9a-0e6149302486"})
    @POST("devices.php")
    Call<Devices> postDevice(
            @Field("type") String deviceType,
            @Field("name") String deviceName,
            @Field("id_room") int id_room,
            @Field("status_isOn") int isOn,
            @Field("status_intensity") int intensity,
            @Field("status_isActive") int isActive,
            @Field("status_humidity") int humidity,
            @Field("status_temperature") float temperature,
            @Field("connectivity") int connectivity,
            @Field("notified") int notified
    );

    //delete zariadenia
    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "devices.php", hasBody = true)
    @Headers({"auth-key: d4e2ad09-b1c3-4d70-9a9a-0e6149302486"})
    Call<Void> deleteDevice(
            @Field("id") int id_device,
            @Field("id_room") int id_room
    );

    //edit zariadenia
    @FormUrlEncoded
    @HTTP(method = "PUT", path = "devices.php", hasBody = true)
    @Headers({"auth-key: d4e2ad09-b1c3-4d70-9a9a-0e6149302486"})
    Call<Devices> editDevice(
            @Field("id") int id_device,
            @Field("type") String deviceType,
            @Field("name") String deviceName,
            @Field("id_room") int id_room,
            @Field("status_isOn") int isOn,
            @Field("status_isActive") int isActive,
            @Field("status_intensity") int intensity,
            @Field("status_humidity") int humidity,
            @Field("status_temperature") float temperature,
            @Field("connectivity") int connectivity
    );

    //getne vsetky scenare pre danu domacnost
    @Headers({"auth-key: d4e2ad09-b1c3-4d70-9a9a-0e6149302486"})
    @GET("scenario.php")
    Call<List<Scenarios>> getScenarios(
            @Query("id_household") int id_household);

    //getne vsetky miestnosti s connected sensors
    @Headers({"auth-key: d4e2ad09-b1c3-4d70-9a9a-0e6149302486"})
    @GET("scenario.php")
    Call<List<Rooms>> getRoomsWithSensors(
            @Query("id_household") int id_household,
            @Query("sensors") int sensors
    );

    //getne vsetky connected senzory z danej roomky
    @Headers({"auth-key: d4e2ad09-b1c3-4d70-9a9a-0e6149302486"})
    @GET("scenario.php")
    Call<List<Devices>> getSensors(
            @Query("id_household") int id_household,
            @Query("id_room") int id_room,
            @Query("sensors") int sensors
    );

    //insert scenario
    @FormUrlEncoded
    @Headers({"auth-key: d4e2ad09-b1c3-4d70-9a9a-0e6149302486"})
    @POST("scenario.php")
    Call<Scenarios> postScenario(
            @Field("name") String scenarioName,
            @Field("executingType") String executingType,
            @Field("id_room") int id_room,
            @Field("sensorId") String id_sensor,
            @Field("isExecutable") int isExecutable,
            @Field("isRunning") int isRunning,
            @Field("value") String status,
            @Field("time") String time,
            @Field("id_household") int id_household
    );

    //edit scenara
    @FormUrlEncoded
    @HTTP(method = "PUT", path = "scenario.php", hasBody = true)
    @Headers({"auth-key: d4e2ad09-b1c3-4d70-9a9a-0e6149302486"})
    Call<Scenarios> editScenario(
            @Field("id") int id_scenario,
            @Field("name") String scenarioName,
            @Field("executingType") String executingType,
            @Field("id_room") int id_room,
            @Field("sensorId") String id_sensor,
            @Field("isExecutable") int isExecutable,
            @Field("isRunning") int isRunning,
            @Field("value") String status,
            @Field("time") String time,
            @Field("id_household") int id_household
    );

    //delete scenara
    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "scenario.php", hasBody = true)
    @Headers({"auth-key: d4e2ad09-b1c3-4d70-9a9a-0e6149302486"})
    Call<Scenarios> deleteScenario(
            @Field("id") int id_step,
            @Field("id_household") int id_household
    );

    //get steps na zakalde scenara
    @Headers({"auth-key: d4e2ad09-b1c3-4d70-9a9a-0e6149302486"})
    @GET("steps.php")
    Call<List<Steps>> getSteps(
            @Query("id_scenar") int id_scenario,
            @Query("id_household") int id_household
    );

    //insert step
    @FormUrlEncoded
    @Headers({"auth-key: d4e2ad09-b1c3-4d70-9a9a-0e6149302486"})
    @POST("steps.php")
    Call<Steps> postSteps(
            @Field("name") String stepName,
            @Field("id_scenar") int id_scenario,
            @Field("deviceId") String id_device,
            @Field("id_room") int id_room,
            @Field("status_isOn") int isOn,
            @Field("status_isActive") int isActive,
            @Field("status_humidity") int humidity,
            @Field("status_temperature") float temperature,
            @Field("status_intensity") int intensity,
            @Field("id_household") int id_household
    );

    //edit stepu
    @FormUrlEncoded
    @HTTP(method = "PUT", path = "steps.php", hasBody = true)
    @Headers({"auth-key: d4e2ad09-b1c3-4d70-9a9a-0e6149302486"})
    Call<Steps> editSteps(
            @Field("id") int id_step,
            @Field("name") String stepName,
            @Field("id_scenar") int id_scenario,
            @Field("deviceId") String id_device,
            @Field("id_room") int id_room,
            @Field("status_isOn") int isOn,
            @Field("status_isActive") int isActive,
            @Field("status_humidity") int humidity,
            @Field("status_temperature") float temperature,
            @Field("status_intensity") int intensity,
            @Field("id_household") int id_household
    );

    //delete stepu
    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "steps.php", hasBody = true)
    @Headers({"auth-key: d4e2ad09-b1c3-4d70-9a9a-0e6149302486"})
    Call<Void> deleteStep(
            @Field("id") int id_step,
            @Field("id_scenar") int id_scenar,
            @Field("id_household") int id_household
    );
}
