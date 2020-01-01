package com.htxtdshopping.htxtd.frame.ui.four.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityTabHomeBinding;
import com.htxtdshopping.htxtd.frame.ui.four.adapter.SimpleHomeAdapter;

public class TabHomeActivity extends BaseActivity<ActivityTabHomeBinding, BaseViewModel> {

    private final String[] mItems = {"SlidingTabLayout", "CommonTabLayout", "SegmentTabLayout"};
    private final Class<?>[] mClasses = {SlidingTabActivity.class, CommonTabActivity.class, SegmentTabActivity.class};

    @Override
    public int getLayoutId() {
        return R.layout.activity_tab_home;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mBinding.lvContent.setAdapter(new SimpleHomeAdapter(this, mItems));

        mBinding.lvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TabHomeActivity.this, mClasses[position]);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }
}
