package com.htxtdshopping.htxtd.frame.ui.third.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.android.dsly.common.base.BasePopupWindow;
import com.android.dsly.common.base.BaseViewModel;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityPopupWindowBinding;

/**
 * @author chenzhipeng
 */
public class PopupWindowActivity extends BaseFitsWindowActivity<ActivityPopupWindowBinding, BaseViewModel> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_popup_window;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    public void fullScreen(View view) {
        BasePopupWindow window = new BasePopupWindow(this, 200, 200) {

            @Override
            public int getLayoutId() {
                return R.layout.popup_test;
            }

            @Override
            public void initView(Bundle savedInstanceState) {

            }

            @Override
            public void initEvent() {

            }

            @Override
            public void initData() {

            }
        };
        //点击屏幕其他部分和返回键都能使其消失
        window.setFocusable(true);
        int[] a = new int[2];
        mBinding.btnFullScreen.getLocationOnScreen(a);
//        window.showAtLocation(mClParent, Gravity.LEFT | Gravity.TOP, 0, 0);
        //上
        window.showAtLocation(mBinding.clParent, Gravity.TOP, 0, a[1] - window.getHeight());
        //左
//      window.showAtLocation(mClParent,Gravity.LEFT|Gravity.TOP,a[0]-window.getWidth(),a[1] + (mBtnFullScreen.getHeight() - window.getHeight()) / 2);
    }
}
