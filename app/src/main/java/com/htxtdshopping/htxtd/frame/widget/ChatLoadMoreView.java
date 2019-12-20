package com.htxtdshopping.htxtd.frame.widget;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.htxtdshopping.htxtd.frame.R;

/**
 * @author 陈志鹏
 * @date 2019-12-04
 */
public class ChatLoadMoreView extends LoadMoreView {

    @Override
    public int getLayoutId() {
        return R.layout.view_chat_load_more;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}
