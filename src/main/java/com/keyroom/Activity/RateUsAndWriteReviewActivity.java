package com.keyroom.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.keyroom.Network.API;
import com.keyroom.Network.ConfigHader;
import com.keyroom.R;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uz.jamshid.library.ExactRatingBar;

public class RateUsAndWriteReviewActivity extends AppCompatActivity {
    EditText edtTitle, edtContent;
    TextView txtLeaveReview;
    RatingBar erbOrganization, erbFriendliness, erbLocation, erbSafety;
    RatingBar erbService;
    float organization = 0, friendliness = 0, location = 0, safety = 0;
    float service = 0;
    ImageView imgBack;
    int hotel_id;
    //ProgressBar progressBar;
    API api;
    JSONObject rateJsonObject = new JSONObject();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = ConfigHader.getClient().create(API.class);
        setContentView(R.layout.activty_rateus_review);
        initView();
    }

    private void initView() {
        imgBack = findViewById(R.id.imgClose);
        edtTitle = findViewById(R.id.edtTitle);
        edtContent = findViewById(R.id.edtContent);
        erbService = findViewById(R.id.erbService);
        erbOrganization = findViewById(R.id.erbOrganization);
        erbFriendliness = findViewById(R.id.erbFriendliness);
        erbLocation = findViewById(R.id.erbLocation);
        erbSafety = findViewById(R.id.erbSafety);
        txtLeaveReview = findViewById(R.id.txtLeaveReview);
       // progressBar = findViewById(R.id.progressBar);
        hotel_id = getIntent().getIntExtra("hotel_id", -1);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtLeaveReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApi();
            }
        });

    }

    private void callApi() {
        service = erbService.getRating();
        organization = erbOrganization.getRating();
        friendliness = erbFriendliness.getRating();
        location = erbLocation.getRating();
        safety = erbSafety.getRating();
        try {
            rateJsonObject.put("Service", service);
            rateJsonObject.put("Organization", organization);
            rateJsonObject.put("Friendliness", friendliness);
            rateJsonObject.put("Location", location);
            rateJsonObject.put("Safety", safety);
        } catch (JSONException e) {
            e.printStackTrace();
        }
       // progressBar.setVisibility(View.VISIBLE);
        Call<JsonObject> call = api.rateUsAndWriteReview(edtTitle.getText().toString(), edtContent.getText().toString(), hotel_id, "hotel", rateJsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                  /*  if (progressBar != null && progressBar.isShown()) {
                        progressBar.setVisibility(View.GONE);
                    }*/
                    Log.e("review service==>","@@"+response.body());
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    Toast.makeText(RateUsAndWriteReviewActivity.this, jsonObject.getString("messages"), Toast.LENGTH_LONG).show();
                    if (jsonObject.getBoolean("status")) {
                        finish();
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
                t.printStackTrace();
            }
        });
    }


}
