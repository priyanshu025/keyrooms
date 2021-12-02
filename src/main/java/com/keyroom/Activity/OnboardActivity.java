package com.keyroom.Activity;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.keyroom.Adapter.CardSwipeAdapter;
import com.keyroom.Model.CardSwipeModel;
import com.keyroom.R;

import java.util.ArrayList;
import java.util.List;

public class OnboardActivity extends AppCompatActivity {

    ViewPager viewPager;
    CardSwipeAdapter adapter;
    List<CardSwipeModel> models;
    Integer[] color = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    private Button btnLeft, btnRight;
    private LinearLayout dotsLayout;
    private TextView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard);


        models = new ArrayList<>();
        models.add(new CardSwipeModel(R.drawable.a1, "Flexible Check-in Check-out"));
        models.add(new CardSwipeModel(R.drawable.a2, "Clean Room"));
        models.add(new CardSwipeModel(R.drawable.a3, "100% Assure Booking"));
        adapter = new CardSwipeAdapter(models, this);
        viewPager = findViewById(R.id.viewPager);
        btnLeft = findViewById(R.id.btnLeft);
        btnRight = findViewById(R.id.btnRight);
        dotsLayout = findViewById(R.id.dotsLayout);
        addDots(0);

        viewPager.setAdapter(adapter);
        btnRight.setOnClickListener(v -> {
        //if button text is next we will go to next page of viewpager
            if (btnRight.getText().toString().equals("Next")) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            } else {
                //we will start Splash screen activity
                startActivity(new Intent(OnboardActivity.this, SplashScreenActivity.class));
                finish();
            }
        });

        btnLeft.setOnClickListener(v -> {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 3);
        });

        Integer[] color_temp = {
                getResources().getColor(R.color.color_main)
                , getResources().getColor(R.color.color_main)
                , getResources().getColor(R.color.color_main)

        };

        color = color_temp;
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position < (adapter.getCount() - 1) && position < (color.length - 1)) {
                    viewPager.setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset, color[position], color[position + 1]));

                    if (btnRight.getText().toString().equals("Finish")) {
                        startActivity(new Intent(OnboardActivity.this, SplashScreenActivity.class));
                        finish();
                    }
                } else {
                    viewPager.setBackgroundColor(color[color.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {
                addDots(position);

                if (position == 0) {
                    btnLeft.setVisibility(View.VISIBLE);
                    btnLeft.setEnabled(true);
                    btnRight.setText("Next");
                } else if (position == 1) {
                    btnLeft.setVisibility(View.GONE);
                    btnLeft.setEnabled(false);
                    btnRight.setText("Next");
                } else {
                    btnLeft.setVisibility(View.GONE);
                    btnLeft.setEnabled(false);
                    btnRight.setText("Finish");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    private void addDots(int position){
        dotsLayout.removeAllViews();
        dots=new TextView[3];
        for (int i=0;i<dots.length;i++){
            dots[i]=new TextView(this);
            // this html dots creates dot
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.dot_color));
            dotsLayout.addView(dots[i]);
        }

        // ok now lets change the selected dot color
        if (dots.length>0){
            dots[position].setTextColor(getResources().getColor(R.color.color_blue));

        }
    }
}