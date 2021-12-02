package com.keyroom.Activity;

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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.keyroom.Adapter.BanquetsDetailsAdapter;
import com.keyroom.Model.BanquetsDetailsModel;
import com.keyroom.Network.API;
import com.keyroom.Network.Config;
import com.keyroom.R;
import com.keyroom.Utility.SharedPrefs;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BanquetsDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView imgBack;
    RecyclerView rcyGallery;
    TextView txthotelName;
    TextView txtHotelAddress;
    TextView txtDescription;

    TextView txtAmount;
    TextView salePrice;

    TextView txtEnquiry;
    TextView txtDesc;
    TextView txtpolicy;
    BanquetsDetailsAdapter banquetsDetailsAdapter;


    ArrayList<BanquetsDetailsModel.Gallery> galleryArrayList = new ArrayList<>();

    BanquetsDetailsModel banquetsDetailsModel;

    API api;
    Integer banquetsID;
    double map_lat;
    double map_lng;


    TextView LocateToMap;

    String slug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banquets_details);
        api = Config.getClient().create(API.class);
        slug = getIntent().getStringExtra("slug");

        initview();
    }

    private void initview() {
        imgBack=findViewById(R.id.imgBack);
        LocateToMap = findViewById(R.id.locate_in_map);
        rcyGallery = findViewById(R.id.rcyGallery);
        txthotelName = findViewById(R.id.txthotelName);
        txtHotelAddress = findViewById(R.id.txtHotelAddress);
        txtDesc = findViewById(R.id.txtDesc);
        txtDescription = findViewById(R.id.txtDescription);
        txtAmount = findViewById(R.id.txtPrice);
        salePrice = findViewById(R.id.sale_Price);
        txtEnquiry = findViewById(R.id.txtEnquiry);
        txtpolicy = findViewById(R.id.txtPolicy);
        rcyGallery.setLayoutManager(new LinearLayoutManager(BanquetsDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));


        clickEvent();

    }

    private void getBanquetsdetails() {

        Call<JsonObject> call = api.banquetsDetails(slug, SharedPrefs.getInt(SharedPrefs.User_id));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("banquets details==>", "@#######" + response);
                banquetsDetailsModel = new Gson().fromJson(response.body(), BanquetsDetailsModel.class);

                if (banquetsDetailsModel.isStatus()) {
                    map_lat = banquetsDetailsModel.getRow().getMap_lat();
                    map_lng = banquetsDetailsModel.getRow().getMap_lng();

                    if (banquetsDetailsModel.getRow().getId() != null) {
                        banquetsID = banquetsDetailsModel.getRow().getId();
                    }
                    if (banquetsDetailsModel.getRow().getTitle() != null) {
                        txthotelName.setText(banquetsDetailsModel.getRow().getTitle());
                    }
                    if (banquetsDetailsModel.getRow().getAddress() != null)
                        txtHotelAddress.setText(banquetsDetailsModel.getRow().getAddress());

                    if (banquetsDetailsModel.getRow().getContent() != null) {
                        String str = banquetsDetailsModel.getRow().getContent();
                        str = str.replaceAll("\\<.*?\\>", "");

                        txtDesc.setText(str);
                    }

                    if (banquetsDetailsModel.getRow().getPolicy() != null)
                        txtpolicy.setText(banquetsDetailsModel.getRow().getPolicy());
                    if (banquetsDetailsModel.getRow().getGallery() != null)
                        galleryArrayList.addAll(banquetsDetailsModel.getRow().getGallery());


                    if (galleryArrayList.size() != 0) {
                        banquetsDetailsAdapter = new BanquetsDetailsAdapter(BanquetsDetailsActivity.this, galleryArrayList);
                        rcyGallery.setAdapter(banquetsDetailsAdapter);
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

        getBanquetsdetails();


    }

    private void clickEvent() {
        LocateToMap.setOnClickListener(this);
        txtEnquiry.setOnClickListener(this);
        imgBack.setOnClickListener(this);
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

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(BanquetsDetailsActivity.this);
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
                                    Log.e("banquets submit==>", "@@" + response);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                    builder.setTitle("Banquets");
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


                        } else {
                            Toast.makeText(BanquetsDetailsActivity.this, "Please enter valid information", Toast.LENGTH_SHORT).show();
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