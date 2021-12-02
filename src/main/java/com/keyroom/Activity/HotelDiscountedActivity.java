 package com.keyroom.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.SettingsClient;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.keyroom.Adapter.BannerDetailsAdapter;
import com.keyroom.Adapter.FacilitesHotelDetailsAdapter;
import com.keyroom.Adapter.GalleryDetailsAdapter;
import com.keyroom.Adapter.NearHotelAdapter;
import com.keyroom.Adapter.PolicyAdapter;
import com.keyroom.Adapter.ReviewHotelAdapter;
import com.keyroom.Adapter.RoomDetailAdapter;
import com.keyroom.Adapter.RoomDetailWithAvaiblityAdapter;
import com.keyroom.Fragment.HomeFragment;
import com.keyroom.Model.CheckAvabilityModel;
import com.keyroom.Model.HotelDetailsModel;
import com.keyroom.Model.NearByHotelModel;
import com.keyroom.Network.API;
import com.keyroom.Network.Config;
import com.keyroom.Network.ConfigHader;
import com.keyroom.R;
import com.keyroom.Utility.SharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelDiscountedActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imgBack;
    ImageView imgShare;
    RecyclerView rcyGallery;
    TextView txthotelName;
    TextView txtHotelAddress;
    TextView txtHotelRate;
    int adults;

    int count = 0;
    TextView txtGuestAndRoom;
    TextView txtCheckIn;
    TextView txtAmount;
    TextView salePrice;
    Button txtBookNow;
    TextView txtCheckOut;
    TextView txtDesc;
    TextView txtTotalReview;
    String phone_no = "9560509906";

    LinearLayout llGuest,llcheckIn,llcheckOut;

    String slug;
    API api;
    API apiHearde;
    Integer hotelID;
    int roomSize = 0;
    double map_lat;
    double map_lng;
    TextView LocateToMap;

    String create_user,id;


    // ProgressBar progressBar;
    GalleryDetailsAdapter galleryDetailsAdapter;
    ReviewHotelAdapter reviewHotelAdapter;
    BannerDetailsAdapter bannerDetailsAdapter;
    PolicyAdapter policyAdapter;
    ConstraintLayout constraintLayout;
    RecyclerView policyRecyelerView;


    ArrayList<HotelDetailsModel.Gallery> galleryArrayList = new ArrayList<>();
    ArrayList<HotelDetailsModel.BannerImage> bannerImageArrayList = new ArrayList<>();
    ArrayList<HotelDetailsModel.Policy> policyArrayList = new ArrayList<>();
    ArrayList<HotelDetailsModel.ReviewList> reviewListArrayList=new ArrayList<>();

    HotelDetailsModel hotelDetailsModel;
    int totalAmt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = Config.getClient().create(API.class);
        apiHearde = ConfigHader.getClient().create(API.class);
        setContentView(R.layout.activity_hotel_discounted);

        initView();

        slug = getIntent().getStringExtra("slug");
    }


    private void initView() {
        imgBack = findViewById(R.id.imgBack);
        LocateToMap = findViewById(R.id.locate_in_map);
        imgShare = findViewById(R.id.imgShare);

        txtCheckIn=findViewById(R.id.txtCheckInDate);
        policyRecyelerView=findViewById(R.id.policyRecyelerView);
        constraintLayout=findViewById(R.id.constraintLayout);
        rcyGallery = findViewById(R.id.rcyGallery);
        txthotelName = findViewById(R.id.txthotelName);
        txtHotelAddress = findViewById(R.id.txtHotelAddress);
        txtHotelRate = findViewById(R.id.txtHotelRate);
        txtDesc=findViewById(R.id.txtDesc);
        txtCheckOut = findViewById(R.id.txtCheckOut);
        llGuest=findViewById(R.id.llGuest);
        llcheckIn=findViewById(R.id.llcheckIn);
        llcheckOut=findViewById(R.id.llcheckOut);

        txtGuestAndRoom=findViewById(R.id.txtGuestAndRoom);
        txtAmount = findViewById(R.id.txtPrice);
        salePrice=findViewById(R.id.sale_Price);
        txtBookNow = findViewById(R.id.txtBookNow);

        txtTotalReview = findViewById(R.id.txtTotalReview);





        LinearLayoutManager layoutManager=new LinearLayoutManager(HotelDiscountedActivity.this, LinearLayoutManager.HORIZONTAL, false);

        rcyGallery.setLayoutManager(new LinearLayoutManager(HotelDiscountedActivity.this, LinearLayoutManager.HORIZONTAL, false));
        policyRecyelerView.setLayoutManager(new LinearLayoutManager(HotelDiscountedActivity.this, LinearLayoutManager.VERTICAL, false));

        galleryDetailsAdapter = new GalleryDetailsAdapter(HotelDiscountedActivity.this, galleryArrayList);
        rcyGallery.setAdapter(galleryDetailsAdapter);

        policyAdapter = new PolicyAdapter(HotelDiscountedActivity.this, policyArrayList);
        policyRecyelerView.setAdapter(policyAdapter);



        clickEvent();

    }


    private void clickEvent() {
        imgBack.setOnClickListener(this);
        LocateToMap.setOnClickListener(this);
        txtBookNow.setOnClickListener(this);
        llGuest.setOnClickListener(this);
        llcheckOut.setOnClickListener(this);
        imgShare.setOnClickListener(this);
        llcheckIn.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        count=0;
       getHotelDetils();
    }



    private void getHotelDetils() {

        galleryArrayList.clear();
        bannerImageArrayList.clear();
        policyArrayList.clear();
        reviewListArrayList.clear();
        Log.e("URL==>Details", "@@" + slug);

        Call<JsonObject> call = api.hotelDiscountedDetails(slug, SharedPrefs.getInt(SharedPrefs.User_id));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    Log.e("hotel details=>>","@@"+response.body());
                    Log.e("URL==>Details", "@@" + response.raw().request().url());
                    hotelDetailsModel = new Gson().fromJson(response.body(), HotelDetailsModel.class);

                    if (hotelDetailsModel.isStatus()) {
////////////////// maps latitude and longitude
                        map_lat = hotelDetailsModel.getRow().getMap_lat();
                        map_lng = hotelDetailsModel.getRow().getMap_lng();

                        Log.e("Map==>Details", "@@@@" + map_lat + "      "+ map_lng);

////////////////// maps latitude and longitude
                        if (hotelDetailsModel.getRow().getId() != null) {
                            hotelID = hotelDetailsModel.getRow().getId();
                        }
                        if (hotelDetailsModel.getRow().getTitle() != null) {
                            //txtHotelTitle.setText(hotelDetailsModel.getRow().getTitle());
                            txthotelName.setText(hotelDetailsModel.getRow().getTitle());
                        }
                        if (hotelDetailsModel.getRow().getAddress() != null) {
                            txtHotelAddress.setText(hotelDetailsModel.getRow().getAddress());
                        }
                        if (hotelDetailsModel.getRow().getContent() != null) {
                            String str=hotelDetailsModel.getRow().getContent();
                            str = str.replaceAll("\\<.*?\\>", "");

                            txtDesc.setText(str);
                        }

                        if (hotelDetailsModel.getRow().getPrice() != null) {
                            totalAmt=Integer.parseInt(String.valueOf(hotelDetailsModel.getRow().getPrice()).split("\\.")[0]);
                            Log.e("totalAmt=",""+totalAmt);
                            txtAmount.setText("\u20B9" + totalAmt);

//                            selectPrice = hotelDetailsModel.getRow().getPrice();
                        }
                        if (hotelDetailsModel.getRow().getSale_price() != null) {
                            constraintLayout.setVisibility(View.VISIBLE);
                            salePrice.setText("\u20B9" + Integer.parseInt(String.valueOf(hotelDetailsModel.getRow().getSale_price()).split("\\.")[0]));


                        }
                        else {
                            constraintLayout.setVisibility(View.GONE);
                            Log.e("salePrice==>","@@"+ hotelDetailsModel.getRow().getSale_price());
                        }

                        if (hotelDetailsModel.getRow().getGallery() != null)
                            galleryArrayList.addAll(hotelDetailsModel.getRow().getGallery());

                        if (hotelDetailsModel.getRow().getBannerImage() != null)
                            bannerImageArrayList.addAll(hotelDetailsModel.getRow().getBannerImage());


                        if (galleryArrayList.size() != 0) {
                         galleryDetailsAdapter.notifyDataSetChanged();

                            rcyGallery.setOnFlingListener(null);
                            SnapHelper snapHelper = new PagerSnapHelper();
                            snapHelper.attachToRecyclerView(rcyGallery);


                            if (galleryDetailsAdapter.getItemCount() > 1) {
                                final int speedScroll = 2500;
                                final Handler handler = new Handler();
                                final Runnable runnable = new Runnable() {
                                    boolean flag = true;

                                    @Override
                                    public void run() {
                                        if (count < galleryDetailsAdapter.getItemCount()) {
                                            if (count == galleryDetailsAdapter.getItemCount() - 1) {
                                                flag = false;
                                            } else if (count == 0) {
                                                flag = true;
                                            }
                                            if (flag) count++;
                                            else count--;

                                            rcyGallery.smoothScrollToPosition(count);
                                            handler.postDelayed(this, speedScroll);
                                        }
                                        if (count>galleryDetailsAdapter.getItemCount()){
                                            count=0;
                                        }
                                    }
                                };
                                handler.postDelayed(runnable, speedScroll);
                            }



                        } else {
                            bannerDetailsAdapter = new BannerDetailsAdapter(HotelDiscountedActivity.this, bannerImageArrayList);
                            rcyGallery.setAdapter(bannerDetailsAdapter);
                        }


                        if (hotelDetailsModel.getRow().getPolicy() != null)
                            policyArrayList.addAll(hotelDetailsModel.getRow().getPolicy());

                        if (policyArrayList.size() != 0) {
                            policyAdapter.notifyDataSetChanged();
                        }

                        if (!ChekInCheckOutActivity.strCheckInDate.equals("")){
                            getDate();
                        }
                        else {
                            getDate();
                        }

                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));
                            JSONObject hotelJson = jsonObject.getJSONObject("hotel");

                            create_user = hotelJson.getString("create_user");
                            id = hotelJson.getString("id");

                            Log.e("create_user",""+create_user);
                            Log.e("id",""+id);

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), hotelDetailsModel.getMessages(), Toast.LENGTH_SHORT).show();
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
        switch (v.getId()) {
            case R.id.imgBack:
                super.onBackPressed();
                break;
            case R.id.locate_in_map:
                getCurrentLocation();
                break;
            case R.id.txtBookNow:
                if (SharedPrefs.getValue(SharedPrefs.SKIP_LOGIN).equals("yes")) {
                    loginScreen();
                } else
                    callBookNow();
                break;


            case R.id.llGuest:
                break;

            case R.id.llcheckIn:
                checkINCheckOutActivity(1);
                break;

            case R.id.llcheckOut:
                checkINCheckOutActivity(2);
                break;

            case R.id.imgShare:
                shareIntent();
                break;
        }
    }

    private void shareIntent() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "http://keyrooms.in/hotel/" + slug);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);


    }


    private void getCurrentLocation() {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + map_lat + "," + map_lng);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }

    }


    private void callBookNow() {
        txtBookNow.setClickable(false);
        Log.e("totalamt==>","@@"+totalAmt);
        Log.e("firstname==>",""+SharedPrefs.getValue(SharedPrefs.First_Name));
        Log.e("lastname==>",""+SharedPrefs.getValue(SharedPrefs.Last_Name));
        Log.e("phonenumber==>",""+SharedPrefs.getValue(SharedPrefs.Phone_No));
        Log.e("slug==>",""+slug);

        if (totalAmt > 0) {
        Call<JsonObject> call=apiHearde.customBooking(SharedPrefs.getValue(SharedPrefs.First_Name),SharedPrefs.getValue(SharedPrefs.Last_Name),SharedPrefs.getValue(SharedPrefs.Phone_No),"offline_payment","1",slug,id,create_user);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("customBooking==",""+response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(String.valueOf(response.body()));
                    if (jsonObject.getBoolean("status")) {
                        JSONObject rowJsonObject = jsonObject.getJSONObject("booking");
                        Log.e("book code==>", "@@" + rowJsonObject.getString("code"));
                        startActivity(new Intent(HotelDiscountedActivity.this, UserInformationAfterCheckOutActivity.class)
                                        .putExtra("code", rowJsonObject.getString("code"))
                                        .putExtra("id", rowJsonObject.getInt("id"))
                                        .putExtra("created_at", rowJsonObject.getString("created_at"))
                                        .putExtra("start_date", rowJsonObject.getString("start_date"))
                                        .putExtra("end_date", rowJsonObject.getString("end_date"))
                                        .putExtra("status", rowJsonObject.getString("status"))
                                        .putExtra("gateway", rowJsonObject.getString("gateway"))
                                        .putExtra("nights","1")
                                        .putExtra("adults",""+adults)
                                        .putExtra("title", txthotelName.getText().toString())
                                        .putExtra("address", txtHotelAddress.getText().toString())
                                        .putExtra("room", "1")
                                        .putExtra("price", String.valueOf(totalAmt))
                        );
                        Log.e("total amount==>", "@@" + totalAmt);
                        finish();

                    } else {
                        txtBookNow.setClickable(true);
                        Toast.makeText(HotelDiscountedActivity.this, jsonObject.getString("messages"), Toast.LENGTH_LONG).show();
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                txtBookNow.setClickable(true);
            }
        });


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            try {
                if (data != null) {
                    // checkAvability();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }


    public void getDate() {

       adults= ChekInCheckOutActivity.totalAdult=2;
        ChekInCheckOutActivity.room=1;

        try {
            txtCheckIn.setText(HomeFragment.formatDateFromDateString("yyyy-MM-dd","dd-MMM-yyyy",ChekInCheckOutActivity.strCheckInDate));
            txtCheckOut.setText(HomeFragment.formatDateFromDateString("yyyy-MM-dd","dd-MMM-yyyy",ChekInCheckOutActivity.strCheckOutDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        txtGuestAndRoom.setText(ChekInCheckOutActivity.room + " Room" + "\n" + (ChekInCheckOutActivity.totalAdult) + " Guests");

    }


    private void loginScreen() {
        Intent it = new Intent(HotelDiscountedActivity.this, LoginActivity.class);
        startActivity(it);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        txtBookNow.setClickable(true);
        ChekInCheckOutActivity.room=1;
        ChekInCheckOutActivity.totalAdult=1;
    }


    private void checkINCheckOutActivity(int type) {
        Intent it = new Intent(getApplicationContext(), ChekInCheckOutActivity.class);
        it.putExtra("type", type);
        startActivityForResult(it, 2);
    }

}
