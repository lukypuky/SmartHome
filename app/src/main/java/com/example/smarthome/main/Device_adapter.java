package com.example.smarthome.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthome.R;

import java.util.ArrayList;

public class Device_adapter extends RecyclerView.Adapter<Device_adapter.DeviceViewHolder>
{
    private ArrayList<Device_item> mDeviceList;

    public static class DeviceViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView mImageView;
        public TextView mTextView;

        public DeviceViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mImageView = itemView.findViewById(R.id.deviceImageView);
            mTextView = itemView.findViewById(R.id.deviceTextView);
        }
    }

    //konstruktor
    public Device_adapter(ArrayList<Device_item> deviceList)
    {
        this.mDeviceList = deviceList;
    }

    @NonNull
    @Override
    public Device_adapter.DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_device_item, parent, false);
        Device_adapter.DeviceViewHolder rvh = new Device_adapter.DeviceViewHolder(v);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull Device_adapter.DeviceViewHolder holder, int position)
    {
        Device_item currentItem = mDeviceList.get(position);

        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView.setText(currentItem.getText());
    }

    @Override
    public int getItemCount()
    {
        return mDeviceList.size();
    }
}
