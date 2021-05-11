package com.example.smarthome.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarthome.connection.Api;
import com.example.smarthome.connection.Login;
import com.example.smarthome.connection.Rooms;
import com.example.smarthome.connection.SessionManagement;
import com.example.smarthome.connection.Users;
import com.example.smarthome.login.Login_screen;
import com.example.smarthome.main.Main_screen;
import com.example.smarthome.R;
import com.example.smarthome.main.Room_item;
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

public class Profile_screen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    //menu
    private DrawerLayout drawerLayout;

    //udaje o userovi
    private EditText userName, householdName, email, password;
    private String stringUserName, stringEmail, stringPassword, stringHouseholdName;
    private int intRole, userId, userHouseholdId;
    private Spinner profileRole;
    private List<Users> users;
    private ArrayList<Room_item> roomList;

    //api
    private Api api;

    //edit
    private FloatingActionButton editProfile, saveProfile, cancelProfile;
    private Animation fabOpen, fabClose, fabRClockwise, fabRAnticlockwise;
    boolean isOpen = false;

    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);

        apiConnection();

        //session
        SessionManagement sessionManagement = new SessionManagement(Profile_screen.this);
        login =  sessionManagement.getLoginSession();

        SessionManagement darkModeSessionManagement = new SessionManagement(Profile_screen.this);
        Dark_mode darkMode = darkModeSessionManagement.getDarkModeSession();
        ////////////////////////////////////////////////////////////////////////////////////////////

        if (darkMode.isDark_mode())
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        userId = login.getUserId();
        userHouseholdId = login.getHouseholdId();

        householdName = findViewById(R.id.profileHome);
        householdName.setText(login.getHouseholdName());
        stringHouseholdName = login.getHouseholdName();

        getUserInfo();
        setEditButtons();

        editProfile.setOnClickListener(v ->
        {
            if (isOpen)
                closeEditButtons();

            else
                openEditButtons();
        });

        saveProfile.setOnClickListener(v ->
        {
            editUserProfile();
        });

        cancelProfile.setOnClickListener(v ->
        {
            closeEditButtons();
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
        NavigationView navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        //nastavenie nazvu domacnosti a pouzivatela v headri menu
        View headerView = navigationView.getHeaderView(0);

        TextView headerHousehold = headerView.findViewById(R.id.headerHousehold);
        TextView headerUsername = headerView.findViewById(R.id.headerUserName);

        headerHousehold.setText(login.getHouseholdName());
        headerUsername.setText(login.getUsername());

        navigationView.bringToFront();
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.mainScreen:
                Intent main_intent = new Intent(Profile_screen.this, Main_screen.class);
                startActivity(main_intent);
                break;
            case R.id.profile:
                break;
            case R.id.scenario:
                Intent scenario_intent = new Intent(Profile_screen.this, Scenario_screen.class);
                startActivity(scenario_intent);
                break;
            case R.id.settings:
                Intent settings_intent = new Intent(Profile_screen.this, Settings_screen.class);
                startActivity(settings_intent);
                break;
            case R.id.logout:
                logout();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //nastavi hodnoty, ktore sa menia podla prihlaseneho usera
    public void setScreenValues()
    {
        userName = findViewById(R.id.profileName);
        userName.setText(stringUserName);

        password = findViewById(R.id.profilePassword);
        password.setText(stringPassword);

        email = findViewById(R.id.profileEmail);
        email.setText(stringEmail);

        profileRole = findViewById(R.id.profileRole);

        profileRole = findViewById(R.id.profileRole);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Profile_screen.this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.roles));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profileRole.setAdapter(adapter);

        String stringRole = getRole();
        profileRole.setSelection(getIndexOfSpinner(profileRole, stringRole));

        userName.setEnabled(false);
        householdName.setEnabled(false);
        email.setEnabled(false);
        password.setEnabled(false);
        profileRole.setEnabled(false);
    }

    //getne zvysne info o pouzivatelovi, ktore sme nedostali pri logine
    public void getUserInfo()
    {
        Call<List<Users>> call = api.getUser(login.getUserId(), login.getHouseholdId());
        call.enqueue(new Callback<List<Users>>()
        {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response)
            {
                if (!response.isSuccessful())
                {
                    System.out.println("call = " + call + ", response = " + response);
                    return;
                }

                users = response.body();

                for (Users user: users)
                {
                    stringUserName = user.getUserName();
                    stringEmail = user.getUserEmail();
                    stringPassword = user.getUserPassword();
                    intRole = user.getUserRole();

                    setScreenValues();
                }
            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t)
            {
                System.out.println("call = " + call + ", t = " + t);
            }
        });
    }

    //get role z id_role
    public String getRole()
    {
        if (intRole == 1)
            return "Admin";

        else
            return "Bežný používateľ";
    }

    public void getValuesFromUser()
    {
        stringUserName = userName.getText().toString();
        stringEmail = email.getText().toString();
        stringPassword = password.getText().toString();

        String tmpRoleString = profileRole.getSelectedItem().toString();
        if (tmpRoleString.equals("Admin"))
            intRole = 1;
        else
            intRole = 2;
    }

    public void setEditButtons()
    {
        editProfile = findViewById(R.id.profileEdit);
        saveProfile = findViewById(R.id.profileOk);
        cancelProfile = findViewById(R.id.profileCancel);

        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fabRClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        fabRAnticlockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);
    }

    public void closeEditButtons()
    {
        userName.setEnabled(false);
        email.setEnabled(false);
        password.setEnabled(false);
        profileRole.setEnabled(false);

        saveProfile.startAnimation(fabClose);
        cancelProfile.startAnimation(fabClose);
        editProfile.startAnimation(fabRClockwise);

        saveProfile.setClickable(false);
        cancelProfile.setClickable(false);

        isOpen = false;

        password.setTransformationMethod(PasswordTransformationMethod.getInstance());

        Intent profile_intent = new Intent(Profile_screen.this, Profile_screen.class);
        startActivity(profile_intent);
        Profile_screen.this.finish();
    }

    public void openEditButtons()
    {
        userName.setEnabled(true);
        email.setEnabled(true);
        password.setEnabled(true);

        saveProfile.startAnimation(fabOpen);
        cancelProfile.startAnimation(fabOpen);
        editProfile.startAnimation(fabRAnticlockwise);

        saveProfile.setClickable(true);
        cancelProfile.setClickable(true);

        isOpen = true;

        password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
    }

    public void editUserProfile()
    {
        getValuesFromUser();
        Call<Users> call = api.editProfile(userId, stringUserName, stringEmail, stringPassword, intRole, userHouseholdId);

        call.enqueue(new Callback<Users>()
        {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response)
            {
                if (!response.isSuccessful())
                {
                    System.out.println("call = " + call + ", response = " + response);
                    return;
                }

                if (response.body().getUserStatus() == 1)
                    Toast.makeText(Profile_screen.this, "Profil bol zmenený", Toast.LENGTH_SHORT).show();

                Login login = new Login(userId, stringUserName, stringEmail, userHouseholdId, stringHouseholdName, intRole);
                SessionManagement sessionManagement = new SessionManagement(Profile_screen.this);
                sessionManagement.saveLoginSession(login);

                Intent profile_intent = new Intent(Profile_screen.this, Profile_screen.class);
                startActivity(profile_intent);
                Profile_screen.this.finish();
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t)
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
        SessionManagement sessionManagement = new SessionManagement(Profile_screen.this);
        sessionManagement.removeSession();

        moveToLoginScreen();
    }

    public void moveToLoginScreen()
    {
        Intent intent = new Intent(Profile_screen.this, Login_screen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
