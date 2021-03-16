package com.example.smarthome.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarthome.connection.Api;
import com.example.smarthome.connection.Login;
import com.example.smarthome.connection.SessionManagement;
import com.example.smarthome.main.Main_screen;
import com.example.smarthome.R;
import com.example.smarthome.registration.Registration_screen;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login_screen extends AppCompatActivity
{
    private Button lBtn;
    private EditText lName, lPass;
    private TextView lRegistration, lConnect;

    //api
    private Api api;
    private int status, householdId, userId, role;
    private String householdName, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        lBtn = findViewById(R.id.loginBtn);
        lName = findViewById(R.id.loginName);
        lPass = findViewById(R.id.loginPass);
        lRegistration = findViewById(R.id.loginRegisterText);
        lConnect = findViewById(R.id.loginConnectText);

        apiConnection();

        //prihlasenie sa
        lBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                loginToApp();
            }
        });

//      prechod na obrazovku registracie
        lRegistration.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), Registration_screen.class));
            }
        });
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

    //pri starte appky sa skontroluje ci so uz prihlaseny (ci mam session)
    @Override
    protected void onStart()
    {
        super.onStart();
        checkSession();
    }

    //if prihlaseny -> Main_screen
    public void checkSession()
    {
        SessionManagement sessionManagement = new SessionManagement(Login_screen.this);
        int userLoggedIn = sessionManagement.getSession();

        if(userLoggedIn != 0)
        {
            //user id logged in and so move to mainActivity
            moveToMainActivity();
        }
    }

    public void moveToMainActivity()
    {
        Intent intent = new Intent(Login_screen.this, Main_screen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //ulozenie session
    public void sessionLogin()
    {
        Login login = new Login(status, role, userName, userId, householdId, householdName);
        SessionManagement sessionManagement = new SessionManagement(Login_screen.this);
        sessionManagement.saveSession(login);
    }

    //POST login , GET ids & names
    public void loginToDatabase(String username, String password)
    {
        Call<Login> call = api.loginUser(username, password);

        call.enqueue(new Callback<Login>()
        {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response)
            {
                if (!response.isSuccessful())
                {
                    System.out.println("call = " + call + ", response = " + response);
                    return;
                }

                status = response.body().getStatus();
                householdId = response.body().getHouseholdId();
                householdName = response.body().getHouseholdName();
                userName = response.body().getUsername();
                userId = response.body().getUserId();
                role = response.body().getRole();
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t)
            {
                System.out.println("call = " + call + ", t = " + t);
            }
        });
    }

    //login screen handling
    public void loginToApp()
    {
        String name = lName.getText().toString().trim();
        String pass = lPass.getText().toString().trim();

        if (TextUtils.isEmpty(name))
        {
            lName.setError("Povinné pole.");
            return;
        }

        else if (TextUtils.isEmpty(pass))
        {
            lPass.setError("Povinné pole.");
            return;
        }

        else
        {
            loginToDatabase(name, pass);
        }

        if (status == 1)
        {
            sessionLogin();
            moveToMainActivity();
        }

        else
        {
            Toast.makeText(Login_screen.this, "Nesprávne používateľské meno, alebo heslo.", Toast.LENGTH_SHORT).show();
        }
    }
}