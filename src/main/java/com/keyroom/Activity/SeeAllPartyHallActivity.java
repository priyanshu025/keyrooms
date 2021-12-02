package com.keyroom.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.keyroom.Adapter.AllPartyHallAdpater;
import com.keyroom.Adapter.BanquetsAdapter;
import com.keyroom.Model.BanquetsAndPartyHallModel;
import com.keyroom.Model.PartyHallModel;
import com.keyroom.Network.API;
import com.keyroom.Network.Config;
import com.keyroom.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeeAllPartyHallActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView imgBack;
    RecyclerView recyclerView;
    API api;
    ArrayList<PartyHallModel.Party> partyArrayList = new ArrayList<>();
   AllPartyHallAdpater allPartyHallAdpater;
    private ShimmerFrameLayout shimmer_hotel_card_dummy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_party_hall);

        api = Config.getClient().create(API.class);

        imgBack=findViewById(R.id.imgBack);
        recyclerView=findViewById(R.id.recyclerView);

        shimmer_hotel_card_dummy = findViewById(R.id.shimmer_hotel_card_dummy);
        shimmer_hotel_card_dummy.startShimmerAnimation();


        recyclerView.setLayoutManager(new LinearLayoutManager(SeeAllPartyHallActivity.this, LinearLayoutManager.VERTICAL, false));

        allPartyHallAdpater = new AllPartyHallAdpater(getApplicationContext(), partyArrayList);
        recyclerView.setAdapter(allPartyHallAdpater);

        imgBack.setOnClickListener(this);
    }

    private void getPartyHall() {
        partyArrayList.clear();
        Call<JsonObject> call = api.getPartyHall();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("PartyHall==>", "@@" + response.body());
                try {
                    PartyHallModel partyHallModel = new Gson().fromJson(response.body(), PartyHallModel.class);
                    if (partyHallModel.isStatus()) {
                        partyArrayList.addAll(partyHallModel.getParty_hall());
                        Log.e("PartyData==>", "@@" + partyArrayList);
                        if (partyArrayList.size() != 0) {
                            allPartyHallAdpater.notifyDataSetChanged();
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
        getPartyHall();
    }
}