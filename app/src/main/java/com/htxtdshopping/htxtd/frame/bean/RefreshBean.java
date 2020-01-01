package com.htxtdshopping.htxtd.frame.bean;

import com.android.dsly.common.network.BaseResponse;

/**
 * @author 陈志鹏
 * @date 2019-12-28
 */
public class RefreshBean<T> extends BaseResponse<T> {
    private boolean isRefresh;

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }
}
