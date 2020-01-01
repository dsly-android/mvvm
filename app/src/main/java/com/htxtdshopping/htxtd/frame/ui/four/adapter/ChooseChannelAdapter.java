package com.htxtdshopping.htxtd.frame.ui.four.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.module.BaseDraggableModule;
import com.chad.library.adapter.base.module.DraggableModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.bean.ChannelBean;
import com.htxtdshopping.htxtd.frame.widget.NewsDraggableModule;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * @author 陈志鹏
 * @date 2019/1/16
 */
public class ChooseChannelAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> implements DraggableModule {

    //已选频道
    public static final int TYPE_SELECTED = 1;
    //未选频道
    public static final int TYPE_NOT_SELECTED = 2;
    //已选固定频道
    public static final int TYPE_SELECTED_FIXED = 4;
    //标题
    public static final int TYPE_SECTION = 3;

    public ChooseChannelAdapter() {
        super(new ArrayList<>());
        addItemType(TYPE_SECTION, R.layout.item_section);
        addItemType(TYPE_SELECTED, R.layout.item_select_channel);
        addItemType(TYPE_NOT_SELECTED, R.layout.item_not_select_channel);
        addItemType(TYPE_SELECTED_FIXED, R.layout.item_select_fixed_channel);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        int itemViewType = helper.getItemViewType();
        switch (itemViewType) {
            case TYPE_SELECTED:
                ChannelBean selectedBean = (ChannelBean) item;
                helper.setText(R.id.tv_select_channel_name, selectedBean.getChannelName());
                break;
            case TYPE_NOT_SELECTED:
                ChannelBean notSelectedBean = (ChannelBean) item;
                helper.setText(R.id.tv_not_select_channel_name, notSelectedBean.getChannelName());
                break;
            case TYPE_SELECTED_FIXED:
                ChannelBean selectedFixedBean = (ChannelBean) item;
                helper.setText(R.id.tv_select_fixed_channel_name, selectedFixedBean.getChannelName());
                break;
            default:
                break;
        }
    }

    @NotNull
    @Override
    public BaseDraggableModule addDraggableModule(@NotNull BaseQuickAdapter<?, ?> baseQuickAdapter) {
        return new NewsDraggableModule(this);
    }

    public int getItemTypeNum(int itemType) {
        int num = 0;
        for (int i = 0; i < getData().size(); i++) {
            if (getData().get(i).getItemType() == itemType) {
                num++;
            }
        }
        return num;
    }

    public boolean inRange(int position) {
        return position >= 0 && position < getData().size();
    }
}
