package com.example.anshultech.miriambakery.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.anshultech.miriambakery.Bean.BakeryIngridentsListBean;
import com.example.anshultech.miriambakery.Bean.BakeryStepsListBean;
import com.example.anshultech.miriambakery.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BakeryDetailsRecyclerViewAdapter extends RecyclerView.Adapter<BakeryDetailsRecyclerViewAdapter.BakeryDetailsHolder> {

    private Context mContext;
    private ArrayList<BakeryStepsListBean> mBakeryStepsListBeans;
    private ArrayList<BakeryIngridentsListBean> mBakeryIngridentsListBeans;
    private BakeryDetailsIngredientsOnClickListener mBakeryDetailsIngredientsOnClickListener;
    private BakeryDetailsStepsOnClickListener mBakeryDetailsStepsOnClickListener;
    private String mListType;


    public BakeryDetailsRecyclerViewAdapter(Context lContext, ArrayList<BakeryIngridentsListBean> bakeryIngridentsListBeans,
                                            BakeryDetailsIngredientsOnClickListener bakeryDetailsIngredientsOnClickListener, String listType) {
        this.mContext = lContext;
        this.mBakeryIngridentsListBeans = bakeryIngridentsListBeans;
        this.mBakeryDetailsIngredientsOnClickListener = bakeryDetailsIngredientsOnClickListener;
        this.mListType = listType;
    }

    public BakeryDetailsRecyclerViewAdapter(Context lContext, ArrayList<BakeryStepsListBean> bakeryStepsListBeans,
                                            BakeryDetailsStepsOnClickListener bakeryDetailsStepsOnClickListener, String listType) {
        this.mContext = lContext;
        this.mBakeryStepsListBeans = bakeryStepsListBeans;
        this.mBakeryDetailsStepsOnClickListener = bakeryDetailsStepsOnClickListener;
        this.mListType = listType;
    }


    @NonNull
    @Override
    public BakeryDetailsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recipiedetailslistlayout, viewGroup, false);
        return new BakeryDetailsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BakeryDetailsHolder bakeryDetailsHolder, final int position) {

        String recipeDesciption = new String();
        bakeryDetailsHolder.ingridentsQuantityTextView.setVisibility(View.GONE);
        bakeryDetailsHolder.ingridentsMeasureTextView.setVisibility(View.GONE);
        bakeryDetailsHolder.favoriteImageRelativeLayout.setVisibility(View.GONE);
        if (mListType.equalsIgnoreCase("Steps")) {
            if (mBakeryStepsListBeans.size() - 1 == position) {
                bakeryDetailsHolder.recipieDetailsHorizontalBar.setVisibility(View.GONE);
            }
            recipeDesciption = mBakeryStepsListBeans.get(position).getShortDescription();
        } else {
            if (mBakeryIngridentsListBeans.size() - 1 == position) {
                bakeryDetailsHolder.recipieDetailsHorizontalBar.setVisibility(View.GONE);
            }
            bakeryDetailsHolder.ingridentsQuantityTextView.setVisibility(View.VISIBLE);
            bakeryDetailsHolder.ingridentsMeasureTextView.setVisibility(View.VISIBLE);
            bakeryDetailsHolder.ingridentsQuantityTextView.setText(Double.toString(mBakeryIngridentsListBeans.get(position).getQuantity()));
            bakeryDetailsHolder.ingridentsMeasureTextView.setText(mBakeryIngridentsListBeans.get(position).getMeasure());
            recipeDesciption = mBakeryIngridentsListBeans.get(position).getIngredient();
        }

        bakeryDetailsHolder.recipieDetailsDesciptionTextView.setText(recipeDesciption);
    }

    @Override
    public int getItemCount() {
        if (mListType.equalsIgnoreCase("Steps")) {
            return mBakeryStepsListBeans.size();
        } else {
            return mBakeryIngridentsListBeans.size();
        }
    }

    public class BakeryDetailsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView recipieDetailsDesciptionTextView;
        View recipieDetailsHorizontalBar;
        TextView ingridentsQuantityTextView;
        TextView ingridentsMeasureTextView;
        ImageView favoriteImageViewDetailsList;
        RelativeLayout favoriteImageRelativeLayout;
        //FireBase Implementation
        /*FirebaseDatabase mFirebaseDatabase;
        DatabaseReference mDatabaseReference;
        ArrayList<DatabaseReference> databaseReferenceArrayList;*/

        public BakeryDetailsHolder(@NonNull View itemView) {
            super(itemView);
            recipieDetailsDesciptionTextView = (TextView) itemView.findViewById(R.id.recipieDetailsDesciptionTextView);
            recipieDetailsHorizontalBar = (View) itemView.findViewById(R.id.recipieDetailsHorizontalBar);
            ingridentsQuantityTextView = (TextView) itemView.findViewById(R.id.ingridentsQuantityTextView);
            ingridentsMeasureTextView = (TextView) itemView.findViewById(R.id.ingridentsMeasureTextView);
            favoriteImageViewDetailsList = (ImageView) itemView.findViewById(R.id.favoriteImageViewDetailsList);
            favoriteImageRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.favoriteImageRelativeLayout);
            //Firebase Object initialization
            /*mFirebaseDatabase = FirebaseDatabase.getInstance();
            mDatabaseReference = mFirebaseDatabase.getReference().child("FavoriteStepsList");*/
            //databaseReferenceArrayList=mDatabaseReference.getDatabase().getReference().child();

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListType.equalsIgnoreCase("Steps")) {


                mBakeryDetailsStepsOnClickListener.onBakeryDetailsStepsCliCkListenerr(getAdapterPosition(), mBakeryStepsListBeans);
            } else {
                mBakeryDetailsIngredientsOnClickListener.onBakeryDetailsIngredientsCliCkListenerr(getAdapterPosition(), mBakeryIngridentsListBeans);
            }


        }
    }

    public interface BakeryDetailsIngredientsOnClickListener {
        public void onBakeryDetailsIngredientsCliCkListenerr(int position, ArrayList<BakeryIngridentsListBean> bakeryIngridentsListBeans);
    }

    public interface BakeryDetailsStepsOnClickListener {
        public void onBakeryDetailsStepsCliCkListenerr(int position, ArrayList<BakeryStepsListBean> bakeryStepsListBeans);

    }

}
