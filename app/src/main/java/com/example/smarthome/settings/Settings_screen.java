package com.example.smarthome.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.EditText;
import android.widget.Spinner;
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
    private Button addUserBtn;
    private EditText userName, email, password, confPassword;
    private Button saveUser, unsaveUser;
    private AlertDialog dialog;

//    private TextView title, languageText, languageDialog;
//    private Spinner language;

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
        ////////////////////////////////////////////////////////////////////////////////////////////

        if (canEdit())
        {
            addUserBtn = findViewById(R.id.settings_add_user_btn);
            addUserBtn.setVisibility(View.VISIBLE);
            addUserBtn.setOnClickListener(v -> addUser());
        }

//        title = findViewById(R.id.options_title);
//        languageText = findViewById(R.id.options_language_text);
//        languageDialog = findViewById(R.id.options_language_type);
//
//        languageDialog.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                changeLanguage();
//            }
//        });

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

//    public void changeLanguage()
//    {
//        //pole jazykov
//        final String[] languageItems = {"Slovenský", "English"};
//        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Settings_screen.this);
//
//        mBuilder.setSingleChoiceItems(languageItems, -1, new DialogInterface.OnClickListener()
//        {
//            @Override
//            public void onClick(DialogInterface dialog, int which)
//            {
//                if (which == 0)
//                {
//                    setAppLocale("sk");
//                    recreate();
//                }
//
//                if (which == 1)
//                {
//                    setAppLocale("en");
//                    recreate();
//                }
//
//                //zrusi dialogove okno ak sa vybral jazyk
//                dialog.dismiss();
//            }
//        });
//
//        AlertDialog mDialog = mBuilder.create();
//        //show alert dialog
//        mDialog.show();
//    }

//    private void setAppLocale(String localeCode)
//    {
//        Resources res = getResources();
//        DisplayMetrics dm = res.getDisplayMetrics();
//        Configuration conf = res.getConfiguration();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
//        {
//            conf.setLocale(new Locale(localeCode.toLowerCase()));
//        }
//
//        else
//        {
//            conf.locale = new Locale(localeCode.toLowerCase());
//        }
//
//        res.updateConfiguration(conf,dm);
//    }

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

        navigationView.setCheckedItem(R.id.settings);
    }

    @Override
    public void onBackPressed()
    {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer((GravityCompat.START));

        else
            super.onBackPressed();
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