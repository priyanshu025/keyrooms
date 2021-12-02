package com.keyroom.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.keyroom.Activity.HotelDetailsActivity;
import com.keyroom.Model.SearchModel;
import com.keyroom.R;

import java.util.ArrayList;

public class GalleryImageAdapter extends RecyclerView.Adapter<GalleryImageAdapter.ViewHolder> {

    Context mContext;
    ArrayList<SearchModel.Gallery> galleriesImages = new ArrayList<>();
    SearchModel.Rows serachHotelModel;

    public GalleryImageAdapter(Context context, ArrayList<SearchModel.Gallery> galleriesImages, SearchModel.Rows serachHotelModel) {
        this.mContext = context;
        this.galleriesImages = galleriesImages;
        this.serachHotelModel = serachHotelModel;
    }

    @NonNull
    @Override
    public GalleryImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_galleryimages, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryImageAdapter.ViewHolder holder, int position) {
        SearchModel.Gallery gallery = galleriesImages.get(position);
        if (gallery.getLarge() != null)
            Glide.with(mContext).load(gallery.getLarge()).placeholder(R.drawable.error_image).error(R.drawable.error_image).into(holder.imgHotel);

        holder.imgHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, HotelDetailsActivity.class);
                it.putExtra("slug", serachHotelModel.getSlug());
                mContext.startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() {
        return galleriesImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHotel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHotel = itemView.findViewById(R.id.imgHotel);
        }
    }
}
