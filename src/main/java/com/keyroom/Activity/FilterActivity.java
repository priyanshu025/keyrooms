package com.keyroom.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.ArraySet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.keyroom.Adapter.FacilitiesFilterAdapter;
import com.keyroom.Adapter.FilterAddLayoutAdapter;
import com.keyroom.Adapter.HotelServiceAdapter;
import com.keyroom.Adapter.PropertyTypeAdapter;
import com.keyroom.Model.FilterModel;
import com.keyroom.Model.Terms;
import com.keyroom.Network.API;
import com.keyroom.Network.Config;
import com.keyroom.R;
import com.keyroom.RangBarSeek.OnRangeChangedListener;
import com.keyroom.RangBarSeek.RangeSeekBar;
import com.keyroom.Utility.Content;
import com.keyroom.Utility.SharedPrefs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.M)
public class FilterActivity extends AppCompatActivity implements View.OnClickListener, PropertyTypeAdapter.TermsArray {

    ImageView imgClose;
    TextView txtResetAll;

    LinearLayout lnyFilterPriceClick;
    ImageView imgFilterPrice;
    TextView txtMinPrice;
    TextView txtMaxPrice;
    RangeSeekBar seekPriceRange;
    LinearLayout lnyMinMaxPrice;

    LinearLayout lnyHotelStarClick;
    ImageView imgHotelStar;
    LinearLayout lnyHotelStar;
    CheckBox chekFive;
    CheckBox chekFour;
    CheckBox chekThree;
    CheckBox chekTwo;
    CheckBox chekOne;

    /*LinearLayout lnyPropertyTypeClick;
    ImageView imgPropertyType;
    LinearLayout lnyPropertyType;
    RecyclerView rcyPropertyType;*/

    LinearLayout lnyFacilitiesClick;
    ImageView imgFacilities;
    LinearLayout lnyFacilities;
    RecyclerView rcyFacilities;

    LinearLayout lnyHotelServiceClick;
    ImageView imgHotelService;
    LinearLayout lnyHotelService;
    RecyclerView rcyHotelService;

    LinearLayout llAddFilterLayout;
    RecyclerView rcyAddFilter;

    TextView txtApplyFilters;

    ArrayList<FilterModel.Attributes> attributesArrayList = new ArrayList<>();
    ArrayList<Terms> termsPropertytypeList = new ArrayList<>();
    ArrayList<Terms> FacilitiesTypeList = new ArrayList<>();
    ArrayList<Terms> hotelServiceTypeList = new ArrayList<>();

    PropertyTypeAdapter propertyTypeAdapter;
    FacilitiesFilterAdapter facilitiesFilterAdapter;
    HotelServiceAdapter hotelServiceAdapter;

    API api;

    String minPrice;
    String maxPrice;
   // ProgressBar progressBar;

