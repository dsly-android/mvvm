package com.android.dsly.common.base;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.android.dsly.common.R;
import com.blankj.utilcode.util.ScreenUtils;
import com.chad.library.BR;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

/**
 * @author 陈志鹏
 * @date 2018/10/26
 */
public abstract class BaseDialogFragment<VB extends ViewDataBinding, VM extends BaseViewModel> extends RxAppcompatRobustDialogFragment implements ILifeCycle {

    protected VB mBinding;
    protected VM mViewModel;
    /**
     * 监听弹出窗是否被取消
     */
    private DialogInterface.OnDismissListener mDismissListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding();
        //私有的ViewModel与View的契约事件回调逻辑
        registerLiveDataCallBack();

        //返回键监听器
        setOnBackKeyListener();

        //注入arouter
        ARouter.getInstance().inject(this);

        initView(savedInstanceState);
        initEvent();
        initData();
    }

    /**
     * 注入绑定
     */
    private void initViewDataBinding() {
        mViewModel = initViewModel();
        if (mViewModel == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = BaseViewModel.class;
            }
            mViewModel = (VM) ViewModelProviders.of(this).get(modelClass);
        }
        //关联ViewModel
        mBinding.setVariable(BR.viewModel, mViewModel);
        //注入RxLifecycle生命周期
        mViewModel.setLifecycleProvider(this);
    }

    /**
     * 私有的ViewModel与View的契约事件回调逻辑
     */
    protected void registerLiveDataCallBack() {
        mViewModel.getDialogEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    showLoading();
                } else {
                    hideLoading();
                }
            }
        });
        mViewModel.getFinishEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                dismiss();
            }
        });
    }

    private void setOnBackKeyListener() {
        //设置返回键监听器
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    onBackPressed();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    public void show(@NonNull FragmentManager manager) {
        show(manager, getDialogTag());
    }

    /**
     * dialog占屏幕宽度的百分比
     *
     * @return
     */
    public double getWidth() {
        return ScreenUtils.getScreenWidth() * 0.73;
    }

    @Override
    public void onStart() {
        super.onStart();
        initWidth();
    }

    /**
     * 修改dialog显示的宽高和位置
     */
    protected void initWidth() {
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = (int) getWidth();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes(params);
    }

    /**
     * 点击返回键调用方法
     */
    protected void onBackPressed() {

    }

    /**
     * dialog调用show方法时需要传的tag
     *
     * @return
     */
    public abstract String getDialogTag();

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    public VM initViewModel() {
        return null;
    }

    public void showLoading() {
        ((BaseActivity) getActivity()).showLoading();
    }

    public void hideLoading() {
        ((BaseActivity) getActivity()).hideLoading();
    }

    @Override
    public int getTheme() {
        return R.style.AppTheme_Dialog;
    }

    public BaseDialogFragment setOnDismissListener(DialogInterface.OnDismissListener dismissListener) {
        this.mDismissListener = dismissListener;
        return this;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mDismissListener != null) {
            mDismissListener.onDismiss(dialog);
        }
        super.onDismiss(dialog);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mBinding != null) {
            mBinding.unbind();
        }
    }
}