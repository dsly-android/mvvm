package com.htxtdshopping.htxtd.frame.ui.center.activity;

import android.os.Bundle;
import android.os.SystemClock;

import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.blankj.utilcode.util.ThreadUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.ui.center.adapter.LinearAdapter;
import com.htxtdshopping.htxtd.frame.widget.ChatLoadMoreView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class LinearActivity extends BaseFitsWindowActivity {

    @BindView(R.id.rv_content)
    RecyclerView mRvContent;
    private LinearAdapter mAdapter;

    private int currentNum = 100;

    @Override
    public int getLayoutId() {
        return R.layout.activity_linear;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mRvContent.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, true));
        mAdapter = new LinearAdapter();
        mAdapter.setEnableLoadMore(true);
        mAdapter.setLoadMoreView(new ChatLoadMoreView());
        mRvContent.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadData();
            }
        }, mRvContent);
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
                mAdapter.loadMoreComplete();
            }
        });
    }
}
