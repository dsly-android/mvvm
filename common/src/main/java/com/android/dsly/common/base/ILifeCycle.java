package com.android.dsly.common.base;

import android.os.Bundle;

import com.android.dsly.rxhttp.IView;

/**
 * @author 陈志鹏
 * @date 2018/9/7
 */
public interface ILifeCycle {

    /**
     * 获取布局id
     *
     * @return
     */
    int getLayoutId();

    /**
     * 将mvp的view设置给presenter
     */
    void setMvpView(IView view);

    /**
     * 初始化 view
     *
     * @param savedInstanceState
     */
    void initView(Bundle savedInstanceState);

    /**
     * 初始化事件
     */
    void initEvent();

    /**
     * 初始化数据
     */
    void initData();
}
