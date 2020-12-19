package com.example.smarthome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.example.smarthome.profile.Profile_screen;
import com.example.smarthome.schemas.Schemas_screen;
import com.example.smarthome.settings.Settings_screen;
import com.google.android.material.navigation.NavigationView;

public class Main_screen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        navigationView.bringToFront(); //pri kliknuti na menu nam menu nezmizne
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //umozni nam klikat v menu
        navigationView.setNavigationItemSelectedListener(this);

        //pri spusteni appky bude zakliknuta defaultne main screena
        navigationView.setCheckedItem(R.id.mainScreen);
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
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.mainScreen:
                break;
            case R.id.profile:
                Intent profile_intent = new Intent(Main_screen.this, Profile_screen.class);
                startActivity(profile_intent);
                break;
            case R.id.schema:
                Intent schema_intent = new Intent(Main_screen.this,Schemas_screen.class);
                startActivity(schema_intent);
                break;
            case R.id.settings:
                Intent settings_intent = new Intent(Main_screen.this, Settings_screen.class);
                startActivity(settings_intent);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}