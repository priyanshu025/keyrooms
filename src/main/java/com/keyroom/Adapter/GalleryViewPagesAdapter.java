package com.keyroom.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.keyroom.Model.SearchModel;
import com.keyroom.R;

import java.util.ArrayList;

public class GalleryViewPagesAdapter extends PagerAdapter {
    LayoutInflater mLayoutInflater;
    Context mContext;
    ArrayList<SearchModel.Gallery> galleriesImages = new ArrayList<>();

    public GalleryViewPagesAdapter(Context context, ArrayList<SearchModel.Gallery> galleriesImages){
        this.mContext = context;
        this.galleriesImages = galleriesImages;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return galleriesImages.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_galleryimages, container, false);
        ImageView imgHotel = itemView.findViewById(R.id.imgHotel);
        SearchModel.Gallery gallery = galleriesImages.get(position);
        if (gallery.getLarge()!= null)
            Glide.with(mContext).load(gallery.getLarge()).placeholder(R.drawable.error_image).error(R.drawable.error_image).into(imgHotel);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

}
