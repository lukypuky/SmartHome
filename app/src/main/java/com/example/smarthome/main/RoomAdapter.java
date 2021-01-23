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

    public static class RoomViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;

        public RoomViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView);
            mTextView2 = itemView.findViewById(R.id.textView2);
        }
    }

    public RoomAdapter(ArrayList<RoomItem> roomList)
    {
        mRoomList = roomList;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.newroom_item, parent, false);
        RoomViewHolder rvh = new RoomViewHolder(v);
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
}
