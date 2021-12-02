package com.keyroom.Activity;

import android.content.Context;
import android.os.StrictMode;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.keyroom.Utility.SharedPrefs;
import com.keyroom.Utility.WelcomeScreenPrefs;

public class KeyRooms extends MultiDexApplication {

    private Context mContext = null;
    public SharedPrefs sharedPrefs = null;
    public WelcomeScreenPrefs welcomeScreenPrefs = null;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        mContext = getApplicationContext();
        sharedPrefs = new SharedPrefs(mContext);
        SharedPrefs.clearOnlyFilter();
        welcomeScreenPrefs = new WelcomeScreenPrefs(mContext);

        StrictMode.VmPolicy builder = new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build();
        StrictMode.setVmPolicy(builder);

    }


}
