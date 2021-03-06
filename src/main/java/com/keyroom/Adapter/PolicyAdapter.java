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

public class PolicyAdapter extends RecyclerView.Adapter<PolicyAdapter.ViewHolder> {

    Context mContext;
    ArrayList<HotelDetailsModel.Policy> policies = new ArrayList<>();

    public PolicyAdapter(Context context, ArrayList<HotelDetailsModel.Policy> policyArrayList) {
        this.mContext = context;
        this.policies = policyArrayList;
    }

    @NonNull
    @Override
    public PolicyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_policy, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PolicyAdapter.ViewHolder holder, int position) {
        HotelDetailsModel.Policy policy = policies.get(position);
        if (policy.getTitle() != null)
            holder.txtPolicyTitle.setText(policy.getTitle());
        if (policy.getContent() != null)
            holder.txtPolicyContent.setText(policy.getContent());
    }

    @Override
    public int getItemCount() {
        return policies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtPolicyTitle, txtPolicyContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPolicyTitle = itemView.findViewById(R.id.txtPolicyTitle);
            txtPolicyContent = itemView.findViewById(R.id.txtPolicyContent);
        }
    }
}
