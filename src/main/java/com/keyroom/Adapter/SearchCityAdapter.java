package com.keyroom.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.keyroom.Activity.SearchHotelActivity;
import com.keyroom.Activity.SearchResultActivity;
import com.keyroom.Model.LocationCityModel;
import com.keyroom.R;
import com.keyroom.SearchCityRecyclerInterface;

import java.util.ArrayList;

public class SearchCityAdapter extends RecyclerView.Adapter<SearchCityAdapter.ViewHolder> {

    Context mContext;
    ArrayList<LocationCityModel> cityNameList = new ArrayList<>();
    private SearchCityRecyclerInterface searchCityRecyclerInterface;

    public SearchCityAdapter(Context context, ArrayList<LocationCityModel> cityList,SearchCityRecyclerInterface searchCityRecyclerInterface) {
        this.mContext = context;
        this.cityNameList = cityList;
        this.searchCityRecyclerInterface=searchCityRecyclerInterface;
    }


    @NonNull
    @Override
    public SearchCityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_searchcity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchCityAdapter.ViewHolder holder, int position) {
        LocationCityModel locationCityModel = cityNameList.get(position);
        if (locationCityModel.getName()!= null)
            holder.txtCityName.setText(locationCityModel.getName());


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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchCityRecyclerInterface.onItemClicked(getAdapterPosition());
                }
            });
        }
    }
}
