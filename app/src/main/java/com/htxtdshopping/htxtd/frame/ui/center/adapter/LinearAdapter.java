package com.htxtdshopping.htxtd.frame.ui.center.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.htxtdshopping.htxtd.frame.R;

import androidx.annotation.NonNull;

/**
 * @author 陈志鹏
 * @date 2019-12-05
 */
public class LinearAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public LinearAdapter() {
        super(R.layout.item_linear);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_content,item);
    }
}
