package com.keyroom.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.keyroom.Activity.ChekInCheckOutActivity;
import com.keyroom.Activity.CityLocationsActivity;
import com.keyroom.Activity.LoginActivity;
import com.keyroom.Activity.NotificationActivity;
import com.keyroom.Activity.PagesActivity;
import com.keyroom.Activity.SearchActivity;
import com.keyroom.Activity.SearchHotelActivity;
import com.keyroom.Activity.SearchResultActivity;
import com.keyroom.Activity.SeeAllBanquetsActivity;
import com.keyroom.Activity.SeeAllNearByHotelActivity;
import com.keyroom.Activity.SeeAllPartyHallActivity;
import com.keyroom.Activity.SeeAllPopularHotelActivity;
import com.keyroom.Adapter.CityAdapter;
import com.keyroom.Adapter.PopularHotelAdapter;
import com.keyroom.InterFace.AddWishListHotel;
import com.keyroom.InterFace.RemoveWishListHotel;
import com.keyroom.Model.Data;
import com.keyroom.Model.GlobalSearchModel;
import com.keyroom.Model.LocationCityModel;
import com.keyroom.Model.LocationModel;
import com.keyroom.Model.PopularHotelModel;
import com.keyroom.Network.API;
import com.keyroom.Network.Config;
import com.keyroom.Network.ConfigHader;
import com.keyroom.R;
import com.keyroom.Utility.SharedPrefs;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment implements View.OnClickListener, AddWishListHotel, RemoveWishListHotel {

    public Calendar calendar;

    LinearLayout near_me_txt;
    TextView edtSearch;
    ImageView notificationImg;
    LinearLayout checkInLinearLayout, checkoutLinearLayout;
    TextView txtToday, txtTomorrow;
    TextView viewAllPopularHotel;
    RecyclerView popularHotelRecycler;
    PopularHotelAdapter popularHotelAdapter;;

    TextView txtGuestOrRoom;
    ArrayList<Data> globleSearchList = new ArrayList<>();
    TextView search_txt;
    CardView listYourProperty,more_offer_card;
    CardView card1, card2, card3,card4;

    CityAdapter cityAdapter;
    ArrayList<LocationCityModel> cityListName = new ArrayList<>();
    ArrayList<PopularHotelModel.Rows> popularHotelList = new ArrayList<>();


    RecyclerView rcyCityList;
    LinearLayout lnyAllCities;
    API api;
    API apiHearde;
    int page_no = 1;
    ImageView call_img;
    String phone_no = "9560509906";
    String msg = "";

    SimpleDateFormat dateFormatDay;
    ImageView moreOffers;
    ImageView img_vaccineFinder;

    Boolean near_me_click = true;
    private ShimmerFrameLayout mShimmerViewContainer, shimmer_hotel_card_dummy;
    ImageView stay_1,stay_2,stay_3,stay_4,stay_5;
    CardView card_party1,card_party2,card_party3,card_party4;


    public static String formatDateFromDateString(String inputDateFormat, String outputDateFormat, String inputDate) throws ParseException {
        Date mParsedDate;
        String mOutputDateString;
        SimpleDateFormat mInputDateFormat = new SimpleDateFormat(inputDateFormat, java.util.Locale.getDefault());
        SimpleDateFormat mOutputDateFormat = new SimpleDateFormat(outputDateFormat, java.util.Locale.getDefault());
        mParsedDate = mInputDateFormat.parse(inputDate);
        mOutputDateString = mOutputDateFormat.format(mParsedDate);
        return mOutputDateString;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        api = Config.getClient().create(API.class);
        apiHearde = ConfigHader.getClient().create(API.class);
        dateFormatDay = new SimpleDateFormat("yyyy-MM-dd");
        calendar = Calendar.getInstance();
//        System.out.println("Usedid : "+SharedPrefs.getValue(SharedPrefs.User_id));
        ChekInCheckOutActivity.strCheckInDate = dateFormatDay.format(calendar.getTime()) + "";
        ChekInCheckOutActivity.isCorrecInDate = true;
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        ChekInCheckOutActivity.strCheckOutDate = dateFormatDay.format(calendar.getTime()) + "";
        ChekInCheckOutActivity.isCorrecOutDate = true;

        initView(rootView);
        return rootView;
    }

    private void initView(View view) {
        edtSearch = view.findViewById(R.id.edtSearch);
        search_txt = view.findViewById(R.id.search_txt);
        rcyCityList = view.findViewById(R.id.rcyCityList);
        near_me_txt = view.findViewById(R.id.near_me);
        popularHotelRecycler = view.findViewById(R.id.popularHotelRecycler);
        checkInLinearLayout = view.findViewById(R.id.checkInLinearLayout);
        checkoutLinearLayout = view.findViewById(R.id.checkoutLinearLayout);
        lnyAllCities = view.findViewById(R.id.lnyAllCities);
        viewAllPopularHotel = view.findViewById(R.id.viewAllPopularHotel);
        txtToday = view.findViewById(R.id.txtToday);
        txtTomorrow = view.findViewById(R.id.txtTomorrow);
        notificationImg = view.findViewById(R.id.notification_img);
        txtGuestOrRoom = view.findViewById(R.id.txt_guest_or_room);
        call_img = view.findViewById(R.id.call_img);
        moreOffers = view.findViewById(R.id.more_offers);
        //img_vaccineFinder = view.findViewById(R.id.img_vaccineFinder);
       // listYourProperty = view.findViewById(R.id.listYourProperty);
        card1 = view.findViewById(R.id.card1);
        card2 = view.findViewById(R.id.card2);
//        card3 = view.findViewById(R.id.card3);
        card4 = view.findViewById(R.id.card4);

//        stay_1=view.findViewById(R.id.stay_1);
//        stay_2=view.findViewById(R.id.stay_2);
//        stay_3=view.findViewById(R.id.stay_3);
//        stay_4=view.findViewById(R.id.stay_4);
        //stay_5=view.findViewById(R.id.stay_5);


        card_party1=view.findViewById(R.id.card_party1);
        card_party2=view.findViewById(R.id.card_party2);
        card_party3=view.findViewById(R.id.card_party3);
        card_party4=view.findViewById(R.id.card_party4);
        more_offer_card=view.findViewById(R.id.more_offer_card);
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        shimmer_hotel_card_dummy = view.findViewById(R.id.shimmer_hotel_card_dummy);
        shimmer_hotel_card_dummy.startShimmerAnimation();
        mShimmerViewContainer.startShimmerAnimation();


        rcyCityList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        popularHotelRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));


        cityAdapter = new CityAdapter(getActivity(), cityListName);
        rcyCityList.setAdapter(cityAdapter);

        popularHotelAdapter = new PopularHotelAdapter(getActivity(), popularHotelList, this, this);
        popularHotelRecycler.setAdapter(popularHotelAdapter);

        getcityList();
        getPopularHotelList();
        clickEvent();
        isFirstTime();
    }

    private void isFirstTime() {
        try {
            SharedPreferences preferences = getActivity().getSharedPreferences("tokenSave", Context.MODE_PRIVATE);
            boolean isFirstTime = preferences.getBoolean("isFirstToken", true);
            //default value true
            if (isFirstTime) {
                //if its true then its
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isFirstToken", true);// set it to false
                editor.apply();

                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                // Get new FCM registration token
                                try {
                                    String token = task.getResult();
                                    Log.e("token%%", "" + token);

                                        Call<JsonObject> call = apiHearde.getToken(token, SharedPrefs.getValue(SharedPrefs.Phone_No));
                                        call.enqueue(new Callback<JsonObject>() {
                                            @Override
                                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                                Log.e("getTokenResponse=", "" + response.body());
                                            }

                                            @Override
                                            public void onFailure(Call<JsonObject> call, Throwable t) {

                                            }
                                        });
                                } catch (Exception e) {
                                    Log.e("exception==", "" + e.getMessage());
                                }
                            }
                        });


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getcityList() {
        cityListName.clear();
        Call<JsonObject> call = api.locations();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("Location", "==@@>>>" + response);
                LocationModel locationModel = new Gson().fromJson(response.body(), LocationModel.class);
                cityListName.addAll(locationModel.getLocations());
                if (cityListName.size() != 0) {
                    cityAdapter.notifyDataSetChanged();

                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    near_me_txt.setVisibility(View.VISIBLE);
                    lnyAllCities.setVisibility(View.VISIBLE);
                    rcyCityList.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }




    private void getPopularHotelList() {
        popularHotelList.clear();
        Call<JsonObject> call = api.popularHotelList(SharedPrefs.getInt(SharedPrefs.User_id), page_no, "");
        Log.e("@@ user_id: ", SharedPrefs.User_id);
        Log.e("@@ page_no: ", String.valueOf(page_no));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    PopularHotelModel popularHotelModel = new Gson().fromJson(response.body(), PopularHotelModel.class);
                    popularHotelList.addAll(popularHotelModel.getRows());
                    if (popularHotelList.size() != 0) {
                        popularHotelAdapter.notifyDataSetChanged();
                        shimmer_hotel_card_dummy.stopShimmerAnimation();
                        shimmer_hotel_card_dummy.setVisibility(View.GONE);
                        popularHotelRecycler.setVisibility(View.VISIBLE);


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

    private void getPremiumHotel() {
        Call<JsonObject> call = api.premiumHotelList(SharedPrefs.getInt(SharedPrefs.User_id), page_no, "");
        Log.e("@@ user_id: ", SharedPrefs.User_id);
        Log.e("@@ page_no: ", String.valueOf(page_no));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                System.out.println("response check:  "+ response.body());
                try {
                    PopularHotelModel popularHotelModel = new Gson().fromJson(response.body(), PopularHotelModel.class);
                    popularHotelList.addAll(popularHotelModel.getRows());
                    if (popularHotelList.size() != 0) {
                        popularHotelAdapter.notifyDataSetChanged();
                        shimmer_hotel_card_dummy.stopShimmerAnimation();
                        shimmer_hotel_card_dummy.setVisibility(View.GONE);
                        popularHotelRecycler.setVisibility(View.VISIBLE);


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

        edtSearch.setOnClickListener(this);
        viewAllPopularHotel.setOnClickListener(this);
        txtGuestOrRoom.setOnClickListener(this);
        near_me_txt.setOnClickListener(this);
        checkInLinearLayout.setOnClickListener(this);
        checkoutLinearLayout.setOnClickListener(this);
        search_txt.setOnClickListener(this);
        notificationImg.setOnClickListener(this);
        call_img.setOnClickListener(this);
        lnyAllCities.setOnClickListener(this);
        //img_vaccineFinder.setOnClickListener(this);
        moreOffers.setOnClickListener(this);
        //listYourProperty.setOnClickListener(this);
        card1.setOnClickListener(this);
        card2.setOnClickListener(this);
//        card3.setOnClickListener(this);
        card4.setOnClickListener(this);
//        stay_1.setOnClickListener(this);
//        stay_2.setOnClickListener(this);
//        stay_3.setOnClickListener(this);
//        stay_4.setOnClickListener(this);
//        stay_5.setOnClickListener(this);
        card_party1.setOnClickListener(this);
        card_party2.setOnClickListener(this);
        card_party3.setOnClickListener(this);
        card_party4.setOnClickListener(this);
        more_offer_card.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.near_me:
                if (near_me_click) {
                    near_me_click = false;
                    seeAllNearByHotel();

                }
                break;
            case R.id.lnyAllCities:
                SerachActivity();
                break;
            case R.id.checkInLinearLayout:
                checkINCheckOutActivity(1);
                break;
            case R.id.checkoutLinearLayout:
                checkINCheckOutActivity(2);
                break;
            case R.id.search_txt:
                startSearchActivity();
                break;
            case R.id.notification_img:
                startNotificationActivity();
                break;
            case R.id.txt_guest_or_room:
                checkINCheckOutActivity(3);
                break;
            case R.id.viewAllPopularHotel:
                seeAllPopularHotelActivity();
                break;
            case R.id.edtSearch:
                startSearchActivityForResult();
                break;
            case R.id.call_img:
                admincall();
                break;

          /*  case R.id.viewAllBanquetsHotel:
                getActivity().startActivity(new Intent(getActivity(), SeeAllBanquetsActivity.class));
                break;
            case R.id.viewAllPartyHotel:
                getActivity().startActivity(new Intent(getActivity(), SeeAllPartyHallActivity.class));
                break;
*/
            case R.id.more_offers:
                if (SharedPrefs.getValue(SharedPrefs.SKIP_LOGIN).equals("yes")) {
                    Intent it = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(it);
                } else {
                    //getActivity().startActivity(new Intent(getActivity(), CityLocationsActivity.class));
                    startSearchActivityForResultBanner();
                    //startSearchActivity();
                }


                break;

//            case R.id.img_vaccineFinder:
//                Intent intent = new Intent(getActivity(), PagesActivity.class);
//                intent.putExtra("url", "https://www.cowin.gov.in/");
//                intent.putExtra("pageName", "Book your slot");
//                getActivity().startActivity(intent);
//                break;

//            case R.id.listYourProperty:
//                Intent intent1 = new Intent(getActivity(), PagesActivity.class);
//                intent1.putExtra("url", "https://keyrooms.in/property");
//                intent1.putExtra("pageName", "Partner With Keyrooms");
//                getActivity().startActivity(intent1);
//                break;

            case R.id.card1:
                seeAllNearByHotel();
                break;
            case R.id.card2:
                Intent i = new Intent(getActivity(), SeeAllPopularHotelActivity.class);
                i.putExtra("premiuim", "yes");
                startActivity(i);
//                seeAllPopularHotelActivity();
                break;
//            case R.id.card3:
//                seeAllPopularHotelActivity();
//                break;
            case R.id.card4:
                Intent it = new Intent(getActivity(), SeeAllPopularHotelActivity.class);
                it.putExtra("premiuim", "no");
                startActivity(it);
                break;

//            case R.id.stay_1:
//                seeAllPopularHotelActivity();
//                break;
//            case R.id.stay_2:
//                seeAllPopularHotelActivity();
//                break;
//            case R.id.stay_3:
//                seeAllPopularHotelActivity();
//                break;
//            case R.id.stay_4:
//                seeAllPopularHotelActivity();
//                break;
//            case R.id.stay_5:
//                seeAllPopularHotelActivity();
//                break;

            case R.id.card_party1:
                getActivity().startActivity(new Intent(getActivity(), SeeAllBanquetsActivity.class));
                break;

            case R.id.card_party2:
                getActivity().startActivity(new Intent(getActivity(), SeeAllPartyHallActivity.class));
                break;

            case R.id.card_party3:
                getActivity().startActivity(new Intent(getActivity(), SeeAllBanquetsActivity.class));
                break;

            case R.id.card_party4:
                getActivity().startActivity(new Intent(getActivity(), SeeAllPartyHallActivity.class));
                break;

            case R.id.more_offer_card:
                seeAllNearByHotel();
                break;
        }
    }


    private void admincall() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                        101);
            }
        } else {
            if (!phone_no.equals("") && phone_no != null) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone_no));
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "Phone number not provided", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void startSearchActivityForResultBanner(){
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        startActivityForResult(intent, 1020);
    }
    private void startSearchActivityForResult() {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        startActivityForResult(intent, 1010);
    }

    private void SerachActivity() {
        Intent it = new Intent(getActivity(), SearchHotelActivity.class);
        startActivity(it);
    }

    private void startNotificationActivity() {
        Intent it = new Intent(getActivity(), NotificationActivity.class);
        startActivity(it);
    }

    private void startSearchActivity() {

        if (!edtSearch.getText().toString().equals("")) {
            for (int i = 0; i < globleSearchList.size(); i++) {

                if (globleSearchList.get(i).getFlage().equals("city")) {
                    Intent intent = new Intent(getContext(), SearchResultActivity.class);
                    intent.putExtra("cityName", edtSearch.getText().toString());
                    startActivity(intent);
                    break;
                }

            }
        } else {
            Toast.makeText(getContext(), "Please select hotel", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1010) {
            if (resultCode == Activity.RESULT_OK) {
                msg = data.getStringExtra("hotel");
                edtSearch.setText(msg);
                globleSearch(msg);
            }
        }
        if(requestCode==1020){
            msg=data.getStringExtra("hotel");
            Log.i("msg",msg);
            globleSearch(msg);
            Intent intent = new Intent(getContext(), SearchResultActivity.class);
            intent.putExtra("cityName", msg);
            startActivity(intent);
            //SerachActivity();
        }

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
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        if (jsonObject.getString("messages").equals("Token is Expired")) {
                            logoutUser();
                        } else {
                            Toast.makeText(getActivity(), jsonObject.getString("messages"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        if (jsonObject.getString("messages").equals("Token is Expired")) {
                            logoutUser();
                        } else {
                            Toast.makeText(getActivity(), jsonObject.getString("messages"), Toast.LENGTH_SHORT).show();
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
        Intent it = new Intent(getActivity(), LoginActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(it);
    }

    private void seeAllNearByHotel() {
        Intent it = new Intent(getActivity(), SeeAllNearByHotelActivity.class);
        startActivity(it);
    }

    private void seeAllPopularHotelActivity() {
        Intent it = new Intent(getActivity(), SeeAllPopularHotelActivity.class);
        it.putExtra("premiuim", "no");
        startActivity(it);
    }

    public void onResume() {
        super.onResume();
        checkClicked();


        try {
            txtToday.setText(formatDateFromDateString("yyyy-MM-dd", "dd-MMM-yyyy", ChekInCheckOutActivity.strCheckInDate));
            txtTomorrow.setText(formatDateFromDateString("yyyy-MM-dd", "dd-MMM-yyyy", ChekInCheckOutActivity.strCheckOutDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        txtGuestOrRoom.setText(ChekInCheckOutActivity.totalAdult + " Adults " + ChekInCheckOutActivity.room + " Room");


    }

    private void checkINCheckOutActivity(int type) {
        Intent it = new Intent(getActivity(), ChekInCheckOutActivity.class);
        it.putExtra("type", type);
        startActivityForResult(it, 2);
    }

    @Override
    public void onStop() {
        super.onStop();
        checkClicked();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        globleSearchList.clear();
        checkClicked();

    }

    @Override
    public void onPause() {
        super.onPause();
        checkClicked();
    }

    public void checkClicked() {
        near_me_click = true;
    }
}
