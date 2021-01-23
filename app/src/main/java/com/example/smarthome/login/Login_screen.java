package com.example.smarthome.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.smarthome.main.Main_screen;
import com.example.smarthome.R;

public class Login_screen extends AppCompatActivity
{
    private Button lBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        lBtn = (Button) findViewById(R.id.loginBtn);

        lBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), Main_screen.class));
            }
        });
    }
}