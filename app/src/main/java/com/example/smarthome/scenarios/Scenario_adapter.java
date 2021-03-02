package com.example.smarthome.scenarios;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthome.R;

import java.util.ArrayList;

public class Scenario_adapter extends RecyclerView.Adapter<Scenario_adapter.ScenarioViewHolder>
{
    private ArrayList<Scenario_item> mScenarioList;

    public static class ScenarioViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView mImageView;
        public TextView mTextView;


        public ScenarioViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mImageView = itemView.findViewById(R.id.scenarioImageView);
            mTextView = itemView.findViewById(R.id.scenarioTextView);
        }
    }

    //konstruktor
    public Scenario_adapter(ArrayList<Scenario_item> scenarioList)
    {
        this.mScenarioList = scenarioList;
    }

    @NonNull
    @Override
    public Scenario_adapter.ScenarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_scenario_item, parent, false);
        Scenario_adapter.ScenarioViewHolder rvh = new Scenario_adapter.ScenarioViewHolder(v);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull Scenario_adapter.ScenarioViewHolder holder, int position)
    {
        Scenario_item currentItem = mScenarioList.get(position);

        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView.setText(currentItem.getText());
    }

    @Override
    public int getItemCount()
    {
        return mScenarioList.size();
    }
}
