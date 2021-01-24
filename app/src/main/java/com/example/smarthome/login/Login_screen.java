package com.example.smarthome.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.smarthome.main.Main_screen;
import com.example.smarthome.R;
import com.example.smarthome.registration.Registration_screen;

public class Login_screen extends AppCompatActivity
{
    private Button lBtn;
    private EditText lName, lPass;
    private TextView lRegistration, lConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        lBtn = (Button) findViewById(R.id.loginBtn);
        lName = (EditText) findViewById(R.id.loginName);
        lPass = (EditText) findViewById(R.id.loginPass);
        lRegistration = (TextView) findViewById(R.id.loginRegisterText);
        lConnect = (TextView) findViewById(R.id.loginConnectText);

        //prihlasenie sa
        lBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), Main_screen.class));
            }
        });

        //prechod na obrazovku registracie
        lRegistration.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), Registration_screen.class));
            }
        });
    }
}