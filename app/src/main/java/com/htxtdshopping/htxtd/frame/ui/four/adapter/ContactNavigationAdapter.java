package com.htxtdshopping.htxtd.frame.ui.four.adapter;

import com.android.dsly.common.decoration.sticky.FullSpanUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.bean.ContactBean;
import com.htxtdshopping.htxtd.frame.widget.WaveSideBarView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author 陈志鹏
 * @date 2019/1/11
 */
public class ContactNavigationAdapter extends BaseMultiItemQuickAdapter<WaveSideBarView.IDisplayName, BaseViewHolder> {

    public static final int CONTACT = 0;

    public ContactNavigationAdapter() {
        super(new ArrayList<>());
        addItemType(CONTACT, R.layout.item_wave_contact);
        addItemType(WaveSideBarView.ITEM_LETTER, R.layout.item_pinned_header);
    }

    @Override
    protected void convert(BaseViewHolder helper, WaveSideBarView.IDisplayName item) {
        int itemViewType = helper.getItemViewType();
        if (itemViewType == CONTACT) {
            ContactBean contactBean = (ContactBean) item;
            helper.setText(R.id.tv_contact_name, contactBean.getName());
        } else {
            helper.setText(R.id.tv_tip, item.getDisplayName());
        }
    }

    /**
     * 需要这两个方法
     *
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        FullSpanUtil.onAttachedToRecyclerView(recyclerView, this, WaveSideBarView.ITEM_LETTER);
    }

    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        FullSpanUtil.onViewAttachedToWindow(holder, this, WaveSideBarView.ITEM_LETTER);
    }
}
