package com.android.dsly.common.base;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.PopupWindow;

import com.android.dsly.rxhttp.IView;

import org.simple.eventbus.EventBus;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author 陈志鹏
 * @date 2018/11/27
 */
public abstract class BasePopupWindow extends PopupWindow implements ILifeCycle {

    protected Context mContext;
    protected View mRootView;
    private Unbinder mUnbinder;

    public BasePopupWindow(Context context, int width, int height) {
        super(width, height);
        mContext = context;
        mRootView = View.inflate(context, getLayoutId(), null);
        setContentView(mRootView);
        mUnbinder = ButterKnife.bind(this, mRootView);
        EventBus.getDefault().registerSticky(this);

        //解决边框距离屏幕四周有间距的问题
        setBackgroundDrawable(null);
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
    public void setMvpView(IView view) {

    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY){
            mUnbinder.unbind();
            mUnbinder = null;
        }
        EventBus.getDefault().unregister(this);
    }
}
