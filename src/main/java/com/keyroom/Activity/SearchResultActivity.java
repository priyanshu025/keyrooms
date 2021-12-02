package com.keyroom.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.keyroom.Adapter.SearchResultAdapter;
import com.keyroom.InterFace.AddWishListHotel;
import com.keyroom.InterFace.OnLoadMoreListener;
import com.keyroom.InterFace.RemoveWishListHotel;
import com.keyroom.Model.SearchModel;
import com.keyroom.Network.API;
import com.keyroom.Network.Config;
import com.keyroom.Network.ConfigHader;
import com.keyroom.R;
import com.keyroom.Utility.Content;
import com.keyroom.Utility.SharedPrefs;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultActivity extends AppCompatActivity implements View.OnClickListener, AddWishListHotel, RemoveWishListHotel, OnLoadMoreListener {

    public static boolean isCheckCallApi = false;
    public TextView txtCheckInCheckOut, txtRoomAndGuest;
    public SearchResultAdapter searchResultAdapter;
    protected Handler handler;
    ImageView img_back;
    RecyclerView rcyHotelList;
    API api;
    API apiHearde;
    String cityName;
    ArrayList<SearchModel.Rows> serachHotelModel = new ArrayList<>();
    boolean isFilter = false;
    String rate, terms;
    int page_no = 1;
    int totalRecode = 0;
    String type = "";
    String sortBy = "";
    TextView txtSort;
    TextView edtSearch;

    private ShimmerFrameLayout shimmer_hotel_card_dummy;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = Config.getClient().create(API.class);
        apiHearde = ConfigHader.getClient().create(API.class);
        setContentView(R.layout.activity_searchresult);
        try {
            initView();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void initView() throws ParseException {
        rcyHotelList = findViewById(R.id.rcyHotelList);
        txtCheckInCheckOut = findViewById(R.id.txtCheckInCheckOut);
        txtRoomAndGuest = findViewById(R.id.txtRoomAndGuest);
        img_back=findViewById(R.id.img_back);
        txtSort = findViewById(R.id.txtSort);
        edtSearch=findViewById(R.id.edtSearch);
        handler = new Handler();
        addDate();

        shimmer_hotel_card_dummy = findViewById(R.id.shimmer_hotel_card_dummy);
        shimmer_hotel_card_dummy.startShimmerAnimation();

        rcyHotelList.setHasFixedSize(true);
        rcyHotelList.setLayoutManager(new LinearLayoutManager(SearchResultActivity.this, LinearLayoutManager.VERTICAL, false));

        cityName = getIntent().getStringExtra("cityName");
        clickEvent();

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!isFilter) {
            page_no = 1;
            searchResultAdapter = new SearchResultAdapter(SearchResultActivity.this, serachHotelModel, SearchResultActivity.this::AddWishListHotel, SearchResultActivity.this::RemoveWishListHotel, rcyHotelList);
            rcyHotelList.setAdapter(searchResultAdapter);
            getSearchHotelList("");

            shimmer_hotel_card_dummy.startShimmerAnimation();
            shimmer_hotel_card_dummy.setVisibility(View.VISIBLE);
            rcyHotelList.setVisibility(View.GONE);


        }
    }

    private void getSearchHotelList(String sortBy) {
        if (serachHotelModel != null)
            serachHotelModel.clear();
        Call<JsonObject> call = api.searchbyname(cityName, ChekInCheckOutActivity.strCheckInDate, ChekInCheckOutActivity.strCheckOutDate, String.valueOf(ChekInCheckOutActivity.totalAdult), String.valueOf(ChekInCheckOutActivity.totalChildren), String.valueOf(ChekInCheckOutActivity.room), SharedPrefs.getInt(SharedPrefs.User_id), page_no, sortBy);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    Log.e("URL", "@@" + response.raw().request().url());
                    searchResultAdapter.setLoaded();
                    SearchModel searchModel = new Gson().fromJson(response.body(), SearchModel.class);
                    totalRecode = searchModel.getTotal_records();
                    serachHotelModel.addAll(searchModel.getRows());
                    if (serachHotelModel.size() != 0) {
                        searchResultAdapter.notifyDataSetChanged();
                        shimmer_hotel_card_dummy.stopShimmerAnimation();
                        shimmer_hotel_card_dummy.setVisibility(View.GONE);
                        rcyHotelList.setVisibility(View.VISIBLE);

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

    private void clickEvent() {

        txtCheckInCheckOut.setOnClickListener(this);
        txtRoomAndGuest.setOnClickListener(this);
        txtSort.setOnClickListener(this);
        img_back.setOnClickListener(this);
        edtSearch.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtCheckInCheckOut:
                checkINCheckOutActivity(1);
                break;
            case R.id.txtRoomAndGuest:
                checkINCheckOutActivity(3);
                break;
            case R.id.txtFilter:
                isFilter = false;
                type = "";
                filterActivity();
                break;
            case R.id.txtSort:
                sortMenu();
                break;

            case R.id.img_back:
                finish();
                break;
            case R.id.edtSearch:
                Intent it = new Intent(SearchResultActivity.this, SearchHotelActivity.class);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(it);
                finish();
                break;
        }
    }

    private void sortMenu() {
        PopupMenu popup = new PopupMenu(SearchResultActivity.this, txtSort);
        popup.getMenuInflater().inflate(R.menu.poupup_menu_sort, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.lowtohigh:
                        page_no = 1;
                        sortBy = "asc";
                        getSearchHotelList(sortBy);
                        break;
                    case R.id.hightolow:
                        page_no = 1;
                        sortBy = "desc";
                        getSearchHotelList(sortBy);
                        break;
                }
                return true;
            }
        });
        popup.show();

    }

    private void filterActivity() {
        startActivityForResult(new Intent(SearchResultActivity.this, FilterActivity.class), 1);
    }

    private void checkINCheckOutActivity(int type) {
        Intent it = new Intent(SearchResultActivity.this, ChekInCheckOutActivity.class);
        it.putExtra("type",type);
        startActivityForResult(it, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
           /* if (progressBar != null && progressBar.isShown()) {
                progressBar.setVisibility(View.GONE);
            }*/
            isFilter = true;
            if (data != null) {
                rate = data.getStringExtra("star");
                terms = data.getStringExtra("terms");
            }
            type = "normal";
            page_no = 1;
            if (serachHotelModel != null)
                serachHotelModel.clear();
            searchResultAdapter = new SearchResultAdapter(SearchResultActivity.this, serachHotelModel, SearchResultActivity.this::AddWishListHotel, SearchResultActivity.this::RemoveWishListHotel, rcyHotelList);
            rcyHotelList.setAdapter(searchResultAdapter);
           // progressBar.setVisibility(View.VISIBLE);
          callFilterAPI();

        }
        if (requestCode == 2 && resultCode == 2) {
            addDate();
        }
    }

    private void addDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(ChekInCheckOutActivity.strCheckInDate);
            String day = HotelDetailsActivity.date2DayTime(date);

            Date dateTomo = format.parse(ChekInCheckOutActivity.strCheckOutDate);
            String dayTomo = HotelDetailsActivity.date2DayTime(dateTomo);

            if (day.equals("Today")) {
                txtCheckInCheckOut.setText(day + " - " + dayTomo);
            } else if (day.equals("Tomorrow")) {
                txtCheckInCheckOut.setText(day + " - " + dayTomo);
            } else {
                txtCheckInCheckOut.setText(day + " - " + dayTomo);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (ChekInCheckOutActivity.strCheckOutDate != null && !ChekInCheckOutActivity.strCheckOutDate.equals("")) {
            if (ChekInCheckOutActivity.totalAdult == 1) {

                txtRoomAndGuest.setText((ChekInCheckOutActivity.totalAdult) + " Adult" + "," + ChekInCheckOutActivity.room + " Room");
            } else
                txtRoomAndGuest.setText((ChekInCheckOutActivity.totalAdult) + " Adult" + "," + ChekInCheckOutActivity.room + " Room");
        }
    }

    @Override
    public void AddWishListHotel(int hotelID) {
        addWishlist(hotelID);
    }


    private void addWishlist(int hotelID) {
        Call<JsonObject> call = apiHearde.addWhishlist(hotelID, "hotel");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getBoolean("status")) {
                        Toast.makeText(SearchResultActivity.this, jsonObject.getString("messages"), Toast.LENGTH_SHORT).show();
                    } else {
                        if (jsonObject.getString("messages").equals("Token is Expired")) {
                            logoutUser();
                        } else {
                            Toast.makeText(SearchResultActivity.this, jsonObject.getString("messages"), Toast.LENGTH_SHORT).show();
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

    @Override
    public void RemoveWishListHotel(Integer hotel_id) {
        removeWishList(hotel_id);
    }

    private void removeWishList(Integer hotelID) {
        Call<JsonObject> call = apiHearde.removeWishList(SharedPrefs.getInt(SharedPrefs.User_id), hotelID);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getBoolean("status")) {
                        Toast.makeText(SearchResultActivity.this, jsonObject.getString("messages"), Toast.LENGTH_SHORT).show();
                    } else {
                        if (jsonObject.getString("messages").equals("Token is Expired")) {
                            logoutUser();
                        } else {
                            Toast.makeText(SearchResultActivity.this, jsonObject.getString("messages"), Toast.LENGTH_SHORT).show();
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
        Intent it = new Intent(SearchResultActivity.this, LoginActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(it);
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public void callFilterAPI() {
        Log.e("Content.MinPrice==>", "@@" + Content.MinPrice);
        Log.e("Content.MaxPrice==>", "@@" + Content.MaxPrice);
        Log.e("rate==>", "@@" + rate);
        Log.e("terms==>", "@@" + terms);
        Log.e("page_no==>", "@@" + page_no);
        Log.e("type==>", "@@" + type);
        Call<JsonObject> call = api.getFiltersApply(Content.MinPrice + "-" + Content.MaxPrice, rate, terms, page_no, type);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                /*if (progressBar != null && progressBar.isShown()) {
                    progressBar.setVisibility(View.GONE);
                }*/
                Log.e("URL==>", "@@" + response.raw().request().url());
                searchResultAdapter.setLoaded();
                SearchModel searchModel = new Gson().fromJson(response.body(), SearchModel.class);
                totalRecode = searchModel.getTotal_records();
                if (searchModel.getRows() != null) {
                    serachHotelModel.addAll(searchModel.getRows());
                    if (serachHotelModel.size() != 0) {
                        searchResultAdapter.notifyDataSetChanged();
                        shimmer_hotel_card_dummy.stopShimmerAnimation();
                        shimmer_hotel_card_dummy.setVisibility(View.GONE);
                        rcyHotelList.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(SearchResultActivity.this, "No data found", Toast.LENGTH_LONG).show();
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

    @Override
    public void onLoadMore() {
        if (totalRecode > serachHotelModel.size()) {
            final Runnable r = new Runnable() {
                public void run() {
                    ++page_no;

                    if (type.equals("popular")) {
                        callFilterAPI();
                    } else {
                        getSearchHotelListPage();
                    }
                }
            };
            handler.post(r);
        }
    }


    private void getSearchHotelListPage() {
        //progressBar.setVisibility(View.VISIBLE);
        Call<JsonObject> call = api.searchbyname(cityName, ChekInCheckOutActivity.strCheckInDate, ChekInCheckOutActivity.strCheckOutDate, String.valueOf(ChekInCheckOutActivity.totalAdult), String.valueOf(ChekInCheckOutActivity.totalChildren), String.valueOf(ChekInCheckOutActivity.room), SharedPrefs.getInt(SharedPrefs.User_id), page_no, sortBy);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    searchResultAdapter.setLoaded();
                    SearchModel searchModel = new Gson().fromJson(response.body(), SearchModel.class);
                    totalRecode = searchModel.getTotal_records();
                    serachHotelModel.addAll(searchModel.getRows());
                    if (serachHotelModel.size() != 0) {
                        searchResultAdapter.notifyDataSetChanged();
                        shimmer_hotel_card_dummy.stopShimmerAnimation();
                        shimmer_hotel_card_dummy.setVisibility(View.GONE);
                        rcyHotelList.setVisibility(View.VISIBLE);
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

}
