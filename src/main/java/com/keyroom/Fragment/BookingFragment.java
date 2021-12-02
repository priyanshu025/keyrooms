package com.keyroom.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.keyroom.R;

public class BookingFragment extends Fragment implements View.OnClickListener {
    TextView item1;
    TextView item2;
    TextView item3;
    TextView select;

    FrameLayout flLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_booking, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View view) {
        flLayout = view.findViewById(R.id.flLayout);
        item1=view.findViewById(R.id.item1);
        item2=view.findViewById(R.id.item2);
        item3=view.findViewById(R.id.item3);
        select=view.findViewById(R.id.select);

        item1.setOnClickListener(this);
        item2.setOnClickListener(this);
        item3.setOnClickListener(this);

        BookingDetailsFragment bookingDetailsFragment = new BookingDetailsFragment();
        Bundle args = new Bundle();
        args.putString("status", "");
        bookingDetailsFragment.setArguments(args);
        selectFragment(bookingDetailsFragment);

    }

    public void selectFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                R.anim.design_bottom_sheet_slide_in,
                R.anim.design_bottom_sheet_slide_out
        ).replace(R.id.flLayout, fragment, fragment.getClass().getSimpleName()).commit();
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.item1){
            select.animate().x(0).setDuration(100);
            item1.setTextColor(Color.WHITE);
            item2.setTextColor(getResources().getColor(R.color.dot_color));
            item3.setTextColor(getResources().getColor(R.color.dot_color));

                BookingDetailsFragment bookingDetailsFragment = new BookingDetailsFragment();
                Bundle args = new Bundle();
                args.putString("status", "");
                bookingDetailsFragment.setArguments(args);
                selectFragment(bookingDetailsFragment);

        }else if (v.getId()==R.id.item2){
            int size=item2.getWidth();
            item1.setTextColor(getResources().getColor(R.color.dot_color));
            item2.setTextColor(Color.WHITE);
            item3.setTextColor(getResources().getColor(R.color.dot_color));
            select.animate().x(size).setDuration(100);
                BookingDetailsFragment bookingDetailsFragment = new BookingDetailsFragment();
                Bundle args = new Bundle();
                args.putString("status", "confirmed");
                bookingDetailsFragment.setArguments(args);
                selectFragment(bookingDetailsFragment);


        }else if (v.getId()==R.id.item3){
            item1.setTextColor(getResources().getColor(R.color.dot_color));
            item2.setTextColor(getResources().getColor(R.color.dot_color));
            item3.setTextColor(Color.WHITE);

            int size=item2.getWidth()*2;
            select.animate().x(size).setDuration(100);

                BookingDetailsFragment bookingDetailsFragment = new BookingDetailsFragment();
                Bundle args = new Bundle();
                args.putString("status", "cancelled");
                bookingDetailsFragment.setArguments(args);
                selectFragment(bookingDetailsFragment);
        }
    }
}
