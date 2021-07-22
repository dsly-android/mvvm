package com.htxtdshopping.htxtd.frame.ui.viewmodel;

import android.app.Application;

import com.android.dsly.common.base.BaseViewModel;

import androidx.annotation.NonNull;

/**
 * @author 陈志鹏
 * @date 2021/7/15
 */
public class HttpViewModel extends BaseViewModel {

    public HttpViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * test
     */
//    private MutableLiveData<BaseResponse> mDataLiveData = new MutableLiveData<>();
//
//    public void test(Long reqId, String msg, String status) {
//        ServerApi.test(reqId, msg, status, getLifecycleProvider(), mDataLiveData);
//    }
//
//    public MutableLiveData<BaseResponse> getDataLiveData() {
//        return mDataLiveData;
//    }
}