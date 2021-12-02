package com.keyroom.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.keyroom.Adapter.HotelDetailsImageViewAdapter;
import com.keyroom.Model.HotelDetailsModel;
import com.keyroom.R;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class HotelDetailsImageViewActivity extends AppCompatActivity {

    ImageView imgBack;
    RecyclerView rcyImages;
    HotelDetailsImageViewAdapter hotelDetailsImageViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_hoteldetailsimageview);
        intiView();
    }

    private void intiView() {
        imgBack = findViewById(R.id.imgBack);
        rcyImages = findViewById(R.id.rcyImages);
        rcyImages.setLayoutManager(new LinearLayoutManager(HotelDetailsImageViewActivity.this, LinearLayoutManager.VERTICAL, false));
        String Gallry = getIntent().getStringExtra("Gallry");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<HotelDetailsModel.Gallery>>(){}.getType();
        ArrayList<HotelDetailsModel.Gallery> galleries = gson.fromJson(Gallry, type);
        hotelDetailsImageViewAdapter = new HotelDetailsImageViewAdapter(HotelDetailsImageViewActivity.this, galleries);
        rcyImages.setAdapter(hotelDetailsImageViewAdapter);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
