package com.example.smarthome.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthome.R;
import com.example.smarthome.connection.Api;
import com.example.smarthome.connection.Login;
import com.example.smarthome.connection.Rooms;
import com.example.smarthome.connection.SessionManagement;
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

public class Main_screen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Room_adapter.OnRoomListener
{
    //menu
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView headerHousehold, headerUsername;

    //pridanie miestnosti
    private AlertDialog.Builder addRoomDialog;
    private AlertDialog dialog;
    private EditText roomName;
    private Spinner roomType;
    private Button saveRoom, unsaveRoom;

    //zoznam miestnosti
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter; // bridge medzi datami a recycler view
    private RecyclerView.LayoutManager mLayoutManager;

    //miestnosti
    private ArrayList<Room_item> roomList;
    private List<Rooms> rooms;

    //api
    private Api api;

    //data z login screeny
    private int householdId;
    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        apiConnection();

        //session
        SessionManagement sessionManagement = new SessionManagement(Main_screen.this);
        login =  sessionManagement.getLoginSession();

        SessionManagement darkModeSessionManagement = new SessionManagement(Main_screen.this);
        Dark_mode darkMode = darkModeSessionManagement.getDarkModeSession();
        ////////////////////////////////////////////////////////////////////////////////////////////

        if (darkMode.isDark_mode())
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        setScreenValues();
        createRoomList();

        //tlacidlo na pridanie novej miestnosti
        FloatingActionButton addRoom = findViewById(R.id.addRoom);
        if (canEdit())
            addRoom.setOnClickListener(v -> addRoomDialog());

        else
            addRoom.setVisibility(View.INVISIBLE);

        getRooms();
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

