package com.example.smarthome.devices;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthome.R;
import com.example.smarthome.connection.Login;

import java.util.ArrayList;

public class Device_adapter extends RecyclerView.Adapter<Device_adapter.DeviceViewHolder>
{
    private ArrayList<Device_item> mDeviceList;
    private final OnDeviceListener mOnDeviceListener;
    private final Login login;

    public class DeviceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public ImageView mImageView;
        public TextView mTextView, deviceValue, deviceUnit, deviceStatus;
        OnDeviceListener onDeviceListener;
        public ImageView mImageEdit;
        public ImageView mImageActive;

        public DeviceViewHolder(@NonNull View itemView, OnDeviceListener onDeviceListener)
        {
            super(itemView);
            mImageView = itemView.findViewById(R.id.deviceImageView);
            mTextView = itemView.findViewById(R.id.deviceTextView);
            mImageEdit = itemView.findViewById(R.id.deviceEdit);
            mImageActive = itemView.findViewById(R.id.deviceActive);
            deviceValue = itemView.findViewById(R.id.deviceValue);
            deviceStatus = itemView.findViewById(R.id.deviceStatusTag);
            deviceUnit = itemView.findViewById(R.id.deviceUnitTag);
            this.onDeviceListener = onDeviceListener;

            itemView.setOnClickListener(this);

            if (canEdit())
            {
                //listener na obrazok "editu"
                mImageEdit.setOnClickListener(v ->
                {
                    if (onDeviceListener != null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                            onDeviceListener.onEditClick(position);
                    }
                });
            }

            else    // ak user nema admin rolu, ikona editu je invisible
                mImageEdit.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onClick(View v)
        {
            onDeviceListener.onDeviceClick(getAdapterPosition());
        }
    }

    public boolean canEdit()
    {
        if (login.getRole() == 1)
            return true;
        else
            return false;
    }

    //konstruktor
    public Device_adapter(ArrayList<Device_item> deviceList, Login login, OnDeviceListener onDeviceListener)
    {
        this.mDeviceList = deviceList;
        this.login = login;
        this.mOnDeviceListener = onDeviceListener;
    }

    @NonNull
    @Override
    public Device_adapter.DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_device_item, parent, false);
        DeviceViewHolder rvh = new DeviceViewHolder(view, mOnDeviceListener);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull Device_adapter.DeviceViewHolder holder, int position)
    {
        Device_item currentItem = mDeviceList.get(position);

        String unit, type;
        boolean active;

        unit = getDeviceUnit(currentItem);
        active = isDeviceActive(currentItem);
        type = currentItem.getDeviceType();

        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView.setText(currentItem.getDeviceName());
        holder.mImageActive.setImageResource(currentItem.getIsActiveImage());

        if (active)
        {
            if (type.equals("Termostat"))
            {
                String stringValue= Double.toString(currentItem.getTemperature());
                holder.deviceUnit.setText(unit);
                holder.deviceValue.setText(stringValue);
                holder.deviceStatus.setText("Teplota: ");
            }

            else if (type.equals("Vlhkomer"))
            {
                String stringValue= Double.toString(currentItem.getHumidity());
                holder.deviceUnit.setText(unit);
                holder.deviceValue.setText(stringValue);
                holder.deviceStatus.setText("Vlhkosť: ");
            }

            else
                holder.deviceStatus.setText("Zariadenie zapnuté");
        }

        else
        {
            holder.deviceStatus.setText("Zariadenie vypnuté");
        }
    }

    public String getDeviceUnit(Device_item currentItem)
    {
        String type = currentItem.getDeviceType();

        if (type.equals("Termostat"))
            return "°C";

        if (type.equals("Vlhkomer"))
            return "%";

        return "";
    }

    public boolean isDeviceActive(Device_item currentItem)
    {
        int isActive = currentItem.getIsActive();
        int isOnOff = currentItem.getIsOn();

        if (isActive == 1)
        {
            if (isOnOff == 1)
                return true;
        }

        return false;
    }

    @Override
    public int getItemCount()
    {
        return mDeviceList.size();
    }

    //interface pre klikanie na izby
    public interface OnDeviceListener
    {
        void  onDeviceClick(int position); //otvori izbu
        void  onEditClick(int position); //otvori edit okno
    }
}
