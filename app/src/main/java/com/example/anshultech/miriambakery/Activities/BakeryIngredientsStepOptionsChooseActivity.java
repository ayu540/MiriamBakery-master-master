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
 //   private boolean mTwoPane = false;

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

        if (getIntent() != null) {
            mBakeryRecipiesListBeans = getIntent().getExtras().getParcelableArrayList(getResources().getString(R.string.bakery_master_list));
            mRecipeMasterListClickedPosition = getIntent().getExtras().getInt(getResources().getString(R.string.clicked_position));
            mBakeryIngridentsListBeans = getIntent().getExtras().getParcelableArrayList(getResources().getString(R.string.ingredient_list));
            mBakeryStepsListBeans = getIntent().getExtras().getParcelableArrayList(getResources().getString(R.string.steps_list));
     //       mTwoPane = getIntent().getExtras().getBoolean(getResources().getString(R.string.is_two_pane));
        }


        if (savedInstanceState != null) {
            mBakeryRecipiesListBeans = savedInstanceState.getParcelableArrayList(getResources().getString(R.string.bakery_master_list));
            mRecipeMasterListClickedPosition = savedInstanceState.getInt(getResources().getString(R.string.clicked_position));
            mBakeryIngridentsListBeans = savedInstanceState.getParcelableArrayList(getResources().getString(R.string.ingredient_list));
            mBakeryStepsListBeans = savedInstanceState.getParcelableArrayList(getResources().getString(R.string.steps_list));
       //     mTwoPane = savedInstanceState.getBoolean(getResources().getString(R.string.is_two_pane));
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
    //    outState.putBoolean(getResources().getString(R.string.is_two_pane), mTwoPane);
    }

    private void prepareRecipieButtonData() {

        if (mBakeryStepsListBeans != null && mBakeryIngridentsListBeans != null) {
            final Bundle bundle = new Bundle();
            bundle.putInt(getResources().getString(R.string.clicked_position), mRecipeMasterListClickedPosition);
            bundle.putParcelableArrayList(getResources().getString(R.string.bakery_master_list), mBakeryRecipiesListBeans);
          //  bundle.putBoolean(getResources().getString(R.string.is_two_pane), mTwoPane);
            mIngredientsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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

                    ArrayList<String> indgirdentStepsData = new ArrayList<String>();

                    for (int i = 0; i < mBakeryStepsListBeans.size(); i++) {
                        indgirdentStepsData.add(mBakeryStepsListBeans.get(i).getShortDescription());
                    }
                    if (indgirdentStepsData != null && indgirdentStepsData.size() > 0) {
                        //call the widget service to load data in it
//                        BakeryRecipieShowService(mContext, indgirdentStepsData);
                    }

                    loadNextActivity(bundle);
                }
            });
        }
    }


    private void loadNextActivity(Bundle bundle) {
       // if (mTwoPane == false) {
            Intent intent = new Intent(mContext, BakerryRecipieDetailViewActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, RECIPIE_CHOOSER_LIST_CODE);
        //}
    }

}