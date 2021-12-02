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

public class PropertyTypeAdapter extends RecyclerView.Adapter<PropertyTypeAdapter.ViewHolder> {

    Context mContext;
    ArrayList<Terms> termsPropertyTypeList = new ArrayList<>();
    TermsArray termsArray;

    public PropertyTypeAdapter(Context context, ArrayList<Terms> termsPropertyTypeList) {
        this.mContext = context;
        this.termsPropertyTypeList = termsPropertyTypeList;
        termsArray= (TermsArray) mContext;
    }

    @NonNull
    @Override
    public PropertyTypeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_filters, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyTypeAdapter.ViewHolder holder, int position) {
        Terms terms = termsPropertyTypeList.get(position);
        holder.checkItem.setChecked(terms.isCheck());

        holder.checkItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    termsArray.getTermIdAdd(termsPropertyTypeList.get(holder.getAdapterPosition()).getId(),1,terms.getName());
                }
                else
                    termsArray.getTermIdAdd(termsPropertyTypeList.get(holder.getAdapterPosition()).getId(),2,terms.getName());
            }
        });
        if (terms.getName()!= null)
        holder.txtName.setText(terms.getName());
    }

    @Override
    public int getItemCount() {
        return termsPropertyTypeList.size();
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
    public interface TermsArray{
        void getTermIdAdd(int id, int type, String name);
    }
}
