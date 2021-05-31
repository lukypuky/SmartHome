package com.example.smarthome.scenarios;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
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
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Scenario_add_screen_three extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    //menu
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    //screen {
    private EditText stepName;
    private TextView seekBarValue, heading;
    private int intSeekBarValue;
    private String[] parserArray;
    private int checked = 0;

    //spinners
    private Spinner roomSpinner, deviceSpinner;
    private ArrayList<Room_item> roomList;
    private ArrayList<Device_item> deviceList;
    private int selectedRoom, editSelectedRoom = 0;
    private String selectedDevice, deviceTypeSelected;
    private String editRoom = "", editDevice = "";

    //switch
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch onOffSwtich;

    //seekBar
    private SeekBar deviceValue;

    //button
    private Button saveBtn, backBtn;
    //screen }

    //api
    private Api api;

    //edit sessions
    private SessionManagement sessionManagementEdit, sessionManagementDeviceType;
    private Step_item editStep;

    //data z login/main screeny
    private Login login;

    //data zo scenario screeny
    private Scenario_item si;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario_add_screen_three);

        apiConnection();

        //session
        SessionManagement sessionManagement = new SessionManagement(Scenario_add_screen_three.this);
        login =  sessionManagement.getLoginSession();

        SessionManagement scenarioSessionManagement = new SessionManagement(Scenario_add_screen_three.this);
        si =  scenarioSessionManagement.getScenarioSession();

        sessionManagementEdit = new SessionManagement(Scenario_add_screen_three.this);
        editStep = sessionManagementEdit.getStepSession();

        sessionManagementDeviceType = new SessionManagement(Scenario_add_screen_three.this);

        SessionManagement darkModeSessionManagement = new SessionManagement(Scenario_add_screen_three.this);
        Dark_mode darkMode = darkModeSessionManagement.getDarkModeSession();
        ////////////////////////////////////////////////////////////////////////////////////////////

        if (darkMode.isDark_mode())
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        createRoomList();
        createDeviceList();

        initializeScreen();

        fillRoomSpinner();
        hideObjects();

        if (editStep.getId_step() != 0)
        {
            setScreenValues();
            heading.setText("Úprava kroku");
        }

        else
            heading.setText("Vytvorenie kroku");

        roomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                getSelectedRoomId();
                createDeviceList();
                fillDeviceSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                return;
            }
        });

        deviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                getSelectedDeviceId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                return;
            }
        });

        onOffSwtich.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            if (isChecked)
            {
                checked = 1;
                if (!(parserArray == null))
                {
                    if (!parserArray[0].equals("2") && !deviceTypeSelected.equals("socket"))
                    {
                        showObjects();
                        controlSeekBar(deviceTypeSelected);
                    }
                }
            }

            else
            {
                checked = 0;
                hideObjects();
            }
        });

        saveBtn.setOnClickListener(v ->
        {
            boolean success = validate();

            if (editStep.getId_step() != 0) //edit
            {
                if (success)
                    editStep();
            }

            else
            {
                if (success) //new step
                    postStep();
            }
        });

        backBtn.setOnClickListener(v ->
        {
            sessionManagementEdit.removeStepSession();
            sessionManagementDeviceType.removeStepDeviceSession();
            Intent intent = new Intent(Scenario_add_screen_three.this, Scenario_add_screen_two.class);
            startActivity(intent);
        });

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
        {
            super.onBackPressed();
            sessionManagementEdit.removeStepSession();
            sessionManagementDeviceType.removeStepDeviceSession();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.mainScreen:
                Intent main_intent = new Intent(Scenario_add_screen_three.this, Main_screen.class);
                startActivity(main_intent);
                sessionManagementEdit.removeStepSession();
                sessionManagementDeviceType.removeStepDeviceSession();
                break;
            case R.id.profile:
                Intent profile_intent = new Intent(Scenario_add_screen_three.this, Profile_screen.class);
                startActivity(profile_intent);
                sessionManagementEdit.removeStepSession();
                sessionManagementDeviceType.removeStepDeviceSession();
                break;
            case R.id.scenario:
                Intent scenario_intent = new Intent(Scenario_add_screen_three.this, Scenario_screen.class);
                startActivity(scenario_intent);
                sessionManagementEdit.removeStepSession();
                sessionManagementDeviceType.removeStepDeviceSession();
                break;
            case R.id.settings:
                Intent settings_intent = new Intent(Scenario_add_screen_three.this, Settings_screen.class);
                startActivity(settings_intent);
                sessionManagementEdit.removeStepSession();
                sessionManagementDeviceType.removeStepDeviceSession();
                break;
            case R.id.logout:
                logout();
                sessionManagementEdit.removeStepSession();
                sessionManagementDeviceType.removeStepDeviceSession();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void initializeScreen()
    {
        stepName = findViewById(R.id.scenarioThreeName);
        roomSpinner = findViewById(R.id.scenarioThreeRoomSpinner);
        deviceSpinner = findViewById(R.id.scenarioThreeDeviceSpinner);
        onOffSwtich = findViewById(R.id.scenarioThreeSwitch);
        seekBarValue = findViewById(R.id.scenarioThreeIntensityValue);
        deviceValue = findViewById(R.id.scenarioThreeSeekBar);

        heading = findViewById(R.id.scenarioThreeHeading);
        saveBtn = findViewById(R.id.scenarioThreeSaveButton);
        backBtn = findViewById(R.id.scenarioThreeBackButton);
    }

    public void showObjects()
    {
        seekBarValue.setVisibility(View.VISIBLE);
        deviceValue.setVisibility(View.VISIBLE);
    }

    public void hideObjects()
    {
        seekBarValue.setVisibility(View.INVISIBLE);
        deviceValue.setVisibility(View.INVISIBLE);
    }

    //pole pre spinner
    public void createRoomList()
    {
        roomList = new ArrayList<>();
    }

    //polepre spinner
    public void createDeviceList()
    {
        deviceList = new ArrayList<>();
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

                ArrayAdapter<Room_item> adapter = new ArrayAdapter<Room_item>(Scenario_add_screen_three.this,
                        android.R.layout.simple_spinner_item, roomList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                roomSpinner.setAdapter(adapter);

                if (!editRoom.equals(""))
                    roomSpinner.setSelection(getIndexOfSpinner(roomSpinner, editRoom));
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
        Call<List<Devices>> call;

        if (editSelectedRoom != 0)
        {
            call = api.getSensors(login.getHouseholdId(), editSelectedRoom, 0);
            editSelectedRoom = 0;
        }

        else
            call = api.getSensors(login.getHouseholdId(), selectedRoom, 0);

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

                ArrayAdapter<Device_item> adapter = new ArrayAdapter<Device_item>(Scenario_add_screen_three.this,
                        android.R.layout.simple_spinner_item, deviceList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                deviceSpinner.setAdapter(adapter);

                if (!editDevice.equals(""))
                    deviceSpinner.setSelection(getIndexOfSpinner(deviceSpinner, editDevice));
            }

            @Override
            public void onFailure(Call<List<Devices>> call, Throwable t)
            {
                System.out.println("call = " + call + ", t = " + t);
            }
        });
    }

    private int getIndexOfSpinner(Spinner spinner, String myString)
    {
        for (int i=0; i<spinner.getCount(); i++)
        {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString))
                return i;
        }

        return 0;
    }

    public void getSelectedRoomId()
    {
        Room_item ri = (Room_item) roomSpinner.getSelectedItem();
        selectedRoom = ri.getId_room();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getSelectedDeviceId()
    {
        Device_item di = (Device_item) deviceSpinner.getSelectedItem();
        selectedDevice = String.valueOf(di.getDeviceId());
        deviceTypeSelected = (String) ((Device_item) deviceSpinner.getSelectedItem()).getDeviceType();
        parserArray = deviceTypeSelected.split("/", 2);
        deviceTypeSelected = parserArray[1];

        if (parserArray[0].equals("2") || deviceTypeSelected.equals("socket"))
            hideObjects();
        else if (checked == 1)
        {
            showObjects();
            controlSeekBar(deviceTypeSelected);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void controlSeekBar(String type)
    {
        String unit;
        intSeekBarValue = 0;

        if (type.equals("heating"))
        {
            deviceValue.setMax(30);
            deviceValue.setMin(18);
            unit = "°C";
        }

        else
        {
            deviceValue.setMax(100);
            deviceValue.setMin(0);
            unit = "%";
        }

        deviceValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                seekBarValue.setText(progress + unit);
                intSeekBarValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    public boolean validate()
    {
        if (TextUtils.isEmpty(stepName.getText().toString()))
        {
            stepName.setError("Zadajte názov kroku");
            return false;
        }

        else
            return true;
    }

    public void postStep()
    {
        Call<Steps> call;

        if (deviceTypeSelected.equals("heating"))
           call = api.postSteps(stepName.getText().toString(), si.getScenarioId(),  selectedDevice, selectedRoom, checked, 0,0, (float)intSeekBarValue, 0, login.getHouseholdId());
        else if(deviceTypeSelected.equals("socket") || deviceTypeSelected.equals("alarm"))
            call = api.postSteps(stepName.getText().toString(), si.getScenarioId(), selectedDevice, selectedRoom, checked, 0,0, 0, 0, login.getHouseholdId());
        else
            call = api.postSteps(stepName.getText().toString(), si.getScenarioId(), selectedDevice, selectedRoom, checked, 0,0, 0, intSeekBarValue, login.getHouseholdId());

        call.enqueue(new Callback<Steps>()
        {
            @Override
            public void onResponse(Call<Steps> call, Response<Steps> response)
            {
                if (!response.isSuccessful())
                {
                    System.out.println("call = " + call + ", response = " + response);
                    return;
                }

                if (response.body().getStatus() == 1)
                {
                    Toast.makeText(Scenario_add_screen_three.this, "Krok bol pridaný do scenára", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Scenario_add_screen_three.this, Scenario_add_screen_two.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Steps> call, Throwable t)
            {
                System.out.println("call = " + call + ", t = " + t);
            }
        });
    }

    public void editStep()
    {
        Call<Steps> call;

        if (parserArray[1].equals("heating"))
            call = api.editSteps(editStep.getId_step(), stepName.getText().toString(), si.getScenarioId(),  selectedDevice, selectedRoom, checked, 0,0, (float)intSeekBarValue, 0, login.getHouseholdId());
        else if(parserArray[1].equals("socket") || parserArray[1].equals("alarm"))
            call = api.editSteps(editStep.getId_step(), stepName.getText().toString(), si.getScenarioId(), selectedDevice, selectedRoom, checked, 0,0, 0, 0, login.getHouseholdId());
        else
            call = api.editSteps(editStep.getId_step(), stepName.getText().toString(), si.getScenarioId(), selectedDevice, selectedRoom, checked, 0,0, 0, intSeekBarValue, login.getHouseholdId());

        call.enqueue(new Callback<Steps>()
        {
            @Override
            public void onResponse(Call<Steps> call, Response<Steps> response)
            {
                if (!response.isSuccessful())
                {
                    System.out.println("call = " + call + ", response = " + response);
                    return;
                }

                if (response.body().getStatus() == 1)
                {
                    Toast.makeText(Scenario_add_screen_three.this, "Krok bol upravený", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Scenario_add_screen_three.this, Scenario_add_screen_two.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Steps> call, Throwable t)
            {
                System.out.println("call = " + call + ", t = " + t);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setScreenValues()
    {
        stepName.setText(editStep.getStepName());

        String deviceType = sessionManagementDeviceType.getStepDeviceSession();
        parserArray = deviceType.split("/", 2);

        roomList = (ArrayList<Room_item>) getIntent().getSerializableExtra("room_steparraylist");
        deviceList = (ArrayList<Device_item>) getIntent().getSerializableExtra("device_steparraylist");

        for(int x = 0; x < deviceList.size(); x++)
        {
            if (deviceList.get(x).getDeviceId() == editStep.getId_device())
            {
                editSelectedRoom = deviceList.get(x).getDeviceRoomId();
                editDevice = deviceList.get(x).getDeviceName();
                break;
            }
        }

        for(int x = 0; x < roomList.size(); x++)
        {
            if (roomList.get(x).getId_room() == editStep.getId_room())
            {
                editRoom = roomList.get(x).getRoomName();
                break;
            }
        }

        if (editStep.getIsOn() == 1)
            onOffSwtich.setChecked(true);
        else
            onOffSwtich.setChecked(false);

        if(onOffSwtich.isChecked())
        {
            showObjects();

            if(parserArray[1].equals("light") || parserArray[1].equals("blinds"))
            {
                seekBarValue.setText(String.valueOf(editStep.getIntensity()));
                deviceValue.setProgress(editStep.getIntensity());
                controlSeekBar(parserArray[1]);
            }

            else if(parserArray[1].equals("heating"))
            {
                seekBarValue.setText(String.valueOf(editStep.getTemperature()));
                deviceValue.setProgress((int)editStep.getTemperature());
                controlSeekBar(parserArray[1]);
            }
        }

        else
            hideObjects();
    }

    //odhlasenie sa z aplikacie (zrusenie session)
    public void logout()
    {
        SessionManagement sessionManagement = new SessionManagement(Scenario_add_screen_three.this);
        sessionManagement.removeSession();

        moveToLoginScreen();
    }

    public void moveToLoginScreen()
    {
        Intent intent = new Intent(Scenario_add_screen_three.this, Login_screen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}