package com.example.anshultech.miriambakery.Adapters;

import android.app.SearchManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.anshultech.miriambakery.Bean.BakeryStepsListBean;
import com.example.anshultech.miriambakery.R;

import java.util.ArrayList;
import java.util.List;

public class FavoriteRecyclerViewAdapter extends RecyclerView.Adapter<FavoriteRecyclerViewAdapter.FavouriteViewHolder> {

    private Context mContext;
    private List<BakeryStepsListBean> mBakeryStepsListBeans;
    ;

    public FavoriteRecyclerViewAdapter(Context context, List<BakeryStepsListBean> bakeryStepsListBeans) {
        this.mContext = context;
        this.mBakeryStepsListBeans = bakeryStepsListBeans;

    }

    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recipiedetailslistlayout, viewGroup, false);
        return new FavouriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteViewHolder favouriteViewHolder, int position) {
        String recipeDesciption = new String();
        favouriteViewHolder.ingridentsQuantityTextView.setVisibility(View.GONE);
        favouriteViewHolder.ingridentsMeasureTextView.setVisibility(View.GONE);

        if (mBakeryStepsListBeans.size() - 1 == position) {
            favouriteViewHolder.recipieDetailsHorizontalBar.setVisibility(View.GONE);
        }
        favouriteViewHolder.recipieDetailsDesciptionTextView.setText(mBakeryStepsListBeans.get(position).getShortDescription());

    }

    @Override
    public int getItemCount() {
        return mBakeryStepsListBeans.size();
    }

    public class FavouriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView recipieDetailsDesciptionTextView;
        View recipieDetailsHorizontalBar;
        TextView ingridentsQuantityTextView;
        TextView ingridentsMeasureTextView;
        ImageView favoriteImageViewDetailsList;
        RelativeLayout favoriteImageRelativeLayout;

        public FavouriteViewHolder(@NonNull View itemView) {
            super(itemView);
            recipieDetailsDesciptionTextView = (TextView) itemView.findViewById(R.id.recipieDetailsDesciptionTextView);
            recipieDetailsHorizontalBar = (View) itemView.findViewById(R.id.recipieDetailsHorizontalBar);
            ingridentsQuantityTextView = (TextView) itemView.findViewById(R.id.ingridentsQuantityTextView);
            ingridentsMeasureTextView = (TextView) itemView.findViewById(R.id.ingridentsMeasureTextView);
            favoriteImageViewDetailsList = (ImageView) itemView.findViewById(R.id.favoriteImageViewDetailsList);
            favoriteImageRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.favoriteImageRelativeLayout);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public void updateFavoriteAdapterList(List<BakeryStepsListBean> bakeryStepsListBeans){
        mBakeryStepsListBeans=bakeryStepsListBeans;
        notifyDataSetChanged();

    }
    public void addData(BakeryStepsListBean bakeryStepsListBean){
        this.mBakeryStepsListBeans.add(bakeryStepsListBean);
    }

}