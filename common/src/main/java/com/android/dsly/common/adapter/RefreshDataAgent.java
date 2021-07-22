package com.android.dsly.common.adapter;

import com.android.dsly.common.bean.PageBean;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import androidx.annotation.NonNull;

/**
 * @author 陈志鹏
 * @date 2020/5/25
 */
public class RefreshDataAgent {

    private BaseRefreshDataAdapter mAdapter;
    private SmartRefreshLayout mNrlRefresh;

    //是否可以加载更多
    private boolean mIsEnableLoadMore = false;

    public RefreshDataAgent(BaseRefreshDataAdapter adapter, SmartRefreshLayout nrlRefresh) {
        mAdapter = adapter;
        mNrlRefresh = nrlRefresh;

        init();
    }

    private void init() {
        if (mNrlRefresh != null) {
            mNrlRefresh.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    mAdapter.getLoadMoreModule().setEnableLoadMore(false);

                    if (mAdapter.getOnRefreshListener() != null) {
                        mAdapter.getOnRefreshListener().onRefresh(refreshLayout);
                    }
                }
            });
        }
    }

    private void setOnLoadMoreListener(){
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mNrlRefresh != null) {
                    mNrlRefresh.setEnableRefresh(false);
                }

                if (mAdapter.getOnLoadMoreListener() != null) {
                    mAdapter.getOnLoadMoreListener().onLoadMore();
                }
            }
        });
    }

    /**
     * 加载数据成功刷新页面
     */
    public <T> void notifyData(PageBean<T> pageBean) {
        //数据是第一页则是下拉刷新，不是第一页是上拉加载
        if (pageBean.getPageNum() == 1) {
            if (mNrlRefresh != null) {
                mNrlRefresh.finishRefresh();
            }
            mAdapter.getLoadMoreModule().setEnableLoadMore(mIsEnableLoadMore);
            //重置数据
            mAdapter.setNewData(pageBean.getResults());
        } else {
            if (mNrlRefresh != null) {
                mNrlRefresh.setEnableRefresh(true);
            }
            //添加数据
            mAdapter.addData(pageBean.getResults());
        }
        if (pageBean.getHasNext()) {
            mAdapter.getLoadMoreModule().loadMoreComplete();
        } else {
            mAdapter.getLoadMoreModule().loadMoreEnd();
        }
    }

    /**
     * 下拉刷新和上拉加载获取到数据失败
     */
    public void refreshFail() {
        if (mNrlRefresh != null) {
            mNrlRefresh.setEnableRefresh(true);
            mNrlRefresh.finishRefresh(false);
        }
        mAdapter.getLoadMoreModule().setEnableLoadMore(mIsEnableLoadMore);
        mAdapter.getLoadMoreModule().loadMoreFail();
    }

    public void setEnableLoadMore(boolean enableLoadMore) {
        mIsEnableLoadMore = enableLoadMore;
        mAdapter.getLoadMoreModule().setEnableLoadMore(mIsEnableLoadMore);
        if (enableLoadMore){
            setOnLoadMoreListener();
        }
    }

    public boolean isEnableLoadMore() {
        return mIsEnableLoadMore;
    }
}