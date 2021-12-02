package com.keyroom.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.keyroom.Model.SearchModel;
import com.keyroom.R;

import java.util.ArrayList;

public class FacilitesAdapter extends RecyclerView.Adapter<FacilitesAdapter.ViewHolder> {

    Context mContext;
    ArrayList<SearchModel.Facilities> facilitiesList = new ArrayList<>();

    public FacilitesAdapter(Context context, ArrayList<SearchModel.Facilities> facilitiesList) {
        this.mContext = context;
        this.facilitiesList = facilitiesList;
    }

    @NonNull
    @Override
    public FacilitesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_facilites, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacilitesAdapter.ViewHolder holder, int position) {
        SearchModel.Facilities facilitiesModel = facilitiesList.get(position);
        if (facilitiesModel.getName() != null)
            holder.txtFacilitesName.setText(facilitiesModel.getName());
    }

    @Override
    public int getItemCount() {
        return facilitiesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtFacilitesName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFacilitesName = itemView.findViewById(R.id.txtFacilitesName);
        }
    }
}
