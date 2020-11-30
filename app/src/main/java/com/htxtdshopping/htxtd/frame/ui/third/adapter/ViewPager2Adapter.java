package com.htxtdshopping.htxtd.frame.ui.third.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.htxtdshopping.htxtd.frame.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author 陈志鹏
 * @date 2020/11/29
 */
public class ViewPager2Adapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public ViewPager2Adapter() {
        super(R.layout.item_viewpager2);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, @Nullable String s) {
        holder.setText(R.id.tv_content, s);
    }
}
