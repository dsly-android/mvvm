package com.android.dsly.common.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * @author 陈志鹏
 * @date 2020-01-06
 */
public abstract class BaseBottomSheetDialog<VB extends ViewDataBinding> extends BottomSheetDialog implements ILifeCycle {

    protected Context mContext;
    protected VB mBinding;

    public BaseBottomSheetDialog(@NonNull Context context) {
        super(context);
        mContext = context;
        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding();
    }

    public BaseBottomSheetDialog(@NonNull Context context, int theme) {
        super(context, theme);
        mContext = context;
        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView(savedInstanceState);
        initEvent();
        initData();
    }

    /**
     * 注入绑定
     */
    private void initViewDataBinding() {
        if (getLayoutId() != 0) {
            //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包
            mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), getLayoutId(), null, false);
            setContentView(mBinding.getRoot());
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mBinding != null) {
            mBinding.unbind();
        }
    }
}
