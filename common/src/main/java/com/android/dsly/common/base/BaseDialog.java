package com.android.dsly.common.base;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;

import com.android.dsly.common.R;
import com.blankj.utilcode.util.ScreenUtils;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatDialog;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * @author 陈志鹏
 * @date 2018/1/31
 */

public abstract class BaseDialog<VB extends ViewDataBinding> extends AppCompatDialog implements ILifeCycle {

    protected Context mContext;
    protected VB mBinding;

    public BaseDialog(@NonNull Context context) {
        this(context, R.style.AppTheme_Dialog);
    }

    public BaseDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding();

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

    public void showDefault() {
        super.show();
    }

    @Override
    public void show() {
        show(0.73);
    }

    public void show(double widthPercent) {
        super.show();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = (int) (ScreenUtils.getScreenWidth() * widthPercent);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }

    public void showBottom() {
        super.show();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(params);
    }

    public void show(int x, int y) {
        show(x, y, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void show(int x, int y, int width, int height) {
        super.show();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = width;
        params.height = height;
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = x;
        params.y = y;
        getWindow().setAttributes(params);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mBinding != null) {
            mBinding.unbind();
        }
    }
}