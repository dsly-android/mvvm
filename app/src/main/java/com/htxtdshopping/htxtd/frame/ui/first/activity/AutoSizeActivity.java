package com.htxtdshopping.htxtd.frame.ui.first.activity;

import android.os.Bundle;
import android.widget.SeekBar;

import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityAutosizeBinding;

import me.jessyan.autosize.AutoSizeConfig;

public class AutoSizeActivity extends BaseFitsWindowActivity<ActivityAutosizeBinding, BaseViewModel> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_autosize;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initEvent() {
        mBinding.sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AutoSizeConfig.getInstance().setFontMagnification(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void initData() {
        mBinding.sbProgress.setProgress((int) AutoSizeConfig.getInstance().getFontMagnification());
    }
}
