package com.keyroom.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.gson.Gson;
import com.keyroom.Activity.HotelDetailsImageViewActivity;
import com.keyroom.Model.HotelDetailsModel;
import com.keyroom.R;

import java.io.Serializable;
import java.util.ArrayList;

public class GalleryDetailsAdapter extends RecyclerView.Adapter<GalleryDetailsAdapter.ViewHolder> {

    Context mContext;
    ArrayList<HotelDetailsModel.Gallery> galleries = new ArrayList<>();

    public GalleryDetailsAdapter(Context context, ArrayList<HotelDetailsModel.Gallery> galleryArrayList) {
        this.mContext = context;
        this.galleries = galleryArrayList;
    }

    @NonNull
    @Override
    public GalleryDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_gallerydetailsimges, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryDetailsAdapter.ViewHolder holder, int position) {
        HotelDetailsModel.Gallery gallery = galleries.get(position);
        if (gallery.getLarge() != null) {
            Glide.with(mContext).load(gallery.getLarge()).placeholder(R.drawable.error_image).error(R.drawable.error_image).into(holder.imgHotel);
            holder.imgHotel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Gson gson = new Gson();
                    String jsonCars = gson.toJson(galleries);
                    Intent intent = new Intent(mContext, HotelDetailsImageViewActivity.class);
                    intent.putExtra("Gallry", jsonCars);
                    mContext.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {

//        return galleries==null?0:Integer.MAX_VALUE;
         return galleries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHotel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHotel = itemView.findViewById(R.id.imgHotel);
        }
    }
}
