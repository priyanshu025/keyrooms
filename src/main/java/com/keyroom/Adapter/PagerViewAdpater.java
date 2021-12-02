package com.keyroom.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.keyroom.Fragment.AllBookingFragment;
import com.keyroom.Fragment.CancelledFragment;
import com.keyroom.Fragment.ConfirmFragment;

public class PagerViewAdpater extends FragmentPagerAdapter {
    public PagerViewAdpater(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        Fragment fragment=null;
        switch (position){
            case 0:
                fragment=new AllBookingFragment();
                break;
            case 1:
                fragment=new ConfirmFragment();
                break;
            case 2:
                fragment=new CancelledFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    
}
