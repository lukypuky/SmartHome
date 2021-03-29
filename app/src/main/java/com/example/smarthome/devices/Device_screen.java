package com.example.smarthome.devices;

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

import com.example.smarthome.R;
import com.example.smarthome.connection.Api;
import com.example.smarthome.connection.Login;
import com.example.smarthome.connection.SessionManagement;
import com.example.smarthome.login.Login_screen;
import com.example.smarthome.main.Main_screen;
import com.example.smarthome.main.Room_screen;
import com.example.smarthome.profile.Profile_screen;
import com.example.smarthome.scenarios.Scenario_screen;
import com.example.smarthome.settings.Settings_screen;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Device_screen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    //menu
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    //api
    private Api api;

    //data z login/room screeny
    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_screen);

        apiConnection();

        //session
        SessionManagement sessionManagement = new SessionManagement(Device_screen.this);
        login =  sessionManagement.getLoginSession();
        ////////////////////////////////////////////////////////////////////////////////////////////

        setNavigationView();
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
    public void setNavigationView()
    {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        //nastavenie nazvu domacnosti a pouzivatela v headri menu
        View headerView = navigationView.getHeaderView(0);

        TextView homeName = headerView.findViewById(R.id.headerHousehold);
        TextView userName = headerView.findViewById(R.id.headerUserName);

        homeName.setText(login.getHouseholdName());
        userName.setText(login.getUsername());

        navigationView.bringToFront(); //pri kliknuti na menu nam menu nezmizne
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //umozni nam klikat v menu
        navigationView.setNavigationItemSelectedListener(this); //this
    }

    @Override
    public void onBackPressed()
    {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer((GravityCompat.START));

        else
            super.onBackPressed();
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.mainScreen:
                Intent main_intent = new Intent(Device_screen.this, Main_screen.class);
                startActivity(main_intent);
                break;
            case R.id.profile:
                Intent profile_intent = new Intent(Device_screen.this, Profile_screen.class);
                startActivity(profile_intent);
                break;
            case R.id.scenario:
                Intent scenario_intent = new Intent(Device_screen.this, Scenario_screen.class);
                startActivity(scenario_intent);
                break;
            case R.id.settings:
                Intent settings_intent = new Intent(Device_screen.this, Settings_screen.class);
                startActivity(settings_intent);
                break;
            case R.id.logout:
                logout();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //odhlasenie sa z aplikacie (zrusenie session)
    public void logout()
    {
        SessionManagement sessionManagement = new SessionManagement(Device_screen.this);
        sessionManagement.removeSession();

        moveToLoginScreen();
    }

    public void moveToLoginScreen()
    {
        Intent intent = new Intent(Device_screen.this, Login_screen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}