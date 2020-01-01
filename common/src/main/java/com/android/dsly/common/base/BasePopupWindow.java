package com.android.dsly.common.base;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.widget.PopupWindow;

import org.simple.eventbus.EventBus;

import java.lang.reflect.Field;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * @author 陈志鹏
 * @date 2018/11/27
 */
public abstract class BasePopupWindow<VB extends ViewDataBinding> extends PopupWindow implements ILifeCycle {

    protected Context mContext;
    protected VB mBinding;

    public BasePopupWindow(Context context, int width, int height) {
        super(width, height);
        mContext = context;

        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding();

        EventBus.getDefault().registerSticky(this);

        //解决边框距离屏幕四周有间距的问题
        setBackgroundDrawable(null);
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

    protected void init(){
        initView(null);
        initEvent();
        initData();
    }

    /**
     * 使PopupWindow能覆盖住状态栏
     */
    public void fitPopupWindowOverStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                Field mLayoutInScreen = PopupWindow.class.getDeclaredField("mLayoutInScreen");
                mLayoutInScreen.setAccessible(true);
                mLayoutInScreen.set(this, true);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mBinding != null){
            mBinding.unbind();
        }
        EventBus.getDefault().unregister(this);
    }
}
