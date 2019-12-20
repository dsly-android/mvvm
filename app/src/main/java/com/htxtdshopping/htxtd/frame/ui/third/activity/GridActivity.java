package com.htxtdshopping.htxtd.frame.ui.third.activity;

import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.android.dsly.common.decoration.GridDividerItemDecoration;
import com.android.dsly.common.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.ui.third.adapter.GridAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * @author chenzhipeng
 */
public class GridActivity extends BaseFitsWindowActivity {

    @BindView(R.id.rv_content)
    RecyclerView mRvContent;
    private GridAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_grid;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        mRvContent.setLayoutManager(manager);
        GridDividerItemDecoration decoration = new GridDividerItemDecoration(this, 20);
        mRvContent.addItemDecoration(decoration);

        mAdapter = new GridAdapter();
        mRvContent.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
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
