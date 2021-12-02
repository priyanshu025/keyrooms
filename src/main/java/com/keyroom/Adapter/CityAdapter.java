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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.keyroom.Activity.SearchResultActivity;
import com.keyroom.Model.LocationCityModel;
import com.keyroom.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {

    Context mContext;
    ArrayList<LocationCityModel> cityNameList = new ArrayList<>();

    public CityAdapter(Context context, ArrayList<LocationCityModel> cityList) {
        this.mContext = context;
        this.cityNameList = cityList;
    }

    @NonNull
    @Override
    public CityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_citylist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityAdapter.ViewHolder holder, int position) {
        LocationCityModel locationCityModel = cityNameList.get(position);
        if (locationCityModel.getName()!= null)
        holder.txtCityName.setText(locationCityModel.getName());
        if (locationCityModel.getImage_link()!= null)
            Glide.with(mContext).load(locationCityModel.getImage_link()).placeholder(R.drawable.error_image).error(R.drawable.error_image).into(holder.imgCity);
        holder.lnyCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationCityModel.getName()!= null) {
                    Intent it = new Intent(mContext, SearchResultActivity.class);
                    it.putExtra("cityName", locationCityModel.getName());
                    mContext.startActivity(it);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cityNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgCity;
        TextView txtCityName;
        LinearLayout lnyCity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCity = itemView.findViewById(R.id.imgCity);
            txtCityName = itemView.findViewById(R.id.txtCityName);
            lnyCity = itemView.findViewById(R.id.lnyCity);
        }
    }
}
