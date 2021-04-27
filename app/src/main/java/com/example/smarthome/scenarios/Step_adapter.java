package com.example.smarthome.scenarios;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smarthome.R;
import java.util.ArrayList;

public class Step_adapter extends RecyclerView.Adapter<Step_adapter.StepViewHolder>
{
    private final ArrayList<Step_item> stepList;
    private final OnStepListener mOnStepListener;

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView stepName;
        OnStepListener onStepListener;

        public StepViewHolder(@NonNull View itemView, OnStepListener onStepListener)
        {
            super(itemView);
            stepName = itemView.findViewById(R.id.stepName);
            this.onStepListener = onStepListener;
        }

        public void onClick(View v)
        {
            onStepListener.onStepClick(getAdapterPosition());
        }
    }

    //konstruktor
    public Step_adapter(ArrayList<Step_item> stepList, OnStepListener onStepListener)
    {
        this.stepList = stepList;
        this.mOnStepListener = onStepListener;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_step_item, parent, false);
        StepViewHolder svh = new StepViewHolder(view, mOnStepListener);
        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position)
    {
        Step_item currentItem = stepList.get(position);
        holder.stepName.setText(currentItem.getStepName());
    }

    @Override
    public int getItemCount()
    {
        return stepList.size();
    }


    public interface OnStepListener
    {
        void onStepClick(int position);
    }
}

