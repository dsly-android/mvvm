package com.htxtdshopping.htxtd.frame.ui.third.activity;

import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.common.decoration.GridDividerItemDecoration;
import com.android.dsly.common.listener.OnItemAntiClickListener;
import com.android.dsly.common.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityGridBinding;
import com.htxtdshopping.htxtd.frame.ui.third.adapter.GridAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;

/**
 * @author chenzhipeng
 */
public class GridActivity extends BaseFitsWindowActivity<ActivityGridBinding, BaseViewModel> {

    private GridAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_grid;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        mBinding.rvContent.setLayoutManager(manager);
        GridDividerItemDecoration decoration = new GridDividerItemDecoration(this, 20);
        mBinding.rvContent.addItemDecoration(decoration);

        mAdapter = new GridAdapter();
        mBinding.rvContent.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        mAdapter.setOnItemClickListener(new OnItemAntiClickListener() {

            @Override
            public void onItemAntiClick(BaseQuickAdapter adapter, View view, int position) {
                ToastUtils.showLong(position + "");
            }
        });
    }

    @Override
    public void initData() {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 17; i++) {
            strings.add("item" + i);
        }
        mAdapter.setNewData(strings);
    }
}
