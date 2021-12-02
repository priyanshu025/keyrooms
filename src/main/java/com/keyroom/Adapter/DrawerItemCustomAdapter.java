package com.keyroom.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.keyroom.InterFace.NavegationClick;
import com.keyroom.R;

import java.util.ArrayList;

public class DrawerItemCustomAdapter extends RecyclerView.Adapter<DrawerItemCustomAdapter.ViewHolder> {

    Context mContext;
    ArrayList<String> itemName = new ArrayList<>();
    NavegationClick navegationClick;
    ArrayList<Integer> icon = new ArrayList<>();

    public DrawerItemCustomAdapter(Context context, ArrayList<String> itemName, NavegationClick navegationClick, ArrayList<Integer> icons) {
        this.mContext = context;
        this.itemName = itemName;
        this.icon = icons;
        this.navegationClick = navegationClick;
    }

    @NonNull
    @Override
    public DrawerItemCustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_item_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DrawerItemCustomAdapter.ViewHolder holder, final int position) {
        holder.textViewName.setText(itemName.get(position));
        holder.imgIcon.setImageResource(icon.get(position));
        holder.lnyMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navegationClick.NavegationClick(position, itemName.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        LinearLayout lnyMain;
        ImageView imgIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            lnyMain = itemView.findViewById(R.id.lnyMain);
            imgIcon = itemView.findViewById(R.id.imgIcon);
        }
    }
}
