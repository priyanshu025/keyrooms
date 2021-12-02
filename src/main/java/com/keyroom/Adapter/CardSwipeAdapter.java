package com.keyroom.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.keyroom.Model.CardSwipeModel;
import com.keyroom.R;

import java.util.List;

public class CardSwipeAdapter extends PagerAdapter {
    private List<CardSwipeModel> models;
    private LayoutInflater layoutInflater;
    private Context context;

    public CardSwipeAdapter(List<CardSwipeModel> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=layoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.card_swipe_item_layout,container,false);
        ImageView imageView;
        TextView textdesc;

        imageView=view.findViewById(R.id.image);
        textdesc=view.findViewById(R.id.textdesc);

        imageView.setImageResource(models.get(position).getImage());
        textdesc.setText(models.get(position).getText());

        container.addView(view,0);



        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
