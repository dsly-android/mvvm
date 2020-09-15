package com.android.dsly.common.listener;

import android.view.View;

import com.blankj.utilcode.constant.TimeConstants;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

/**
 * @author 陈志鹏
 * @date 2020/6/15
 */
public abstract class OnItemAntiClickListener implements OnItemClickListener {

    //防重复点击间隔(秒)
    public static final int CLICK_INTERVAL = 1;

    private long mLastTimeMillis = 0;

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (System.currentTimeMillis() - mLastTimeMillis < CLICK_INTERVAL * TimeConstants.SEC) {
            return;
        }
        mLastTimeMillis = System.currentTimeMillis();

        onItemAntiClick(adapter, view, position);
    }

    public abstract void onItemAntiClick(BaseQuickAdapter adapter, View view, int position);
}
