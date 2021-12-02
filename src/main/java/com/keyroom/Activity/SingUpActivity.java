package com.keyroom.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.JsonObject;
import com.keyroom.Network.API;
import com.keyroom.Network.Config;
import com.keyroom.Network.ConfigHader;
import com.keyroom.Network.SmsBroadcastReceiver;
import com.keyroom.R;
import com.keyroom.Utility.SharedPrefs;
import com.truecaller.android.sdk.ITrueCallback;
import com.truecaller.android.sdk.TrueError;
import com.truecaller.android.sdk.TrueProfile;
import com.truecaller.android.sdk.TruecallerSDK;
import com.truecaller.android.sdk.TruecallerSdkScope;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingUpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_USER_CONSENT = 200;
    EditText edtFirstName;
    EditText edtLastName;
    Button txtSingUp;
    API api,apiHeadre;
    Integer user_id;
    String phone;
    String country_code;
    SmsBroadcastReceiver smsBroadcastReceiver;
    EditText et1;
    EditText et2;
    EditText et3;
    EditText et4;
    TextView txtVerify;
    private BottomSheetBehavior mBottomSheetBehavior;
    ImageView imgLogo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = Config.getClient().create(API.class);
        apiHeadre= ConfigHader.getClient().create(API.class);
        user_id = getIntent().getIntExtra("user_id", 0);
        phone = getIntent().getStringExtra("phone");
        country_code = getIntent().getStringExtra("countrycode");
        setContentView(R.layout.activity_singup);

        initView();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        txtSingUp = findViewById(R.id.txtSingUp);

        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);
        imgLogo=findViewById(R.id.imgLogo);


        txtVerify = findViewById(R.id.txtVerify);
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


        CoordinatorLayout mainLayout = findViewById(R.id.mainlayout);
        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull @NotNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull @NotNull View bottomSheet, float slideOffset) {


            }

        });

        mainLayout.setOnTouchListener((v, event) -> {
            hideSoftKeyboard();
            return false;
        });

        clickEvent();
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

    private void clickEvent() {
        txtSingUp.setOnClickListener(this);
        txtVerify.setOnClickListener(this);
        edtFirstName.setOnClickListener(this);
        edtLastName.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txtSingUp) {
            if (valid()) {
                signupAPI(user_id,edtFirstName.getText().toString(),edtLastName.getText().toString(),phone);

            }
        }

        if (v.getId() == R.id.txtVerify) {
            if (valid()) {
                txtVerify.setClickable(false);
                verifyOTP();
            }

        }
        if (v.getId()==R.id.edtFirstName){
            imgLogo.setAlpha(0.5f);
        }
        if (v.getId()==R.id.edtFirstName){
            imgLogo.setAlpha(0.5f);
        }



    }


    private void verifyOTP() {
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
                        txtVerify.setClickable(true);

                    } else {
                        Toast.makeText(SingUpActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                txtVerify.setClickable(true);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_USER_CONSENT) {
            if (resultCode == RESULT_OK && data != null) {
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                getOTP(message);

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
            }

        }
    }


    private boolean valid() {
        boolean isValid = true;

        if (TextUtils.isEmpty(edtFirstName.getText().toString())) {
            isValid = false;
            edtFirstName.setError("This field is required");
        }
        if (TextUtils.isEmpty(edtLastName.getText().toString())) {
            isValid = false;
            edtLastName.setError("This field is required");
        }

        return isValid;
    }


    private void signupAPI(Integer user_id,String first_name,String last_name,String phone) {

        Call<JsonObject> call = api.register(user_id, first_name, last_name,
                "", phone, "");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {

                    Log.e("signUp", "@@" + response.body());
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getBoolean("status")) {
                        JSONObject object = jsonObject.getJSONObject("userData");
                        SharedPrefs.setValue(SharedPrefs.Access_Token, jsonObject.getString("AuthenicateToken"));
                        SharedPrefs.setInt(SharedPrefs.User_id, object.getInt("id"));
                        SharedPrefs.setValue(SharedPrefs.User_Name, object.getString("first_name") + " " + object.getString("last_name"));
                        SharedPrefs.setValue(SharedPrefs.First_Name, object.getString("first_name"));
                        SharedPrefs.setValue(SharedPrefs.Last_Name, object.getString("last_name"));
                        SharedPrefs.setValue(SharedPrefs.Email, object.getString("email"));
                        SharedPrefs.setValue(SharedPrefs.SKIP_LOGIN, "no");

                       // otpActivity(user_id,country_code,phone);
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);


                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

    private void DashboardActivity() {
        Intent it = new Intent(SingUpActivity.this, NewHomeActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(it);
    }


    public void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    private void otpActivity(int user_id, String countrycode, String phoneNo) {
        Intent it = new Intent(SingUpActivity.this, OtpActivity.class);
        it.putExtra("user_id", user_id);
        it.putExtra("countrycode", countrycode);
        it.putExtra("phoneNo", phoneNo);
        startActivity(it);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        txtVerify.setClickable(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcastReceiver);
        imgLogo.setAlpha(0.9f);
    }

}
