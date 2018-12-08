package com.example.anshultech.miriambakery.Adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.anshultech.miriambakery.Bean.BakeryRecipiesListBean;
import com.example.anshultech.miriambakery.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.view.View.LAYOUT_DIRECTION_LTR;

public class BakeryRecipiesListRecyclerViewAdapter extends RecyclerView.Adapter<BakeryRecipiesListRecyclerViewAdapter.BakeryRecipiesListHolder> {

    private Context mContext;
    private ArrayList<BakeryRecipiesListBean> mBakeryRecipiesListBeans;
    private BakeryRecipiesListOnClickListener mBakeryRecipiesListOnClickListener;

    public BakeryRecipiesListRecyclerViewAdapter(Context context, ArrayList<BakeryRecipiesListBean> bakeryRecipiesListBeans, BakeryRecipiesListOnClickListener bakeryRecipiesListOnClickListener) {
        this.mContext = context;
        this.mBakeryRecipiesListBeans = bakeryRecipiesListBeans;
        this.mBakeryRecipiesListOnClickListener = bakeryRecipiesListOnClickListener;
    }

    @NonNull
    @Override
    public BakeryRecipiesListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.masterrecipelistlayout, viewGroup, false);

        return new BakeryRecipiesListHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(@NonNull BakeryRecipiesListHolder holder, int position) {

        String recipiName = mBakeryRecipiesListBeans.get(position).getName();
        int servings = mBakeryRecipiesListBeans.get(position).getServings();

        if (mBakeryRecipiesListBeans.get(position).getImage() != null) {
            String imageSource = mBakeryRecipiesListBeans.get(position).getImage().toString();
            if (!imageSource.equalsIgnoreCase("")) {
                Picasso.get().load(imageSource).into(holder.recipiMasterListImageView);
            }
        }
        holder.servingValueTextView.setText(Integer.toString(servings));
        holder.masterListRecipeNameTV.setText(recipiName);
    }

    @Override
    public int getItemCount() {
        return mBakeryRecipiesListBeans.size();
    }

    public class BakeryRecipiesListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView masterListRecipeNameTV;
        TextView servingValueTextView;
        ImageView recipiMasterListImageView;
        RelativeLayout servingRelativeLayout;


        public BakeryRecipiesListHolder(@NonNull View itemView) {
            super(itemView);
            masterListRecipeNameTV = (TextView) itemView.findViewById(R.id.masterListRecipeNameTV);
            servingValueTextView = (TextView) itemView.findViewById(R.id.servingValueTextView);
            recipiMasterListImageView = (ImageView) itemView.findViewById(R.id.recipiMasterListImageView);
            servingRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.servingRelativeLayout);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mBakeryRecipiesListOnClickListener.onRecipiesClickItem(getAdapterPosition(), mBakeryRecipiesListBeans);
        }
    }

    public interface BakeryRecipiesListOnClickListener {
        void onRecipiesClickItem(int position, ArrayList<BakeryRecipiesListBean> bakeryRecipiesListBeans);
    }

    public void clearData() {
        int size  = mBakeryRecipiesListBeans.size();
        mBakeryRecipiesListBeans.clear();
        /*if(size>0) {

            for (int i = 0; i < size; i++) {
                mBakeryRecipiesListBeans.remove(i);

            }*/
        notifyItemRangeRemoved(0, size);
        /*}

    }*/
    }
}