package com.htxtdshopping.htxtd.frame.ui.first.view;

import com.android.dsly.rxhttp.IView;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * @author 陈志鹏
 * @date 2019/1/17
 */
public interface IRefreshAndLoadMoreView extends IView {
    void loadDataSuccess(List<MultiItemEntity> datas, boolean isRefresh);

    void loadDataFail();
}
