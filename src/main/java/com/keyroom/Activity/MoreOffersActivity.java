package com.keyroom.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.keyroom.Adapter.MoreOfferAdapter;
import com.keyroom.Adapter.SearchResultAdapter;
import com.keyroom.CutomeView.LinearLayoutManagerWrapper;
import com.keyroom.InterFace.OnLoadMoreListener;
import com.keyroom.Model.Data;
import com.keyroom.Model.GlobalSearchModel;
import com.keyroom.Model.SearchModel;
import com.keyroom.Network.API;
import com.keyroom.Network.Config;
import com.keyroom.Network.ConfigHader;
import com.keyroom.R;
import com.keyroom.Utility.SharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoreOffersActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rcyHotelList;
    ImageView imgBack;
    API api;
    MoreOfferAdapter moreOfferAdapter;
    ArrayList<SearchModel.Rows> serachHotelModelList = new ArrayList<>();
    ArrayList<Data> searchModels=new ArrayList<>();
    Integer location_id;
    private ShimmerFrameLayout shimmer_hotel_card_dummy;
    SearchResultAdapter searchResultAdapter;
    int page_no = 1;
    int totalRecode = 0;
    String sortBy = "";
    Handler handler;
    String type = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = ConfigHader.getClient().create(API.class);
        setContentView(R.layout.activity_more_offers);
        location_id=getIntent().getIntExtra("location_id",-1);
        Log.e("location_id",""+location_id);

        initView();
    }

    private void initView() {
        imgBack = findViewById(R.id.imgBack);
        rcyHotelList = findViewById(R.id.recyclerViewMoreOffer);
        shimmer_hotel_card_dummy = findViewById(R.id.shimmer_hotel_card_dummy);
        shimmer_hotel_card_dummy.startShimmerAnimation();

        rcyHotelList.setLayoutManager(new LinearLayoutManager(MoreOffersActivity.this, LinearLayoutManager.VERTICAL, false));
        rcyHotelList.setHasFixedSize(true);

        moreOfferAdapter = new MoreOfferAdapter(MoreOffersActivity.this, serachHotelModelList);
        rcyHotelList.setAdapter(moreOfferAdapter);


        clickEvent();
        getHotels();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void getHotels() {
        serachHotelModelList.clear();
        Call<JsonObject> call = api.getDiscountedHotel(String.valueOf(location_id));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("DiscountedHotel==>", "@@" + response.body());

                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getBoolean("status")) {
                        SearchModel searchModel = new Gson().fromJson(response.body(), SearchModel.class);
                        serachHotelModelList.addAll(searchModel.getRows());
                        if (serachHotelModelList.size() != 0) {
                            moreOfferAdapter.notifyDataSetChanged();
                            shimmer_hotel_card_dummy.stopShimmerAnimation();
                            shimmer_hotel_card_dummy.setVisibility(View.GONE);
                            rcyHotelList.setVisibility(View.VISIBLE);

                        }
                    } else {
                        Intent intent=new Intent(MoreOffersActivity.this,SeeAllPopularHotelActivity.class);
                        intent.putExtra("premiuim", "no");
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


    }



    private void clickEvent() {
        imgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                super.onBackPressed();
                break;
        }
    }

}