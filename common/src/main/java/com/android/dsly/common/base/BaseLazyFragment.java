package com.android.dsly.common.base;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentTransaction;

/**
 * @author 陈志鹏
 * @date 2018/9/7
 */
public abstract class BaseLazyFragment<VB extends ViewDataBinding, VM extends BaseViewModel> extends BaseFragment<VB,VM> {

    private static final String INVISIBLE_WHEN_LEAVE = "invisible_when_leave";
    private boolean mInvisibleWhenLeave;

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化的时候，判断当前fragment可见状态
        if (!mInvisibleWhenLeave && isFragmentVisible()) {
            dispatchUserVisibleHint(true);
        }
    }

    /**
     * 用FragmentTransaction来控制fragment的hide和show时，
     * 那么这个方法就会被调用。每当你对某个Fragment使用hide
     * 或者是show的时候，那么这个Fragment就会自动调用这个方法。
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            dispatchUserVisibleHint(false);
        } else {
            dispatchUserVisibleHint(true);
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
    protected boolean isLazy() {
        return true;
    }
}
