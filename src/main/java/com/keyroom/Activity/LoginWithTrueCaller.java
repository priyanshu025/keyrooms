package com.keyroom.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.keyroom.Network.API;
import com.keyroom.Network.Config;
import com.keyroom.R;
import com.keyroom.Utility.SharedPrefs;
import com.truecaller.android.sdk.TruecallerSDK;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginWithTrueCaller extends AppCompatActivity {

    API api;
    String first_name;
    String last_name;
    String code;
    String phone;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = Config.getClient().create(API.class);
        setContentView(R.layout.activity_login_with_true_caller);
        first_name = getIntent().getStringExtra("first_name");
        last_name = getIntent().getStringExtra("last_name");
        code = getIntent().getStringExtra("code");
        phone = getIntent().getStringExtra("phone");


        dialog = new ProgressDialog(this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setMessage("Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        callTrueCallerApi(phone);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TruecallerSDK.clear();
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions,
                                           @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void callTrueCallerApi(String str2) {
        Call<JsonObject> call = api.checkUser(str2);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("response=", "" + response.body());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getString("user_type").equals("old_user")) {
                        SharedPrefs.setValue(SharedPrefs.Access_Token, jsonObject.getString("AuthenicateToken"));
                        SharedPrefs.setInt(SharedPrefs.User_id, jsonObject.getInt("user_id"));
                        SharedPrefs.setValue(SharedPrefs.User_Name, jsonObject.getString("first_name") + " " + jsonObject.getString("last_name"));
                        SharedPrefs.setValue(SharedPrefs.First_Name, jsonObject.getString("first_name"));
                        SharedPrefs.setValue(SharedPrefs.Last_Name, jsonObject.getString("last_name"));
                        SharedPrefs.setValue(SharedPrefs.Phone_No, phone);
                        SharedPrefs.setValue(SharedPrefs.Country_Code, code.substring(1, 3));

                        SharedPrefs.setValue(SharedPrefs.SKIP_LOGIN, "no");
                        dialog.dismiss();
                        DashboardActivity();
                    } else {
                        login();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
            }
        });

    }

    private void login() {
        Call<JsonObject> call = api.truecallerLogin(first_name, last_name, phone, code);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("response=", "" + response.body());

                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    JSONObject object = jsonObject.getJSONObject("userData");
                    SharedPrefs.setValue(SharedPrefs.Access_Token, jsonObject.getString("token"));
                    SharedPrefs.setInt(SharedPrefs.User_id, object.getInt("id"));
                    SharedPrefs.setValue(SharedPrefs.User_Name, object.getString("first_name") + " " + object.getString("last_name"));
                    SharedPrefs.setValue(SharedPrefs.First_Name, object.getString("first_name"));
                    SharedPrefs.setValue(SharedPrefs.Last_Name, object.getString("last_name"));
                    SharedPrefs.setValue(SharedPrefs.Email, object.getString("email"));
                    SharedPrefs.setValue(SharedPrefs.Country_Code, code.substring(1, 3));
                    SharedPrefs.setValue(SharedPrefs.Phone_No, phone);
                    SharedPrefs.setValue(SharedPrefs.SKIP_LOGIN, "no");

                    dialog.dismiss();
                   DashboardActivity();


                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
            }
        });

    }


    private void DashboardActivity() {
        Intent it = new Intent(LoginWithTrueCaller.this, NewHomeActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(it);
    }
}