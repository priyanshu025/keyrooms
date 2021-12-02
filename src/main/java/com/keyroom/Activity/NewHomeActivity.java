package com.keyroom.Activity;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.firebase.inappmessaging.FirebaseInAppMessagingClickListener;
import com.google.firebase.inappmessaging.model.Action;
import com.google.firebase.inappmessaging.model.InAppMessage;
import com.keyroom.Fragment.HomeFragment;
import com.keyroom.Fragment.WalletFragment;
import com.keyroom.Fragment.WishListFragment;
import com.keyroom.Network.API;
import com.keyroom.Network.ConfigHader;
import com.keyroom.R;
import com.keyroom.Utility.AppRater;
import com.keyroom.Utility.SharedPrefs;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


public class NewHomeActivity extends AppCompatActivity {

    private final static int MIN_NUMBER_OF_DAYS = 1;//Min number of days 3
    private final static int LAUNCHES_TIME = 1;//Min number of launches 3
    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = null;
    public BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = menuItem -> {

        switch (menuItem.getItemId()) {

            case R.id.bottom_home:
                selectedFragment = new HomeFragment();
                break;

            case R.id.bottom_wallet:
                selectedFragment = new WalletFragment();
                break;


            case R.id.bottom_booking:
                if (SharedPrefs.getValue(SharedPrefs.SKIP_LOGIN).equals("yes")) {
                    loginScreen();
                } else {
                    selectedFragment = null;
                    startActivity(new Intent(NewHomeActivity.this, NewBookingActivity.class));
                }
                break;


            case R.id.bottom_wishlist:
                if (SharedPrefs.getValue(SharedPrefs.SKIP_LOGIN).equals("yes")) {
                    loginScreen();
                } else {
                    selectedFragment = new WishListFragment();
                }
                break;


            case R.id.user_profile:
                if (SharedPrefs.getValue(SharedPrefs.SKIP_LOGIN).equals("yes")) {
                    loginScreen();
                } else {
                    selectedFragment = null;
                    startActivity(new Intent(NewHomeActivity.this, UserProfileActivity.class));
                }
                break;

        }
        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        }
        return true;
    };
    String currentVersion;
    API api;

    public static void showUpdateDialog(final Context mContext, final SharedPreferences.Editor editor) {


        Dialog dialog = new Dialog(mContext);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.update_dialog);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);


        TextView txtNo = dialog.findViewById(R.id.txtNo);
        TextView txtYes = dialog.findViewById(R.id.txtYes);
        TextView txtMsg = dialog.findViewById(R.id.txtMsg);
        TextView txtlater = dialog.findViewById(R.id.txt_later);

        txtMsg.setText("New Update Available please update for better performance");

        txtYes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.keyroom")));
                editor.putBoolean("dontshowagain", true);
                editor.commit();
                dialog.dismiss();
            }
        });

        txtlater.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        txtNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = ConfigHader.getClient().create(API.class);

        setContentView(R.layout.activity_new_home);

        try {
            MyClickListener listener = new MyClickListener();
            FirebaseInAppMessaging.getInstance().addClickListener(listener);

        } catch (Exception e) {
            e.printStackTrace();
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

        Log.e("FirebaseInstanceId==>", "@@" + FirebaseInstanceId.getInstance().getToken());

        AppRater.app_launched(this);


        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        new GetVersionCode().execute();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void loginScreen() {
        Intent it = new Intent(NewHomeActivity.this, LoginActivity.class);
        startActivity(it);
    }

    public void app_launched(Context mContext) {

        SharedPreferences prefs = mContext.getSharedPreferences("update", 0);
        if (prefs.getBoolean("dontshowagain", false)) {
            return;
        }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);
        Log.e("lunch_count", "" + launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_TIME) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    (MIN_NUMBER_OF_DAYS * 24 * 60 * 60 * 1000)) {
                showUpdateDialog(mContext, editor);

            }
        }
        if (launch_count >= 25) {
            editor.putBoolean("dontshowagain", false);
            editor.commit();
        }


        editor.commit();
    }

    public class MyClickListener implements FirebaseInAppMessagingClickListener {

        @Override
        public void messageClicked(InAppMessage inAppMessage, Action action) {
            // Determine which URL the user clicked
            String url = action.getActionUrl();
            if (url.equals("https://play.google.com/store/apps/details?id=com.keyroom")) {

            } else {
                Intent intent = new Intent(NewHomeActivity.this, PagesActivity.class);
                intent.putExtra("pageName","");
                intent.putExtra("url", url);
                startActivity(intent);
            }

        }

    }

    class GetVersionCode extends AsyncTask<Void, String, String> {

        @Override

        protected String doInBackground(Void... voids) {

            String newVersion = null;

            try {
                Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + NewHomeActivity.this.getPackageName() + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get();
                if (document != null) {
                    Elements element = document.getElementsContainingOwnText("Current Version");
                    for (Element ele : element) {
                        if (ele.siblingElements() != null) {
                            Elements sibElemets = ele.siblingElements();
                            for (Element sibElemet : sibElemets) {
                                newVersion = sibElemet.text();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newVersion;

        }


        @Override

        protected void onPostExecute(String onlineVersion) {

            super.onPostExecute(onlineVersion);
            try {

                onlineVersion = onlineVersion.replaceAll("\\.", "");  
                currentVersion = currentVersion.replaceAll("\\.", "");

                if (Integer.parseInt(currentVersion) < Integer.parseInt(onlineVersion)) {

                    Log.e("values", "" + onlineVersion + " " + currentVersion);
                    app_launched(NewHomeActivity.this);

                }
            } catch (Exception e) {
                Log.e("Version_control", "" + e.getMessage());
            }


        }
    }
}