package com.example.smarthome.main;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarthome.R;
import com.example.smarthome.connection.Api;
import com.example.smarthome.connection.Devices;
import com.example.smarthome.connection.Login;
import com.example.smarthome.connection.SessionManagement;
import com.example.smarthome.devices.Device_adapter;
import com.example.smarthome.devices.Device_item;
import com.example.smarthome.login.Login_screen;
import com.example.smarthome.profile.Profile_screen;
import com.example.smarthome.scenarios.Scenario_screen;
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

public class Room_screen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Device_adapter.OnDeviceListener
{
    //menu
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    //pridanie zariadenia
    private AlertDialog dialog;
    private EditText deviceName;
    private Spinner spinnerDeviceType;
    private Button saveDevice, unsaveDevice;

    //ovladanie zariadenia
    private AlertDialog controlDialog;
    private TextView controlDeviceName, controlDeviceStatusTag, controlDeviceValue;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch controlDeviceSwitch, controlDeviceWarningSwitch;
    private SeekBar controlDeviceSeekBar;
    private Button saveControlDevice, unsaveControlDevice;
    private int seekBarValue;

    //zoznam zariadeni
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter; // bridge medzi datami a recycler view
    private RecyclerView.LayoutManager mLayoutManager;

    //zariadenia
    private ArrayList<Device_item> deviceList;
    private TextView popUpTag;

    //api
    private Api api;

    //data z login/main screeny
    private Login login;
    private Room_item ri;
    private int roomId;

