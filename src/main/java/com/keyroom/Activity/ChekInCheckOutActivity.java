package com.keyroom.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.keyroom.Adapter.AdultAdapter;
import com.keyroom.Fragment.AdultFragment;
import com.keyroom.Fragment.CheckInFragment;
import com.keyroom.Fragment.CheckOutFragment;
import com.keyroom.Model.CheckAvabilityModel;
import com.keyroom.Model.RoomModel;
import com.keyroom.Network.API;
import com.keyroom.Network.Config;
import com.keyroom.R;
import com.keyroom.Utility.SharedPrefs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ChekInCheckOutActivity extends AppCompatActivity implements View.OnClickListener, AdultAdapter.TotalAdult, CheckInFragment.CheckInCheckOutDate, CheckInFragment.AllView {

    API api;
    ImageView imgClose;
    TextView txtCheckInApply, txtSelectDateTitle;
    public static String strCheckInDate="", strCheckOutDate="";
    public static boolean isCorrecInDate = false, isCorrecOutDate = false;
    public static int totalAdult = 1, room = 0, totalChildren = 0,maxAdult=1,totalAll=1;
    FrameLayout frameLayout;
    LinearLayout llCheckInLayout, llCheckOutLayout, llAdultLayout;
    public View viewCheckIn, viewCheckOut, viewAdult;
    public TextView txtAdult, txtCheckIn, txtCheckOut, txtRoom;
    ProgressBar progressBar;
    Integer HotelID;
    public Calendar calendar;
    DateFormat dateFormatDay;
    public ArrayList<CheckAvabilityModel.Rooms> arrayListRoom;
    int type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = Config.getClient().create(API.class);
        setContentView(R.layout.activity_checkincheckout);
        HotelID = getIntent().getIntExtra("hotel_id", 0);
        type=getIntent().getIntExtra("type",0);


        imgClose = findViewById(R.id.imgClose);
        txtCheckInApply = findViewById(R.id.txtApply);
        txtSelectDateTitle = findViewById(R.id.txtSelectDateTitle);
        progressBar = findViewById(R.id.progressBar);
        frameLayout = findViewById(R.id.flLayout);
        llCheckInLayout = findViewById(R.id.llCheckInLayout);
        llCheckOutLayout = findViewById(R.id.llCheckOutLayout);
        llAdultLayout = findViewById(R.id.llAdultLayout);
        viewCheckIn = findViewById(R.id.viewCheckIn);
        viewCheckOut = findViewById(R.id.viewCheckOut);
        viewAdult = findViewById(R.id.viewAdult);
        txtAdult = findViewById(R.id.txtAdult);
        txtCheckIn = findViewById(R.id.txtCheckIn);
        txtCheckOut = findViewById(R.id.txtCheckOut);
        txtRoom = findViewById(R.id.txtRoom);

        dateFormatDay = new SimpleDateFormat("EEE, dd MMM");
        calendar = Calendar.getInstance();
       // txtCheckIn.setText(dateFormatDay.format(calendar.getTime()) + "");
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        //txtCheckOut.setText(dateFormatDay.format(calendar.getTime()) + "");
        txtCheckOut.setText("Select Date");
        selectFragment(new CheckInFragment());
        clickEvent();

        txtCheckInApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checkAvability();
                if (HotelID == 0) {
                    setResult(2);
                    finish();
                }
                else{
                    Intent intent = new Intent();
                    intent.putExtra("adult", String.valueOf(totalAdult - 1));
                    setResult(1, intent);
                    finish();
                }
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (type==1){
            getAllView(type);
        }
        if (type==2){
            getAllView(type);
        }
        if (type==3){
            getAllView(type);
        }
    }

    private void clickEvent() {
//        llAdultLayout.setOnClickListener(this);
        llCheckInLayout.setOnClickListener(this);
        llCheckOutLayout.setOnClickListener(this);
    }

    public void selectFragment(Fragment fragment) {

        getSupportFragmentManager().beginTransaction().setCustomAnimations(
                R.anim.slide_from_right,R.anim.slideot_from_left
        ).addToBackStack(null).replace(R.id.flLayout, fragment, fragment.getClass().getSimpleName()).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.txtApply:
                checkAvability();
                break;*/
            case R.id.llCheckInLayout:
                getAllView(1);
                break;
            case R.id.llCheckOutLayout:
                getAllView(2);
                break;
            case R.id.llAdultLayout:
               // getAllView(3);
                break;
            case R.id.imgClose:
                finish();
                break;
            default:
                break;
        }
    }


  /*  public void setText(String CheckInDate, String CheckOutDate, int adult, int room) {
        txtRoom.setText(room + "");
        txtCheckIn.setText(CheckInDate);
        txtCheckOut.setText(CheckOutDate);
        txtAdult.setText(adult + "");
    }*/

    @Override
    public void getTotalAdult(ArrayList<RoomModel> roomModelArrayList) {
        totalAdult = 0;
        for (int i = 0; i < roomModelArrayList.size(); i++) {
            totalAdult += roomModelArrayList.get(i).getAdult();
        }
        txtRoom.setText(roomModelArrayList.size() + " Room");
        txtAdult.setText((totalAdult) + " Adult");
        totalAll=totalAdult+totalChildren;
    }

    @Override
    public void getTotalChild(ArrayList<RoomModel> roomModelArrayList) {
        totalChildren = 0;
        for (int i = 0; i < roomModelArrayList.size(); i++) {
            if(roomModelArrayList.get(i).isHaveChildren()) {
                totalChildren += roomModelArrayList.get(i).getChildren();
            }
        }
        totalAll=totalAdult+totalChildren;
    }

    @Override
    public void allDates(Calendar calendar, Calendar sen, int type) {
        try {
            if (type == 1) {
                Calendar cal = Calendar.getInstance();
                cal = calendar;
                txtCheckIn.setText(dateFormatDay.format(cal.getTime()) + "");
                // calendar.add(Calendar.DAY_OF_YEAR, 1);
                //txtCheckOut.setText(dateFormatDay.format(sen.getTime()) + "");
                txtCheckOut.setText("Select Date");
            } else if (type == 2) {
                txtCheckOut.setText(dateFormatDay.format(sen.getTime()) + "");
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void getAllView(int type) {
        if (type == 1) {
            txtSelectDateTitle.setText("Select Check-In Date");
            viewCheckIn.setVisibility(View.VISIBLE);
            viewCheckOut.setVisibility(View.GONE);
            llAdultLayout.setVisibility(View.GONE);
            txtCheckIn.setTextColor(ContextCompat.getColor(this, R.color.colorBlue));
            txtCheckOut.setTextColor(ContextCompat.getColor(this, R.color.colorbottomIcon));
            txtAdult.setTextColor(ContextCompat.getColor(this, R.color.colorbottomIcon));
            selectFragment(new CheckInFragment());
        } else if (type == 2) {
            txtSelectDateTitle.setText("Select Check-Out Date");
            viewCheckIn.setVisibility(View.GONE);
            viewCheckOut.setVisibility(View.VISIBLE);
            llAdultLayout.setVisibility(View.GONE);
            txtCheckIn.setTextColor(ContextCompat.getColor(this, R.color.colorbottomIcon));
            txtCheckOut.setTextColor(ContextCompat.getColor(this, R.color.colorBlue));
            txtAdult.setTextColor(ContextCompat.getColor(this, R.color.colorbottomIcon));
            selectFragment(new CheckOutFragment());
        } else if (type == 3) {
            txtSelectDateTitle.setText("Select Guest And Room");
            llCheckInLayout.setVisibility(View.GONE);
            llCheckOutLayout.setVisibility(View.GONE);
            llAdultLayout.setVisibility(View.VISIBLE);
            viewAdult.setVisibility(View.VISIBLE);
            txtCheckIn.setTextColor(ContextCompat.getColor(this, R.color.colorbottomIcon));
            txtCheckOut.setTextColor(ContextCompat.getColor(this, R.color.colorbottomIcon));
            txtAdult.setTextColor(ContextCompat.getColor(this, R.color.colorBlue));
            selectFragment(new AdultFragment());
        }

    }
    public void onBackPressed() {
        super.onBackPressed();
       // allDataClear();
    }

    private void allDataClear() {
        SharedPrefs.clearOnlyFilter();
        ChekInCheckOutActivity.strCheckInDate = "";
        ChekInCheckOutActivity.strCheckOutDate = "";
        ChekInCheckOutActivity.totalAdult = 1;
        ChekInCheckOutActivity.totalChildren = 0;
        ChekInCheckOutActivity.totalAll=1;
        ChekInCheckOutActivity.room = 1;
        ChekInCheckOutActivity.isCorrecOutDate = false;
        ChekInCheckOutActivity.isCorrecInDate = false;
        SearchResultActivity.isCheckCallApi = false;
    }

}


