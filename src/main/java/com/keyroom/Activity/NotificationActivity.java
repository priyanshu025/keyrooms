package com.keyroom.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.keyroom.Adapter.NotificationAdapter;
import com.keyroom.Model.NotificationModel;
import com.keyroom.Network.API;
import com.keyroom.Network.Config;
import com.keyroom.Network.ConfigHader;
import com.keyroom.NotificationItemSelected;
import com.keyroom.R;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity implements NotificationItemSelected {

    RecyclerView notification_Recycler_view;
    NotificationAdapter notificationAdapter;
    NotificationModel notificationModel;
    ArrayList<NotificationModel.Row> dataArrayList=new ArrayList<>();
    API api;
    TextView total_notify;
    ImageView img_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        api = ConfigHader.getClient().create(API.class);
        total_notify=findViewById(R.id.total_notify);

        img_back=findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        notification_Recycler_view=findViewById(R.id.notification_Recycler_view);
        notification_Recycler_view.setLayoutManager(new LinearLayoutManager(NotificationActivity.this,RecyclerView.VERTICAL,false));

        notificationAdapter=new NotificationAdapter(NotificationActivity.this,dataArrayList,this);
        notification_Recycler_view.setAdapter(notificationAdapter);
        callNotificationApi();
    }

    private void callNotificationApi() {
        dataArrayList.clear();
        Call<JsonObject> call=api.getNotification();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("notificationApi==",""+response.body());
                try {
                    notificationModel = new Gson().fromJson(response.body(), NotificationModel.class);
                    if (notificationModel.isStatus()) {
                        dataArrayList.addAll(notificationModel.getRows());
                        total_notify.setText(notificationModel.getTotal_records() + " New Notification");
                        Log.e("Total_records=", "" + notificationModel.getTotal_records());

                    }
                    notificationAdapter.notifyDataSetChanged();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }



    @Override
    public void onItemClicked(int position) {
        NotificationModel.Row notificationModel=dataArrayList.get(position);

        Call<JsonObject> call=api.clearNotification(notificationModel.getData().getId());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("clear notification",""+response.body());
                Toast.makeText(NotificationActivity.this, "One Notification Cleared", Toast.LENGTH_SHORT).show();
                callNotificationApi();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


    }
}