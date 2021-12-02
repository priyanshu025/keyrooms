package com.keyroom.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.keyroom.Adapter.SearchResultAdapter;
import com.keyroom.InterFace.AddWishListHotel;
import com.keyroom.InterFace.OnLoadMoreListener;
import com.keyroom.InterFace.RemoveWishListHotel;
import com.keyroom.Model.SearchModel;
import com.keyroom.Network.API;
import com.keyroom.Network.Config;
import com.keyroom.Network.ConfigHader;
import com.keyroom.R;
import com.keyroom.Utility.SharedPrefs;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeeAllNearByHotelActivity extends AppCompatActivity implements AddWishListHotel, RemoveWishListHotel, View.OnClickListener, OnLoadMoreListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult> {
    RecyclerView rcyNearHotelList;
    ImageView imgBack;
    SearchResultAdapter searchResultAdapter;
    API api;
    API apiHearde;
    ArrayList<SearchModel.Rows> serachHotelModel = new ArrayList<>();
    Handler handler = new Handler();
    int totalRecode = 0, page_no = 1;
    TextView cityName;

    Double lati,longi;

    public static final int REQUEST_CHECK_SETTING = 101;
    public static final int REQUEST_CHECK_SETTINGS_ADOPT = 2472;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    private GoogleApiClient mGoogleApiClient;

    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationCodexRequest;
    private LocationRequest locationRequest;
    private LocationSettingsRequest mLocationCodexSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private ShimmerFrameLayout shimmer_hotel_card_dummy;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = Config.getClient().create(API.class);
        apiHearde = ConfigHader.getClient().create(API.class);
        setContentView(R.layout.activity_seeallnearbyhotel);
        intiView();
    }

    private void intiView() {
        rcyNearHotelList = findViewById(R.id.rcyNearHotelList);
        imgBack = findViewById(R.id.imgBack);
        shimmer_hotel_card_dummy = findViewById(R.id.shimmer_hotel_card_dummy);
        shimmer_hotel_card_dummy.startShimmerAnimation();

        rcyNearHotelList.setLayoutManager(new LinearLayoutManager(SeeAllNearByHotelActivity.this, LinearLayoutManager.VERTICAL, false));
        clickEvent();
    }

    private void clickEvent() {
        imgBack.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        page_no = 1;
        if (serachHotelModel != null)
            serachHotelModel.clear();
        searchResultAdapter = new SearchResultAdapter(SeeAllNearByHotelActivity.this, serachHotelModel, SeeAllNearByHotelActivity.this::AddWishListHotel, SeeAllNearByHotelActivity.this::RemoveWishListHotel, rcyNearHotelList);
        rcyNearHotelList.setAdapter(searchResultAdapter);
        shimmer_hotel_card_dummy.startShimmerAnimation();
        shimmer_hotel_card_dummy.setVisibility(View.VISIBLE);
        rcyNearHotelList.setVisibility(View.GONE);
        locationPermissionAdoption();
        buildGoogleApiClient();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            initGPS();
        }
        if (requestCode == REQUEST_CHECK_SETTING) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Toast.makeText(SeeAllNearByHotelActivity.this, "GPS is Turn On", Toast.LENGTH_SHORT).show();
                    break;

                case Activity.RESULT_CANCELED:
                    Toast.makeText(SeeAllNearByHotelActivity.this, "GPS location is required to be Turn on", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .build();
    }

    private void locationPermissionAdoption() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            initGPS();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
    private void initGPS() {
        this.runOnUiThread(new Runnable() {
            @SuppressLint("MissingPermission")
            @Override
            public void run() {
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(SeeAllNearByHotelActivity.this);
                mSettingsClient = LocationServices.getSettingsClient(SeeAllNearByHotelActivity.this);

                mLocationCodexRequest = LocationRequest.create();
                mLocationCodexRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
                mLocationCodexRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
                mLocationCodexRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
                builder.addLocationRequest(mLocationCodexRequest);
                builder.setAlwaysShow(true);
                Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(SeeAllNearByHotelActivity.this)
                        .checkLocationSettings(builder.build());

                result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                        try {
                            LocationSettingsResponse response = task.getResult(ApiException.class);
                        } catch (ApiException e) {
                            switch (e.getStatusCode()) {
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                                    try {
                                        ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                        resolvableApiException.startResolutionForResult(SeeAllNearByHotelActivity.this, REQUEST_CHECK_SETTING);
                                    } catch (IntentSender.SendIntentException sendIntentException) {
                                        sendIntentException.printStackTrace();
                                    }
                                    break;

                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    break;
                            }
                        }


                    }
                });
                mLocationCodexSettingsRequest = builder.build();

                SettingsClient settingsClient = LocationServices.getSettingsClient(SeeAllNearByHotelActivity.this);
                settingsClient.checkLocationSettings(mLocationCodexSettingsRequest);

                mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            onLocationChanged(location);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });

    }

    public void onLocationChanged(Location location) {
        SharedPrefs.setDouble(SharedPrefs.lat, location.getLatitude());
        SharedPrefs.setDouble(SharedPrefs.longi, location.getLongitude());

         lati=location.getLatitude();
         longi=location.getLongitude();

        nearbyHotel(location.getLatitude(),location.getLongitude());
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        onLocationChanged(location);
                    }
                }
            }
        };
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(SeeAllNearByHotelActivity.this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }



    private void nearbyHotel(Double lat, Double lon) {
        Log.e("Lat==>", "@@" + lat);
        Log.e("longi==>", "@@" + lon);
        Log.e("User_id==>", "@@" + SharedPrefs.getInt(SharedPrefs.User_id));

        serachHotelModel.clear();
        Call<JsonObject> call = api.nearbyHotel(lat,lon, SharedPrefs.getInt(SharedPrefs.User_id));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    Log.e("URL==>", "@@" + response.raw().request().url());

                    searchResultAdapter.setLoaded();
                    SearchModel searchModel = new Gson().fromJson(response.body(), SearchModel.class);
                    totalRecode = searchModel.getTotal_records();
                    serachHotelModel.addAll(searchModel.getRows());
                    if (serachHotelModel.size() != 0) {
                        searchResultAdapter.notifyDataSetChanged();
                        shimmer_hotel_card_dummy.stopShimmerAnimation();
                        shimmer_hotel_card_dummy.setVisibility(View.GONE);
                        rcyNearHotelList.setVisibility(View.VISIBLE);

                    } else {
                        shimmer_hotel_card_dummy.stopShimmerAnimation();
                        shimmer_hotel_card_dummy.setVisibility(View.GONE);
                        rcyNearHotelList.setVisibility(View.VISIBLE);
                        Toast.makeText(SeeAllNearByHotelActivity.this, "No hotel found near you", Toast.LENGTH_SHORT).show();
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


    @Override
    public void AddWishListHotel(int hotelID) {
        addWishlist(hotelID);
    }

    @Override
    public void RemoveWishListHotel(Integer hotel_id) {
        removeWishList(hotel_id);
    }

    private void addWishlist(int hotelID) {
        Call<JsonObject> call = apiHearde.addWhishlist(hotelID, "hotel");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getBoolean("status")) {
                        Toast.makeText(SeeAllNearByHotelActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        if (jsonObject.getString("messages").equals("Token is Expired")) {
                            logoutUser();
                        } else {
                            Toast.makeText(SeeAllNearByHotelActivity.this, jsonObject.getString("messages"), Toast.LENGTH_SHORT).show();
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

    private void removeWishList(Integer hotelID) {
        Call<JsonObject> call = apiHearde.removeWishList(SharedPrefs.getInt(SharedPrefs.User_id), hotelID);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getBoolean("status")) {
                        Toast.makeText(SeeAllNearByHotelActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        if (jsonObject.getString("messages").equals("Token is Expired")) {
                            logoutUser();
                        } else {
                            Toast.makeText(SeeAllNearByHotelActivity.this, jsonObject.getString("messages"), Toast.LENGTH_SHORT).show();
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
        Intent it = new Intent(SeeAllNearByHotelActivity.this, LoginActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(it);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                super.onBackPressed();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onLoadMore() {
        if (totalRecode > serachHotelModel.size()) {
            final Runnable r = new Runnable() {
                public void run() {
                    ++page_no;
                    nearbyHotel(lati,longi);
                }
            };
            handler.post(r);
        }
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {

    }

    @Override
    public void onConnected(@Nullable  Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull  ConnectionResult connectionResult) {

    }
}
