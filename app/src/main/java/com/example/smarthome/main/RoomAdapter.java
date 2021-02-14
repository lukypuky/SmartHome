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

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder>
{
    private ArrayList<RoomItem> mRoomList;
    private  OnRoomListener mOnRoomListener;

    public static class RoomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public ImageView mImageView;
        public TextView mTextView1, mTextView2;
        OnRoomListener onRoomListener;

        public RoomViewHolder(@NonNull View itemView, OnRoomListener onRoomListener)
        {
            super(itemView);
            mImageView = itemView.findViewById(R.id.roomImageView);
            mTextView1 = itemView.findViewById(R.id.roomTextView);
            mTextView2 = itemView.findViewById(R.id.roomTextView2);
            this.onRoomListener = onRoomListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            onRoomListener.onRoomClick(getAdapterPosition());
        }
    }

    //konstruktor
    public RoomAdapter(ArrayList<RoomItem> roomList, OnRoomListener onRoomListener)
    {
        this.mRoomList = roomList;
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
        RoomItem currentItem = mRoomList.get(position);

        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
    }

    @Override
    public int getItemCount()
    {
        return mRoomList.size();
    }

    //interface pre klikanie na izby
    public interface OnRoomListener
    {
        void  onRoomClick(int position);
    }
}
