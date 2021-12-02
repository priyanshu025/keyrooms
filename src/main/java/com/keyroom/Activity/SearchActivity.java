package com.keyroom.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.keyroom.Adapter.CustomAdapter;
import com.keyroom.Adapter.SearchCityAdapter;
import com.keyroom.Database.DatabaseHelper;
import com.keyroom.Fragment.HomeFragment;
import com.keyroom.InterFace.NavegationClick;
import com.keyroom.Model.Data;
import com.keyroom.Model.GlobalSearchModel;
import com.keyroom.Model.LocationCityModel;
import com.keyroom.Model.LocationModel;
import com.keyroom.Network.API;
import com.keyroom.Network.Config;
import com.keyroom.R;
import com.keyroom.SearchCityRecyclerInterface;
import com.keyroom.Utility.SharedPrefs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements SearchCityRecyclerInterface, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult>, NavegationClick  {

    AutoCompleteTextView edtSearch;
    LinearLayout lnySerchNearBy;
    RecyclerView rcyCityList;
    ImageView image_back;
    ArrayList searchList=new ArrayList();
    CustomAdapter arrayAdapter;
    DatabaseHelper databaseHelper;
    boolean islocation=false;
    TextView recent_search_txt;


    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationCodexRequest;
    private LocationSettingsRequest mLocationCodexSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    public static final int REQUEST_CHECK_SETTINGS_ADOPT = 2472;

    API api;
    ArrayList<LocationCityModel> cityListName = new ArrayList<>();
    SearchCityAdapter searchCityAdapter;
    String[] array;
    ArrayList<Data> globleSearchList = new ArrayList<>();

    LinearLayout lnyCheckInDate,lnyCheckOutDate,lnyGuestAndRoom;
    TextView txtToday,txtTomorrow,txtRoom,txtAdult;

    private ShimmerFrameLayout shimmer_hotel_card_dummy;
    RecyclerView rcy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = Config.getClient().create(API.class);
        setContentView(R.layout.activity_serach);
        initView();
    }

    private void initView() {
        edtSearch = findViewById(R.id.edtSearch);
        lnySerchNearBy = findViewById(R.id.lnySerchNearBy);
        image_back=findViewById(R.id.image_back);
        rcyCityList = findViewById(R.id.rcyCityList);
        rcy=findViewById(R.id.rcy);
        lnyCheckInDate = findViewById(R.id.lnyCheckInDate);
        lnyCheckOutDate = findViewById(R.id.lnyCheckOutDate);
        lnyGuestAndRoom = findViewById(R.id.lnyGuestAndRoom);
        txtToday = findViewById(R.id.txtToday);
        txtTomorrow = findViewById(R.id.txtTomorrow);
        txtRoom = findViewById(R.id.txtRoom);
        txtAdult = findViewById(R.id.txtAdult);
        shimmer_hotel_card_dummy = findViewById(R.id.shimmer_hotel_card_dummy);
        shimmer_hotel_card_dummy.startShimmerAnimation();
        recent_search_txt=findViewById(R.id.recent_search_txt);





        rcy.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));

        rcyCityList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        searchCityAdapter = new SearchCityAdapter(SearchActivity.this, cityListName,this);
        rcyCityList.setAdapter(searchCityAdapter);

        databaseHelper=new DatabaseHelper(SearchActivity.this);
        searchList.clear();
        searchList=databaseHelper.getAllText();
        //arrayAdapter=new ArrayAdapter(SearchActivity.this, android.R.layout.simple_list_item_1,searchList);
        arrayAdapter=new CustomAdapter(SearchActivity.this,searchList,this);
        rcy.setAdapter(arrayAdapter);
        clickEvent();
        SetDate();

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e("beforeTextChanged==>", "@@" + s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("onTextChanged==>", "@@" + s.toString());
                for (int i = 0; i < globleSearchList.size(); i++) {
                    if(globleSearchList.get(i).getTitle().equals(s.toString())){
                        if(globleSearchList.get(i).getFlage().equals("city")){
                            Intent it = new Intent(SearchActivity.this, SearchResultActivity.class);
                            it.putExtra("cityName", s.toString());
                            if (!searchList.contains(globleSearchList.get(i).getTitle())) {
                                databaseHelper.addText(globleSearchList.get(i).getTitle());
                            }
                            searchList.clear();
                            searchList.addAll(databaseHelper.getAllText());
                            arrayAdapter.notifyDataSetChanged();
                            startActivity(it);
                            break;

                        }else {
                            Intent it = new Intent(SearchActivity.this, HotelDetailsActivity.class);
                            it.putExtra("slug", globleSearchList.get(i).getSlug());
                            if (!searchList.contains(globleSearchList.get(i).getSlug())) {
                                databaseHelper.addText(globleSearchList.get(i).getSlug());
                            }
                            searchList.clear();
                            searchList.addAll(databaseHelper.getAllText());
                            arrayAdapter.notifyDataSetChanged();
                            startActivity(it);
                            break;

                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    Log.e("afterTextChanged==>", "@@" + s.toString());
                    globleSearch(s.toString());
                }
            }
        });

    }

    private void SetDate(){

        try {
            txtToday.setText(HomeFragment.formatDateFromDateString("yyyy-MM-dd","dd-MMM-yyyy",ChekInCheckOutActivity.strCheckInDate));
            txtTomorrow.setText(HomeFragment.formatDateFromDateString("yyyy-MM-dd","dd-MMM-yyyy",ChekInCheckOutActivity.strCheckOutDate));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (ChekInCheckOutActivity.totalAdult == 1) {
            txtRoom.setText(ChekInCheckOutActivity.room + " Room");
            txtAdult.setText(ChekInCheckOutActivity.totalAdult + " Guest");
        }
        else {
            txtRoom.setText(ChekInCheckOutActivity.room + " Room");
            txtAdult.setText(ChekInCheckOutActivity.totalAdult + " Guest");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SetDate();
        globleSearchList.clear();
        edtSearch.setText("");
        locationAPI();
        if (searchList.size()>0){
            recent_search_txt.setVisibility(View.VISIBLE);
        }
        islocation=false;
        shimmer_hotel_card_dummy.startShimmerAnimation();
        shimmer_hotel_card_dummy.setVisibility(View.VISIBLE);
        rcyCityList.setVisibility(View.GONE);
    }

    private void globleSearch(String search) {
        globleSearchList.clear();
        Call<JsonObject> call = api.globalSearch(search);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    GlobalSearchModel globalSearchModel = new Gson().fromJson(response.body(), GlobalSearchModel.class);
                    if (globalSearchModel.getStatus() == 1) {
                        globleSearchList.addAll(globalSearchModel.getData());
                        if (globleSearchList.size() != 0) {
                            array = new String[globleSearchList.size()];
                            for (int i = 0; i < globleSearchList.size(); i++) {
                                array[i] = globleSearchList.get(i).getTitle();
                            }
                            ArrayAdapter orderTypeAdapter = new ArrayAdapter(SearchActivity.this,android.R.layout.simple_list_item_1, array);
                            edtSearch.setAdapter(orderTypeAdapter);
                            edtSearch.showDropDown();
                            edtSearch.setThreshold(0);
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

    private void locationAPI() {
        cityListName.clear();

        Call<JsonObject> call = api.locations();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                LocationModel locationModel = new Gson().fromJson(response.body(), LocationModel.class);
                cityListName.addAll(locationModel.getLocations());
                if (cityListName.size() != 0) {
                    searchCityAdapter.notifyDataSetChanged();
                    shimmer_hotel_card_dummy.stopShimmerAnimation();
                    shimmer_hotel_card_dummy.setVisibility(View.GONE);
                    rcyCityList.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                t.printStackTrace();
            }
        });
    }


    private void clickEvent() {
        lnySerchNearBy.setOnClickListener(this);
        image_back.setOnClickListener(this);
        lnyCheckInDate.setOnClickListener(this);
        lnyCheckOutDate.setOnClickListener(this);
        lnyGuestAndRoom.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.lnySerchNearBy:
             seeAllNearByHotel();
                //getCurrentLocation();
                break;

            case R.id.image_back:
                finish();
                break;
            case R.id.lnyCheckInDate:
                checkINCheckOutActivity(1);
                break;
            case R.id.lnyCheckOutDate:
                checkINCheckOutActivity(2);
                break;
            case R.id.lnyGuestAndRoom:
                checkINCheckOutActivity(3);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            initGPS();
        }
    }

    private void getCurrentLocation() {
        buildGoogleApiClient();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(SearchActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .build();
        locationPermissionAdoption();
    }
    private void seeAllNearByHotel() {
        Intent it = new Intent(SearchActivity.this, SeeAllNearByHotelActivity.class);
        startActivity(it);
    }

    private void locationPermissionAdoption() {
        Dexter.withActivity(SearchActivity.this)
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
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
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
        Uri uri = Uri.fromParts("package", SearchActivity.this.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 100);
    }


    private void initGPS() {
        runOnUiThread(new Runnable() {
            @SuppressLint("MissingPermission")
            @Override
            public void run() {
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(SearchActivity.this);
                mSettingsClient = LocationServices.getSettingsClient(SearchActivity.this);

                mLocationCodexRequest = new LocationRequest();
                mLocationCodexRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
                mLocationCodexRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
                mLocationCodexRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
                builder.addLocationRequest(mLocationCodexRequest);
                mLocationCodexSettingsRequest = builder.build();

                SettingsClient settingsClient = LocationServices.getSettingsClient(SearchActivity.this);
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
        getCityName(SharedPrefs.getDouble(SharedPrefs.lat), SharedPrefs.getDouble(SharedPrefs.longi));

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
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(SearchActivity.this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }


    private void getCityName(double MyLat, double MyLong) {
        try {
            Geocoder geocoder = new Geocoder(SearchActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(MyLat, MyLong, 1);
            String cityName = addresses.get(0).getLocality();
            Intent it = new Intent(SearchActivity.this, SearchResultActivity.class);
            it.putExtra("cityName", cityName);
            startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public void onItemClicked(int position) {
        LocationCityModel locationCityModel = cityListName.get(position);
        Intent intent=new Intent();
        intent.putExtra("hotel", locationCityModel.getName());
        setResult(RESULT_OK,intent);
        finish();

    }

    private void checkINCheckOutActivity(int type) {
        Intent it = new Intent(getApplicationContext(), ChekInCheckOutActivity.class);
        it.putExtra("type", type);
        startActivityForResult(it, 2);
    }

    @Override
    public void NavegationClick(int position, String name) {
        for (int i=0;i<cityListName.size();i++){
                if (cityListName.get(i).getName().toLowerCase().trim().equals(name.toLowerCase().trim())) {
                    islocation = true;
                    break;
                }
        }

        if (islocation){
            Intent it = new Intent(SearchActivity.this, SearchResultActivity.class);
            it.putExtra("cityName",name);
            startActivity(it);
        }
        else {
            Intent it = new Intent(SearchActivity.this, HotelDetailsActivity.class);
            it.putExtra("slug",name);
            startActivity(it);
        }
    }

}
