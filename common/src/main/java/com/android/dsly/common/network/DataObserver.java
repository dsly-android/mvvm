package com.android.dsly.common.network;

/**
 * @author 陈志鹏
 * @date 2020/4/8
 */

import com.android.dsly.rxhttp.model.RxHttpResponse;
import com.android.dsly.rxhttp.observer.BaseObserver;

import androidx.lifecycle.MutableLiveData;
import retrofit2.HttpException;
import retrofit2.Response;

public class DataObserver<T> extends BaseObserver<T> {

    private MutableLiveData mLiveData;

    public DataObserver() {
    }

    public DataObserver(MutableLiveData liveData) {
        this.mLiveData = liveData;
    }

    /**
     * 成功回调
     *
     * @param t
     */
    protected void onSuccess(T t) {
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);

        BaseResponse response = new BaseResponse();

        String errorMsg = handleException(e);
        if (e instanceof HttpException) {
            response.setMsg(errorMsg);
            response.setCode(((HttpException) e).code());
        } else {
            response.setCode(0);
            response.setMsg(errorMsg);
        }

        if (mLiveData != null) {
            mLiveData.postValue(response);
        }
    }

    @Override
    public void onNext(T t) {
        Object baseResponse = null;
        if (t instanceof Response) {
            baseResponse = ((Response) t).body();
        } else if (t instanceof RxHttpResponse) {
            baseResponse = ((RxHttpResponse) t).body();
        } else {
            baseResponse = t;
        }
        if (baseResponse instanceof BaseResponse) {
            //可以根据需求对code统一处理
            if (((BaseResponse) baseResponse).getCode() == BaseResponse.SUCCESS) {
                onSuccess(t);
            } else {
                onError(((BaseResponse) baseResponse).getCode(), ((BaseResponse) baseResponse).getMsg());
            }
        } else {
            onSuccess(t);
        }

        if (mLiveData != null) {
            mLiveData.postValue(t);
        }
    }
}