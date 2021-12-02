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

public class HotelPolicyAdapter extends RecyclerView.Adapter<HotelPolicyAdapter.ViewHolder> {

    Context mContext;
    ArrayList<SearchModel.HotelPolicy> hotelPolicies = new ArrayList<>();

    public HotelPolicyAdapter(Context context, ArrayList<SearchModel.HotelPolicy> hotelPolicies) {
        this.hotelPolicies = hotelPolicies;
        this.mContext = context;
    }

    @NonNull
    @Override
    public HotelPolicyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_policy, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelPolicyAdapter.ViewHolder holder, int position) {
        SearchModel.HotelPolicy hotelPolicy = hotelPolicies.get(position);
        holder.txtPolicyTitle.setText(hotelPolicy.getName());
        holder.txtPolicyContent.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return hotelPolicies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtPolicyTitle;
        TextView txtPolicyContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPolicyTitle = itemView.findViewById(R.id.txtPolicyTitle);
            txtPolicyContent = itemView.findViewById(R.id.txtPolicyContent);
        }
    }
}
