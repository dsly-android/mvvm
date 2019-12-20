package com.android.dsly.common.base;

/**
 * @author 陈志鹏
 * @date 2018/9/7
 */
public abstract class BaseFitsWindowActivity<P extends BasePresenter>  extends BaseActivity<P> {

    @Override
    protected boolean isFitWindow() {
        return true;
    }
}