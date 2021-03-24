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

import android.content.DialogInterface;
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
import com.example.smarthome.profile.Profile_screen;
import com.example.smarthome.scenarios.Scenario_adapter;
import com.example.smarthome.scenarios.Scenario_item;
import com.example.smarthome.scenarios.Scenario_screen;
import com.example.smarthome.settings.Settings_screen;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class Room_screen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private static final String TAG = "Room_screen";

    //menu
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    //pridanie zariadenia
    private FloatingActionButton addDevice;
    private AlertDialog.Builder addDeviceDialog;
    private AlertDialog dialog;
    private EditText deviceName;
    private Spinner deviceType;
    private Button saveDevice, unsaveDevice;

    //zoznam zariadeni
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter; // bridge medzi datami a recycler view
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<Scenario_item> deviceList;

    private TextView textView;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_screen);

        createDeviceList();

        //tlacidlo na pridanie noveho scenara
        addDevice= (FloatingActionButton) findViewById(R.id.addDevice);
        addDevice.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                addDeviceDialog();
            }
        });

        setRecyclerView();
        setNavigationView();

        textView = findViewById(R.id.roomText);
        img = findViewById(R.id.roomImage);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null)
        {
            String string = (String) bundle.get("roomName");
            String image = getIntent().getStringExtra("roomImg");

            img.setImageResource(Integer.parseInt(image));
            textView.setText(string);
        }
    }

    //plocha pre zariadenia v domacnosti
    public void  setRecyclerView()
    {
        mRecyclerView = findViewById(R.id.roomRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new Scenario_adapter(deviceList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        new ItemTouchHelper(deviceToRemove).attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
    }

    //bocny navigacny panel
    public void setNavigationView()
    {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView.bringToFront(); //pri kliknuti na menu nam menu nezmizne
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //umozni nam klikat v menu
        navigationView.setNavigationItemSelectedListener(this); //this

//        //pri spusteni appky bude zakliknuta defaultne scenar screena
//        navigationView.setCheckedItem(R.id.);
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
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void createDeviceList()
    {
        deviceList = new ArrayList<>();
    }

    //prida zariadenie na index 0 v arrayliste
    public void insertDevice(EditText deviceName, int deviceType)
    {
        String name = deviceName.getText().toString();
        int position = 0;

        switch (deviceType)
        {
            case 1:
                deviceList.add(position, new Scenario_item(R.drawable.alarm,name));
                break;
            case 2:
                deviceList.add(position, new Scenario_item(R.drawable.heater,name));
                break;
            case 3:
                deviceList.add(position, new Scenario_item(R.drawable.light,name));
                break;
            case 4:
                deviceList.add(position, new Scenario_item(R.drawable.temperature,name));;
                break;
            case 5:
                deviceList.add(position, new Scenario_item(R.drawable.blinds,name));
                break;
        }

        mAdapter.notifyItemInserted(position);
    }

    //metoda na pridanie noveho zariadenia v domacnosti
    public void addDeviceDialog()
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

        //potvrdenie pridania zariadenia
        saveDevice.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int deviceTypeInt = 0;

                //ak sa nevyplnil nazov zariadanie -> message
                if (deviceName.getText().toString().isEmpty())
                {
                    Toast.makeText(Room_screen.this, "Zadajte názov pre zariadenie", Toast.LENGTH_SHORT).show();
                }

                //ak sa nezvolil typ zariadenia -> message
                else if(deviceType.getSelectedItem().toString().equals("Typ zariadenia"))
                {
                    Toast.makeText(Room_screen.this, "Zvoľte typ zariadenia", Toast.LENGTH_SHORT).show();
                }

                //ak je vsetko vyplnene, pridaj zariadenie do arraylistu
                else
                {
                    switch (deviceType.getSelectedItem().toString())
                    {
                        case "Alarm":
                            deviceTypeInt = 1;
                            break;
                        case "Kúrenie":
                            deviceTypeInt = 2;
                            break;
                        case "Svetlo":
                            deviceTypeInt = 3;
                            break;
                        case "Teplomer":
                            deviceTypeInt = 4;
                            break;
                        case "Žalúzie":
                            deviceTypeInt = 5;
                            break;
                    }

                    insertDevice(deviceName, deviceTypeInt);
                    Toast.makeText(Room_screen.this, "Zariadenie pridané", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });

        //zrusenie pridania zariadenia
        unsaveDevice.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
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
            //builder na potvrdenie zmazania
            AlertDialog.Builder builder = new AlertDialog.Builder(Room_screen.this);
            builder.setCancelable(true);
            builder.setMessage("Naozaj chcete odstrániť toto zariadenie '" + deviceList.get(viewHolder.getAdapterPosition()).getText().toUpperCase() + "' ?");
            builder.setPositiveButton("Áno", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    deviceList.remove(viewHolder.getAdapterPosition());
                    mAdapter.notifyDataSetChanged();
                }
            });

            builder.setNegativeButton("Nie", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                    mAdapter.notifyDataSetChanged();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    };
}