    //plocha pre miestnosti v domacnosti
    public void setRecyclerView()
    {
        mRecyclerView = findViewById(R.id.mainRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new Room_adapter(roomList, login, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        new ItemTouchHelper(roomToRemove).attachToRecyclerView(mRecyclerView);
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

        headerHousehold = headerView.findViewById(R.id.headerHousehold);
        headerUsername = headerView.findViewById(R.id.headerUserName);

        headerHousehold.setText(login.getHouseholdName());
        headerUsername.setText(login.getUsername());

        navigationView.bringToFront(); //pri kliknuti na menu nam menu nezmizne
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //umozni nam klikat v menu
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(0);
    }

    //pri pouziti tlacidla "Back" alebo po pouziti gesta na vratenie spat, sa zasunie menu, miesto toho aby sa vratilo a obrazovku spat
    @Override
    public void onBackPressed()
    {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer((GravityCompat.START));

        else
            super.onBackPressed();
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
            case R.id.logout:
                logout();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //prida miestnost do DB
    public void insertRoom(EditText roomName, String roomType)
    {
        String name = roomName.getText().toString();
        Call<Rooms> call = api.postRoom(name, roomType, householdId);

        call.enqueue(new Callback<Rooms>()
        {
            @Override
            public void onResponse(Call<Rooms> call, Response<Rooms> response)
            {
                if (!response.isSuccessful())
                {
                    System.out.println("call = " + call + ", response = " + response);
                }

                if (response.body().getRoomStatus() == 1)
                    Toast.makeText(Main_screen.this, "Miestnosť pridaná", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Rooms> call, Throwable t)
            {
                System.out.println("call = " + call + ", t = " + t);
            }
        });

        Intent main_intent = new Intent(Main_screen.this, Main_screen.class);
        startActivity(main_intent);
        Main_screen.this.finish();
    }

    public void createRoomList()
    {
        roomList = new ArrayList<>();
    }

    //dialog window pre add a edit miestnosti
    public void initializeDialog()
    {
        addRoomDialog = new AlertDialog.Builder(Main_screen.this);
        View contactPopupView = getLayoutInflater().inflate(R.layout.add_room_popup, null);

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
    }

    //metoda na pridanie novej miestnosti v domacnosti
    public void addRoomDialog()
    {
        initializeDialog();

        //potvrdenie pridania miestnosti
        saveRoom.setOnClickListener(v ->
        {
            //ak sa nevyplnil nazov miestnosti -> message
            if (roomName.getText().toString().isEmpty())
                Toast.makeText(Main_screen.this, "Zadajte názov pre miestnosť", Toast.LENGTH_SHORT).show();

            //ak sa nezvolil typ miestnosti -> message
            else if(roomType.getSelectedItem().toString().equals("Typ miestnosti"))
                Toast.makeText(Main_screen.this, "Zvoľte typ miestnosti", Toast.LENGTH_SHORT).show();

            //ak je vsetko vyplnene, pridaj miestnost do arraylistu
            else
            {
                insertRoom(roomName, roomType.getSelectedItem().toString());
                dialog.dismiss();
            }
        });

        //zrusenie editovania miestnosti
        unsaveRoom.setOnClickListener(v -> dialog.dismiss());
    }

    //metoda na edit existujucej miestnosti v domacnosti
    public void editRoomDialog(int position)
    {
        initializeDialog();

        String type = roomList.get(position).getRoomType();
        roomName.setText(roomList.get(position).getRoomName()); //set room name
        roomType.setSelection(getIndexOfSpinner(roomType, type)); //set room type

        //potvrdenie editu miestnosti
        saveRoom.setOnClickListener(v ->
        {
            //ak sa nevyplnil nazov miestnosti -> message
            if (roomName.getText().toString().isEmpty())
                Toast.makeText(Main_screen.this, "Zadajte názov pre miestnosť", Toast.LENGTH_SHORT).show();

            //ak sa nezvolil typ miestnosti -> message
            else if(roomType.getSelectedItem().toString().equals("Typ miestnosti"))
                Toast.makeText(Main_screen.this, "Zvoľte typ miestnosti", Toast.LENGTH_SHORT).show();

            //ak je vsetko vyplnene, pridaj zedituj miestnost
            else
            {
                String editRoomName = roomName.getText().toString();
                String editRoomType = roomType.getSelectedItem().toString();
                int editRoomId = roomList.get(position).getId_room();
                int editHouseholdId = roomList.get(position).getId_household();

                Call<Rooms> call = api.editRoom(editRoomId, editRoomName, editRoomType, editHouseholdId);

                call.enqueue(new Callback<Rooms>()
                {
                    @Override
                    public void onResponse(Call<Rooms> call, Response<Rooms> response)
                    {
                        if (!response.isSuccessful())
                        {
                            System.out.println("call = " + call + ", response = " + response);
                            return;
                        }

                        if (response.body().getRoomStatus() == 1)
                            Toast.makeText(Main_screen.this, "Miestnosť zmenená", Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                        Intent main_intent = new Intent(Main_screen.this, Main_screen.class);
                        startActivity(main_intent);
                        Main_screen.this.finish();
                    }

                    @Override
                    public void onFailure(Call<Rooms> call, Throwable t)
                    {
                        System.out.println("call = " + call + ", t = " + t);
                    }
                });
            }
        });

        //zrusenie edit miestnosti
        unsaveRoom.setOnClickListener(v -> dialog.dismiss());
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

            if (canEdit()) //user je admin
            {
                //builder na potvrdenie zmazania
                AlertDialog.Builder builder = new AlertDialog.Builder(Main_screen.this);
                builder.setCancelable(true);
                builder.setMessage("Naozaj chcete odstrániť túto miestnosť '" + roomList.get(viewHolder.getAdapterPosition()).getRoomName().toUpperCase() + "' ?");
                builder.setPositiveButton("Áno", (dialog, which) ->
                {
                    int id_household = roomList.get(viewHolder.getAdapterPosition()).getId_household();
                    int id_room = roomList.get(viewHolder.getAdapterPosition()).getId_room();

                    deleteRoom(id_room, id_household);

                    //refresh recycleviewra
                    roomList.remove(viewHolder.getAdapterPosition());
                    mAdapter.notifyDataSetChanged();

                    Toast.makeText(Main_screen.this, "Miestnosť bola úspešne odstránená.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Main_screen.this, "Nemáte povolenie odstrániť miestnosť.", Toast.LENGTH_SHORT).show();
                mAdapter.notifyDataSetChanged();
            }

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

    //click na miestnost
    @Override
    public void onRoomClick(int position)
    {
        Room_item ri = new Room_item(roomList.get(position).getRoomName(), getRoomImage(roomList.get(position).getRoomType()), roomList.get(position).getId_room());
        SessionManagement sessionManagement = new SessionManagement(Main_screen.this);
        sessionManagement.saveRoomSession(ri);

        Intent intent = new Intent(this, Room_screen.class);
        startActivity(intent);
    }

    //click na edit miestnosti
    @Override
    public void onEditClick(int position)
    {
        editRoomDialog(position);
    }

    //getne vsetky miestnosti pre danu domacnost
    public void getRooms()
    {
        Call<List<Rooms>> call = api.getRooms(householdId);
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
                    roomList.add(position, new Room_item(image, room.getRoomName(), room.getRoomType(), householdId, room.getRoomId(), room.getDevicesCount()));
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
            case "Garáž":
                return R.drawable.garage;
            case "Jedáleň":
                return R.drawable.dinningroom;
            case "Kuchyňa":
                return R.drawable.kitchen;
            case "Kúpeľňa":
                return R.drawable.bathroom;
            case "Obývačka":
                return R.drawable.livingroom;
            case "Pracovňa":
                return R.drawable.office;
            case "Spálňa":
                return R.drawable.bedroom;
            case "Záhrada":
                return R.drawable.garden;
            case "Iné":
                return R.drawable.defaultroom;
        }
        return 0;
    }

    //nastavi hodnoty, ktore sa menia podla prihlaseneho usera
    public void setScreenValues()
    {
        //nastavenie HomeName v headeri appky
        TextView homeName = findViewById(R.id.mainHomeName);
        homeName.setText(login.getHouseholdName());

        //nastavenie UserName v headeri appky
        TextView userName = findViewById(R.id.mainUserName);
        userName.setText(login.getUsername());

        //nastavenie id_household
        householdId = login.getHouseholdId();
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
        SessionManagement sessionManagement = new SessionManagement(Main_screen.this);
        sessionManagement.removeSession();

        moveToLoginScreen();
    }

    public void moveToLoginScreen()
    {
        Intent intent = new Intent(Main_screen.this, Login_screen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
