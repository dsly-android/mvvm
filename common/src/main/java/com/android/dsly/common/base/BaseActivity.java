package com.android.dsly.common.base;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.android.dsly.common.R;
import com.android.dsly.common.dialog.LoadingDialog;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.FragmentUtils;
import com.chad.library.BR;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;

/**
 * @author 陈志鹏
 * @date 2018/7/29
 */
public abstract class BaseActivity<VB extends ViewDataBinding, VM extends BaseViewModel> extends RxAppCompatActivity implements ILifeCycle, SlidingPaneLayout.PanelSlideListener {

    protected VB mBinding;
    protected VM mViewModel;

    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //侧滑关闭
        initSlideBackClose();
        super.onCreate(savedInstanceState);
        //刘海屏适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            // 仅当缺口区域完全包含在状态栏之中时，才允许窗口延伸到刘海区域显示。
            // 也就是说，如果没有设置为全屏显示模式，就允许窗口延伸到刘海区域，否则不允许。
//            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT;
            // 永远不允许窗口延伸到刘海区域
//            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER;
            // 始终允许窗口延伸到屏幕短边上的刘海区域
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(lp);
        }

        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding();
        //私有的ViewModel与View的契约事件回调逻辑
        registerLiveDataCallBack();

        //注入arouter
        ARouter.getInstance().inject(this);

        initStatusBar();
        if (isFitWindow()) {
            setFitsSystemWindows(true);
        }
        findViewById(android.R.id.content).setBackgroundResource(R.color._f3f3f3);

        initView(savedInstanceState);
        initEvent();
        initData();
    }

    /**
     * 侧滑关闭
     */
    private void initSlideBackClose() {
        if (isSupportSwipeClose()) {
            SlidingPaneLayout slidingPaneLayout = new SlidingPaneLayout(this);
            // 通过反射改变mOverhangSize的值为0，
            // 这个mOverhangSize值为菜单到右边屏幕的最短距离，
            // 默认是32dp，现在给它改成0
            try {
                Field overhangSize = SlidingPaneLayout.class.getDeclaredField("mOverhangSize");
                overhangSize.setAccessible(true);
                overhangSize.set(slidingPaneLayout, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            slidingPaneLayout.setPanelSlideListener(this);
            slidingPaneLayout.setSliderFadeColor(getResources()
                    .getColor(android.R.color.transparent));

            // 左侧的透明视图
            View leftView = new View(this);
            leftView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            leftView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            slidingPaneLayout.addView(leftView, 0);

            ViewGroup decorView = (ViewGroup) getWindow().getDecorView();


            // 右侧的内容视图
            ViewGroup decorChild = (ViewGroup) decorView.getChildAt(0);
            decorChild.setBackgroundColor(getResources()
                    .getColor(android.R.color.white));
            decorView.removeView(decorChild);
            decorView.addView(slidingPaneLayout);

            // 为 SlidingPaneLayout 添加内容视图
            slidingPaneLayout.addView(decorChild, 1);
        }
    }

    protected boolean isSupportSwipeClose() {
        return false;
    }

    @Override
    public void onPanelOpened(@NonNull View panel) {
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onPanelClosed(@NonNull View panel) {

    }

    @Override
    public void onPanelSlide(@NonNull View panel, float slideOffset) {

    }

    /**
     * 注入绑定
     */
    private void initViewDataBinding() {
        if (getLayoutId() != 0) {
            //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包
            mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        }
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
                finish();
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
     * 将返回键的点击方法传递到fragment
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        List<Fragment> fragments = FragmentUtils.getFragments(getSupportFragmentManager());
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            if (fragment instanceof BaseFragment){
                ((BaseFragment) fragment).onBackPressed();
            }
        }
    }

    /**
     * 初始化状态栏
     */
    protected void initStatusBar(){
        BarUtils.setStatusBarColor(this, getResources().getColor(R.color._81D8CF));
    }

    protected boolean isFitWindow() {
        return true;
    }

    protected void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog();
        }
        if (!mLoadingDialog.isVisible()) {
            mLoadingDialog.show(getSupportFragmentManager());
        }
    }

    protected void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isAdded()) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = null;
    }

    protected void setFitsSystemWindows(boolean fitSystemWindows) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        ViewGroup parent = findViewById(android.R.id.content);
        View childView = parent.getChildAt(0);
        if (childView != null && childView instanceof ViewGroup) {
            childView.setFitsSystemWindows(fitSystemWindows);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBinding != null) {
            mBinding.unbind();
        }
    }
}
