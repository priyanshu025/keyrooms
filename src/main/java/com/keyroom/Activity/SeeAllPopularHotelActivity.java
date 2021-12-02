package com.keyroom.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeeAllPopularHotelActivity extends AppCompatActivity implements View.OnClickListener, AddWishListHotel, RemoveWishListHotel, OnLoadMoreListener {

    RecyclerView rcyHotelList;
    ImageView imgBack;
    TextView txtFilter;
    TextView txtSort;
    String rate, terms;
    TextView edtSearch;
    private ShimmerFrameLayout shimmer_hotel_card_dummy;


    SearchResultAdapter searchResultAdapter;
    API api;
    API apiHearde;
    ArrayList<SearchModel.Rows> serachHotelModel = new ArrayList<>();
    boolean isFilter = false;
    int page_no = 1;
    int totalRecode = 0;
    Handler handler;
    String type = "";
    String sortBy = "";
//    private Intent intent = getIntent();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = Config.getClient().create(API.class);
        apiHearde = ConfigHader.getClient().create(API.class);
        setContentView(R.layout.activity_popularhotel);
        initView();
    }

    private void initView() {
        rcyHotelList = findViewById(R.id.rcyHotelList);
        imgBack = findViewById(R.id.img_back);
        txtFilter = findViewById(R.id.txtFilter);
        txtSort = findViewById(R.id.txtSort);
        edtSearch=findViewById(R.id.edtSearch);

        shimmer_hotel_card_dummy = findViewById(R.id.shimmer_hotel_card_dummy);
        shimmer_hotel_card_dummy.startShimmerAnimation();

        handler = new Handler();

        rcyHotelList.setLayoutManager(new LinearLayoutManager(SeeAllPopularHotelActivity.this, LinearLayoutManager.VERTICAL, false));

        clickEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFilter) {
            page_no = 1;
            searchResultAdapter = new SearchResultAdapter(SeeAllPopularHotelActivity.this, serachHotelModel, SeeAllPopularHotelActivity.this::AddWishListHotel, SeeAllPopularHotelActivity.this::RemoveWishListHotel, rcyHotelList);
            rcyHotelList.setAdapter(searchResultAdapter);
            Intent intent = getIntent();
            if(intent.getStringExtra("premiuim").equals("yes"))
            {
                getPremiumrHotelList("");
            }
            else {
                getPopularHotelList("");
            }


//            String func = getIntent().getStringExtra("premium");
//            System.out.println("this is boolean: "+func);
//            if(func.equals("0"))
//            {
//                getPremiumrHotelList("");
//            }else
//            {
//            }

        }
    }

    private void getPopularHotelList(String sortBy) {
        if (serachHotelModel != null)
            serachHotelModel.clear();
        Call<JsonObject> call = api.popularHotelList(SharedPrefs.getInt(SharedPrefs.User_id), page_no, sortBy);
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
    private void getPremiumrHotelList(String sortBy) {
        if (serachHotelModel != null)
            serachHotelModel.clear();
        Call<JsonObject> call = api.premiumHotelList(SharedPrefs.getInt(SharedPrefs.User_id), page_no, sortBy);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("premResponse : "+response.body());
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
        imgBack.setOnClickListener(this);
        txtFilter.setOnClickListener(this);
        txtSort.setOnClickListener(this);
        edtSearch.setOnClickListener(this);


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
            page_no = 1;
            type = "popular";
            if (serachHotelModel != null)
                serachHotelModel.clear();
            searchResultAdapter = new SearchResultAdapter(SeeAllPopularHotelActivity.this, serachHotelModel, SeeAllPopularHotelActivity.this::AddWishListHotel, SeeAllPopularHotelActivity.this::RemoveWishListHotel, rcyHotelList);
            rcyHotelList.setAdapter(searchResultAdapter);
            callFilterAPI();
        }

    }


    public void callFilterAPI() {
        if (page_no == 1) {
            if (serachHotelModel.size() > 0)
                serachHotelModel.clear();
        }
        Call<JsonObject> call = api.getFiltersApply(Content.MinPrice + "-" + Content.MaxPrice, rate, terms, page_no, type);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                searchResultAdapter.setLoaded();
                SearchModel searchModel = new Gson().fromJson(response.body(), SearchModel.class);
                totalRecode = searchModel.getTotal_records();
                if (searchModel.getRows() != null) {
                    serachHotelModel.addAll(searchModel.getRows());
                    searchResultAdapter = new SearchResultAdapter(SeeAllPopularHotelActivity.this, serachHotelModel, SeeAllPopularHotelActivity.this::AddWishListHotel, SeeAllPopularHotelActivity.this::RemoveWishListHotel, rcyHotelList);
                    rcyHotelList.setAdapter(searchResultAdapter);
                    if (serachHotelModel.size() != 0) {
                        searchResultAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(SeeAllPopularHotelActivity.this, "No data found", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.txtFilter:
                isFilter = false;
                type = "popular";
                filterActivity();
                break;
            case R.id.txtSort:
                sortMenu();
                break;

            case R.id.edtSearch:
                Intent it = new Intent(SeeAllPopularHotelActivity.this, SearchHotelActivity.class);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(it);
                finish();
                break;
        }
    }

    private void sortMenu() {
        PopupMenu popup = new PopupMenu(SeeAllPopularHotelActivity.this, txtSort);
        popup.getMenuInflater().inflate(R.menu.poupup_menu_sort, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            Intent intent = getIntent();
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.lowtohigh:
                        page_no = 1;
                        sortBy = "asc";
                        if(intent.getStringExtra("premiuim").equals("yes"))
                        {
                            getPremiumrHotelList("");
                        }
                        getPopularHotelList(sortBy);
                        break;
                    case R.id.hightolow:
                        page_no = 1;
                        sortBy = "desc";
                        if(intent.getStringExtra("premiuim").equals("yes"))
                        {
                            getPremiumrHotelList("");
                        }
                        getPopularHotelList(sortBy);
                        break;
                }
                return true;
            }
        });
        popup.show();

    }

    private void filterActivity() {
        startActivityForResult(new Intent(SeeAllPopularHotelActivity.this, FilterActivity.class), 1);
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
                        Toast.makeText(SeeAllPopularHotelActivity.this, jsonObject.getString("messages"), Toast.LENGTH_SHORT).show();
                    } else {
                        if (jsonObject.getString("messages").equals("Token is Expired")) {
                            logoutUser();
                        } else {
                            Toast.makeText(SeeAllPopularHotelActivity.this, jsonObject.getString("messages"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SeeAllPopularHotelActivity.this, jsonObject.getString("messages"), Toast.LENGTH_SHORT).show();
                    } else {
                        if (jsonObject.getString("messages").equals("Token is Expired")) {
                            logoutUser();
                        } else {
                            Toast.makeText(SeeAllPopularHotelActivity.this, jsonObject.getString("messages"), Toast.LENGTH_SHORT).show();
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
        Intent it = new Intent(SeeAllPopularHotelActivity.this, LoginActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(it);
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
                    if (type.equals("popular")) {
                        callFilterAPI();
                    } else {
                        getPopularHotelListPage();
                    }
                }
            };
            handler.post(r);
        }
    }

    private void getPopularHotelListPage() {
        Call<JsonObject> call = api.popularHotelList(SharedPrefs.getInt(SharedPrefs.User_id), page_no, sortBy);
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
