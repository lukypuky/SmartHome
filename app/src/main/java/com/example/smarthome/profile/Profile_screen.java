package com.example.smarthome.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.smarthome.connection.Api;
import com.example.smarthome.connection.Login;
import com.example.smarthome.connection.SessionManagement;
import com.example.smarthome.connection.Users;
import com.example.smarthome.login.Login_screen;
import com.example.smarthome.main.Main_screen;
import com.example.smarthome.R;
import com.example.smarthome.scenarios.Scenario_screen;
import com.example.smarthome.settings.Settings_screen;
import com.google.android.material.navigation.NavigationView;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Profile_screen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    //menu
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView headerHousehold, headerUsername;

    //udaje o userovi
    private TextView userName, householdName, email, role;
    private String stringUserName, stringEmail;
    private int intRole;
    private List<Users> users;

    //api
    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);

        apiConnection();

        //session
        SessionManagement sessionManagement = new SessionManagement(Profile_screen.this);
        Login login =  sessionManagement.getLoginSession();

        //DOCASNE/////////////////////////////////////////////////////////////////
        householdName = findViewById(R.id.profileHome);
        householdName.setText(login.getHouseholdName());
        //DOCASNE/////////////////////////////////////////////////////////////////

        System.out.println("BEFORE API CONNECT " + stringUserName);
        getUserInfo(login);
        System.out.println("AFTER API CONNECT " + stringUserName);
//        setScreenValues();
        System.out.println("AFTER SET VALUES " + stringUserName);
        setNavigationView(login);
    }

    //pripojenie sa na api
    public void apiConnection()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://147.175.121.237/api2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(Api.class);
    }

    //bocny navigacny panel
    public void setNavigationView(Login login)
    {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        //nastavenie nazvu domacnosti a pouzivatela v headri menu
        View headerView = navigationView.getHeaderView(0);

        headerHousehold = headerView.findViewById(R.id.headerHousehold);
        headerUsername = headerView.findViewById(R.id.headerUserName);

        headerHousehold.setText(login.getHouseholdName());
        headerUsername.setText(login.getUsername());

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //umozni nam klikat v menu
        navigationView.setNavigationItemSelectedListener(this);

        //pri spusteni appky bude zakliknuta defaultne main screena
        navigationView.setCheckedItem(R.id.profile);
    }

    //pri pouziti tlacidla "Back" alebo po pouziti gesta na vratenie spat, sa zasunie menu, miesto toho aby sa vratilo a obrazovku spat
    @Override
    public void onBackPressed()
    {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer((GravityCompat.START));
        }

        else
        {
            super.onBackPressed();
        }
    }

    //prepinanie medzi obrazovkami v menu
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.mainScreen:
                Intent main_intent = new Intent(Profile_screen.this, Main_screen.class);
                startActivity(main_intent);
                break;
            case R.id.profile:
                break;
            case R.id.scenario:
                Intent scenario_intent = new Intent(Profile_screen.this, Scenario_screen.class);
                startActivity(scenario_intent);
                break;
            case R.id.settings:
                Intent settings_intent = new Intent(Profile_screen.this, Settings_screen.class);
                startActivity(settings_intent);
                break;
            case R.id.logout:
                logout();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //nastavi hodnoty, ktore sa menia podla prihlaseneho usera
    public void setScreenValues()
    {
        userName = findViewById(R.id.profileName);
        userName.setText(stringUserName);

        //GET HOUSEHOLD
//        householdName = findViewById(R.id.profileHome);
//        householdName.setText();

        email = findViewById(R.id.profileEmail);
        email.setText(stringEmail);

        role = findViewById(R.id.profileRole);
        role.setText(getRole());
    }

    //getne zvysne info o pouzivatelovi, ktore sme nedostali pri logine
    public void getUserInfo(Login login)
    {
        int tmpUserId = login.getUserId();
        int tmpHomeId = login.getHouseholdId();

        System.out.println("1 " + stringUserName);
        Call<List<Users>> call = api.getUser(tmpUserId, tmpHomeId);
        System.out.println("2 " + stringUserName);
        call.enqueue(new Callback<List<Users>>()
        {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response)
            {
                System.out.println("3 " + stringUserName);
                if (!response.isSuccessful())
                {
                    System.out.println("call = " + call + ", response = " + response);
                    return;
                }
                System.out.println("4 " + stringUserName);
                users = response.body();

                for (Users user: users)
                {
                    stringUserName = user.getUserName();
                    stringEmail = user.getUserEmail();
                    intRole = user.getUserRole();

                    setScreenValues();
                    System.out.println("5 " + stringUserName);
                }

                System.out.println("6 " + stringUserName);
            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t)
            {
                System.out.println("call = " + call + ", t = " + t);
            }
        });

        System.out.println("7 " + stringUserName);
    }

    //get role z id_role
    public String getRole()
    {
        if (intRole == 1)
            return "Admin";

        else
            return "Bežný používateľ";
    }

    //odhlasenie sa z aplikacie (zrusenie session)
    public void logout()
    {
        SessionManagement sessionManagement = new SessionManagement(Profile_screen.this);
        sessionManagement.removeSession();

        moveToLoginScreen();
    }

    public void moveToLoginScreen()
    {
        Intent intent = new Intent(Profile_screen.this, Login_screen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
