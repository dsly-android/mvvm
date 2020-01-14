package com.android.dsly.materialdesign;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author 陈志鹏
 * @date 2020-01-06
 */
public class LinkageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public LinkageAdapter() {
        super(R.layout.design_item_linkage);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, @Nullable String s) {
        holder.setText(R.id.tv_msg, s);
    }
}
