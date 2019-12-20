package com.htxtdshopping.htxtd.frame.widget.refresh;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import android.widget.TextView;

import com.htxtdshopping.htxtd.frame.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;

/**
 * @author 陈志鹏
 * @date 2019/1/17
 */
public class NewsRefreshLayout extends SmartRefreshLayout {

    private boolean mIsRefreshSuccess;

    public NewsRefreshLayout(Context context) {
        super(context);
    }

    public NewsRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected ValueAnimator animSpinner(int endSpinner, int startDelay, Interpolator interpolator, int duration) {
        if (getState() == RefreshState.RefreshFinish && mIsRefreshSuccess) {
            TextView tvRefreshNum = findViewById(R.id.tv_refresh_num);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    NewsRefreshLayout.super.animSpinner(0, 0, interpolator, duration);
                }
            }, startDelay);
            return super.animSpinner(tvRefreshNum.getLayoutParams().height, 0, interpolator, duration);
        } else {
            return super.animSpinner(endSpinner, startDelay, interpolator, duration);
        }
    }

    @Override
    public RefreshLayout finishRefresh(boolean success) {
        mIsRefreshSuccess = success;
        return super.finishRefresh(success);
    }
}