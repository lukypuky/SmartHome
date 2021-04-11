package com.example.smarthome.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarthome.R;
import com.example.smarthome.connection.Api;
import com.example.smarthome.connection.Login;
import com.example.smarthome.connection.Registration;
import com.example.smarthome.login.Login_screen;
import com.example.smarthome.main.Main_screen;
import com.example.smarthome.profile.Profile_screen;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;

public class Registration_screen extends AppCompatActivity
{
    private EditText rName, rHomeName, rEmail, rPass, rConfPass;

    //api
    private Api api;
    private int status;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_screen);

        Button rBtn = findViewById(R.id.registerBtn);
        rName = findViewById(R.id.registerName);
        rHomeName = findViewById(R.id.registerHomeName);
        rEmail = findViewById(R.id.registerEmail);
        rPass = findViewById(R.id.registerPass);
        rConfPass = findViewById(R.id.registerConfPass);
        TextView rLogin = findViewById(R.id.registerLoginText);

        //pripojenie sa na api
        apiConnection();

        rBtn.setOnClickListener(v ->
        {
            String username = rName.getText().toString();
            String email = rEmail.getText().toString();
            String householdname = rHomeName.getText().toString();
            String password = rPass.getText().toString();
            String confPassword = rConfPass.getText().toString();
            boolean success;

            success = validate(username, email, householdname, password, confPassword);

            if (success)
                registrateToDatabase(username, email, householdname, password);
        });

        rLogin.setOnClickListener(v -> moveToLoginScreen());
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

    public void moveToLoginScreen()
    {
        Intent intent = new Intent(Registration_screen.this, Login_screen.class);
        startActivity(intent);
    }

    //POST registration
    public void registrateToDatabase(String username, String email, String householdname, String password)
    {
        Call<Registration> call = api.postUser(username, email, householdname, password);

        call.enqueue(new Callback<Registration>()
        {
            @Override
            public void onResponse(Call<Registration> call, Response<Registration> response)
            {
                if (!response.isSuccessful())
                {
                    System.out.println("call = " + call + ", response = " + response);
                    return;
                }

                status = response.body().getStatus();

                if (status == 1)
                {
                    Toast.makeText(Registration_screen.this, "Registrácia úspešná", Toast.LENGTH_SHORT).show();
                    moveToLoginScreen();
                }

                else
                    Toast.makeText(Registration_screen.this, "Domácnosť s rovnakým menom už existuje", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Registration> call, Throwable t)
            {
                System.out.println("call = " + call + ", t = " + t);
            }
        });
    }

    //registration screen handling
    public boolean validate(String username, String email, String householdname, String password, String confPass)
    {
        if (TextUtils.isEmpty(username))
        {
            rName.setError("Povinné pole");
            return false;
        }

        if (TextUtils.isEmpty(email))
        {
            rEmail.setError("Povinné pole");
            return false;
        }

        if (TextUtils.isEmpty(householdname))
        {
            rHomeName.setError("Povinné pole");
            return false;
        }

        if (TextUtils.isEmpty(password))
        {
            rPass.setError("Povinné pole");
            return false;
        }

        if (TextUtils.isEmpty(confPass))
        {
            rConfPass.setError("Povinné pole");
            return false;
        }

        else if (!password.equals(confPass))
        {
            rConfPass.setError("Heslá sa nezhodujú");
            return false;
        }

        return true;

//        else
//            registrateToDatabase(username, email, householdname, password);
//
//        if (status == 1)
//            return true;
//
//        else
//        {
//            Toast.makeText(Registration_screen.this, "Domácnosť s rovnakým menom už existuje.", Toast.LENGTH_SHORT).show();
//            return false;
//        }
    }
}