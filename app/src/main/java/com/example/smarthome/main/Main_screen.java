package com.example.smarthome.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthome.R;
import com.example.smarthome.profile.Profile_screen;
import com.example.smarthome.schemas.Schemas_screen;
import com.example.smarthome.settings.Settings_screen;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class Main_screen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    //menu
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    //pridanie miestnosti
    private Button addRoom;
    private AlertDialog.Builder addRoomDialog;
    private AlertDialog dialog;
    private EditText roomName;
    private Spinner roomType;
    private Button saveRoom;
    private Button unsaveRoom;

    //miestnosti
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter; // bridge medzi datami a recycler view
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        //tlacidlo na pridanie novej miestnosti
        addRoom = (Button) findViewById(R.id.addRoom);
        addRoom.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                addRoomDialog();
            }
        });

        //miesnosti ktore si vie user pridat
        ArrayList<RoomItem> roomList = new ArrayList<>();
        roomList.add(new RoomItem(R.drawable.kitchen,"Kuchyňa","blabla"));
        roomList.add(new RoomItem(R.drawable.kitchen,"Kuchyňa","baaaaaaaaaa"));
        roomList.add(new RoomItem(R.drawable.kitchen,"Kuchyňa","ggggggggggg"));
        roomList.add(new RoomItem(R.drawable.kitchen,"Kuchyňa","base qwfqwfasla"));
        roomList.add(new RoomItem(R.drawable.livingroom,"Obývačka","asdasdas"));

        //plocha pre miestnosti v domacnosti
        mRecyclerView = findViewById(R.id.mainRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RoomAdapter(roomList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        //bocny navigacny panel
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
            case R.id.schema:
                Intent schema_intent = new Intent(Main_screen.this,Schemas_screen.class);
                startActivity(schema_intent);
                break;
            case R.id.settings:
                Intent settings_intent = new Intent(Main_screen.this, Settings_screen.class);
                startActivity(settings_intent);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //metoda na pridanie novej miestnosti v domacnosti
    public void addRoomDialog()
    {
        addRoomDialog = new AlertDialog.Builder(Main_screen.this);
        View contactPopupView = getLayoutInflater().inflate(R.layout.activity_add_room_popup, null);

        roomName = (EditText) contactPopupView.findViewById(R.id.roomName);
        saveRoom = (Button) contactPopupView.findViewById(R.id.saveRoomButton);
        unsaveRoom = (Button) contactPopupView.findViewById(R.id.unsaveRoomButton);

        roomType = (Spinner) contactPopupView.findViewById(R.id.roomType);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Main_screen.this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.rooms));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomType.setAdapter(adapter);

        addRoomDialog.setView(contactPopupView);
        dialog = addRoomDialog.create();
        dialog.show();

        saveRoom.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (roomName.getText().toString().isEmpty())
                {
                    Toast.makeText(Main_screen.this, "Zadajte názov pre miestnosť", Toast.LENGTH_SHORT).show();
                }

                else if(roomType.getSelectedItem().toString().equals("Typ miestnosti"))
                {
                    Toast.makeText(Main_screen.this, "Zvoľte typ miestnosti", Toast.LENGTH_SHORT).show();
                    //vypis message nech sa zvoli miestnost
                }

                else
                {
                    Toast.makeText(Main_screen.this, "Miestnosť pridaná", Toast.LENGTH_SHORT).show();
                }
            }
        });

        unsaveRoom.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
    }
}