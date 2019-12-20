package com.android.dsly.common.widget;

import com.android.dsly.common.R;
import com.chad.library.adapter.base.loadmore.LoadMoreView;

/**
 *
 * @author 陈志鹏
 * @date 2018/10/12
 */
public final class CustomizeLoadMoreView extends LoadMoreView {

    @Override public int getLayoutId() {
        return R.layout.view_load_more;
    }

    @Override protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    /**
     * isLoadEndGone()为true，可以返回0
     * isLoadEndGone()为false，不能返回0
     */
    @Override protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}