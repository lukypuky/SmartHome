package com.example.smarthome.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.smarthome.R;
import com.example.smarthome.connection.Api;
import com.example.smarthome.login.Login_screen;
import com.example.smarthome.main.Main_screen;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Registration_screen extends AppCompatActivity
{
    private Button rBtn;
    private EditText rName, rHomeName, rEmail, rPass, rConfPass;
    private TextView rLogin;

    //api
    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_screen);

        rBtn = findViewById(R.id.registerBtn);
        rName = findViewById(R.id.registerName);
        rHomeName = findViewById(R.id.registerHomeName);
        rEmail = findViewById(R.id.registerEmail);
        rPass = findViewById(R.id.registerPass);
        rConfPass = findViewById(R.id.registerConfPass);
        rLogin = findViewById(R.id.registerLoginText);

        //pripojenie sa na api
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://147.175.121.237/api2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(Api.class);

        rLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), Login_screen.class));
            }
        });
    }
}