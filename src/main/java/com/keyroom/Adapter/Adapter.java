package com.keyroom.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.keyroom.Activity.SearchResultActivity;
import com.keyroom.Model.BannerModel;
import com.keyroom.Model.LocationCityModel;
import com.keyroom.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends PagerAdapter {
    private final Context context;
    ArrayList<BannerModel.Offers> bannerModelArrayList=new ArrayList<>();

    public Adapter(Context context, ArrayList<BannerModel.Offers> bannerModelArrayList) {
        this.context = context;
        this.bannerModelArrayList = bannerModelArrayList;
    }

    @Override
    public int getCount() {
        return bannerModelArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        BannerModel.Offers bannerModel=bannerModelArrayList.get(position);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view= layoutInflater.inflate(R.layout.item,container,false);

        ImageView imageView=view.findViewById(R.id.image);

        if (bannerModel.getOffer_image_id()!=null){
            Glide.with(context).load(bannerModel.getOffer_image_id().get(position).getLarge()).placeholder(R.drawable.error_image).error(R.drawable.error_image).into(imageView);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bannerModel.getUrl()!= null) {
                    Intent it = new Intent(context, SearchResultActivity.class);
                    it.putExtra("cityName", bannerModel.getUrl());
                    context.startActivity(it);
                }
            }
        });
        container.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
