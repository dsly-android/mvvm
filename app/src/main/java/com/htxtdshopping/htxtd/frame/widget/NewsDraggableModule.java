package com.htxtdshopping.htxtd.frame.widget;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.BaseDraggableModule;

import org.jetbrains.annotations.NotNull;

import androidx.recyclerview.widget.RecyclerView;

import static com.htxtdshopping.htxtd.frame.ui.four.adapter.ChooseChannelAdapter.TYPE_SELECTED;

/**
 * @author 陈志鹏
 * @date 2019-12-30
 */
public class NewsDraggableModule extends BaseDraggableModule {

    public NewsDraggableModule(@NotNull BaseQuickAdapter<?, ?> baseQuickAdapter) {
        super(baseQuickAdapter);
    }

    @Override
    public void onItemDragMoving(@NotNull RecyclerView.ViewHolder source, @NotNull RecyclerView.ViewHolder target) {
        if (source.getItemViewType() == TYPE_SELECTED && target.getItemViewType() == TYPE_SELECTED) {
            super.onItemDragMoving(source, target);
        }
    }
}