package com.android.dsly.common.base;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

/**
 * @author 陈志鹏
 * @date 2018/9/9
 */
public abstract class BasePageLazyFragment<VB extends ViewDataBinding, VM extends BaseViewModel> extends BaseFragment<VB, VM> {
    //fragment 生命周期：
    // onAttach -> onCreate -> onCreatedView -> onActivityCreated -> onStart -> onResume -> onPause -> onStop -> onDestroyView -> onDestroy -> onDetach
    //对于 ViewPager + Fragment 的实现我们需要关注的几个生命周期有：
    //onCreatedView + onActivityCreated + onResume + onPause + onDestroyView

    private boolean isViewCreated = false;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewCreated = true;
        //初始化的时候，判断当前fragment可见状态
        if (isFragmentVisible()) {
            dispatchUserVisibleHint(true);
        }
    }

    /**
     * setUserVisibleHint被调用有两种情况：
     * 1：在切换tab的时候，会先于所有fragment的其他生命周期，先调用这个函数
     * 对于默认tab和间隔checked tab需要等到isviewcreated = true后才可以通过此通知用户可见
     * 2：对于之前已经调用过setUserVisibleHint方法的fragment，让frangment从可见到不可见之间状态的变化
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //  对于情况1）不予处理，用 isViewCreated 进行判断，如果isViewCreated false，说明它没有被创建
        if (isViewCreated) {
            //对于情况2）要分情况考虑，如果是不可见->可见是下面的情况 2.1），如果是可见->不可见是下面的情况2.2）
            //对于2.1）我们需要如何判断呢？首先必须是可见的（isVisibleToUser 为true）而且只有当可见状态进行改变的时候才需要切换，否则会出现反复调用的情况
            //从而导致事件分发带来的多次更新
            if (isVisibleToUser && ! mCurrentVisibleState) {
                dispatchUserVisibleHint(true);
            } else if (!isVisibleToUser && mCurrentVisibleState) {
                dispatchUserVisibleHint(false);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewCreated = false;
        unbindDrawables(getView());
    }

    @Override
    protected boolean isLazy() {
        return true;
    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }
}
