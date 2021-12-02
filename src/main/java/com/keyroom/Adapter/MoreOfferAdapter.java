package com.keyroom.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.keyroom.Activity.HotelDetailsActivity;
import com.keyroom.Activity.HotelDiscountedActivity;
import com.keyroom.Model.SearchModel;
import com.keyroom.R;

import java.util.ArrayList;

public class MoreOfferAdapter extends RecyclerView.Adapter<MoreOfferAdapter.ViewHolder> {
    Context mContext;
    ArrayList<SearchModel.Rows> serachHotelModel = new ArrayList<>();
    GalleryOfferImageAdapter galleryImageAdapter;

    public MoreOfferAdapter(Context mContext, ArrayList<SearchModel.Rows> serachHotelModel) {
        this.mContext = mContext;
        this.serachHotelModel = serachHotelModel;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_searchresult, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull  MoreOfferAdapter.ViewHolder holder, int position) {
        SearchModel.Rows serachModel = serachHotelModel.get(position);
        if (serachModel.getTitle() != null)
             holder.txthotelName.setText(serachModel.getTitle());
        if (serachModel.getReview_detail() != null) {
            if (serachModel.getReview_detail().getScore_total() != null)
                 holder.txtHotelRate.setText(serachModel.getReview_detail().getScore_total());
            if (serachModel.getReview_detail().getTotal_review() != null) {
                // ((ViewHolderDemo) holder).txtTotalReview.setText(serachModel.getReview_detail().getTotal_review());
            }
        }
        if (serachModel.getAddress() != null)
             holder.txtHotelAddress.setText(serachModel.getAddress());
        if (serachModel.getPrice() == null) {
             holder.txtPrice.setText("\u20B9" + "0");
        } else
            holder.txtPrice.setText("\u20B9" + serachModel.getPrice());

        if (serachModel.isIs_sold()) {
            holder.imgSoldOut.setVisibility(View.VISIBLE);
        } else {
            holder.imgSoldOut.setVisibility(View.GONE);
        }


        holder.rcyGallery.setOnFlingListener(null);
        if (serachModel.getGallery().size() != 0) {
            holder.rcyGallery.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            galleryImageAdapter = new GalleryOfferImageAdapter(mContext, serachModel.getGallery(), serachModel);
            holder.rcyGallery.setAdapter(galleryImageAdapter);

            SnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView( holder.rcyGallery);
        }


         holder.lnyHotelDetils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, HotelDiscountedActivity.class);
                it.putExtra("slug", serachModel.getSlug());
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() {
        return serachHotelModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txthotelName;
        TextView txtHotelRate;
        TextView txtHotelAddress;
        TextView txtPrice;
        RecyclerView rcyGallery;
        LinearLayout lnyHotelDetils;
        ImageView imgUnWish;
        ImageView imgWish;
        TextView imgSoldOut;
        TextView imgPrimum;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txthotelName = itemView.findViewById(R.id.txthotelName);
            txtHotelRate = itemView.findViewById(R.id.txtHotelRate);
            txtHotelAddress = itemView.findViewById(R.id.txtHotelAddress);
            txtPrice = itemView.findViewById(R.id.txtPrice);

            rcyGallery = itemView.findViewById(R.id.rcyGallery);
            lnyHotelDetils = itemView.findViewById(R.id.lnyHotelDetils);
            imgUnWish = itemView.findViewById(R.id.imgUnWish);
            imgWish = itemView.findViewById(R.id.imgWish);
            imgSoldOut = itemView.findViewById(R.id.imgSoldOut);
            imgPrimum = itemView.findViewById(R.id.imgPrimum);
        }
    }
}
