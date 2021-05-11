package com.example.smarthome.scenarios;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthome.R;
import com.example.smarthome.connection.Login;

import java.util.ArrayList;

public class Scenario_adapter extends RecyclerView.Adapter<Scenario_adapter.ScenarioViewHolder>
{
    private final ArrayList<Scenario_item> mScenarioList;
    private final OnScenarioListener mOnScenarioListener;
    private final Login login;

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

            mExecuteBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    System.out.println("CLICK " + mDeviceName.getText().toString());
                }
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
            holder.mDeviceExecutable.setText(setExecutableScenario(currentItem.getScenarioExecutable()));
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
}
