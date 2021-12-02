package com.keyroom.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.keyroom.Model.HotelDetailsModel;
import com.keyroom.R;

import java.util.ArrayList;

public class ReviewHotelAdapter extends RecyclerView.Adapter<ReviewHotelAdapter.ViewHolder> {

    Context mContext;
    ArrayList<HotelDetailsModel.ReviewList> arrayList=new ArrayList<>();

    public ReviewHotelAdapter(Context mContext, ArrayList<HotelDetailsModel.ReviewList> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ReviewHotelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_hotel_rate_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHotelAdapter.ViewHolder holder, int position) {

        HotelDetailsModel.ReviewList hotelDetailsModel=arrayList.get(position);

        if (hotelDetailsModel.getContent()!=null){
            holder.content.setText(hotelDetailsModel.getContent());
        }
        if (hotelDetailsModel.getAuthor().getName()!=null){
            holder.author.setText(hotelDetailsModel.getAuthor().getName());
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView content;
        TextView author;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            content=itemView.findViewById(R.id.content);
            author=itemView.findViewById(R.id.txt_user);


        }
    }
}
