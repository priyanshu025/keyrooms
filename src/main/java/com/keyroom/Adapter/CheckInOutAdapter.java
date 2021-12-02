package com.keyroom.Adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.keyroom.Fragment.AdultFragment;
import com.keyroom.Fragment.CheckInFragment;
import com.keyroom.Fragment.CheckOutFragment;


public class CheckInOutAdapter extends FragmentPagerAdapter {

    Context context;
    int totalTabs;


    public CheckInOutAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm, totalTabs);
        this.context = c;
        this.totalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                CheckInFragment checkInFragment = new CheckInFragment();
                return checkInFragment;
            case 1:
                CheckOutFragment checkOutFragment = new CheckOutFragment();
                return checkOutFragment;
            case 2:
                AdultFragment adultFragment = new AdultFragment();
                return adultFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
