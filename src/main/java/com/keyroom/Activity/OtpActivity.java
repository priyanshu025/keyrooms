package com.keyroom.Activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.gson.JsonObject;
import com.keyroom.Network.API;
import com.keyroom.Network.Config;
import com.keyroom.Network.SmsBroadcastReceiver;
import com.keyroom.R;
import com.keyroom.Utility.SharedPrefs;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText et1;
    EditText et2;
    EditText et3;
    EditText et4;
    TextView txtVerify;
    TextView txt_resend;


    TextView txtView;
    String countrycode;
    String phoneNo;
    Integer user_id;
    API api;
    // ProgressBar progressBar;


    private static final int REQUEST_USER_CONSENT = 200;
    SmsBroadcastReceiver smsBroadcastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = Config.getClient().create(API.class);
        setContentView(R.layout.activity_otp);
        initView();

        startSmartUserConsent();
    }


    private void startSmartUserConsent() {

        SmsRetrieverClient client = SmsRetriever.getClient(this);
        client.startSmsUserConsent(null);

    }

    private void registerBroadcastReceiver() {

        smsBroadcastReceiver = new SmsBroadcastReceiver();
        smsBroadcastReceiver.smsBroadcastReceiverListener = new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
            @Override
            public void onSuccess(Intent intent) {

                startActivityForResult(intent, REQUEST_USER_CONSENT);
            }

            @Override
            public void onFailure() {

            }
        };
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcastReceiver, intentFilter);

    }

    private void initView() {
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);
        txtVerify = findViewById(R.id.txtVerify);
        txtView = findViewById(R.id.txtView);
        txt_resend = findViewById(R.id.txt_resend);
        // progressBar = findViewById(R.id.progressBar);


        countrycode = getIntent().getStringExtra("countrycode");
        phoneNo = getIntent().getStringExtra("phoneNo");
        user_id = getIntent().getIntExtra("user_id", 0);

        txtView.setText("We have sent temporary passcode to you at " + "+" + countrycode + "-" + phoneNo);

        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    et2.requestFocus();
                } else if (s.length() == 0) {
                    et1.requestFocus();
                }
            }
        });

        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    et3.requestFocus();
                } else if (s.length() == 0) {
                    et1.requestFocus();
                }
            }
        });

        et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    et4.requestFocus();
                } else if (s.length() == 0) {
                    et2.requestFocus();
                }
            }
        });

        et4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    et4.requestFocus();
                } else if (s.length() == 0) {
                    et3.requestFocus();
                }
            }

        });
        clickEvent();
    }

    private void clickEvent() {
        txtVerify.setOnClickListener(this);
        txt_resend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txtVerify) {
            if (valid()) {
                verifyOTP();
            }
        }
        if (v.getId() == R.id.txt_resend) {
            sendOtpAPI();
        }
    }

    private void sendOtpAPI() {

        Call<JsonObject> call = api.login("+" + countrycode, phoneNo);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void verifyOTP() {
        // progressBar.setVisibility(View.VISIBLE);
        final String otp = et1.getText().toString() + "" + et2.getText().toString() + "" + et3.getText().toString() + "" + et4.getText().toString();
        Call<JsonObject> call = api.verify(user_id, otp);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {

                    Log.e("otp_verfiy", "" + response.body());
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getBoolean("status")) {
                        JSONObject object = jsonObject.getJSONObject("userData");
                        SharedPrefs.setValue(SharedPrefs.Access_Token, jsonObject.getString("AuthenicateToken"));
                        SharedPrefs.setInt(SharedPrefs.User_id, object.getInt("id"));
                        SharedPrefs.setValue(SharedPrefs.User_Name, object.getString("first_name") + " " + object.getString("last_name"));
                        SharedPrefs.setValue(SharedPrefs.First_Name, object.getString("first_name"));
                        SharedPrefs.setValue(SharedPrefs.Last_Name, object.getString("last_name"));
                        SharedPrefs.setValue(SharedPrefs.Email, object.getString("email"));

                        if (!object.getString("birthday").equals("null")) {
                            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                            DateFormat outputFormat = new SimpleDateFormat("dd - MMM - yyyy");
                            Date date = inputFormat.parse(object.getString("birthday"));
                            String outputDateStr = outputFormat.format(date);
                            SharedPrefs.setValue(SharedPrefs.Birthday_Date_Display, object.getString("birthday"));
                            SharedPrefs.setValue(SharedPrefs.Birthday_Date, outputDateStr);
                        }

                        if (!object.getString("city").equals("null")) {
                            SharedPrefs.setValue(SharedPrefs.City, object.getString("city"));
                            SharedPrefs.setValue(SharedPrefs.State, object.getString("state"));
                            SharedPrefs.setValue(SharedPrefs.Country, object.getString("country"));
                            SharedPrefs.setValue(SharedPrefs.Zip_Code, object.getString("zip_code"));
                        }
                        SharedPrefs.setValue(SharedPrefs.SKIP_LOGIN, "no");

                        DashboardActivity();

                    } else {
                        Toast.makeText(OtpActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
             /*   if (progressBar != null && progressBar.isShown()) {
                    progressBar.setVisibility(View.GONE);
                }*/
                t.printStackTrace();
            }
        });
    }

    private void signUpActivity() {

        Intent it = new Intent(OtpActivity.this, SingUpActivity.class);
        it.putExtra("user_id", user_id);
        it.putExtra("phone", phoneNo);
        startActivity(it);
    }

    private void DashboardActivity() {
        Intent it = new Intent(OtpActivity.this, NewHomeActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(it);
    }

    private boolean valid() {
        boolean isValid = true;
        if (TextUtils.isEmpty(et1.getText().toString()) || TextUtils.isEmpty(et2.getText().toString()) || TextUtils.isEmpty(et3.getText().toString()) || TextUtils.isEmpty(et4.getText().toString())) {
            isValid = false;
            Toast.makeText(getApplicationContext(), "Please enter valid otp", Toast.LENGTH_SHORT).show();
        }
        return isValid;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_USER_CONSENT) {
            if (resultCode == RESULT_OK && data != null) {

                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);

                getOTP(message);


            }
        }
    }

    private void getOTP(String msg) {

        Pattern otpPattern = Pattern.compile("(|^)\\d{4}");
        Matcher matcher = otpPattern.matcher(msg);
        if (matcher.find()) {

            String val = matcher.group(0);
            Log.e("getOtp=", "" + matcher.group(0));
            if (val != null) {
                et1.setText("" + val.charAt(0));
                et2.setText("" + val.charAt(1));
                et3.setText("" + val.charAt(2));
                et4.setText("" + val.charAt(3));
                Log.e("value 1", "" + val.charAt(0));
                Log.e("value 2", "" + val.charAt(1));
                Log.e("value 3", "" + val.charAt(2));
                Log.e("value 4", "" + val.charAt(3));
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcastReceiver);
    }
}
