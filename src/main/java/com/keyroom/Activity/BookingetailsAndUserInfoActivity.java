package com.keyroom.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.keyroom.Adapter.RoomTypeAdapter;
import com.keyroom.Model.RoomTypeModel;
import com.keyroom.Network.API;
import com.keyroom.Network.ConfigHader;
import com.keyroom.R;
import com.keyroom.Utility.SharedPrefs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingetailsAndUserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imgBack;

    LinearLayout lnyBooking;
    LinearLayout lnyBookingDetails;
    ImageView imgBooking;
    TextView txtBooking;
    TextView viewBooking, txtBookingID;


    TextView txtBookingStatus;
    TextView txtBookingDate;
    TextView txtBookingStartDate;
    TextView txtBookingEndDate;
    TextView txtNight;
    TextView txtAdult;
    TextView txtHotelPrice;

    TextView txtUserName, txtLastName;
    TextView txtEmail;
    TextView txtPhoneNo;

    TextView txtTotalPrice;
    TextView txtCallAtHotel, txtLocate;

    API apiHearde;
//    ProgressBar progressBar;
    String code;
    final int REQUEST_CODE_ASK_PERMISSIONS = 1000;
    String phone_no = "";
    String lat = "", log = "";
    RecyclerView rcyRoomPrice;
    RoomTypeAdapter roomTypeAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiHearde = ConfigHader.getClient().create(API.class);
        setContentView(R.layout.activity_bookingdetailsanduserinfo);
        code = getIntent().getStringExtra("code");
        initView();
    }

    private void initView() {
        imgBack = findViewById(R.id.imgBack);

        lnyBooking = findViewById(R.id.lnyBooking);
        lnyBookingDetails = findViewById(R.id.lnyBookingDetails);
        imgBooking = findViewById(R.id.imgBooking);
        txtBooking = findViewById(R.id.txtBooking);
        viewBooking = findViewById(R.id.viewBooking);
        txtBookingID = findViewById(R.id.txtBookingID);

        txtBookingStatus = findViewById(R.id.txtBookingStatus);
        txtBookingDate = findViewById(R.id.txtBookingDate);
        txtBookingStartDate = findViewById(R.id.txtBookingStartDate);
        txtBookingEndDate = findViewById(R.id.txtBookingEndDate);
        txtNight = findViewById(R.id.txtNight);
        txtAdult = findViewById(R.id.txtAdult);
        txtHotelPrice = findViewById(R.id.txtHotelPrice);


        txtUserName = findViewById(R.id.txtUserName);
        txtLastName = findViewById(R.id.txtLastName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhoneNo = findViewById(R.id.txtPhoneNo);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);

        txtCallAtHotel = findViewById(R.id.txtCallAtHotel);
        txtLocate = findViewById(R.id.txtLocate);
        rcyRoomPrice = findViewById(R.id.rcyRoomPrice);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BookingetailsAndUserInfoActivity.this);
        rcyRoomPrice.setLayoutManager(linearLayoutManager);

        onClick();
        getDetails();
    }


    private void getDetails() {
        ArrayList<RoomTypeModel> roomTypeModels = new ArrayList<>();
        Call<JsonObject> call = apiHearde.bookingDetilesHistory(code);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getInt("status") == 1) {
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        JSONArray jsonRoom = jsonData.getJSONArray("rooms");

                        for (int i = 0; i < jsonRoom.length(); i++) {
                            JSONObject jsonRoomObj = jsonRoom.getJSONObject(i);
                            RoomTypeModel roomTypeModel = new RoomTypeModel();
                            roomTypeModel.setName(jsonRoomObj.getString("name"));
                            roomTypeModel.setNumber(jsonRoomObj.getInt("number"));
                            roomTypeModel.setPrice(jsonRoomObj.getInt("price"));
                            roomTypeModels.add(roomTypeModel);
                        }

                        roomTypeAdapter = new RoomTypeAdapter(BookingetailsAndUserInfoActivity.this, roomTypeModels);
                        rcyRoomPrice.setAdapter(roomTypeAdapter);


                        txtBookingStatus.setText(jsonData.getString("status"));
                        txtNight.setText(jsonData.getString("nights"));
                        txtAdult.setText(jsonData.getString("adults"));
                        txtBookingID.setText("Booking ID:" + jsonData.getString("id"));

                        DateFormat BookingDateinputFormat = new SimpleDateFormat("yyyy-MM-dd");
                        DateFormat BookingDateoutputFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String inputDateStr = jsonData.getString("created_at");
                        Date date = BookingDateinputFormat.parse(inputDateStr);
                        String BookingDateoutputDateStr = BookingDateoutputFormat.format(date);
                        txtBookingDate.setText(BookingDateoutputDateStr);

                        DateFormat BookingStartDateinputFormat = new SimpleDateFormat("yyyy-MM-dd");
                        DateFormat BookingStartDateoutputFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String BookingStartDateinputDateStr = jsonData.getString("start_date");
                        Date BookingStartDatedate = BookingStartDateinputFormat.parse(BookingStartDateinputDateStr);
                        String BookingStartDateoutputDateStr = BookingStartDateoutputFormat.format(BookingStartDatedate);
                        txtBookingStartDate.setText(BookingStartDateoutputDateStr);

                        DateFormat BookingEndDateinputFormat = new SimpleDateFormat("yyyy-MM-dd");
                        DateFormat BookingEndDateoutputFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String BookingEndDateinputDateStr = jsonData.getString("end_date");
                        Date BookingEndDatedate = BookingEndDateinputFormat.parse(BookingEndDateinputDateStr);
                        String BookingEndDateoutputDateStr = BookingEndDateoutputFormat.format(BookingEndDatedate);
                        txtBookingEndDate.setText(BookingEndDateoutputDateStr);

                        txtUserName.setText(jsonData.getString("first_name"));
                        txtLastName.setText(jsonData.getString("last_name"));
                        txtEmail.setText(jsonData.getString("email"));
                        txtPhoneNo.setText(jsonData.getString("phone"));

                        txtTotalPrice.setText(jsonData.getString("total") + "" + "\u20B9");
                        JSONObject hotelData = jsonData.getJSONObject("hotel");
                        phone_no = hotelData.getString("contact_no");
                        lat = hotelData.getString("map_lat");
                        log = hotelData.getString("map_lng");

                    } else {
                        if (jsonObject.getString("messages").equals("Token is Expired")) {
                            logoutUser();
                        } else {
                            Toast.makeText(BookingetailsAndUserInfoActivity.this, jsonObject.getString("messages"), Toast.LENGTH_SHORT).show();
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

    private void logoutUser() {
        SharedPrefs.clearData();
        Intent it = new Intent(BookingetailsAndUserInfoActivity.this, LoginActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(it);
    }


    private void onClick() {
        lnyBooking.setOnClickListener(this::onClick);
        imgBack.setOnClickListener(this::onClick);
        txtCallAtHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(BookingetailsAndUserInfoActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                                REQUEST_CODE_ASK_PERMISSIONS);
                    }
                } else {
                    if (!phone_no.equals("") && phone_no != null) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone_no));
                        startActivity(intent);
                    } else {
                        Toast.makeText(BookingetailsAndUserInfoActivity.this, "Phone number not provided", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
        txtLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + log);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Call Permission Granted..Please dial again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Call permission not granted", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;
        }
    }
}
