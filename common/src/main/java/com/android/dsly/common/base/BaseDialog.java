package com.android.dsly.common.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;

import com.android.dsly.common.R;
import com.android.dsly.rxhttp.IView;
import com.blankj.utilcode.util.ScreenUtils;

import org.simple.eventbus.EventBus;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author 陈志鹏
 * @date 2018/1/31
 */

public abstract class BaseDialog extends Dialog implements ILifeCycle{

    protected Context mContext;
    private Unbinder mUnbinder;

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
        setContentView(getLayoutId());
        mUnbinder = ButterKnife.bind(this);
        EventBus.getDefault().registerSticky(this);

        initView(savedInstanceState);
        initEvent();
        initData();
    }

    public void showDefault(){
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