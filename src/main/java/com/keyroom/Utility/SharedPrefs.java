package com.keyroom.Utility;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by webclues on 8/26/2016.
 */
public class SharedPrefs {

    public static String Access_Token = "access_token";
    public static String User_id = "user_id";
    public static String First_Name = "first_name";
    public static String Last_Name = "last_name";
    public static String User_Name = "user_name";
    public static String lat = "lat";
    public static String longi = "longi";
    public static String Email = "email";
    public static String Phone_No = "phone_no";
    public static String Country_Code = "country_code";
    public static String Birthday_Date = "birthday_date";
    public static String Birthday_Date_Display = "birthday_date_display";
    public static String City = "city";
    public static String State = "state";
    public static String Country = "country";
    public static String Zip_Code = "zip_code";
    public static String SKIP_LOGIN = "skip_login";
    public static String Address = "address";
//    public static String totalPrice="totalPrice";

    //Filter screen
    public static String minPrice = "minPrice";
    public static String maxPrice = "maxPrice";
    public static String termId = "termId";
    public static String rateId = "rateId";


    private static SharedPreferences sharedPreferences;
//    private static SharedPreferences preferences;
    private Context mContext = null;


    public SharedPrefs(Context mContext) {
        this.mContext = mContext;
        sharedPreferences = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
      //  preferences =mContext.getSharedPreferences("tokenSave", Context.MODE_PRIVATE);
    }

    public static String getValue(String key) {
        return sharedPreferences.getString(key, "0");
    }

    public static void setValue(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static float getFloat(String key) {
        return sharedPreferences.getFloat(key, (float) 0.0);
    }

    public static void setFloat(String key, float value) {
        SharedPreferences.Editor editor = sharedPreferences .edit();
        editor.putFloat(key, value);
        editor.apply();
    }


    public static boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public static void setBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static long getLong(String key) {
        return sharedPreferences.getLong(key, 0);
    }

    public static void setLong(String key, long value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static double getDouble(String key) {
        return Double.longBitsToDouble(sharedPreferences.getLong(key, 0));
    }

    public static void setDouble(String key, double value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, Double.doubleToRawLongBits(value));
        editor.apply();
    }

    public static int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public static int getPageScrip(String key) {
        return sharedPreferences.getInt(key, 25);
    }

    public static void setInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void setArrayList(String key, Set<String> value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(key, (Set<String>) value);
        editor.apply();
    }

    public static Set<String> getArrayList(String key) {
        return sharedPreferences.getStringSet(key, null);
    }

    public static void clearData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

  /*      SharedPreferences.Editor editor1=preferences.edit();
        editor1.clear();
        editor.apply();*/

    }

    public static void clearOnlyFilter() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(SharedPrefs.termId);
        editor.remove(SharedPrefs.rateId);
        editor.remove(SharedPrefs.minPrice);
        editor.remove(SharedPrefs.maxPrice);

        editor.apply();
    }


}