    ArrayList<String> nameArrayList=new ArrayList<>();
    FilterAddLayoutAdapter filterAddLayoutAdapter;
    Set<String> termList=new ArraySet<>();
    Set<String> starList=new ArraySet<>();
    Set<String> arrayTerm;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = Config.getClient().create(API.class);
        setContentView(R.layout.activty_filter);
        initView();
    }

    private void initView() {
      //  progressBar=findViewById(R.id.progressBar);
        imgClose = findViewById(R.id.imgClose);
        txtResetAll = findViewById(R.id.txtResetAll);

        lnyFilterPriceClick = findViewById(R.id.lnyFilterPriceClick);
        imgFilterPrice = findViewById(R.id.imgFilterPrice);
        txtMinPrice = findViewById(R.id.txtMinPrice);
        txtMaxPrice = findViewById(R.id.txtMaxPrice);
        seekPriceRange = findViewById(R.id.seekPriceRange);
        lnyMinMaxPrice = findViewById(R.id.lnyMinMaxPrice);

        lnyHotelStarClick = findViewById(R.id.lnyHotelStarClick);
        imgHotelStar = findViewById(R.id.imgHotelStar);
        lnyHotelStar = findViewById(R.id.lnyHotelStar);
        chekFive = findViewById(R.id.chekFive);
        chekFour = findViewById(R.id.chekFour);
        chekThree = findViewById(R.id.chekThree);
        chekTwo = findViewById(R.id.chekTwo);
        chekOne = findViewById(R.id.chekOne);

       /* lnyPropertyTypeClick = findViewById(R.id.lnyPropertyTypeClick);
        imgPropertyType = findViewById(R.id.imgPropertyType);
        lnyPropertyType = findViewById(R.id.lnyPropertyType);
        rcyPropertyType = findViewById(R.id.rcyPropertyType);*/

        lnyFacilitiesClick = findViewById(R.id.lnyFacilitiesClick);
        imgFacilities = findViewById(R.id.imgFacilities);
        lnyFacilities = findViewById(R.id.lnyFacilities);
        rcyFacilities = findViewById(R.id.rcyFacilities);

        lnyHotelServiceClick = findViewById(R.id.lnyHotelServiceClick);
        imgHotelService = findViewById(R.id.imgHotelService);
        lnyHotelService = findViewById(R.id.lnyHotelService);
        rcyHotelService = findViewById(R.id.rcyHotelService);

        rcyAddFilter = findViewById(R.id.rcyAddFilter);
        llAddFilterLayout = findViewById(R.id.llAddFilterLayout);

        txtApplyFilters = findViewById(R.id.txtApplyFilters);

        //rcyPropertyType.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcyFacilities.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcyHotelService.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcyAddFilter.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        propertyTypeAdapter = new PropertyTypeAdapter(FilterActivity.this, termsPropertytypeList);
        //rcyPropertyType.setAdapter(propertyTypeAdapter);

        facilitiesFilterAdapter = new FacilitiesFilterAdapter(FilterActivity.this, FacilitiesTypeList);
        rcyFacilities.setAdapter(facilitiesFilterAdapter);

        hotelServiceAdapter = new HotelServiceAdapter(FilterActivity.this, hotelServiceTypeList);
        rcyHotelService.setAdapter(hotelServiceAdapter);

        filterAddLayoutAdapter=new FilterAddLayoutAdapter(this,nameArrayList);
        rcyAddFilter.setAdapter(filterAddLayoutAdapter);

        clickEvent();
        txtApplyFilters.setClickable(false);
        getFilter();


        //seekPriceRange.getLeftSeekBar().setThumbDrawableId(R.drawable.ic_dot);
        //seekPriceRange.getRightSeekBar().setThumbDrawableId(R.drawable.ic_dot);


        seekPriceRange.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftPinValue, float rightPinValue, boolean isFromUser) {
                int leftP = (int) leftPinValue;
                int rightP = (int) rightPinValue;
                txtMinPrice.setText("\u20B9" + leftP);
                txtMaxPrice.setText("\u20B9" + rightP);

                Content.MinPrice = String.valueOf(leftP);
                Content.MaxPrice = String.valueOf(rightP);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });

    }

    private void getFilter() {
        attributesArrayList.clear();
        termsPropertytypeList.clear();
      //  progressBar.setVisibility(View.VISIBLE);
        Call<JsonObject> call = api.getFilters();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
               /* if (progressBar != null && progressBar.isShown()) {
                    progressBar.setVisibility(View.GONE);
                }*/
                FilterModel filterModel = new Gson().fromJson(response.body(), FilterModel.class);
                if (filterModel.getStatus() == 1) {
                    if(SharedPrefs.getInt(SharedPrefs.minPrice)>25){
                        Content.MinPrice= String.valueOf(SharedPrefs.getInt(SharedPrefs.minPrice));
                        Content.MaxPrice= String.valueOf(SharedPrefs.getInt(SharedPrefs.maxPrice));
                        txtMinPrice.setText("\u20B9" +SharedPrefs.getInt(SharedPrefs.minPrice) );
                        txtMaxPrice.setText("\u20B9" + SharedPrefs.getInt(SharedPrefs.maxPrice));
                        seekPriceRange.setRange(Float.valueOf(filterModel.getData().getPrice_range().getMin_price()), Float.valueOf(filterModel.getData().getPrice_range().getMax_price()));
                        seekPriceRange.setProgress(Float.valueOf(SharedPrefs.getInt(SharedPrefs.minPrice)), Float.valueOf(SharedPrefs.getInt(SharedPrefs.maxPrice)));

                    }
                    else {
                        txtMinPrice.setText("\u20B9" + filterModel.getData().getPrice_range().getMin_price());
                        txtMaxPrice.setText("\u20B9" + filterModel.getData().getPrice_range().getMax_price());
                        seekPriceRange.setRange(Float.valueOf(filterModel.getData().getPrice_range().getMin_price()), Float.valueOf(filterModel.getData().getPrice_range().getMax_price()));
                        seekPriceRange.setProgress(Float.valueOf(filterModel.getData().getPrice_range().getMin_price()), Float.valueOf(filterModel.getData().getPrice_range().getMax_price()));
                    }
                    minPrice = filterModel.getData().getPrice_range().getMin_price();
                    maxPrice = filterModel.getData().getPrice_range().getMax_price();

                    attributesArrayList.addAll(filterModel.getData().getAttributes());

                    for (int i = 0; i < attributesArrayList.size(); i++) {
                        if (attributesArrayList.get(i).getName().equals("Property type")) {
                            termsPropertytypeList.addAll(attributesArrayList.get(i).getTerms());
                        } else if (attributesArrayList.get(i).getName().equals("Facilities")) {
                            FacilitiesTypeList.addAll(attributesArrayList.get(i).getTerms());
                        } else if (attributesArrayList.get(i).getName().equals("Hotel Service")) {
                            hotelServiceTypeList.addAll(attributesArrayList.get(i).getTerms());
                        }
                    }

                    if (termsPropertytypeList.size() != 0) {
                        propertyTypeAdapter.notifyDataSetChanged();
                    }
                    if (FacilitiesTypeList.size() != 0) {
                        facilitiesFilterAdapter.notifyDataSetChanged();
                    }
                    if (hotelServiceTypeList.size() != 0) {
                        hotelServiceAdapter.notifyDataSetChanged();
                    }
                    Set<String> array=SharedPrefs.getArrayList(SharedPrefs.rateId);
                    if(array!=null) {
                        Iterator it = array.iterator();
                        while (it.hasNext()) {
                            Object data = it.next();
                            if (data.equals("1")) {
                                chekOne.setChecked(true);
                            } else if (data.equals("2")) {
                                chekTwo.setChecked(true);
                            } else if (data.equals("3")) {
                                chekThree.setChecked(true);
                            } else if (data.equals("4")) {
                                chekFour.setChecked(true);
                            } else if (data.equals("5")) {
                                chekFive.setChecked(true);
                            }
                        }
                    }
                    arrayTerm= SharedPrefs.getArrayList(SharedPrefs.termId);
                    if(arrayTerm!=null) {
                        Iterator iterator = arrayTerm.iterator();
                        while (iterator.hasNext()) {
                            Object data = iterator.next();
                            termList.add(String.valueOf(data));
                            for (int i = 0; i < termsPropertytypeList.size(); i++) {
                                if (String.valueOf(termsPropertytypeList.get(i).getId()).equals(data)) {
                                    termsPropertytypeList.get(i).setCheck(true);
                                }
                            }
                            for (int i = 0; i < hotelServiceTypeList.size(); i++) {
                                if (String.valueOf(hotelServiceTypeList.get(i).getId()).equals(data)) {
                                    hotelServiceTypeList.get(i).setCheck(true);
                                }
                            }
                            for (int i = 0; i < FacilitiesTypeList.size(); i++) {
                                if (String.valueOf(FacilitiesTypeList.get(i).getId()).equals(data)) {
                                    FacilitiesTypeList.get(i).setCheck(true);
                                }
                            }
                        }
                    }

                    txtApplyFilters.setClickable(true);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
              /*  if (progressBar != null && progressBar.isShown()) {
                    progressBar.setVisibility(View.GONE);
                }*/
                t.printStackTrace();

            }
        });
    }

    private void clickEvent() {
        imgClose.setOnClickListener(this::onClick);
        txtResetAll.setOnClickListener(this::onClick);
        lnyFilterPriceClick.setOnClickListener(this::onClick);
        lnyHotelStarClick.setOnClickListener(this::onClick);
        //lnyPropertyTypeClick.setOnClickListener(this::onClick);
        lnyFacilitiesClick.setOnClickListener(this::onClick);
        lnyHotelServiceClick.setOnClickListener(this::onClick);
        txtApplyFilters.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgClose:
                onBackPressed();
                break;
            case R.id.txtResetAll:
                resetData();
                break;
            case R.id.lnyFilterPriceClick:
                if (imgFilterPrice.getRotation() == 180) {
                    imgFilterPrice.setRotation(0);
                    lnyMinMaxPrice.setVisibility(View.GONE);
                    seekPriceRange.setVisibility(View.GONE);
                } else {
                    imgFilterPrice.setRotation(180);
                    lnyMinMaxPrice.setVisibility(View.VISIBLE);
                    seekPriceRange.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.lnyHotelStarClick:
                if (imgHotelStar.getRotation() == 180) {
                    imgHotelStar.setRotation(0);
                    lnyHotelStar.setVisibility(View.GONE);
                } else {
                    imgHotelStar.setRotation(180);
                    lnyHotelStar.setVisibility(View.VISIBLE);
                }
                break;
          /*  case R.id.lnyPropertyTypeClick:
                if (imgPropertyType.getRotation() == 180) {
                    imgPropertyType.setRotation(0);
                    lnyPropertyType.setVisibility(View.GONE);
                } else {
                    imgPropertyType.setRotation(180);
                    lnyPropertyType.setVisibility(View.VISIBLE);
                }
                break;*/
            case R.id.lnyFacilitiesClick:
                if (imgFacilities.getRotation() == 180) {
                    imgFacilities.setRotation(0);
                    lnyFacilities.setVisibility(View.GONE);
                } else {
                    imgFacilities.setRotation(180);
                    lnyFacilities.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.lnyHotelServiceClick:
                if (imgHotelService.getRotation() == 180) {
                    imgHotelService.setRotation(0);
                    lnyHotelService.setVisibility(View.GONE);
                } else {
                    imgHotelService.setRotation(180);
                    lnyHotelService.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.txtApplyFilters:
                getFilterApplyAPI();
                break;
        }
    }

    private void getFilterApplyAPI() {
        if(chekOne.isChecked()) {
            starList.add("1");
        }
        if(chekTwo.isChecked()) {
            starList.add("2");
        }
        if(chekThree.isChecked()) {
            starList.add("3");
        }
        if(chekFour.isChecked()) {
            starList.add("4");
        }
        if(chekFive.isChecked()) {
            starList.add("5");
        }

        SharedPrefs.setInt(SharedPrefs.minPrice,Integer.parseInt(Content.MinPrice));
        SharedPrefs.setInt(SharedPrefs.maxPrice,Integer.parseInt(Content.MaxPrice));
        SharedPrefs.setArrayList(SharedPrefs.rateId, starList);
        SharedPrefs.setArrayList(SharedPrefs.termId, termList);

        String starRate="",termStr="";
        if(starList.size()!=0) {
            Iterator iterator = starList.iterator();
            while (iterator.hasNext()) {
                Object data = iterator.next();
                starRate += data + ",";
            }
        }
        if(termList.size()!=0) {
            Iterator iterator1 = termList.iterator();
            while (iterator1.hasNext()) {
                Object data = iterator1.next();
                termStr += data + ",";
            }
        }

        String rate ="";
        String term="";
        if(starRate.length()>0)
            rate=starRate.substring(0,starRate.length()-1);
        if (termStr.length()>0)
            term=termStr.substring(0,termStr.length()-1);
        Intent intent=new Intent();
        intent.putExtra("star",rate);
        intent.putExtra("terms",term);
        setResult(1,intent);
        finish();
    }

    private void resetData() {
        txtMinPrice.setText("\u20B9" + minPrice);
        txtMaxPrice.setText("\u20B9" + maxPrice);
        seekPriceRange.setRange(Float.valueOf(minPrice), Float.valueOf(maxPrice));
        seekPriceRange.setProgress(Float.valueOf(minPrice), Float.valueOf(maxPrice));

        chekOne.setChecked(false);
        chekTwo.setChecked(false);
        chekThree.setChecked(false);
        chekFour.setChecked(false);
        chekFive.setChecked(false);
        arrayTerm= SharedPrefs.getArrayList(SharedPrefs.termId);
        if(arrayTerm!=null) {
            Iterator iterator = arrayTerm.iterator();
            while (iterator.hasNext()) {
                Object data = iterator.next();
                for (int i = 0; i < termsPropertytypeList.size(); i++) {
                    if (String.valueOf(termsPropertytypeList.get(i).getId()).equals(data)) {
                        termsPropertytypeList.get(i).setCheck(false);
                    }
                }
                for (int i = 0; i < hotelServiceTypeList.size(); i++) {
                    if (String.valueOf(hotelServiceTypeList.get(i).getId()).equals(data)) {
                        hotelServiceTypeList.get(i).setCheck(false);
                    }
                }
                for (int i = 0; i < FacilitiesTypeList.size(); i++) {
                    if (String.valueOf(FacilitiesTypeList.get(i).getId()).equals(data)) {
                        FacilitiesTypeList.get(i).setCheck(false);
                    }
                }
            }
        }
        if (termsPropertytypeList.size() != 0) {
            propertyTypeAdapter.notifyDataSetChanged();
        }
        if (FacilitiesTypeList.size() != 0) {
            facilitiesFilterAdapter.notifyDataSetChanged();
        }
        if (hotelServiceTypeList.size() != 0) {
            hotelServiceAdapter.notifyDataSetChanged();
        }
        SharedPrefs.clearOnlyFilter();


    }

    @Override
    public void getTermIdAdd(int id, int type,String name) {
        if(termList!=null){
            if(type==1){
                termList.add(id+"");
            }
            if(type==2){
                Iterator iterator = termList.iterator();
                while (iterator.hasNext()) {
                    Object data = iterator.next();
                    if(termList.contains(data)){
                        termList.remove(data);
                        break;
                    }
                }
            }

        }
    }
}
