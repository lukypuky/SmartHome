package com.example.smarthome.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smarthome.R;

public class Room_screen extends AppCompatActivity
{
    private static final String TAG = "Room_screen";

    private TextView textView;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_screen);

        textView = (TextView)findViewById(R.id.roomText);
        img = (ImageView)findViewById(R.id.testId);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null)
        {
            String string = (String) bundle.get("roomName");
            String image = getIntent().getStringExtra("roomImg");

            img.setImageResource(Integer.parseInt(image));
            textView.setText(string);

//            System.out.println("toto je string: " + string);
        }
    }
}