package com.example.smarthome.scenarios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.smarthome.connection.Api;
import com.example.smarthome.connection.Login;
import com.example.smarthome.connection.SessionManagement;
import com.example.smarthome.login.Login_screen;
import com.example.smarthome.main.Main_screen;
import com.example.smarthome.R;
import com.example.smarthome.profile.Profile_screen;
import com.example.smarthome.settings.Settings_screen;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Scenario_screen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    //menu
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    //pridanie scenara
//    private AlertDialog dialog;
//    private EditText scenarioName;
//    private Button nextScenario, backScenario;

    //zoznam scenarov
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter; // bridge medzi datami a recycler view
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<Scenario_item> scenarioList;

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
        ////////////////////////////////////////////////////////////////////////////////////////////

        createScenarioList();

        //tlacidlo na pridanie noveho scenara
        FloatingActionButton addScenario = (FloatingActionButton) findViewById(R.id.addScenario);
        addScenario.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Scenario_screen.this, Scenario_add_screen_one.class);
                startActivity(intent);
            }
        });

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
        mAdapter = new Scenario_adapter(scenarioList);

        mRecyclerView.setLayoutManager(mLayoutManager);
//        new ItemTouchHelper(scenarioToRemove).attachToRecyclerView(mRecyclerView);
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

//    //prida scenar na index 0 v arrayliste
//    public void insertScenario(EditText scenarioName)
//    {
//        String name = scenarioName.getText().toString();
//        int position = 0;
//
//        scenarioList.add(position, new Scenario_item(R.drawable.scenario_icon,name));
//        mAdapter.notifyItemInserted(position);
//    }

    //metoda na pridanie noveho scenara v domacnosti
//    public void addScenarioFirstDialog()
//    {
//        initializeFirstDialog();
//
//        //potvrdenie pridania scenara
//        nextScenario.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
////                //ak sa nevyplnil nazov scenara -> message
////                if (scenarioName.getText().toString().isEmpty())
////                {
////                    Toast.makeText(Scenario_screen.this, "Zadajte názov pre scenár", Toast.LENGTH_SHORT).show();
////                }
////
////                //ak je vsetko vyplnene, pridaj scenar do arraylistu
////                else
////                {
////                    insertScenario(scenarioName);
////                    Toast.makeText(Scenario_screen.this, "Scenár pridaný", Toast.LENGTH_SHORT).show();
////                    dialog.dismiss();
////                }
//            }
//        });
//
//        //zrusenie pridania scenara
//        backScenario.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                dialog.dismiss();
//            }
//        });
//    }

//    public void initializeFirstDialog()
//    {
//        AlertDialog.Builder addScenarioDialog = new AlertDialog.Builder(Scenario_screen.this);
//        View contactPopupView = getLayoutInflater().inflate(R.layout.add_scenario_first_screen, null);
//
//        nextScenario =  contactPopupView.findViewById(R.id.nextScenarioButton);
//        backScenario =  contactPopupView.findViewById(R.id.unsaveScenarioButton);
//
//        addScenarioDialog.setView(contactPopupView);
//        dialog = addScenarioDialog.create();
//        dialog.show();
//    }

//    //mazanie scenara (with swap right)
//    ItemTouchHelper.SimpleCallback scenarioToRemove = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT)
//    {
//        @Override
//        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target)
//        {
//            return false;
//        }
//
//        @Override
//        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
//        {
//            //builder na potvrdenie zmazania
//            AlertDialog.Builder builder = new AlertDialog.Builder(Scenario_screen.this);
//            builder.setCancelable(true);
//            builder.setMessage("Naozaj chcete odstrániť tento scenár '" + scenarioList.get(viewHolder.getAdapterPosition()).getText().toUpperCase() + "' ?");
//            builder.setPositiveButton("Áno", new DialogInterface.OnClickListener()
//            {
//                @Override
//                public void onClick(DialogInterface dialog, int which)
//                {
//                    scenarioList.remove(viewHolder.getAdapterPosition());
//                    mAdapter.notifyDataSetChanged();
//                }
//            });
//
//            builder.setNegativeButton("Nie", new DialogInterface.OnClickListener()
//            {
//                @Override
//                public void onClick(DialogInterface dialog, int which)
//                {
//                    dialog.dismiss();
//                    mAdapter.notifyDataSetChanged();
//                }
//            });
//
//            AlertDialog dialog = builder.create();
//            dialog.show();
//        }
//    };

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