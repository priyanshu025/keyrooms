package com.keyroom.Utility;

import android.content.Context;
import android.content.SharedPreferences;

public class WelcomeScreenPrefs {

    public static String USER_REMEMBER = "user_remember";
    private static SharedPreferences sharedPreferences;
    private Context mContext = null;


    public WelcomeScreenPrefs(Context mContext) {
        this.mContext = mContext;
        sharedPreferences = mContext.getSharedPreferences("MyPrefsWelcome", Context.MODE_PRIVATE);
    }

    public static boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public static void setBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void clearData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
