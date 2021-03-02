package com.example.smarthome.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.smarthome.main.Main_screen;
import com.example.smarthome.R;
import com.example.smarthome.profile.Profile_screen;
import com.example.smarthome.scenarios.Scenario_screen;
import com.google.android.material.navigation.NavigationView;

public class Settings_screen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setNavigationView();
    }

    //bocny navigacny panel
    public void setNavigationView()
    {
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.settings);
    }

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

    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.mainScreen:
                Intent main_intent = new Intent(Settings_screen.this, Main_screen.class);
                startActivity(main_intent);
                break;
            case R.id.profile:
                Intent profile_intent = new Intent(Settings_screen.this, Profile_screen.class);
                startActivity(profile_intent);
                break;
            case R.id.scenario:
                Intent scenario_intent = new Intent(Settings_screen.this, Scenario_screen.class);
                startActivity(scenario_intent);
                break;
            case R.id.settings:
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}