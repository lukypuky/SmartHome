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
    private final ArrayList<Scenario_item> mScenarioList;
    private final OnScenarioListener mOnScenarioListener;

    public class ScenarioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public ImageView mImageView;
        public TextView mTextView;
        OnScenarioListener onScenarioListener;

        public ScenarioViewHolder(@NonNull View itemView, OnScenarioListener onScenarioListener)
        {
            super(itemView);
            mImageView = itemView.findViewById(R.id.scenarioImageView);
            mTextView = itemView.findViewById(R.id.scenarioTextView);
            this.onScenarioListener = onScenarioListener;

            itemView.setOnClickListener(this);
        }

        public void onClick(View v)
        {
            onScenarioListener.onScenarioClick(getAdapterPosition());
        }
    }

    //konstruktor
    public Scenario_adapter(ArrayList<Scenario_item> scenarioList, OnScenarioListener onScenarioListener)
    {
        this.mScenarioList = scenarioList;
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
        holder.mTextView.setText(currentItem.getText());
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
    }
}
