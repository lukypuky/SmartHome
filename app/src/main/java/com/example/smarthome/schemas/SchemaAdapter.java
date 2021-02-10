package com.example.smarthome.schemas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthome.R;

import java.util.ArrayList;

public class SchemaAdapter extends RecyclerView.Adapter<SchemaAdapter.SchemaViewHolder>
{
    private ArrayList<SchemaItem> mSchemaList;

    public static class SchemaViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView mImageView;
        public TextView mTextView;


        public SchemaViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mImageView = itemView.findViewById(R.id.schemaImageView);
            mTextView = itemView.findViewById(R.id.schemaTextView);
        }
    }

    public SchemaAdapter(ArrayList<SchemaItem> schemaList)
    {
        mSchemaList = schemaList;
    }

    @NonNull
    @Override
    public SchemaAdapter.SchemaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_schema_item, parent, false);
        SchemaAdapter.SchemaViewHolder rvh = new SchemaAdapter.SchemaViewHolder(v);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull SchemaAdapter.SchemaViewHolder holder, int position)
    {
        SchemaItem currentItem = mSchemaList.get(position);

        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView.setText(currentItem.getText());
    }

    @Override
    public int getItemCount()
    {
        return mSchemaList.size();
    }
}
