package com.keyroom.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.keyroom.Model.LocationCityModel;
import com.keyroom.Model.ReviewModel;
import com.keyroom.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import uz.jamshid.library.ExactRatingBar;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    Context mContext;
    ArrayList<ReviewModel.Review> arrayList = new ArrayList<>();
//    private final int limit = 10;

    public ReviewAdapter(Context mContext, ArrayList<ReviewModel.Review> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(mContext).inflate(R.layout.rating_and_review_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    ReviewModel.Review reviewModel=arrayList.get(position);

    if (reviewModel.getContent()!=null){
        holder.txtReview.setText(reviewModel.getContent());

    }
    if (reviewModel.getAuthor().getName()!=null){
        holder.txtUsername.setText(reviewModel.getAuthor().getName());
    }
        if (reviewModel.getRate_number()!=null){
            Log.e("rate_number==>","@@"+reviewModel.getRate_number());
            holder.exactRatingBar.setStar(Float.parseFloat(reviewModel.getRate_number()));

        }

    }

    @Override
    public int getItemCount() {
       /* if (arrayList.size() > limit) {
            return limit;
        } else {
            return arrayList.size();
        }*/

        return arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtUsername;
        TextView txtReview;
        ExactRatingBar exactRatingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUsername=itemView.findViewById(R.id.user_name);
            txtReview=itemView.findViewById(R.id.txtReview);
            exactRatingBar=itemView.findViewById(R.id.rating_bar);

        }
    }
}
