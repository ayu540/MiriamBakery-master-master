package com.example.anshultech.miriambakery.Widgets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.anshultech.miriambakery.Bean.BakeryRecipiesListBean;
import com.example.anshultech.miriambakery.R;

import java.util.ArrayList;

import static com.example.anshultech.miriambakery.Widgets.WidgetBakeryRecipieHome.mBakeryRecipiesArrayListBeansHomeWidget;


public class GridWidgetSevice extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }
}

class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    ArrayList<BakeryRecipiesListBean> mBakeryRecipiesArrayListBeans;
    boolean mTwoPane=false;


    public GridRemoteViewsFactory(Context context) {
        mContext = context;
        mBakeryRecipiesArrayListBeans = new ArrayList<BakeryRecipiesListBean>();
    }

    @Override
    public void onCreate() {

    }

    //called on start and when notifyAllWidgetDataChanged is called
    @Override
    public void onDataSetChanged() {

        mBakeryRecipiesArrayListBeans=  mBakeryRecipiesArrayListBeansHomeWidget;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mBakeryRecipiesArrayListBeans.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        if (mBakeryRecipiesArrayListBeans != null && mBakeryRecipiesArrayListBeans.size() > 0) {
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_bakery_recipie_home);
            views.setTextViewText(R.id.appwidget_text, mBakeryRecipiesArrayListBeans.get(position).getName());

            Bundle bundle = new Bundle();
            bundle.putInt(mContext.getString(R.string.clicked_position), position);
            bundle.putParcelableArrayList(mContext.getString(R.string.bakery_master_list), mBakeryRecipiesArrayListBeans);
            bundle.putParcelableArrayList(mContext.getString(R.string.ingredient_list), mBakeryRecipiesArrayListBeans.get(position).getBakeryIngridentsListBeans());
            bundle.putParcelableArrayList(mContext.getString(R.string.steps_list), mBakeryRecipiesArrayListBeans.get(position).getBakeryStepsListBeans());
            bundle.putBoolean(mContext.getString(R.string.is_two_pane), mTwoPane);
            Intent intent= new Intent();
            intent.putExtras(bundle);
            views.setOnClickFillInIntent(R.id.appwidget_RelativeLayout, intent);
            return views;
        }
        return null;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}