package com.example.anshultech.miriambakery.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.anshultech.miriambakery.Bean.BakeryStepsListBean;
import com.example.anshultech.miriambakery.R;

import java.util.List;

public class FavoriteListViewAdapter extends ArrayAdapter<BakeryStepsListBean> {
    private List<BakeryStepsListBean> mBakeryStepsListBeans;
    private Context mContext;

    public FavoriteListViewAdapter(Context context, int resource, List<BakeryStepsListBean> bakeryStepsListBeans) {
        super(context, resource, bakeryStepsListBeans);
        mBakeryStepsListBeans = bakeryStepsListBeans;
        mContext = context;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.recipiedetailslistlayout, parent, false);
        }

        TextView recipieDetailsDesciptionTextView = (TextView) convertView.findViewById(R.id.recipieDetailsDesciptionTextView);
        View recipieDetailsHorizontalBar = (View) convertView.findViewById(R.id.recipieDetailsHorizontalBar);
        TextView ingridentsQuantityTextView = (TextView) convertView.findViewById(R.id.ingridentsQuantityTextView);
        TextView ingridentsMeasureTextView = (TextView) convertView.findViewById(R.id.ingridentsMeasureTextView);

        ingridentsQuantityTextView.setVisibility(View.GONE);
        ingridentsMeasureTextView.setVisibility(View.GONE);
        BakeryStepsListBean bakeryStepsListBean = mBakeryStepsListBeans.get(position);

        recipieDetailsDesciptionTextView.setText(bakeryStepsListBean.getShortDescription());

        return convertView;
    }

    public void updateListView(List<BakeryStepsListBean> bakeryStepsListBeans) {
        mBakeryStepsListBeans = bakeryStepsListBeans;
        notifyDataSetChanged();
    }
}
