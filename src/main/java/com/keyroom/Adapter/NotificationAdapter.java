package com.keyroom.Adapter;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.keyroom.Activity.BookingetailsAndUserInfoActivity;
import com.keyroom.Model.NotificationModel;
import com.keyroom.Network.API;
import com.keyroom.Network.ConfigHader;
import com.keyroom.NotificationItemSelected;
import com.keyroom.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    Context mContext;
    ArrayList<NotificationModel.Row> dataArrayList=new ArrayList<>();
    NotificationItemSelected notificationItemSelected;

    public NotificationAdapter(Context mContext, ArrayList<NotificationModel.Row> dataArrayList,NotificationItemSelected notificationItemSelected) {
        this.mContext = mContext;
        this.dataArrayList = dataArrayList;
        this.notificationItemSelected=notificationItemSelected;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(mContext).inflate(R.layout.notification_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {


        NotificationModel.Row data=dataArrayList.get(position);

        if (data.getData().getNotification().getMessage()!= null){
            holder.txt_message.setText(data.getData().getNotification().getMessage());
        }
        if (data.getCreated_at()!=null){
            holder.notification_date.setText(data.getCreated_at());
        }



      /*  holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=data.getData().getId();
                Intent intent=new Intent(mContext, BookingetailsAndUserInfoActivity.class);
                intent.putExtra("code",id);
                mContext.startActivity(intent);
            }
        });*/


    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txt_message,notification_date,clear;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_message=itemView.findViewById(R.id.txt_message);
            notification_date=itemView.findViewById(R.id.notification_date);
            clear=itemView.findViewById(R.id.clear);

            clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notificationItemSelected.onItemClicked(getAdapterPosition());
                }
            });
        }

    }
    public int getSize(){
        int size=dataArrayList.size();
        return size;
    }
}
