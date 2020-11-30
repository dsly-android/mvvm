package com.htxtdshopping.htxtd.frame.ui.center.adapter;

import com.android.dsly.common.adapter.BaseBindingAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ItemSwipeBinding;
import com.htxtdshopping.htxtd.frame.widget.ViewBinderHelper;

import androidx.annotation.NonNull;

/**
 * @author 陈志鹏
 * @date 2019-12-05
 */
public class SwipeAdapter extends BaseBindingAdapter<String, BaseViewHolder> implements LoadMoreModule {

    private ViewBinderHelper mHelper;

    public SwipeAdapter(ViewBinderHelper helper) {
        super(R.layout.item_swipe);
        mHelper = helper;

        addChildClickViewIds(R.id.tv_delete, R.id.tv_collect, R.id.ll_content);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        super.convert(helper, item);
        ItemSwipeBinding binding = helper.getBinding();
        mHelper.bind(binding.srlSwipe, item);
    }
}