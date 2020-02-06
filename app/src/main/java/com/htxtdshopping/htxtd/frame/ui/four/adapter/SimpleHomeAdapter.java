package com.htxtdshopping.htxtd.frame.ui.four.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import me.jessyan.autosize.utils.AutoSizeUtils;

public class SimpleHomeAdapter extends BaseAdapter {
    private Context mContext;
    private String[] mItems;

    public SimpleHomeAdapter(Context context, String[] items) {
        this.mContext = context;
        this.mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int padding = AutoSizeUtils.dp2px(mContext,10);

        TextView tv = new TextView(mContext);
        tv.setText(mItems[position]);
        tv.setTextSize(18);
        tv.setTextColor(Color.parseColor("#468ED0"));
        // tv.setGravity(Gravity.CENTER);
        tv.setPadding(padding, padding, padding, padding);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                AbsListView.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(lp);
        return tv;
    }
}