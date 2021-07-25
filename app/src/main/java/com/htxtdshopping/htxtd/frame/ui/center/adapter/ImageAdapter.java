package com.htxtdshopping.htxtd.frame.ui.center.adapter;

import com.android.dsly.common.utils.GlideUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.htxtdshopping.htxtd.frame.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author 陈志鹏
 * @date 2021/7/25
 */
public class ImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public ImageAdapter() {
        super(R.layout.item_image);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, @Nullable String s) {
        GlideUtils.loadImage(getContext(), s, holder.getView(R.id.iv_img));
    }
}
