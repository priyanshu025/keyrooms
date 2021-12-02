package com.keyroom.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.keyroom.Model.HotelDetailsModel;
import com.keyroom.R;

import java.util.ArrayList;

public class HotelDetailsImageViewAdapter extends RecyclerView.Adapter<HotelDetailsImageViewAdapter.ViewHolder> {

    Context mContext;
    ArrayList<HotelDetailsModel.Gallery> galleries = new ArrayList<>();

    public HotelDetailsImageViewAdapter(Context context, ArrayList<HotelDetailsModel.Gallery> galleryArrayList) {
        this.mContext = context;
        this.galleries = galleryArrayList;
    }

    @NonNull
    @Override
    public HotelDetailsImageViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_hoteldetailsimageview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelDetailsImageViewAdapter.ViewHolder holder, int position) {
        HotelDetailsModel.Gallery gallery = galleries.get(position);
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
