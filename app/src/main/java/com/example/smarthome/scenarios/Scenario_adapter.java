package com.example.smarthome.scenarios;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthome.R;
import com.example.smarthome.connection.Api;
import com.example.smarthome.connection.Login;
import com.example.smarthome.connection.Scenarios;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Scenario_adapter extends RecyclerView.Adapter<Scenario_adapter.ScenarioViewHolder>
{
    private final ArrayList<Scenario_item> mScenarioList;
    private final OnScenarioListener mOnScenarioListener;
    private final Login login;
    private Context context;

    //api
    private Api api;

    public class ScenarioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public ImageView mImageView, mImageEdit;
        public TextView mDeviceName, mDeviceExecutingType, mDeviceExecutable;
        public Button mExecuteBtn;
        OnScenarioListener onScenarioListener;

        public ScenarioViewHolder(@NonNull View itemView, OnScenarioListener onScenarioListener)
        {
            super(itemView);
            mImageView = itemView.findViewById(R.id.scenarioImageView);
            mImageEdit = itemView.findViewById(R.id.scenarioEdit);
            mDeviceName = itemView.findViewById(R.id.scenarioTextView);
            mDeviceExecutingType = itemView.findViewById(R.id.scenarioType);
            mDeviceExecutable = itemView.findViewById(R.id.scenarioExecutable);
            mExecuteBtn = itemView.findViewById(R.id.scenarioExecuteBtn);
            this.onScenarioListener = onScenarioListener;

            itemView.setOnClickListener(this);
            apiConnection();

            mExecuteBtn.setOnClickListener(v ->
            {
                int position = getAdapterPosition();
                executeScenario(position);
            });

            if (canEdit())
            {
                mImageEdit.setOnClickListener(v ->
                {
                    if (onScenarioListener != null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                            onScenarioListener.onEditClick(position);
                    }
                });
            }

            else    // ak user nema admin rolu, ikona editu je invisible
                mImageEdit.setVisibility(View.INVISIBLE);
        }

        public void onClick(View v)
        {
            onScenarioListener.onScenarioClick(getAdapterPosition());
        }
    }

    //konstruktor
    public Scenario_adapter(ArrayList<Scenario_item> scenarioList, Login login, OnScenarioListener onScenarioListener)
    {
        this.mScenarioList = scenarioList;
        this.login = login;
        this.mOnScenarioListener = onScenarioListener;
    }

    @NonNull
    @Override
    public ScenarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_scenario_item, parent, false);
        ScenarioViewHolder svh = new ScenarioViewHolder(view, mOnScenarioListener);
        context = parent.getContext();
        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull ScenarioViewHolder holder, int position)
    {
        Scenario_item currentItem = mScenarioList.get(position);

        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mDeviceName.setText(currentItem.getScenarioName());
        holder.mDeviceExecutingType.setText(setScenarioType(currentItem.getScenarioType()));

        if (currentItem.getScenarioType().equals("auto"))
        {
            holder.mDeviceExecutable.setText(setExecutableScenario(currentItem.getScenarioExecutable()));
            if (currentItem.getScenarioExecutable() == 1)
                holder.mExecuteBtn.setText("Deaktivovať scenár");
            else
                holder.mExecuteBtn.setText("Aktivovať scenár");
        }

        else
            holder.mExecuteBtn.setText("Aktivovať scenár");

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

    public boolean canEdit()
    {
        if (login.getRole() == 1)
            return true;
        else
            return false;
    }

    @Override
    public int getItemCount()
    {
        return mScenarioList.size();
    }

    //interface pre klikanie na scenare
    public interface OnScenarioListener
    {
        void onScenarioClick(int position); //otvori scenar
        void onEditClick(int position);
    }

    public String setScenarioType(String type)
    {
        if (type.equals("auto"))
            return "automatický";
        else
            return "manuálny";
    }

    public String setExecutableScenario(int executable)
    {
        if (executable == 1)
            return "(Aktívny)";
        else
            return "(Neaktívny)";
    }

    public void executeScenario(int position)
    {
        Call<Scenarios> call;
        Scenario_item currentItem = mScenarioList.get(position);

        if (currentItem.getScenarioExecutable() == 0)
        {
            call = api.editScenario(currentItem.getScenarioId(), currentItem.getScenarioName(), currentItem.getScenarioType(), currentItem.getId_room(), currentItem.getSensorId(),1,
                    currentItem.getIsRunning(), currentItem.getValue(), currentItem.getTime(), login.getHouseholdId());
        }

        else
        {
            call = api.editScenario(currentItem.getScenarioId(), currentItem.getScenarioName(), currentItem.getScenarioType(), currentItem.getId_room(), currentItem.getSensorId(),0,
                    currentItem.getIsRunning(), currentItem.getValue(), currentItem.getTime(), login.getHouseholdId());
        }


        call.enqueue(new Callback<Scenarios>()
        {
            @Override
            public void onResponse(Call<Scenarios> call, Response<Scenarios> response)
            {
                if (!response.isSuccessful())
                {
                    System.out.println("call = " + call + ", response = " + response);
                }

                if (response.body().getStatus() == 1)
                {
                    Toast.makeText(context, "Scenár bol nastavený", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(context, Scenario_screen.class);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            }

            @Override
            public void onFailure(Call<Scenarios> call, Throwable t)
            {
                System.out.println("call = " + call + ", t = " + t);
            }
        });
    }
}
