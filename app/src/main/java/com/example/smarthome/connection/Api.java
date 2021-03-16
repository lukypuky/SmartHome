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
    Call<Users> getUser(@Query("id") int userId,
                        @Query("id_household") int id_household
    );

    //getne vsetky miestnosti na zaklade ID domacnosti
    @Headers({"auth-key: d4e2ad09-b1c3-4d70-9a9a-0e6149302486"})
    @GET("rooms.php")
    Call<List<Rooms>> getRooms(@Query("id_household") int id_household);

//    //getne 1 miestnost na zaklade ID domacnosti
//    @Headers({"auth-key: d4e2ad09-b1c3-4d70-9a9a-0e6149302486"})
//    @GET("rooms.php")
//    Call<Rooms> getRoom(@Query("id_household") int id_household);

    //insert miestnosti do DB
    @FormUrlEncoded
    @Headers({"auth-key: d4e2ad09-b1c3-4d70-9a9a-0e6149302486"})
    @POST("rooms.php")
    Call<Rooms> postRoom(
            @Field("name") String roomName,
            @Field("type") String roomTyp,
            @Field("id_household") int id_household
    );

    //mazanie izieb
    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "rooms.php", hasBody = true)
    @Headers({"auth-key: d4e2ad09-b1c3-4d70-9a9a-0e6149302486"})
    Call<Void> deleteRoom(
            @Field("id") int id_room,
            @Field("id_household") int id_household
    );

    //prihlasenie
    @FormUrlEncoded
    @Headers({"auth-key: d4e2ad09-b1c3-4d70-9a9a-0e6149302486"})
    @POST("login.php")
    Call<Login> loginUser(
            @Field("username") String userName,
            @Field("password") String password
    );

    //pridanie domacnosti do DB pri registracii
//    @FormUrlEncoded
//    @Headers({"auth-key: d4e2ad09-b1c3-4d70-9a9a-0e6149302486"})
//    @POST("rooms.php")
//    Call<Rooms> postHousehold(
//            @Field("name") String householdName
//    );

    //pridanie usera a do DB pri registracii
//    @FormUrlEncoded
//    @Headers({"auth-key: d4e2ad09-b1c3-4d70-9a9a-0e6149302486"})
//    @POST("users.php")
//    Call<Rooms> postUser(
//            @Field("username") String roomName,
//            @Field("email") String roomTyp,
//            @Field("password") String password,
//            @Field("role") int role,
//            @Field("id_household") int id_household
//    );
}
