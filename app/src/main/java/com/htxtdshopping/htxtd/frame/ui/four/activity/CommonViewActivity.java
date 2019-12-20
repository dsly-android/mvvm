package com.htxtdshopping.htxtd.frame.ui.four.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.dsly.common.base.BaseActivity;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.widget.IndicatorView;
import com.htxtdshopping.htxtd.frame.widget.ShadowDrawable;
import com.htxtdshopping.htxtd.frame.widget.SwitchButton;

import butterknife.BindView;
import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * @author chenzhipeng
 */
public class CommonViewActivity extends BaseActivity {

    @BindView(R.id.sb_test1)
    SwitchButton mSbTest1;
    @BindView(R.id.sb_test2)
    SwitchButton mSbTest2;
    @BindView(R.id.iv_indicator)
    IndicatorView mIvIndicator;
    @BindView(R.id.tv_shadow)
    TextView mTvShadow;

    @Override
    public int getLayoutId() {
        return R.layout.activity_common_view;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mSbTest1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSbTest2.toggle();
            }
        });
        mIvIndicator.setSelectedPosition(1);

        ShadowDrawable.setShadowDrawable(mTvShadow, Color.parseColor("#3D5AFE"),
                AutoSizeUtils.pt2px(this,16), Color.parseColor("#66000000"),
                AutoSizeUtils.pt2px(this,16), 0, 0);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }
}
