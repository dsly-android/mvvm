package com.htxtdshopping.htxtd.frame.ui.third.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.htxtdshopping.htxtd.frame.R;

/**
 * @author 陈志鹏
 * @date 2018/11/2
 */
public class GridAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public GridAdapter() {
        super(R.layout.item_grid);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_content, item);
    }
}
