package com.android.dsly.common.adapter;

import com.android.dsly.common.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author 陈志鹏
 * @date 2021/1/8
 */
public class CenterActionItemAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public CenterActionItemAdapter(List<String> datas) {
        super(R.layout.item_center_action_item, datas);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, @Nullable String s) {
        holder.setText(R.id.tv_content, s);
    }
}
