package com.keyroom.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.keyroom.Activity.BanquetsDetailsActivity;
import com.keyroom.Activity.SearchResultActivity;
import com.keyroom.Model.BanquetsAndPartyHallModel;
import com.keyroom.R;

import java.util.ArrayList;


public class BanquetsAdapter extends RecyclerView.Adapter<BanquetsAdapter.ViewHolder> {
    Context mContext;
    ArrayList<BanquetsAndPartyHallModel.Banquets> banquetsArrayList = new ArrayList<>();

    public BanquetsAdapter(Context mContext, ArrayList<BanquetsAndPartyHallModel.Banquets> banquetsArrayList) {
        this.mContext = mContext;
        this.banquetsArrayList = banquetsArrayList;
    }

    @NonNull
    @Override
    public BanquetsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.see_all_banquets_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull BanquetsAdapter.ViewHolder holder, int position) {
        BanquetsAndPartyHallModel.Banquets banquetModel = banquetsArrayList.get(position);
        if (banquetModel.getTitle() != null) {
            holder.txthotelName.setText(banquetModel.getTitle());
        }
        if (banquetModel.getAddress() != null) {
            holder.txtHotelAddress.setText(banquetModel.getAddress());
        }
        if (banquetModel.getPrice() != null) {
            holder.txtPrice.setText("\u20B9" +banquetModel.getPrice());
        }
        if (banquetModel.getGallery().size() != 0) {
            Glide.with(mContext).load(banquetModel.getParty_banner_image_id().get(position).getThumb()).placeholder(R.drawable.error_image).error(R.drawable.error_image).into(holder.imgHotel);
        }
        holder.cardViewMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchResultActivity.isCheckCallApi = true;
                Intent it = new Intent(mContext, BanquetsDetailsActivity.class);
                it.putExtra("slug", banquetModel.getSlug());
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() {
            return banquetsArrayList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHotel;
        TextView txthotelName;
        TextView txtHotelAddress;
        TextView txtPrice;
        CardView cardViewMain;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHotel = itemView.findViewById(R.id.imgHotel);
            txthotelName = itemView.findViewById(R.id.txthotelName);
            txtHotelAddress = itemView.findViewById(R.id.txtHotelAddress);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            cardViewMain = itemView.findViewById(R.id.cardViewMain);

        }
    }
}
