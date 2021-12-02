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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.keyroom.Activity.HotelDetailsActivity;
import com.keyroom.Activity.LoginActivity;
import com.keyroom.Activity.SearchResultActivity;
import com.keyroom.InterFace.AddWishListHotel;
import com.keyroom.InterFace.RemoveWishListHotel;
import com.keyroom.Model.PopularHotelModel;
import com.keyroom.R;
import com.keyroom.Utility.SharedPrefs;

import java.util.ArrayList;

public class PopularHotelAdapter extends RecyclerView.Adapter<PopularHotelAdapter.ViewHolder> {

    Context mContext;
    ArrayList<PopularHotelModel.Rows> popularHotelList = new ArrayList<>();
    private final int limit = 5;
    AddWishListHotel addWishListHotel;
    RemoveWishListHotel removeWishListHotel;

    public PopularHotelAdapter(Context context, ArrayList<PopularHotelModel.Rows> popularHotelList, AddWishListHotel addWishListHotel, RemoveWishListHotel removeWishListHotel) {
        this.mContext = context;
        this.popularHotelList = popularHotelList;
        this.addWishListHotel = addWishListHotel;
        this.removeWishListHotel = removeWishListHotel;
    }

    @NonNull
    @Override
    public PopularHotelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_popularhotel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularHotelAdapter.ViewHolder holder, int position) {
        PopularHotelModel.Rows popularModel = popularHotelList.get(position);
        if (popularModel.getTitle() != null)
            holder.txthotelName.setText(popularModel.getTitle());
        if (popularModel.getAddress() != null)
            holder.txtHotelAddress.setText(popularModel.getAddress());
        if (popularModel.getPrice() != null)
            holder.txtPrice.setText("\u20B9" + popularModel.getPrice());
        if (popularModel.getSale_price()!=null){
            holder.txtSalePrice.setText("\u20B9" + popularModel.getSale_price());
        }
        else
            holder.txtPrice.setText("\u20B9" + "0");

        if (popularModel.isIs_sold()) {
            holder.imgSoldout.setVisibility(View.VISIBLE);
        } else {
            holder.imgSoldout.setVisibility(View.GONE);
        }

        if (popularModel.getGallery().size() != 0) {
            Glide.with(mContext).load(popularModel.getGallery().get(0).getThumb()).placeholder(R.drawable.error_image).error(R.drawable.error_image).into(holder.imgHotel);
        } else {
            Glide.with(mContext).load(popularModel.getBannerImage().get(0).getThumb()).placeholder(R.drawable.error_image).error(R.drawable.error_image).into(holder.imgHotel);
        }

        holder.cardViewMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchResultActivity.isCheckCallApi = true;
                Intent it = new Intent(mContext, HotelDetailsActivity.class);
                it.putExtra("slug", popularModel.getSlug());
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(it);
            }
        });

        holder.imgWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPrefs.getValue(SharedPrefs.SKIP_LOGIN).equals("yes")) {
                    Intent it = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(it);
                } else {
                    holder.imgUnWish.setVisibility(View.VISIBLE);
                    holder.imgWish.setVisibility(View.GONE);
                    addWishListHotel.AddWishListHotel(popularModel.getId());
                }

            }
        });

        holder.imgUnWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPrefs.getValue(SharedPrefs.SKIP_LOGIN).equals("yes")) {
                    Intent it = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(it);
                } else {
                    holder.imgUnWish.setVisibility(View.GONE);
                    holder.imgWish.setVisibility(View.VISIBLE);
                    removeWishListHotel.RemoveWishListHotel(popularModel.getId());
                }

            }
        });

        if (popularModel.isWishList()) {
            holder.imgUnWish.setVisibility(View.VISIBLE);
            holder.imgWish.setVisibility(View.GONE);
        } else {
            holder.imgUnWish.setVisibility(View.GONE);
            holder.imgWish.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (popularHotelList.size() > limit) {
            return limit;
        } else {
            return popularHotelList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHotel;
        ImageView imgWish;
        TextView imgSoldout;
        ImageView imgUnWish;
        TextView txthotelName;
        TextView txtHotelAddress;
        TextView txtPrice,txtSalePrice;
        CardView cardViewMain;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHotel = itemView.findViewById(R.id.imgHotel);
            imgWish = itemView.findViewById(R.id.imgWish);
            imgUnWish = itemView.findViewById(R.id.imgUnWish);
            txthotelName = itemView.findViewById(R.id.txthotelName);
            txtHotelAddress = itemView.findViewById(R.id.txtHotelAddress);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            cardViewMain = itemView.findViewById(R.id.cardViewMain);
            txtSalePrice=itemView.findViewById(R.id.txtSalePrice);
            imgSoldout = itemView.findViewById(R.id.imgSoldOut);
        }
    }
}
