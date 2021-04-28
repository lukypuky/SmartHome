package com.example.smarthome.settings;

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

public class User_adapter extends RecyclerView.Adapter<User_adapter.UserViewHolder>
{
    private final ArrayList<User_item> mUserList;
    private final User_adapter.OnUserListener mOnUserListener;
    private final Login login;

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView mUserName, mUserRole;
        User_adapter.OnUserListener onUserListener;
        public ImageView mImageEdit;

        public UserViewHolder(@NonNull View itemView, User_adapter.OnUserListener onUserListener)
        {
            super(itemView);
            mUserName = itemView.findViewById(R.id.userTextView);
            mUserRole = itemView.findViewById(R.id.userRoleView);
            mImageEdit = itemView.findViewById(R.id.userEdit);
            this.onUserListener = onUserListener;

            itemView.setOnClickListener(this);

            if (canEdit())
            {
                //listener na obrazok "editu"
                mImageEdit.setOnClickListener(v ->
                {
                    if (onUserListener != null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                            onUserListener.onEditClick(position);
                    }
                });
            }

            else    // ak user nema admin rolu, ikona editu je invisible
                mImageEdit.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onClick(View v)
        {
            onUserListener.onUserClick(getAdapterPosition());
        }
    }

    public boolean canEdit()
    {
        if (login.getRole() == 1)
            return true;
        else
            return false;
    }

    //get role z id_role
    public String getRole(int role)
    {
        if (role == 1)
            return "Admin";

        else
            return "Bežný používateľ";
    }

    //konstruktor
    public User_adapter(ArrayList<User_item> userList, Login login, User_adapter.OnUserListener onUserListener)
    {
        this.mUserList = userList;
        this.login = login;
        this.mOnUserListener = onUserListener;
    }

    @NonNull
    @Override
    public User_adapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_user_item, parent, false);
        User_adapter.UserViewHolder uvh = new User_adapter.UserViewHolder(view, mOnUserListener);
        return uvh;
    }

    @Override
    public void onBindViewHolder(@NonNull User_adapter.UserViewHolder holder, int position)
    {
        User_item currentItem = mUserList.get(position);
        String role = getRole(currentItem.getSettingsUserRole());

        holder.mUserName.setText(currentItem.getSettingsUserName());
        holder.mUserRole.setText(role);
    }

    @Override
    public int getItemCount()
    {
        return mUserList.size();
    }

    //interface pre klikanie na izby
    public interface OnUserListener
    {
        void  onUserClick(int position); //otvori usera
        void  onEditClick(int position); //otvori edit okno
    }
}
