package com.example.smarthome.scenarios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarthome.R;
import com.example.smarthome.connection.Api;
import com.example.smarthome.connection.Devices;
import com.example.smarthome.connection.Login;
import com.example.smarthome.connection.Rooms;
import com.example.smarthome.connection.SessionManagement;
import com.example.smarthome.connection.Steps;
import com.example.smarthome.devices.Device_item;
import com.example.smarthome.login.Login_screen;
import com.example.smarthome.main.Main_screen;
import com.example.smarthome.main.Room_item;
import com.example.smarthome.profile.Profile_screen;
import com.example.smarthome.settings.Dark_mode;
import com.example.smarthome.settings.Settings_screen;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

    //kroky
    private ArrayList<Step_item> stepList;
    private ArrayList<Room_item> roomList;
    private ArrayList<Device_item> deviceList;
    private int selectedRoom;

    //api
    private Api api;

    //data z login/main screeny
    private Login login;

    //data zo scenario screeny
    private Scenario_item si;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario_add_screen_two);

        apiConnection();

        //session
        SessionManagement sessionManagement = new SessionManagement(Scenario_add_screen_two.this);
        login =  sessionManagement.getLoginSession();

        SessionManagement scenarioSessionManagement = new SessionManagement(Scenario_add_screen_two.this);
        si =  scenarioSessionManagement.getScenarioSession();

        SessionManagement darkModeSessionManagement = new SessionManagement(Scenario_add_screen_two.this);
        Dark_mode darkMode = darkModeSessionManagement.getDarkModeSession();
        ////////////////////////////////////////////////////////////////////////////////////////////

        if (darkMode.isDark_mode())
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        createStepList();

        FloatingActionButton addStep = findViewById(R.id.addStep);
        addStep.setOnClickListener(v ->
        {
            Intent intent = new Intent(Scenario_add_screen_two.this, Scenario_add_screen_three.class);
            startActivity(intent);
        });

        Button backBtn = findViewById(R.id.scenarioTwoBackButton);
        backBtn.setOnClickListener(v ->
        {
            Intent intent = new Intent(Scenario_add_screen_two.this, Scenario_screen.class);
            startActivity(intent);
        });

        getSteps();
        setRecyclerView();
        setNavigationView();
    }

    //pripojenie sa na api
    public void apiConnection()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://bcjurajstekla.ddnsfree.com/public_api/api2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(Api.class);
    }

    //plocha pre stepy
    public void setRecyclerView()
    {
        mRecyclerView = findViewById(R.id.scenarioTwoRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new Step_adapter(stepList, login, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        new ItemTouchHelper(stepToRemove).attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void createStepList()
    {
        stepList = new ArrayList<>();
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

    public void getSteps()
    {
        Call<List<Steps>> call = api.getSteps(si.getScenarioId(), login.getHouseholdId());

        call.enqueue(new Callback<List<Steps>>()
        {
            @Override
            public void onResponse(Call<List<Steps>> call, Response<List<Steps>> response)
            {
                if (!response.isSuccessful())
                {
                    System.out.println("call = " + call + ", response = " + response);
                    return;
                }

                List<Steps> steps = response.body();
                final int position = 0;

                for (Steps step: steps)
                {
                    stepList.add(position, new Step_item(step.getId_step(), step.getStepName(), step.getId_scenario(), step.getId_device(), step.getId_room(), step.getIsOn(),
                            step.getIs_active(), step.getHumidity(), step.getTemperature(), step.getIntensity()));

                    mAdapter.notifyItemInserted(position);
                }
            }

            @Override
            public void onFailure(Call<List<Steps>> call, Throwable t)
            {
                System.out.println("call = " + call + ", t = " + t);
            }
        });
    }

    //mazanie krokov (with swap right)
    ItemTouchHelper.SimpleCallback stepToRemove = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT)
    {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target)
        {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
        {
            if (canEdit()) //user je admin
            {
                //builder na potvrdenie zmazania
                AlertDialog.Builder builder = new AlertDialog.Builder(Scenario_add_screen_two.this);
                builder.setCancelable(true);
                builder.setMessage("Naozaj chcete odstrániť tento krok '" + stepList.get(viewHolder.getAdapterPosition()).getStepName().toUpperCase() + "' ?");
                builder.setPositiveButton("Áno", (dialog, which) ->
                {
                    int id_step = stepList.get(viewHolder.getAdapterPosition()).getId_step();

                    deleteStep(id_step);

                    //refresh recycleviewra
                    stepList.remove(viewHolder.getAdapterPosition());
                    mAdapter.notifyDataSetChanged();

                    Toast.makeText(Scenario_add_screen_two.this, "Krok bol úspešne odstránený", Toast.LENGTH_SHORT).show();
                });

                builder.setNegativeButton("Nie", (dialog, which) ->
                {
                    dialog.dismiss();
                    mAdapter.notifyDataSetChanged();
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }

            else // user nie je admin
            {
                Toast.makeText(Scenario_add_screen_two.this, "Nemáte povolenie odstrániť krok scenára", Toast.LENGTH_SHORT).show();
                mAdapter.notifyDataSetChanged();
            }

        }
    };

    public boolean canEdit()
    {
        if (login.getRole() == 1)
            return true;
        else
            return false;
    }

    public void deleteStep(int id_step)
    {
        Call<Void> call = api.deleteStep(id_step, si.getScenarioId(), login.getHouseholdId());

        call.enqueue(new Callback<Void>()
        {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response)
            {
                System.out.println("call = " + call + ", response = " + response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t)
            {
                System.out.println("call = " + call + ", t = " + t);
            }
        });
    }

    @Override
    public void onStepClick(int position)
    {
        //nothing to do
    }

    public void fillRoomSpinner()
    {
        Call<List<Rooms>> call = api.getRoomsWithSensors(login.getHouseholdId(), 0);
        call.enqueue(new Callback<List<Rooms>>()
        {
            @Override
            public void onResponse(Call<List<Rooms>> call, Response<List<Rooms>> response)
            {
                if (!response.isSuccessful())
                {
                    System.out.println("call = " + call + ", response = " + response);
                    return;
                }

                List<Rooms> rooms = response.body();
                final int position = 0;

                for (Rooms room: rooms)
                {
                    roomList.add(position, new Room_item(room.getRoomName(), room.getRoomId()));
                }

                fillDeviceSpinner();
            }

            @Override
            public void onFailure(Call<List<Rooms>> call, Throwable t)
            {
                System.out.println("call = " + call + ", t = " + t);
            }
        });
    }

    public void fillDeviceSpinner()
    {
        Call<List<Devices>> call = api.getSensors(login.getHouseholdId(), selectedRoom, 0);

        call.enqueue(new Callback<List<Devices>>()
        {
            @Override
            public void onResponse(Call<List<Devices>> call, Response<List<Devices>> response)
            {
                if (!response.isSuccessful())
                {
                    System.out.println("call = " + call + ", response = " + response);
                    return;
                }

                List<Devices> devices = response.body();
                final int position = 0;

                for (Devices device: devices)
                {
                    deviceList.add(position, new Device_item(device.getDeviceName(), device.getDeviceId(), device.getDeviceType(), device.getIdRoom()));
                }

                Intent intent = new Intent(Scenario_add_screen_two.this, Scenario_add_screen_three.class);
                intent.putExtra("room_steparraylist", roomList);
                intent.putExtra("device_steparraylist", deviceList);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<List<Devices>> call, Throwable t)
            {
                System.out.println("call = " + call + ", t = " + t);
            }
        });
    }

    public void createRoomList()
    {
        roomList = new ArrayList<>();
    }

    public void createDeviceList()
    {
        deviceList = new ArrayList<>();
    }

    public void getDeviceType(int roomId, int deviceId)
    {
        Call<List<Devices>> call = api.getDevice(roomId, deviceId);

        call.enqueue(new Callback<List<Devices>>()
        {
            @Override
            public void onResponse(Call<List<Devices>> call, Response<List<Devices>> response)
            {
                if (!response.isSuccessful())
                {
                    System.out.println("call = " + call + ", response = " + response);
                    return;
                }

                List<Devices> devices = response.body();
                final int position = 0;

                for (Devices device: devices)
                {
                    SessionManagement stepDeviceTypeManagement = new SessionManagement(Scenario_add_screen_two.this);
                    stepDeviceTypeManagement.saveStepDeviceSession(device.getDeviceType());
                }
            }

            @Override
            public void onFailure(Call<List<Devices>> call, Throwable t)
            {
                System.out.println("call = " + call + ", t = " + t);
            }
        });
    }

    @Override
    public void onEditClick(int position)
    {
        Step_item step_item = new Step_item(stepList.get(position).getId_step(), stepList.get(position).getStepName(), stepList.get(position).getId_scenario(), stepList.get(position).getId_device(),
                stepList.get(position).getId_room(), stepList.get(position).getIsOn(), stepList.get(position).getIsActive(), stepList.get(position).getHumidity(), stepList.get(position).getTemperature(),
                stepList.get(position).getIntensity());

        SessionManagement stepSessionManagement = new SessionManagement(Scenario_add_screen_two.this);
        stepSessionManagement.saveStepSession(step_item);

        selectedRoom = stepList.get(position).getId_room();
        createRoomList();
        createDeviceList();
        getDeviceType(stepList.get(position).getId_room(), stepList.get(position).getId_device());
        fillRoomSpinner();
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
}