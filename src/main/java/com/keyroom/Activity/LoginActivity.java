package com.keyroom.Activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.JsonObject;
import com.keyroom.Network.API;
import com.keyroom.Network.Config;
import com.keyroom.Network.SmsBroadcastReceiver;
import com.keyroom.R;
import com.keyroom.Utility.SharedPrefs;
import com.keyroom.Utility.WelcomeScreenPrefs;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;
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
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private final static int CREDENTIAL_PICKER_REQUEST = 1011;
    private static final int REQUEST_USER_CONSENT = 200;
    ProgressBar progressBar, pgBar;
    LinearLayout lnyPhoneNumber;
    CountryCodePicker ccp;
    EditText edtPhoneNumber;
    private int progressStatus = 0;
    Button txtLogin, login_with_truecaller;
    TextView txtSkipLogin;
    API api;
    int user_id;
    SmsBroadcastReceiver smsBroadcastReceiver;
    EditText et1;
    EditText et2;
    EditText et3;
    EditText et4;
    TextView txtVerify;
    TextView txt_resend;
    private Handler handler = new Handler();
    private BottomSheetBehavior mBottomSheetBehavior;
    ImageView image;
    int i=0;

    ProgressDialog progressDoalog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = Config.getClient().create(API.class);
        setContentView(R.layout.activity_login);

        login_with_truecaller = findViewById(R.id.login_with_truecaller);
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

        TruecallerSdkScope trueScope = new TruecallerSdkScope.Builder(this, new ITrueCallback() {
            @Override
            public void onSuccessProfileShared(@NonNull @NotNull TrueProfile trueProfile) {
                Log.e("Success", trueProfile.firstName + " " + trueProfile.lastName);
                String str = trueProfile.phoneNumber.substring(0, 3);
                String str2 = trueProfile.phoneNumber.substring(3, 13);
                Log.i("number1",str);
                Log.i("number2",str2);
                startActivity(new Intent(LoginActivity.this, LoginWithTrueCaller.class)
                        .putExtra("first_name", trueProfile.firstName)
                        .putExtra("last_name", trueProfile.lastName)
                        .putExtra("code", str)
                        .putExtra("phone", str2)
                );
                //startActivity(new Intent(LoginActivity.this,LoginWithTrueCaller.class));
            }

            @Override
            public void onFailureProfileShared(@NonNull @NotNull TrueError trueError) {

                  Log.e("checking",String.valueOf(trueError.getErrorType()));
                  Log.e("checking",String.valueOf(trueError.describeContents()));

                //Toast.makeText(LoginActivity.this, "Error while Loging", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationRequired(TrueError trueError) {

            }
        })
                .consentMode(TruecallerSdkScope.CONSENT_MODE_POPUP)
                .consentTitleOption(TruecallerSdkScope.SDK_CONSENT_TITLE_VERIFY)
                .footerType(TruecallerSdkScope.FOOTER_TYPE_SKIP)
                .sdkOptions(TruecallerSdkScope.SDK_OPTION_WITH_OTP)
                .termsOfServiceUrl("http://www.truecaller.com")
                .privacyPolicyUrl("http://www.truecaller.com")
                .build();
        TruecallerSDK.init(trueScope);
        initView();
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();


        PendingIntent intent = Credentials.getClient(LoginActivity.this).getHintPickerIntent(hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(), CREDENTIAL_PICKER_REQUEST, null, 0, 0, 0, new Bundle());
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }

        startSmartUserConsent();
    }

    private void initView() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        lnyPhoneNumber = findViewById(R.id.lnyPhoneNumber);
        ccp = findViewById(R.id.ccp);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        txtLogin = findViewById(R.id.txtLogin);
        txtSkipLogin = findViewById(R.id.txtSkipLogin);
        image=findViewById(R.id.image);


        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);
        txtVerify = findViewById(R.id.txtVerify);
        txt_resend = findViewById(R.id.txt_resend);


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
        ccp.setCountryForPhoneCode(+91);
        ccp.hideNameCode(true);

        clickEvent();
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == RESULT_OK) {

            Credential credentials = data.getParcelableExtra(Credential.EXTRA_KEY);

            edtPhoneNumber.setText(credentials.getId().substring(3));

        } else if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == CredentialsApi.ACTIVITY_RESULT_NO_HINTS_AVAILABLE) {
            Toast.makeText(this, "No phone number found", Toast.LENGTH_SHORT).show();
        }

        if (requestCode == REQUEST_USER_CONSENT) {
            if (resultCode == RESULT_OK && data != null) {
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                getOTP(message);

            }
        }

        if (requestCode == TruecallerSDK.SHARE_PROFILE_REQUEST_CODE) {
            TruecallerSDK.getInstance().onActivityResultObtained(LoginActivity.this, requestCode, resultCode, data);
            Log.i("onActivityResult","working");
            Log.i("RequestCode",String.valueOf(requestCode));
            Log.i("ResultCode",String.valueOf(resultCode));
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
            }

        }
    }

    private void clickEvent() {
        txtLogin.setOnClickListener(this);
        txtSkipLogin.setOnClickListener(this);
        txtVerify.setOnClickListener(this);
        login_with_truecaller.setOnClickListener(this);
        txt_resend.setOnClickListener(this);
        edtPhoneNumber.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SharedPrefs.getValue(SharedPrefs.SKIP_LOGIN).equals("yes")) {
            txtSkipLogin.setVisibility(View.GONE);
        }
        txtLogin.setClickable(true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtLogin:
                if (valid()) {
                    sendOtpAPI();
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    progressBar.setVisibility(View.VISIBLE);

                }
                break;

            case R.id.txtSkipLogin:
                SharedPrefs.setValue(SharedPrefs.SKIP_LOGIN, "yes");
                SharedPrefs.setValue(SharedPrefs.User_Name, "Guest");
                SharedPrefs.setValue(SharedPrefs.Email, "");
                DashboardActivity();
                break;

            case R.id.txtVerify:
                if (valid()) {
                    verifyOTP();
                    progressDoalog = new ProgressDialog(LoginActivity.this);
                    progressDoalog.setMax(100);
                    progressDoalog.setMessage("Please wait....");
                    progressDoalog.setTitle("Logging in");
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
                break;

            case R.id.txt_resend:
                sendOtp();
                txt_resend.setEnabled(false);
                txt_resend.setVisibility(View.INVISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        txt_resend.setVisibility(View.VISIBLE);
                        txt_resend.setEnabled(true);
                        Toast.makeText(LoginActivity.this, "You can resend OTP again", Toast.LENGTH_SHORT).show();
                    }
                },5000);
                Toast.makeText(this, "You can resend OTP again after 30 seconds", Toast.LENGTH_SHORT).show();

                break;

            case R.id.login_with_truecaller:
                if (TruecallerSDK.getInstance().isUsable()) {
                    TruecallerSDK.getInstance().getUserProfile(LoginActivity.this);
                   // Log.i("checking state","working");
                } else {
                    androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(LoginActivity.this);
                    dialogBuilder.setMessage("Truecaller App not installed.");

                    dialogBuilder.setPositiveButton("OK", (dialog, which) -> {
                                Log.e("TAG", "onClick: Closing dialog");
                                dialog.dismiss();
                            }
                    );

                    dialogBuilder.setIcon(R.drawable.com_truecaller_icon);
                    dialogBuilder.setTitle(" ");

                    AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();
                }
                break;
            case R.id.edtPhoneNumber:
                image.setAlpha(0.4f);
                break;
        }
    }

    private void sendOtp() {
        Call<JsonObject> call = api.login("+" + ccp.getSelectedCountryCode(), edtPhoneNumber.getText().toString());
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
                        progressDoalog.dismiss();
                        DashboardActivity();

                    } else {
                        Toast.makeText(LoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

    private boolean valid() {
        boolean isValid = true;
        if (TextUtils.isEmpty(edtPhoneNumber.getText().toString())) {
            isValid = false;
            edtPhoneNumber.setError("This field is required");
        }

        if (edtPhoneNumber.getText().toString().length() < 8 || edtPhoneNumber.getText().toString().length() > 13) {
            isValid = false;
            edtPhoneNumber.setError("Please enter valid phone number");
        }
        return isValid;
    }

    private void sendOtpAPI() {
        txtLogin.setClickable(false);
        Call<JsonObject> call = api.login("+" + ccp.getSelectedCountryCode(), edtPhoneNumber.getText().toString());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {

                    Log.e("Login==>", "#####" + response.body());
                    JSONObject jsonObject = new JSONObject(response.body().toString());

                    if (jsonObject.getBoolean("status")) {
                        if (jsonObject.getString("user_type").equals("old_user")) {
                            SharedPrefs.setValue(SharedPrefs.Phone_No, edtPhoneNumber.getText().toString());
                            SharedPrefs.setValue(SharedPrefs.Country_Code, ccp.getSelectedCountryCode());
                            user_id = jsonObject.getInt("user_id");

                            //  otpActivity(jsonObject.getInt("user_id"), ccp.getSelectedCountryCode(), edtPhoneNumber.getText().toString(),jsonObject.getString("user_type"));

                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            progressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        } else {
                            SharedPrefs.setValue(SharedPrefs.Phone_No, edtPhoneNumber.getText().toString());
                            SharedPrefs.setValue(SharedPrefs.Country_Code, ccp.getSelectedCountryCode());
                            SignUpActivity(jsonObject.getInt("user_id"), edtPhoneNumber.getText().toString(), ccp.getSelectedCountryCode());
                        }
                    } else {
                        WelcomeScreenPrefs.clearData();
                        Toast.makeText(LoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                txtLogin.setClickable(true);
                t.printStackTrace();
            }
        });
    }

    private void DashboardActivity() {
        Intent it = new Intent(LoginActivity.this, NewHomeActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(it);
        finish();
    }

    private void SignUpActivity(Integer user_id, String phone, String countryCode) {
        Intent it = new Intent(LoginActivity.this, SingUpActivity.class);
        it.putExtra("user_id", user_id);
        it.putExtra("countrycode", countryCode);
        it.putExtra("phone", phone);
        startActivity(it);
        finish();
    }

    private void otpActivity(int user_id, String countrycode, String phoneNo, String user_type) {
        Intent it = new Intent(LoginActivity.this, OtpActivity.class);
        it.putExtra("user_id", user_id);
        it.putExtra("countrycode", countrycode);
        it.putExtra("phoneNo", phoneNo);
        it.putExtra("user_type", user_type);
        startActivity(it);
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
        image.setAlpha(0.9f);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        image.setAlpha( 0.9f);
        finish();
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
}
