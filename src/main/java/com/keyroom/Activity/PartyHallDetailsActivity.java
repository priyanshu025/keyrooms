package com.keyroom.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.keyroom.Adapter.BanquetsDetailsAdapter;
import com.keyroom.Adapter.PartyHallDetailsAdapter;
import com.keyroom.Model.BanquetsDetailsModel;
import com.keyroom.Model.PartyHallDetailsModel;
import com.keyroom.Model.PartyHallModel;
import com.keyroom.Network.API;
import com.keyroom.Network.Config;
import com.keyroom.R;
import com.keyroom.Utility.SharedPrefs;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartyHallDetailsActivity extends AppCompatActivity  implements View.OnClickListener{

    RecyclerView rcyGallery;
    TextView txthotelName;
    TextView txtHotelAddress;
    TextView txtHotelRate;
    TextView txtDescription;

    TextView txtAmount;
    TextView salePrice;

    ImageView imgBack;
    TextView txtEnquiry;
    TextView txtDesc;
    TextView txtTotalReview;
    TextView txtpolicy;
    PartyHallDetailsAdapter partyHallDetailsAdapter;


    ArrayList<PartyHallDetailsModel.Gallery> galleryArrayList = new ArrayList<>();

    PartyHallDetailsModel partyHallModel;

    API api;
    Integer banquetsID;
    double map_lat;
    double map_lng;

    TextView LocateToMap;

    String slug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_hall_details);
        api = Config.getClient().create(API.class);

        slug = getIntent().getStringExtra("slug");
        initview();
    }
    private void initview() {
        LocateToMap = findViewById(R.id.locate_in_map);
        imgBack=findViewById(R.id.imgBack);
        rcyGallery = findViewById(R.id.rcyGallery);
        txthotelName = findViewById(R.id.txthotelName);
        txtHotelAddress = findViewById(R.id.txtHotelAddress);
        txtHotelRate = findViewById(R.id.txtHotelRate);
        txtDesc = findViewById(R.id.txtDesc);
        txtDescription = findViewById(R.id.txtDescription);
        txtAmount = findViewById(R.id.txtPrice);
        salePrice = findViewById(R.id.sale_Price);
        txtEnquiry = findViewById(R.id.txtEnquiry);
        txtTotalReview = findViewById(R.id.txtTotalReview);
        txtpolicy = findViewById(R.id.txtPolicy);
        rcyGallery.setLayoutManager(new LinearLayoutManager(PartyHallDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));


        clickEvent();

    }

    private void getPartyHallDetails() {

        Call<JsonObject> call = api.partyDetails(slug, SharedPrefs.getInt(SharedPrefs.User_id));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("banquets details==>", "@#######" + response);
                partyHallModel = new Gson().fromJson(response.body(), PartyHallDetailsModel.class);

                if (partyHallModel.isStatus()) {
                    map_lat = partyHallModel.getRow().getMap_lat();
                    map_lng = partyHallModel.getRow().getMap_lng();

                    if (partyHallModel.getRow().getId() != null) {
                        banquetsID = partyHallModel.getRow().getId();
                    }
                    if (partyHallModel.getRow().getTitle() != null) {
                        txthotelName.setText(partyHallModel.getRow().getTitle());
                    }
                    if (partyHallModel.getRow().getAddress() != null)
                        txtHotelAddress.setText(partyHallModel.getRow().getAddress());

                    if (partyHallModel.getRow().getContent() != null) {
                        String str=partyHallModel.getRow().getContent();
                        str = str.replaceAll("\\<.*?\\>", "");

                        txtDesc.setText(str);
                    }

                    if (partyHallModel.getRow().getPolicy() != null)
                        txtpolicy.setText(partyHallModel.getRow().getPolicy());
                    if (partyHallModel.getRow().getGallery() != null)
                        galleryArrayList.addAll(partyHallModel.getRow().getGallery());


                    if (galleryArrayList.size() != 0) {
                        partyHallDetailsAdapter = new PartyHallDetailsAdapter(PartyHallDetailsActivity.this, galleryArrayList);
                        rcyGallery.setAdapter(partyHallDetailsAdapter);
                        rcyGallery.setOnFlingListener(null);
                        SnapHelper snapHelper = new PagerSnapHelper();
                        snapHelper.attachToRecyclerView(rcyGallery);

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        getPartyHallDetails();


    }

    private void clickEvent() {
        imgBack.setOnClickListener(this);
        LocateToMap.setOnClickListener(this);
        txtEnquiry.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.imgBack:
                super.onBackPressed();
                break;

            case R.id.locate_in_map:
                getCurrentLocation();
                break;

            case R.id.txtEnquiry:

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PartyHallDetailsActivity.this);
                bottomSheetDialog.setContentView(R.layout.bottom_sheet_form);
                bottomSheetDialog.setCanceledOnTouchOutside(false);

                EditText username = bottomSheetDialog.findViewById(R.id.username);
                EditText phone = bottomSheetDialog.findViewById(R.id.phone);
                EditText email = bottomSheetDialog.findViewById(R.id.email);
                EditText guest = bottomSheetDialog.findViewById(R.id.guest);
                EditText hour = bottomSheetDialog.findViewById(R.id.hour);
                EditText purpose = bottomSheetDialog.findViewById(R.id.purpose);
                Button submit = bottomSheetDialog.findViewById(R.id.submit);


                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!username.getText().toString().equals("") && !phone.getText().toString().equals("") &&
                                !email.getText().toString().equals("") && !guest.getText().toString().equals("")
                                && !hour.getText().toString().equals("") && !purpose.getText().toString().equals("")
                        ) {
                            Call<JsonObject> call = api.banquetsForm(slug, username.getText().toString(), phone.getText().toString(), email.getText().toString(),
                                    guest.getText().toString(), hour.getText().toString(), purpose.getText().toString());

                            call.enqueue(new Callback<JsonObject>() {
                                @Override
                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                    Log.e("banquets submit==>", "@@" + response.body());
                                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                    builder.setTitle("Banquets And Party Hall");
                                    builder.setMessage("Your request is successfully send to our executor ,they will contact you soon.");

                                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            bottomSheetDialog.dismiss();
                                        }
                                    });
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                }

                                @Override
                                public void onFailure(Call<JsonObject> call, Throwable t) {

                                }
                            });


                        }
                        else {
                            Toast.makeText(PartyHallDetailsActivity.this, "Please enter valid information", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                bottomSheetDialog.show();
                break;


        }


    }


    private void getCurrentLocation() {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + map_lat + "," + map_lng);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}