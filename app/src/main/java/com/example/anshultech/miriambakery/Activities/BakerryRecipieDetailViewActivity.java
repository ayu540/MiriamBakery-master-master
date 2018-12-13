package com.example.anshultech.miriambakery.Activities;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.anshultech.miriambakery.Adapters.BakeryDetailsRecyclerViewAdapter;
import com.example.anshultech.miriambakery.Adapters.FavoriteRecyclerViewAdapter;
import com.example.anshultech.miriambakery.Bean.BakeryIngridentsListBean;
import com.example.anshultech.miriambakery.Bean.BakeryRecipiesListBean;
import com.example.anshultech.miriambakery.Bean.BakeryStepsListBean;
import com.example.anshultech.miriambakery.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class BakerryRecipieDetailViewActivity extends AppCompatActivity implements FavoriteRecyclerViewAdapter.favoriteClickListener {


    private ArrayList<BakeryRecipiesListBean> mBakeryRecipiesListBeans;
    private ArrayList<BakeryIngridentsListBean> mBakeryIngridentsListBeans;
    private ArrayList<BakeryStepsListBean> mBakeryStepsListBeans;
    private Context mContext;
    private int mRecipeMasterListClickedPosition;
    private BakeryDetailsRecyclerViewAdapter mbBakeryDetailsRecyclerViewAdapter;
    private RecyclerView mRecipiDetailsViewRecyClerView;
    private String RECIPE_LIST_TYPE;
    private final int BAKERY_STEPS_CLICKED = 13;

    //Firebase Implementation
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ValueEventListener mValueEventListener;
    //FavoriteList
    private RecyclerView favoriteListView;
    private FavoriteRecyclerViewAdapter favoriteRecyclerViewAdapter;
    private TextView favoriteDetailsCountTextView;
    private ArrayList<BakeryStepsListBean> favoriteBakeryStepsListBeans;
    private ImageView plusMinusDetailsButton;
    private RelativeLayout favotiteRelativeLayout;
    private String mUserName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bakery_recipie_detail_view);
        mContext = BakerryRecipieDetailViewActivity.this;
        mBakeryRecipiesListBeans = new ArrayList<BakeryRecipiesListBean>();
        mBakeryIngridentsListBeans = new ArrayList<BakeryIngridentsListBean>();
        mBakeryStepsListBeans = new ArrayList<BakeryStepsListBean>();

        mRecipiDetailsViewRecyClerView = (RecyclerView) findViewById(R.id.recipiDetailsViewRecyClerView);
        mRecipiDetailsViewRecyClerView.setLayoutManager(new LinearLayoutManager(mContext));


        //Firebase initialization
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child(getResources().getString(R.string.FavoriteStepsList));

        //favoriteList Initialization
        favoriteBakeryStepsListBeans = new ArrayList<>();
        favoriteListView = (RecyclerView) findViewById(R.id.favoriteListViewRecyClerView);
        favoriteListView.setLayoutManager(new LinearLayoutManager(mContext));
        favoriteDetailsCountTextView = (TextView) findViewById(R.id.favoriteDetailsCountTextView);
        plusMinusDetailsButton = (ImageView) findViewById(R.id.plusDetailsButton);
        favotiteRelativeLayout = (RelativeLayout) findViewById(R.id.favotiteRelativeLayout);
        favotiteRelativeLayout.setVisibility(View.VISIBLE);
        favoriteListView.setVisibility(View.GONE);

        if (getIntent() != null) {
            mBakeryRecipiesListBeans = getIntent().getExtras().getParcelableArrayList(getResources().getString(R.string.bakery_master_list));
            mRecipeMasterListClickedPosition = getIntent().getExtras().getInt(getResources().getString(R.string.ingredient_list));
            RECIPE_LIST_TYPE = getIntent().getExtras().getString(getResources().getString(R.string.list_type));
            mUserName = mBakeryRecipiesListBeans.get(mRecipeMasterListClickedPosition).getLoggedUserName();
        }

        if (savedInstanceState != null) {
            mBakeryRecipiesListBeans = savedInstanceState.getParcelableArrayList(getResources().getString(R.string.instance_bakery_master_list));
            mRecipeMasterListClickedPosition = savedInstanceState.getInt(getResources().getString(R.string.instance_clicked_position));
            RECIPE_LIST_TYPE = savedInstanceState.getString(getResources().getString(R.string.instance_list_type));
            mUserName = savedInstanceState.getString(getResources().getString(R.string.LoggedUserName));
        }
        plusMinusDetailsButton.setContentDescription(getResources().getString(R.string.FavoriteListOpeenorClosebutton));
        valueFavouriteListListener();
        plusMinusDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (plusMinusDetailsButton.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_add_circle_black).getConstantState()) {

                    plusMinusDetailsButton.setImageResource(R.drawable.ic_remove_circle_black);
                    if (favoriteBakeryStepsListBeans != null && favoriteBakeryStepsListBeans.size() > 0) {
                        favoriteListView.setVisibility(View.VISIBLE);
                        plusMinusDetailsButton.announceForAccessibility(getResources().getString(R.string.FavoriteListOpens));
                    }
                } else {
                    favoriteListView.setVisibility(View.GONE);
                    plusMinusDetailsButton.setImageResource(R.drawable.ic_add_circle_black);
                    plusMinusDetailsButton.announceForAccessibility(getResources().getString(R.string.FavoriteListClose));
                }
            }
        });
        loadRecipieListItems();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getResources().getString(R.string.instance_bakery_master_list), mBakeryRecipiesListBeans);
        outState.putInt(getResources().getString(R.string.instance_clicked_position), mRecipeMasterListClickedPosition);
        outState.putString(getResources().getString(R.string.instance_list_type), RECIPE_LIST_TYPE);
        outState.putString(getResources().getString(R.string.LoggedUserName), mUserName);
    }

    private void loadRecipieListItems() {

        if (RECIPE_LIST_TYPE.equalsIgnoreCase(getResources().getString(R.string.Ingredients))) {
            favotiteRelativeLayout.setVisibility(View.GONE);
            getSupportActionBar().setTitle(getResources().getString(R.string.RecipieIngredients));
            getSupportActionBar().setHomeActionContentDescription(getResources().getString(R.string.RecipieIngredients));
            mBakeryIngridentsListBeans = getIntent().getExtras().getParcelableArrayList(getResources().getString(R.string.ingredient_list));
            if (mBakeryIngridentsListBeans != null) {

                mbBakeryDetailsRecyclerViewAdapter = new BakeryDetailsRecyclerViewAdapter(mContext, mBakeryIngridentsListBeans
                        , new BakeryDetailsRecyclerViewAdapter.BakeryDetailsIngredientsOnClickListener() {
                    @Override
                    public void onBakeryDetailsIngredientsCliCkListenerr(int position,
                                                                         ArrayList<BakeryIngridentsListBean> bakeryIngridentsListBeans) {
                        //do nothing
                    }
                }, RECIPE_LIST_TYPE
                );

                mRecipiDetailsViewRecyClerView.setAdapter(mbBakeryDetailsRecyclerViewAdapter);
            }
        } else if (RECIPE_LIST_TYPE.equalsIgnoreCase(getResources().getString(R.string.Steps))) {
            mBakeryStepsListBeans = getIntent().getExtras().getParcelableArrayList(getResources().getString(R.string.steps_list));
            getSupportActionBar().setHomeActionContentDescription(getResources().getString(R.string.steps_list));
            getSupportActionBar().setTitle(getResources().getString(R.string.RecipieSteps));
            if (mBakeryStepsListBeans != null) {
                mbBakeryDetailsRecyclerViewAdapter = new BakeryDetailsRecyclerViewAdapter(mContext, mBakeryStepsListBeans,
                        new BakeryDetailsRecyclerViewAdapter.BakeryDetailsStepsOnClickListener() {
                            @Override
                            public void onBakeryDetailsStepsCliCkListenerr(final int position,
                                                                           final ArrayList<BakeryStepsListBean> bakeryStepsListBeans) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                builder.setMessage(getResources().getString(R.string.PleaseChooseoption));
                                builder.setCancelable(true);
                                builder.setPositiveButton(getResources().getString(R.string.SetFavourite), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        bakeryStepsListBeans.get(position).setUserName(mUserName);
                                        //setting the id of the records to maintin the listings of favorite data
                                        BakeryStepsListBean favoriteBakeryStepsListBean = new BakeryStepsListBean();
                                        int itemID = 0;
                                        if (favoriteBakeryStepsListBeans != null && favoriteBakeryStepsListBeans.size() > 0) {
                                            itemID = favoriteBakeryStepsListBeans.size();
                                            favoriteBakeryStepsListBean = new BakeryStepsListBean(itemID,
                                                    bakeryStepsListBeans.get(position).getShortDescription(), bakeryStepsListBeans.get(position).getDescription()
                                                    , bakeryStepsListBeans.get(position).getVideoURL(), bakeryStepsListBeans.get(position).getThumbnailURL(),
                                                    bakeryStepsListBeans.get(position).getUserName());
                                        } else {
                                            favoriteBakeryStepsListBean = new BakeryStepsListBean(itemID,
                                                    bakeryStepsListBeans.get(position).getShortDescription(), bakeryStepsListBeans.get(position).getDescription()
                                                    , bakeryStepsListBeans.get(position).getVideoURL(), bakeryStepsListBeans.get(position).getThumbnailURL(),
                                                    bakeryStepsListBeans.get(position).getUserName());
                                        }

                                        mDatabaseReference.push().setValue(favoriteBakeryStepsListBean);
                                        valueFavouriteListListener();
                                    }

                                });

                                builder.setNegativeButton(getResources().getString(R.string.View), new DialogInterface.OnClickListener()

                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(getResources().getString(R.string.steps_clicked_position), position);
                                        bundle.putParcelableArrayList(getResources().getString(R.string.video_steps_list), bakeryStepsListBeans);
                                        Intent intent = new Intent(mContext, BakeryRecipeStepsVideoPlayerActivity.class);
                                        intent.putExtras(bundle);
                                        startActivityForResult(intent, BAKERY_STEPS_CLICKED);
                                    }
                                });
                                builder.create();
                                builder.show();

                            }
                        }, RECIPE_LIST_TYPE
                );
                mRecipiDetailsViewRecyClerView.setAdapter(mbBakeryDetailsRecyclerViewAdapter);
            }

        }

    }

    public void valueFavouriteListListener() {
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                BakeryStepsListBean bakeryStepsListBean = dataSnapshot.getValue(BakeryStepsListBean.class);
                if (favoriteBakeryStepsListBeans != null && favoriteBakeryStepsListBeans.size() > 0) {
                    favoriteBakeryStepsListBeans.clear();
                }
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    BakeryStepsListBean bakeryStepsListBean1 = postSnapshot.getValue(BakeryStepsListBean.class);
                    favoriteBakeryStepsListBeans.add(bakeryStepsListBean1);
                }
                long count = dataSnapshot.getChildrenCount();
                favoriteDetailsCountTextView.setText(getResources().getString(R.string.FavouriteCountopenbracket) + Long.toString(count) + getResources().getString(R.string.closebracket));
                favoriteDetailsCountTextView.setContentDescription(getResources().getString(R.string.FavouriteCountopenbracket) + Long.toString(count) + getResources().getString(R.string.closebracket));

                favoriteRecyclerViewAdapter = new FavoriteRecyclerViewAdapter(mContext, favoriteBakeryStepsListBeans, BakerryRecipieDetailViewActivity.this);
                favoriteRecyclerViewAdapter.updateFavoriteAdapterList(favoriteBakeryStepsListBeans);
                favoriteListView.setAdapter(favoriteRecyclerViewAdapter);
                mDatabaseReference.removeEventListener(mValueEventListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabaseReference.addValueEventListener(mValueEventListener);

    }


    @Override
    public void favoriteItemClick(final int position, final ArrayList<BakeryStepsListBean> bakeryStepsListBeans) {
        AlertDialog.Builder favoriteDialog = new AlertDialog.Builder(mContext);
        favoriteDialog.setMessage(getResources().getString(R.string.PleaseChooseoption));
        favoriteDialog.setCancelable(true);
        favoriteDialog.setNegativeButton(getResources().getString(R.string.View),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(getResources().getString(R.string.steps_clicked_position), position);
                        bundle.putParcelableArrayList(getResources().getString(R.string.video_steps_list), bakeryStepsListBeans);
                        Intent intent = new Intent(mContext, BakeryRecipeStepsVideoPlayerActivity.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, BAKERY_STEPS_CLICKED);
                    }
                }
        );
        favoriteDialog.create();
        favoriteDialog.show();
    }
}
