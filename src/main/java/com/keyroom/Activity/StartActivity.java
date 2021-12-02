package com.keyroom.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.keyroom.R;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        isFirstTime();
    }
    private void isFirstTime() {
        //for checking if the app is running for the very first time
        //we need to save a value to shared prefrences
        SharedPreferences preferences=getApplication().getSharedPreferences("onBoard", Context.MODE_PRIVATE);
        boolean isFirstTime=preferences.getBoolean("isFirstTime",true);
        //default value true
        if (isFirstTime){
            //if its true then its
            SharedPreferences.Editor editor=preferences.edit();
            editor.putBoolean("isFirstTime",false);
            editor.apply();

            //start onboard Activity
            startActivity(new Intent(StartActivity.this,OnboardActivity.class));
            finish();

        }else{
            // start Slpashscreen  Activity
            startActivity(new Intent(StartActivity.this,SplashScreenActivity.class));
            finish();

        }
    }
}