package com.keyroom.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.keyroom.Model.Terms;
import com.keyroom.R;

import java.util.ArrayList;

public class FacilitiesFilterAdapter extends RecyclerView.Adapter<FacilitiesFilterAdapter.ViewHolder> {

    Context mContext;
    ArrayList<Terms> FacilitiesTypeList = new ArrayList<>();
    PropertyTypeAdapter.TermsArray termsArray;

    public FacilitiesFilterAdapter(Context context, ArrayList<Terms> FacilitiesTypeList) {
        this.mContext = context;
        this.FacilitiesTypeList = FacilitiesTypeList;
        termsArray = (PropertyTypeAdapter.TermsArray) mContext;
    }


    @NonNull
    @Override
    public FacilitiesFilterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_filters, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacilitiesFilterAdapter.ViewHolder holder, int position) {
        Terms terms = FacilitiesTypeList.get(position);
        holder.checkItem.setChecked(terms.isCheck());
        if (terms.getName() != null)
            holder.txtName.setText(terms.getName());
        holder.checkItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    termsArray.getTermIdAdd(FacilitiesTypeList.get(holder.getAdapterPosition()).getId(), 1, terms.getName());
                } else
                    termsArray.getTermIdAdd(FacilitiesTypeList.get(holder.getAdapterPosition()).getId(), 2, terms.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return FacilitiesTypeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkItem;
        TextView txtName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkItem = itemView.findViewById(R.id.checkItem);
            txtName = itemView.findViewById(R.id.txtName);
        }
    }
}
