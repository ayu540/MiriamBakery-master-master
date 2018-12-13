package com.example.anshultech.miriambakery.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.anshultech.miriambakery.Adapters.BakeryRecipiesListRecyclerViewAdapter;
import com.example.anshultech.miriambakery.Bean.BakeryRecipiesListBean;
import com.example.anshultech.miriambakery.Connection.ConnectionURL;
import com.example.anshultech.miriambakery.Connection.VolleyConnectionClass;
import com.example.anshultech.miriambakery.R;
import com.example.anshultech.miriambakery.Utilities.SimpleIdlingResource;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.anshultech.miriambakery.Widgets.BakeryDataLoadWidgetService.checkUserLoggedIn;

public class BakeryHome extends AppCompatActivity implements VolleyConnectionClass.NetworkConnectionInferface {

    private Context mContext;
    private RecyclerView mRecipiListRecyclerView;
    private BakeryRecipiesListRecyclerViewAdapter mBakeryRecipiesListRecyclerViewAdapter;
    private final int RECIPIE_MASTER_LIST_LISTENER_CODE = 11;

    //Firebase Authenticaion Implementation
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private static final int RC_SIGN_IN = 123;
    private String mUserName;
    private TextView userNameHomeTextView;

    ArrayList<BakeryRecipiesListBean> mBakeryRecipiesArrayListBeans;
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    public BakeryHome() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bakery_home);
        mContext = BakeryHome.this;
        mRecipiListRecyclerView = (RecyclerView) findViewById(R.id.recipiesMasterListRecyclerView);
        mRecipiListRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBakeryRecipiesArrayListBeans = new ArrayList<BakeryRecipiesListBean>();
        userNameHomeTextView = (TextView) findViewById(R.id.user_name_home_text_view);

        //Firebase Auth intialization
        mFirebaseAuth = FirebaseAuth.getInstance();

        //Firebase Auth Listener
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser User1 = firebaseAuth.getCurrentUser();
                if (User1 != null) {
                    //user is signedd in
                    onSignedInInitialize(User1.getDisplayName());
                } else {
                    onSignedOutCleanUp();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RC_SIGN_IN) {

                Toast.makeText(mContext, getResources().getString(R.string.Signedin), Toast.LENGTH_SHORT).show();

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(mContext, getResources().getString(R.string.SignedinCanceled), Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    private void networkCallToLoadData() {

        JsonArrayRequest jsonArrayRequest = VolleyConnectionClass.getInstance(mContext).volleyJSONArrayRequest(ConnectionURL.BAKING_RECIPIES_URL
                , new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response != null) {
                            Gson gson = new Gson();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject = response.getJSONObject(i);
                                    BakeryRecipiesListBean bakeryRecipiesListBean = gson.fromJson(jsonObject.toString(), BakeryRecipiesListBean.class);
                                    int a = jsonObject.getInt("id");
                                    bakeryRecipiesListBean.setLoggedUserName(mUserName);
                                    mBakeryRecipiesArrayListBeans.add(bakeryRecipiesListBean);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            if (mBakeryRecipiesArrayListBeans != null || mBakeryRecipiesArrayListBeans.size() > 0) {
                                mBakeryRecipiesListRecyclerViewAdapter = new BakeryRecipiesListRecyclerViewAdapter(mContext,
                                        mBakeryRecipiesArrayListBeans,
                                        new BakeryRecipiesListRecyclerViewAdapter.BakeryRecipiesListOnClickListener() {
                                            @Override
                                            public void onRecipiesClickItem(int position, ArrayList<BakeryRecipiesListBean> lBakeryRecipiesListBeans) {

                                                Bundle bundle = new Bundle();
                                                bundle.putInt(getResources().getString(R.string.clicked_position), position);
                                                bundle.putParcelableArrayList(getResources().getString(R.string.bakery_master_list), lBakeryRecipiesListBeans);
                                                bundle.putParcelableArrayList(getResources().getString(R.string.ingredient_list), lBakeryRecipiesListBeans.get(position).getBakeryIngridentsListBeans());
                                                bundle.putParcelableArrayList(getResources().getString(R.string.steps_list), lBakeryRecipiesListBeans.get(position).getBakeryStepsListBeans());
                                                Intent intent = new Intent(mContext, BakeryIngredientsStepOptionsChooseActivity.class);
                                                intent.putExtras(bundle);
                                                startActivityForResult(intent, RECIPIE_MASTER_LIST_LISTENER_CODE);

                                            }
                                        });
                                mRecipiListRecyclerView.setAdapter(mBakeryRecipiesListRecyclerViewAdapter);
                                mIdlingResource.setIdleState(true);
                            }

                        } else {
                            Toast.makeText(mContext, getResources().getString(R.string.ServerErrorpleasetryagain), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener()

                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        VolleyConnectionClass.getInstance(mContext).addToRequestQueue(jsonArrayRequest, this);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void isNetworkAvailable() {
        Toast.makeText(mContext, getResources().getString(R.string.NetworknotAvailablepleaseccheckyourinternetconnection), Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mFirebaseAuth != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        if (mBakeryRecipiesListRecyclerViewAdapter != null && mBakeryRecipiesListRecyclerViewAdapter.getItemCount() > 0) {
            mBakeryRecipiesListRecyclerViewAdapter.clearData();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    public void onSignedInInitialize(String userName) {
        checkUserLoggedIn(true);
        if (userName != null && !userName.equalsIgnoreCase("")) {
            mUserName = userName;
            userNameHomeTextView.setVisibility(View.VISIBLE);
            userNameHomeTextView.setText(getResources().getString(R.string.Welcome) + mUserName);
            userNameHomeTextView.setContentDescription(getResources().getString(R.string.Welcome)+ mUserName);
        } else {
            mUserName = "";
            userNameHomeTextView.setVisibility(View.GONE);
        }
        getSupportActionBar().setTitle(getResources().getString(R.string.MiriamRecipieList));
        getSupportActionBar().setHomeActionContentDescription(getResources().getString(R.string.MiriamRecipieList));
        networkCallToLoadData();
        getIdlingResource();
    }

    public void onSignedOutCleanUp() {
        checkUserLoggedIn(false);
        if (mBakeryRecipiesListRecyclerViewAdapter != null && mBakeryRecipiesListRecyclerViewAdapter.getItemCount() > 0) {
            mBakeryRecipiesListRecyclerViewAdapter.clearData();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_signed_out_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signed_out_menu_view: {
                AuthUI.getInstance().signOut(mContext);
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }
}

