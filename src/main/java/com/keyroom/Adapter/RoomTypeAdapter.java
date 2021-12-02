package com.keyroom.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.keyroom.Model.RoomTypeModel;
import com.keyroom.R;

import java.util.ArrayList;

public class RoomTypeAdapter extends RecyclerView.Adapter<RoomTypeAdapter.ViewHolder> {

    Context mContext;
    ArrayList<RoomTypeModel> roomTypeModels = new ArrayList<>();

    public RoomTypeAdapter(Context context, ArrayList<RoomTypeModel> roomTypeModels) {
        this.mContext = context;
        this.roomTypeModels = roomTypeModels;
    }

    @NonNull
    @Override
    public RoomTypeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_roomtype, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomTypeAdapter.ViewHolder holder, int position) {
        RoomTypeModel roomTypeModel = roomTypeModels.get(position);
        holder.txtRoomType.setText(roomTypeModel.getName() + "*" + roomTypeModel.getNumber());
        holder.txtHotelPrice.setText("" + roomTypeModel.getPrice());
    }

    @Override
    public int getItemCount() {
        return roomTypeModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtRoomType;
        TextView txtHotelPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtRoomType = itemView.findViewById(R.id.txtRoomType);
            txtHotelPrice = itemView.findViewById(R.id.txtHotelPrice);
        }
    }
}
