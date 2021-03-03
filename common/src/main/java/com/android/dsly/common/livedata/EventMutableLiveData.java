package com.android.dsly.common.livedata;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

/**
 * 用作事件传递的 {@link MutableLiveData}
 */
public class EventMutableLiveData<T> extends MutableLiveData<T> {
    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        LiveEventObserver.bind(owner,this, observer);
    }

    @Override
    public void postValue(T value) {
        LiveDataUtils.setValue(this, value);
    }
}
