package com.example.smarthome.scenarios;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.smarthome.R;
import com.example.smarthome.connection.Api;
import com.example.smarthome.connection.Devices;
import com.example.smarthome.connection.Login;
import com.example.smarthome.connection.Rooms;
import com.example.smarthome.connection.Scenarios;
import com.example.smarthome.connection.SessionManagement;
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

public class Scenario_add_screen_one extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, TimePickerDialog.OnTimeSetListener
{
    //menu
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    //screen {
    private TextView spinnerTag, seekBarTag, valueFromTag, valueFrom;
    private TextView valueToTag, valueTo;
    private EditText scenarioName;

    //spinners
    private Spinner roomSpinner, deviceSpinner;
    private ArrayList<Room_item> roomList;
    private ArrayList<Device_item> deviceList;
    private int selectedRoom;
    private String selectedDevice, deviceTypeSelected;

    //seekBars
    private SeekBar fromSeekBar, toSeekBar;
    private int seekBarFromValue, seekBarToValue;

    //radioBtns
    private RadioGroup radioGroup;
    private RadioButton radioButtonManual, radioButtonAuto;

    //buttons
    private Button nextBtn, timeBtn;

    //time picker
    private TextView hourValue, hourTag, minuteValue, minuteTag, pickedTime;
    //screen }

    //api
    private Api api;

