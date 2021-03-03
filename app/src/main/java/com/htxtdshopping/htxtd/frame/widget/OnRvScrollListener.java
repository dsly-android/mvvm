package com.htxtdshopping.htxtd.frame.widget;

import android.widget.AbsListView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author 陈志鹏
 * @date 2021/1/29
 */
public class OnRvScrollListener extends RecyclerView.OnScrollListener {

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        //recyclerView.canScrollVertically
        //-1:能否向下滑动  1：能否向上滑动
        if (!recyclerView.canScrollVertically(-1)) {
            onScrolledToTop(); //滑到顶部
        } else if (!recyclerView.canScrollVertically(1)) {
            onScrolledToBottom(); //滑到底部
        } else if (dy > 0) {
            onScrolledUp();
        } else if (dy < 0) {
            onScrolledDown();
        }
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            onStop();
        } else {
            onScrolling();
        }
    }

    /**
     * 向上滚动
     */
    public void onScrolledUp() {
    }

    /**
     * 向下滚动
     */
    public void onScrolledDown() {
    }

    /**
     * 滚动到顶部
     */
    public void onScrolledToTop() {
    }

    /**
     * 滚动到底部
     */
    public void onScrolledToBottom() {
    }

    /**
     * 停止
     */
    public void onStop() {
    }

    /**
     * 正在滚动
     */
    public void onScrolling() {
    }
}
