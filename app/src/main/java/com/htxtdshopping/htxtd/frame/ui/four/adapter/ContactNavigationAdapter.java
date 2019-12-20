package com.htxtdshopping.htxtd.frame.ui.four.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.bean.ContactBean;

import java.util.ArrayList;

/**
 * @author 陈志鹏
 * @date 2019/1/11
 */
public class ContactNavigationAdapter extends BaseMultiItemQuickAdapter<ContactBean, BaseViewHolder> {

    public static final int CONTACT = 0;
    public static final int HEADER = 1;

    public ContactNavigationAdapter() {
        super(new ArrayList<>());
        addItemType(CONTACT, R.layout.item_wave_contact);
        addItemType(HEADER, R.layout.item_pinned_header);
    }

    @Override
    protected void convert(BaseViewHolder helper, ContactBean item) {
        int itemViewType = helper.getItemViewType();
        if (itemViewType == CONTACT) {
            helper.setText(R.id.tv_contact_name, item.getName());
        } else {
            helper.setText(R.id.tv_tip, item.getPys().substring(0, 1).toUpperCase());
        }
    }

    public int getLetterPosition(String letter){
        for (int i = 0 ; i < getData().size(); i++){
            if(getData().get(i).getType() ==1 && getData().get(i).getPys().equals(letter)){
                return i;
            }
        }
        return -1;
    }
}
