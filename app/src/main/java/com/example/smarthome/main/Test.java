package com.example.smarthome.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.smarthome.R;
import com.example.smarthome.connection.Api;
import com.example.smarthome.connection.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Test extends AppCompatActivity
{
    private TextView testText;
    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        testText = findViewById(R.id.test);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://147.175.121.237/api2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(Api.class);

        getPosts();
    }

    public void getPosts()
    {
        Call<List<Post>> call = api.getPosts();

        call.enqueue(new Callback<List<Post>>()
        {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response)
            {
                if (!response.isSuccessful())
                {
                    testText.setText("Code: " + response.code());
                    return;
                }

                List<Post> posts = response.body();

                for (Post post: posts)
                {
                    String content = "";
                    content += "id: " + post.getUserId() + "\n";
                    content += "name: " + post.getUserName() + "\n";
                    content += "email: " + post.getUserEmail() + "\n\n";
                    testText.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t)
            {
                testText.setText(t.getMessage());
            }
        });
    }
}