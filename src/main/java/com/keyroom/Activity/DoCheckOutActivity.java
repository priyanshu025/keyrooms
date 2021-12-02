package com.keyroom.Activity;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.JsonObject;
import com.keyroom.Fragment.BookingFragment;
import com.keyroom.Fragment.HomeFragment;
import com.keyroom.Network.API;
import com.keyroom.Network.ConfigHader;
import com.keyroom.R;
import com.keyroom.Utility.SharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoCheckOutActivity extends AppCompatActivity {
    TextView txtUserName;
    TextView txtEmail;
    TextView txtPhoneNo;
    TextView txtSubmit;
    String offline_payment;
    String condition;
    RelativeLayout bookNow;

    TextView txtBookingStartDate;
    TextView txtBookingEndDate;
    TextView txtAdult;

    ProgressDialog progressDoalog;

    private String CHANNEL_ID = "my_channel_01";
    private CharSequence name = "my_channel";
    private String Description = "This is my channel";


    int totalamount;

    EditText couponEditText;
    Button couponApplyButton;
    TextView originalPrice;
    TextView couponPrice;
    TextView totalPayAmount;
    LinearLayout totalPriceLinearLayout;
    String mainprice;
    String couponAmount;
    String txtusername;
    String email;
    String phoneno;
    String lastname;
    Intent intent;
    String image;
    String phone_no = "9560509906";
    ImageView phone_icon, notification_icon;
    LinearLayout price_layout, promo_Layout;



    String code;
    API apiHearde;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apiHearde = ConfigHader.getClient().create(API.class);
        setContentView(R.layout.activity_docheckout);
        initView();
        clickEvent();
    }

    private void initView() {

        phone_icon = findViewById(R.id.phone_icon);
        notification_icon = findViewById(R.id.notification_icon);
        bookNow = findViewById(R.id.bottom_book_now);
        price_layout = findViewById(R.id.price_layout);
        promo_Layout = findViewById(R.id.promo_layout);


        offline_payment = "offline_payment";
        condition = "on";


        // coupon layout
        couponEditText = findViewById(R.id.edittext_coupon_code);
        couponApplyButton = findViewById(R.id.coupon_apply_btn);
        originalPrice = findViewById(R.id.original_price);
        couponPrice = findViewById(R.id.coupon_price);
        totalPayAmount = findViewById(R.id.total_pay_amount);
        totalPriceLinearLayout = findViewById(R.id.price_total_linearLayout);
        // coupon layout
        txtUserName = findViewById(R.id.txtUserName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhoneNo = findViewById(R.id.txtPhoneNo);
        txtSubmit = findViewById(R.id.txtSubmit);
        intent = getIntent();
        code = intent.getStringExtra("code");


        txtBookingStartDate = findViewById(R.id.txtBookingStartDate);
        txtBookingEndDate = findViewById(R.id.txtBookingEndDate);
        txtAdult = findViewById(R.id.txtAdult);

        txtusername = SharedPrefs.getValue(SharedPrefs.First_Name);
        lastname = SharedPrefs.getValue(SharedPrefs.Last_Name);
        email = SharedPrefs.getValue(SharedPrefs.Email);
        phoneno = SharedPrefs.getValue(SharedPrefs.Phone_No);
        txtUserName.setText(SharedPrefs.getValue(SharedPrefs.First_Name) + " " + SharedPrefs.getValue(SharedPrefs.Last_Name));
        if (!SharedPrefs.getValue(SharedPrefs.Email).equals(null))
        {
            txtEmail.setText(SharedPrefs.getValue(SharedPrefs.Email));
        }
        else
            txtEmail.setHint("No Email Found");
        txtPhoneNo.setText("+91 " + SharedPrefs.getValue(SharedPrefs.Phone_No));
        txtAdult.setText(intent.getStringExtra("adults") + " Guest");
        originalPrice.setText("Rs." + intent.getStringExtra("price") + "/-");
        Log.e("total price==>", "@@" + intent.getStringExtra("price"));


        try {
            DateFormat BookingDateinputFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat BookingDateoutputFormat = new SimpleDateFormat("dd/MM/yyyy");
            String inputDateStr = intent.getStringExtra("start_date");
            Date date = BookingDateinputFormat.parse(inputDateStr);
            String BookingDateoutputDateStr = BookingDateoutputFormat.format(date);
            txtBookingStartDate.setText(HomeFragment.formatDateFromDateString("yyyy-MM-dd", "dd-MMM-yyyy", inputDateStr));
            // txtBookingStartDate.setText(BookingDateoutputDateStr);
            String inputDateStr1 = intent.getStringExtra("end_date");
            Date date1 = BookingDateinputFormat.parse(inputDateStr1);
            String BookingDateoutputDateStr1 = BookingDateoutputFormat.format(date1);
            txtBookingEndDate.setText(HomeFragment.formatDateFromDateString("yyyy-MM-dd", "dd-MMM-yyyy", inputDateStr1));
            //txtBookingEndDate.setText(BookingDateoutputDateStr1);

            String inputDateStr2 = intent.getStringExtra("created_at");
            Date date2 = BookingDateinputFormat.parse(inputDateStr2);
            String BookingDateoutputDateStr2 = BookingDateoutputFormat.format(date2);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clickEvent() {

        totalamount = Integer.parseInt(intent.getStringExtra("price"));

        ///// coupon layout
        couponApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String edittextcoupon = couponEditText.getText().toString().trim();
                if (!edittextcoupon.equals("")) {
                    Call<JsonObject> call = apiHearde.couponcode(edittextcoupon);
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            Log.e("coupon==>Response", "@@" + response);
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().toString());
                                if (jsonObject.getBoolean("status")) {
                                    if (jsonObject.getString("amount_type").equals("fixed")) {
                                        if (jsonObject.getBoolean("status")) {
                                            totalamount = 0;
                                            price_layout.setVisibility(View.VISIBLE);
                                            promo_Layout.setVisibility(View.VISIBLE);
                                            mainprice = intent.getStringExtra("price");
                                            originalPrice.setText("Rs." + mainprice + "/-");

                                            couponAmount = jsonObject.getString("amount");
                                            couponPrice.setText("Rs." + couponAmount + "/-");
                                            totalamount = Integer.parseInt(mainprice) - Integer.parseInt(couponAmount);
                                            totalPayAmount.setText("Rs." + totalamount + "/-");

                                            Toast.makeText(DoCheckOutActivity.this, "" + jsonObject.getString("messages"), Toast.LENGTH_SHORT).show();

                                        } else {
                                            price_layout.setVisibility(View.GONE);
                                            promo_Layout.setVisibility(View.GONE);
                                            Toast.makeText(DoCheckOutActivity.this, "" + jsonObject.getString("messages"), Toast.LENGTH_SHORT).show();
                                            totalamount = Integer.parseInt(intent.getStringExtra("price"));

                                        }
                                    }
                                    if (jsonObject.getString("amount_type").equals("percent")) {
                                        if (jsonObject.getBoolean("status")) {
                                            totalamount = 0;
                                            price_layout.setVisibility(View.VISIBLE);
                                            promo_Layout.setVisibility(View.VISIBLE);
                                            mainprice = intent.getStringExtra("price");
                                            originalPrice.setText("Rs." + mainprice + "/-");
                                            String couponPercentage = jsonObject.getString("amount");
                                            couponPrice.setText(couponPercentage + "%");

                                            int percentagePrice = ((Integer.parseInt(mainprice) * Integer.parseInt(couponPercentage)) / 100);
                                            totalPayAmount.setText("Rs." + (Integer.parseInt(mainprice) - percentagePrice) + "/-");
                                            totalamount = Integer.parseInt(mainprice) - percentagePrice;
                                        } else {
                                            price_layout.setVisibility(View.GONE);
                                            promo_Layout.setVisibility(View.GONE);
                                            Toast.makeText(DoCheckOutActivity.this, "" + jsonObject.getString("messages"), Toast.LENGTH_SHORT).show();
                                            totalamount = Integer.parseInt(intent.getStringExtra("price"));
                                        }

                                    }
                                }else {
                                    price_layout.setVisibility(View.GONE);
                                    promo_Layout.setVisibility(View.GONE);
                                    Toast.makeText(DoCheckOutActivity.this, "" + jsonObject.getString("messages"), Toast.LENGTH_SHORT).show();
                                    totalamount = Integer.parseInt(intent.getStringExtra("price"));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {

                        }
                    });

                } else {
                    Toast.makeText(DoCheckOutActivity.this, "Please enter the Promo code", Toast.LENGTH_SHORT).show();
                }
            }
        });


        bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCheckOut();
                progressDoalog = new ProgressDialog(DoCheckOutActivity.this);
                progressDoalog.setMax(100);
                progressDoalog.setMessage("Please wait....");
                progressDoalog.setTitle("Confirming your stay");
                progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDoalog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (progressDoalog.getProgress() <= progressDoalog
                                    .getMax()) {
                                Thread.sleep(200);
                                //handle.sendMessage(handle.obtainMessage());
                                if (progressDoalog.getProgress() == progressDoalog
                                        .getMax()) {
                                    progressDoalog.dismiss();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });

        phone_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(DoCheckOutActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                                101);
                    }
                } else {
                    if (!phone_no.equals("") && phone_no != null) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone_no));
                        startActivity(intent);
                    } else {
                        Toast.makeText(DoCheckOutActivity.this, "Phone number not provided", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        notification_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(DoCheckOutActivity.this, NotificationActivity.class);
                startActivity(it);
            }
        });

    }

    private void doCheckOut() {
        // progressBar.setVisibility(View.VISIBLE);
        Log.e("code==>Details", "@@@@" + code);
        Log.e("username==", "@@@@" + txtUserName.getText().toString());
        Log.e("email==", "@@@@" + txtEmail.getText().toString());
        Log.e("phone==", "@@@@" + txtPhoneNo.getText().toString());
        Log.e("amount==", "@@" + totalamount);
        Call<JsonObject> call = apiHearde.doCheckOut(code, txtusername, lastname, email, phoneno, offline_payment, condition, String.valueOf(totalamount));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("response########", "@@" + response.body());
              /*  if (progressBar != null && progressBar.isShown()) {
                    progressBar.setVisibility(View.GONE);
                }*/
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));

                    if (jsonObject.getBoolean("status")) {
                        JSONObject rowJsonObject = jsonObject.getJSONObject("row");
                        Log.e("book code==>", "@@" + rowJsonObject.getString("code"));
                        startActivity(new Intent(DoCheckOutActivity.this, UserInformationAfterCheckOutActivity.class)

                                        .putExtra("code", rowJsonObject.getString("code"))
                                        .putExtra("id", rowJsonObject.getInt("id"))
                                        .putExtra("created_at", rowJsonObject.getString("created_at"))
                                        .putExtra("start_date", rowJsonObject.getString("start_date"))
                                        .putExtra("end_date", rowJsonObject.getString("end_date"))
                                        .putExtra("status", rowJsonObject.getString("status"))
                                        .putExtra("gateway", rowJsonObject.getString("gateway"))
                                        .putExtra("nights", intent.getStringExtra("nights"))
                                        .putExtra("adults", intent.getStringExtra("adults"))
                                        .putExtra("title", intent.getStringExtra("title"))
                                        .putExtra("address", intent.getStringExtra("address"))
                                        .putExtra("room", intent.getStringExtra("room"))
//                                .putExtra("price", intent.getStringExtra("price"))
                                        .putExtra("price", String.valueOf(totalamount))
                        );
                        progressDoalog.dismiss();
                        Log.e("total amount==>", "@@" + totalamount);

                        int NOTIFICATION_ID = 234;
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            int importance = NotificationManager.IMPORTANCE_HIGH;
                            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                            mChannel.setDescription(Description);
                            mChannel.enableLights(true);
                            mChannel.setLightColor(Color.RED);
                            mChannel.enableVibration(true);
                            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                            mChannel.setShowBadge(false);
                            notificationManager.createNotificationChannel(mChannel);
                        }

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(DoCheckOutActivity.this, CHANNEL_ID)
                                .setSmallIcon(R.drawable.blue_tick)
                                .setContentTitle("Your booking has been confirmed " + txtUserName.getText().toString()+"!")
                                .setContentText("Enjoy your stay at "+intent.getStringExtra("title"));

                        Intent resultIntent = new Intent(DoCheckOutActivity.this, BookingFragment.class);
                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(DoCheckOutActivity.this);
                        stackBuilder.addParentStack(DoCheckOutActivity.class);
                        stackBuilder.addNextIntent(resultIntent);
                        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                        builder.setContentIntent(resultPendingIntent);
                        notificationManager.notify(NOTIFICATION_ID, builder.build());
                        finish();

                    } else {
                        Toast.makeText(DoCheckOutActivity.this, jsonObject.getString("messages"), Toast.LENGTH_LONG).show();
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
                Toast.makeText(DoCheckOutActivity.this, "Something went wrong!!!", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }
}
