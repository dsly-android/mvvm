package com.android.dsly.common.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.BR;
import com.trello.rxlifecycle3.components.support.RxFragment;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

/**
 * @author 陈志鹏
 * @date 2018/7/29
 */
public abstract class BaseFragment<VB extends ViewDataBinding, VM extends BaseViewModel> extends RxFragment implements ILifeCycle {

    protected VB mBinding;
    protected VM mViewModel;
    /**
     * 当前是否可见
     */
    protected boolean mCurrentVisibleState = false;
    /**
     * 是否第一次加载
     */
    protected boolean mIsFirstVisible = true;

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

        //注入arouter
        ARouter.getInstance().inject(this);

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
                getActivity().finish();
            }
        });
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
     * 点击返回键调用方法
     */
    protected void onBackPressed(){

    }

    @Override
    public void onResume() {
        super.onResume();
        //在滑动或者跳转的过程中，第一次创建fragment的时候均会调用onResume方法，类似于在tab1 滑到tab2，此时tab3会缓存，这个时候会调用tab3 fragment的
        //onResume，所以，此时是不需要去调用 dispatchUserVisibleHint(true)的，因而出现了下面的if
        if (isLazy() && !mIsFirstVisible) {
            //由于Activit1 中如果有多个fragment，然后从Activity1 跳转到Activity2，此时会有多个fragment会在activity1缓存，此时，如果再从activity2跳转回
            //activity1，这个时候会将所有的缓存的fragment进行onResume生命周期的重复，这个时候我们无需对所有缓存的fragnment 调用dispatchUserVisibleHint(true)
            //我们只需要对可见的fragment进行加载，因此就有下面的if
            if (isFragmentVisible() && !mCurrentVisibleState) {
                dispatchUserVisibleHint(true);
            }
        }
    }

    /**
     * 只有当当前页面由可见状态转变到不可见状态时才需要调用 dispatchUserVisibleHint
     * currentVisibleState && getUserVisibleHint() 能够限定是当前可见的 Fragment
     * 当前 Fragment 包含子 Fragment 的时候 dispatchUserVisibleHint 内部本身就会通知子 Fragment 不可见
     *  子 fragment 走到这里的时候自身又会调用一遍
     */
    @Override
    public void onPause() {
        super.onPause();
        if (isLazy() && mCurrentVisibleState && isFragmentVisible()) {
            dispatchUserVisibleHint(false);
        }
    }

    /**
     * 统一处理用户可见信息分发
     * @param isVisible
     */
    protected void dispatchUserVisibleHint(boolean isVisible) {
        //事实上作为父 Fragment 的 BottomTabFragment2 并没有分发可见事件，
        // 他通过 getUserVisibleHint() 得到的结果为 false，首先我想到的
        // 是能在负责分发事件的方法中判断一下当前父 fragment 是否可见，
        // 如果父 fragment 不可见我们就不进行可见事件的分发
        if (isVisible && isParentInvisible()) {
            return;
        }
        //为了代码严谨
        if (mCurrentVisibleState == isVisible) {
            return;
        }
        mCurrentVisibleState = isVisible;
        if (isVisible) {
            if (mIsFirstVisible) {
                mIsFirstVisible = false;
                //第一显示时初始化数据
                initData();
            }
            onFragmentResume();
            //在双重ViewPager嵌套的情况下，第一次滑到Frgment 嵌套ViewPager(fragment)的场景的时候
            //此时只会加载外层Fragment的数据，而不会加载内嵌viewPager中的fragment的数据，因此，我们
            //需要在此增加一个当外层Fragment可见的时候，分发可见事件给自己内嵌的所有Fragment显示
            dispatchChildVisibleState(true);
        } else {
            onFragmentPause();
            dispatchChildVisibleState(false);
        }
    }

    private boolean isParentInvisible() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof BaseLazyFragment) {
            BaseFragment fragment = (BaseFragment)parentFragment;
            return !fragment.isSupportVisible();
        }
        return false;
    }

    private void dispatchChildVisibleState(boolean visible) {
        FragmentManager fragmentManager = getChildFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment: fragments) {
                if (fragment instanceof BaseFragment && isFragmentVisible()) {
                    ((BaseFragment)fragment).dispatchUserVisibleHint(visible);
                }
            }
        }
    }

    protected void onFragmentResume() {

    }

    protected void onFragmentPause() {

    }

    private boolean isSupportVisible() {
        return mCurrentVisibleState;
    }

    public boolean isFragmentVisible() {
        return !isHidden() && getUserVisibleHint();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mBinding != null) {
            mBinding.unbind();
        }
        mIsFirstVisible = false;
    }
}
