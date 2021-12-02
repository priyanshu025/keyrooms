package com.keyroom.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.keyroom.Activity.HotelDetailsImageViewActivity;
import com.keyroom.Model.BanquetsDetailsModel;
import com.keyroom.Model.HotelDetailsModel;
import com.keyroom.R;

import java.util.ArrayList;

public class BanquetsDetailsAdapter extends RecyclerView.Adapter<BanquetsDetailsAdapter.ViewHolder> {

    Context mContext;
    ArrayList<BanquetsDetailsModel.Gallery> galleries = new ArrayList<>();

    public BanquetsDetailsAdapter(Context mContext, ArrayList<BanquetsDetailsModel.Gallery> galleries) {
        this.mContext = mContext;
        this.galleries = galleries;
    }

    @NonNull
    @Override
    public BanquetsDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_gallerydetailsimges, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BanquetsDetailsAdapter.ViewHolder holder, int position) {
        BanquetsDetailsModel.Gallery gallery = galleries.get(position);
        if (gallery.getLarge() != null) {
            Glide.with(mContext).load(gallery.getLarge()).placeholder(R.drawable.error_image).error(R.drawable.error_image).into(holder.imgHotel);
        }
    }

    @Override
    public int getItemCount() {
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
