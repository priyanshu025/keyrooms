package com.keyroom.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.keyroom.Activity.HotelDetailsActivity;
import com.keyroom.Activity.LoginActivity;
import com.keyroom.Activity.SearchResultActivity;
import com.keyroom.InterFace.AddWishListHotel;
import com.keyroom.InterFace.OnLoadMoreListener;
import com.keyroom.InterFace.RemoveWishListHotel;
import com.keyroom.Model.SearchModel;
import com.keyroom.R;
import com.keyroom.Utility.SharedPrefs;

import java.util.ArrayList;

public class SearchResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    ArrayList<SearchModel.Rows> serachHotelModel = new ArrayList<>();
    GalleryImageAdapter galleryImageAdapter;
    BannerImageAdapter bannerImageAdapter;
    AddWishListHotel addWishListHotel;
    RemoveWishListHotel removeWishListHotel;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private final int visibleThreshold = 1;
    int PAGE_SIZE = 2;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;



    public SearchResultAdapter(Context context, ArrayList<SearchModel.Rows> serachHotelModel, AddWishListHotel addWishListHotel, RemoveWishListHotel removeWishListHotel, RecyclerView recyclerView) {
        this.mContext = context;
        this.serachHotelModel = serachHotelModel;
        this.addWishListHotel = addWishListHotel;
        this.removeWishListHotel = removeWishListHotel;
        onLoadMoreListener = (OnLoadMoreListener) mContext;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int visibleItemCount = linearLayoutManager.getChildCount();
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                    if (!loading) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE) {
                            if (serachHotelModel.size() > 5) {
                                if ((linearLayoutManager.findLastVisibleItemPosition() >= (getItemCount() - 2)) && onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                            }
                            loading = true;
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return serachHotelModel.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_searchresult, parent, false);
            vh = new ViewHolderDemo(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderDemo) {
            SearchModel.Rows serachModel = serachHotelModel.get(position);
            if (serachModel.getTitle() != null)
                ((ViewHolderDemo) holder).txthotelName.setText(serachModel.getTitle());
            if (serachModel.getReview_detail() != null) {
                if (serachModel.getReview_detail().getScore_total() != null)
                    ((ViewHolderDemo) holder).txtHotelRate.setText(serachModel.getReview_detail().getScore_total());
                if (serachModel.getReview_detail().getTotal_review() != null) {
                    // ((ViewHolderDemo) holder).txtTotalReview.setText(serachModel.getReview_detail().getTotal_review());
                }
            }
            if (serachModel.getAddress() != null)
                ((ViewHolderDemo) holder).txtHotelAddress.setText(serachModel.getAddress());
            if (serachModel.getPrice() == null) {
                ((ViewHolderDemo) holder).txtPrice.setText("\u20B9" + "0");
            } else
                ((ViewHolderDemo) holder).txtPrice.setText("\u20B9" + serachModel.getPrice());
            if (serachModel.getSale_price()!=null){
                ((ViewHolderDemo) holder).txtSalePrice.setText("\u20B9" + serachModel.getSale_price());
            }
            if (serachModel.isIs_sold()) {
                ((ViewHolderDemo) holder).imgSoldOut.setVisibility(View.VISIBLE);
            } else {
                ((ViewHolderDemo) holder).imgSoldOut.setVisibility(View.GONE);
            }
            ((ViewHolderDemo) holder).rcyGallery.setOnFlingListener(null);
            if (serachModel.getGallery().size() != 0) {
                ((ViewHolderDemo) holder).rcyGallery.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                galleryImageAdapter = new GalleryImageAdapter(mContext, serachModel.getGallery(), serachModel);
                ((ViewHolderDemo) holder).rcyGallery.setAdapter(galleryImageAdapter);

                SnapHelper snapHelper = new PagerSnapHelper();
                snapHelper.attachToRecyclerView(((ViewHolderDemo) holder).rcyGallery);
            } else {
                ((ViewHolderDemo) holder).rcyGallery.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                bannerImageAdapter = new BannerImageAdapter(mContext, serachModel.getBannerImage(), serachModel);
                ((ViewHolderDemo) holder).rcyGallery.setAdapter(bannerImageAdapter);
                SnapHelper snapHelper = new PagerSnapHelper();
                snapHelper.attachToRecyclerView(((ViewHolderDemo) holder).rcyGallery);
            }

            ((ViewHolderDemo) holder).lnyHotelDetils.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, HotelDetailsActivity.class);
                    it.putExtra("slug", serachModel.getSlug());
                    mContext.startActivity(it);
                }
            });

            if (serachModel.isWishList()) {
                ((ViewHolderDemo) holder).imgUnWish.setVisibility(View.GONE);
                ((ViewHolderDemo) holder).imgWish.setVisibility(View.VISIBLE);
            } else {
                ((ViewHolderDemo) holder).imgUnWish.setVisibility(View.VISIBLE);
                ((ViewHolderDemo) holder).imgWish.setVisibility(View.GONE);
            }

            ((ViewHolderDemo) holder).imgUnWish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SharedPrefs.getValue(SharedPrefs.SKIP_LOGIN).equals("yes")) {
                        loginScreen();
                    } else {
                        ((ViewHolderDemo) holder).imgUnWish.setVisibility(View.GONE);
                        ((ViewHolderDemo) holder).imgWish.setVisibility(View.VISIBLE);
                        addWishListHotel.AddWishListHotel(serachModel.getId());
                    }
                }
            });

            if (serachModel.getIs_featured() != null){
                if (serachModel.getIs_featured() == 1) {
                    ((ViewHolderDemo) holder).imgPrimum.setVisibility(View.VISIBLE);
                } else {
                    ((ViewHolderDemo) holder).imgPrimum.setVisibility(View.GONE);
                }
            }

            ((ViewHolderDemo) holder).imgWish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ViewHolderDemo) holder).imgUnWish.setVisibility(View.VISIBLE);
                    ((ViewHolderDemo) holder).imgWish.setVisibility(View.GONE);
                    removeWishListHotel.RemoveWishListHotel(serachModel.getId());
                }
            });
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    private void loginScreen() {
        Intent it = new Intent(mContext, LoginActivity.class);
        mContext.startActivity(it);
    }

    @Override
    public int getItemCount() {
        return serachHotelModel.size();
    }

    class ViewHolderDemo extends RecyclerView.ViewHolder {
        TextView txthotelName;
        TextView txtHotelRate;
        TextView txtHotelAddress;
        TextView txtPrice;
        RecyclerView rcyGallery;
        LinearLayout lnyHotelDetils;
        ImageView imgUnWish;
        ImageView imgWish;
        TextView imgSoldOut;
        TextView imgPrimum;
        TextView txtSalePrice;


        public ViewHolderDemo(@NonNull View itemView) {
            super(itemView);
            txthotelName = itemView.findViewById(R.id.txthotelName);
            txtHotelRate = itemView.findViewById(R.id.txtHotelRate);
            txtHotelAddress = itemView.findViewById(R.id.txtHotelAddress);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            rcyGallery = itemView.findViewById(R.id.rcyGallery);
            lnyHotelDetils = itemView.findViewById(R.id.lnyHotelDetils);
            imgUnWish = itemView.findViewById(R.id.imgUnWish);
            imgWish = itemView.findViewById(R.id.imgWish);
            imgSoldOut = itemView.findViewById(R.id.imgSoldOut);
            imgPrimum = itemView.findViewById(R.id.imgPrimum);
            txtSalePrice=itemView.findViewById(R.id.txtSalePrice);
        }
    }

    class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        }
    }
}
