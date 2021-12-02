package com.keyroom.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.keyroom.InterFace.NavegationClick;
import com.keyroom.R;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    ArrayList<String> arrayList=new ArrayList<>();
    NavegationClick navegationClick;

    public CustomAdapter(Context context, ArrayList<String> arrayList,NavegationClick navegationClick) {
        this.context = context;
        this.arrayList = arrayList;
        this.navegationClick = navegationClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_item_suggest, parent, false);
        return new CustomAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.text.setText(arrayList.get(position));
        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navegationClick.NavegationClick(position, arrayList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (arrayList.size()>7){
            return 7;
        }
        else {
            return arrayList.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text=itemView.findViewById(R.id.text);
        }
    }
}
