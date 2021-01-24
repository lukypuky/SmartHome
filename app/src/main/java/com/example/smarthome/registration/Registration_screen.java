package com.example.smarthome.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.smarthome.R;
import com.example.smarthome.login.Login_screen;
import com.example.smarthome.main.Main_screen;

public class Registration_screen extends AppCompatActivity
{
    private Button rBtn;
    private EditText rName, rHomeName, rEmail, rPass, rConfPass;
    private TextView rLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_screen);

        rBtn = (Button) findViewById(R.id.registerBtn);
        rName = (EditText) findViewById(R.id.registerName);
        rHomeName = (EditText) findViewById(R.id.registerHomeName);
        rEmail = (EditText) findViewById(R.id.registerEmail);
        rPass = (EditText) findViewById(R.id.registerPass);
        rConfPass = (EditText) findViewById(R.id.registerConfPass);
        rLogin = (TextView) findViewById(R.id.registerLoginText);

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