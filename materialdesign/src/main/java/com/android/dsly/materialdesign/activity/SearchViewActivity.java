package com.android.dsly.materialdesign.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.common.utils.ToastUtils;
import com.android.dsly.materialdesign.R;
import com.android.dsly.materialdesign.databinding.DesignActivitySearchViewBinding;

import androidx.annotation.NonNull;

public class SearchViewActivity extends BaseActivity<DesignActivitySearchViewBinding, BaseViewModel> {

    @Override
    public int getLayoutId() {
        return R.layout.design_activity_search_view;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mBinding.tbTitle2.setTitle("");
        setSupportActionBar(mBinding.tbTitle2);
        mBinding.tbTitle2.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.design_menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_finish) {
            ToastUtils.showShort("finish");
        }
        return true;
    }
}
