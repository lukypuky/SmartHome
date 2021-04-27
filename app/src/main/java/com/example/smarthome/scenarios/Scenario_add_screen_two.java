package com.example.smarthome.scenarios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.smarthome.R;
import com.example.smarthome.connection.Login;
import com.example.smarthome.connection.SessionManagement;
import com.example.smarthome.login.Login_screen;
import com.example.smarthome.main.Main_screen;
import com.example.smarthome.main.Room_adapter;
import com.example.smarthome.main.Room_item;
import com.example.smarthome.profile.Profile_screen;
import com.example.smarthome.settings.Settings_screen;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class Scenario_add_screen_two extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Step_adapter.OnStepListener
{
    //menu
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    //zoznam stepov
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter; // bridge medzi datami a recycler view
    private RecyclerView.LayoutManager mLayoutManager;

    //miestnosti
    private ArrayList<Step_item> stepList;

    //data z login/main screeny
    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario_add_screen_two);

        //session
        SessionManagement sessionManagement = new SessionManagement(Scenario_add_screen_two.this);
        login =  sessionManagement.getLoginSession();
        ////////////////////////////////////////////////////////////////////////////////////////////

        createStepList();

        FloatingActionButton addStep = findViewById(R.id.addStep);
        addStep.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Scenario_add_screen_two.this, Scenario_add_screen_three.class);
                startActivity(intent);
            }
        });

        setRecyclerView();
        setNavigationView();
    }

    //plocha pre stepy
    public void setRecyclerView()
    {
        mRecyclerView = findViewById(R.id.scenarioTwoRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new Step_adapter(stepList, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
//        new ItemTouchHelper(roomToRemove).attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void createStepList()
    {
        stepList = new ArrayList<>();
        stepList.add(0, new Step_item("test"));
        stepList.add(1, new Step_item("test2"));
        stepList.add(2, new Step_item("test3"));
        stepList.add(0, new Step_item("test"));
        stepList.add(1, new Step_item("test2"));
        stepList.add(2, new Step_item("test3"));
        stepList.add(0, new Step_item("test"));
        stepList.add(1, new Step_item("test2"));
        stepList.add(2, new Step_item("test3"));
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
                Intent main_intent = new Intent(Scenario_add_screen_two.this, Main_screen.class);
                startActivity(main_intent);
                break;
            case R.id.profile:
                Intent profile_intent = new Intent(Scenario_add_screen_two.this, Profile_screen.class);
                startActivity(profile_intent);
                break;
            case R.id.scenario:
                Intent scenario_intent = new Intent(Scenario_add_screen_two.this, Scenario_screen.class);
                startActivity(scenario_intent);
                break;
            case R.id.settings:
                Intent settings_intent = new Intent(Scenario_add_screen_two.this, Settings_screen.class);
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
        SessionManagement sessionManagement = new SessionManagement(Scenario_add_screen_two.this);
        sessionManagement.removeSession();

        moveToLoginScreen();
    }

    public void moveToLoginScreen()
    {
        Intent intent = new Intent(Scenario_add_screen_two.this, Login_screen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onStepClick(int position) {

    }
}