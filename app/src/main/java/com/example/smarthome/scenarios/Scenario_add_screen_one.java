package com.example.smarthome.scenarios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.smarthome.R;
import com.example.smarthome.connection.Login;
import com.example.smarthome.connection.SessionManagement;
import com.example.smarthome.login.Login_screen;
import com.example.smarthome.main.Main_screen;
import com.example.smarthome.profile.Profile_screen;
import com.example.smarthome.settings.Settings_screen;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

public class Scenario_add_screen_one extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, TimePickerDialog.OnTimeSetListener
{
    //menu
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private TextView hourValue, hourTag, minuteValue, minuteTag, pickedTime;
    private Button nextBtn, timeBtn;

    //data z login/main screeny
    private Login login;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario_add_screen_one);

        //session
        SessionManagement sessionManagement = new SessionManagement(Scenario_add_screen_one.this);
        login =  sessionManagement.getLoginSession();
        ////////////////////////////////////////////////////////////////////////////////////////////

        nextBtn = findViewById(R.id.nextScenarioButton);
        timeBtn = findViewById(R.id.scenarioOneTimeBtn);
        hourValue = findViewById(R.id.scenarioOneHour);
        hourTag = findViewById(R.id.scenarioOneHourTag);
        minuteValue = findViewById(R.id.scenarioOneMin);
        minuteTag = findViewById(R.id.scenarioOneMinTag);
        pickedTime = findViewById(R.id.scenarioOnePickedTime);

        //vyber casu
        timeBtn.setOnClickListener(v ->
        {
            DialogFragment timePicker = new TimePickerFragment();
            timePicker.show(getSupportFragmentManager(), "time picker");
        });

        //screen 2/3
        nextBtn.setOnClickListener(v ->
        {
            Intent intent = new Intent(Scenario_add_screen_one.this, Scenario_add_screen_two.class);
            startActivity(intent);
        });

        setNavigationView();
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
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(0);
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
                Intent main_intent = new Intent(Scenario_add_screen_one.this, Main_screen.class);
                startActivity(main_intent);
                break;
            case R.id.profile:
                Intent profile_intent = new Intent(Scenario_add_screen_one.this, Profile_screen.class);
                startActivity(profile_intent);
                break;
            case R.id.scenario:
                Intent scenario_intent = new Intent(Scenario_add_screen_one.this, Scenario_screen.class);
                startActivity(scenario_intent);
                break;
            case R.id.settings:
                Intent settings_intent = new Intent(Scenario_add_screen_one.this, Settings_screen.class);
                startActivity(settings_intent);
                break;
            case R.id.logout:
                logout();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
    {
        hourValue.setText(String.valueOf(hourOfDay));
        minuteValue.setText(String.valueOf(minute));

        hourValue.setVisibility(View.VISIBLE);
        hourTag.setVisibility(View.VISIBLE);
        minuteValue.setVisibility(View.VISIBLE);
        minuteTag.setVisibility(View.VISIBLE);
        pickedTime.setVisibility(View.VISIBLE);
    }

    //odhlasenie sa z aplikacie (zrusenie session)
    public void logout()
    {
        SessionManagement sessionManagement = new SessionManagement(Scenario_add_screen_one.this);
        sessionManagement.removeSession();

        moveToLoginScreen();
    }

    public void moveToLoginScreen()
    {
        Intent intent = new Intent(Scenario_add_screen_one.this, Login_screen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}