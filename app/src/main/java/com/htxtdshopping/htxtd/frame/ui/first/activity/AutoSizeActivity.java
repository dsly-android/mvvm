package com.htxtdshopping.htxtd.frame.ui.first.activity;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.htxtdshopping.htxtd.frame.R;

import butterknife.BindView;
import me.jessyan.autosize.AutoSizeConfig;

public class AutoSizeActivity extends BaseFitsWindowActivity {

    @BindView(R.id.sb_progress)
    SeekBar mSbProgress;
    @BindView(R.id.tv_text)
    TextView mTvText;

    @Override
    public int getLayoutId() {
        return R.layout.activity_autosize;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initEvent() {
        mSbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        mSbProgress.setProgress((int) AutoSizeConfig.getInstance().getFontMagnification());
    }
}
