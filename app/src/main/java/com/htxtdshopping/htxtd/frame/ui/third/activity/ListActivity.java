package com.htxtdshopping.htxtd.frame.ui.third.activity;

import android.os.Bundle;

import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.common.decoration.LinearDividerItemDecoration;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityListBinding;
import com.htxtdshopping.htxtd.frame.ui.third.adapter.GridAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * @author 陈志鹏
 * @date 2018/9/7
 */
public class ListActivity extends BaseFitsWindowActivity<ActivityListBinding, BaseViewModel> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_list;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mBinding.rvContent.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rvContent.addItemDecoration(new LinearDividerItemDecoration(this,10));
        GridAdapter adapter = new GridAdapter();
        mBinding.rvContent.setAdapter(adapter);

        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            strings.add("item"+i);
        }
        adapter.setNewData(strings);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }
}