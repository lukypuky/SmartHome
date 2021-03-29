package com.example.smarthome.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthome.R;
import com.example.smarthome.connection.Login;
import com.example.smarthome.connection.SessionManagement;

import java.util.ArrayList;

public class Room_adapter extends RecyclerView.Adapter<Room_adapter.RoomViewHolder>
{
    private final ArrayList<Room_item> mRoomList;
    private final OnRoomListener mOnRoomListener;
    private final Login login;

    public class RoomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public ImageView mImageView;
        public TextView mTextView1, mTextView2;
        OnRoomListener onRoomListener;
        public ImageView mImageEdit;

        public RoomViewHolder(@NonNull View itemView, OnRoomListener onRoomListener)
        {
            super(itemView);
            mImageView = itemView.findViewById(R.id.roomImageView);
            mTextView1 = itemView.findViewById(R.id.roomTextView);
            mTextView2 = itemView.findViewById(R.id.roomTextView2);
            mImageEdit = itemView.findViewById(R.id.roomEdit);
            this.onRoomListener = onRoomListener;

            itemView.setOnClickListener(this);

            if (canEdit())
            {
                //listener na obrazok "editu"
                mImageEdit.setOnClickListener(v ->
                {
                    if (onRoomListener != null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                            onRoomListener.onEditClick(position);
                    }
                });
            }

            else    // ak user nema admin rolu, ikona editu je invisible
                mImageEdit.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onClick(View v)
        {
            onRoomListener.onRoomClick(getAdapterPosition());
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
    public Room_adapter(ArrayList<Room_item> roomList, Login login, OnRoomListener onRoomListener)
    {
        this.mRoomList = roomList;
        this.login = login;
        this.mOnRoomListener = onRoomListener;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_room_item, parent, false);
        RoomViewHolder rvh = new RoomViewHolder(view, mOnRoomListener);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position)
    {
        Room_item currentItem = mRoomList.get(position);

        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView1.setText(currentItem.getRoomName());
    }

    @Override
    public int getItemCount()
    {
        return mRoomList.size();
    }

    //interface pre klikanie na izby
    public interface OnRoomListener
    {
        void  onRoomClick(int position); //otvori izbu
        void  onEditClick(int position); //otvori edit okno
    }
}
