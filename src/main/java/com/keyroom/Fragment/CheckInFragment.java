package com.keyroom.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.keyroom.Activity.ChekInCheckOutActivity;
import com.keyroom.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CheckInFragment extends Fragment {
    CalendarView calendarView;
    public static Date checkInDateFormat;
    DateFormat dateFormat;
    Calendar calendar;
    ChekInCheckOutActivity activity;
    CheckInCheckOutDate checkInCheckOutDate;
    List<EventDay> mEventDays = new ArrayList<>(1);
    EventDay eventDay;
    AllView allView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_checkin, container, false);
        calendarView = (CalendarView) rootView.findViewById(R.id.calendarView);
        activity = new ChekInCheckOutActivity();
        checkInCheckOutDate = (CheckInCheckOutDate) getContext();
        allView = (AllView) getContext();
        calendar = Calendar.getInstance();

        eventDay = new EventDay(calendar, getResources().getDrawable(R.drawable.ic_arrow_right_cal, null));
        mEventDays.add(0, eventDay);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        calendarView.setMinimumDate(calendar);
        calendarView.setEvents(mEventDays);
        try {

            ChekInCheckOutActivity.strCheckInDate = dateFormat.format(calendar.getTime());
            checkInDateFormat = calendar.getTime();
            ChekInCheckOutActivity.isCorrecInDate = true;
            calendarView.setDate(checkInDateFormat);
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }
        clickEvent();

        return rootView;
    }


    private void clickEvent() {
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();
                mEventDays.remove(0);
                eventDay = new EventDay(clickedDayCalendar, getResources().getDrawable(R.drawable.ic_arrow_right_cal, null));
                mEventDays.add(0, eventDay);
                calendarView.setEvents(mEventDays);
                try {
                    if (new Date().compareTo(clickedDayCalendar.getTime()) == 0) {
                        Log.e("strCheckInDate==>IF", "@@@" + dateFormat.format(clickedDayCalendar.getTime()));
                        ChekInCheckOutActivity.strCheckInDate = dateFormat.format(clickedDayCalendar.getTime());
                        checkInDateFormat = clickedDayCalendar.getTime();
                        calendarView.setDate(checkInDateFormat);
                        ChekInCheckOutActivity.isCorrecInDate = true;

                    } else if (new Date().before(clickedDayCalendar.getTime())) {
                        Log.e("strCheckInDate==>Else IF", "@@@" + dateFormat.format(clickedDayCalendar.getTime()));
                        ChekInCheckOutActivity.strCheckInDate = dateFormat.format(clickedDayCalendar.getTime());
                        checkInDateFormat = clickedDayCalendar.getTime();
                        calendarView.setDate(checkInDateFormat);
                        ChekInCheckOutActivity.isCorrecInDate = true;
                    }
                    allView.getAllView(2);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, 1);
                checkInCheckOutDate.allDates(clickedDayCalendar, cal, 1);


            }
        });
    }


    public interface CheckInCheckOutDate {
        public void allDates(Calendar calendar, Calendar cal, int type);
    }

    public interface AllView {
        public void getAllView(int type);
    }
}