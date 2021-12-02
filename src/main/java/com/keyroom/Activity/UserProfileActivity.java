package com.keyroom.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.keyroom.Network.API;
import com.keyroom.Network.ConfigHader;
import com.keyroom.R;
import com.keyroom.Utility.SharedPrefs;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {


    TextView txtEdit;
    ImageView imgBack;
    TextView txtPhoneNo;
    TextView txtUserName;
    TextView logOut;

    RelativeLayout personalInfo,bookingHistory,wallet,referAndEarn,helpAndSupport,listYourProperty;
    API api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = ConfigHader.getClient().create(API.class);
        setContentView(R.layout.activity_userprofile);
        initView();
        setProfileData();
    }

    private void initView() {

        txtEdit = findViewById(R.id.txtEdit);
        imgBack = findViewById(R.id.imgBack);
        personalInfo=findViewById(R.id.personalInfo);
        bookingHistory=findViewById(R.id.bookingHistory);
        wallet=findViewById(R.id.wallet);
        referAndEarn=findViewById(R.id.referAndEarn);
        helpAndSupport=findViewById(R.id.helpAndSupport);
        txtPhoneNo = findViewById(R.id.txtPhoneNo);
        txtUserName = findViewById(R.id.txtUserName);
        //listYourProperty=findViewById(R.id.listYourProperty);
        logOut=findViewById(R.id.logOut);

        clickEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setProfileData();
    }

    private void setProfileData() {
        txtUserName.setText(SharedPrefs.getValue(SharedPrefs.First_Name) + " " + SharedPrefs.getValue(SharedPrefs.Last_Name));
        txtPhoneNo.setText("+" + SharedPrefs.getValue(SharedPrefs.Country_Code) + " " + SharedPrefs.getValue(SharedPrefs.Phone_No));
    }

    private void clickEvent() {
        imgBack.setOnClickListener(this);
        logOut.setOnClickListener(this);
        personalInfo.setOnClickListener(this);
        bookingHistory.setOnClickListener(this);
        wallet.setOnClickListener(this);
        referAndEarn.setOnClickListener(this);
        helpAndSupport.setOnClickListener(this);
        txtEdit.setOnClickListener(this);
        //listYourProperty.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case  R.id.imgBack:
                super.onBackPressed();
                break;
            case R.id.logOut:
                logoutDialog();
                break;

            case R.id.txtEdit:
                editProfile();
                break;

            case R.id.personalInfo:
                Toast.makeText(this, "personal info", Toast.LENGTH_SHORT).show();
                editProfile();
                break;
            case R.id.bookingHistory:
                Toast.makeText(this, "booking history", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UserProfileActivity.this,NewBookingActivity.class));

                break;
            case R.id.wallet:
                Toast.makeText(this, "wallet is not active", Toast.LENGTH_SHORT).show();
                break;
            case R.id.referAndEarn:
                Toast.makeText(this, "refer and earn", Toast.LENGTH_SHORT).show();
                break;
            case R.id.helpAndSupport:
                startActivity(new Intent(UserProfileActivity.this,HelpAndSupportActivity.class));
                break;

//            case R.id.listYourProperty:
//                Intent intent1=new Intent(UserProfileActivity.this, PagesActivity.class);
//                intent1.putExtra("url","https://keyrooms.in/property");
//                intent1.putExtra("pageName","Partner With keyrooms");
//                startActivity(intent1);
//                break;

        }

    }

    private void logoutDialog() {
        Dialog dialog = new Dialog(UserProfileActivity.this);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_logout);
        dialog.setCancelable(true);

        TextView txtNo = dialog.findViewById(R.id.txtNo);
        TextView txtYes = dialog.findViewById(R.id.txtYes);
        TextView txtMsg = dialog.findViewById(R.id.txtMsg);

        txtMsg.setText("Are you sure you want to logout?");

        txtYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
                dialog.dismiss();
            }
        });
        txtNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void logoutUser() {
        Call<JsonObject> call = api.userLogout();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getBoolean("status")) {
                        SharedPrefs.clearData();
                        Intent it = new Intent(UserProfileActivity.this, LoginActivity.class);
                        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(it);
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



    private void editProfile() {
        Intent it = new Intent(UserProfileActivity.this, EditProfileActivity.class);
        startActivity(it);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
