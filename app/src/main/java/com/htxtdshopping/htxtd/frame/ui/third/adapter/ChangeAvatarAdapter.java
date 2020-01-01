package com.htxtdshopping.htxtd.frame.ui.third.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.viewholder.ChangeAvatarViewHolder;

import androidx.annotation.NonNull;

/**
 * @author 陈志鹏
 * @date 2018/12/13
 */
public class ChangeAvatarAdapter extends BaseQuickAdapter<String, ChangeAvatarViewHolder> {

    public ChangeAvatarAdapter() {
        super(R.layout.item_change_avatar);
    }

    @Override
    protected void convert(@NonNull ChangeAvatarViewHolder helper, String item) {
        helper.convert(getContext(),item);
    }
}