    public Room_screen() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_screen);

        apiConnection();

        //session
        SessionManagement sessionManagement = new SessionManagement(Room_screen.this);
        login =  sessionManagement.getLoginSession();

        SessionManagement roomSessionManagement = new SessionManagement(Room_screen.this);
        ri =  roomSessionManagement.getRoomSession();

        SessionManagement darkModeSessionManagement = new SessionManagement(Room_screen.this);
        Dark_mode darkMode = darkModeSessionManagement.getDarkModeSession();
        ////////////////////////////////////////////////////////////////////////////////////////////

        if (darkMode.isDark_mode())
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        setScreenValues();
        createDeviceList();

        //tlacidlo na pridanie noveho zariadenia
        FloatingActionButton addDevice = findViewById(R.id.addDevice);
        if (canEdit())
            addDevice.setOnClickListener(v -> addDeviceDialog());

        else
            addDevice.setVisibility(View.INVISIBLE);

        getDevices();
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

    //plocha pre zariadenia v domacnosti
    public void  setRecyclerView()
    {
        mRecyclerView = findViewById(R.id.roomRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new Device_adapter(deviceList, login, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        new ItemTouchHelper(deviceToRemove).attachToRecyclerView(mRecyclerView);
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
                Intent main_intent = new Intent(Room_screen.this, Main_screen.class);
                startActivity(main_intent);
                break;
            case R.id.profile:
                Intent profile_intent = new Intent(Room_screen.this, Profile_screen.class);
                startActivity(profile_intent);
                break;
            case R.id.scenario:
                Intent scenario_intent = new Intent(Room_screen.this, Scenario_screen.class);
                startActivity(scenario_intent);
                break;
            case R.id.settings:
                Intent settings_intent = new Intent(Room_screen.this, Settings_screen.class);
                startActivity(settings_intent);
                break;
            case R.id.logout:
                logout();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void createDeviceList()
    {
        deviceList = new ArrayList<>();
    }

    //prida zariadenie na index 0 v arrayliste
    public void insertDevice(EditText deviceName, String spinDeviceType)
    {
        Call<Devices> call;
        String name = deviceName.getText().toString();
        String deviceType = setDeviceType(spinDeviceType);

        if (deviceType.charAt(0) == '1')
            call = api.postDevice(deviceType, name, roomId, 1,0,0,0,0.0f, 0,0);

        else
            call = api.postDevice(deviceType, name, roomId, 0,0,0,0,0.0f, 0,0);

        call.enqueue(new Callback<Devices>()
        {
            @Override
            public void onResponse(Call<Devices> call, Response<Devices> response)
            {
                if (!response.isSuccessful())
                {
                    System.out.println("call = " + call + ", response = " + response);
                }

                if (response.body().getDeviceStatus() == 1)
                    Toast.makeText(Room_screen.this, "Zariadenie pridané", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Devices> call, Throwable t)
            {
                System.out.println("call = " + call + ", t = " + t);
            }
        });

        Intent room_intent = new Intent(Room_screen.this, Room_screen.class);
        startActivity(room_intent);
        Room_screen.this.finish();
    }

    public String setDeviceType(String spinDeviceType)
    {
        if(spinDeviceType.equals("Alarm"))
            return "2/alarm";
        else if(spinDeviceType.equals("Dymový senzor"))
            return "2/smoke_sensor";
        else if(spinDeviceType.equals("Kúrenie"))
            return "0/heating";
        else if(spinDeviceType.equals("Svetelný senzor"))
            return "1/light_sensor";
        else if(spinDeviceType.equals("Svetlo"))
            return "0/light";
        else if(spinDeviceType.equals("Teplomer"))
            return "1/thermometer";
        else if(spinDeviceType.equals("Vlhkomer"))
            return "1/hygrometer";
        else if(spinDeviceType.equals("Záplavový senzor"))
            return "2/flood_sensor";
        else if(spinDeviceType.equals("Zásuvka"))
            return "0/socket";
        else if(spinDeviceType.equals("Žalúzie"))
            return "0/blinds";
        return "";
    }

    public String setSpinnerDeviceType(String type)
    {
        if(type.equals("alarm"))
            return "Alarm";
        else if(type.equals("smoke_sensor"))
            return "Dymový senzor";
        else if(type.equals("heating"))
            return "Kúrenie";
        else if(type.equals("light_sensor"))
            return "Svetelný senzor";
        else if(type.equals("light"))
            return "Svetlo";
        else if(type.equals("thermometer"))
            return "Teplomer";
        else if(type.equals("hygrometer"))
            return "Vlhkomer";
        else if(type.equals("flood_sensor"))
            return "Záplavový senzor";
        else if(type.equals("socket"))
            return "Zásuvka";
        else if(type.equals("blinds"))
            return "Žalúzie";
        return "";
    }

    //metoda na pridanie noveho zariadenia v domacnosti
    public void addDeviceDialog()
    {
        initializeDialog();
        popUpTag.setText("Pridanie nového zariadenia");

        //potvrdenie pridania zariadenia
        saveDevice.setOnClickListener(v ->
        {
            //ak sa nevyplnil nazov zariadanie -> message
            if (deviceName.getText().toString().isEmpty())
                Toast.makeText(Room_screen.this, "Zadajte názov pre zariadenie", Toast.LENGTH_SHORT).show();

            //ak sa nezvolil typ zariadenia -> message
            else if(spinnerDeviceType.getSelectedItem().toString().equals("Typ zariadenia"))
                Toast.makeText(Room_screen.this, "Zvoľte typ zariadenia", Toast.LENGTH_SHORT).show();

            //ak je vsetko vyplnene, pridaj zariadenie do arraylistu
            else
            {
                insertDevice(deviceName, spinnerDeviceType.getSelectedItem().toString());
                dialog.dismiss();
            }
        });

        //zrusenie pridania zariadenia
        unsaveDevice.setOnClickListener(v -> dialog.dismiss());
    }

    public void editDeviceDialog(int position)
    {
        initializeDialog();
        popUpTag.setText("Úprava zariadenia");

        String type = setSpinnerDeviceType(deviceList.get(position).getDeviceType());
        deviceName.setText(deviceList.get(position).getDeviceName());
        spinnerDeviceType.setSelection(getIndexOfSpinner(spinnerDeviceType, type));

        //potvrdenie editu zariadenia
        saveDevice.setOnClickListener(v ->
        {
            //ak sa nevyplnil nazov zariadanie -> message
            if (deviceName.getText().toString().isEmpty())
                Toast.makeText(Room_screen.this, "Zadajte názov pre zariadenie", Toast.LENGTH_SHORT).show();

            //ak sa nezvolil typ zariadenia -> message
            else if(spinnerDeviceType.getSelectedItem().toString().equals("Typ zariadenia"))
                Toast.makeText(Room_screen.this, "Zvoľte typ zariadenia", Toast.LENGTH_SHORT).show();

            //ak je vsetko vyplnene, zedituj zariadenie
            else
            {
                int deviceId = deviceList.get(position).getDeviceId();
                int isActive = deviceList.get(position).getIsActive();
                int isOn = deviceList.get(position).getIsOn();
                int intensity = deviceList.get(position).getIntensity();
                int humidity = deviceList.get(position).getHumidity();
                float temperature = deviceList.get(position).getTemperature();
                int connectivity = deviceList.get(position).getConnectivity();
                String stringDeviceType = setDeviceType(spinnerDeviceType.getSelectedItem().toString());
                String stringDeviceName = deviceName.getText().toString();

                Call<Devices> call = api.editDevice(deviceId, stringDeviceType, stringDeviceName, roomId, isOn, isActive, intensity, humidity, temperature, connectivity);

                call.enqueue(new Callback<Devices>()
                {
                    @Override
                    public void onResponse(Call<Devices> call, Response<Devices> response)
                    {
                        if (!response.isSuccessful())
                        {
                            System.out.println("call = " + call + ", response = " + response);
                            return;
                        }

                        if (response.body().getDeviceStatus() == 1)
                            Toast.makeText(Room_screen.this, "Zariadenie zmenené", Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                        Intent room_intent = new Intent(Room_screen.this, Room_screen.class);
                        startActivity(room_intent);
                        Room_screen.this.finish();
                    }

                    @Override
                    public void onFailure(Call<Devices> call, Throwable t)
                    {
                        System.out.println("call = " + call + ", t = " + t);
                    }
                });
            }
        });

        //zrusenie edit zariadenia
        unsaveDevice.setOnClickListener(v -> dialog.dismiss());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void controlDevice(int position)
    {
        String stringDeviceType = deviceList.get(position).getDeviceType();

        if (stringDeviceType.equals("socket") || stringDeviceType.equals("alarm") || stringDeviceType.equals("flood_sensor") || stringDeviceType.equals("smoke_sensor"))
            initializeBasicDialog(position);

        else
            initializeDeviceRangeDialog(stringDeviceType, position);

        saveControlDevice.setOnClickListener(v ->
        {
            int isOn;
            int isActive = 0;
            int intensity = deviceList.get(position).getIntensity();
            float temperature = deviceList.get(position).getTemperature();

            boolean switchState = controlDeviceSwitch.isChecked();
            if (switchState)
                isOn = 1;
            else
                isOn = 0;

            if (stringDeviceType.equals("socket") || stringDeviceType.equals("alarm") || stringDeviceType.equals("flood_sensor") || stringDeviceType.equals("smoke_sensor"))
            {
                boolean warningSwtichState = controlDeviceWarningSwitch.isChecked();
                if (warningSwtichState)
                {
                    isActive = 0;
                    controlDeviceWarningSwitch.setVisibility(View.INVISIBLE);
                }

                else
                    isActive = deviceList.get(position).getIsActive();
            }

            if (stringDeviceType.equals("heating"))
                temperature = seekBarValue;
            else if(stringDeviceType.equals("light") || (stringDeviceType.equals("blinds")))
                intensity = seekBarValue;

            int deviceId = deviceList.get(position).getDeviceId();
            int humidity = deviceList.get(position).getHumidity();
            int connectivity = deviceList.get(position).getConnectivity();
            String stringDeviceName = deviceList.get(position).getDeviceName();

            String deviceType1 = setSpinnerDeviceType(stringDeviceType);
            String deviceType2 = setDeviceType(deviceType1);

            Call<Devices> call = api.editDevice(deviceId, deviceType2, stringDeviceName, roomId, isOn, isActive, intensity, humidity, temperature, connectivity);

            call.enqueue(new Callback<Devices>()
            {
                @Override
                public void onResponse(Call<Devices> call, Response<Devices> response)
                {
                    if (!response.isSuccessful())
                    {
                        System.out.println("call = " + call + ", response = " + response);
                        return;
                    }

                    if (response.body().getDeviceStatus() == 1)
                        Toast.makeText(Room_screen.this, "Zariadenie nastavené", Toast.LENGTH_SHORT).show();

                    controlDialog.dismiss();
                    Intent room_intent = new Intent(Room_screen.this, Room_screen.class);
                    startActivity(room_intent);
                    Room_screen.this.finish();
                }

                @Override
                public void onFailure(Call<Devices> call, Throwable t)
                {
                    System.out.println("call = " + call + ", t = " + t);
                }
            });
        });

        unsaveControlDevice.setOnClickListener(v -> controlDialog.dismiss());
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

    //dialog window pre add a edit zariadenia
    public void initializeDialog()
    {
        AlertDialog.Builder addDeviceDialog = new AlertDialog.Builder(Room_screen.this);
        View contactPopupView = getLayoutInflater().inflate(R.layout.add_device_popup, null);

        deviceName = contactPopupView.findViewById(R.id.deviceName);
        saveDevice = contactPopupView.findViewById(R.id.saveDeviceButton);
        unsaveDevice = contactPopupView.findViewById(R.id.unsaveDeviceButton);
        popUpTag = contactPopupView.findViewById(R.id.devicePopUpTag);

        spinnerDeviceType = contactPopupView.findViewById(R.id.deviceType);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Room_screen.this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.devices));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerDeviceType.setAdapter(adapter);

        addDeviceDialog.setView(contactPopupView);
        dialog = addDeviceDialog.create();
        dialog.show();
    }

    public void initializeBasicDialog(int position)
    {
        AlertDialog.Builder controlDeviceDialog = new AlertDialog.Builder(Room_screen.this);
        View contactPopupView;

        contactPopupView = getLayoutInflater().inflate(R.layout.device_basic_popup, null);

        controlDeviceName = contactPopupView.findViewById(R.id.devicePopUpName);
        saveControlDevice = contactPopupView.findViewById(R.id.saveControlDeviceButton);
        unsaveControlDevice = contactPopupView.findViewById(R.id.unsaveControlDeviceButton);
        controlDeviceSwitch = contactPopupView.findViewById(R.id.devicePopUpSwitch);
        controlDeviceWarningSwitch = contactPopupView.findViewById(R.id.devicePopUpWarningSwitch);

        setSwitch(position);

        //nastavenie hodnot z DB
        controlDeviceName.setText(deviceList.get(position).getDeviceName());

        controlDeviceDialog.setView(contactPopupView);
        controlDialog = controlDeviceDialog.create();
        controlDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initializeDeviceRangeDialog(String type, int position)
    {
        AlertDialog.Builder controlDeviceDialog = new AlertDialog.Builder(Room_screen.this);
        View contactPopupView;

        contactPopupView = getLayoutInflater().inflate(R.layout.device_range_popup, null);

        saveControlDevice = contactPopupView.findViewById(R.id.saveControlDeviceButton);
        unsaveControlDevice = contactPopupView.findViewById(R.id.unsaveControlDeviceButton);
        controlDeviceName = contactPopupView.findViewById(R.id.devicePopUpName);
        controlDeviceSwitch = contactPopupView.findViewById(R.id.devicePopUpSwitch);
        controlDeviceStatusTag = contactPopupView.findViewById(R.id.deviceDisplayTag);
        controlDeviceValue = contactPopupView.findViewById(R.id.devicePopUpIntensity);
        controlDeviceSeekBar = contactPopupView.findViewById(R.id.devicePopUpSeekBar);

        controlSeekBar(type);

        switch (type)
        {
            case "heating":
                controlDeviceSeekBar.setMax(30);
                controlDeviceSeekBar.setMin(18);
                controlDeviceSeekBar.setProgress((int) deviceList.get(position).getTemperature());
                controlDeviceStatusTag.setText("Teplota: ");
                break;
            case "light":
                controlDeviceSeekBar.setProgress(deviceList.get(position).getIntensity());
                controlDeviceStatusTag.setText("Intenzita: ");
                break;
            case "blinds":
                controlDeviceSeekBar.setProgress(deviceList.get(position).getIntensity());
                controlDeviceStatusTag.setText("Zatiahnuté: ");
                break;
        }

        setSwitch(position);

        //nastavenie hodnot z DB
        controlDeviceName.setText(deviceList.get(position).getDeviceName());

        controlDeviceDialog.setView(contactPopupView);
        controlDialog = controlDeviceDialog.create();
        controlDialog.show();
    }

    public void setSwitch(int position)
    {
        if (deviceList.get(position).getIsOn() == 1)
        {
            controlDeviceSwitch.setChecked(true);

            if(deviceList.get(position).getIsActive() == 1)
            {
                controlDeviceWarningSwitch.setVisibility(View.VISIBLE);
                controlDeviceWarningSwitch.setChecked(false);
            }
        }

        else
            controlDeviceSwitch.setChecked(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void controlSeekBar(String type)
    {
        String unit;
        seekBarValue = 0;

        if (type.equals("heating"))
            unit = "°C";

        else
            unit = "%";

        controlDeviceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                controlDeviceValue.setText(progress + unit);
                seekBarValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    public void setScreenValues()
    {
        TextView screenHeading = findViewById(R.id.roomText);
        ImageView screenImg = findViewById(R.id.roomImage);

        screenHeading.setText(ri.getRoomName());
        screenImg.setImageResource(ri.getIntRoomType());
        roomId = ri.getId_room();
    }

    //mazanie zariadenia (with swap right)
    ItemTouchHelper.SimpleCallback deviceToRemove = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT)
    {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target)
        {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
        {
            if(canEdit())
            {
                //builder na potvrdenie zmazania
                AlertDialog.Builder builder = new AlertDialog.Builder(Room_screen.this);
                builder.setCancelable(true);
                builder.setMessage("Naozaj chcete odstrániť toto zariadenie '" + deviceList.get(viewHolder.getAdapterPosition()).getDeviceName().toUpperCase() + "' ?");
                builder.setPositiveButton("Áno", (dialog, which) ->
                {
                    int id_device = deviceList.get(viewHolder.getAdapterPosition()).getDeviceId();

                    deleteDevice(id_device, roomId);

                    //refresh recycleviewra
                    deviceList.remove(viewHolder.getAdapterPosition());
                    mAdapter.notifyDataSetChanged();

                    Toast.makeText(Room_screen.this, "Zariadenie bolo úspešne odstránené.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Room_screen.this, "Nemáte povolenie odstrániť zariadenie.", Toast.LENGTH_SHORT).show();
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    public void deleteDevice(int id_device, int id_room)
    {
        Call<Void> call = api.deleteDevice(id_device, id_room);

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

    public void getDevices()
    {
        Call<List<Devices>> call = api.getDevices(roomId);

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
                    String[] parserArray = device.getDeviceType().split("/", 2);
                    int image = getDeviceImage(parserArray[1]);
                    int imageConnected = getConnectedImage(device.getConnectivity());
                    String parseType = parserArray[1];

                    deviceList.add(position, new Device_item(image, device.getDeviceName(), parseType, device.getDeviceId(), device.getIsOn(),
                            device.getConnectivity(), device.getIs_active(), imageConnected, device.getIntensity(), device.getHumidity(), device.getTemperature()));
                    mAdapter.notifyItemInserted(position);
                }
            }

            @Override
            public void onFailure(Call<List<Devices>> call, Throwable t)
            {
                System.out.println("call = " + call + ", t = " + t);
            }
        });
    }

    //metoda ktora vrati obrazok pre zariadenie na zaklade typu zariadenia
    public int getDeviceImage(String type)
    {
        switch (type)
        {
            case "alarm":
                return R.drawable.alarm;
            case "smoke_sensor":
                return R.drawable.fire_sensor;
            case "heating":
                return R.drawable.heater;
            case "light_sensor":
                return R.drawable.light_sensor;
            case "light":
                return R.drawable.light;
            case "thermometer":
                return R.drawable.temperature;
            case "hygrometer":
                return R.drawable.humidity;
            case "flood_sensor":
                return R.drawable.water_sensor;
            case "socket":
                return  R.drawable.socket;
            case "blinds":
                return R.drawable.blinds;
        }

        return 0;
    }

    public int getConnectedImage(int active)
    {
        switch (active)
        {
            case 0:
                return R.drawable.red;
            case 1:
                return R.drawable.green;
        }

        return 0;
    }

    //click na zariadenie
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDeviceClick(int position)
    {
        int isConnected = deviceList.get(position).getConnectivity();
        String type = deviceList.get(position).getDeviceType();

        if (isConnected == 1)
        {
            if (!type.equals("thermometer")  && !type.equals("hygrometer") && !type.equals("light_sensor"))
                controlDevice(position);
        }

        else
            Toast.makeText(Room_screen.this, "Zariadenie nie je pripojené", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEditClick(int position)
    {
        editDeviceDialog(position);
    }

    public boolean canEdit()
    {
        if (login.getRole() == 1)
            return true;
        else
            return false;
    }

    //odhlasenie sa z aplikacie (zrusenie session)
    public void logout()
    {
        SessionManagement sessionManagement = new SessionManagement(Room_screen.this);
        sessionManagement.removeSession();

        moveToLoginScreen();
    }

    public void moveToLoginScreen()
    {
        Intent intent = new Intent(Room_screen.this, Login_screen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}