package com.example.anshultech.miriambakery.Widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViews;

import com.example.anshultech.miriambakery.Activities.BakeryIngredientsStepOptionsChooseActivity;
import com.example.anshultech.miriambakery.Bean.BakeryRecipiesListBean;
import com.example.anshultech.miriambakery.R;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetBakeryRecipieHome extends AppWidgetProvider {

    public static ArrayList<BakeryRecipiesListBean> mBakeryRecipiesArrayListBeansHomeWidget = new ArrayList<BakeryRecipiesListBean>();
    public static boolean mWidgetHomeIsUserLoggedIn;

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        BakeryDataLoadWidgetService.startActionLoadDataWidgets(context);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                ArrayList<BakeryRecipiesListBean> BakeryRecipiesArrayListBeans
                                , boolean isUserLoggedIn,
                                int appWidgetId) {

        mBakeryRecipiesArrayListBeansHomeWidget = BakeryRecipiesArrayListBeans;
        mWidgetHomeIsUserLoggedIn = isUserLoggedIn;

        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        int width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);

        RemoteViews remoteViews = null;
        remoteViews = getWidgetHomeGridViewWidget(context);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    private static RemoteViews getWidgetHomeGridViewWidget(Context context) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_list_view);

        Intent intent = new Intent(context, GridWidgetSevice.class);
        remoteViews.setRemoteAdapter(R.id.widget_list_view, intent);
        // Set the PlantDetailActivity intent to launch when clicked
        Intent appIntent = new Intent(context, BakeryIngredientsStepOptionsChooseActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.widget_list_view, appPendingIntent);

        remoteViews.setEmptyView(R.id.widget_list_view, R.id.empty_view);

        return remoteViews;
    }

    //this will called whenever new widget is createdd and there is any update
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        BakeryDataLoadWidgetService.startActionLoadDataWidgets(context);

    }

    /**
     * Updates all widget instances given the widget Ids and display information
     *
     * @param context                       The calling context
     * @param appWidgetManager              The widget manager
     * @param mBakeryRecipiesArrayListBeans This will load the data for home screen
     * @param appWidgetIds                  Array of widget Ids to be updated
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void updateBakeryRecipieWidgets(Context context, AppWidgetManager appWidgetManager,
                                                  ArrayList<BakeryRecipiesListBean> mBakeryRecipiesArrayListBeans,boolean isUserLoggedIn, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, mBakeryRecipiesArrayListBeans,isUserLoggedIn, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}