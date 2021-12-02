package com.keyroom.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.keyroom.Model.BanquetsDetailsModel;
import com.keyroom.Model.PartyHallDetailsModel;
import com.keyroom.R;

import java.util.ArrayList;

public class PartyHallDetailsAdapter extends RecyclerView.Adapter<PartyHallDetailsAdapter.ViewHolder>{
    Context mContext;
    ArrayList<PartyHallDetailsModel.Gallery> galleries = new ArrayList<>();

    public PartyHallDetailsAdapter(Context mContext, ArrayList<PartyHallDetailsModel.Gallery> galleries) {
        this.mContext = mContext;
        this.galleries = galleries;
    }

    @NonNull
    @Override
    public PartyHallDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_gallerydetailsimges, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PartyHallDetailsAdapter.ViewHolder holder, int position) {
        PartyHallDetailsModel.Gallery gallery = galleries.get(position);
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
