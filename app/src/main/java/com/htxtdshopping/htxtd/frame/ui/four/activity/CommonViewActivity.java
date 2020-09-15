package com.htxtdshopping.htxtd.frame.ui.four.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.common.utils.ToastUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityCommonViewBinding;
import com.htxtdshopping.htxtd.frame.widget.ShadowDrawable;
import com.sunfusheng.marqueeview.MarqueeView;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * @author chenzhipeng
 */
public class CommonViewActivity extends BaseActivity<ActivityCommonViewBinding, BaseViewModel> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_common_view;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mBinding.sbTest1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mBinding.sbTest2.toggle();
            }
        });
        mBinding.ivIndicator.setSelectedPosition(1);

        ShadowDrawable.setShadowDrawable(mBinding.tvShadow, Color.parseColor("#3D5AFE"),
                AutoSizeUtils.dp2px(this,8), Color.parseColor("#66000000"),
                AutoSizeUtils.dp2px(this,8), 0, 0);
    }

    @Override
    public void initEvent() {
        mBinding.mvMarquee.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                ToastUtils.showShort(textView.getText().toString());
            }
        });
    }

    @Override
    public void initData() {
        List<String> messages = new ArrayList<>();
        messages.add("1. 大家好，我是孙福生。");
        messages.add("2. 欢迎大家关注我哦！");
        messages.add("3. GitHub帐号：sunfusheng");
        messages.add("4. 新浪微博：孙福生微博");
        messages.add("5. 个人博客：sunfusheng.com");
        messages.add("6. 微信公众号：孙福生");
        mBinding.mvMarquee.startWithList(messages);
    }
}