    //data z login/main screeny
    private Login login;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario_add_screen_one);

        apiConnection();

        //session
        SessionManagement sessionManagement = new SessionManagement(Scenario_add_screen_one.this);
        login =  sessionManagement.getLoginSession();

        SessionManagement darkModeSessionManagement = new SessionManagement(Scenario_add_screen_one.this);
        Dark_mode darkMode = darkModeSessionManagement.getDarkModeSession();
        ////////////////////////////////////////////////////////////////////////////////////////////

        if (darkMode.isDark_mode())
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        initializeScreen();

        //nastavenie obrazovky podla zvoleneho typu ovladania scenara
        radioGroup.setOnCheckedChangeListener((group, checkedId) ->
        {

            if (checkedId == R.id.scenarioOneAutoBtn)
            {
                showObjects();
                createRoomList();
                fillRoomSpinner();
            }

            else
                hideObjects();
        });

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
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                getSelectedDeviceId();
                controlSeekBarFrom(deviceTypeSelected);
                controlSeekBarTo(deviceTypeSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                return;
            }
        });

        //vyber casu
        timeBtn.setOnClickListener(v ->
        {
            DialogFragment timePicker = new TimePickerFragment();
            timePicker.show(getSupportFragmentManager(), "time picker");
        });

        //screen 2/3
        nextBtn.setOnClickListener(v ->
        {
            boolean success = validate();
            if (success)
            {
//                saveScenario();

//                Scenario_item si = new Scenario_item(DOPLNIT ID);
//                SessionManagement scenarioSessionManagement = new SessionManagement(Scenario_add_screen_one.this);
//                scenarioSessionManagement.saveScenarioSession(si);
//
                Intent intent = new Intent(Scenario_add_screen_one.this, Scenario_add_screen_two.class);
                startActivity(intent);
            }
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

    public void initializeScreen()
    {
        scenarioName = findViewById(R.id.scenarioOneName);
        radioGroup = findViewById(R.id.scenarioOneRadioGroup);
        radioButtonManual = findViewById(R.id.scenarioOneManualBtn);
        radioButtonAuto = findViewById(R.id.scenarioOneAutoBtn);
        spinnerTag = findViewById(R.id.scenarioOneSpinnerTag);
        roomSpinner = findViewById(R.id.scenarioOneRoomSpinner);
        deviceSpinner = findViewById(R.id.scenarioOneDeviceSpinner);
        timeBtn = findViewById(R.id.scenarioOneTimeBtn);
        pickedTime = findViewById(R.id.scenarioOnePickedTime);
        hourValue = findViewById(R.id.scenarioOneHour);
        hourTag = findViewById(R.id.scenarioOneHourTag);
        minuteValue = findViewById(R.id.scenarioOneMin);
        minuteTag = findViewById(R.id.scenarioOneMinTag);
        seekBarTag = findViewById(R.id.scenarioOneSeekBarTag);
        valueFromTag = findViewById(R.id.scenarioOneValueFromTag);
        fromSeekBar = findViewById(R.id.scenarioOneBarFrom);
        valueFrom = findViewById(R.id.scenarioOneValueFrom);
        valueToTag = findViewById(R.id.scenarioOneValueToTag);
        toSeekBar = findViewById(R.id.scenarioOneBarTo);
        valueTo = findViewById(R.id.scenarioOneValueTo);

        nextBtn = findViewById(R.id.nextScenarioButton);
    }

    public void showObjects()
    {
        spinnerTag.setVisibility(View.VISIBLE);
        roomSpinner.setVisibility(View.VISIBLE);
        deviceSpinner.setVisibility(View.VISIBLE);
    }

    public void hideObjects()
    {
        spinnerTag.setVisibility(View.INVISIBLE);
        roomSpinner.setVisibility(View.INVISIBLE);
        deviceSpinner.setVisibility(View.INVISIBLE);
        timeBtn.setVisibility(View.INVISIBLE);
        pickedTime.setVisibility(View.INVISIBLE);
        hourValue.setVisibility(View.INVISIBLE);
        hourTag.setVisibility(View.INVISIBLE);
        minuteValue.setVisibility(View.INVISIBLE);
        minuteTag.setVisibility(View.INVISIBLE);
        seekBarTag.setVisibility(View.INVISIBLE);
        valueFromTag.setVisibility(View.INVISIBLE);
        fromSeekBar.setVisibility(View.INVISIBLE);
        valueFrom.setVisibility(View.INVISIBLE);
        valueToTag.setVisibility(View.INVISIBLE);
        toSeekBar.setVisibility(View.INVISIBLE);
        valueTo.setVisibility(View.INVISIBLE);

        hourValue.setText("");
        minuteValue.setText("");
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

                ArrayAdapter<Room_item> adapter = new ArrayAdapter<Room_item>(Scenario_add_screen_one.this,
                        android.R.layout.simple_spinner_item, roomList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                roomSpinner.setAdapter(adapter);
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
        Call<List<Devices>> call = api.getSensors(login.getHouseholdId(),selectedRoom, 1); //selectedRoom

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

                deviceList.add(position, new Device_item("Zapnúť na základe času",0,"time/time"));

                for (Devices device: devices)
                {
                    deviceList.add(position, new Device_item(device.getDeviceName(), device.getDeviceId(), device.getDeviceType()));
                }


                ArrayAdapter<Device_item> adapter = new ArrayAdapter<Device_item>(Scenario_add_screen_one.this,
                        android.R.layout.simple_spinner_item, deviceList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                deviceSpinner.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Devices>> call, Throwable t)
            {
                System.out.println("call = " + call + ", t = " + t);
            }
        });
    }

    public void getSelectedRoomId()
    {
        Room_item ri = (Room_item) roomSpinner.getSelectedItem();
        selectedRoom = ri.getId_room();
    }

    public void getSelectedDeviceId()
    {
        Device_item di = (Device_item) deviceSpinner.getSelectedItem();
        selectedDevice = String.valueOf(di.getDeviceId());
        deviceTypeSelected = (String) ((Device_item) deviceSpinner.getSelectedItem()).getDeviceType();
        String[] parserArray = deviceTypeSelected.split("/", 2);
        deviceTypeSelected = parserArray[1];

        if (deviceTypeSelected.equals("time") || deviceTypeSelected.equals("smoke_sensor")
                || deviceTypeSelected.equals("flood_sensor") || deviceTypeSelected.equals("alarm"))
        {
            if (deviceTypeSelected.equals("time"))
                timeBtn.setVisibility(View.VISIBLE);

            else
                timeBtn.setVisibility(View.INVISIBLE);

            seekBarTag.setVisibility(View.INVISIBLE);
            valueFromTag.setVisibility(View.INVISIBLE);
            fromSeekBar.setVisibility(View.INVISIBLE);
            valueFrom.setVisibility(View.INVISIBLE);
            valueToTag.setVisibility(View.INVISIBLE);
            toSeekBar.setVisibility(View.INVISIBLE);
            valueTo.setVisibility(View.INVISIBLE);
        }

        else
        {
            timeBtn.setVisibility(View.INVISIBLE);
            pickedTime.setVisibility(View.INVISIBLE);
            hourValue.setVisibility(View.INVISIBLE);
            hourTag.setVisibility(View.INVISIBLE);
            minuteValue.setVisibility(View.INVISIBLE);
            minuteTag.setVisibility(View.INVISIBLE);

            seekBarTag.setVisibility(View.VISIBLE);
            valueFromTag.setVisibility(View.VISIBLE);
            fromSeekBar.setVisibility(View.VISIBLE);
            valueFrom.setVisibility(View.VISIBLE);
            valueToTag.setVisibility(View.VISIBLE);
            toSeekBar.setVisibility(View.VISIBLE);
            valueTo.setVisibility(View.VISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void controlSeekBarFrom(String type)
    {
        String unit;
        seekBarFromValue = 0;

        if (type.equals("thermometer"))
            unit = "°C";

        else
            unit = "%";

        fromSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                valueFrom.setText(progress + unit);
                seekBarFromValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void controlSeekBarTo(String type)
    {
        String unit;
        seekBarToValue = 0;

        if (type.equals("thermometer"))
            unit = "°C";

        else
            unit = "%";

        toSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                valueTo.setText(progress + unit);
                seekBarToValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    public void saveScenario()
    {
        Call<Scenarios> call;
        String stringScenarioName = scenarioName.getText().toString();
        String status = seekBarFromValue + "-" + seekBarToValue;
        String time = hourValue.getText().toString() + ":" + minuteValue.getText().toString();

        if (radioButtonManual.isChecked())
        {
            call = api.postScenario(stringScenarioName, "manual", "0",0, "0", "0", login.getHouseholdId());
        }

        else
        {
            if (deviceTypeSelected.equals("time"))
                call = api.postScenario(stringScenarioName, "auto", deviceTypeSelected,0, "0", time, login.getHouseholdId());
            else if(deviceTypeSelected.equals("smoke_sensor") || deviceTypeSelected.equals("flood_sensor") || deviceTypeSelected.equals("alarm"))
                call = api.postScenario(stringScenarioName, "auto", selectedDevice,0, "1","0", login.getHouseholdId());
            else
                call = api.postScenario(stringScenarioName, "auto", selectedDevice,0, status,"0", login.getHouseholdId());
        }

        call.enqueue(new Callback<Scenarios>()
        {
            @Override
            public void onResponse(Call<Scenarios> call, Response<Scenarios> response)
            {
                if (!response.isSuccessful())
                {
                    System.out.println("call = " + call + ", response = " + response);
                }

//                if (response.body().getScenarioStatus() == 1)
//                    Toast.makeText(Scenario_add_screen_one.this, "Scenár bol pridaný", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Scenarios> call, Throwable t)
            {
                System.out.println("call = " + call + ", t = " + t);
            }
        });
    }

    public boolean validate()
    {
        if (TextUtils.isEmpty(scenarioName.getText().toString()))
        {
            scenarioName.setError("Zadajte názov scenára");
            return false;
        }

        else if (!radioButtonManual.isChecked() && !radioButtonAuto.isChecked())
        {
            Toast.makeText(Scenario_add_screen_one.this, "Zvoľte typ ovládania scenára", Toast.LENGTH_SHORT).show();
            return false;
        }

        else if (radioButtonManual.isChecked())
            return true;

        else if(radioButtonAuto.isChecked())
        {
            if (deviceTypeSelected.equals("time"))
            {
                if (TextUtils.isEmpty(hourValue.getText().toString()) || TextUtils.isEmpty(minuteValue.getText().toString()))
                {
                    Toast.makeText(Scenario_add_screen_one.this, "Zvoľte čas, kedy sa má scenár aktivovať", Toast.LENGTH_SHORT).show();
                    return false;
                }

                else
                    return true;

            }

            else if (deviceTypeSelected.equals("smoke_sensor") || deviceTypeSelected.equals("light_sensor") || deviceTypeSelected.equals("flood_sensor") || deviceTypeSelected.equals("alarm"))
                return true;

            else
            {
                if (seekBarFromValue > seekBarToValue || seekBarFromValue == seekBarToValue)
                {
                    Toast.makeText(Scenario_add_screen_one.this, "Hodnota 'Od' musí byť menšia ako hodnota 'Do'", Toast.LENGTH_SHORT).show();
                    return false;
                }

                else
                    return true;
            }
        }
        return false;
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