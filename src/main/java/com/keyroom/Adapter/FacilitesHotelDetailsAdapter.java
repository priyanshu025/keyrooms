package com.keyroom.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.keyroom.Model.HotelDetailsModel;
import com.keyroom.R;

import java.util.ArrayList;

public class FacilitesHotelDetailsAdapter extends RecyclerView.Adapter<FacilitesHotelDetailsAdapter.ViewHolder> {

    Context mContext;
    ArrayList<HotelDetailsModel.Facilities> facilities = new ArrayList<>();

    public FacilitesHotelDetailsAdapter(Context context, ArrayList<HotelDetailsModel.Facilities> facilitiesArrayList) {
        this.mContext = context;
        this.facilities = facilitiesArrayList;
    }

    @NonNull
    @Override
    public FacilitesHotelDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_facilitieshoteldetails, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacilitesHotelDetailsAdapter.ViewHolder holder, int position) {
        HotelDetailsModel.Facilities facilitiesModel = facilities.get(position);
        if (facilitiesModel.getName() != null)
            holder.txtFacilitesName.setText(facilitiesModel.getName());
        if (facilitiesModel.getImage() != null)
            Glide.with(mContext).load(facilitiesModel.getImage()).into(holder.imgFacilites);
    }

    @Override
    public int getItemCount() {
        return facilities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFacilites;
        TextView txtFacilitesName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFacilites = itemView.findViewById(R.id.imgFacilites);
            txtFacilitesName = itemView.findViewById(R.id.txtFacilitesName);
        }
    }
}
