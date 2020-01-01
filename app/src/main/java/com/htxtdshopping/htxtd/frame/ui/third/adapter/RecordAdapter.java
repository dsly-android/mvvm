package com.htxtdshopping.htxtd.frame.ui.third.adapter;

import com.blankj.utilcode.util.FileUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.htxtdshopping.htxtd.frame.R;

import java.io.File;

/**
 * @author 陈志鹏
 * @date 2018/11/16
 */
public class RecordAdapter extends BaseQuickAdapter<File, BaseViewHolder> {

    public RecordAdapter() {
        super(R.layout.item_record);
        addChildClickViewIds(R.id.iv_icon);
    }

    @Override
    protected void convert(BaseViewHolder helper, File item) {
        helper.setText(R.id.tv_file_name,item.getName()+"  "+FileUtils.getFileSize(item));
    }
}
