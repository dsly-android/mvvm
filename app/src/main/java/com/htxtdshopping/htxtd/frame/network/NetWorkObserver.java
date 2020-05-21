package com.htxtdshopping.htxtd.frame.network;

import androidx.lifecycle.Observer;

/**
 * @author 陈志鹏
 * @date 2020/5/19
 */
public abstract class NetWorkObserver<T> implements Observer<T> {

    @Override
    public void onChanged(T t) {
        if (t == null) {
            onError();
        } else {
            onSuccess(t);
        }
    }

    /**
     * 成功
     *
     * @param t
     */
    public abstract void onSuccess(T t);

    /**
     * 失败
     */
    public void onError() {
    }
}
