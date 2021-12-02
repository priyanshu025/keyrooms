package com.keyroom.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.keyroom.Activity.SearchHotelActivity;
import com.keyroom.Adapter.WishListAdapter;
import com.keyroom.InterFace.OnLoadMoreListener;
import com.keyroom.InterFace.RemoveWishListHotel;
import com.keyroom.Model.WishListModel;
import com.keyroom.Network.API;
import com.keyroom.Network.ConfigHader;
import com.keyroom.R;
import com.keyroom.Utility.SharedPrefs;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishListFragment extends Fragment implements RemoveWishListHotel {

    RecyclerView rcyWishList;
    RelativeLayout lnyMain;
    API api;
    ArrayList<WishListModel.Wishlist> wishlistArrayList = new ArrayList<>();
    WishListAdapter wishListAdapter;
    Handler handler;
    Integer page_no=1,total_record=0;
    ImageView img_back;
    TextView edtSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        api = ConfigHader.getClient().create(API.class);
        View rootView = inflater.inflate(R.layout.fragment_wishlist, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View view) {
        handler=new Handler();
        rcyWishList = view.findViewById(R.id.rcyWishList);
        lnyMain = view.findViewById(R.id.lnyMain);
        img_back=view.findViewById(R.id.image_back);
        edtSearch=view.findViewById(R.id.edtSearch);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            }
        });

        edtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), SearchHotelActivity.class);
                startActivity(it);
            }
        });


        rcyWishList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        wishListAdapter = new WishListAdapter(getActivity(), wishlistArrayList, this::RemoveWishListHotel, rcyWishList, () -> {
            if (total_record > wishlistArrayList.size()) {
                final Runnable r = new Runnable() {
                    public void run() {
                        ++page_no;
                        wishList();
                    }
                };
                handler.post(r);
            }
        });
        rcyWishList.setAdapter(wishListAdapter);
        wishList();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void wishList() {
        rcyWishList.setVisibility(View.VISIBLE);
        if(rcyWishList.getVisibility()==View.VISIBLE) {
            if(page_no==1)
            wishlistArrayList.clear();
            Call<JsonObject> call = api.wishList(SharedPrefs.getInt(SharedPrefs.User_id),page_no);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        wishListAdapter.setLoaded();
                        WishListModel wishListModel = new Gson().fromJson(response.body(), WishListModel.class);
                        total_record=wishListModel.getTotal_records();
                        wishlistArrayList.addAll(wishListModel.getWishlist());
                        if (wishlistArrayList.size() != 0) {
                            wishListAdapter.notifyDataSetChanged();
                        } else {
                            wishListAdapter.notifyDataSetChanged();
                            Toast.makeText(getActivity(), "Data Not Found", Toast.LENGTH_SHORT).show();
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

    @Override
    public void RemoveWishListHotel(Integer hotel_id) {
        removeWishList(hotel_id);
    }

    private void removeWishList(Integer hotelID) {
        Call<JsonObject> call = api.removeWishList(SharedPrefs.getInt(SharedPrefs.User_id), hotelID);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if(jsonObject.getBoolean("status")){
                        wishList();
                    }else {
                        Toast.makeText(getActivity(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
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
