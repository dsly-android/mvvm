package com.android.dsly.common.widget.layoutmanager;

import android.content.Context;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView设置为LinearLayoutManager时，竖直方向上的滑动距离
 *
 * @author 陈志鹏
 * @date 2021/7/27
 */
public class OffsetLinearLayoutManager extends LinearLayoutManager {

    public OffsetLinearLayoutManager(Context context) {
        super(context);
    }

    private Map<Integer, Integer> heightMap = new HashMap<>();

    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            heightMap.put(i, view.getHeight());
        }
    }

    @Override
    public int computeVerticalScrollOffset(RecyclerView.State state) {
        if (getChildCount() == 0) {
            return 0;
        }
        int firstVisiblePosition = findFirstVisibleItemPosition();
        View firstVisibleView = findViewByPosition(firstVisiblePosition);
        int offsetY = -(int) (firstVisibleView.getY());
        for (int i = 0; i < firstVisiblePosition; i++) {
            offsetY += heightMap.get(i) == null ? 0 : heightMap.get(i);
        }
        return offsetY;
    }
}