package com.htxtdshopping.htxtd.frame.ui.center.activity;

import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivitySwipeBinding;
import com.htxtdshopping.htxtd.frame.ui.center.adapter.SwipeAdapter;
import com.htxtdshopping.htxtd.frame.widget.ViewBinderHelper;

import androidx.recyclerview.widget.LinearLayoutManager;

public class SwipeActivity extends BaseActivity<ActivitySwipeBinding, BaseViewModel> {

    private SwipeAdapter mAdapter;
    private ViewBinderHelper mHelper;

    @Override
    public int getLayoutId() {
        return R.layout.activity_swipe;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mHelper = new ViewBinderHelper();

        mBinding.rvContent.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SwipeAdapter(mHelper);
        mBinding.rvContent.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.tv_delete:
                        mHelper.closeLayout(mAdapter.getItem(position));
                        break;
                    case R.id.tv_collect:
                        mHelper.closeLayout(mAdapter.getItem(position));
                        break;
                    case R.id.ll_content:
                        ToastUtils.showShort("onItemClick");
                        break;
                    default:
                        break;
                }
            }
        });
        //不能使用，事件冲突
//        mAdapter.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                ToastUtils.showShort("onItemClick");
//            }
//        });
    }

    @Override
    public void initData() {
        for (int i = 0; i < 10; i++) {
            mAdapter.addData("data" + i);
        }
    }
}
