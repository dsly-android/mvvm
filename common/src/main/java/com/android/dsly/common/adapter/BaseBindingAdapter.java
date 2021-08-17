package com.android.dsly.common.adapter;

import com.chad.library.BR;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import androidx.databinding.DataBindingUtil;

/**
 * @author 陈志鹏
 * @date 2020/5/26
 */
public class BaseBindingAdapter<T, VM extends BaseViewHolder> extends BaseQuickAdapter<T, VM> {

    public BaseBindingAdapter(int layoutId) {
        super(layoutId);
    }

    @Override
    protected void convert(@NotNull VM vm, @Nullable T t) {
        vm.getBinding().setVariable(BR.entity, t);
    }

    @Override
    protected void onItemViewHolderCreated(@NotNull BaseViewHolder viewHolder, int viewType) {
        DataBindingUtil.bind(viewHolder.itemView);
    }
}
