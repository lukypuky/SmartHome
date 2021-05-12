package com.example.smarthome.devices;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
//    private Context context;

    public class DeviceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public ImageView mImageView, mImageEdit, mImageConnected, mImageWarning;
        public TextView mTextView, deviceValue, deviceUnit, deviceStatus;
        OnDeviceListener onDeviceListener;

        public DeviceViewHolder(@NonNull View itemView, OnDeviceListener onDeviceListener)
        {
            super(itemView);
            mImageView = itemView.findViewById(R.id.deviceImageView);
            mTextView = itemView.findViewById(R.id.deviceTextView);
            mImageEdit = itemView.findViewById(R.id.deviceEdit);
            mImageWarning = itemView.findViewById(R.id.deviceStatusWarning);
            mImageConnected = itemView.findViewById(R.id.deviceConnected);
            deviceValue = itemView.findViewById(R.id.deviceValue);
            deviceStatus = itemView.findViewById(R.id.deviceStatusTag);
            deviceUnit = itemView.findViewById(R.id.deviceUnit);
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
        DeviceViewHolder dvh = new DeviceViewHolder(view, mOnDeviceListener);
//        context = parent.getContext();
        return dvh;
    }

    @Override
    public void onBindViewHolder(@NonNull Device_adapter.DeviceViewHolder holder, int position)
    {
        Device_item currentItem = mDeviceList.get(position);

        String unit, type;
        boolean connected;

        connected = isDeviceConnected(currentItem);
        type = currentItem.getDeviceType();

        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView.setText(currentItem.getDeviceName());
        holder.mImageConnected.setImageResource(currentItem.getIsConnectedImage());

        if (connected)
        {
            if (type.equals("heating") || type.equals("thermometer"))
            {
                String stringValue= Float.toString(currentItem.getTemperature());
                unit = getDeviceUnit(currentItem);
                holder.deviceUnit.setText(unit);
                holder.deviceValue.setText(stringValue);
                holder.deviceStatus.setText("Teplota: ");
            }

            else if (type.equals("hygrometer"))
            {
                String stringValue= Float.toString(currentItem.getHumidity());
                unit = getDeviceUnit(currentItem);
                holder.deviceUnit.setText(unit);
                holder.deviceValue.setText(stringValue);
                holder.deviceStatus.setText("Vlhkosť: ");
            }

            else if (type.equals("blinds"))
            {
                String stringValue= Float.toString(currentItem.getIntensity());
                unit = getDeviceUnit(currentItem);
                holder.deviceUnit.setText(unit);
                holder.deviceValue.setText(stringValue);
                holder.deviceStatus.setText("Zatiahnuté: ");
            }

            else if (type.equals("light") || type.equals("light_sensor"))
            {
                String stringValue= Float.toString(currentItem.getIntensity());
                unit = getDeviceUnit(currentItem);
                holder.deviceUnit.setText(unit);
                holder.deviceValue.setText(stringValue);
                holder.deviceStatus.setText("Intenzita: ");
            }

            else if (type.equals("socket"))
            {
                holder.deviceStatus.setText("Zariadenie zapnuté");
            }

            else
            {
                if (currentItem.getIsActive() == 1)
                {
                    holder.deviceStatus.setText("Spustil sa senzor! ");
                    holder.mImageWarning.setVisibility(View.VISIBLE);
//                    Animation animation = AnimationUtils.loadAnimation(context,R.anim.warning_blink);
//                    holder.mImageWarning.startAnimation(animation);
                }

                else if (currentItem.getIsOn() == 1)
                {
                    holder.deviceStatus.setText("Senzor je aktívny");
                    holder.mImageWarning.setVisibility(View.INVISIBLE);
                }
            }
        }

        else
        {
            if (type.equals("alarm") || type.equals("flood_sensor") || type.equals("smoke_sensor"))
            {
                holder.deviceStatus.setText("Senzor je neaktívny");
                holder.mImageWarning.setVisibility(View.INVISIBLE);
            }

            else
            {
                holder.deviceUnit.setText("");
                holder.deviceValue.setText("");
                holder.deviceStatus.setText("Zariadenie vypnuté");
            }
        }
    }

    public String getDeviceUnit(Device_item currentItem)
    {
        String type = currentItem.getDeviceType();

        if (type.equals("heating") || type.equals("thermometer"))
            return "°C";

        else
            return "%";
    }

    public boolean isDeviceConnected(Device_item currentItem)
    {
        int isConnected = currentItem.getConnectivity();
        int isOnOff = currentItem.getIsOn();

        if (isConnected == 1)
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
