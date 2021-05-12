package com.example.smarthome.scenarios;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smarthome.R;
import com.example.smarthome.connection.Api;
import com.example.smarthome.connection.Login;

import java.util.ArrayList;

public class Step_adapter extends RecyclerView.Adapter<Step_adapter.StepViewHolder>
{
    private final ArrayList<Step_item> stepList;
    private final OnStepListener mOnStepListener;
    private final Login login;

    //api
    private Api api;

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public ImageView mImageEdit;
        public TextView stepName;
        OnStepListener onStepListener;

        public StepViewHolder(@NonNull View itemView, OnStepListener onStepListener)
        {
            super(itemView);
            stepName = itemView.findViewById(R.id.stepName);
            mImageEdit = itemView.findViewById(R.id.stepEdit);
            this.onStepListener = onStepListener;

            if (canEdit())
            {
                mImageEdit.setOnClickListener(v ->
                {
                    if (onStepListener != null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                            onStepListener.onEditClick(position);
                    }
                });
            }

            else    // ak user nema admin rolu, ikona editu je invisible
                mImageEdit.setVisibility(View.INVISIBLE);
        }

        public void onClick(View v)
        {
            onStepListener.onStepClick(getAdapterPosition());
        }
    }

    //konstruktor
    public Step_adapter(ArrayList<Step_item> stepList, Login login, OnStepListener onStepListener)
    {
        this.stepList = stepList;
        this.login = login;
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
        return stepList.size();
    }

    public interface OnStepListener
    {
        void onStepClick(int position);
        void onEditClick(int position);
    }
}

