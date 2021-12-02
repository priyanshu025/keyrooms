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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.keyroom.Activity.HotelDetailsActivity;
import com.keyroom.InterFace.OnLoadMoreListener;
import com.keyroom.InterFace.RemoveWishListHotel;
import com.keyroom.Model.WishListModel;
import com.keyroom.R;

import java.util.ArrayList;

import uz.jamshid.library.ExactRatingBar;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {

    Context mContext;
    ArrayList<WishListModel.Wishlist> wishlistArrayList = new ArrayList<>();
    RemoveWishListHotel removeWishListHotel;
    private boolean loading;
    private final OnLoadMoreListener onLoadMoreListener;

    public WishListAdapter(Context context, ArrayList<WishListModel.Wishlist> wishlistArrayList, RemoveWishListHotel removeWishListHotel, RecyclerView recyclerView, OnLoadMoreListener onLoadMoreListener) {
        this.mContext = context;
        this.wishlistArrayList = wishlistArrayList;
        this.removeWishListHotel = removeWishListHotel;
        this.onLoadMoreListener = onLoadMoreListener;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView,
                                       int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int visibleItemCount = linearLayoutManager.getChildCount();
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                    if (!loading) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                                && firstVisibleItemPosition >= 0) {
                            if (wishlistArrayList.size() > 5) {
                                if ((linearLayoutManager.findLastVisibleItemPosition() >= (getItemCount() - 2)) && onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                            }
                            loading = true;
                        }
                    }
                }
            });
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @NonNull
    @Override
    public WishListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_wiahlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishListAdapter.ViewHolder holder, int position) {
        WishListModel.Wishlist wishlist = wishlistArrayList.get(position);
        if (wishlist.getTitle() != null)
            holder.txthotelName.setText(wishlist.getTitle());
        if (wishlist.getAddress() != null)
            holder.txtHotelAddress.setText(wishlist.getAddress());
        if (wishlist.getPrice() != null)
            holder.txtPrice.setText("₹" + wishlist.getPrice());
        else
            holder.txtPrice.setText("₹0");
        if (wishlist.getStar_rate() != null)
            holder.rate.setStar(Float.valueOf(wishlist.getStar_rate()));

        if (wishlist.getGallery().size() != 0) {
            Glide.with(mContext).load(wishlist.getGallery().get(position).getThumb()).placeholder(R.drawable.error_image).error(R.drawable.error_image).into(holder.imgHotel);
        } else {
            Glide.with(mContext).load(wishlist.getBannerImage().get(position).getThumb()).placeholder(R.drawable.error_image).error(R.drawable.error_image).into(holder.imgHotel);
        }

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeWishListHotel.RemoveWishListHotel(wishlist.getId());
            }
        });

        holder.lnyHotelDetils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, HotelDetailsActivity.class);
                it.putExtra("slug", wishlist.getSlug());
                mContext.startActivity(it);
            }
        });

    }

    @Override
    public int getItemCount() {
        return wishlistArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHotel;
        TextView txthotelName;
        TextView txtHotelAddress;
        TextView txtPrice;
        ExactRatingBar rate;
        ImageView imgDelete;
        LinearLayout lnyHotelDetils;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHotel = itemView.findViewById(R.id.imgHotel);
            txthotelName = itemView.findViewById(R.id.txthotelName);
            txtHotelAddress = itemView.findViewById(R.id.txtHotelAddress);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            rate = itemView.findViewById(R.id.rate);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            lnyHotelDetils = itemView.findViewById(R.id.lnyHotelDetils);
        }
    }
}
