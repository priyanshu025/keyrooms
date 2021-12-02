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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.keyroom.Adapter.CityLocationAdapter;
import com.keyroom.Adapter.SearchCityAdapter;
import com.keyroom.Fragment.HomeFragment;
import com.keyroom.Model.LocationCityModel;
import com.keyroom.Model.LocationModel;
import com.keyroom.Network.API;
import com.keyroom.Network.Config;
import com.keyroom.R;

import java.text.ParseException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityLocationsActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView locationCityRecyclerView;
    API api;
    ArrayList<LocationCityModel> cityListName = new ArrayList<>();

    String phone_no = "9560509906";

    CityLocationAdapter cityLocationAdapter;
    EditText edittext;

    ImageView image_back;

    LinearLayout lnyCheckInDate,lnyCheckOutDate,lnyGuestAndRoom;
    TextView txtToday,txtTomorrow,txtRoom,txtAdult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = Config.getClient().create(API.class);

        setContentView(R.layout.activity_city_locations);
        initView();
        SetDate();
    }

    private void initView() {
        locationCityRecyclerView = findViewById(R.id.locationCityRecyclerView);
        edittext=findViewById(R.id.edittext);
        lnyCheckInDate = findViewById(R.id.lnyCheckInDate);
        lnyCheckOutDate = findViewById(R.id.lnyCheckOutDate);
        lnyGuestAndRoom = findViewById(R.id.lnyGuestAndRoom);
        txtToday = findViewById(R.id.txtToday);
        txtTomorrow = findViewById(R.id.txtTomorrow);
        txtRoom = findViewById(R.id.txtRoom);
        txtAdult = findViewById(R.id.txtAdult);
        image_back=findViewById(R.id.image_back);



        locationCityRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        cityLocationAdapter = new CityLocationAdapter(CityLocationsActivity.this, cityListName);
        locationCityRecyclerView.setAdapter(cityLocationAdapter);

        clickEvent();
        locationAPI();

        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());

            }
        });


    }
    private void SetDate(){

        try {
            txtToday.setText(HomeFragment.formatDateFromDateString("yyyy-MM-dd","dd-MMM-yyyy",ChekInCheckOutActivity.strCheckInDate));
            txtTomorrow.setText(HomeFragment.formatDateFromDateString("yyyy-MM-dd","dd-MMM-yyyy",ChekInCheckOutActivity.strCheckOutDate));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (ChekInCheckOutActivity.totalAdult == 1) {
            txtRoom.setText(ChekInCheckOutActivity.room + " Room");
            txtAdult.setText(ChekInCheckOutActivity.totalAdult + " Guest");
        }
        else {
            txtRoom.setText(ChekInCheckOutActivity.room + " Room");
            txtAdult.setText(ChekInCheckOutActivity.totalAdult + " Guest");
        }
    }

    private void filter(String text) {

        ArrayList<LocationCityModel> filteredList=new ArrayList<>();

        for (LocationCityModel item:cityListName){
            if (item.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        cityLocationAdapter.filterList(filteredList);
    }

    private void locationAPI() {

        cityListName.clear();
        Call<JsonObject> call = api.locations();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                LocationModel locationModel = new Gson().fromJson(response.body(), LocationModel.class);
                cityListName.addAll(locationModel.getLocations());
                if (cityListName.size() != 0) {
                    cityLocationAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void clickEvent() {

        lnyCheckInDate.setOnClickListener(this);
        lnyCheckOutDate.setOnClickListener(this);
        lnyGuestAndRoom.setOnClickListener(this);
        image_back.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.lnyCheckInDate:
                checkINCheckOutActivity(1);
                break;
            case R.id.lnyCheckOutDate:
                checkINCheckOutActivity(2);
                break;
            case R.id.lnyGuestAndRoom:
                checkINCheckOutActivity(3);
                break;

            case R.id.image_back:
                finish();
                break;
        }
    }


    private void checkINCheckOutActivity(int type) {
        Intent it = new Intent(getApplicationContext(), ChekInCheckOutActivity.class);
        it.putExtra("type", type);
        startActivityForResult(it, 2);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onResume() {
        super.onResume();
        SetDate();
    }
}