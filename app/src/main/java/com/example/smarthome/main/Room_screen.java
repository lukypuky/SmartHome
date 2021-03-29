package com.example.smarthome.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarthome.R;
import com.example.smarthome.connection.Api;
import com.example.smarthome.connection.Devices;
import com.example.smarthome.connection.Login;
import com.example.smarthome.connection.Rooms;
import com.example.smarthome.connection.SessionManagement;
import com.example.smarthome.devices.Device_adapter;
import com.example.smarthome.devices.Device_item;
import com.example.smarthome.devices.Device_screen;
import com.example.smarthome.login.Login_screen;
import com.example.smarthome.profile.Profile_screen;
import com.example.smarthome.scenarios.Scenario_screen;
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
    private static final String TAG = "Room_screen";

    //menu
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    //pridanie zariadenia
    private AlertDialog.Builder addDeviceDialog;
    private AlertDialog dialog;
    private EditText deviceName;
    private Spinner deviceType;
    private Button saveDevice, unsaveDevice;

    //zoznam zariadeni
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter; // bridge medzi datami a recycler view
    private RecyclerView.LayoutManager mLayoutManager;

    //zariadenia
    private ArrayList<Device_item> deviceList;
    private List<Devices> devices;
    private ImageView active;

    //api
    private Api api;

    //data z login/main screeny
    private Login login;
    private Room_item ri;
    private int roomId;

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
        ri =  roomSessionManagement.getRoomItemSession();
        ////////////////////////////////////////////////////////////////////////////////////////////

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
                .baseUrl("http://147.175.121.237/api2/")
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
    public void insertDevice(EditText deviceName, String deviceType)
    {
        String name = deviceName.getText().toString();
        Call<Devices> call = api.postDevice(deviceType, name, roomId, 0);

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

    //metoda na pridanie noveho zariadenia v domacnosti
    public void addDeviceDialog()
    {
        initializeDialog();

        //potvrdenie pridania zariadenia
        saveDevice.setOnClickListener(v ->
        {
            //ak sa nevyplnil nazov zariadanie -> message
            if (deviceName.getText().toString().isEmpty())
                Toast.makeText(Room_screen.this, "Zadajte názov pre zariadenie", Toast.LENGTH_SHORT).show();

            //ak sa nezvolil typ zariadenia -> message
            else if(deviceType.getSelectedItem().toString().equals("Typ zariadenia"))
                Toast.makeText(Room_screen.this, "Zvoľte typ zariadenia", Toast.LENGTH_SHORT).show();

            //ak je vsetko vyplnene, pridaj zariadenie do arraylistu
            else
            {
                insertDevice(deviceName, deviceType.getSelectedItem().toString());
                dialog.dismiss();
            }
        });

        //zrusenie pridania zariadenia
        unsaveDevice.setOnClickListener(v -> dialog.dismiss());
    }

    public void editDeviceDialog(int position)
    {
        initializeDialog();

        String type = deviceList.get(position).getDeviceType();
        deviceName.setText(deviceList.get(position).getDeviceName());
        deviceType.setSelection(getIndexOfSpinner(deviceType, type));

        //potvrdenie editu zariadenia
        saveDevice.setOnClickListener(v ->
        {
            //ak sa nevyplnil nazov zariadanie -> message
            if (deviceName.getText().toString().isEmpty())
                Toast.makeText(Room_screen.this, "Zadajte názov pre zariadenie", Toast.LENGTH_SHORT).show();

            //ak sa nezvolil typ zariadenia -> message
            else if(deviceType.getSelectedItem().toString().equals("Typ zariadenia"))
                Toast.makeText(Room_screen.this, "Zvoľte typ zariadenia", Toast.LENGTH_SHORT).show();

            //ak je vsetko vyplnene, zedituj zariadenie
            else
            {
                int deviceId = deviceList.get(position).getDeviceId();
                int isActive = deviceList.get(position).getIsActive();
                String stringDeviceType = deviceType.getSelectedItem().toString();
                String stringDeviceName = deviceName.getText().toString();

                Call<Devices> call = api.editDevice(deviceId, stringDeviceType, stringDeviceName, roomId, isActive);

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
        addDeviceDialog = new AlertDialog.Builder(Room_screen.this);
        View contactPopupView = getLayoutInflater().inflate(R.layout.activity_add_device_popup, null);

        deviceName = contactPopupView.findViewById(R.id.deviceName);
        saveDevice = contactPopupView.findViewById(R.id.saveDeviceButton);
        unsaveDevice = contactPopupView.findViewById(R.id.unsaveDeviceButton);

        deviceType = contactPopupView.findViewById(R.id.deviceType);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Room_screen.this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.devices));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        deviceType.setAdapter(adapter);

        addDeviceDialog.setView(contactPopupView);
        dialog = addDeviceDialog.create();
        dialog.show();
    }

    public void setScreenValues()
    {
        TextView screenHeading = findViewById(R.id.roomText);
        ImageView screenImg = findViewById(R.id.roomImage);

        screenHeading.setText(ri.getRoomName());
        screenImg.setImageResource(ri.getIntRoomType());
        roomId = ri.getId_room();

        active =  findViewById(R.id.deviceActive);
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

                devices = response.body();
                final int position = 0;

                for (Devices device: devices)
                {
                    int image = getDeviceImage(device.getDeviceType());
                    int imageActive = getActiveImage(device.getIs_active());

                    deviceList.add(position, new Device_item(image, device.getDeviceName(), device.getDeviceType(), device.getDeviceId(), device.getIs_active(), imageActive));
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
            case "Alarm":
                return R.drawable.alarm;
            case "Kúrenie":
                return R.drawable.heater;
            case "Svetlá":
                return R.drawable.light;
            case "Teplomer":
                return R.drawable.temperature;
            case "Zásuvka":
                return  R.drawable.socket;
            case "Žalúzie":
                return R.drawable.blinds;
        }

        return 0;
    }

    public int getActiveImage(int active)
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
    @Override
    public void onDeviceClick(int position)
    {
        Intent intent = new Intent(this, Device_screen.class);
        startActivity(intent);
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