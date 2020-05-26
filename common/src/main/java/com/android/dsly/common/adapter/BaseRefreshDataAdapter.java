package com.android.dsly.common.adapter;

import com.android.dsly.common.bean.PageBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import androidx.annotation.NonNull;

/**
 * @author 陈志鹏
 * @date 2020/5/25
 */
public abstract class BaseRefreshDataAdapter<T, VM extends BaseViewHolder> extends BaseQuickAdapter<T, VM> implements LoadMoreModule {

    //数据刷新代理
    private RefreshDataAgent mRefreshDataAgent;
    //下拉刷新监听器
    private OnRefreshListener mRefreshListener;
    //上拉加载监听器
    private OnLoadMoreListener mLoadMoreListener;

    public BaseRefreshDataAdapter(int layoutId) {
        this(layoutId, null);
    }

    public BaseRefreshDataAdapter(int layoutId, SmartRefreshLayout nrlRefresh) {
        super(layoutId);

        mRefreshDataAgent = new RefreshDataAgent(this, nrlRefresh);
    }

    /**
     * 加载数据成功刷新页面
     */
    public void notifyData(PageBean<T> pageBean) {
        mRefreshDataAgent.notifyData(pageBean);
    }

    /**
     * 下拉刷新和上拉加载获取到数据失败
     */
    public void refreshFail() {
        mRefreshDataAgent.refreshFail();
    }

    public void setEnableLoadMore(boolean enableLoadMore) {
        mRefreshDataAgent.setEnableLoadMore(enableLoadMore);
    }

    public boolean isEnableLoadMore() {
        return mRefreshDataAgent.isEnableLoadMore();
    }

    /**
     * 刷新监听器
     */
    public interface OnRefreshListener {
        void onRefresh(@NonNull RefreshLayout refreshLayout);
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        mRefreshListener = listener;
    }

    public OnRefreshListener getOnRefreshListener() {
        return mRefreshListener;
    }

    /**
     * 加载监听器
     */
    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mLoadMoreListener = listener;
    }

    public OnLoadMoreListener getOnLoadMoreListener() {
        return mLoadMoreListener;
    }
}