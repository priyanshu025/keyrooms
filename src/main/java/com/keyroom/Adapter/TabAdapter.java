package com.keyroom.Adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.keyroom.Fragment.BookingFragment;
import com.keyroom.Fragment.HomeFragment;
import com.keyroom.Fragment.WishListFragment;

public class TabAdapter extends FragmentPagerAdapter {

    Context context;
    int totalTabs;


    public TabAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm, totalTabs);
        this.context = c;
        this.totalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                BookingFragment bookingFragment = new BookingFragment();
                return bookingFragment;
            case 2:
                WishListFragment wishListFragment = new WishListFragment();
                return wishListFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
