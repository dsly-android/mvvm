package com.htxtdshopping.htxtd.frame.ui.center.fragment;


import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseLazyFragment;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.ui.center.activity.LinearActivity;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class CenterFragment extends BaseLazyFragment {

    @BindView(R.id.v_bar)
    View mVBar;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_center;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        BarUtils.setStatusBarColor(mVBar, getResources().getColor(R.color._81D8CF));

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.btn_linear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_linear:
                ActivityUtils.startActivity(LinearActivity.class);
                break;
            default:
                break;
        }
    }
}
