package com.keyroom.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.keyroom.Adapter.BanquetsAdapter;
import com.keyroom.Adapter.BanquetsAndPartyHallAdapter;
import com.keyroom.Model.BanquetsAndPartyHallModel;
import com.keyroom.Network.API;
import com.keyroom.Network.Config;
import com.keyroom.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeeAllBanquetsActivity extends AppCompatActivity implements View.OnClickListener {


    ImageView imgBack;
    RecyclerView recyclerView;
    API api;
    ArrayList<BanquetsAndPartyHallModel.Banquets> banquetsArrayList = new ArrayList<>();
    BanquetsAdapter banquetsAdapter;
    private ShimmerFrameLayout shimmer_hotel_card_dummy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_banquets);
        api = Config.getClient().create(API.class);

        imgBack=findViewById(R.id.imgBack);
        recyclerView=findViewById(R.id.recyclerView);


        shimmer_hotel_card_dummy = findViewById(R.id.shimmer_hotel_card_dummy);
        shimmer_hotel_card_dummy.startShimmerAnimation();

        recyclerView.setLayoutManager(new LinearLayoutManager(SeeAllBanquetsActivity.this, LinearLayoutManager.VERTICAL, false));
        banquetsAdapter = new BanquetsAdapter(getApplicationContext(), banquetsArrayList);
        recyclerView.setAdapter(banquetsAdapter);

        imgBack.setOnClickListener(this);

    }

    private void getBanquets() {
        banquetsArrayList.clear();
        Call<JsonObject> call = api.getBanquet();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("banquets==>", "@@" + response.body());
                try {
                    BanquetsAndPartyHallModel banquetsAndPartyHallModel = new Gson().fromJson(response.body(), BanquetsAndPartyHallModel.class);
                    if (banquetsAndPartyHallModel.isStatus()) {
                        banquetsArrayList.addAll(banquetsAndPartyHallModel.getBanquets());
                        Log.e("arrayData==>", "@@" + banquetsArrayList);
                        if (banquetsArrayList.size() != 0) {
                            banquetsAdapter.notifyDataSetChanged();
                            shimmer_hotel_card_dummy.stopShimmerAnimation();
                            shimmer_hotel_card_dummy.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
               /* if (progressBar != null && progressBar.isShown()) {
                    progressBar.setVisibility(View.GONE);
                }*/
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imgBack) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        shimmer_hotel_card_dummy.startShimmerAnimation();
        shimmer_hotel_card_dummy.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        getBanquets();
    }
}