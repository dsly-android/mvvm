package com.android.dsly.common.base;

import com.android.dsly.rxhttp.IView;

import org.simple.eventbus.EventBus;

/**
 * @author 陈志鹏
 * @date 2018/9/14
 */
public abstract class BasePresenter<V extends IView> implements IPresenter {

    protected V mView;

    public BasePresenter() {
        onStart();
    }

    public void setView(V view) {
        mView = view;
    }

    @Override
    public void onStart() {
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
    }
}
