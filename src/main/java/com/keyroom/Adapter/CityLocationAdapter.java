package com.keyroom.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.keyroom.Activity.MoreOffersActivity;
import com.keyroom.Model.LocationCityModel;
import com.keyroom.R;
import com.keyroom.SearchCityRecyclerInterface;

import java.util.ArrayList;

public class CityLocationAdapter extends RecyclerView.Adapter<CityLocationAdapter.ViewHolder> {

    Context mContext;
    ArrayList<LocationCityModel> cityNameList = new ArrayList<>();

    public CityLocationAdapter(Context context, ArrayList<LocationCityModel> cityList) {
        this.mContext = context;
        this.cityNameList = cityList;
    }


    @NonNull
    @Override
    public CityLocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_searchcity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityLocationAdapter.ViewHolder holder, int position) {
        LocationCityModel locationCityModel = cityNameList.get(position);
        if (locationCityModel.getName()!= null) {
            holder.txtCityName.setText(locationCityModel.getName());
        }
        holder.lnyCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, MoreOffersActivity.class);
                intent.putExtra("location_id",locationCityModel.getId());
                Log.e("id===============",""+locationCityModel.getId());
                mContext.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return cityNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lnyCity;
        TextView txtCityName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lnyCity = itemView.findViewById(R.id.lnyCity);
            txtCityName = itemView.findViewById(R.id.txtCityName);
        }
    }

    public void filterList(ArrayList<LocationCityModel> filteredList){
        cityNameList=filteredList;
        notifyDataSetChanged();
    }
}
