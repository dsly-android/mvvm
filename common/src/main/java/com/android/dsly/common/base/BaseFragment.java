package com.android.dsly.common.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle3.components.support.RxFragment;

import org.simple.eventbus.EventBus;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

/**
 * @author 陈志鹏
 * @date 2018/7/29
 */
public abstract class BaseFragment<VB extends ViewDataBinding, VM extends BaseViewModel> extends RxFragment implements ILifeCycle {

    protected VB mBinding;
    protected VM mViewModel;
    private int mViewModelId;

    private long lastClick = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().registerSticky(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding();
        //私有的ViewModel与View的契约事件回调逻辑
        registerLiveDataCallBack();

        initView(savedInstanceState);
        initEvent();
        if (!isLazy()) {
            initData();
        }
    }

    /**
     * 注入绑定
     */
    private void initViewDataBinding() {
        mViewModelId = initVariableId();
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
        if (mViewModelId != 0) {
            mBinding.setVariable(mViewModelId, mViewModel);
        }
        //注入RxLifecycle生命周期
        mViewModel.setLifecycleProvider(this);
    }

    /**
     * 私有的ViewModel与View的契约事件回调逻辑
     */
    protected void registerLiveDataCallBack(){
        mViewModel.getDialogEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    showLoading();
                }else{
                    hideLoading();
                }
            }
        });
        mViewModel.getFinishEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                getActivity().finish();
            }
        });
    }

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    public int initVariableId(){
        return 0;
    }

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    public VM initViewModel() {
        return null;
    }

    /**
     * 是否使用懒加载
     *
     * @return
     */
    protected boolean isLazy() {
        return false;
    }

    public void showLoading() {
        ((BaseActivity) getActivity()).showLoading();
    }

    public void hideLoading() {
        ((BaseActivity) getActivity()).hideLoading();
    }

    private boolean isFastClick() {
        long now = System.currentTimeMillis();
        if (now - lastClick >= 200) {
            lastClick = now;
            return false;
        }
        return true;
    }

    public boolean isNotFastClick() {
        return !isFastClick();
    }

    public boolean isFragmentVisible() {
        return !isHidden() && getUserVisibleHint();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mBinding != null){
            mBinding.unbind();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
