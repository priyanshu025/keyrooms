package com.keyroom.Activity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.keyroom.R;
import com.keyroom.Utility.SharedPrefs;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CityNameActivity extends AppCompatActivity {

    String CityName = "";
    ImageView imgBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cityname);
        initView();
    }

    private void initView() {
        imgBack = findViewById(R.id.imgBack);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getResources().getString(R.string.mapKey));
        }

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_support_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        autocompleteFragment.setHint("Search New Location");

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                CityName = place.getName();
                SharedPrefs.setValue(SharedPrefs.City, CityName);
                setLatLongi(CityName);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setLatLongi(String cityname) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocationName(cityname, 1);
           /* double latitude = addresses.get(0).getLatitude();
            double longitude = addresses.get(0).getLongitude();*/
            finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
