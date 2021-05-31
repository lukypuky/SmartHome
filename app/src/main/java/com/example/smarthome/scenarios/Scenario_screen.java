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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarthome.connection.Api;
import com.example.smarthome.connection.Devices;
import com.example.smarthome.connection.Login;
import com.example.smarthome.connection.Rooms;
import com.example.smarthome.connection.Scenarios;
import com.example.smarthome.connection.SessionManagement;
import com.example.smarthome.devices.Device_item;
import com.example.smarthome.login.Login_screen;
import com.example.smarthome.main.Main_screen;
import com.example.smarthome.R;
import com.example.smarthome.main.Room_item;
import com.example.smarthome.main.Room_screen;
import com.example.smarthome.profile.Profile_screen;
import com.example.smarthome.settings.Dark_mode;
import com.example.smarthome.settings.Settings_screen;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Scenario_screen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Scenario_adapter.OnScenarioListener
{
    //menu
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    //zoznam scenarov
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter; // bridge medzi datami a recycler view
    private RecyclerView.LayoutManager mLayoutManager;

    //scenare
    private ArrayList<Scenario_item> scenarioList;
    private ArrayList<Room_item> roomList;
    private ArrayList<Device_item> deviceList;
    private int selectedRoom;

    //api
    private Api api;

    //data z login/main screeny
    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenarios_screen);

        apiConnection();

        //session
        SessionManagement sessionManagement = new SessionManagement(Scenario_screen.this);
        login =  sessionManagement.getLoginSession();

        SessionManagement darkModeSessionManagement = new SessionManagement(Scenario_screen.this);
        Dark_mode darkMode = darkModeSessionManagement.getDarkModeSession();
        //////////////////////////////////////////////////////////////////////////////////////////

        if (darkMode.isDark_mode())
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        createScenarioList();

        //tlacidlo na pridanie noveho scenara
        FloatingActionButton addScenario = findViewById(R.id.addScenario);
        if (canEdit())
        {
            addScenario.setOnClickListener(v ->
            {
                Intent intent = new Intent(Scenario_screen.this, Scenario_add_screen_one.class);
                startActivity(intent);
            });
        }

        else
            addScenario.setVisibility(View.INVISIBLE);

        getScenarios();
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

    //plocha pre scenare v domacnosti
    public void  setRecyclerView()
    {
        mRecyclerView = findViewById(R.id.scenarioRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new Scenario_adapter(scenarioList, login,this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        new ItemTouchHelper(scenarioToRemove).attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
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
                Intent main_intent = new Intent(Scenario_screen.this, Main_screen.class);
                startActivity(main_intent);
                break;
            case R.id.profile:
                Intent profile_intent = new Intent(Scenario_screen.this, Profile_screen.class);
                startActivity(profile_intent);
                break;
            case R.id.scenario:
                break;
            case R.id.settings:
                Intent settings_intent = new Intent(Scenario_screen.this, Settings_screen.class);
                startActivity(settings_intent);
                break;
            case R.id.logout:
                logout();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void createScenarioList()
    {
        scenarioList = new ArrayList<>();
    }

    public void getScenarios()
    {
        Call<List<Scenarios>> call = api.getScenarios(login.getHouseholdId());
        call.enqueue(new Callback<List<Scenarios>>()
        {
            @Override
            public void onResponse(Call<List<Scenarios>> call, Response<List<Scenarios>> response)
            {
                if (!response.isSuccessful())
                {
                    System.out.println("call = " + call + ", response = " + response);
                    return;
                }

                List<Scenarios> scenarios = response.body();
                final int position = 0;

                for (Scenarios scenario: scenarios)
                {
                    scenarioList.add(position, new Scenario_item(R.drawable.scenario_icon, scenario.getScenarioId(), scenario.getScenarioName(), scenario.getExecutingType(), scenario.getId_room(),
                            scenario.getSensorId(), scenario.getIsExecutable(), scenario.getIsRunning(), scenario.getScenarioValue(), scenario.getTime()));
                    mAdapter.notifyItemInserted(position);
                }
            }

            @Override
            public void onFailure(Call<List<Scenarios>> call, Throwable t)
            {
                System.out.println("call = " + call + ", t = " + t);
            }
        });
    }

    public boolean canEdit()
    {
        if (login.getRole() == 1)
            return true;
        else
            return false;
    }

    //mazanie krokov (with swap right)
    ItemTouchHelper.SimpleCallback scenarioToRemove = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT)
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
                AlertDialog.Builder builder = new AlertDialog.Builder(Scenario_screen.this);
                builder.setCancelable(true);
                builder.setMessage("Naozaj chcete odstrániť tento scenár '" + scenarioList.get(viewHolder.getAdapterPosition()).getScenarioName().toUpperCase() + "' ?");
                builder.setPositiveButton("Áno", (dialog, which) ->
                {
                    int id_scenario = scenarioList.get(viewHolder.getAdapterPosition()).getScenarioId();
                    int position = viewHolder.getAdapterPosition();

                    deleteScenario(id_scenario, position);
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
                Toast.makeText(Scenario_screen.this, "Nemáte povolenie odstrániť scenár.", Toast.LENGTH_SHORT).show();
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    public void createRoomList()
    {
        roomList = new ArrayList<>();
    }

    public void createDeviceList()
    {
        deviceList = new ArrayList<>();
    }

    public void deleteScenario(int id_scenario, int position)
    {
        Call<Scenarios> call = api.deleteScenario(id_scenario, login.getHouseholdId());

        call.enqueue(new Callback<Scenarios>()
        {
            @Override
            public void onResponse(Call<Scenarios> call, Response<Scenarios> response)
            {
                System.out.println("call = " + call + ", response = " + response);

                if (response.body().getInfo().equals("Scenar has steps!"))
                {
                    Toast.makeText(Scenario_screen.this, "Pre ostránenie scenára musíte najprv odstŕaniť kroky v scenári", Toast.LENGTH_SHORT).show();
                    mAdapter.notifyDataSetChanged();
                }

                else
                {
                    //refresh recycleviewra
                    scenarioList.remove(position);
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(Scenario_screen.this, "Scenár bol úspešne odstráneý", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Scenarios> call, Throwable t)
            {
                System.out.println("call = " + call + ", t = " + t);
            }
        });
    }

    public void fillRoomSpinner()
    {
        Call<List<Rooms>> call = api.getRoomsWithSensors(login.getHouseholdId(), 1);
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
        Call<List<Devices>> call = api.getSensors(login.getHouseholdId(), selectedRoom, 1);

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

                deviceList.add(position, new Device_item("Zapnúť na základe času",0,"time/time",0));

                for (Devices device: devices)
                {
                    deviceList.add(position, new Device_item(device.getDeviceName(), device.getDeviceId(), device.getDeviceType(), device.getIdRoom()));
                }

                Intent intent = new Intent(Scenario_screen.this, Scenario_add_screen_one.class);
                intent.putExtra("room_arraylist", roomList);
                intent.putExtra("device_arraylist", deviceList);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<List<Devices>> call, Throwable t)
            {
                System.out.println("call = " + call + ", t = " + t);
            }
        });
    }

    @Override
    public void onScenarioClick(int position)
    {
        Scenario_item si = new Scenario_item(scenarioList.get(position).getScenarioId());
        SessionManagement scenarioSessionManagement = new SessionManagement(Scenario_screen.this);
        scenarioSessionManagement.saveScenarioSession(si);

        Intent intent = new Intent(this, Scenario_add_screen_two.class);
        startActivity(intent);
    }

    @Override
    public void onEditClick(int position)
    {
        Scenario_item si = new Scenario_item(scenarioList.get(position).getScenarioId(), scenarioList.get(position).getScenarioName(), scenarioList.get(position).getScenarioType(),
                scenarioList.get(position).getId_room(), scenarioList.get(position).getSensorId(), scenarioList.get(position).getScenarioExecutable(), scenarioList.get(position).getIsRunning(),
                scenarioList.get(position).getValue(), scenarioList.get(position).getTime());
        SessionManagement scenarioSessionManagement = new SessionManagement(Scenario_screen.this);
        scenarioSessionManagement.saveScenarioSession(si);

        selectedRoom = scenarioList.get(position).getId_room();
        createRoomList();
        createDeviceList();
        fillRoomSpinner();
    }

    //odhlasenie sa z aplikacie (zrusenie session)
    public void logout()
    {
        SessionManagement sessionManagement = new SessionManagement(Scenario_screen.this);
        sessionManagement.removeSession();

        moveToLoginScreen();
    }

    public void moveToLoginScreen()
    {
        Intent intent = new Intent(Scenario_screen.this, Login_screen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}