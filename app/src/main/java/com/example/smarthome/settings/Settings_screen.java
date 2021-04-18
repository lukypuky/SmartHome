package com.example.smarthome.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarthome.connection.Api;
import com.example.smarthome.connection.Login;
import com.example.smarthome.connection.Registration;
import com.example.smarthome.connection.SessionManagement;
import com.example.smarthome.connection.Users;
import com.example.smarthome.login.Login_screen;
import com.example.smarthome.main.Main_screen;
import com.example.smarthome.R;
import com.example.smarthome.main.Room_screen;
import com.example.smarthome.profile.Profile_screen;
import com.example.smarthome.registration.Registration_screen;
import com.example.smarthome.scenarios.Scenario_screen;
import com.google.android.material.navigation.NavigationView;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Settings_screen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    //menu
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    //pridanie usera
    private Button saveUser, unsaveUser;
    private EditText userName, email, password, confPassword;
    private AlertDialog dialog;
    private TextView addUser;

    //dark mode
    private SwitchCompat switchCompat;

    //api
    private Api api;

    //data z login/main screeny
    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);

        apiConnection();

        //session
        SessionManagement sessionManagement = new SessionManagement(Settings_screen.this);
        login =  sessionManagement.getLoginSession();

        SessionManagement darkModeSessionManagement = new SessionManagement(Settings_screen.this);
        Dark_mode darkMode = darkModeSessionManagement.getDarkModeSession();
        ////////////////////////////////////////////////////////////////////////////////////////////

        if (canEdit())
        {
            addUser = findViewById(R.id.settings_add_user);
            addUser.setVisibility(View.VISIBLE);
            addUser.setOnClickListener(v -> addUser());
        }

        switchCompat = findViewById(R.id.settings_dark_mode_swtich);
        if (darkMode.isDark_mode())
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            switchCompat.setChecked(true);
        }

        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            if (isChecked)
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                switchCompat.setChecked(true);
                setDarkMode(true);
            }

            else
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                switchCompat.setChecked(false);
                setDarkMode(false);
            }
        });

        setNavigationView();
    }


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

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

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
                Intent main_intent = new Intent(Settings_screen.this, Main_screen.class);
                startActivity(main_intent);
                break;
            case R.id.profile:
                Intent profile_intent = new Intent(Settings_screen.this, Profile_screen.class);
                startActivity(profile_intent);
                break;
            case R.id.scenario:
                Intent scenario_intent = new Intent(Settings_screen.this, Scenario_screen.class);
                startActivity(scenario_intent);
                break;
            case R.id.settings:
                break;
            case R.id.logout:
                logout();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void initializeDialog()
    {
        AlertDialog.Builder addUserDialog = new AlertDialog.Builder(Settings_screen.this);
        View contactPopupView = getLayoutInflater().inflate(R.layout.add_user_popup, null);

        userName = contactPopupView.findViewById(R.id.settingsUsername);
        email = contactPopupView.findViewById(R.id.settingsEmail);
        password = contactPopupView.findViewById(R.id.settingsPass);
        confPassword = contactPopupView.findViewById(R.id.settingsConfPass);
        saveUser = contactPopupView.findViewById(R.id.saveUserButton);
        unsaveUser = contactPopupView.findViewById(R.id.unsaveUserButton);

        addUserDialog.setView(contactPopupView);
        dialog = addUserDialog.create();
        dialog.show();
    }

    public boolean canEdit()
    {
        if (login.getRole() == 1)
            return true;
        else
            return false;
    }

    private void addUser()
    {
        initializeDialog();

        saveUser.setOnClickListener(v ->
        {
            String username = userName.getText().toString();
            String userEmail = email.getText().toString();
            int householdId = login.getHouseholdId();
            String userPassword = password.getText().toString();
            String userConfPassword = confPassword.getText().toString();
            boolean success;

            success = validate(username, userEmail, userPassword, userConfPassword);

            if (success)
                addUsertoDatabase(username, userEmail, householdId, userPassword);
        });

        unsaveUser.setOnClickListener(v -> dialog.dismiss());
    }

    public boolean validate(String username, String userEmail, String userPassword, String userConfPassword)
    {
        if (TextUtils.isEmpty(username))
        {
            userName.setError("Povinné pole");
            return false;
        }

        if (TextUtils.isEmpty(userEmail))
        {
            email.setError("Povinné pole");
            return false;
        }

        if (TextUtils.isEmpty(userPassword))
        {
            password.setError("Povinné pole");
            return false;
        }

        else if (!userPassword.equals(userConfPassword))
        {
            confPassword.setError("Heslá sa nezhodujú");
            return false;
        }

        return true;
    }

    public void addUsertoDatabase(String username, String userEmail, int householdId, String userPassword)
    {
        Call<Users> call = api.postUserByAdmin(username, userEmail, userPassword, 2, householdId);

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

                int status = response.body().getUserStatus();

                if (status == 1)
                {
                    Toast.makeText(Settings_screen.this, "Používateľ bol vytvorený", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

                else
                    Toast.makeText(Settings_screen.this, "Používateľ s rovnakým emailom už existuje", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t)
            {
                System.out.println("call = " + call + ", t = " + t);
            }
        });
    }

    public void setDarkMode(boolean option)
    {
        Dark_mode dark_mode = new Dark_mode(option);
        SessionManagement sessionManagement = new SessionManagement(Settings_screen.this);
        sessionManagement.saveDarkModeSession(dark_mode);
    }

    //odhlasenie sa z aplikacie (zrusenie session)
    public void logout()
    {
        SessionManagement sessionManagement = new SessionManagement(Settings_screen.this);
        sessionManagement.removeSession();

        moveToLoginScreen();
    }

    public void moveToLoginScreen()
    {
        Intent intent = new Intent(Settings_screen.this, Login_screen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}