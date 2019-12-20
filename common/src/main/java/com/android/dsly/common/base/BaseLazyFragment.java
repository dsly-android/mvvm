package com.android.dsly.common.base;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

/**
 * @author 陈志鹏
 * @date 2018/9/7
 */
public abstract class BaseLazyFragment<P extends BasePresenter> extends BaseFragment<P> {

    private static final String INVISIBLE_WHEN_LEAVE = "invisible_when_leave";
    private boolean mInvisibleWhenLeave;
    /**
     * 是否第一次加载
     */
    protected boolean isFirstLoad = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mInvisibleWhenLeave = savedInstanceState.getBoolean(INVISIBLE_WHEN_LEAVE);
            judgeState(mInvisibleWhenLeave);
        }
    }

    /**
     * 判断fragment被意外回收后。也就是内存重启后，是否需要重新显示
     * @param invisibleWhenLeave true-内存重启前改Fragment是hide状态，反之是显示状态
     */
    private void judgeState(boolean invisibleWhenLeave) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (invisibleWhenLeave) {
            fragmentTransaction.hide(this);
        } else {
            fragmentTransaction.show(this);
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(INVISIBLE_WHEN_LEAVE, mInvisibleWhenLeave);
    }

    /**
     * 只有FragmentTransaction的show()和hide()方法才会调用onHiddenChanged()方法
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && isFirstLoad && isResumed()) {
            isFirstLoad = false;
            initData();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!mInvisibleWhenLeave && isFragmentVisible() && isFirstLoad) {
            isFirstLoad = false;
            initData();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isFragmentVisible()) {
            mInvisibleWhenLeave = false;
        } else {
            mInvisibleWhenLeave = true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isFirstLoad = true;
    }

    @Override
    protected boolean isLazy() {
        return true;
    }
}
