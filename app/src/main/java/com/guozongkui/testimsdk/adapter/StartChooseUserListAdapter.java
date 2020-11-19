package com.guozongkui.testimsdk.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.guozongkui.testimsdk.R;
import com.guozongkui.testimsdk.model.MyUser;
import com.guozongkui.testimsdk.ui.MainActivity;

import java.util.List;

public class StartChooseUserListAdapter extends RecyclerView.Adapter<StartChooseUserListAdapter.ViewHolder> {

    private List<MyUser> mMyUserList;
    private Context mContext;

    public StartChooseUserListAdapter( Context context, List<MyUser> myUserList){
        mMyUserList = myUserList;
        mContext = context;
    }

    @NonNull
    @Override
    public StartChooseUserListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myuser_item, parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull StartChooseUserListAdapter.ViewHolder holder, final int position) {

        holder.mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyUser myUser = mMyUserList.get(position);

                Intent intent = new Intent(mContext, MainActivity.class);
//                intent.putExtra("token", myUser.getToken());
//                intent.putExtra("name", myUser.getName());
//                intent.putExtra( "userId", myUser.getUserId());
                Bundle bundle = new Bundle();
                intent.putExtra("user", myUser);
                mContext.startActivity(intent);
            }
        });

        MyUser user = mMyUserList.get(position);
        Glide.with(mContext).load(user.getPortraitUri()).into(holder.avatar);
        holder.name.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return mMyUserList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView avatar;
        TextView name;
        View mItemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemView = itemView;
            avatar = itemView.findViewById(R.id.user_avatar);
            name = itemView.findViewById(R.id.user_name);
        }
    }
}
