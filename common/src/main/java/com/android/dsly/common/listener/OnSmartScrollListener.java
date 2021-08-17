package com.android.dsly.common.listener;

import android.content.Context;

import com.bumptech.glide.Glide;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * 快速滑动时候，停止加载图片
 * 滑动速度降低或停止滑动的时候加载图片
 */
public class OnSmartScrollListener extends RecyclerView.OnScrollListener {
    private Context mContext;

    private int previousFirstVisibleItem = 0;
    private long previousEventTime = 0;

    public OnSmartScrollListener(Context context) {
        mContext = context;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            resumeImageLoad();
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (!recyclerView.canScrollVertically(1)) {//不能再向上,滑动到底部
            resumeImageLoad();
            return;
        }

        if (!recyclerView.canScrollVertically(-1)) {//不能再向下
            resumeImageLoad();
            return;
        }

        //速度降低时候，也加载
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int firstVisibleItem = 0;
        if (layoutManager instanceof LinearLayoutManager) {
            firstVisibleItem = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else if (layoutManager instanceof GridLayoutManager) {
            firstVisibleItem = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] mFirstVisibleItems = null;
            mFirstVisibleItems = ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(mFirstVisibleItems);
            if (mFirstVisibleItems != null) {
                firstVisibleItem = mFirstVisibleItems[0];
            }
        }
        if (previousFirstVisibleItem != firstVisibleItem) {
            long currTime = System.currentTimeMillis();
            long timeToScrollOneElement = currTime - previousEventTime;
            double speed = ((double) Math.abs(firstVisibleItem - previousFirstVisibleItem) / timeToScrollOneElement) * 1000;
            previousFirstVisibleItem = firstVisibleItem;
            previousEventTime = currTime;
            //滑动速度大于每秒10个item的时候停止加载图片，小于每秒10个item继续加载图片
            if (speed > 10) {
                pauseImageLoad();
            } else {
                resumeImageLoad();
            }
        }
    }

    private void pauseImageLoad() {
        try {
            if (mContext != null) {
                Glide.with(mContext).pauseRequests();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resumeImageLoad() {
        try {
            if (mContext != null && Glide.with(mContext).isPaused()) {
                Glide.with(mContext).resumeRequests();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}