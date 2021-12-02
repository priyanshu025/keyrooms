package com.keyroom.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.viewpager.widget.ViewPager;

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
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.keyroom.Adapter.Adapter;
import com.keyroom.Adapter.BannerDetailsAdapter;
import com.keyroom.Adapter.FacilitesHotelDetailsAdapter;
import com.keyroom.Adapter.GalleryDetailsAdapter;
import com.keyroom.Adapter.NearHotelAdapter;
import com.keyroom.Adapter.OfferAdapter;
import com.keyroom.Adapter.PolicyAdapter;
import com.keyroom.Adapter.ReviewHotelAdapter;
import com.keyroom.Adapter.RoomDetailAdapter;
import com.keyroom.Adapter.RoomDetailWithAvaiblityAdapter;
import com.keyroom.Adapter.SearchResultAdapter;
import com.keyroom.Fragment.HomeFragment;
import com.keyroom.InterFace.AddWishListHotel;
import com.keyroom.InterFace.RemoveWishListHotel;
import com.keyroom.Model.BannerModel;
import com.keyroom.Model.CheckAvabilityModel;
import com.keyroom.Model.HotelDetailsModel;
import com.keyroom.Model.NearByHotelModel;
import com.keyroom.Network.API;
import com.keyroom.Network.Config;
import com.keyroom.Network.ConfigHader;
import com.keyroom.R;
import com.keyroom.Utility.SharedPrefs;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelDetailsActivity extends AppCompatActivity implements View.OnClickListener, RoomDetailWithAvaiblityAdapter.RoomDetailCount,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult>, AddWishListHotel, RemoveWishListHotel {

    ImageView imgBack;
    TextView txtHotelTitle;
    ImageView imgShare;
    RecyclerView rcyGallery;
    TextView txthotelName;
    TextView txtHotelAddress;
    TextView txtHotelRate;


    TextView txtGuestAndRoom;
    TextView txtCheckIn;
    TextView txtAmount;
    TextView salePrice;
    RecyclerView facilitiesRecyclerView;
    TextView txtPrice;
    Button txtBookNow;
    TextView txtCheckOut;
    TextView txtDesc;
    ImageView imgUnWish;
    ImageView imgWish;
    TextView txtTotalReview;

    LinearLayout llGuest,llcheckIn,llcheckOut;

    String slug;
    API api;
    API apiHearde;
    Integer hotelID;
    int roomSize = 0;
    double map_lat;
    double map_lng;
    TextView LocateToMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    private final static int REQUEST_CODE = 101;
    Double userCurrentLatitude;
    Double userCurrentLongitude;


    int currentPage = 0;
    Timer timer,timer1;
    TimerTask timerTask,timerTask1;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.

    int count = 0;
    int position;

   // ProgressBar progressBar;
    GalleryDetailsAdapter galleryDetailsAdapter;
    ReviewHotelAdapter reviewHotelAdapter;
    BannerDetailsAdapter bannerDetailsAdapter;
    PolicyAdapter policyAdapter;
    FacilitesHotelDetailsAdapter facilitesHotelDetailsAdapter;
    RecyclerView rcyRoomDetail;
    LinearLayout llRoomDetailView;
    ConstraintLayout constraintLayout;
    RecyclerView policyRecyelerView;
    RecyclerView nearByHotelRecycler;
    ViewPager viewPagerimage;

    ArrayList<HotelDetailsModel.Gallery> galleryArrayList = new ArrayList<>();
    ArrayList<HotelDetailsModel.BannerImage> bannerImageArrayList = new ArrayList<>();
    ArrayList<HotelDetailsModel.Policy> policyArrayList = new ArrayList<>();
    ArrayList<HotelDetailsModel.Facilities> facilities = new ArrayList<>();
    ArrayList<HotelDetailsModel.Attributes> attributes = new ArrayList<>();
    ArrayList<NearByHotelModel.Rows> nearByHotelList = new ArrayList<>();
    ArrayList<HotelDetailsModel.ReviewList> reviewListArrayList=new ArrayList<>();

    HotelDetailsModel hotelDetailsModel;
    String selectPrice = "0";
    JsonArray arryJson = new JsonArray();
    JsonArray arrayID = new JsonArray();
    RoomDetailAdapter roomDetailAdapter;
    RoomDetailWithAvaiblityAdapter roomDetailWithAvaiblityAdapter;
    public ArrayList<CheckAvabilityModel.Rooms> arrayListRoom = new ArrayList<>();
    int totalAmt = 0;
    NearHotelAdapter nearHotelAdapter;
    RecyclerView review_recycler;
    LinearLayoutManager layoutManager;




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


    private Uri dynamicLink = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = Config.getClient().create(API.class);
        apiHearde = ConfigHader.getClient().create(API.class);
        setContentView(R.layout.activity_hoteldetails);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        initView();


        if (getIntent().getStringExtra("slug") !=null) {
            slug = getIntent().getStringExtra("slug");
        }

        // open by share link
        Uri uri=getIntent().getData();
        if (uri!=null){
            List<String> params=uri.getPathSegments();
            String id=params.get(params.size()-1);
            slug = id;
        }
        // open by banner
        Uri link = getIntent().getExtras().getParcelable("link");
        if (link!=null){
            List<String> params=link.getPathSegments();
            String id=params.get(params.size()-1);
            slug = id;
        }



    }

    private void initView() {
        imgBack = findViewById(R.id.imgBack);
        LocateToMap = findViewById(R.id.locate_in_map);
        imgShare = findViewById(R.id.imgShare);

        txtCheckIn=findViewById(R.id.txtCheckInDate);
        facilitiesRecyclerView=findViewById(R.id.facilitiesRecyclerView);
        nearByHotelRecycler=findViewById(R.id.nearByHotelRecycler);
        viewPagerimage=findViewById(R.id.viewPagerHotel);
        policyRecyelerView=findViewById(R.id.policyRecyelerView);
        constraintLayout=findViewById(R.id.constraintLayout);
        rcyGallery = findViewById(R.id.rcyGallery);
        txthotelName = findViewById(R.id.txthotelName);
        txtHotelAddress = findViewById(R.id.txtHotelAddress);
        txtHotelRate = findViewById(R.id.txtHotelRate);
        txtDesc=findViewById(R.id.txtDesc);
        txtCheckOut = findViewById(R.id.txtCheckOut);
        llGuest=findViewById(R.id.llGuest);
        llcheckIn=findViewById(R.id.llcheckIn);
        llcheckOut=findViewById(R.id.llcheckOut);

        txtGuestAndRoom=findViewById(R.id.txtGuestAndRoom);
        txtAmount = findViewById(R.id.txtPrice);
        salePrice=findViewById(R.id.sale_Price);
        txtBookNow = findViewById(R.id.txtBookNow);
        imgUnWish = findViewById(R.id.imgUnWish);
        imgWish = findViewById(R.id.imgWish);
        rcyRoomDetail = findViewById(R.id.rcyRoomDetail);
        txtTotalReview = findViewById(R.id.txtTotalReview);
        review_recycler=findViewById(R.id.review_recycler);





        layoutManager=new LinearLayoutManager(HotelDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);

        rcyGallery.setLayoutManager(layoutManager);
        review_recycler.setLayoutManager(new LinearLayoutManager(HotelDetailsActivity.this,LinearLayoutManager.HORIZONTAL,false));

        nearByHotelRecycler.setLayoutManager(new LinearLayoutManager(HotelDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rcyRoomDetail.setLayoutManager(new LinearLayoutManager(HotelDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
        policyRecyelerView.setLayoutManager(new LinearLayoutManager(HotelDetailsActivity.this, LinearLayoutManager.VERTICAL, false));


        facilitiesRecyclerView.setLayoutManager(new LinearLayoutManager(HotelDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));

        galleryDetailsAdapter = new GalleryDetailsAdapter(HotelDetailsActivity.this, galleryArrayList);
        rcyGallery.setAdapter(galleryDetailsAdapter);

        policyAdapter = new PolicyAdapter(HotelDetailsActivity.this, policyArrayList);
        policyRecyelerView.setAdapter(policyAdapter);

        facilitesHotelDetailsAdapter = new FacilitesHotelDetailsAdapter(HotelDetailsActivity.this, facilities);
        facilitiesRecyclerView.setAdapter(facilitesHotelDetailsAdapter);

        nearHotelAdapter = new NearHotelAdapter(HotelDetailsActivity.this, nearByHotelList, this, this);
        nearByHotelRecycler.setAdapter(nearHotelAdapter);


        rcyGallery.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState==1){
                    stopAutoScrollBanner();
                }else if (newState==0){
                    position=layoutManager.findFirstCompletelyVisibleItemPosition();
                    runAutoScrollBanner();
                }
            }
        });

        clickEvent();
        newImageSlider();
        getNearByHotel();

    }


    private void getNearByHotel() {
        locationPermissionAdoption();
        buildGoogleApiClient();
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
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(HotelDetailsActivity.this);
                mSettingsClient = LocationServices.getSettingsClient(HotelDetailsActivity.this);

                mLocationCodexRequest = LocationRequest.create();
                mLocationCodexRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
                mLocationCodexRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
                mLocationCodexRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
                builder.addLocationRequest(mLocationCodexRequest);
                builder.setAlwaysShow(true);
                Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(HotelDetailsActivity.this)
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
                                        resolvableApiException.startResolutionForResult(HotelDetailsActivity.this, REQUEST_CHECK_SETTING);
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

                SettingsClient settingsClient = LocationServices.getSettingsClient(HotelDetailsActivity.this);
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
        nearbyHotel(SharedPrefs.getDouble(SharedPrefs.lat), SharedPrefs.getDouble(SharedPrefs.longi));
        //stopLocationUpdates();
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
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(HotelDetailsActivity.this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }


    private void getCityName(double MyLat, double MyLong) {
        try {
            Geocoder geocoder = new Geocoder(HotelDetailsActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(MyLat, MyLong, 1);
            String cityName = addresses.get(0).getLocality();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void nearbyHotel(Double lat, Double lon) {
        nearByHotelList.clear();
        Log.e("lat==>", "@@" + lat);
        Log.e("lon==>", "@@" + lon);
        Log.e("User_id==>", "@@" + SharedPrefs.getInt(SharedPrefs.User_id));
        Call<JsonObject> call = api.nearbyHotel(lat, lon, SharedPrefs.getInt(SharedPrefs.User_id));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Log.e("near Hotel==>","@@@@"+response);
                try {

                    NearByHotelModel nearByHotelModel = new Gson().fromJson(response.body(), NearByHotelModel.class);
                    nearByHotelList.addAll(nearByHotelModel.getRows());
                    if (nearByHotelList.size() != 0) {
                        nearHotelAdapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(HotelDetailsActivity.this, "No Hotel Found", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(HotelDetailsActivity.this, "No Hotel Found", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private void clickEvent() {
        imgBack.setOnClickListener(this);
        LocateToMap.setOnClickListener(this);
        imgUnWish.setOnClickListener(this);
        imgWish.setOnClickListener(this);
        txtBookNow.setOnClickListener(this);
        llGuest.setOnClickListener(this);
        llcheckOut.setOnClickListener(this);
        imgShare.setOnClickListener(this);
        llcheckIn.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        count=0;
        getHotelDetils();
        txtBookNow.setClickable(true);
        runAutoScrollBanner();

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAutoScrollBanner();
    }

    private void newImageSlider() {
        ArrayList<BannerModel.Offers> images = new ArrayList<>();
        images.clear();
        OfferAdapter adapter = new OfferAdapter(getApplicationContext(), images);
        Call<JsonObject> call = api.offers();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                BannerModel bannerModel = new Gson().fromJson(response.body(), BannerModel.class);
                images.addAll(bannerModel.getOffers());
                if (images.size() != 0) {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });

        viewPagerimage.setAdapter(adapter);


        /*After setting the adapter use the timer on view pager */
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == images.size()) {
                    currentPage = 0;
                }
                viewPagerimage.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
        viewPagerimage.setClipToPadding(false);
        viewPagerimage.setPadding(30, 0, 30, 0);
    }


    private void getHotelDetils() {
        galleryArrayList.clear();
        bannerImageArrayList.clear();
        policyArrayList.clear();
        facilities.clear();
        attributes.clear();
        reviewListArrayList.clear();

        Log.e("URL==>Details", "@@" + slug);
        Call<JsonObject> call = api.hotelDetails(slug, SharedPrefs.getInt(SharedPrefs.User_id));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    Log.e("hotel details=>>","@@"+response.body());
                    Log.e("URL==>Details", "@@" + response.raw().request().url());
                    hotelDetailsModel = new Gson().fromJson(response.body(), HotelDetailsModel.class);
                    if (hotelDetailsModel.isStatus()) {
////////////////// maps latitude and longitude

                        map_lat = hotelDetailsModel.getRow().getMap_lat();
                        map_lng = hotelDetailsModel.getRow().getMap_lng();

                        Log.e("Map==>Details", "@@@@" + map_lat + "      "+ map_lng);

////////////////// maps latitude and longitude
                        if (hotelDetailsModel.getRow().getId() != null)
                            hotelID = hotelDetailsModel.getRow().getId();
                        if (hotelDetailsModel.getRow().getTitle() != null) {
                            //txtHotelTitle.setText(hotelDetailsModel.getRow().getTitle());
                            txthotelName.setText(hotelDetailsModel.getRow().getTitle());
                        }
                        if (hotelDetailsModel.getRow().getAddress() != null)
                            txtHotelAddress.setText(hotelDetailsModel.getRow().getAddress());
                        if (hotelDetailsModel.getReview_detail().getScore_total() != null)
                            txtHotelRate.setText(hotelDetailsModel.getReview_detail().getScore_total());
                        if (hotelDetailsModel.getReview_detail().getTotal_review() != null)
                            txtTotalReview.setText(hotelDetailsModel.getReview_detail().getTotal_review());
                        if (hotelDetailsModel.getRow().getContent() != null) {

                            String str=hotelDetailsModel.getRow().getContent();
                            str = str.replaceAll("\\<.*?\\>", "");

                            txtDesc.setText(str);
                        }

                        if (hotelDetailsModel.getRow().getPrice() != null) {
                            selectPrice = hotelDetailsModel.getRow().getPrice();
                        }
                        if (hotelDetailsModel.getRow().getSale_price() != null) {
                            constraintLayout.setVisibility(View.VISIBLE);
                            salePrice.setText("\u20B9" + hotelDetailsModel.getRow().getSale_price());

                            Log.e("salePrice==>","@@"+ hotelDetailsModel.getRow().getSale_price());

                        }
                        else {
                            constraintLayout.setVisibility(View.GONE);
                            Log.e("salePrice==>","@@"+ hotelDetailsModel.getRow().getSale_price());
                        }
                        if (hotelDetailsModel.getReview_list()!=null){
                            reviewListArrayList.addAll(hotelDetailsModel.getReview_list());
                        }
                        if (hotelDetailsModel.getRow().getGallery() != null)
                            galleryArrayList.addAll(hotelDetailsModel.getRow().getGallery());

                        if (hotelDetailsModel.getRow().getBannerImage() != null)
                            bannerImageArrayList.addAll(hotelDetailsModel.getRow().getBannerImage());

                        if (hotelDetailsModel.getRow().getAttributes() != null) {
                            for (int i = 0; i < hotelDetailsModel.getRow().getAttributes().size(); i++) {
                                if (hotelDetailsModel.getRow().getAttributes().get(i).getName().equals("Facilities")) {
                                    facilities.addAll(hotelDetailsModel.getRow().getAttributes().get(i).getFacilities());
                                }
                            }
                        }

                        if (galleryArrayList.size() != 0) {

                            if (galleryArrayList!=null){
                                position=Integer.MAX_VALUE/2;
                                rcyGallery.scrollToPosition(position);
                            }

                            rcyGallery.setOnFlingListener(null);
                            SnapHelper snapHelper = new LinearSnapHelper();
                            snapHelper.attachToRecyclerView(rcyGallery);
                            rcyGallery.smoothScrollBy(5,0);



                           /* if (galleryDetailsAdapter.getItemCount() > 1) {
                                final int speedScroll = 2500;
                                final Handler handler = new Handler();
                                final Runnable runnable = new Runnable() {
                                    boolean flag = true;

                                    @Override
                                    public void run() {
                                        if (count < galleryDetailsAdapter.getItemCount()) {
                                            if (count == galleryDetailsAdapter.getItemCount() - 1) {
                                                flag = false;
                                            } else if (count == 0) {
                                                flag = true;
                                            }
                                            if (flag) count++;
                                            else count--;

                                            rcyGallery.smoothScrollToPosition(count);
                                            handler.postDelayed(this, speedScroll);
                                        }
                                        if (count>galleryDetailsAdapter.getItemCount()){
                                            count=0;
                                        }
                                    }
                                };
                                handler.postDelayed(runnable, speedScroll);
                            }*/



                        } else {
                            bannerDetailsAdapter = new BannerDetailsAdapter(HotelDetailsActivity.this, bannerImageArrayList);
                            rcyGallery.setAdapter(bannerDetailsAdapter);
                        }
                        if (reviewListArrayList.size()!=0){
                            reviewHotelAdapter=new ReviewHotelAdapter(HotelDetailsActivity.this,reviewListArrayList);
                            review_recycler.setAdapter(reviewHotelAdapter);

                        }

                        if (facilities.size() != 0) {
                            facilitesHotelDetailsAdapter.notifyDataSetChanged();
                        }

                        if (hotelDetailsModel.getRow().isWishList()) {
                            imgWish.setVisibility(View.VISIBLE);
                            imgUnWish.setVisibility(View.GONE);
                        } else {
                            imgWish.setVisibility(View.GONE);
                            imgUnWish.setVisibility(View.VISIBLE);
                        }

                        if (hotelDetailsModel.getRow().getPolicy() != null)
                            policyArrayList.addAll(hotelDetailsModel.getRow().getPolicy());

                        if (policyArrayList.size() != 0) {
                            policyAdapter.notifyDataSetChanged();
                        }

                        if (!ChekInCheckOutActivity.strCheckInDate.equals("")){
                            checkAvability();
                        }
                        else {
                            getDate();
                            checkAvability();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), hotelDetailsModel.getMessages(), Toast.LENGTH_SHORT).show();
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

    private void stopAutoScrollBanner(){
        if (timer1!=null&&timerTask1!=null){
            timerTask1.cancel();
            timer1.cancel();
            timer1=null;
            timerTask1=null;
            position=layoutManager.findFirstCompletelyVisibleItemPosition();

        }
    }


    private void runAutoScrollBanner(){
        if (timer1==null && timerTask1==null){
            timer1=new Timer();
            timerTask1=new TimerTask() {
                @Override
                public void run() {
                    if (position==Integer.MAX_VALUE){
                        position=Integer.MAX_VALUE/2;

                        rcyGallery.scrollToPosition(position);
                        rcyGallery.smoothScrollBy(5,0);
                    }
                    else {
                        position++;
                        rcyGallery.smoothScrollToPosition(position);
                    }
                }
            };
            timer1.schedule(timerTask1,4000,4000);
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                super.onBackPressed();
                break;
            case R.id.locate_in_map:
                getCurrentLocation();
                break;
            case R.id.txtBookNow:
                if (SharedPrefs.getValue(SharedPrefs.SKIP_LOGIN).equals("yes")) {
                    loginScreen();
                } else
                    callBookNow();
                break;
            case R.id.imgUnWish:
                if (SharedPrefs.getValue(SharedPrefs.SKIP_LOGIN).equals("yes")) {
                    loginScreen();
                } else {
                    imgUnWish.setVisibility(View.GONE);
                    imgWish.setVisibility(View.VISIBLE);
                    addWishlist();
                }
                break;
            case R.id.imgWish:
                imgUnWish.setVisibility(View.VISIBLE);
                imgWish.setVisibility(View.GONE);
                removeWishList();
                break;
            case R.id.txtEdit:
                startActivityForResult(new Intent(this, ChekInCheckOutActivity.class).putExtra("hotel_id", hotelID), 1);
                break;

            case R.id.llGuest:
                checkINCheckOutActivity(3);
                break;

            case R.id.llcheckIn:
                checkINCheckOutActivity(1);
                break;

            case R.id.llcheckOut:
                checkINCheckOutActivity(2);
                break;

            case R.id.imgShare:
               shareIntent();
                break;
        }
    }

    private void shareIntent() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "https://keyrooms.in/hotel/"+slug);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);

    }


    private void getCurrentLocation() {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + map_lat + "," + map_lng);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }

    }


    private void callBookNow() {
        txtBookNow.setClickable(false);
        Log.e("arraysize==>","@@"+arryJson.size());
        Log.e("totalamt==>","@@"+totalAmt);
        if (arryJson.size() > 0 && totalAmt > 0) {
            //progressBar.setVisibility(View.VISIBLE);
            Call<JsonObject> call = apiHearde.addToCart(String.valueOf(hotelID), "hotel", ChekInCheckOutActivity.strCheckInDate, ChekInCheckOutActivity.strCheckOutDate, String.valueOf(ChekInCheckOutActivity.totalAdult), String.valueOf(ChekInCheckOutActivity.totalChildren), arryJson.toString());
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                  /*  if (progressBar != null && progressBar.isShown()) {
                        progressBar.setVisibility(View.GONE);
                    }*/
                    try {
                        Log.e("addToCart==",""+response.body());
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getBoolean("status")) {
                            JSONObject jsonDataBooking = jsonObject.getJSONObject("booking");
                            JSONObject jsonDataUser = jsonObject.getJSONObject("user");
                            JSONObject jsonDataService = jsonObject.getJSONObject("service");
                            JSONObject jsonDataPayment = jsonObject.getJSONObject("gateways");
                            JSONObject jsonDataMode = jsonDataPayment.getJSONObject("offline_payment");
                            startActivity(new Intent(HotelDetailsActivity.this, DoCheckOutActivity.class).
                                    putExtra("code", jsonDataBooking.getString("code")).
                                    putExtra("id", jsonDataBooking.getString("id")).
                                    putExtra("created_at", jsonDataBooking.getString("created_at")).
                                    putExtra("start_date", jsonDataBooking.getString("start_date")).
                                    putExtra("end_date", jsonDataBooking.getString("end_date")).
                                    putExtra("status", jsonDataBooking.getString("status")).
                                    putExtra("gateway", jsonDataMode.getString("name")).
                                    putExtra("nights", jsonDataBooking.getString("nights")).
                                    putExtra("adults", jsonDataBooking.getString("total_guests")).
                                    putExtra("title", jsonDataService.getString("title")).
                                    putExtra("price", String.valueOf(totalAmt)).
                                    putExtra("first_name", jsonDataUser.getString("first_name")).
                                    putExtra("last_name", jsonDataUser.getString("last_name"))
                                    .putExtra("phone", jsonDataUser.getString("phone"))
                                    .putExtra("email", jsonDataUser.getString("email"))
                                    .putExtra("address", jsonDataUser.getString("address"))
                                    .putExtra("address2", jsonDataUser.getString("address2"))
                                    .putExtra("country", jsonDataUser.getString("country"))
                                    .putExtra("zip_code", jsonDataUser.getString("zip_code"))
                                    .putExtra("city", jsonDataUser.getString("city"))
                                    .putExtra("address",txtHotelAddress.getText().toString())
                                    .putExtra("room",ChekInCheckOutActivity.room+"")
                                    .putExtra("state", jsonDataUser.getString("state")));
                            finish();
                        } else {
                            txtBookNow.setClickable(true);
                            Toast.makeText(HotelDetailsActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    txtBookNow.setClickable(true);
                    Toast.makeText(HotelDetailsActivity.this, "Something went wrong!!!", Toast.LENGTH_LONG).show();
                    t.printStackTrace();
                }
            });
        } else {
            txtBookNow.setClickable(true);
            Toast.makeText(HotelDetailsActivity.this, "Please select room", Toast.LENGTH_LONG).show();
        }

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
                    Toast.makeText(HotelDetailsActivity.this, "GPS is Turn On", Toast.LENGTH_SHORT).show();
                    break;

                case Activity.RESULT_CANCELED:
                    Toast.makeText(HotelDetailsActivity.this, "GPS location is required to be Turn on", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        if (requestCode == 1) {
            try {
                if (data != null) {
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }


    public void getDate() {

        ChekInCheckOutActivity.isCorrecInDate = true;
        ChekInCheckOutActivity.isCorrecOutDate = true;

            Calendar calendar = Calendar.getInstance();
            Date today = calendar.getTime();

            calendar.add(Calendar.DAY_OF_MONTH, +1);

            Date tomorrow = calendar.getTime();

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            ChekInCheckOutActivity.strCheckInDate = dateFormat.format(today);
            ChekInCheckOutActivity.strCheckOutDate = dateFormat.format(tomorrow);

    }


    private void loginScreen() {
        Intent it = new Intent(HotelDetailsActivity.this, LoginActivity.class);
        startActivity(it);
    }

    private void addWishlist() {
        Call<JsonObject> call = apiHearde.addWhishlist(hotelID, "hotel");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getBoolean("status")) {
                        Toast.makeText(HotelDetailsActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        if (jsonObject.getString("messages").equals("Token is Expired")) {
                            logoutUser();
                        } else {
                            Toast.makeText(HotelDetailsActivity.this, jsonObject.getString("messages"), Toast.LENGTH_SHORT).show();
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

    private void removeWishList() {
        Call<JsonObject> call = apiHearde.removeWishList(SharedPrefs.getInt(SharedPrefs.User_id), hotelID);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getBoolean("status")) {
                        Toast.makeText(HotelDetailsActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        if (jsonObject.getString("messages").equals("Token is Expired")) {
                            logoutUser();
                        } else {
                            Toast.makeText(HotelDetailsActivity.this, jsonObject.getString("messages"), Toast.LENGTH_SHORT).show();
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
        Intent it = new Intent(HotelDetailsActivity.this, LoginActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(it);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void getRoomDetailCount(int id, int no_content, int price) {
        if (price == -1) {
            for (int i = 0; i < arryJson.size(); i++) {
                JsonObject object = (JsonObject) arryJson.get(i);
                JsonObject priceObject = (JsonObject) arrayID.get(i);
                if (Integer.parseInt(String.valueOf(object.get("id"))) == id) {
                    arryJson.remove(i);
                    totalAmt = totalAmt - Integer.parseInt(String.valueOf(priceObject.get("price")));
                    arrayID.remove(i);
                    break;
                }
            }
            txtAmount.setText("\u20B9" + totalAmt);
        } else {
            for (int i = 0; i < arryJson.size(); i++) {
                JsonObject object = (JsonObject) arryJson.get(i);
                JsonObject priceObject = (JsonObject) arrayID.get(i);
                if (Integer.parseInt(String.valueOf(object.get("id"))) == id) {
                    arryJson.remove(i);
                    totalAmt = totalAmt - Integer.parseInt(String.valueOf(priceObject.get("price")));
                    arrayID.remove(i);
                    break;
                }
            }
            JsonObject hashMap = new JsonObject();
            hashMap.addProperty("id", id);
            hashMap.addProperty("number_selected", no_content);
            arryJson.add(hashMap);
            JsonObject priceObj = new JsonObject();
            priceObj.addProperty("id", id);
            priceObj.addProperty("price", price);
            arrayID.add(priceObj);
            totalAmt = totalAmt + price;
            txtAmount.setText("\u20B9" + totalAmt);
        }
        if (totalAmt == 0) {
            if (hotelDetailsModel.getRow().getPrice() != null) {
//                if (price == 0 && no_content == 0)
//                {
//                    txtAmount.setText("\u20B9" + "0");
//                }
//                else
                txtAmount.setText("\u20B9" + hotelDetailsModel.getRow().getPrice());

            }
            else {
                txtAmount.setText("\u20B9" + "0");
            }
        }
    }

    private void checkAvability() {
        if (ChekInCheckOutActivity.isCorrecInDate && ChekInCheckOutActivity.isCorrecOutDate) {
            Log.e("hotelID", "@@" + hotelID);
            Log.e("ChekInCheckOutActivity.strCheckInDate", "@@" + ChekInCheckOutActivity.strCheckInDate);
            Log.e("ChekInCheckOutActivity.strCheckOutDate", "@@" + ChekInCheckOutActivity.strCheckOutDate);
            Log.e("ChekInCheckOutActivity.totalAdult", "@@" + ChekInCheckOutActivity.totalAdult);
            Log.e("ChekInCheckOutActivity.totalChildren", "@@" + ChekInCheckOutActivity.totalChildren);
            Call<JsonObject> call = api.checkAvability((hotelID), ChekInCheckOutActivity.strCheckInDate, ChekInCheckOutActivity.strCheckOutDate, (ChekInCheckOutActivity.totalAdult), (ChekInCheckOutActivity.totalChildren));
            call.enqueue(new Callback<JsonObject>() {

                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e("URL==>checkAvability", "@@" + response.raw().request().url());
                  /*  if (progressBar != null && progressBar.isShown()) {
                        progressBar.setVisibility(View.GONE);
                    }*/
                    try {
                        SearchResultActivity.isCheckCallApi = false;
                        CheckAvabilityModel checkAvabilityModel = new Gson().fromJson(response.body(), CheckAvabilityModel.class);
                        if (checkAvabilityModel.isStatus()) {
                            if (hotelID != 0) {
                                if (arrayListRoom != null)
                                    arrayListRoom.clear();
                                    arrayListRoom = checkAvabilityModel.getRooms();
                                    callCheckInData();
                            }
                        } else {
                            Toast.makeText(HotelDetailsActivity.this, checkAvabilityModel.getMessages(), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(HotelDetailsActivity.this, "Something went wrong!!!", Toast.LENGTH_LONG).show();
                    t.printStackTrace();
                }
            });
        }
    }


    private void callCheckInData() {
        roomSize = ChekInCheckOutActivity.room;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        txtCheckOut.setText(ChekInCheckOutActivity.strCheckOutDate);


        try {

            /*
            Date date = format.parse(ChekInCheckOutActivity.strCheckInDate);
            String day = date2DayTime(date);

            //txtCheckIn.setText(day);

            Date dateTomo = format.parse(ChekInCheckOutActivity.strCheckOutDate);
            String dayTomo = date2DayTime(dateTomo);
            txtCheckOut.setText(dayTomo);
*/
            txtCheckIn.setText(HomeFragment.formatDateFromDateString("yyyy-MM-dd","dd-MMM-yyyy",ChekInCheckOutActivity.strCheckInDate));
            txtCheckOut.setText(HomeFragment.formatDateFromDateString("yyyy-MM-dd","dd-MMM-yyyy",ChekInCheckOutActivity.strCheckOutDate));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (ChekInCheckOutActivity.totalAdult == 1) {
            txtGuestAndRoom.setText(ChekInCheckOutActivity.room + " Room" + "\n" + (ChekInCheckOutActivity.totalAdult) + " Guest");
        }
        else {
            txtGuestAndRoom.setText(ChekInCheckOutActivity.room + " Rooms" + "\n" + (ChekInCheckOutActivity.totalAdult) + " Guests");
        }

        if (arrayListRoom.size() > 0) {
            roomDetailWithAvaiblityAdapter = new RoomDetailWithAvaiblityAdapter(HotelDetailsActivity.this, arrayListRoom);
            rcyRoomDetail.setAdapter(roomDetailWithAvaiblityAdapter);
        } else {
            roomDetailWithAvaiblityAdapter = new RoomDetailWithAvaiblityAdapter(HotelDetailsActivity.this, arrayListRoom);
            rcyRoomDetail.setAdapter(roomDetailWithAvaiblityAdapter);
            Toast.makeText(HotelDetailsActivity.this, "This search criteria hotel rooms not available.", Toast.LENGTH_LONG).show();
        }
    }


    private static SimpleDateFormat yyyy_MM_dd_HH_mm = new SimpleDateFormat("dd MMM", Locale.getDefault());
    private static SimpleDateFormat MM_dd_HHmm = new SimpleDateFormat("dd MMM", Locale.getDefault());

    public static String date2DayTime(Date oldTime) {
        Date newTime = new Date();
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(newTime);

            Calendar oldCal = Calendar.getInstance();
            oldCal.setTime(oldTime);

            int oldYear = oldCal.get(Calendar.YEAR);
            int year = cal.get(Calendar.YEAR);
            int oldDay = oldCal.get(Calendar.DAY_OF_YEAR);
            int day = cal.get(Calendar.DAY_OF_YEAR);

            Log.e("date2DayTime==>", "@@" + oldTime);

            if (oldYear == year) {
                int value = oldDay - day;
                if (value == -1) {
                    return "yesterday";
                } else if (value == 0) {
                    return "Today";
                } else if (value == 1) {
                    return "Tomorrow";
                } else {
                    return MM_dd_HHmm.format(oldTime);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return yyyy_MM_dd_HH_mm.format(oldTime);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {

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
    public void AddWishListHotel(int hotelID) {

    }

    @Override
    public void RemoveWishListHotel(Integer hotel_id) {

    }

    private void checkINCheckOutActivity(int type) {
        Intent it = new Intent(getApplicationContext(), ChekInCheckOutActivity.class);
        it.putExtra("type", type);
        startActivityForResult(it, 2);
    }

}
