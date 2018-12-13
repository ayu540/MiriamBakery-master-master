package com.example.anshultech.miriambakery.Widgets;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.anshultech.miriambakery.Bean.BakeryRecipiesListBean;
import com.example.anshultech.miriambakery.Connection.ConnectionURL;
import com.example.anshultech.miriambakery.Connection.VolleyConnectionClass;
import com.example.anshultech.miriambakery.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BakeryDataLoadWidgetService extends IntentService implements VolleyConnectionClass.NetworkConnectionInferface {

    public static final String ACTION_LOAD_HOME_DATA = "com.example.anshultech.miriambakery.action.load_home_data";
    private static Context mContext;

    private static ArrayList<BakeryRecipiesListBean> mBakeryRecipiesArrayListBeans;
    private static boolean mIsLoggedIn = false;


    public BakeryDataLoadWidgetService() {
        super("BakeryDataLoadWidgetService");
    }

    /**
     * Starts this service to perform action with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionLoadDataWidgets(Context context) {
        mContext = context;
        mBakeryRecipiesArrayListBeans = new ArrayList<BakeryRecipiesListBean>();
        Intent intent = new Intent(context, BakeryDataLoadWidgetService.class);
        intent.setAction(ACTION_LOAD_HOME_DATA);
        context.startService(intent);
    }
    public static void checkUserLoggedIn(boolean isLoggedIn){
        mIsLoggedIn= isLoggedIn;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_LOAD_HOME_DATA.equals(action)) {
                loadDatainBackGroundForWidget();
            }
        }
    }

    //load data in the background through service
    private void loadDatainBackGroundForWidget() {
        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
        final int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(mContext, WidgetBakeryRecipieHome.class));
        //Trigger data update to handle the ListView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);

        if (mIsLoggedIn == true) {
            JsonArrayRequest jsonArrayRequest = VolleyConnectionClass.getInstance(mContext).volleyJSONArrayRequest(ConnectionURL.BAKING_RECIPIES_URL
                    , new Response.Listener<JSONArray>() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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
                                        mBakeryRecipiesArrayListBeans.add(bakeryRecipiesListBean);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }


                                if (mBakeryRecipiesArrayListBeans != null || mBakeryRecipiesArrayListBeans.size() > 0) {
                                    WidgetBakeryRecipieHome.updateBakeryRecipieWidgets(mContext, appWidgetManager, mBakeryRecipiesArrayListBeans,mIsLoggedIn,  appWidgetIds);
                                }

                            }

                        }


                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }
            );
            VolleyConnectionClass.getInstance(mContext).addToRequestQueue(jsonArrayRequest, this);
        }
        else {

            WidgetBakeryRecipieHome.updateBakeryRecipieWidgets(mContext, appWidgetManager, mBakeryRecipiesArrayListBeans,mIsLoggedIn,  appWidgetIds);
        }
    }

    @Override
    public void isNetworkAvailable() {
        Toast.makeText(mContext, mContext.getResources().getString(R.string.NetworknotAvailablepleaseccheckyourinternetconnection), Toast.LENGTH_SHORT).show();
    }
}