package com.keyroom.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.keyroom.Model.HappyClientModel;
import com.keyroom.R;
import java.util.ArrayList;
import uz.jamshid.library.ExactRatingBar;

public class HappyClientAdapter extends RecyclerView.Adapter<HappyClientAdapter.ViewHolder> {

    Context mContext;
    ArrayList<HappyClientModel.Data> happyclientList = new ArrayList<>();

    public HappyClientAdapter(Context context, ArrayList<HappyClientModel.Data> happyclientList) {
        this.mContext = context;
        this.happyclientList = happyclientList;
    }

    @NonNull
    @Override
    public HappyClientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_happyclient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HappyClientAdapter.ViewHolder holder, int position) {
        HappyClientModel.Data happyClientModel = happyclientList.get(position);
        if (happyClientModel.getImage_url()!= null)
        Glide.with(mContext).load(happyClientModel.getImage_url()).placeholder(R.drawable.login_user).error(R.drawable.login_user).into(holder.imgUser);
        if (happyClientModel.getName()!= null)
        holder.txtName.setText(happyClientModel.getName());
        if (happyClientModel.getDesc()!= null)
        holder.txtDescription.setText(happyClientModel.getDesc());
        if (happyClientModel.getNumber_star()!= null)
        holder.rate.setStarCount(happyClientModel.getNumber_star());
        if (happyClientModel.getNumber_star()!= null)
        holder.rate.setStar(happyClientModel.getNumber_star());
    }

    @Override
    public int getItemCount() {
        return happyclientList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgUser;
        TextView txtName;
        ExactRatingBar rate;
        TextView txtDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.imgUserProfile);
            txtName = itemView.findViewById(R.id.txtName);
            rate = itemView.findViewById(R.id.rate);
            txtDescription = itemView.findViewById(R.id.txtDescription);
        }
    }
}
