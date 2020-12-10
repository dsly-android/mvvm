package com.htxtdshopping.htxtd.frame.ui.center.activity;

import android.os.Bundle;

import com.android.dsly.common.adapter.BaseBindingAdapter;
import com.android.dsly.common.adapter.BaseRefreshDataAdapter;
import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.common.bean.PageBean;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityTestBinding;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

public class TestActivity extends BaseActivity<ActivityTestBinding, BaseViewModel> {

    private BaseBindingAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mBinding.rvContent.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BaseBindingAdapter(R.layout.item_test, mBinding.srlRefresh);
        mAdapter.setEnableLoadMore(true);
        mBinding.rvContent.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        mAdapter.setOnRefreshListener(new BaseRefreshDataAdapter.OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum = 1;
                loadData();
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseRefreshDataAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadData();
            }
        });

        liveData.observe(this, new Observer<PageBean<String>>() {
            @Override
            public void onChanged(PageBean<String> pageBean) {
                if (pageBean == null) {
                    mAdapter.refreshFail();
                } else {
                    mAdapter.notifyData(pageBean);
                }
            }
        });
    }

    @Override
    public void initData() {
        loadData();
    }

    private MutableLiveData<PageBean<String>> liveData = new MutableLiveData<>();

    private int pageNum = 1;

    private void loadData() {
        PageBean<String> pageBean = new PageBean();
        pageBean.setPageNum(pageNum);
        pageBean.setHasNext(true);
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            data.add("item" + i);
        }
        pageBean.setResults(data);
        liveData.postValue(pageBean);

        pageNum++;
    }

    public void aaa(){

    }
}