package com.example.smarthome.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthome.R;
import com.example.smarthome.connection.Api;
import com.example.smarthome.connection.Rooms;
import com.example.smarthome.profile.Profile_screen;
import com.example.smarthome.scenarios.Scenario_screen;
import com.example.smarthome.settings.Settings_screen;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Main_screen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Room_adapter.OnRoomListener
{
    private static final String TAG = "Main_screen";
    //menu
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    //pridanie miestnosti
    private FloatingActionButton addRoom;
    private AlertDialog.Builder addRoomDialog;
    private AlertDialog dialog;
    private EditText roomName;
    private Spinner roomType;
    private Button saveRoom, unsaveRoom;

    //zoznam miestnosti
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter; // bridge medzi datami a recycler view
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<Room_item> roomList;

    //api
    private Api api;

    private List<Rooms> rooms;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        //pripojenie sa na api
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://147.175.121.237/api2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(Api.class);

        createRoomList();

        //tlacidlo na pridanie novej miestnosti
        addRoom = findViewById(R.id.addRoom);
        addRoom.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                addRoomDialog();
            }
        });

        getRooms();
        setRecyclerView();
        setNavigationView();
    }

    //plocha pre miestnosti v domacnosti
    public void setRecyclerView()
    {
        mRecyclerView = findViewById(R.id.mainRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new Room_adapter(roomList, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        new ItemTouchHelper(roomToRemove).attachToRecyclerView(mRecyclerView);
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
    @SuppressLint("NonConstantResourceId")
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
            case R.id.scenario:
                Intent scenario_intent = new Intent(Main_screen.this, Scenario_screen.class);
                startActivity(scenario_intent);
                break;
            case R.id.settings:
                Intent settings_intent = new Intent(Main_screen.this, Settings_screen.class);
                startActivity(settings_intent);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //prida miestnost na index 0 v arrayliste
    public void insertRoom(EditText roomName, String roomType)
    {
        String name = roomName.getText().toString();

        Rooms room = new Rooms(name, roomType, 1);

        Call<Rooms> call = api.postRoom(name, roomType, 1);

        call.enqueue(new Callback<Rooms>()
        {
            @Override
            public void onResponse(Call<Rooms> call, Response<Rooms> response)
            {
                if (!response.isSuccessful())
                {
                    System.out.println("call = " + call + ", response = " + response);
                }
            }

            @Override
            public void onFailure(Call<Rooms> call, Throwable t)
            {
                System.out.println("call = " + call + ", t = " + t);
            }
        });

        Intent main_intent = new Intent(Main_screen.this, Main_screen.class);
        startActivity(main_intent);
//        mAdapter.notifyItemInserted(roomList.size());
    }

    public void createRoomList()
    {
        roomList = new ArrayList<>();
    }

    //metoda na pridanie novej miestnosti v domacnosti
    public void addRoomDialog()
    {
        addRoomDialog = new AlertDialog.Builder(Main_screen.this);
        View contactPopupView = getLayoutInflater().inflate(R.layout.activity_add_room_popup, null);

        roomName = contactPopupView.findViewById(R.id.roomName);
        saveRoom = contactPopupView.findViewById(R.id.saveRoomButton);
        unsaveRoom = contactPopupView.findViewById(R.id.unsaveRoomButton);

        roomType = contactPopupView.findViewById(R.id.roomType);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Main_screen.this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.rooms));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        roomType.setAdapter(adapter);

        addRoomDialog.setView(contactPopupView);
        dialog = addRoomDialog.create();
        dialog.show();

        //potvrdenie pridania miestnosti
        saveRoom.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String roomTypeString = "";

                //ak sa nevyplnil nazov miestnosti -> message
                if (roomName.getText().toString().isEmpty())
                {
                    Toast.makeText(Main_screen.this, "Zadajte názov pre miestnosť", Toast.LENGTH_SHORT).show();
                }

                //ak sa nezvolil typ miestnosti -> message
                else if(roomType.getSelectedItem().toString().equals("Typ miestnosti"))
                {
                    Toast.makeText(Main_screen.this, "Zvoľte typ miestnosti", Toast.LENGTH_SHORT).show();
                }

                //ak je vsetko vyplnene, pridaj miestnost do arraylistu
                else
                {
                    switch (roomType.getSelectedItem().toString())
                    {
                        case "Garáž":
                            roomTypeString = "garaz";
                            break;
                        case "Jedáleň":
                            roomTypeString = "jedalen";
                            break;
                        case "Kuchyňa":
                            roomTypeString = "kuchyna";
                            break;
                        case "Kúpeľňa":
                            roomTypeString = "kupelna";
                            break;
                        case "Obývačka":
                            roomTypeString = "obyvacka";
                            break;
                        case "Pracovňa":
                            roomTypeString = "pracovna";
                            break;
                        case "Spálňa":
                            roomTypeString = "spalna";
                            break;
                        case "Záhrada":
                            roomTypeString = "zahrada";
                            break;
                        case "Iné":
                            roomTypeString = "ine";
                            break;
                    }

                    insertRoom(roomName, roomTypeString);
                    Toast.makeText(Main_screen.this, "Miestnosť pridaná", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });

        //zrusenie pridania miestnosti
        unsaveRoom.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
    }

    //mazanie izieb (with swap right)
    ItemTouchHelper.SimpleCallback roomToRemove = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT)
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
            AlertDialog.Builder builder = new AlertDialog.Builder(Main_screen.this);
            builder.setCancelable(true);
            builder.setMessage("Naozaj chcete odstrániť túto miestnosť '" + roomList.get(viewHolder.getAdapterPosition()).getRoomName().toUpperCase() + "' ?");
            builder.setPositiveButton("Áno", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    int id_household = roomList.get(viewHolder.getAdapterPosition()).getId_household();
                    int id_room = roomList.get(viewHolder.getAdapterPosition()).getId_room();

                    deleteRoom(id_room, id_household);

                    //refresh recycleviewra
                    roomList.remove(viewHolder.getAdapterPosition());
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

    public void deleteRoom(int id_room, int id_household)
    {
        Call<Void> call = api.deleteRoom(id_room, id_household);

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
    public void onRoomClick(int position)
    {
        Log.d(TAG, "onRoomClick: clicked." + position);

        Intent intent = new Intent(this, Room_screen.class);
        intent.putExtra("roomName", String.valueOf(roomList.get(position).getRoomName()));
        intent.putExtra("roomImg", String.valueOf(roomList.get(position).getMimageResource()));
        startActivity(intent);
    }

    //getne vsetky miestnosti pre danu domacnost
    public void getRooms()
    {
        Call<List<Rooms>> call = api.getRooms(1);

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

                rooms = response.body();
                final int position = 0;

                for (Rooms room: rooms)
                {
                    int image = getRoomImage(room.getRoomType());
                    roomList.add(position, new Room_item(image, room.getRoomName(), 1, room.getRoomId()));
                    mAdapter.notifyItemInserted(position);
                }
            }

            @Override
            public void onFailure(Call<List<Rooms>> call, Throwable t)
            {
                System.out.println("call = " + call + ", t = " + t);
            }
        });
    }

    //metoda ktora vrati obrazok pre miestnot na zaklade typu miestnosti
    public int getRoomImage(String type)
    {
        switch (type)
        {
            case "garaz":
                return R.drawable.garage;
            case "jedalen":
                return R.drawable.dinningroom;
            case "kuchyna":
                return R.drawable.kitchen;
            case "kupelna":
                return R.drawable.bathroom;
            case "obyvacka":
                return R.drawable.livingroom;
            case "pracovna":
                return R.drawable.office;
            case "spalna":
                return R.drawable.bedroom;
            case "zahrada":
                return R.drawable.garden;
            case "ine":
                return R.drawable.defaultroom;
        }
        return 0;
    }
}
