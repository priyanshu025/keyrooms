package com.keyroom.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.keyroom.Model.HotelDetailsModel;
import com.keyroom.R;

import java.util.ArrayList;

import uz.jamshid.library.ExactRatingBar;

public class RoomDetailAdapter extends RecyclerView.Adapter<RoomDetailAdapter.ViewHolder> {

    Context mContext;
    ArrayList<HotelDetailsModel.Rooms> roomsArrayList = new ArrayList<>();

    public RoomDetailAdapter(Context context, ArrayList<HotelDetailsModel.Rooms> roomsArrayList) {
        this.mContext = context;
        this.roomsArrayList = roomsArrayList;
    }

    @NonNull
    @Override
    public RoomDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_roomdetails, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomDetailAdapter.ViewHolder holder1, int position) {
        RoomDetailAdapter.ViewHolder holder = holder1;
        HotelDetailsModel.Rooms row = roomsArrayList.get(position);
        //BookingModel.Booking_Service bookingModel = roomsArrayList.get(position).getBooking_service();
        if (row.getTitle() != null)
            holder.txthotelName.setText(row.getTitle());
        if (row.getPrice() != null)
            holder.txtPrice.setText("₹" + row.getPrice());
        else
            holder.txtPrice.setText("₹0");
        if (row.getImage() != null) {
            Glide.with(mContext).load(row.getImage()).placeholder(R.drawable.error_image).error(R.drawable.error_image).into(holder.imgHotel);
        }
    }

    @Override
    public int getItemCount() {
        return roomsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHotel;
        TextView txthotelName;
        Spinner txtRoomDetail;
        TextView txtPrice;
        ExactRatingBar rate;
        LinearLayout lnyHotelDetils;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHotel = itemView.findViewById(R.id.imgHotel);
            txthotelName = itemView.findViewById(R.id.txthotelName);
            txtRoomDetail = itemView.findViewById(R.id.txtRoomDetail);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            rate = itemView.findViewById(R.id.rate);
            lnyHotelDetils = itemView.findViewById(R.id.lnyHotelDetils);
        }
    }
}
