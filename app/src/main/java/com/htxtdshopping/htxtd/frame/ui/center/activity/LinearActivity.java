package com.htxtdshopping.htxtd.frame.ui.center.activity;

import android.os.Bundle;
import android.os.SystemClock;

import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.blankj.utilcode.util.ThreadUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityLinearBinding;
import com.htxtdshopping.htxtd.frame.ui.center.adapter.LinearAdapter;
import com.htxtdshopping.htxtd.frame.widget.ChatLoadMoreView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LinearActivity extends BaseFitsWindowActivity<ActivityLinearBinding, BaseViewModel> {

    private LinearAdapter mAdapter;

    private int currentNum = 100;

    @Override
    public int getLayoutId() {
        return R.layout.activity_linear;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mBinding.rvContent.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, true));
        mAdapter = new LinearAdapter();
        mAdapter.getLoadMoreModule().setEnableLoadMore(true);
        mAdapter.getLoadMoreModule().setLoadMoreView(new ChatLoadMoreView());
        mBinding.rvContent.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadData();
            }
        });
    }

    @Override
    public void initData() {
        loadData();
    }

    public void loadData() {
        ThreadUtils.executeByIo(new ThreadUtils.SimpleTask<List<String>>() {
            @Override
            public List<String> doInBackground() throws Throwable {
                SystemClock.sleep(1000);
                List<String> strs = new ArrayList<>();
                int index = currentNum - 20;
                for (int i = currentNum; i > index; i--) {
                    currentNum = i;
                    strs.add(currentNum + "");
                }
                return strs;
            }

            @Override
            public void onSuccess(List<String> result) {
                mAdapter.addData(result);
                mAdapter.getLoadMoreModule().loadMoreComplete();
            }
        });
    }
}
