package com.keyroom.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.JsonObject;
import com.keyroom.Adapter.RoomTypeAdapter;
import com.keyroom.Fragment.HomeFragment;
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

public class UserInformationAfterCheckOutActivity extends AppCompatActivity {
    TextView txtUserName;
    ImageView imgClose;

    TextView txtBookingID;
    TextView txtLocate,txtCallAtHotel;

    TextView txtAddress;
    TextView txtBookingDate;
    TextView txtRoom;
    TextView txtBookingStartDate;
    TextView txtBookingEndDate;
    TextView txtAdult;
    TextView txtHotelPrice;
    TextView txtServiceCharge;
    Intent intent;
    API apiHearde;
    String lat = "", log = "";
    final int REQUEST_CODE_ASK_PERMISSIONS = 1000;
    String phone_no = "";
    ImageView imageView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfoaftercheckout);
        apiHearde = ConfigHader.getClient().create(API.class);
        intent=getIntent();
        initView();
        clickEvent();

        getDetails();

    }
    private void getDetails() {
        ArrayList<RoomTypeModel> roomTypeModels = new ArrayList<>();
        String code=intent.getStringExtra("code");
        Call<JsonObject> call = apiHearde.bookingDetilesHistory(code);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {

                    Log.e("bookingDetails=","@#"+response.body());
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


                        JSONObject hotelData = jsonData.getJSONObject("hotel");
                        phone_no = hotelData.getString("contact_no");
                        lat = hotelData.getString("map_lat");
                        log = hotelData.getString("map_lng");

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

    private void clickEvent() {

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserInformationAfterCheckOutActivity.this,NewHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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
        txtCallAtHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(UserInformationAfterCheckOutActivity.this,
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
                        Toast.makeText(UserInformationAfterCheckOutActivity.this, "Phone number not provided", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }

    private void initView() {
        txtUserName = findViewById(R.id.txtUserName);
        txtRoom=findViewById(R.id.txtRoom);
        txtAddress=findViewById(R.id.txtaddress);
        imgClose = findViewById(R.id.cancel_btn);
        txtLocate=findViewById(R.id.txtLocate);
        txtCallAtHotel = findViewById(R.id.txtCallAtHotel);
        txtBookingID = findViewById(R.id.txtBookingID);
        imageView=findViewById(R.id.image);
        txtBookingDate = findViewById(R.id.txtBookingDate);
        txtBookingStartDate = findViewById(R.id.txtBookingStartDate);
        txtBookingEndDate = findViewById(R.id.txtBookingEndDate);
        txtAdult = findViewById(R.id.txtAdult);
        txtHotelPrice = findViewById(R.id.txtHotelPrice);
        txtServiceCharge = findViewById(R.id.txtServiceCharge);
        
        txtUserName.setText(SharedPrefs.getValue(SharedPrefs.First_Name));
        txtBookingID.setText("Booking ID:"+intent.getIntExtra("id",-1));
        txtAdult.setText(intent.getStringExtra("adults"));
        txtHotelPrice.setText(intent.getStringExtra("price"));
        txtServiceCharge.setText(intent.getStringExtra("title"));
        txtAddress.setText(intent.getStringExtra("address"));
        txtRoom.setText(intent.getStringExtra("room"));



        try {
            DateFormat BookingDateInputFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat BookingDateOutputFormat = new SimpleDateFormat("dd/MM/yyyy");
            String inputDateStr = intent.getStringExtra("start_date");
            Date date = BookingDateInputFormat.parse(inputDateStr);
            String BookingDateOutputDateStr = BookingDateOutputFormat.format(date);
            txtBookingDate.setText(BookingDateOutputDateStr);
            txtBookingStartDate.setText(HomeFragment.formatDateFromDateString("yyyy-MM-dd","dd-MMM-yyyy",inputDateStr));

            String inputDateStr1 = intent.getStringExtra("end_date");
            Date date1 = BookingDateInputFormat.parse(inputDateStr1);
            txtBookingEndDate.setText(HomeFragment.formatDateFromDateString("yyyy-MM-dd","dd-MMM-yyyy",inputDateStr1));
            String inputDateStr2 = intent.getStringExtra("created_at");
            Date date2 = BookingDateInputFormat.parse(inputDateStr2);
            String BookingDateoutputDateStr2 = BookingDateOutputFormat.format(date2);
            txtBookingDate.setText("Booking Date - "+BookingDateoutputDateStr2);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(UserInformationAfterCheckOutActivity.this,NewHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
