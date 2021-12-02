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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.keyroom.Activity.HotelDetailsActivity;
import com.keyroom.Activity.LoginActivity;
import com.keyroom.InterFace.AddWishListHotel;
import com.keyroom.InterFace.RemoveWishListHotel;
import com.keyroom.Model.NearByHotelModel;
import com.keyroom.R;
import com.keyroom.Utility.SharedPrefs;

import java.util.ArrayList;

import uz.jamshid.library.ExactRatingBar;

public class NearHotelAdapter extends RecyclerView.Adapter<NearHotelAdapter.ViewHolder> {

    Context mContext;
    ArrayList<NearByHotelModel.Rows> nearbyHotel = new ArrayList<>();
    AddWishListHotel addWishListHotel;
    RemoveWishListHotel removeWishListHotel;

    public NearHotelAdapter(Context context, ArrayList<NearByHotelModel.Rows> nearbyHotel,AddWishListHotel addWishListHotel, RemoveWishListHotel removeWishListHotel) {
        this.mContext = context;
        this.nearbyHotel = nearbyHotel;
        this.addWishListHotel = addWishListHotel;
        this.removeWishListHotel = removeWishListHotel;
    }

    @NonNull
    @Override
    public NearHotelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_popularhotel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NearHotelAdapter.ViewHolder holder, int position) {
        NearByHotelModel.Rows nearbyHotelModel = nearbyHotel.get(position);
        if(nearbyHotelModel.getTitle()!=null)
        holder.txthotelName.setText(nearbyHotelModel.getTitle());
        if(nearbyHotelModel.getAddress()!=null)
            holder.txtHotelAddress.setText(nearbyHotelModel.getAddress());
        if(nearbyHotelModel.getPrice()!=null)
            holder.txtPrice.setText("\u20B9" + nearbyHotelModel.getPrice());
        else
            holder.txtPrice.setText("\u20B9" + "0");
        if(nearbyHotelModel.getSale_price()!=null){
            holder.txtSalePrice.setText("\u20B9" + nearbyHotelModel.getSale_price());
        }

        if(nearbyHotelModel.isIs_sold())
        {
            holder.imgSoldOut.setVisibility(View.VISIBLE);
        }else {
            holder.imgSoldOut.setVisibility(View.GONE);
        }
        if (nearbyHotelModel.getGallery().size() != 0) {
            Glide.with(mContext).load(nearbyHotelModel.getGallery().get(0).getThumb()).placeholder(R.drawable.error_image).error(R.drawable.error_image).into(holder.imgHotel);
        } else {
            Glide.with(mContext).load(nearbyHotelModel.getBannerImage().get(0).getThumb()).placeholder(R.drawable.error_image).error(R.drawable.error_image).into(holder.imgHotel);
        }

        holder.cardViewMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, HotelDetailsActivity.class);
                it.putExtra("slug", nearbyHotelModel.getSlug());
                mContext.startActivity(it);
            }
        });

        if(nearbyHotelModel.isWishList()){
            holder.imgUnWish.setVisibility(View.VISIBLE);
            holder.imgWish.setVisibility(View.GONE);
        }else {
            holder.imgUnWish.setVisibility(View.GONE);
            holder.imgWish.setVisibility(View.VISIBLE);
        }

        holder.imgWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPrefs.getValue(SharedPrefs.SKIP_LOGIN).equals("yes")) {
                    Intent it = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(it);
                } else {
                    holder.imgUnWish.setVisibility(View.VISIBLE);
                    holder.imgWish.setVisibility(View.GONE);
                    addWishListHotel.AddWishListHotel(nearbyHotelModel.getId());
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
                    removeWishListHotel.RemoveWishListHotel(nearbyHotelModel.getId());
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return nearbyHotel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHotel;
        ImageView imgWish;
        ImageView imgUnWish;
        TextView imgSoldOut;
        TextView txthotelName;
        TextView txtHotelAddress;
        TextView txtPrice;
        CardView cardViewMain;
        TextView txtSalePrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHotel = itemView.findViewById(R.id.imgHotel);
            imgWish = itemView.findViewById(R.id.imgWish);
            imgUnWish = itemView.findViewById(R.id.imgUnWish);
            txthotelName = itemView.findViewById(R.id.txthotelName);
            txtHotelAddress = itemView.findViewById(R.id.txtHotelAddress);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            cardViewMain = itemView.findViewById(R.id.cardViewMain);
            imgSoldOut = itemView.findViewById(R.id.imgSoldOut);
            txtSalePrice=itemView.findViewById(R.id.txtSalePrice);
        }
    }
}
