package com.keyroom.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.keyroom.Activity.BookingetailsAndUserInfoActivity;
import com.keyroom.Activity.HotelDetailsActivity;
import com.keyroom.Activity.RateUsAndWriteReviewActivity;
import com.keyroom.InterFace.OnLoadMoreListener;
import com.keyroom.Model.BookingModel;
import com.keyroom.R;
import com.keyroom.Utility.BookingHistoryDiffCallback;

import java.util.ArrayList;
import java.util.List;

import uz.jamshid.library.ExactRatingBar;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    Context mContext;
    ArrayList<BookingModel.Row> bookingModelArrayList = new ArrayList<>();
    CancelBooking cancelBooking;
    private boolean loading;
    private final OnLoadMoreListener onLoadMoreListener;

    public BookingAdapter(Context context, ArrayList<BookingModel.Row> bookingModelArrayList, CancelBooking cancelBooking, RecyclerView recyclerView, OnLoadMoreListener onLoadMoreListener) {
        this.mContext = context;
        this.bookingModelArrayList = bookingModelArrayList;
        this.cancelBooking = cancelBooking;
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
                            if (bookingModelArrayList.size() > 5) {
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
    public BookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_booking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingAdapter.ViewHolder holder, int position) {
        BookingModel.Row row = bookingModelArrayList.get(position);
        BookingModel.Booking_Service bookingModel = bookingModelArrayList.get(position).getBooking_service();
        if (bookingModel.getTitle() != null)
            holder.txthotelName.setText(bookingModel.getTitle());
        if (bookingModel.getAddress() != null)
            holder.txtHotelAddress.setText(bookingModel.getAddress());
        if (row.getTotal() == null) {
            holder.txtPrice.setText("₹0");
        } else
            holder.txtPrice.setText("₹" + row.getTotal());
        if (bookingModel.getStar_rate() != null)
            holder.rate.setStar(bookingModel.getStar_rate());

        if (row.getStatus().equals("confirmed")) {
            holder.txtCancel.setVisibility(View.VISIBLE);
            holder.txtRateUs.setVisibility(View.VISIBLE);
        } else if (row.getStatus().equals("check out")) {
            holder.txtCancel.setVisibility(View.GONE);
            holder.txtRateUs.setVisibility(View.VISIBLE);
        } else {
            holder.txtCancel.setVisibility(View.GONE);
            holder.txtRateUs.setVisibility(View.VISIBLE);
        }
        if (bookingModel.getBanner_image_id_link() != null) {
            Glide.with(mContext).load(bookingModel.getBanner_image_id_link()).placeholder(R.drawable.error_image).error(R.drawable.error_image).into(holder.imgHotel);
        }
        holder.lnyHotelDetils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, BookingetailsAndUserInfoActivity.class);
                it.putExtra("code", row.getCode());
                mContext.startActivity(it);
            }
        });
        holder.txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelBooking.cancelBooking(bookingModelArrayList.get(holder.getAdapterPosition()).getCode(), bookingModelArrayList.get(holder.getAdapterPosition()).getId());


            }
        });
        holder.txtRateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, RateUsAndWriteReviewActivity.class).putExtra("hotel_id", bookingModel.getId()));
            }
        });
        holder.bookAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookingModel.getSlug()!=null){
                    Intent it = new Intent(mContext, HotelDetailsActivity.class);
                    it.putExtra("slug", bookingModel.getSlug());
                    mContext.startActivity(it);
                }
            }
        });

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            Bundle o = (Bundle) payloads.get(0);
            for (String key : o.keySet()) {
                if (key.equals("status")) {
                    if (bookingModelArrayList.get(position).getStatus().equals("confirmed")) {
                        holder.txtCancel.setVisibility(View.VISIBLE);
                    } else {
                        holder.txtCancel.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return bookingModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHotel;
        TextView txthotelName;
        TextView txtHotelAddress;
        TextView txtPrice;
        ExactRatingBar rate;
        LinearLayout lnyHotelDetils;
        TextView txtCancel, txtRateUs;
        TextView bookAgain;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHotel = itemView.findViewById(R.id.imgHotel);
            txthotelName = itemView.findViewById(R.id.txthotelName);
            txtHotelAddress = itemView.findViewById(R.id.txtHotelAddress);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            rate = itemView.findViewById(R.id.rate);
            lnyHotelDetils = itemView.findViewById(R.id.lnyHotelDetils);
            txtCancel = itemView.findViewById(R.id.txtCancel);
            txtRateUs = itemView.findViewById(R.id.txtRateUs);
            bookAgain=itemView.findViewById(R.id.bookAgain);
        }
    }

    public interface CancelBooking {
        void cancelBooking(String code, Integer id);
    }


    public void updateList(List<BookingModel.Row> list) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new BookingHistoryDiffCallback(this.bookingModelArrayList, list));
        this.bookingModelArrayList.clear();
        this.bookingModelArrayList.addAll(list);
        diffResult.dispatchUpdatesTo(this);
    }
}
