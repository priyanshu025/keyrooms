package com.keyroom.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.keyroom.DateHelper.Date.DatePickerDialog;
import com.keyroom.Network.API;
import com.keyroom.Network.ConfigHader;
import com.keyroom.R;
import com.keyroom.Utility.Content;
import com.keyroom.Utility.SharedPrefs;

import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_IMAGE = 100;
    private final int[] items = new int[]{R.string.camera, R.string.gallery};
    private final int[] icons = new int[]{R.drawable.ic_camera_chat, R.drawable.ic_photo_gallery};
    ImageView imgClose;
    TextView txtSave;
    EditText edtEmail;
    EditText edtFirstName;
    EditText edtLastName;
    EditText edtPhoneNo;
    CircleImageView edtCityName;
    RadioGroup radioGroup;
    String date, passDate = "";
    String passData = "";
    String month, day;
    API api;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = ConfigHader.getClient().create(API.class);
        setContentView(R.layout.activity_editprofile);
        initView();
    }

    private void initView() {
        imgClose = findViewById(R.id.imgClose);
        txtSave = findViewById(R.id.txtSave);
        edtEmail = findViewById(R.id.edtEmail);
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtPhoneNo = findViewById(R.id.edtPhoneNo);
        edtCityName = findViewById(R.id.edtCityName);
        radioGroup = findViewById(R.id.radioGroup);

        clickEvent();

    }


    private void clickEvent() {
        imgClose.setOnClickListener(this);
        txtSave.setOnClickListener(this);
        edtCityName.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgClose:
                super.onBackPressed();
                break;


            case R.id.txtSave:
                if (valid()) {
                    saveUserData();
                }
                break;
            case R.id.edtCityName:
                checkPermissionExist();
                break;
        }

    }


    private void checkPermissionExist() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            openBottomSheet();
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();
    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 100);
    }

    private void openBottomSheet() {
        BottomSheet.Builder builder = new BottomSheet.Builder(EditProfileActivity.this);
        builder.setItems(items, icons, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        launchCameraIntent();
                        break;
                    case 1:
                        launchGalleryIntent();
                        break;
                }
            }
        });
        builder.show();
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(EditProfileActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 512);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 512);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(EditProfileActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE:
                    selectedImageUri = imageReturnedIntent.getParcelableExtra("path");

                    //edtCityName.setText(selectedImageUri.toString());
                    edtCityName.setImageBitmap(BitmapFactory.decodeFile(selectedImageUri.getPath()));

                    uploadToServer(selectedImageUri.getPath());
                    break;
            }
        }
    }


    private void cityNameActivity() {
        Intent it = new Intent(EditProfileActivity.this, CityNameActivity.class);
        startActivity(it);
    }

    private void setStartDate() {
        Calendar now = Calendar.getInstance();
        if (!SharedPrefs.getValue(SharedPrefs.Birthday_Date_Display).equals("0")) {
            StringTokenizer tokens = new StringTokenizer(SharedPrefs.getValue(SharedPrefs.Birthday_Date_Display), "-");
            String year = tokens.nextToken();
            String month_pass = tokens.nextToken();
            String day = tokens.nextToken();
            now.set(Calendar.YEAR, Integer.valueOf(year));
            now.set(Calendar.MONTH, Integer.valueOf(month_pass) - 1);
            now.set(Calendar.DAY_OF_MONTH, Integer.valueOf(day));
        }


        DatePickerDialog dpd = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                                                                @Override
                                                                public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                                                    date = dayOfMonth + " " + (++monthOfYear) + "," + year;
                                                                    Calendar cal = Calendar.getInstance();
                                                                    cal.set(Calendar.YEAR, year);
                                                                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                                                    cal.set(Calendar.MONTH, monthOfYear - 1);
                                                                    passData = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
                                                                    if (String.valueOf(monthOfYear).length() == 1) {
                                                                        month = "0" + monthOfYear;
                                                                    } else {
                                                                        month = "" + monthOfYear;
                                                                    }
                                                                    if (String.valueOf(dayOfMonth).length() == 1) {
                                                                        day = "0" + dayOfMonth;
                                                                    } else {
                                                                        day = "" + dayOfMonth;
                                                                    }
                                                                    passDate = year + "-" + month + "-" + day;
                                                                }
                                                            },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.dismissOnPause(true);
        dpd.setThemeDark(false);
        Content.ShowDay = true;
        Content.ShowMonth = true;
        dpd.showYearPickerFirst(false);
        dpd.setMaxDate(Calendar.getInstance());
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    private boolean valid() {
        boolean isValid = true;
        if (TextUtils.isEmpty(edtEmail.getText().toString())) {
            isValid = false;
            edtEmail.setError("This field is required");
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches()) {
            isValid = false;
            edtEmail.setError("Please Enter valid Email");
        }
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

    private void saveUserData() {
        Call<JsonObject> call = api.profile(SharedPrefs.getInt(SharedPrefs.User_id), edtFirstName.getText().toString(), edtLastName.getText().toString(), edtEmail.getText().toString(), 189, "", "", "", "", "", "", "");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getBoolean("status")) {
                        SharedPrefs.setValue(SharedPrefs.Email, edtEmail.getText().toString());
                        SharedPrefs.setValue(SharedPrefs.First_Name, edtFirstName.getText().toString());
                        SharedPrefs.setValue(SharedPrefs.Last_Name, edtLastName.getText().toString());
                        Toast.makeText(EditProfileActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        if (jsonObject.getString("messages").equals("Token is Expired")) {
                            logoutUser();
                        } else {
                            Toast.makeText(EditProfileActivity.this, jsonObject.getString("messages"), Toast.LENGTH_SHORT).show();
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
        Intent it = new Intent(EditProfileActivity.this, LoginActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(it);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserData();
    }

    private void getUserData() {
        edtFirstName.setText(SharedPrefs.getValue(SharedPrefs.First_Name));
        edtLastName.setText(SharedPrefs.getValue(SharedPrefs.Last_Name));
        edtEmail.setText(SharedPrefs.getValue(SharedPrefs.Email));
        edtPhoneNo.setText("+" + SharedPrefs.getValue(SharedPrefs.Country_Code) + " " + SharedPrefs.getValue(SharedPrefs.Phone_No));
    }


    private void uploadToServer(String filePath) {
        File file = new File(filePath);
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), fileReqBody);
        RequestBody description = RequestBody.create(MediaType.parse("user_id"), String.valueOf(SharedPrefs.getInt(SharedPrefs.User_id)));
        Call call = api.uploadImage(part, description);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
