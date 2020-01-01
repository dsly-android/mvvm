package com.android.dsly.common.base;

import androidx.databinding.ViewDataBinding;

/**
 * @author 陈志鹏
 * @date 2018/9/7
 */
public abstract class BaseFitsWindowActivity<VB extends ViewDataBinding, VM extends BaseViewModel>  extends BaseActivity<VB,VM> {

    @Override
    protected boolean isFitWindow() {
        return true;
    }
}