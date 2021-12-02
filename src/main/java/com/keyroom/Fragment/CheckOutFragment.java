package com.keyroom.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import java.util.List;


public class CheckOutFragment extends Fragment {
    CalendarView calendarView;
    Calendar calendar;
    static Calendar calendar1;
    DateFormat dateFormatDay;
    CheckInFragment.CheckInCheckOutDate checkInCheckOutDate;
    List<Calendar> getDays;
    List<EventDay> mEventDays = new ArrayList<>(1);
    List<Calendar> selectDateList = new ArrayList<>(2);
    EventDay eventDay, eventDay1;
    CheckInFragment.AllView allView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_checkout, container, false);
        calendarView = (CalendarView) rootView.findViewById(R.id.calendarView);
        dateFormatDay = new SimpleDateFormat("EEE, dd MMM");

        try {
            checkInCheckOutDate = (CheckInFragment.CheckInCheckOutDate) getContext();
            allView = (CheckInFragment.AllView) getContext();
            calendar = Calendar.getInstance();
            calendar.setTime(CheckInFragment.checkInDateFormat);

        }catch (Exception e){
            Toast.makeText(getContext(), "Please select check In Date First", Toast.LENGTH_SHORT).show();
        }
        calendarView.setMinimumDate(calendar);

        selectDateList.add(0, calendar);
        Calendar cal = calendar;
        cal.setTime(calendar.getTime());
        selectDateList.add(1, cal);
        calendarView.setSelectedDates(selectDateList);
        calendarView.setSelected(true);
        getDays = calendarView.getSelectedDates();
        eventDay = new EventDay(getDays.get(0), getResources().getDrawable(R.drawable.ic_arrow_right_cal, null));
        eventDay1 = new EventDay(getDays.get(1), getResources().getDrawable(R.drawable.quantum_ic_arrow_back_grey600_24, null));
        mEventDays.add(0, eventDay);
        mEventDays.add(1, eventDay1);
        calendarView.setEvents(mEventDays);
        try {
            calendarView.setDate(calendar);
            calendarView.setDate(getDays.get(0));
            calendarView.setDate(getDays.get(1));
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }

        clickEvent();
        Toast.makeText(getContext(), "Select Check Out Date", Toast.LENGTH_SHORT).show();
        return rootView;
    }

    private void clickEvent() {
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();
                mEventDays.remove(1);
                selectDateList.remove(1);
                selectDateList.add(1, clickedDayCalendar);
                calendarView.setSelectedDates(selectDateList);
                getDays = calendarView.getSelectedDates();
                eventDay = new EventDay(clickedDayCalendar,getResources().getDrawable(R.drawable.quantum_ic_arrow_back_grey600_24,null));
                mEventDays.add(1, eventDay);
                calendarView.setEvents(mEventDays);

                calendar1 = clickedDayCalendar;
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    calendarView.setDate(calendar1);
                    if (CheckInFragment.checkInDateFormat.before(clickedDayCalendar.getTime())) {
                        Log.e("strCheckOutDate==>", "@@@" + dateFormat.format(clickedDayCalendar.getTime()));
                        ChekInCheckOutActivity.strCheckOutDate = dateFormat.format(clickedDayCalendar.getTime());
                        ChekInCheckOutActivity.isCorrecOutDate = true;
                        checkInCheckOutDate.allDates(clickedDayCalendar, calendar1, 2);
                        getActivity().finish();
                    } else {
                        ChekInCheckOutActivity.isCorrecOutDate = false;
                        ChekInCheckOutActivity.strCheckOutDate = "";
                        Toast.makeText(getContext(), "Please select correct date", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}