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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.anshultech.miriambakery.Adapters.BakeryDetailsRecyclerViewAdapter;
import com.example.anshultech.miriambakery.Adapters.FavoriteListViewAdapter;
import com.example.anshultech.miriambakery.Adapters.FavoriteRecyclerViewAdapter;
import com.example.anshultech.miriambakery.Bean.BakeryIngridentsListBean;
import com.example.anshultech.miriambakery.Bean.BakeryRecipiesListBean;
import com.example.anshultech.miriambakery.Bean.BakeryStepsListBean;
import com.example.anshultech.miriambakery.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class BakerryRecipieDetailViewActivity extends AppCompatActivity {


    private ArrayList<BakeryRecipiesListBean> mBakeryRecipiesListBeans;
    private ArrayList<BakeryIngridentsListBean> mBakeryIngridentsListBeans;
    private ArrayList<BakeryStepsListBean> mBakeryStepsListBeans;
    private Context mContext;
    private int mRecipeMasterListClickedPosition;
    private BakeryDetailsRecyclerViewAdapter mbBakeryDetailsRecyclerViewAdapter;
    private RecyclerView mRecipiDetailsViewRecyClerView;
    private String RECIPE_LIST_TYPE;
    private final int BAKERY_STEPS_CLICKED = 13;
    //   private boolean mTwoPane = false;

    //Firebase Implementation
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ValueEventListener mValueEventListener;
    //FavoriteList
    private RecyclerView favoriteListView;
    // private FavoriteListViewAdapter favoriteListViewAdapter;
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
        mDatabaseReference = mFirebaseDatabase.getReference().child("FavoriteStepsList");

        //favoriteList Initialization
        favoriteBakeryStepsListBeans = new ArrayList<>();
        favoriteListView = (RecyclerView) findViewById(R.id.favoriteListViewRecyClerView);
        favoriteListView.setLayoutManager(new LinearLayoutManager(mContext));
        favoriteDetailsCountTextView = (TextView) findViewById(R.id.favoriteDetailsCountTextView);
        plusMinusDetailsButton = (ImageView) findViewById(R.id.plusDetailsButton);
        favotiteRelativeLayout = (RelativeLayout) findViewById(R.id.favotiteRelativeLayout);
        favotiteRelativeLayout.setVisibility(View.VISIBLE);
        favoriteListView.setVisibility(View.GONE);

        valueFavouriteListListener();
        plusMinusDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (plusMinusDetailsButton.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_add_circle_black).getConstantState()) {

                    plusMinusDetailsButton.setImageResource(R.drawable.ic_remove_circle_black);
                    if (favoriteBakeryStepsListBeans != null && favoriteBakeryStepsListBeans.size() > 0) {
                        favoriteListView.setVisibility(View.VISIBLE);
                    }
                } else {
                    favoriteListView.setVisibility(View.GONE);
                    plusMinusDetailsButton.setImageResource(R.drawable.ic_add_circle_black);
                }
            }
        });


        if (getIntent() != null) {
            mBakeryRecipiesListBeans = getIntent().getExtras().getParcelableArrayList(getResources().getString(R.string.bakery_master_list));
            mRecipeMasterListClickedPosition = getIntent().getExtras().getInt(getResources().getString(R.string.ingredient_list));
            RECIPE_LIST_TYPE = getIntent().getExtras().getString(getResources().getString(R.string.list_type));
            mUserName = mBakeryRecipiesListBeans.get(mRecipeMasterListClickedPosition).getLoggedUserName();
            //     mTwoPane = getIntent().getExtras().getBoolean(getResources().getString(R.string.is_two_pane));
        }

        if (savedInstanceState != null) {
            mBakeryRecipiesListBeans = savedInstanceState.getParcelableArrayList(getResources().getString(R.string.instance_bakery_master_list));
            mRecipeMasterListClickedPosition = savedInstanceState.getInt(getResources().getString(R.string.instance_clicked_position));
            RECIPE_LIST_TYPE = savedInstanceState.getString(getResources().getString(R.string.instance_list_type));
            mUserName = savedInstanceState.getString(getResources().getString(R.string.LoggedUserName));
            //    mTwoPane = savedInstanceState.getBoolean(getResources().getString(R.string.instance_is_two_pane));
        }
        loadRecipieListItems();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getResources().getString(R.string.instance_bakery_master_list), mBakeryRecipiesListBeans);
        outState.putInt(getResources().getString(R.string.instance_clicked_position), mRecipeMasterListClickedPosition);
        outState.putString(getResources().getString(R.string.instance_list_type), RECIPE_LIST_TYPE);
        outState.putString(getResources().getString(R.string.LoggedUserName), mUserName);
        //      outState.putBoolean(getResources().getString(R.string.instance_is_two_pane), mTwoPane);
    }

    private void loadRecipieListItems() {

        if (RECIPE_LIST_TYPE.equalsIgnoreCase("Ingredients")) {
            favotiteRelativeLayout.setVisibility(View.GONE);
            getSupportActionBar().setTitle(getResources().getString(R.string.RecipieIngredients));
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
        } else if (RECIPE_LIST_TYPE.equalsIgnoreCase("Steps")) {
            mBakeryStepsListBeans = getIntent().getExtras().getParcelableArrayList(getResources().getString(R.string.steps_list));
            getSupportActionBar().setTitle(getResources().getString(R.string.RecipieSteps));
            if (mBakeryStepsListBeans != null) {
                mbBakeryDetailsRecyclerViewAdapter = new BakeryDetailsRecyclerViewAdapter(mContext, mBakeryStepsListBeans,
                        new BakeryDetailsRecyclerViewAdapter.BakeryDetailsStepsOnClickListener() {
                            @Override
                            public void onBakeryDetailsStepsCliCkListenerr(final int position,
                                                                           final ArrayList<BakeryStepsListBean> bakeryStepsListBeans) {


                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                builder.setMessage("Please Choose option");
                                builder.setCancelable(true);
                                builder.setPositiveButton("Set Favourite", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        bakeryStepsListBeans.get(position).setUserName(mUserName);
                                        BakeryStepsListBean favoriteBakeryStepsListBean = new BakeryStepsListBean(bakeryStepsListBeans.get(position).getId(),
                                                bakeryStepsListBeans.get(position).getShortDescription(), bakeryStepsListBeans.get(position).getDescription()
                                                , bakeryStepsListBeans.get(position).getVideoURL(), bakeryStepsListBeans.get(position).getUserName()
                                        );


                                        mDatabaseReference.push().setValue(favoriteBakeryStepsListBean);

                                        valueFavouriteListListener();


                                    }


                                });

                                builder.setNegativeButton("View", new DialogInterface.OnClickListener()

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
                    BakeryStepsListBean post = postSnapshot.getValue(BakeryStepsListBean.class);
                    favoriteBakeryStepsListBeans.add(post);
                }
                long count = dataSnapshot.getChildrenCount();
                favoriteDetailsCountTextView.setText("Favourite Count (" + Long.toString(count) + ")");

                favoriteRecyclerViewAdapter = new FavoriteRecyclerViewAdapter(mContext, favoriteBakeryStepsListBeans
                        , new FavoriteRecyclerViewAdapter.favoriteClickListener() {
                    @Override
                    public void favoriteItemClick(final int position, final ArrayList<BakeryStepsListBean> bakeryStepsListBeans) {
                        AlertDialog.Builder favoriteDialog = new AlertDialog.Builder(mContext);
                        favoriteDialog.setMessage("Please Choose option");
                        favoriteDialog.setCancelable(true);
                        favoriteDialog.setPositiveButton("Delete Favorite",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteFavoriteDBItem();
                                    }
                                }
                        );

                        favoriteDialog.setNegativeButton("View",
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
                    }
                }

                );
                favoriteRecyclerViewAdapter.updateFavoriteAdapterList(favoriteBakeryStepsListBeans);
                favoriteListView.setAdapter(favoriteRecyclerViewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabaseReference.addValueEventListener(mValueEventListener);

    }

    private void deleteFavoriteDBItem() {
        Query favoriteQuery = mDatabaseReference.child("FavoriteStepsList").orderByChild("id");
        favoriteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
