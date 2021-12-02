package com.keyroom.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.keyroom.Adapter.PagerViewAdpater;
import com.keyroom.R;

public class NewBookingActivity extends AppCompatActivity {

    TextView item1,item2,item3;
    ViewPager viewPager;
    PagerViewAdpater pagerViewAdapter;
    TextView select;
    ImageView img_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_booking);
        item1=findViewById(R.id.item1);
        item2=findViewById(R.id.item2);
        item3=findViewById(R.id.item3);
        select=findViewById(R.id.select);
        img_back=findViewById(R.id.img_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);

            }
        });

        item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });

        viewPager=findViewById(R.id.fragment);
        pagerViewAdapter=new PagerViewAdpater(getSupportFragmentManager());
        viewPager.setAdapter(pagerViewAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                onTabChange(position);

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void onTabChange(int position) {

        if (position==0){
            select.animate().x(0).setDuration(100);
            item1.setTextColor(Color.WHITE);
            item2.setTextColor(getResources().getColor(R.color.dot_color));
            item3.setTextColor(getResources().getColor(R.color.dot_color));


        }
        if (position==1)
        {
            int size=item2.getWidth();
            item1.setTextColor(getResources().getColor(R.color.dot_color));
            item2.setTextColor(Color.WHITE);
            item3.setTextColor(getResources().getColor(R.color.dot_color));
            select.animate().x(size).setDuration(100);
        }
        if (position==2){
            item1.setTextColor(getResources().getColor(R.color.dot_color));
            item2.setTextColor(getResources().getColor(R.color.dot_color));
            item3.setTextColor(Color.WHITE);

            int size=item2.getWidth()*2;
            select.animate().x(size).setDuration(100);
        }
    }
}