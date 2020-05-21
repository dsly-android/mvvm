package com.android.dsly.common.network;

import com.android.dsly.rxhttp.model.RxHttpResponse;

import androidx.lifecycle.Observer;
import retrofit2.Response;

/**
 * @author 陈志鹏
 * @date 2020/5/21
 */
public abstract class NetObserver<T> implements Observer<T> {

    public abstract void onSuccess(T t);

    public abstract void onError(int code, String msg);

    @Override
    public void onChanged(T t) {
        Object baseResponse = null;
        if (t instanceof Response) {
            baseResponse = ((Response) t).body();
        } else if (t instanceof RxHttpResponse) {
            baseResponse = ((RxHttpResponse) t).body();
        } else {
            baseResponse = t;
        }
        if (baseResponse instanceof BaseResponse) {
            if (((BaseResponse) baseResponse).getCode() == BaseResponse.SUCCESS) {
                onSuccess(t);
            } else {
                onError(((BaseResponse) baseResponse).getCode(), ((BaseResponse) baseResponse).getMsg());
            }
        } else {
            onSuccess(t);
        }
    }
}
