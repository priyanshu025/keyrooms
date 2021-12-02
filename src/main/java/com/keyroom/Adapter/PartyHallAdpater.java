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
import com.keyroom.Activity.PartyHallDetailsActivity;
import com.keyroom.Activity.SearchResultActivity;
import com.keyroom.Model.BanquetsAndPartyHallModel;
import com.keyroom.Model.PartyHallModel;
import com.keyroom.R;

import java.util.ArrayList;

public class PartyHallAdpater extends RecyclerView.Adapter<PartyHallAdpater.ViewHolder> {

    private final int limit = 5;
    Context mContext;
    ArrayList<PartyHallModel.Party> partyArrayList = new ArrayList<>();

    public PartyHallAdpater(Context mContext, ArrayList<PartyHallModel.Party> partyArrayList) {
        this.mContext = mContext;
        this.partyArrayList = partyArrayList;
    }

    @NonNull
    @Override
    public PartyHallAdpater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.banquets_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PartyHallAdpater.ViewHolder holder, int position) {
        PartyHallModel.Party partyModel = partyArrayList.get(position);
        if (partyModel.getTitle() != null) {
            holder.txthotelName.setText(partyModel.getTitle());
        }
        if (partyModel.getAddress() != null) {
            holder.txtHotelAddress.setText(partyModel.getAddress());
        }
        if (partyModel.getPrice() != null) {
            holder.txtPrice.setText("\u20B9" +partyModel.getPrice());
        }
        if (partyModel.getGallery().size() != 0) {
            Glide.with(mContext).load(partyModel.getParty_banner_image_id().get(position).getThumb()).placeholder(R.drawable.error_image).error(R.drawable.error_image).into(holder.imgHotel);
        }
        holder.cardViewMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchResultActivity.isCheckCallApi = true;
                Intent it = new Intent(mContext, PartyHallDetailsActivity.class);
                it.putExtra("slug", partyModel.getSlug());
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (partyArrayList.size() > limit) {
            return limit;
        } else {
            return partyArrayList.size();
        }

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
