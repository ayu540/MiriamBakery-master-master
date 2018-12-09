package com.example.anshultech.miriambakery.Activities;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.anshultech.miriambakery.Bean.BakeryIngridentsListBean;
import com.example.anshultech.miriambakery.Bean.BakeryRecipiesListBean;
import com.example.anshultech.miriambakery.Bean.BakeryStepsListBean;
import com.example.anshultech.miriambakery.R;

import java.util.ArrayList;


public class BakeryIngredientsStepOptionsChooseActivity extends AppCompatActivity {
    private Button mIngredientsButton;
    private Button mStepButton;
    private Context mContext;
    private ArrayList<BakeryRecipiesListBean> mBakeryRecipiesListBeans;
    private ArrayList<BakeryIngridentsListBean> mBakeryIngridentsListBeans;
    private ArrayList<BakeryStepsListBean> mBakeryStepsListBeans;
    private int mRecipeMasterListClickedPosition;
    private final int RECIPIE_CHOOSER_LIST_CODE = 12;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.indridentasstepsclicklayout);
        mContext = BakeryIngredientsStepOptionsChooseActivity.this;
        mBakeryRecipiesListBeans = new ArrayList<BakeryRecipiesListBean>();
        mBakeryIngridentsListBeans = new ArrayList<BakeryIngridentsListBean>();
        mBakeryStepsListBeans = new ArrayList<BakeryStepsListBean>();
        mIngredientsButton = (Button) findViewById(R.id.recipeOptyionChooseIngriedientsButton);
        mStepButton = (Button) findViewById(R.id.recipeOptyionChooseStepsButton);
        mIngredientsButton.setContentDescription(getResources().getString(R.string.RecipeIngredientsButton));
        mStepButton.setContentDescription(getResources().getString(R.string.RecipeStepsButton));

        if (getIntent() != null) {
            mBakeryRecipiesListBeans = getIntent().getExtras().getParcelableArrayList(getResources().getString(R.string.bakery_master_list));
            mRecipeMasterListClickedPosition = getIntent().getExtras().getInt(getResources().getString(R.string.clicked_position));
            mBakeryIngridentsListBeans = getIntent().getExtras().getParcelableArrayList(getResources().getString(R.string.ingredient_list));
            mBakeryStepsListBeans = getIntent().getExtras().getParcelableArrayList(getResources().getString(R.string.steps_list));
        }

        if (savedInstanceState != null) {
            mBakeryRecipiesListBeans = savedInstanceState.getParcelableArrayList(getResources().getString(R.string.bakery_master_list));
            mRecipeMasterListClickedPosition = savedInstanceState.getInt(getResources().getString(R.string.clicked_position));
            mBakeryIngridentsListBeans = savedInstanceState.getParcelableArrayList(getResources().getString(R.string.ingredient_list));
            mBakeryStepsListBeans = savedInstanceState.getParcelableArrayList(getResources().getString(R.string.steps_list));
        }

        prepareRecipieButtonData();
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getResources().getString(R.string.bakery_master_list), mBakeryRecipiesListBeans);
        outState.putInt(getResources().getString(R.string.clicked_position), mRecipeMasterListClickedPosition);
        outState.putParcelableArrayList(getResources().getString(R.string.ingredient_list), mBakeryIngridentsListBeans);
        outState.putParcelableArrayList(getResources().getString(R.string.steps_list), mBakeryStepsListBeans);
    }

    private void prepareRecipieButtonData() {

        if (mBakeryStepsListBeans != null && mBakeryIngridentsListBeans != null) {
            final Bundle bundle = new Bundle();
            bundle.putInt(getResources().getString(R.string.clicked_position), mRecipeMasterListClickedPosition);
            bundle.putParcelableArrayList(getResources().getString(R.string.bakery_master_list), mBakeryRecipiesListBeans);
            mIngredientsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mIngredientsButton.announceForAccessibility("Recipie Ingredients");
                    bundle.putParcelableArrayList(getResources().getString(R.string.ingredient_list), mBakeryIngridentsListBeans);
                    bundle.putString(getResources().getString(R.string.list_type), "Ingredients");
                    loadNextActivity(bundle);
                }
            });

            mStepButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bundle.putParcelableArrayList(getResources().getString(R.string.steps_list), mBakeryStepsListBeans);
                    bundle.putString(getResources().getString(R.string.list_type), "Steps");
                    mStepButton.announceForAccessibility("Recipie Steps");
                    loadNextActivity(bundle);
                }
            });
        }
    }


    private void loadNextActivity(Bundle bundle) {
        Intent intent = new Intent(mContext, BakerryRecipieDetailViewActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, RECIPIE_CHOOSER_LIST_CODE);
    }

}