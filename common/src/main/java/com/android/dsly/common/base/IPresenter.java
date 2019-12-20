package com.android.dsly.common.base;

/**
 * @author 陈志鹏
 * @date 2018/9/14
 */
public interface IPresenter {
    /**
     * 做一些初始化操作
     */
    void onStart();

    /**
     * 在框架中 {@link BaseActivity#onDestroy()} 时会默认调用 {@link IPresenter#onDestroy()}
     */
    void onDestroy();
}
