package com.example.smarthome.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
    private EditText lEmail, lPass;

    //api
    private Api api;
    private int status, householdId, userId, role;
    private String householdName, userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        Button lBtn = findViewById(R.id.loginBtn);
        lEmail = findViewById(R.id.loginEmail);
        lPass = findViewById(R.id.loginPass);
        TextView lRegistration = findViewById(R.id.loginRegisterText);
        TextView lConnect = findViewById(R.id.loginConnectText);

        apiConnection();

        //prihlasenie sa
        lBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String loginName = lEmail.getText().toString();
                String loginPass = lPass.getText().toString();
                boolean success;

                success = validate(loginName, loginPass);

                System.out.println("NAME     " + loginName + "    PASS    " + loginPass);
                loginToDatabase(loginName,loginPass);
                System.out.println("STATUSS   " + status);

                if (success)
                {
                    makeSession();
                    moveToMainActivity();
                }
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

        //pouzivatel je prihlaseny
        if(userLoggedIn != 0)
            moveToMainActivity();
    }

    public void moveToMainActivity()
    {
        Intent intent = new Intent(Login_screen.this, Main_screen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //ulozenie session
    public void makeSession()
    {
        Login login = new Login(status, role, userEmail, userId, householdId, householdName);
        SessionManagement sessionManagement = new SessionManagement(Login_screen.this);
        sessionManagement.saveSession(login);
    }

    //POST login , GET ids & names
    public void loginToDatabase(String useremail, String password)
    {
        Call<Login> call = api.loginUser(useremail, password);

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
                userEmail = response.body().getUsername();
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
    public boolean validate(String name, String pass)
    {
        if (TextUtils.isEmpty(name))
        {
            lEmail.setError("Povinné pole.");
        }

        if (TextUtils.isEmpty(pass))
        {
            lPass.setError("Povinné pole.");
            return false;
        }

        else
            loginToDatabase(name, pass);

        if (status == 1)
            return true;

        else
        {
            Toast.makeText(Login_screen.this, "Nesprávny email, alebo heslo.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}